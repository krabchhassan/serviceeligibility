package com.cegedim.next.consumer.worker.kafka;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.KAFKA_TOPIC_BDD_TO_BLB_BENEF_FEEDING;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.consumer.worker.model.BeneficiaireBLB;
import com.cegedim.next.serviceeligibility.core.kafka.publisher.AbstractCloudEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BlbProducer extends AbstractCloudEventPublisher<BeneficiaireBLB> {

  public BlbProducer(
      MessageProducerWithApiKey messageProducer, BeyondPropertiesService beyondPropertiesService) {
    super(
        messageProducer,
        beyondPropertiesService.getPropertyOrThrowError(KAFKA_TOPIC_BDD_TO_BLB_BENEF_FEEDING));
  }
}
