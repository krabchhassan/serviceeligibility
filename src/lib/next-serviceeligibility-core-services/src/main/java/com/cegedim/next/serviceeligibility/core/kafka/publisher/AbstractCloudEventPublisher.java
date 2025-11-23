package com.cegedim.next.serviceeligibility.core.kafka.publisher;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.SendResult;
import com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCloudEventPublisher<T> {
  private final MessageProducerWithApiKey messageProducer;
  private final String topicName;

  public void publishMessage(final MessageType messageType, final T data, final String key) {
    log.info("Send event {} with key {} to Topic {}", messageType, key, topicName);
    CompletableFuture<SendResult<T>> completableFuture = messageProducer.send(topicName, key, data);
    completableFuture.whenComplete(
        (result, error) -> {
          if (error != null) {
            log.error(
                String.format(
                    "fail to send message to topic %s for key %s, exception : ", topicName, key),
                error);
          } else {
            log.info(
                "Record with key {} written in topic {} to offset {} timestamp {}",
                key,
                topicName,
                result.getOffset(),
                result.getTimestamp());
          }
        });
  }
}
