package com.cegedim.next.beneficiary.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.beneficiary.worker.service.BenefV5Service;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@MessageListener(
    topics = "#{'${SPEC_KAFKA_TOPIC_DECLARATIONS:${KAFKA_TOPIC_BDD-DECL}}'}",
    id = "declarationConsumer",
    idIsGroup = false,
    groupId =
        "#{'${spring.kafka.consumer.group-id:${CLOUD_PROVIDER}-${TARGET_ENV}-bdd-declaration}'}")
public class Consumer {
  private final BenefV5Service benefV5Service;
  private final BeneficiaryService benefService;
  private final TraceService traceService;
  private final ObjectMapper objectMapper;

  @MessageHandler
  public void processMessage(String json) throws JsonProcessingException {
    Declaration declaration = objectMapper.readValue(json, Declaration.class);
    processDeclaration(declaration);
  }

  public void processDeclaration(Declaration declaration) {
    String traceId = "";
    String idDeclarant = "";
    String numeroPersonne = "";
    String origine = "";
    if (declaration != null) {
      traceId =
          traceService.createTrace(
              Constants.SERVICE_TP,
              declaration.get_id(),
              Constants.DECLARATION_TRACE,
              declaration.getIdDeclarant());
      idDeclarant = declaration.getIdDeclarant();
      numeroPersonne = declaration.getBeneficiaire().getNumeroPersonne();
      origine = TriggerUtils.getOrigineDeclaration(declaration);
    }

    log.debug("Processing benef");

    Source source =
        Constants.ORIGINE_DECLARATIONTDB.equals(origine) ? Source.TDB_DECLARATION : Source.AUTRE;
    BenefAIV5 benefToSave =
        benefService.returnCreatedOrUpdatedBeneficiary(declaration, traceId, source);
    benefToSave.setKey(benefService.calculateKey(idDeclarant, numeroPersonne));

    if (log.isDebugEnabled()) {
      for (int i = 0; i < benefToSave.getServices().size(); i++) {
        log.debug("Service n°{} : {}", i + 1, benefToSave.getServices().get(i));
      }
    }
    BenefAIV5 beneficiary = benefV5Service.process(benefToSave, true, source);

    log.debug("Benef processed with key : {}", beneficiary.getKey());

    if (StringUtils.isNotBlank(traceId)) {
      traceService.completeTrace(
          traceId,
          TraceStatus.SuccesfullyProcessed,
          beneficiary.getTraceId(),
          Constants.DECLARATION_TRACE);
    }
  }
}
