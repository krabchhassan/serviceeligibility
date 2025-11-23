package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public abstract class TriggerProcessingService {

  private final Logger logger = LoggerFactory.getLogger(TriggerProcessingService.class);

  protected final TriggerBuildDeclarationNewService triggerBuildDeclarationService;

  protected final TriggerCreationService triggerCreationService;

  protected final TriggerService triggerService;

  protected final SasContratService sasContratService;

  protected final ServicePrestationDao servicePrestationDao;

  protected final Producer producer;

  protected final TriggerRecyclageService triggerRecyclageService;

  protected TriggerProcessingService(
      TriggerBuildDeclarationNewService triggerBuildDeclarationService,
      TriggerCreationService triggerCreationService,
      TriggerService triggerService,
      SasContratService sasContratService,
      ServicePrestationDao servicePrestationDao,
      Producer producer,
      TriggerRecyclageService triggerRecyclageService) {
    this.triggerBuildDeclarationService = triggerBuildDeclarationService;
    this.triggerCreationService = triggerCreationService;
    this.triggerService = triggerService;
    this.sasContratService = sasContratService;
    this.servicePrestationDao = servicePrestationDao;
    this.producer = producer;
    this.triggerRecyclageService = triggerRecyclageService;
  }

  public abstract ResponseTriggerProcessing processTrigger(
      RequestTriggerProcessing requestTriggerProcessing);

  // used by
  // Trigger renouvellement
  // Pre conditions, all triggerBenefs need to be in status ToProcess and not
  // ERROR -> recycling needs to fix this before calling this method
  @ContinueSpan(log = "manageMessageContrat from TriggerRenouvellementWorker")
  protected boolean manageMessageContrat(
      String idTrigger,
      SasContrat sasContratRecyclage,
      boolean isRenouvellement,
      List<TriggeredBeneficiary> benefs,
      String updateTrigger,
      Long randomRecyclingId,
      ClientSession session) {
    boolean isError = false;
    if (StringUtils.isNotBlank(idTrigger)) {
      logger.debug("recherche du trigger {} et de ses benefs", idTrigger);

      if (CollectionUtils.isNotEmpty(benefs)) {

        ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
        manageBenefsContract.setBenefs(benefs);
        manageBenefsContract.setSasCree(false);
        manageBenefsContract.setSasContrat(null);
        // rempli seulement depuis manageRecycle : utile dans TriggerBuildDeclaration
        // (handleBuildDeclarationError) pour ne pas recréer un sas identique
        manageBenefsContract.setSasContratRecyclage(sasContratRecyclage);
        manageBenefsContract.setDeclarations(new LinkedList<>());
        manageBenefsContract.setErreurBenef(false);
        manageBenefsContract.setWarningBenef(false);

        Trigger trigger = triggerService.getTriggerById(idTrigger);

        triggerBuildDeclarationService.manageBenefs(
            trigger,
            manageBenefsContract,
            !TriggerEmitter.Renewal.equals(trigger.getOrigine()),
            session);

        isError = manageBenefsContract.isErreurBenef();
        processAccordingToError(isRenouvellement, isError, manageBenefsContract);
        // Modification des benefs en base
        logger.debug("Modification du beneficiaire de déclencheur en base");
        triggerService.updateTriggeredBeneficiaries(manageBenefsContract.getBenefs());
        // Modification du déclencheur
        logger.debug("Modification du déclencheur en base, renouvellement {}", isRenouvellement);
        logger.debug("updateTrigger {}", updateTrigger);
        logger.debug("randomRecyclingId {}", randomRecyclingId);
        if (!isRenouvellement) {
          updateTriggerOnEvent(
              sasContratRecyclage, randomRecyclingId, trigger, manageBenefsContract, isError);
        }
      }
    } else {
      logger.error("Le trigger {} n'existe pas ou ne référence aucun bénéficiaire", idTrigger);
    }
    return isError;
  }

  private void updateTriggerOnEvent(
      SasContrat sasContratRecyclage,
      Long randomRecyclingId,
      Trigger trigger,
      ManageBenefsContract manageBenefsContract,
      boolean isError) {
    // sur le renouvellement, la modification du statut se fait au niveau du
    // triggerrenouvellementworker.
    trigger =
        triggerService.updateStatutTrigger(
            manageBenefsContract, trigger, isError, randomRecyclingId);

    if (sasContratRecyclage == null) {
      triggerRecyclageService.launchFinishedEvents(
          trigger, triggerService.getTriggeredBeneficiaries(trigger.getId()));
    } else {
      triggerRecyclageService.launchFinishedEventsForRecycling(trigger, sasContratRecyclage);
    }
  }

  protected void processAccordingToError(
      boolean isRenouvellement, boolean isError, ManageBenefsContract manageBenefsContract) {
    if (isError) {
      if (!isRenouvellement) {
        manageBenefsContract.setNbBenefWarning(0);
        manageBenefsContract.setNbBenefKO(manageBenefsContract.getBenefs().size());
      }
      manageSasContractOnError(manageBenefsContract);
    } else {
      triggerRecyclageService.sendEventsDeclarationAndDeleteSasContract(manageBenefsContract);
    }
  }

  private void manageSasContractOnError(ManageBenefsContract manageBenefsContract) {
    // Gestion du sasContrat
    logger.debug("Création du SasContrat en base");
    final SasContrat sas = manageBenefsContract.getSasContrat();
    Anomaly anomaly =
        sas != null ? Anomaly.SAS_FOUND_FOR_THIS_CONTRACT : Anomaly.ERROR_FOUND_FOR_THIS_CONTRACT;

    List<TriggeredBeneficiary> benefsToChange = manageBenefsContract.getBenefs();
    benefsToChange.forEach(
        beneficiary -> {
          if (!TriggeredBeneficiaryStatusEnum.Error.equals(beneficiary.getStatut())) {
            TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
                TriggeredBeneficiaryAnomaly.create(anomaly);
            TriggerUtils.addStatus(
                beneficiary,
                TriggeredBeneficiaryStatusEnum.Error,
                triggeredBeneficiaryAnomaly,
                true);
            sasContratService.manageSasContrat(
                sas, beneficiary, triggeredBeneficiaryAnomaly.getDescription());
          }
        });

    if (sas != null) {
      sas.setRecycling(false);
      sasContratService.save(sas);
    }
  }

  /**
   * traite tous les triggers dans le sas, le recyclage pour un même contrat peut mettre à jour
   * d'autres triggers il ne faut pas mettre à jour le trigger de renouvellement à processed.
   */
  @ContinueSpan(log = "manageRecycle")
  private boolean manageRecycle(
      String idTrigger,
      SasContrat sasContrat,
      List<String> benefIds,
      Long randomRecyclingId,
      String updateTrigger,
      ClientSession clientSession) {
    ContratAIV6 contrat;
    // la mise à jour au statut processing se fait dans le triggerRenouvellement :
    // processMessageListener.
    Trigger trigger = getTriggerAndUpdateItToProcessing(idTrigger);
    if (randomRecyclingId.equals(0L)
        && Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE.equals(updateTrigger)) {
      // on arrive depuis le triggerworker et on traite un trigger de renouvellement
      // qui se trouve dans le même sas
      updateTrigger = Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER_INPROGRESS;
      RecyclingPeriods recyclingPeriods = new RecyclingPeriods();
      randomRecyclingId = RandomUtils.nextLong();
      recyclingPeriods.setId(randomRecyclingId);
      Periode periode = new Periode();
      periode.setDebut(DateUtils.generateDate());
      recyclingPeriods.setPeriode(periode);
      trigger.getPeriodes().add(recyclingPeriods);
      triggerService.saveTrigger(trigger);
    }
    List<TriggeredBeneficiary> triggeredBenefs = new ArrayList<>();

    String servicePrestationId = sasContrat.getServicePrestationId();
    boolean errorContract = false;
    // Get contract
    contrat = servicePrestationDao.getContratById(servicePrestationId);

    // If the contract is found...
    boolean sansEffet = false;
    if (contrat != null) {
      // We get TriggeredBeneficiaries from database with SasContrat
      for (String triggeredBenefId : benefIds) {
        sansEffet =
            computeTriggerBenefOnRecycling(
                triggerService.getTriggeredBenefById(triggeredBenefId), sansEffet, triggeredBenefs);
      }
      // Save toProcess status in all triggerBenef
      triggerService.updateTriggeredBeneficiaries(triggeredBenefs);

      trigger = updateRecyclingTrigger(trigger, triggeredBenefs, errorContract);

      errorContract =
          manageMessageContrat(
              trigger.getId(),
              sasContrat,
              TriggerEmitter.Renewal.equals(trigger.getOrigine()),
              triggeredBenefs,
              updateTrigger,
              randomRecyclingId,
              clientSession);
    }
    return errorContract;
  }

  private static boolean computeTriggerBenefOnRecycling(
      TriggeredBeneficiary triggerBenef,
      boolean sansEffet,
      List<TriggeredBeneficiary> triggeredBenefs) {
    if (triggerBenef != null) {
      if (triggerBenef.getNewContract() == null) {
        sansEffet = true;
      }
      TriggerUtils.addStatus(
          triggerBenef,
          TriggeredBeneficiaryStatusEnum.ToProcess,
          TriggeredBeneficiaryAnomaly.create(Anomaly.ERROR_RETRY_RECYCLING),
          true);
      triggeredBenefs.add(triggerBenef);
    }
    return sansEffet;
  }

  private Trigger updateRecyclingTrigger(
      Trigger trigger, List<TriggeredBeneficiary> triggeredBenefs, boolean errorContract) {
    if (TriggerEmitter.Renewal.equals(trigger.getOrigine())) {
      trigger = triggerService.saveToMongoForRecycling(trigger, triggeredBenefs, null, false);

    } else if (errorContract) {
      trigger = triggerService.saveToMongoForRecycling(trigger, triggeredBenefs, null, true);
      int retourmanageBenefCounter = 0;
      if (!triggeredBenefs.isEmpty()) {
        retourmanageBenefCounter =
            triggerService.manageBenefCounter(
                trigger.getId(), triggeredBenefs.size(), 0, -triggeredBenefs.size());
      }
      logger.debug("retour manageBenefCounter {}", retourmanageBenefCounter);
      triggerService.updateOnlyStatus(
          trigger.getId(), TriggerStatus.ProcessedWithErrors, Constants.ORIGINE_RECYCLAGE);
    }
    return trigger;
  }

  @ContinueSpan(log = "getTriggerAndUpdateItToProcessing")
  Trigger getTriggerAndUpdateItToProcessing(String idTrigger) {
    Trigger trigger = triggerService.getTriggerById(idTrigger);
    if (!TriggerEmitter.Renewal.equals(trigger.getOrigine())) {
      trigger.setStatus(TriggerStatus.Processing);
      triggerService.saveTrigger(trigger);
    }
    return trigger;
  }

  protected List<String> manageRecycleBySasContrat(
      SasContrat sasContrat,
      Long randomRecyclingId,
      String updateTrigger,
      ClientSession clientSession) {
    List<String> recycledTriggers = new ArrayList<>();
    List<TriggerBenefs> triggersBenefs = sasContrat.getTriggersBenefs();
    boolean error = false;
    if (CollectionUtils.isNotEmpty(triggersBenefs)) {

      for (TriggerBenefs triggerBenefs : triggersBenefs) {
        if (error) {
          Trigger trigger = triggerService.getTriggerById(triggerBenefs.getTriggerId());
          triggerService.updateRecyclagePeriodeFin(trigger, randomRecyclingId);
          trigger.setStatus(TriggerStatus.ProcessedWithErrors);
          triggerService.saveTrigger(trigger);
        }
        if (!error
            && manageRecycle(
                triggerBenefs.getTriggerId(),
                sasContrat,
                triggerBenefs.getBenefsInfos().stream().map(BenefInfos::getBenefId).toList(),
                randomRecyclingId,
                updateTrigger,
                clientSession)) {
          error = true;
        }
        if (!error) {
          recycledTriggers.add(triggerBenefs.getTriggerId());
        }
      }

      // En cas d'erreur on modifie tous les trigger ToProcess du SasContrat a leur
      // ancien status ProcessedWithErrors
      if (error) {
        updateTriggersRecycleFailed(triggersBenefs);
      }
    }
    return recycledTriggers;
  }

  /**
   * A partir d une liste de trigger en recyclage, remplace le status toProcess vers
   * ProcessedWithErrors et ajoute/complete une periode de recyclage sur le trigger
   */
  private void updateTriggersRecycleFailed(List<TriggerBenefs> triggersBenefs) {
    for (TriggerBenefs triggersBenef : triggersBenefs) {
      for (BenefInfos benefInfos : triggersBenef.getBenefsInfos()) {
        TriggeredBeneficiary triggeredBeneficiary =
            triggerService.getTriggeredBenefById(benefInfos.getBenefId());
        if (triggeredBeneficiary != null
            && TriggeredBeneficiaryStatusEnum.ToProcess.equals(triggeredBeneficiary.getStatut())) {
          TriggerUtils.addStatus(
              triggeredBeneficiary,
              TriggeredBeneficiaryStatusEnum.Error,
              TriggeredBeneficiaryAnomaly.errorRetryRecycling,
              false);
        }
      }
    }
  }

  private static void mapDeclarationToConsolidationDeclarationsContratTrigger(
      TriggerContract triggerContract,
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger) {
    consolidationDeclarationsContratTrigger.setIdDeclarant(triggerContract.getIdDeclarant());
    consolidationDeclarationsContratTrigger.setNumeroContrat(triggerContract.getNumeroContrat());
    consolidationDeclarationsContratTrigger.setNumeroAdherent(triggerContract.getNumeroAdherent());
  }

  public void sendToTopicContratTP(String idTrigger, TriggerContract triggerContract) {
    ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger =
        new ConsolidationDeclarationsContratTrigger();
    consolidationDeclarationsContratTrigger.setIdTrigger(idTrigger);
    String keyTopicConsoDeclaration = null;
    if (triggerContract != null) {
      mapDeclarationToConsolidationDeclarationsContratTrigger(
          triggerContract, consolidationDeclarationsContratTrigger);
      keyTopicConsoDeclaration =
          triggerContract.getIdDeclarant()
              + triggerContract.getNumeroContrat()
              + triggerContract.getNumeroAdherent();
    }
    // création de l'historique de contrat TP.
    // envoi les déclarations pour mises à jour du contrat
    producer.sendDeclarationsByContratAndTrigger(
        consolidationDeclarationsContratTrigger, keyTopicConsoDeclaration);
  }
}
