package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.SEND_EVENTS;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ManageBenefsContract;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TriggerRecyclageService {

  private final TriggerService triggerService;

  private final EventService eventService;

  private final SasContratService sasContratService;

  private final boolean sendEvents;

  public TriggerRecyclageService(
      TriggerService triggerService,
      EventService eventService,
      SasContratService sasContratService,
      BeyondPropertiesService beyondPropertiesService) {
    this.triggerService = triggerService;
    this.eventService = eventService;
    this.sasContratService = sasContratService;
    sendEvents = beyondPropertiesService.getBooleanProperty(SEND_EVENTS).orElse(Boolean.TRUE);
  }

  public void launchFinishedEventsForRecycling(Trigger trigger, SasContrat sasContratRecyclage) {
    List<TriggeredBeneficiary> triggeredBeneficiaries = new ArrayList<>();
    if (sasContratRecyclage == null && trigger.getBenefsToRecycle() != null) {
      trigger.getBenefsToRecycle().stream()
          .map(triggerService::getTriggeredBenefById)
          .forEach(triggeredBeneficiaries::add);
    } else if (sasContratRecyclage != null) {
      sasContratRecyclage.getTriggersBenefs().stream()
          .flatMap(tb -> tb.getBenefsInfos().stream().map(BenefInfos::getBenefId))
          .map(triggerService::getTriggeredBenefById)
          .forEach(triggeredBeneficiaries::add);
    }

    if (!triggeredBeneficiaries.isEmpty()) {
      launchFinishedEvents(trigger, triggeredBeneficiaries);
    }
  }

  public void launchFinishedEvents(
      Trigger trigger, List<TriggeredBeneficiary> triggeredBeneficiaries) {
    if (sendEvents) {
      eventService.sendObservabilityEventTriggerFinished(trigger);

      for (TriggeredBeneficiary triggeredBeneficiary : triggeredBeneficiaries) {
        eventService.sendObservabilityEventTriggerBeneficiaryFinished(triggeredBeneficiary);
      }
    }
  }

  public void launchFinishedEventsForRenewal(Trigger trigger) {
    if (sendEvents) {
      eventService.sendObservabilityEventTriggerFinished(trigger);
    }
  }

  public void launchFinishedEventsForRenewal(TriggeredBeneficiary triggeredBeneficiary) {
    eventService.sendObservabilityEventTriggerBeneficiaryFinished(triggeredBeneficiary);
  }

  public void sendEventsDeclarationAndDeleteSasContract(ManageBenefsContract manageBenefsContract) {
    LinkedList<Declaration> declarationList = manageBenefsContract.getDeclarations();
    if (!CollectionUtils.isEmpty(declarationList)) {
      log.debug("SUCCESS : envoi evenement observabilité des déclarations");
      for (Declaration declaration : declarationList) {
        eventService.sendObservabilityEventDeclarationCreation(declaration);
      }
    }
    // Suppression du sas contrat en cas de recyclage
    if (manageBenefsContract.getSasContratRecyclage() != null) {
      log.debug("Suppression du sasContrat");
      sasContratService.delete(manageBenefsContract.getSasContratRecyclage().getId());
    }
  }
}
