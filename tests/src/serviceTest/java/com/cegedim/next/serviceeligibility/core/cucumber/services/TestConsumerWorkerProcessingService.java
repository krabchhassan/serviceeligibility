package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ManageBenefsContract;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerBuildDeclarationNewService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerProcessingService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TestConsumerWorkerProcessingService extends TriggerProcessingService {

  public TestConsumerWorkerProcessingService(
      TriggerBuildDeclarationNewService triggerBuildDeclarationService,
      TriggerCreationService triggerCreationService,
      TriggerService triggerService,
      SasContratService sasContratService,
      ServicePrestationDao servicePrestationDao,
      Producer producer,
      TriggerRecyclageService triggerRecyclageService) {
    super(
        triggerBuildDeclarationService,
        triggerCreationService,
        triggerService,
        sasContratService,
        servicePrestationDao,
        producer,
        triggerRecyclageService);
  }

  public ResponseTriggerProcessing processTrigger(
      RequestTriggerProcessing requestTriggerProcessing) {
    boolean isError = false;
    ResponseTriggerProcessing responseTriggerProcessing = new ResponseTriggerProcessing();
    if (StringUtils.isNotBlank(requestTriggerProcessing.getIdTrigger())) {
      log.debug(
          "recherche du trigger {} et de ses benefs", requestTriggerProcessing.getIdTrigger());

      if (CollectionUtils.isNotEmpty(requestTriggerProcessing.getBenefs())) {

        ManageBenefsContract manageBenefsContract =
            getManageBenefsContract(requestTriggerProcessing);

        Trigger trigger = triggerService.getTriggerById(requestTriggerProcessing.getIdTrigger());

        triggerBuildDeclarationService.manageBenefs(
            trigger, manageBenefsContract, true, requestTriggerProcessing.getSession());

        isError = manageBenefsContract.isErreurBenef();
        processAccordingToError(false, isError, manageBenefsContract);
        // Modification des benefs en base
        log.debug("Modification du beneficiaire de déclencheur en base");
        triggerService.updateTriggeredBeneficiaries(manageBenefsContract.getBenefs());
        // Modification du déclencheur
        // sur le renouvellement, la modification du statut se fait au niveau du
        // triggerrenouvellementworker.
        trigger = triggerService.updateStatutTrigger(manageBenefsContract, trigger, isError, 0L);

        triggerRecyclageService.launchFinishedEvents(
            trigger, triggerService.getTriggeredBeneficiaries(trigger.getId()));
      }
    } else {
      log.error(
          "Le trigger {} n'existe pas ou ne référence aucun bénéficiaire",
          requestTriggerProcessing.getIdTrigger());
    }
    if (!isError) {
      responseTriggerProcessing
          .getTriggerContracts()
          .add(new TriggerContract(requestTriggerProcessing.getBenefs()));
    }
    return responseTriggerProcessing;
  }

  private static ManageBenefsContract getManageBenefsContract(
      RequestTriggerProcessing requestTriggerProcessing) {
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setBenefs(requestTriggerProcessing.getBenefs());
    manageBenefsContract.setSasCree(false);
    manageBenefsContract.setSasContrat(null);
    manageBenefsContract.setSasContratRecyclage(null);
    manageBenefsContract.setDeclarations(new LinkedList<>());
    manageBenefsContract.setErreurBenef(false);
    manageBenefsContract.setWarningBenef(false);
    return manageBenefsContract;
  }
}
