package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.ContratAivService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.NumeroAssureException;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestContratService {

  private final ContratAivService serviceV6;

  private final TriggerCreationService triggerCreationService;

  private final TriggerService triggerService;

  private final TraceService traceService;

  private final MongoClient mongoClient;

  private final ContractService contractService;

  private final ContractTPService contractTPService;

  private final DeclarationService declarationService;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  private final TestConsumerWorkerProcessingService testConsumerWorkerProcessingService;

  public void processMessageTest(ContratAIV6 contract, String traceId) {
    log.info("In contract consumer - Processing message - for trace Id {}", traceId);
    List<ContratAIV6> generatedContracts = null;
    String newContractId = "";
    ContratAIV6 newContract = null;
    ContratAIV6 oldContract = null;
    ResultContrat result =
        getResultContrat(
            contract, traceId, generatedContracts, newContract, oldContract, newContractId);

    log.info("In contract consumer - shouldProcess Finished, will now generate trigger");

    // If contracts have been correctly generated...
    if (result.generatedContracts() != null && result.generatedContracts().size() == 2) {
      String idTrigger =
          triggerCreationService.generateTriggerFromContracts(
              result.newContract(), result.oldContract(), false, null, false);
      if (idTrigger != null) {
        ClientSession session = mongoClient.startSession();
        session.startTransaction(
            TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
        triggerService.updateTriggerToProcessing(idTrigger);
        RequestTriggerProcessing requestTriggerProcessing = new RequestTriggerProcessing();
        requestTriggerProcessing.setBenefs(triggerService.getTriggeredBeneficiaries(idTrigger));
        requestTriggerProcessing.setIdTrigger(idTrigger);
        requestTriggerProcessing.setSession(session);
        ResponseTriggerProcessing responseTriggerProcessing =
            testConsumerWorkerProcessingService.processTrigger(requestTriggerProcessing);
        if (!CollectionUtils.isEmpty(responseTriggerProcessing.getTriggerContracts())) {
          session.commitTransaction();
          session.close();
          for (TriggerContract triggerContract : responseTriggerProcessing.getTriggerContracts()) {
            createContratTPForTest(requestTriggerProcessing.getIdTrigger(), triggerContract);
          }
        } else {
          session.abortTransaction();
          session.close();
        }
      } else {
        log.info("generation problem");
      }
    }
  }

  private ResultContrat getResultContrat(
      ContratAIV6 contract,
      String traceId,
      List<ContratAIV6> generatedContracts,
      ContratAIV6 newContract,
      ContratAIV6 oldContract,
      String newContractId) {
    try {
      if (StringUtils.isNotBlank(traceId)) {
        traceService.updateStatus(traceId, TraceStatus.ReceivedFromKafka, Constants.CONTRACT_TRACE);
      }

      log.info("In contract consumer - Processing message V5/V6");
      generatedContracts = serviceV6.process(contract);
      if (!generatedContracts.isEmpty()) {
        newContract = generatedContracts.get(0);
        oldContract = generatedContracts.get(1);

        newContractId = serviceV6.getId(newContract);
      }

      if (StringUtils.isNotBlank(traceId)) {
        traceService.completeTrace(traceId, TraceStatus.SuccesfullyProcessed, newContractId);
      }
    } catch (NumeroAssureException e) {
      log.error(e.getMessage(), e);
    } catch (Exception e) {
      log.error(String.format("UnexpectedBddsException %s", e.getLocalizedMessage()), e);
    }
    return new ResultContrat(generatedContracts, newContract, oldContract);
  }

  private record ResultContrat(
      List<ContratAIV6> generatedContracts, ContratAIV6 newContract, ContratAIV6 oldContract) {}

  private static void mapDeclarationToConsolidationDeclarationsContratTrigger(
      TriggerContract triggerContract,
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger) {
    consolidationDeclarationsContratTrigger.setIdDeclarant(triggerContract.getIdDeclarant());
    consolidationDeclarationsContratTrigger.setNumeroContrat(triggerContract.getNumeroContrat());
    consolidationDeclarationsContratTrigger.setNumeroAdherent(triggerContract.getNumeroAdherent());
  }

  private void createContratTPForTest(String idTrigger, TriggerContract triggerContract) {
    ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger =
        new ConsolidationDeclarationsContratTrigger();
    consolidationDeclarationsContratTrigger.setIdTrigger(idTrigger);
    if (triggerContract != null) {
      mapDeclarationToConsolidationDeclarationsContratTrigger(
          triggerContract, consolidationDeclarationsContratTrigger);
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
        for (Declaration declaration1 : declarationList) {
          log.debug("Processing declaration id {}", declaration1.get_id());
          contractTPService.processDeclaration(declaration1);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        errorConsolidation = true;
        // send message to beyondEvent
      }

      if (!errorConsolidation) {
        // historisation
        elasticHistorisationContractService.putContractHistoryOnElastic(
            contractTPBeforeConsolidation);
      }
    }
  }
}
