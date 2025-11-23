package com.cegedim.next.serviceeligibility.core.kafka.services;

import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PersonAICommun;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryKafkaService {
  @Autowired private ProducerBenef producer;

  @ContinueSpan(log = "send benefKafka")
  public void send(final PersonAICommun person) throws KafkaSendingException, InterruptedException {
    producer.send(person, Constants.ORIGINE_SERVICE_PRESTATION);
  }
}
