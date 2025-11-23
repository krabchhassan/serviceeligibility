package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerService {

  private final TriggerDao triggerDao;

  @ContinueSpan(log = "getTriggers")
  public TriggerResponse getTriggers(
      int perPage, int page, String sortBy, String direction, TriggerRequest request) {
    return triggerDao.getTriggers(perPage, page, sortBy, direction, request);
  }

  @ContinueSpan(log = "createTrigger")
  public Trigger createTrigger(Trigger trigger) {
    return triggerDao.saveTrigger(trigger);
  }

  @ContinueSpan(log = "createTriggeredBenef")
  public TriggeredBeneficiary createTriggeredBenef(TriggeredBeneficiary triggeredBeneficiary) {
    return triggerDao.saveTriggeredBeneficiary(triggeredBeneficiary);
  }

  @ContinueSpan(log = "removeAll triggers")
  public void removeAll() {
    triggerDao.removeAll();
  }

  @ContinueSpan(log = "saveToMongoForDeleteContrat")
  public Trigger saveToMongoForDeleteContrat(Trigger trigger, List<TriggeredBeneficiary> benefs) {
    return saveToMongoTriggerAndBenefs(trigger, benefs, null, null, true, true);
  }

  // saveToMongo for newly created trigger (event consumer)
  @ContinueSpan(log = "saveToMongo Trigger + TriggeredBenefs (3 params)")
  public Trigger saveToMongoNewTrigger(
      Trigger trigger, List<TriggeredBeneficiary> benefs, Integer seuil) {
    Trigger savedTrigger = saveTriggerToMongo(trigger, benefs.size(), seuil);

    // If there is at least one beneficiary...
    if (!CollectionUtils.isEmpty(benefs)) {
      // For each beneficiary...
      for (TriggeredBeneficiary benef : benefs) {
        benef.setIdTrigger(savedTrigger.getId());
        triggerDao.saveTriggeredBeneficiary(benef);
      }

      savedTrigger.setNbBenef(benefs.size());
      savedTrigger.setNbBenefKO(0);

      savedTrigger = triggerDao.saveTrigger(savedTrigger);

      return savedTrigger;
    }

    return null;
  }

  // save to mongo trigger updated from recyclage
  @ContinueSpan(log = "saveToMongo Trigger + TriggeredBenefs (4 params)")
  public Trigger saveToMongoForRecycling(
      Trigger trigger, List<TriggeredBeneficiary> benefs, Integer seuil, boolean saveTrigger) {
    return saveToMongoTriggerAndBenefs(trigger, benefs, seuil, null, saveTrigger, false);
  }

  // save to mongo trigger from UI and Batch Renouv
  @ContinueSpan(log = "saveToMongo Trigger + TriggeredBenefs (5 params)")
  public Trigger saveToMongoTriggerUIandRenouv(
      Trigger trigger,
      List<TriggeredBeneficiary> benefs,
      Integer seuil,
      String dateDebutDroitTP,
      boolean saveTrigger) {
    return saveToMongoTriggerAndBenefs(
        trigger, benefs, seuil, dateDebutDroitTP, saveTrigger, false);
  }

  // common method to update trigger and triggeredBenef
  @ContinueSpan(log = "saveToMongo Trigger + TriggeredBenefs (6 params)")
  private Trigger saveToMongoTriggerAndBenefs(
      Trigger trigger,
      List<TriggeredBeneficiary> benefs,
      Integer seuil,
      String dateDebutDroitTP,
      boolean saveTrigger,
      boolean deleteContrat) {
    boolean missingParamCarteTp = false;

    log.debug("saveToMongo trigger id {} number of triggers : {}", trigger.getId(), benefs.size());
    // pas de parametrageCarteTPId sur le delete du contrat (sans effet)
    if (!deleteContrat
        && StringUtils.isBlank(trigger.getId())
        && benefs.stream()
            .anyMatch(
                benef ->
                    StringUtils.isBlank(benef.getParametrageCarteTPId())
                        && benef.getNewContract() != null)) {
      missingParamCarteTp = true;
    }

    Trigger savedTrigger = trigger;
    if (saveTrigger) {
      if (missingParamCarteTp) {
        return saveToMongoSpecifiqueMissingParamCarteTP(trigger, benefs);
      }
      savedTrigger = saveTriggerToMongo(trigger, benefs.size(), seuil);
    }

    boolean isBatchRenouv = trigger.getOrigine().equals(TriggerEmitter.Renewal) && !saveTrigger;
    boolean isRenewal = TriggerEmitter.Renewal.equals(trigger.getOrigine());
    int nbBenefKO = trigger.getNbBenefKO();
    int nbBenef = isBatchRenouv ? trigger.getNbBenef() : 0;
    int currentYear = DateUtils.getYearFromDate(LocalDate.now());
    LocalDate dateTraitement =
        (StringUtils.isNotBlank(dateDebutDroitTP)
            ? DateUtils.stringToDate(dateDebutDroitTP)
            : LocalDate.of(currentYear, 1, 1));

    // process beneficiaries...
    nbBenef =
        processBenefOKandKO(benefs, dateTraitement, nbBenef, savedTrigger, nbBenefKO, isRenewal);

    if (nbBenef > 0) {
      savedTrigger.setNbBenef(nbBenef);
      if (saveTrigger) {
        savedTrigger = triggerDao.saveTrigger(savedTrigger);
      }
      return savedTrigger;
    } else if (!isRenewal) {
      triggerDao.deleteTriggerById(trigger.getId());
    }

    return null;
  }

  // check benefs status, update KO number in trigger and its status, save triggerBenef and return
  // number of OK benef
  private int processBenefOKandKO(
      List<TriggeredBeneficiary> benefs,
      LocalDate dateTraitement,
      int nbBenef,
      Trigger savedTrigger,
      int nbBenefKO,
      boolean isRenewal) {
    for (TriggeredBeneficiary benef : benefs) {
      // Récupération de la plus petite des dates limitantes entre dateRadiation,
      // dateResiliation, et le max des dates de fin de garanties
      LocalDate minDatesLimitantes = calculMinDatesLimitantes(benef.getNewContract());
      // Si cette date est nulle ou si elle est supérieure ou égale a l'année en
      // cours, on n'est pas limité dans la création du triggeredBenef
      boolean limitCoverCurrentYear =
          minDatesLimitantes == null || !minDatesLimitantes.isBefore(dateTraitement);

      // Cas d'un nouveau bénéf limité par les dates citées plus haut
      if (benef.getOldContract() == null
          && benef.getNewContract() != null
          && !limitCoverCurrentYear) {
        log.debug(
            "Aucun triggeredBeneficiary généré pour le bénéficiaire avec le numéro personne {}",
            benef.getNumeroPersonne());
        continue;
      }
      // Cas normal
      nbBenef++;
      benef.setIdTrigger(savedTrigger.getId());
      // Initialisation de la liste des statuts du benef
      if (TriggeredBeneficiaryStatusEnum.Error.equals(benef.getStatut())) {
        nbBenefKO++;
        savedTrigger.setStatus(TriggerStatus.ProcessedWithErrors);
        savedTrigger.setNbBenefKO(nbBenefKO);
      } else if (!isRenewal) {
        TriggerUtils.addStatus(benef, TriggeredBeneficiaryStatusEnum.Processed, null, true);
      }

      triggerDao.saveTriggeredBeneficiary(benef);
    }
    return nbBenef;
  }

  public Trigger saveToMongoSpecifiqueMissingParamCarteTP(
      Trigger trigger, List<TriggeredBeneficiary> benefs) {
    trigger.setStatus(TriggerStatus.ProcessedWithErrors);
    Trigger savedTrigger = triggerDao.saveTrigger(trigger);

    int nbBenefKO = getNbBenefKOSpecifiqueMissingParamCarteTP(benefs, savedTrigger);
    savedTrigger.setNbBenef(nbBenefKO);
    savedTrigger.setNbBenefKO(nbBenefKO);
    savedTrigger = triggerDao.saveTrigger(savedTrigger);
    return savedTrigger;
  }

  private int getNbBenefKOSpecifiqueMissingParamCarteTP(
      List<TriggeredBeneficiary> benefs, Trigger savedTrigger) {
    int nbBenefKO = 0;
    Anomaly anomaly = Anomaly.NO_CARD_RIGHT_PARAM;
    TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
        TriggeredBeneficiaryAnomaly.create(anomaly);

    Anomaly anomaly2 = Anomaly.ISSUE_BLOCKING_OTHER_BENEF_IN_TRIGGER;
    TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly2 =
        TriggeredBeneficiaryAnomaly.create(anomaly2);

    // For each beneficiary...
    for (TriggeredBeneficiary benef : benefs) {
      nbBenefKO++;
      benef.setIdTrigger(savedTrigger.getId());

      if (StringUtils.isNotBlank(benef.getParametrageCarteTPId())) {
        if (!TriggeredBeneficiaryStatusEnum.Error.equals(benef.getStatut())) {
          TriggerUtils.addStatus(
              benef, TriggeredBeneficiaryStatusEnum.Error, triggeredBeneficiaryAnomaly2, true);
        }
      } else if (benef.getDerniereAnomalie() != null
          && !Anomaly.BOBB_NO_PRODUCT_FOUND.equals(benef.getDerniereAnomalie().getAnomaly())
          && !Anomaly.NO_CARD_RIGHT_PERIODS.equals(benef.getDerniereAnomalie().getAnomaly())) {
        TriggerUtils.addStatus(
            benef, TriggeredBeneficiaryStatusEnum.Error, triggeredBeneficiaryAnomaly, true);
      }
      triggerDao.saveTriggeredBeneficiary(benef);
    }

    return nbBenefKO;
  }

  private LocalDate calculMinDatesLimitantes(ServicePrestationTriggerBenef contract) {
    if (contract != null) {
      LocalDate maxDateFinGaranties = calculMaxDateFinGaranties(contract);
      LocalDate dateRadiation = DateUtils.stringToDate(contract.getDateRadiation());
      LocalDate dateResiliation = DateUtils.stringToDate(contract.getDateResiliation());

      return DateUtils.getMinDate(maxDateFinGaranties, dateRadiation, dateResiliation);
    }
    return null;
  }

  private LocalDate calculMaxDateFinGaranties(ServicePrestationTriggerBenef contract) {
    if (contract == null || CollectionUtils.isEmpty(contract.getDroitsGaranties())) {
      return null;
    }

    String maxDateFin = null;

    for (DroitAssure droitGarantie : contract.getDroitsGaranties()) {
      if (droitGarantie.getPeriode().getFin() == null) {
        return null;
      }
      maxDateFin =
          DateUtils.getMaxDate(
              maxDateFin, droitGarantie.getPeriode().getFin(), DateUtils.FORMATTER);
    }

    return DateUtils.stringToDate(maxDateFin);
  }

  @ContinueSpan(log = "saveTriggerToMongo")
  public Trigger saveTriggerToMongo(Trigger trigger, Integer nbBenefs, Integer seuil) {
    log.debug(
        "Sauvegarde du déclencheur : Origine={}, seuil={}, nbBenefs={}",
        trigger.getOrigine(),
        seuil,
        nbBenefs);
    if (trigger.getOrigine().equals(TriggerEmitter.Renewal)) {
      trigger.setStatus(
          seuil != null && nbBenefs >= seuil ? TriggerStatus.StandBy : TriggerStatus.ToProcess);
    } else {
      trigger.setStatus(TriggerStatus.ToProcess);
    }

    return triggerDao.saveTrigger(trigger);
  }

  @ContinueSpan(log = "getTriggeredBeneficiaries")
  public List<TriggeredBeneficiary> getTriggeredBeneficiaries(String idTrigger) {
    return triggerDao.getTriggeredBeneficiaries(idTrigger);
  }

  @ContinueSpan(log = "getTriggeredBeneficiariesStream")
  public Iterator<TriggeredBeneficiary> getTriggeredBeneficiariesStream(String idTrigger) {
    return triggerDao.getTriggeredBeneficiariesStream(idTrigger);
  }

  @ContinueSpan(log = "getTriggeredBeneficiarieIdsWithStatus")
  public List<String> getTriggeredBeneficiarieIdsWithStatus(
      String idTrigger, TriggeredBeneficiaryStatusEnum triggeredBeneficiaryStatusEnum) {
    return triggerDao.getTriggeredBeneficiarieIdsWithStatus(
        idTrigger, triggeredBeneficiaryStatusEnum);
  }

  @ContinueSpan(log = "getTriggeredBeneficiariesByServicePrestation")
  public List<TriggeredBeneficiary> getTriggeredBeneficiariesByServicePrestation(
      String idServicePrestation) {
    return triggerDao.getTriggeredBeneficiariesByServicePrestation(idServicePrestation);
  }

  @ContinueSpan(log = "getTriggeredBeneficiariesByServicePrestation")
  public TriggeredBeneficiary getLastTriggeredBeneficiariesByServicePrestation(
      String idServicePrestation, String idTriggerBenef) {
    return triggerDao.getLastTriggeredBeneficiariesByServicePrestation(
        idServicePrestation, idTriggerBenef);
  }

  @ContinueSpan(log = "getTriggeredBeneficiariesWithError")
  public TriggeredBeneficiaryResponse getTriggeredBeneficiariesWithError(
      int perPage, int page, String idTrigger, String motifAnomalieSortDirection) {
    return triggerDao.getTriggeredBeneficiariesWithError(
        perPage, page, idTrigger, motifAnomalieSortDirection);
  }

  @ContinueSpan(log = "getNbTriggeredBeneficiaries")
  public long getNbTriggeredBeneficiaries(String idTrigger) {
    return triggerDao.getNbTriggeredBeneficiaries(idTrigger);
  }

  @ContinueSpan(log = "getNbTriggeredBeneficiariesToProcess")
  public long getNbTriggeredBeneficiariesWithStatus(
      String idTrigger, TriggeredBeneficiaryStatusEnum triggeredBeneficiaryStatusEnum) {
    return triggerDao.getNbTriggeredBeneficiariesWithStatus(
        idTrigger, triggeredBeneficiaryStatusEnum);
  }

  @ContinueSpan(log = "updateTriggeredBeneficiaries")
  public void updateTriggeredBeneficiaries(List<TriggeredBeneficiary> benefs) {
    triggerDao.updateTriggeredBeneficiaries(benefs);
  }

  @ContinueSpan(log = "getTriggeredBenefById")
  public TriggeredBeneficiary getTriggeredBenefById(String id) {
    return triggerDao.getTriggeredBenefById(id);
  }

  @ContinueSpan(log = "manageBenefCounter")
  public int manageBenefCounter(
      String triggerId, int nbBenefError, int nbBenefWarning, int nbBenefToProcess) {
    return triggerDao.manageBenefCounter(triggerId, nbBenefError, nbBenefWarning, nbBenefToProcess);
  }

  @ContinueSpan(log = "getTriggerById")
  public Trigger getTriggerById(String id) {
    return triggerDao.getTriggerById(id);
  }

  @ContinueSpan(log = "deleteTriggerByAmc")
  public long deleteTriggerByAmc(final String idDeclarant) {
    return triggerDao.deleteTriggerByAmc(idDeclarant);
  }

  @ContinueSpan(log = "deleteTriggeredBeneficiaryByAmc")
  public long deleteTriggeredBeneficiaryByAmc(final String idDeclarant) {
    return triggerDao.deleteTriggeredBeneficiaryByAmc(idDeclarant);
  }

  @ContinueSpan(log = "deleteTriggerById")
  public long deleteTriggerById(final String id) {
    return triggerDao.deleteTriggerById(id);
  }

  @ContinueSpan(log = "deleteTriggeredBeneficiaryById")
  public long deleteTriggeredBeneficiaryById(final String id) {
    return triggerDao.deleteTriggeredBeneficiaryById(id);
  }

  @ContinueSpan(log = "saveTrigger")
  public Trigger saveTrigger(Trigger trigger) {
    return triggerDao.saveTrigger(trigger);
  }

  @ContinueSpan(log = "updateOnlyStatus Trigger")
  public void updateOnlyStatus(String id, TriggerStatus status, String source) {
    triggerDao.updateOnlyStatus(id, status, source);
  }

  @ContinueSpan(log = "updateTriggerToProcessing")
  public void updateTriggerToProcessing(String idTrigger) {
    Trigger trigger = getTriggerById(idTrigger);
    trigger.setDateDebutTraitement(new Date());
    trigger.setStatus(TriggerStatus.Processing);
    saveTrigger(trigger);
  }

  public Trigger updateStatutTrigger(
      ManageBenefsContract manageBenefsContract,
      Trigger trigger,
      boolean isError,
      Long randomRecyclingId) {
    trigger.setNbBenefToProcess(trigger.getNbBenefToProcess());

    if (randomRecyclingId != 0L) {
      updateRecyclagePeriodeFin(trigger, randomRecyclingId);
    } else {
      trigger.setDateFinTraitement(new Date());
    }

    // trigger worker : on se base sur manageBenefsContract car l'on a qu'un contrat
    // dans le trigger
    if (isError || manageBenefsContract.getNbBenefKO() > 0) {
      trigger.setStatus(TriggerStatus.ProcessedWithErrors);
    } else if (manageBenefsContract.isWarningBenef()) {
      trigger.setStatus(TriggerStatus.ProcessedWithWarnings);
    } else {
      trigger.setStatus(TriggerStatus.Processed);
    }
    trigger.setNbBenefKO(isError ? manageBenefsContract.getNbBenefKO() : 0);
    trigger.setNbBenefWarning(manageBenefsContract.getNbBenefWarning());
    trigger.setDateModification(LocalDateTime.now(ZoneOffset.UTC));
    return saveTrigger(trigger);
  }

  public void updateRecyclagePeriodeFin(Trigger trigger, Long randomRecyclingId) {
    String date = DateUtils.generateDate();
    Optional<RecyclingPeriods> periode =
        trigger.getPeriodes().stream()
            .filter(recyclingPeriods -> recyclingPeriods.getId().equals(randomRecyclingId))
            .findFirst();
    if (periode.isPresent()) {
      periode.get().getPeriode().setFin(date);
    } else {
      RecyclingPeriods recyclingPeriods = new RecyclingPeriods();
      recyclingPeriods.setId(randomRecyclingId);
      recyclingPeriods.setPeriode(new Periode(date, date));
      trigger.getPeriodes().add(recyclingPeriods);
    }
  }

  public Iterator<Trigger> getIDsTriggerRenewNotExported() {
    return triggerDao.getIDsTriggerRenewNotArchived();
  }

  public void setExported(String id, boolean exported) {
    triggerDao.setExported(id, exported);
  }

  public int getNombreServicePrestationByTriggerId(String idTrigger, boolean recyclage) {
    return triggerDao.getNombreServicePrestationByTriggerId(idTrigger, recyclage);
  }
}
