package com.cegedim.beyond.blb.recalcul.kafka.publisher;

import com.cegedim.beyond.blb.recalcul.kafka.model.BlbLogicalDeletionDto;
import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType;
import com.cegedim.next.serviceeligibility.core.kafka.publisher.AbstractCloudEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
    value = {"event.publisher.activate"},
    havingValue = "true",
    matchIfMissing = true)
public class LogicalDeletionMessagePublisher
    extends AbstractCloudEventPublisher<BlbLogicalDeletionDto> {

  public LogicalDeletionMessagePublisher(
      final MessageProducerWithApiKey messageProducer,
      @Value("${kafka.topic.bdds-to-blb-logical-deletion:bdds-to-blb-logical-deletion}")
          final String topicName) {
    super(messageProducer, topicName);
  }

  public void publishDeletion() {
    this.publishMessage(MessageType.BLB_LOGICAL_DELETION, new BlbLogicalDeletionDto(), null);
  }
}
