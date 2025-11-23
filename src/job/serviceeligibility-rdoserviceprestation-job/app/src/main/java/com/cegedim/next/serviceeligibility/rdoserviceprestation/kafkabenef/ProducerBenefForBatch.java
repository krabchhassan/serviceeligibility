package com.cegedim.next.serviceeligibility.rdoserviceprestation.kafkabenef;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PersonAICommun;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerBenefForBatch {
  @Value("${KAFKA_TOPIC_BDD-BENEFAI:bdd-benefai}")
  private String topicBenefAi;

  public static final String KAFKA_HEADER_BENEF_VERSION = "benefVersion";
  public static final String CONTRAT_VERSION_5 = "5";

  private final MessageProducerWithApiKey messageProducerWithApiKey;

  @ContinueSpan(log = "send - ProducerBenef")
  public void send(final PersonAICommun message, String source)
      throws InterruptedException, KafkaSendingException {
    try {
      String key = message.getAmc().getIdDeclarant() + "-";
      if (message.getIdentite() != null) {
        key += message.getIdentite().getNumeroPersonne();
      } else {
        key += "0";
      }

      log.info("sending to the topic {} with key {}", this.topicBenefAi, key);
      Map<String, Object> headers =
          Map.of(
              KAFKA_HEADER_BENEF_VERSION, CONTRAT_VERSION_5, Constants.UPDATE_BENEF_SOURCE, source);
      messageProducerWithApiKey
          .send(topicBenefAi, null, key, message, null, headers)
          .get(60, TimeUnit.SECONDS);
    } catch (final ExecutionException | TimeoutException e) {
      throw new KafkaSendingException(
          "Error sending benef " + message + " Error:" + e.getLocalizedMessage());
    }
  }
}
