package com.cegedim.next.triggerrenouvellement.worker.kafka;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.NO_EXPORT;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerCountService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.InstanceProperties;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerProcessingService;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerRecyclingService;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@MessageListener(
    topics = "${kafka.topic.bdd-demande-renouvellement-declaration-unitaire}",
    id = "triggerRenouvellementUnitaire",
    idIsGroup = false)
public class Consumer {

  private final TriggerUnitaireWorkerProcessingService triggerUnitaireWorkerProcessingService;

  private final TriggerUnitaireWorkerRecyclingService triggerUnitaireWorkerRecyclingService;

  private final MongoClient client;

  private final TriggerCountService triggerCountService;

  private final TriggerService triggerService;
  private final TriggerRecyclageService triggerRecyclageService;
  private final TriggerCSVService triggerCSVService;

  private final boolean transactionnal;

  public Consumer(
      BeyondPropertiesService beyondPropertiesService,
      TriggerUnitaireWorkerProcessingService triggerUnitaireWorkerProcessingService,
      TriggerUnitaireWorkerRecyclingService triggerUnitaireWorkerRecyclingService,
      MongoClient client,
      TriggerCountService triggerCountService,
      TriggerService triggerService,
      TriggerRecyclageService triggerRecyclageService,
      TriggerCSVService triggerCSVService) {
    this.triggerUnitaireWorkerProcessingService = triggerUnitaireWorkerProcessingService;
    this.triggerUnitaireWorkerRecyclingService = triggerUnitaireWorkerRecyclingService;
    this.client = client;
    this.triggerCountService = triggerCountService;
    this.triggerService = triggerService;
    this.triggerRecyclageService = triggerRecyclageService;
    this.triggerCSVService = triggerCSVService;
    this.transactionnal =
        beyondPropertiesService.getBooleanProperty(InstanceProperties.TRANSACTIONNAL).orElse(true);
  }

