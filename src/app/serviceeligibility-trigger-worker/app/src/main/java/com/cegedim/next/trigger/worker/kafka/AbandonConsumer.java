package com.cegedim.next.trigger.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(topics = "${kafka.topic.bdd-demande-abandon-trigger}")
public class AbandonConsumer {
  private final TriggerService triggerService;
  private final SasContratService sasContratService;

  @MessageHandler
  public void processMessageAbandon(@Payload TriggerId idTrigger) {
    sasContratService.abandonTrigger(idTrigger.getTriggerId());
    // Fin de l'abandon, on change le status a abandonne
    triggerService.updateOnlyStatus(idTrigger.getTriggerId(), TriggerStatus.Abandonned, "Abandon");
    log.debug("Abandon complete");
  }
}
