package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestTriggerService {

  private final MongoTemplate template;
  private final TriggerService triggerService;
  private final SasContratService sasContratService;

  public void deleteTriggerAndSas() {
    template.remove(new Query(), Constants.TRIGGER_COLLECTION);
    template.remove(new Query(), Constants.TRIGGERED_BENEFICIARY_COLLECTION);
    template.remove(new Query(), Constants.SAS_CONTRAT_COLLECTION);
  }

  public void deleteSas() {
    template.findAllAndRemove(new Query(), Constants.SAS_CONTRAT_COLLECTION);
  }

  private TriggerRequest getTriggerRequest(
      String idDeclarant, String numeroContrat, TriggerEmitter... emitters) {
    TriggerRequest request = new TriggerRequest();
    List<String> amcs = new ArrayList<>();
    amcs.add(idDeclarant);
    request.setAmcs(amcs);
    request.setNumeroContrat(numeroContrat);
    request.setIsContratIndividuel(true);
    request.setEmitters(List.of(emitters));

    return request;
  }

  public TriggerResponse getTriggers(
      String idDeclarant, String numeroContrat, TriggerEmitter... emitters) {
    TriggerRequest request = getTriggerRequest(idDeclarant, numeroContrat, emitters);
    return triggerService.getTriggers(10, 1, null, null, request);
  }

  public List<TriggeredBeneficiary> getTriggerBenefOnTrigger(String idTrigger) {
    return triggerService.getTriggeredBeneficiaries(idTrigger);
  }

  public void createTrigger(Trigger trigger) {
    triggerService.createTrigger(trigger);
  }

  public void createTriggerBeneficiary(TriggeredBeneficiary triggeredBeneficiary) {
    triggerService.createTriggeredBenef(triggeredBeneficiary);
  }

  public int getNbTriggerBenefOnSasByIdTrigger(String idTrigger) {
    int nbTriggerBenef = 0;
    List<SasContrat> sasContrats = sasContratService.getByIdTrigger(idTrigger);
    for (SasContrat sasContrat : sasContrats) {
      for (TriggerBenefs triggerBenefs : sasContrat.getTriggersBenefs()) {
        if (triggerBenefs.getTriggerId().equals(idTrigger)) {
          nbTriggerBenef += triggerBenefs.getBenefsInfos().size();
        }
      }
    }
    return nbTriggerBenef;
  }

  public SasContrat getSasContrat(String idTrigger) {
    return sasContratService.getByIdTrigger(idTrigger).get(0);
  }

  public List<SasContrat> getSasContratByContractNumber(String contractNumber) {
    return sasContratService.getByContractNumber(contractNumber);
  }
}
