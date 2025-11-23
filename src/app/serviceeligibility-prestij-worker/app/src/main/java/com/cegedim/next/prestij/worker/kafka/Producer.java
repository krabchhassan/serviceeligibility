package com.cegedim.next.prestij.worker.kafka;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.next.prestij.worker.exception.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
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
public class Producer {
  @Value("${KAFKA_TOPIC_BDD-PRESTIJ:prestij}")
  private String topic;

  private final MessageProducerWithApiKey messageProducer;

  public void send(PrestIJ message) throws KafkaSendingException, InterruptedException {
    String key = message.getContrat().getNumero() + "-" + message.getOc().getIdentifiant();
    message.set_id(key);

    try {
      // TODO reliquat BLUE-3312 MongoKey en key avant -> passage en string
      messageProducer.send(topic, key, message).get(60, TimeUnit.SECONDS);
    } catch (ExecutionException | TimeoutException firstE) {

      Thread.sleep(1000);
      log.warn("Retrying sending to Kakfa after 1 sec{}", firstE.getMessage());
      try {
        messageProducer.send(topic, key, message).get(60, TimeUnit.SECONDS);
      } catch (ExecutionException | TimeoutException e) {
        throw new KafkaSendingException(
            "Error sending prestij " + message + " Error:" + e.getLocalizedMessage());
      }
    }
  }
}
