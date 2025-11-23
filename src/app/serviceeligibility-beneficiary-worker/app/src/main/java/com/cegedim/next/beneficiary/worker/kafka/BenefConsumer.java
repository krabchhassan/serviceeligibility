package com.cegedim.next.beneficiary.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.beneficiary.worker.service.BenefV5Service;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(
    topics = "#{'${KAFKA_TOPIC_BDD-BENEFAI:bdd-benefai}'}",
    id = "benefConsumer",
    groupId =
        "#{'${spring.kafka.consumer.group-id:${CLOUD_PROVIDER}-${TARGET_ENV}-bdd-beneficiary}'}",
    idIsGroup = false)
public class BenefConsumer {
  private final BenefV5Service benefAiV5Service;
  private final TraceService traceService;

  @MessageHandler
  public void processMessage(
      @Payload BenefAIV5 benefAIV5, @Header(Constants.UPDATE_BENEF_SOURCE) String source) {
    log.debug("Processing message from {}", source);

    String idDeclarant = benefAIV5.getAmc().getIdDeclarant();
    String traceId =
        traceService.createTrace(source, benefAIV5.getId(), Constants.BENEF_TRACE, idDeclarant);

    benefAIV5.setTraceId(traceId);
    benefAIV5.setKey(calculateKey(idDeclarant, benefAIV5.getIdentite()));

    log.debug("Services processing complete");

    processAccordingToVersion(traceId, benefAIV5, getSource(source));
  }

  private Source getSource(String service) {
    return switch (service) {
      case Constants.ORIGINE_SERVICE_PRESTATION -> Source.SERVICE_PRESTATION;
      case Constants.ORIGINE_PREST_IJ -> Source.PREST_IJ;
      default -> Source.AUTRE;
    };
  }

  private void processAccordingToVersion(String traceId, BenefAIV5 benefAIV5, Source source) {
    BenefAIV5 beneficiary = benefAiV5Service.process(benefAIV5, true, source);
    if (StringUtils.isNotBlank(traceId)) {
      traceService.completeTrace(
          traceId, TraceStatus.SuccesfullyProcessed, beneficiary.getId(), Constants.BENEF_TRACE);
    }
  }

  private String calculateKey(String idDeclarant, IdentiteContrat identite) {
    // Setting key for connectors
    return idDeclarant + "-" + identite.getNumeroPersonne();
  }
}
