package com.cegedim.next.triggerrenouvellement.worker.kafka;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.NO_EXPORT;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerStatus;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryStatusEnum;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(
    topics = "${kafka.topic.bdd-update-renouvellement-declaration}",
    id = "triggerUpdateRenouvellement",
    idIsGroup = false)
public class ConsumerUpdateRenouv {
  private final TriggerService triggerService;
  private final TriggerRecyclageService triggerRecyclageService;
  private final TriggerCSVService triggerCSVService;

  @MessageHandler
  public void processMessageListenerUpdate(
      @Payload TriggerId idTrigger,
      @Header(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER) String updateTrigger,
      @Header(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE) String randomRecyclingIdString,
      Acknowledgment acknowledgment) {
    Trigger trigger = triggerService.getTriggerById(idTrigger.getTriggerId());
    long randomRecyclingId = Long.parseLong(randomRecyclingIdString);
    saveTrigger(idTrigger.getTriggerId(), trigger, updateTrigger, randomRecyclingId);
    if (acknowledgment != null) {
      acknowledgment.acknowledge();
    }
  }

  private void saveTrigger(
      String idTrigger, Trigger trigger, String updateTrigger, Long randomRecyclingId) {
    int nbBenefToProcess =
        (int)
            triggerService.getNbTriggeredBeneficiariesWithStatus(
                idTrigger, TriggeredBeneficiaryStatusEnum.ToProcess);
    trigger.setNbBenefToProcess(nbBenefToProcess);
    log.debug(
        "saveTrigger - nbBenefToProcess : {} updateTrigger {}", nbBenefToProcess, updateTrigger);
    trigger.setNbBenefKO(
        (int)
            triggerService.getNbTriggeredBeneficiariesWithStatus(
                idTrigger, TriggeredBeneficiaryStatusEnum.Error));
    trigger.setNbBenefWarning(
        (int)
            triggerService.getNbTriggeredBeneficiariesWithStatus(
                idTrigger, TriggeredBeneficiaryStatusEnum.Warning));
    if ((trigger.getDateFinTraitement() != null && randomRecyclingId == 0L)
        || nbBenefToProcess == 0) {

      if (trigger.getNbBenefKO() > 0) {
        trigger.setStatus(TriggerStatus.ProcessedWithErrors);
      } else if (trigger.getNbBenefWarning() > 0) {
        trigger.setStatus(TriggerStatus.ProcessedWithWarnings);
      } else {
        trigger.setStatus(TriggerStatus.Processed);
      }
      trigger.setNbBenefToProcess(0);
      if (randomRecyclingId != 0L) {
        triggerService.updateRecyclagePeriodeFin(trigger, randomRecyclingId);
        triggerRecyclageService.launchFinishedEventsForRecycling(trigger, null);
      } else {
        trigger.setDateFinTraitement(new Date());
        triggerRecyclageService.launchFinishedEventsForRenewal(trigger);
      }

      String path =
          triggerCSVService.saveTriggerProcessToS3AndSendEvent(
              trigger.getId(), randomRecyclingId == 0L);
      if (!NO_EXPORT.equals(path)) {
        trigger.setExported(true);
      }
    }
    triggerService.saveTrigger(trigger);
  }
}