  @MessageHandler
  public void processMessageUnitaireListener(
      @Payload TriggerBatchUnitaire triggerBatchUnitaire,
      @Header(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE) String randomRecyclingIdString,
      @Header(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER) String updateTrigger,
      Acknowledgment acknowledgment) {
    long randomRecyclingId = Long.parseLong(randomRecyclingIdString);
    log.debug(
        "In triggerRenouvellementUnitaire consumer - Processing message - recyclage {} - update du trigger {}",
        randomRecyclingId,
        updateTrigger);

    log.debug(
        "In triggerRenouvellementUnitaire consumer - Processing message triggerId {}, servicePrestationId : {}",
        triggerBatchUnitaire.getTriggerId(),
        triggerBatchUnitaire.getServicePrestationId());
    List<TriggeredBeneficiary> triggeredBeneficiaries =
        triggerBatchUnitaire.getTriggeredBeneficiaries();

    ClientSession session = client.startSession();
    log.debug("triggerRenouvellementUnitaire session {}", session);
    session.startTransaction(
        TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
    ResponseTriggerProcessing responseTriggerProcessing = new ResponseTriggerProcessing();
    try {
      RequestTriggerProcessing requestTriggerProcessing = new RequestTriggerProcessing();
      requestTriggerProcessing.setBenefs(triggeredBeneficiaries);
      requestTriggerProcessing.setIdTrigger(triggerBatchUnitaire.getTriggerId());
      requestTriggerProcessing.setSession(session);
      requestTriggerProcessing.setUpdateTrigger(updateTrigger);
      requestTriggerProcessing.setRandomRecyclingId(randomRecyclingId);
      if (randomRecyclingId == 0L) {
        responseTriggerProcessing =
            triggerUnitaireWorkerProcessingService.processTrigger(requestTriggerProcessing);
      } else {
        requestTriggerProcessing.setServicePrestationId(
            triggerBatchUnitaire.getServicePrestationId());
        responseTriggerProcessing =
            triggerUnitaireWorkerRecyclingService.processTrigger(requestTriggerProcessing);
      }
      if (acknowledgment != null) {
        acknowledgment.acknowledge();
        log.debug(
            "In triggerRenouvellementUnitaire consumer - acknowledge message - servicePrestationId {} - recyclage {}",
            triggerBatchUnitaire.getServicePrestationId(),
            randomRecyclingId);
      }
    } catch (KafkaException e) {
      log.error(e.getMessage(), e);
      responseTriggerProcessing.setTriggerContracts(new ArrayList<>());
    } finally {
      if (transactionnal) {
        if (CollectionUtils.isNotEmpty(responseTriggerProcessing.getTriggerContracts())) {
          session.commitTransaction();
          session.close();
          if (!responseTriggerProcessing.isWarning()) {
            for (TriggerContract contract : responseTriggerProcessing.getTriggerContracts()) {
              triggerUnitaireWorkerProcessingService.sendToTopicContratTP(
                  Objects.requireNonNullElse(
                      contract.getIdTrigger(), triggerBatchUnitaire.getTriggerId()),
                  contract);
            }
          }
        } else {
          session.abortTransaction();
          session.close();
        }
      }
      updateTriggerCount(triggerBatchUnitaire, randomRecyclingId);
    }

    log.debug("In triggerRenouvellementUnitaire consumer - Finished");
  }

  private void updateTriggerCount(
      TriggerBatchUnitaire triggerBatchUnitaire, long randomRecyclingId) {
    long result = triggerCountService.findAndLock(triggerBatchUnitaire.getTriggerId());
    long timeout = 60000;
    long startTime = System.currentTimeMillis();
    long difference;
    while (result == 0L) {
      result = triggerCountService.findAndLock(triggerBatchUnitaire.getTriggerId());
      difference = System.currentTimeMillis() - startTime;
      if (difference > timeout) {
        log.info("triggerCount timeout");
        return;
      }
    }
    triggerCountService.incCountTriggerUnitaire(triggerBatchUnitaire.getTriggerId());
    TriggerCount triggerCount =
        triggerCountService.getTriggerCount(triggerBatchUnitaire.getTriggerId());
    log.debug(
        "triggerCount after increments {} / {}", triggerCount.getCount(), triggerCount.getTotal());
    if (triggerCount.getCount() == triggerCount.getTotal()) {
      log.info("triggerCount finished");
      saveTrigger(triggerBatchUnitaire.getTriggerId(), randomRecyclingId, true);
    } else {
      boolean save = triggerCount.getCount() % 50 == 0;
      if (save) {
        log.info("triggerCount save {} / {}", triggerCount.getCount(), triggerCount.getTotal());
        saveTrigger(triggerBatchUnitaire.getTriggerId(), randomRecyclingId, false);
      }
    }
    triggerCountService.updateLockTriggerUnitaire(triggerBatchUnitaire.getTriggerId());
  }

  private void saveTrigger(String triggerId, Long randomRecyclingId, boolean endTrigger) {
    Trigger trigger = triggerService.getTriggerById(triggerId);
    if (trigger != null) {
      int nbBenefToProcess =
          (int)
              triggerService.getNbTriggeredBeneficiariesWithStatus(
                  trigger.getId(), TriggeredBeneficiaryStatusEnum.ToProcess);
      trigger.setNbBenefToProcess(nbBenefToProcess);
      log.debug("saveTrigger - nbBenefToProcess : {} ", nbBenefToProcess);
      trigger.setNbBenefKO(
          (int)
              triggerService.getNbTriggeredBeneficiariesWithStatus(
                  trigger.getId(), TriggeredBeneficiaryStatusEnum.Error));
      trigger.setNbBenefWarning(
          (int)
              triggerService.getNbTriggeredBeneficiariesWithStatus(
                  trigger.getId(), TriggeredBeneficiaryStatusEnum.Warning));
      if (endTrigger) {
        endTrigger(trigger, randomRecyclingId);
      }
      triggerService.saveTrigger(trigger);
    }
  }

  private void endTrigger(Trigger trigger, Long randomRecyclingId) {
    if (trigger.getNbBenefKO() > 0) {
      trigger.setStatus(TriggerStatus.ProcessedWithErrors);
    } else if (trigger.getNbBenefWarning() > 0) {
      trigger.setStatus(TriggerStatus.ProcessedWithWarnings);
    } else {
      trigger.setStatus(TriggerStatus.Processed);
    }
    trigger.setNbBenefToProcess(0);
    if (randomRecyclingId != 0L) {
      triggerService.updateRecyclagePeriodeFin(trigger, randomRecyclingId);
      triggerRecyclageService.launchFinishedEventsForRecycling(trigger, null);
    } else {
      trigger.setDateFinTraitement(new Date());
      triggerRecyclageService.launchFinishedEventsForRenewal(trigger);
    }

    String path =
        triggerCSVService.saveTriggerProcessToS3AndSendEvent(
            trigger.getId(), randomRecyclingId == 0L);
    if (!NO_EXPORT.equals(path)) {
      trigger.setExported(true);
    }
  }
}
