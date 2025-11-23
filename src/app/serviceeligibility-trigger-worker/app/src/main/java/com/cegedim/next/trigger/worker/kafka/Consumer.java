package com.cegedim.next.trigger.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.InstanceProperties;
import com.cegedim.next.trigger.worker.service.TriggerWorkerProcessingService;
import com.cegedim.next.trigger.worker.service.TriggerWorkerRecyclingService;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import java.util.ArrayList;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@MessageListener(topics = "${kafka.topic.bdd-demande-declaration}")
public class Consumer {

  private final TriggerService triggerService;

  private final TriggerWorkerProcessingService triggerWorkerProcessingService;

  private final TriggerWorkerRecyclingService triggerRecyclageProcessingService;

  private final MongoClient client;

  private final boolean transactionnal;

  public Consumer(
      TriggerService triggerService,
      TriggerWorkerProcessingService triggerWorkerProcessingService,
      TriggerWorkerRecyclingService triggerRecyclingProcessingService,
      MongoClient client,
      BeyondPropertiesService beyondPropertiesService) {
    this.triggerService = triggerService;
    this.triggerWorkerProcessingService = triggerWorkerProcessingService;
    this.triggerRecyclageProcessingService = triggerRecyclingProcessingService;
    this.client = client;
    this.transactionnal =
        beyondPropertiesService.getBooleanProperty(InstanceProperties.TRANSACTIONNAL).orElse(true);
  }

  @MessageHandler
  public void processMessage(
      @Payload TriggerId idTrigger,
      @Header(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE) String recyclage,
      Acknowledgment acknowledgment) {
    log.debug(
        "In trigger consumer - Processing message - idTrigger {} - recyclage {}",
        idTrigger,
        recyclage);
    ClientSession session = null;
    if (transactionnal) {
      session = client.startSession();
      session.startTransaction(
          TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
    }
    ResponseTriggerProcessing responseTriggerProcessing = new ResponseTriggerProcessing();
    try {
      RequestTriggerProcessing requestTriggerProcessing = new RequestTriggerProcessing();
      requestTriggerProcessing.setBenefs(
          triggerService.getTriggeredBeneficiaries(idTrigger.getTriggerId()));
      requestTriggerProcessing.setIdTrigger(idTrigger.getTriggerId());
      requestTriggerProcessing.setSession(session);
      requestTriggerProcessing.setRecycling(Boolean.parseBoolean(recyclage));
      if (!Boolean.parseBoolean(recyclage)) {
        triggerService.updateTriggerToProcessing(idTrigger.getTriggerId());
        responseTriggerProcessing =
            triggerWorkerProcessingService.processTrigger(requestTriggerProcessing);
      } else {
        responseTriggerProcessing =
            triggerRecyclageProcessingService.processTrigger(requestTriggerProcessing);
      }

      if (acknowledgment != null) {
        acknowledgment.acknowledge();
        log.debug(
            "In trigger consumer - acknowledge message - idTrigger {} - recyclage {}",
            idTrigger,
            recyclage);
      }
    } catch (KafkaException e) {
      log.debug("In trigger consumer - plantage kafka");
      log.error(e.getMessage(), e);
      responseTriggerProcessing.setTriggerContracts(new ArrayList<>());
    } finally {
      if (transactionnal) {
        if (CollectionUtils.isNotEmpty(responseTriggerProcessing.getTriggerContracts())) {
          log.debug("In trigger consumer - commit");
          session.commitTransaction();
          session.close();
          if (!responseTriggerProcessing.isWarning()) {
            for (TriggerContract contract : responseTriggerProcessing.getTriggerContracts()) {
              triggerWorkerProcessingService.sendToTopicContratTP(
                  Objects.requireNonNullElse(contract.getIdTrigger(), idTrigger.getTriggerId()),
                  contract);
            }
          }
        } else {
          log.debug("In trigger consumer - rollback");
          session.abortTransaction();
          session.close();
        }
      }
    }

    log.debug("In trigger consumer - Finished");
  }
}
