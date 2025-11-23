package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.beyond.schemas.*;
import com.cegedim.next.serviceeligibility.core.dao.RetentionDao;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.model.domain.AffiliationRO;
import com.cegedim.next.serviceeligibility.core.model.domain.AttachementRO;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionHistorique;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Nir;
import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.AssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetentionServiceImpl implements RetentionService {

  private final RetentionDao retentionDao;
  private final ServicePrestationDao servicePrestationDao;
  private final EventService eventService;

  @Override
  public String calculDateRetention(ContratAIV6 contract, String numeroPersonne) {
    ContratAIV6 defaultContract = new ContratAIV6();
    defaultContract.setAssures(new ArrayList<>());
    contract = Objects.requireNonNullElse(contract, defaultContract);
    final String dateResiliation = contract.getDateResiliation();

    List<String> datesToCompare = new ArrayList<>();
    if (StringUtils.isNotBlank(dateResiliation)) {
      datesToCompare.add(dateResiliation);
    }

    String maxDateRadiation = getMaxDateRadiation(contract, numeroPersonne);
    if (maxDateRadiation != null) {
      datesToCompare.add(maxDateRadiation);
    }

    return datesToCompare.stream().min(String::compareTo).orElse(null);
  }

  public String getMaxDateRadiation(ContratAIV6 contract, String numeroPersonne) {
    List<String> datesRadiation = new ArrayList<>();
    for (Assure assure : contract.getAssures()) {
      if (numeroPersonne.equals(assure.getIdentite().getNumeroPersonne())) {
        datesRadiation.add(assure.getDateRadiation());
      }
    }
    return DateUtils.getMaxDateOrNull(datesRadiation);
  }

  @Override
  public void updateOrCreateRetention(Retention retention) {

    long retentionExists = retentionDao.findAndLock(retention);
    if (retentionExists > 0) {
      retentionDao.updateRetention(retention, RetentionStatus.TO_PROCESS);
      eventService.sendObservabilityEventRetentionFinished(
          DelaiRetentionFinishedEventDto.Action.MODIFIE, retention);
    } else {
      retentionDao.createRetention(retention);
      eventService.sendObservabilityEventRetentionFinished(
          DelaiRetentionFinishedEventDto.Action.CREE, retention);
    }
  }

  private void cancelRetention(Retention retention) {
    long retentionExists = retentionDao.findAndLock(retention);
    if (retentionExists > 0) {
      retentionDao.updateRetention(retention, RetentionStatus.CANCELLED);
      eventService.sendObservabilityEventRetentionFinished(
          DelaiRetentionFinishedEventDto.Action.ANNULE, retention);
    }
  }

  @Override
  public List<Retention> getAll() {
    return retentionDao.getAll();
  }

  @Override
  public void updateStatus(Retention retention, RetentionStatus status) {
    retentionDao.updateRetentionStatus(retention, status);
    DelaiRetentionFinishedEventDto.Action action =
        switch (status) {
          case TO_PROCESS, LOCKED -> DelaiRetentionFinishedEventDto.Action.MODIFIE;
          case CANCELLED -> DelaiRetentionFinishedEventDto.Action.ANNULE;
          case PROCESSED -> DelaiRetentionFinishedEventDto.Action.TRAITE;
        };
    eventService.sendObservabilityEventRetentionFinished(action, retention);
  }

  public List<Retention> manageRetention(ContratAIV6 newContract, ContratAIV6 existingContract) {
    List<Retention> retentionList = new ArrayList<>();
    for (Assure assure : newContract.getAssures()) {
      manageRetentionAssure(newContract, existingContract, assure, retentionList);
    }
    if (CollectionUtils.isNotEmpty(retentionList)) {
      for (Retention retention : retentionList) {
        updateOrCreateRetention(retention);
      }
    }
    return retentionList;
  }

  public void manageRetentionAssure(
      ContratAIV6 newContract,
      ContratAIV6 existingContract,
      Assure assure,
      List<Retention> retentionList) {
    String idDeclarant = Objects.requireNonNullElse(newContract, existingContract).getIdDeclarant();
    String numeroAdherent =
        Objects.requireNonNullElse(newContract, existingContract).getNumeroAdherent();
    String numeroContrat = Objects.requireNonNullElse(newContract, existingContract).getNumero();

    String numeroPersonne = assure.getIdentite().getNumeroPersonne();

    String retentionDate;
    if (newContract == null) {
      retentionDate = existingContract.getDateSouscription();
    } else {
      retentionDate = calculDateRetention(newContract, numeroPersonne);
    }

    Retention oldRetention =
        retentionDao.findRetention(idDeclarant, numeroAdherent, numeroContrat, numeroPersonne);
    String originalEndDate = null;
    if (oldRetention != null) {
      originalEndDate = oldRetention.getOriginalEndDate();
    } else if (existingContract != null) {
      originalEndDate = calculDateRetention(existingContract, numeroPersonne);
    }

    String today = DateUtils.formatDate(new Date(), DateUtils.YYYY_MM_DD);

    if (retentionDate == null
        || !DateUtils.before(retentionDate, today, DateUtils.FORMATTER)
        || (originalEndDate != null
            && !DateUtils.before(retentionDate, originalEndDate, DateUtils.FORMATTER))) {
      // CANCEL
      if (oldRetention != null) {
        updateHistoRetention(newContract, existingContract, numeroPersonne, oldRetention);
        oldRetention.setCurrentEndDate(retentionDate);
        cancelRetention(oldRetention);
      }
    } else if (existingContract != null
        && (oldRetention == null || !retentionDate.equals(oldRetention.getCurrentEndDate()))) {
      // initialize or update retention
      Retention retention =
          initializeRetention(
              newContract, existingContract, assure, originalEndDate, retentionDate);
      setMostRecentDate(retentionList, retention);
      retentionList.add(retention);
    }
  }

  private static void setMostRecentDate(List<Retention> retentionList, Retention retention) {
    String personNumber = retention.getPersonNumber();
    Optional<Retention> optionalRetention =
        retentionList.stream()
            .filter(retention1 -> personNumber.equals(retention1.getPersonNumber()))
            .findFirst();
    if (optionalRetention.isPresent()) {
      retentionList.remove(optionalRetention.get());
      retention.setCurrentEndDate(
          DateUtils.getMaxDateOrNull(
              optionalRetention.get().getCurrentEndDate(), retention.getCurrentEndDate()));
    }
  }

  private Retention initializeRetention(
      ContratAIV6 newContract,
      ContratAIV6 existingContract,
      AssureCommun assure,
      String originalEndDate,
      String newEndDate) {
    ContratAIV6 contract = Objects.requireNonNullElse(newContract, existingContract);
    Retention retention = new Retention();
    retention.setInsurerId(contract.getIdDeclarant());
    retention.setIssuingCompanyCode(contract.getSocieteEmettrice());
    retention.setSubscriberNumber(contract.getNumeroAdherent());
    retention.setContractNumber(contract.getNumero());
    String numeroPersonne = assure.getIdentite().getNumeroPersonne();
    retention.setPersonNumber(numeroPersonne);

    if (assure.getIdentite().getNir() != null) {
      retention.setNir(assure.getIdentite().getNir().getCode());
    }
    if (!CollectionUtils.isEmpty(assure.getIdentite().getAffiliationsRO())) {
      List<AffiliationRO> affiliationROList = new ArrayList<>();
      for (NirRattachementRO nirRattachementRO : assure.getIdentite().getAffiliationsRO()) {
        AffiliationRO affiliationRO = new AffiliationRO();
        Nir nir = new Nir();
        nir.setCode(nirRattachementRO.getNir().getCode());
        nir.setCle(nirRattachementRO.getNir().getCle());
        if (nirRattachementRO.getRattachementRO() != null) {
          AttachementRO attachementRO = new AttachementRO();
          attachementRO.setCenterCode(nirRattachementRO.getRattachementRO().getCodeCentre());
          attachementRO.setRegimeCode(nirRattachementRO.getRattachementRO().getCodeRegime());

          attachementRO.setHealthInsuranceCompanyCode(
              nirRattachementRO.getRattachementRO().getCodeCaisse());

          affiliationRO.setAttachementRO(attachementRO);
        }
        Periode periode = new Periode();
        periode.setDebut(nirRattachementRO.getPeriode().getDebut());
        if (nirRattachementRO.getPeriode().getFin() != null) {
          periode.setFin(nirRattachementRO.getPeriode().getFin());
        }

        affiliationRO.setNir(nir);
        affiliationRO.setPeriode(periode);
        affiliationROList.add(affiliationRO);
        retention.setAffiliationsRO(affiliationROList);
      }
    }

    retention.setBirthDate(assure.getIdentite().getDateNaissance());
    retention.setBirthRank(assure.getIdentite().getRangNaissance());
    retention.setOriginalEndDate(originalEndDate);
    retention.setCurrentEndDate(newEndDate);
    retention.setStatus(RetentionStatus.TO_PROCESS);
    retention.setReceptionDate(LocalDateTime.now());

    updateHistoRetention(newContract, existingContract, numeroPersonne, retention);

    return retention;
  }

  private void updateHistoRetention(
      ContratAIV6 newContract,
      ContratAIV6 existingContract,
      String numeroPersonne,
      Retention retention) {
    LinkedList<RetentionHistorique> histo = new LinkedList<>();
    if (existingContract != null) {
      RetentionHistorique retentionHistoriqueOriginal =
          new RetentionHistorique(
              LocalDateTime.now(),
              existingContract.getDateResiliation(),
              getMaxDateRadiation(existingContract, numeroPersonne),
              null);

      histo.add(retentionHistoriqueOriginal);
    }
    RetentionHistorique retentionHistoriqueCurrent;
    if (newContract != null) {
      retentionHistoriqueCurrent =
          new RetentionHistorique(
              LocalDateTime.now(),
              newContract.getDateResiliation(),
              getMaxDateRadiation(newContract, numeroPersonne),
              null);

    } else {
      retentionHistoriqueCurrent =
          new RetentionHistorique(
              LocalDateTime.now(), null, null, LocalDateTime.now().format(DateUtils.FORMATTER));
    }
    histo.add(retentionHistoriqueCurrent);
    retention.setHistorique(histo);
  }

  @Override
  public boolean isMultiContrat(String idDeclarant, String personNumber) {
    List<ContratAIV6> listContrats =
        servicePrestationDao.findServicePrestationV6(idDeclarant, personNumber);
    String today = LocalDate.now().toString();
    return listContrats.stream()
        .filter(
            contratAIV6 ->
                contratAIV6.getDateResiliation() == null
                    || !DateUtils.before(contratAIV6.getDateResiliation(), today))
        .map(ContratAIV6::getAssures)
        .flatMap(List::stream)
        .toList()
        .stream()
        .anyMatch(
            assure ->
                personNumber.equals(assure.getIdentite().getNumeroPersonne())
                    && (assure.getDateRadiation() == null
                        || !DateUtils.before(assure.getDateRadiation(), today)));
  }
}
