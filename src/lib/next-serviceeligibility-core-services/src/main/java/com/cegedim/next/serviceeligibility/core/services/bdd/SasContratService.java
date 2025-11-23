package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.SasContratDao;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryStatusEnum;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SasContratService {

  private final SasContratDao sasContratDao;

  @ContinueSpan(log = "save SasContrat")
  public SasContrat save(SasContrat sas) {
    return sasContratDao.save(sas);
  }

  @ContinueSpan(log = "getByFunctionalKey SasContrat")
  public SasContrat getByFunctionalKey(
      String idDeclarant, String numeroContrat, String numeroAdherent) {
    return sasContratDao.getByFunctionalKey(idDeclarant, numeroContrat, numeroAdherent);
  }

  @ContinueSpan(log = "getByServicePrestationId SasContrat")
  public SasContrat getByServicePrestationId(String idServicePrestation) {
    return sasContratDao.getByServicePrestationId(idServicePrestation);
  }

  @ContinueSpan(log = "getByIdTrigger SasContrat")
  public List<SasContrat> getByIdTrigger(String idTrigger) {
    return sasContratDao.getByIdTrigger(idTrigger);
  }

  @ContinueSpan(log = "delete SasContrat")
  public void delete(String id) {
    sasContratDao.delete(id);
  }

  @ContinueSpan(log = "deleteByAmc SasContrat")
  public long deleteByAmc(String amc) {
    return sasContratDao.deleteByAmc(amc);
  }

  @ContinueSpan(log = "deleteByServicePrestationId SasContrat")
  public long deleteByServicePrestationId(String servicePrestationId) {
    return sasContratDao.deleteByAmc(servicePrestationId);
  }

  @ContinueSpan(log = "manageSasContrat (3 params)")
  public SasContrat manageSasContrat(
      SasContrat sasContrat, TriggeredBeneficiary benef, String anomalie) {
    if (sasContrat == null) {
      sasContrat = new SasContrat();
      sasContrat.setIdDeclarant(benef.getAmc());
      sasContrat.setNumeroContrat(benef.getNumeroContrat());
      sasContrat.setNumeroAdherent(benef.getNumeroAdherent());
      sasContrat.setServicePrestationId(benef.getServicePrestationId());
      List<TriggerBenefs> triggersBenefs = new ArrayList<>();
      TriggerBenefs tb = new TriggerBenefs();
      tb.setTriggerId(benef.getIdTrigger());
      triggersBenefs.add(tb);
      sasContrat.setTriggersBenefs(triggersBenefs);
      List<BenefInfos> benefsInfos = new ArrayList<>();
      BenefInfos benefInfos = new BenefInfos();
      benefInfos.setBenefId(benef.getId());
      benefInfos.setNumeroPersonne(benef.getNumeroPersonne());
      benefsInfos.add(benefInfos);
      tb.setBenefsInfos(benefsInfos);
      sasContrat.setTriggersBenefs(triggersBenefs);
      List<String> anomalies = new ArrayList<>();
      anomalies.add(anomalie);
      sasContrat.setAnomalies(anomalies);
      List<Date> dates = new ArrayList<>();
      dates.add(new Date());
      sasContrat.setDates(dates);
    } else {
      List<TriggerBenefs> triggersBenefs = sasContrat.getTriggersBenefs();
      // Recherche du TriggerBenefs contenant le trigger actuel
      int index = -1;
      for (int i = 0; i < triggersBenefs.size(); i++) {
        if (triggersBenefs.get(i).getTriggerId().equals(benef.getIdTrigger())) {
          index = i;
          break;
        }
      }
      if (index == -1) {
        TriggerBenefs tb = new TriggerBenefs();
        tb.setTriggerId(benef.getIdTrigger());
        List<BenefInfos> benefsInfos = new ArrayList<>();
        BenefInfos benefInfos = new BenefInfos();
        benefInfos.setBenefId(benef.getId());
        benefInfos.setNumeroPersonne(benef.getNumeroPersonne());
        benefsInfos.add(benefInfos);
        tb.setBenefsInfos(benefsInfos);
        triggersBenefs.add(tb);
        sasContrat.setTriggersBenefs(triggersBenefs);
        List<Date> dates = sasContrat.getDates();
        dates.add(new Date());
        sasContrat.setDates(dates);
      } else {
        List<BenefInfos> benefsInfos = sasContrat.getTriggersBenefs().get(index).getBenefsInfos();
        boolean alreadyPresent =
            benefsInfos.stream().anyMatch(b -> b.getBenefId().equals(benef.getId()));
        if (!alreadyPresent) {
          BenefInfos benefInfos = new BenefInfos();
          benefInfos.setBenefId(benef.getId());
          benefInfos.setNumeroPersonne(benef.getNumeroPersonne());
          benefsInfos.add(benefInfos);
        }
      }
      List<String> anomalies = sasContrat.getAnomalies();
      anomalies.add(anomalie);
      sasContrat.setAnomalies(anomalies);
      sasContrat.setRecycling(false);
    }
    return sasContrat;
  }

  @ContinueSpan(log = "abandonTrigger")
  public void abandonTrigger(String triggerId) {
    sasContratDao.abandonTrigger(triggerId);
    sasContratDao.removeEmptySas();
  }

  @ContinueSpan(log = "manageSasContrat (6 params)")
  public void manageSasContrat(
      Trigger trigger,
      List<TriggeredBeneficiary> benefs,
      String idDeclarant,
      String numeroContrat,
      String servicePrestationId,
      String numeroAdherent) {
    // Test d'existence d'un sasContrat
    SasContrat sasContrat =
        sasContratDao.getByFunctionalKey(idDeclarant, numeroContrat, numeroAdherent);
    if (sasContrat == null) {
      sasContrat = new SasContrat();
      sasContrat.setIdDeclarant(idDeclarant);
      sasContrat.setNumeroContrat(numeroContrat);
      sasContrat.setNumeroAdherent(numeroAdherent);
      sasContrat.setServicePrestationId(servicePrestationId);

      List<TriggerBenefs> triggersBenefs = new ArrayList<>();
      TriggerBenefs triggerBenef = new TriggerBenefs();
      triggerBenef.setTriggerId(trigger.getId());
      List<BenefInfos> benefsInfos = new ArrayList<>();
      List<String> anomalies = new ArrayList<>();
      for (TriggeredBeneficiary benef : benefs) {
        BenefInfos benefInfos = new BenefInfos();
        benefInfos.setBenefId(benef.getId());
        benefInfos.setNumeroPersonne(benef.getNumeroPersonne());
        benefsInfos.add(benefInfos);
        if (TriggeredBeneficiaryStatusEnum.Error.equals(benef.getStatut())) {
          anomalies.add(benef.getDerniereAnomalie().getDescription());
        }
      }
      triggerBenef.setBenefsInfos(benefsInfos);
      triggersBenefs.add(triggerBenef);
      sasContrat.setTriggersBenefs(triggersBenefs);
      List<Date> dates = new ArrayList<>();
      dates.add(new Date());
      sasContrat.setDates(dates);
      sasContrat.setAnomalies(anomalies);
    } else {
      List<TriggerBenefs> triggersBenefs = sasContrat.getTriggersBenefs();
      // Recherche du TriggerBenefs contenant le trigger actuel
      int index = -1;
      for (int i = 0; i < triggersBenefs.size(); i++) {
        if (triggersBenefs.get(i).getTriggerId().equals(trigger.getId())) {
          index = i;
          break;
        }
      }
      TriggerBenefs tb;
      List<BenefInfos> benefsInfos;
      List<String> anomalies = sasContrat.getAnomalies();
      if (index == -1) {
        tb = new TriggerBenefs();
        tb.setTriggerId(trigger.getId());
        benefsInfos = new ArrayList<>();
        tb.setBenefsInfos(benefsInfos);
        triggersBenefs.add(tb);
      } else {
        tb = sasContrat.getTriggersBenefs().get(index);
        benefsInfos = tb.getBenefsInfos();
      }

      for (TriggeredBeneficiary benef : benefs) {
        boolean exists = benefsInfos.stream().anyMatch(b -> b.getBenefId().equals(benef.getId()));
        if (!exists) {
          BenefInfos newBenef = new BenefInfos();
          newBenef.setBenefId(benef.getId());
          newBenef.setNumeroPersonne(benef.getNumeroPersonne());
          benefsInfos.add(newBenef);
        }
        if (TriggeredBeneficiaryStatusEnum.Error.equals(benef.getStatut())) {
          anomalies.add(benef.getDerniereAnomalie().getDescription());
        }
      }

      sasContrat.getDates().add(new Date());
      sasContrat.setRecycling(false);
    }
    sasContratDao.save(sasContrat);
  }

  @ContinueSpan(log = "dropCollection SasContrat")
  public void dropCollection() {
    sasContratDao.deleteAll();
  }

  @ContinueSpan(log = "updateRecycling SasContrat")
  public void updateRecycling(String sasId, boolean recycling) {
    sasContratDao.updateRecycling(sasId, recycling);
  }

  public List<String> getByPersonNumber(String numeroPersonne) {
    return sasContratDao.getByPersonNumber(numeroPersonne);
  }

  public List<SasContrat> getByContractNumber(String numeroContrat) {
    return sasContratDao.getByContractNumber(numeroContrat);
  }
}
