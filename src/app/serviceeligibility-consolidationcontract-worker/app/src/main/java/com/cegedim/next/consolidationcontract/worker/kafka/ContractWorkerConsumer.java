package com.cegedim.next.consolidationcontract.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
@MessageListener(
    topics = "${kafka.topic.bdd-contrattp}",
    id = "declarationConsumer",
    idIsGroup = false)
public class ContractWorkerConsumer {
  private final Logger logger = LoggerFactory.getLogger(ContractWorkerConsumer.class);

  private final ContractService contractService;

  private final ContractTPService contractTPService;

  private final DeclarationService declarationService;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  private final EventService eventService;

  @MessageHandler
  public void processDeclaration(
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger) {
    ContractTP contractTPBeforeConsolidation =
        contractService.getContract(
            consolidationDeclarationsContratTrigger.getIdDeclarant(),
            consolidationDeclarationsContratTrigger.getNumeroContrat(),
            consolidationDeclarationsContratTrigger.getNumeroAdherent());

    List<Declaration> declarationList =
        declarationService.findDeclarationsOfContratAndTrigger(
            consolidationDeclarationsContratTrigger);
    boolean errorConsolidation = false;
    try {
      for (Declaration declaration : declarationList) {
        logger.debug("Processing declaration id {}", declaration.get_id());
        contractTPService.processDeclaration(declaration);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      errorConsolidation = true;
      // send message to beyondEvent
      eventService.sendObservabilityEventContractTPConsolidationFailed(
          consolidationDeclarationsContratTrigger,
          LocalDateTime.now(ZoneOffset.UTC).toString(),
          ExceptionUtils.getStackTrace(e));
    }

    if (!errorConsolidation) {
      // historisation
      elasticHistorisationContractService.putContractHistoryOnElastic(
          contractTPBeforeConsolidation);
    }
  }
}
