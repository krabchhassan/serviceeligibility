package com.cegedim.next.serviceeligibility.core.kafka.serviceprestation;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.KAFKA_TOPIC_BDD_EXTRACT_CONTRACT;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.SendResult;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbTrackingRepo;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafka.publisher.AbstractMessagePublisher;
import com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExtractContractProducer extends AbstractMessagePublisher<BeneficiaireId> {
  private final BddsToBlbTrackingRepo blbTrackingDao;

  public ExtractContractProducer(
      MessageProducerWithApiKey messageProducer,
      BeyondPropertiesService beyondPropertiesService,
      BddsToBlbTrackingRepo blbTrackingDao) {
    super(beyondPropertiesService, messageProducer);
    this.blbTrackingDao = blbTrackingDao;
  }

  @Override
  public void send(BeneficiaireId beneficiaireId, String key) throws KafkaSendingException {
    // aucun intérêt d'envoyer des messages sans nir :)
    if (beneficiaireId.getNir() == null) {
      return;
    }
    String topic =
        beyondPropertiesService.getPropertyOrThrowError(KAFKA_TOPIC_BDD_EXTRACT_CONTRACT);
    CompletableFuture<SendResult<BeneficiaireId>> completableFuture =
        messageProducer.send(topic, key, beneficiaireId);
    completableFuture.whenComplete(
        (result, error) -> {
          if (error != null) {
            String detail = details(error, key);
            this.updateTrackingStatus(String.format("Kafka error : %s", detail), beneficiaireId);
            throw new KafkaSendingException(String.format("Error sending kafka record %s", detail));
          } else {
            log.debug("Successfully received kafka record {}", beneficiaireId);
          }
        });
  }

  private void updateTrackingStatus(String error, BeneficiaireId beneficiary) {
    var tracking = blbTrackingDao.findByIdHex(beneficiary.getTrackingId());
    if (tracking == null) return; // early exit

    tracking.setStatus(BddsToBlbStatus.NO_SEND);
    tracking.setErrorCode("TechnicalError");
    tracking.setErrorLabel(error);
    blbTrackingDao.save(tracking);
  }
}
