package com.cegedim.next.triggerrenouvellement.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerCountService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(
    topics = "${kafka.topic.bdd-demande-renouvellement-declaration}",
    id = "triggerRenouvellement",
    idIsGroup = false)
public class ConsumerDemandeRenouv {
  private final TriggerService triggerService;
  private final TriggerKafkaService triggerKafkaService;
  private final TriggerCountService triggerCountService;

  @MessageHandler
  public void processMessageListener(
      @Payload TriggerId idTrigger,
      @Header(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE) String recyclage,
      Acknowledgment acknowledgment) {
    log.debug(
        "In triggerRenouvellement consumer - Processing message for trigger {} - recyclage {}",
        idTrigger.getTriggerId(),
        recyclage);
    Long randomRecyclingId = 0L;

    Trigger trigger = triggerService.getTriggerById(idTrigger.getTriggerId());
    if (!TriggerStatus.Processing.equals(trigger.getStatus())) {
      log.info("In triggerRenouvellement consumer - Started");
      trigger.setStatus(TriggerStatus.Processing);
      if (Boolean.parseBoolean(recyclage)) {
        RecyclingPeriods recyclingPeriods = new RecyclingPeriods();
        randomRecyclingId = RandomUtils.nextLong();
        recyclingPeriods.setId(randomRecyclingId);
        Periode periode = new Periode();
        periode.setDebut(DateUtils.generateDate());
        recyclingPeriods.setPeriode(periode);
        trigger.getPeriodes().add(recyclingPeriods);
        trigger.setNbBenefToProcess(trigger.getNbBenefKO());
        trigger.setBenefsToRecycle(
            triggerService.getTriggeredBeneficiarieIdsWithStatus(
                idTrigger.getTriggerId(), TriggeredBeneficiaryStatusEnum.Error));
      } else {
        trigger.setDateDebutTraitement(new Date());
        trigger.setNbBenefToProcess(trigger.getNbBenef());
      }
      trigger.setNbBenefKO(0);
      triggerService.saveTrigger(trigger);
      try {
        int nombreTriggerUnitaire;
        nombreTriggerUnitaire =
            triggerService.getNombreServicePrestationByTriggerId(
                idTrigger.getTriggerId(), Boolean.parseBoolean(recyclage));
        log.info("triggerBatchUnitaires {}", nombreTriggerUnitaire);
        triggerCountService.saveTriggerCount(idTrigger.getTriggerId(), nombreTriggerUnitaire);
        triggerKafkaService.sendMessageUnitaire(idTrigger.getTriggerId(), randomRecyclingId);
        log.info("In triggerRenouvellement consumer - Finished");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        trigger.setNbBenefKO(trigger.getNbBenef());
        trigger.setNbBenefToProcess(0);
        trigger.setStatus(TriggerStatus.ProcessedWithErrors);
        triggerService.saveTrigger(trigger);
        List<TriggeredBeneficiary> triggeredBeneficiaries =
            triggerService.getTriggeredBeneficiaries(trigger.getId());
        for (TriggeredBeneficiary triggeredBeneficiary : triggeredBeneficiaries) {
          if (TriggeredBeneficiaryStatusEnum.ToProcess.equals(triggeredBeneficiary.getStatut())) {
            TriggerUtils.addStatus(
                triggeredBeneficiary,
                TriggeredBeneficiaryStatusEnum.Error,
                TriggeredBeneficiaryAnomaly.create(Anomaly.TECHNICAL_ERROR),
                true);
          }
        }
        triggerService.updateTriggeredBeneficiaries(triggeredBeneficiaries);
        log.info("In triggerRenouvellement consumer - Finished With Error");
      }
    }
    if (acknowledgment != null) {
      acknowledgment.acknowledge();
    }
  }
}
