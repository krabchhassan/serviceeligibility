package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestConsumerContractService {

  private final ExceptionService exceptionService;
  private final TraceService traceService;
  private final ObjectMapper mapper;
  private final ConsumerServicePrestationService consumerServicePrestationService;
  private final TestContratService testContractService;

  @Value("${CLIENT_USERNAME}")
  private String apiKeyUsername;

  /**
   * @TEMP Création d'un event Contrat en V4 avec contrôle du flux et en cas d'exception gestion du
   * provider
   *
   * @param body corps du message
   * @return résultat de création
   */
  public ResponseEntity<CreationResponse> createContractTest(
      @RequestBody String body, String version, String idDeclarant) {

    body = body.replace("\n", "").replace("\r", "");
    String traceId = traceService.createTrace(body, Constants.CONTRACT_TRACE, idDeclarant);
    CreationResponse response = new CreationResponse();
    response.setTraceid(traceId);
    try {
      ContratAICommun contrat;
      if (Constants.CONTRAT_VERSION_5.equals(version)) {
        ContratAIV5 contratV5 = mapper.readValue(body, ContratAIV5.class);
        contrat = MapperContrat.mapV5toV6(contratV5);
      } else {
        contrat = mapper.readValue(body, ContratAIV6.class);
      }
      traceService.updateNumeroContrat(
          traceId, contrat.getNumero(), contrat.getNumeroAdherent(), Constants.CONTRACT_TRACE);
      manageContractTest(contrat, idDeclarant, traceId);
      response.setStatus(TraceStatus.SentToKafka.toString());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ValidationContractException e) {
      exceptionService.manageValidationContratException(
          e, response, Constants.CONTRACT_TRACE, traceId);
    } catch (IdClientBoException e) {
      exceptionService.manageIdClientBoException(e, response, Constants.CONTRACT_TRACE, traceId);
    } catch (JsonProcessingException e) {
      log.warn("couldn't deserialize {}", body);
      exceptionService.manageJsonProcessingException(
          e, response, Constants.CONTRACT_TRACE, traceId);
    } catch (Exception e) {
      log.error(String.format("UnexpectedBddsException %s", e.getLocalizedMessage()), e);
      exceptionService.manageException(e, response, Constants.CONTRACT_TRACE, traceId);
    }
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private void manageContractTest(Object body, String idDeclarant, String traceId)
      throws JsonProcessingException, IdClientBoException {
    consumerServicePrestationService.processV6(
        (ContratAIV6) body, apiKeyUsername, traceId, idDeclarant);
    testContractService.processMessageTest((ContratAIV6) body, traceId);
  }
}
