package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.PersonAICommun;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.kafkabenef.ProducerBenefForBatch;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BeneficiaryKafkaServiceForBatch {
  private ProducerBenefForBatch producer;

  @ContinueSpan(log = "send benefKafka")
  public void send(final PersonAICommun person) throws KafkaSendingException, InterruptedException {
    producer.send(person, Constants.ORIGINE_SERVICE_PRESTATION);
  }
}
