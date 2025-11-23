package com.cegedim.next.consumer.api.kafka;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.KAFKA_TOPIC_BDD_CONTRACT;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.tracing.WithNewTrace;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerConsumerApi {
  private final BeyondPropertiesService beyondPropertiesService;

  private final MessageProducerWithApiKey messageProducer;

  @WithNewTrace
  public void send(
      ContratAICommun data,
      String key,
      String version,
      String traceId,
      String idClientBo,
      String idDeclarant)
      throws InterruptedException, KafkaSendingException {
    try {
      String topic = beyondPropertiesService.getPropertyOrThrowError(KAFKA_TOPIC_BDD_CONTRACT);
      log.info("sending to the topic {} with key {} and traceId {}", topic, key, traceId);
      Map<String, Object> headers =
          Map.of(
              Constants.KAFKA_HEADER,
              version,
              Constants.KAFKA_HEADER_TRACE,
              traceId,
              Constants.KAFKA_HEADER_ID_CLIENT_BO,
              idClientBo,
              Constants.KAFKA_HEADER_ID_DECLARANT,
              idDeclarant);
      messageProducer.send(topic, null, key, data, null, headers).get(60, TimeUnit.SECONDS);
    } catch (ExecutionException | TimeoutException e) {
      throw new KafkaSendingException(
          "Error sending contract " + data + " Error:" + e.getLocalizedMessage());
    }
  }
}
