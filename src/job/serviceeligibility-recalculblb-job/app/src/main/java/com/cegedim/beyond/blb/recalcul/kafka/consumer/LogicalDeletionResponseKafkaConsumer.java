package com.cegedim.beyond.blb.recalcul.kafka.consumer;

import com.cegedim.beyond.blb.recalcul.kafka.model.BlbLogicalDeletionResponseDto;
import com.cegedim.beyond.blb.recalcul.service.BlbRecalculService;
import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(
    id = "deletion",
    idIsGroup = false,
    groupId = "${spring.kafka.consumer.group-id}-deletion",
    topics = "${kafka.topic.bdds-to-blb-logical-deletion-response}")
public class LogicalDeletionResponseKafkaConsumer {
  private final BlbRecalculService blbRecalculService;

  @MessageHandler
  public void processMessage(@Payload final BlbLogicalDeletionResponseDto responseDto) {
    if (responseDto == null) {
      log.error("cannot read message bdds-to-blb-logical-deletion-response");
      return; // early exit
    }
    this.blbRecalculService.processLogicalDeletionResponse(responseDto);
  }
}
