package com.cegedim.next.trigger.worker.service;

import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ManageBenefsContract;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerBuildDeclarationNewService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerProcessingService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TriggerWorkerProcessingService extends TriggerProcessingService {

  private final EventService eventService;

  public TriggerWorkerProcessingService(
      TriggerBuildDeclarationNewService triggerBuildDeclarationService,
      TriggerCreationService triggerCreationService,
      TriggerService triggerService,
      SasContratService sasContratService,
      ServicePrestationDao servicePrestationDao,
      Producer producer,
      TriggerRecyclageService triggerRecyclageService,
      EventService eventService) {
    super(
        triggerBuildDeclarationService,
        triggerCreationService,
        triggerService,
        sasContratService,
        servicePrestationDao,
        producer,
        triggerRecyclageService);
    this.eventService = eventService;
  }

  @Override
  @ContinueSpan(log = "processTrigger from TriggerWorkerProcessingService")
  public ResponseTriggerProcessing processTrigger(
      RequestTriggerProcessing requestTriggerProcessing) {
    boolean isError = false;
    boolean allWarning = false;
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
        sendAnomalyRelatedEvents(
            requestTriggerProcessing.isRecycling(),
            requestTriggerProcessing.isRenouvellement(),
            isError,
            manageBenefsContract);
        // Modification des benefs en base
        log.debug("Modification du beneficiaire de déclencheur en base");
        triggerService.updateTriggeredBeneficiaries(manageBenefsContract.getBenefs());
        // Modification du déclencheur
        // sur le renouvellement, la modification du statut se fait au niveau du
        // triggerrenouvellementworker.
        trigger = triggerService.updateStatutTrigger(manageBenefsContract, trigger, isError, 0L);
        allWarning = trigger.getNbBenef() <= trigger.getNbBenefWarning();
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
    responseTriggerProcessing.setWarning(allWarning);
    return responseTriggerProcessing;
  }

  private void sendAnomalyRelatedEvents(
      boolean isRecycle,
      boolean isRenouv,
      boolean isError,
      ManageBenefsContract manageBenefsContract) {
    if (isError && !isRecycle && !isRenouv) {
      for (TriggeredBeneficiary benef : manageBenefsContract.getBenefs()) {
        if (benef.getDerniereAnomalie() != null) {
          Anomaly anomaly = benef.getDerniereAnomalie().getAnomaly();
          if (Anomaly.BOBB_NO_PRODUCT_FOUND.equals(anomaly)
              || Anomaly.NO_CARD_RIGHT_PARAM.equals(anomaly)) {
            eventService.sendObservabilityEventTriggerCreatedInError(
                benef.getAmc(),
                benef.getGestionnaire(),
                benef.getNumeroAdherent(),
                benef.getNumeroContrat(),
                benef.getDerniereAnomalie().getDescription());
            break;
          }
        }
      }
    }
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
