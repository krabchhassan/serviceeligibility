package com.cegedim.beyond.blb.recalcul.kafka.consumer;

import com.cegedim.beyond.blb.recalcul.kafka.model.BeneficiaryFeedingDto;
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
    id = "beneficiary",
    idIsGroup = false,
    groupId = "${spring.kafka.consumer.group-id}-beneficiary",
    topics = "${kafka.topic.bdds-to-blb-beneficiary-feeding-response}")
public class BeneficiaryFeedingResponseKafkaConsumer {
  private final BlbRecalculService blbRecalculService;

  @MessageHandler
  public void processMessage(@Payload final BeneficiaryFeedingDto feedingResponse) {
    if (feedingResponse == null) {
      log.error("cannot read message bdds-to-blb-beneficiary-feeding-response");
      return; // early exit
    }
    this.blbRecalculService.processBeneficiaryFeedingResponse(feedingResponse);
  }
}
