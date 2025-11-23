package com.cegedim.next.serviceeligibility.core.kafka.publisher;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.SendResult;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessagePublisher<T> {
  protected final BeyondPropertiesService beyondPropertiesService;
  protected final MessageProducerWithApiKey messageProducer;

  public void send(final T object, final String key) throws KafkaSendingException {
    String topic =
        beyondPropertiesService.getPropertyOrThrowError(KAFKA_TOPIC_BDD_EXTRACT_CONTRACT);
    log.info("sending to the topic {} with key {}", topic, key);
    CompletableFuture<SendResult<T>> completableFuture = messageProducer.send(topic, key, object);
    completableFuture.whenComplete(
        (result, error) -> {
          if (error != null) {
            throw new KafkaSendingException(
                String.format("Error sending kafka record %s", details(error, key)));
          } else {
            log.debug("Successfully received kafka record {}", object);
          }
        });
  }

  protected String details(Throwable ex, String key) {
    if (ex instanceof KafkaProducerException kafkaProducerException) {
      return String.format(
          "{key: %s, topic: %s, partition: %d, timestamp: %d}",
          key,
          kafkaProducerException.getFailedProducerRecord().topic(),
          kafkaProducerException.getFailedProducerRecord().partition(),
          kafkaProducerException.getFailedProducerRecord().timestamp());
    }
    return ex.getMessage();
  }
}
