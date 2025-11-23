package com.cegedim.next.consumer.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.ContratAivService;
import com.cegedim.next.serviceeligibility.core.services.ExtractContractsService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.NumeroAssureException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(topics = "${kafka.topic.bdd-contract}")
public class Consumer {
  private final ContratAivService serviceV6;
  private final TriggerCreationService triggerService;
  private final TraceService traceService;
  private final ExtractContractsService extractContractsService;

  @MessageHandler
  public void processMessage(
      @Payload final ContratAICommun data,
      @Header(Constants.KAFKA_HEADER) final String version,
      @Header(Constants.KAFKA_HEADER_TRACE) final String traceId) {
    log.info(
        "In contract consumer - Processing message - Version {} for traceId {}", version, traceId);
    String newContractId = "";
    List<ContratAIV6> generatedContracts = null;
    ContratAIV6 newContract = null;
    ContratAIV6 oldContract = null;
    try {
      if (StringUtils.isNotBlank(traceId)) {
        this.traceService.updateStatus(
            traceId,
            TraceStatus.ReceivedFromKafka,
            com.cegedim.next.serviceeligibility.core.utils.Constants.CONTRACT_TRACE);
      }

      log.info("In contract consumer - Processing message V5 or V6");
      generatedContracts = serviceV6.process((ContratAIV6) data);
      if (CollectionUtils.isNotEmpty(generatedContracts)) {
        newContract = generatedContracts.get(0);
        oldContract = generatedContracts.get(1);

        newContractId = serviceV6.getId(newContract);

        log.info("In contract consumer - Send Extract Contrat");
        this.extractContractsService.sendExtractContractByBeneficiaryIdMessage(
            generatedContracts.getFirst());
      }

      if (StringUtils.isNotBlank(traceId)) {
        this.traceService.completeTrace(traceId, TraceStatus.SuccesfullyProcessed, newContractId);
      }
    } catch (final NumeroAssureException e) {
      log.error(e.getMessage(), e);
    } catch (final Exception e) {
      log.error(String.format("Unexpected exception : %s", e.getMessage()), e);
    }

    log.info("In contract consumer - shouldProcess Finished, will now generate trigger");

    // If contracts have been correctly generated...
    if (generatedContracts != null && generatedContracts.size() == 2) {
      this.triggerService.generateTriggersConsumer(newContract, oldContract, true);
    }
  }
}
