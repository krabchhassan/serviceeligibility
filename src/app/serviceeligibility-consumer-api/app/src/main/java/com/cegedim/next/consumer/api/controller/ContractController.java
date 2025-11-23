package com.cegedim.next.consumer.api.controller;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;

import com.cegedim.next.consumer.api.kafka.ProducerConsumerApi;
import com.cegedim.next.consumer.api.service.*;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.ContractProcessResponseV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.AbstractValidationContratService;
import com.cegedim.next.serviceeligibility.core.services.IdClientBOService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ContractValidationBean;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContractReceptionEventUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ValidationContractException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ContractController {
  private final ObjectMapper mapper;
  private final ProducerConsumerApi producerConsumerApi;
  private final ProducerBenef benefProducer;
  private final TraceService traceService;
  private final RestContratService restContratService;
  private final AbstractValidationContratService validationService;
  private final AuthenticationFacade authenticationFacade;
  private final ExceptionService exceptionService;
  private final IdClientBOService idClientBOService;
  private final EventService eventService;
  private final ConsumerServicePrestationService consumerServicePrestationService;

  /**
   * Création d'un event Contrat avec une réponse en V2
   *
   * @param body corps du message
   * @param version version du contrat
   * @param idDeclarant identifiant AMC
   * @return résultat de création
   */
  protected ResponseEntity<CreationResponse> createContractV2(
      @RequestBody String body, String version, String idDeclarant) {
    body = body.replace("\n", "").replace("\r", "");
    String traceId = traceService.createTrace(body, Constants.CONTRACT_TRACE, idDeclarant);
    CreationResponse response = new CreationResponse();
    response.setTraceid(traceId);
    ContratAICommun contrat = null;
    try {
      if (Constants.CONTRAT_VERSION_5.equals(version)) {
        contrat = mapper.readValue(body, ContratAIV5.class);
      } else {
        contrat = mapper.readValue(body, ContratAIV6.class);
      }
      traceService.updateNumeroContrat(
          traceId, contrat.getNumero(), contrat.getNumeroAdherent(), Constants.CONTRACT_TRACE);
      manageContractSending(contrat, version, idDeclarant, traceId);
      response.setStatus(TraceStatus.SentToKafka.toString());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ValidationContractException e) {
      exceptionService.manageValidationContratException(
          e, response, Constants.CONTRACT_TRACE, traceId);
    } catch (IdClientBoException e) {
      exceptionService.manageIdClientBoException(e, response, Constants.CONTRACT_TRACE, traceId);
    } catch (JsonProcessingException e) {
      String errorMessage = "couldn't deserialize " + body;
      log.warn(errorMessage);
      ContratAIV5 contratAIV5 = new ContratAIV5();
      contratAIV5.setTraceId(traceId);
      eventService.sendObservabilityEventContractReceptionInvalid(contratAIV5, errorMessage);
      exceptionService.manageJsonProcessingException(
          e, response, Constants.CONTRACT_TRACE, traceId);
    } catch (InterruptedException e) {
      log.warn("Interrupted!", e);
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      log.error(String.format("UnexpectedBddsException %s", e.getLocalizedMessage()), e);
      exceptionService.manageException(e, response, Constants.CONTRACT_TRACE, traceId);
    } finally {
      ContractReceptionEventUtils.sendReceptionEvent(contrat, traceId, eventService);
    }
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private void manageContractSending(
      Object body, String version, String idDeclarant, String traceId)
      throws IdClientBoException, InterruptedException, KafkaSendingException {
    String keycloakUser = this.authenticationFacade.getAuthenticationUserName();
    ContractProcessResponseV5 processedData = new ContractProcessResponseV5();
    if (Constants.CONTRAT_VERSION_5.equals(version)) {
      processedData = processV5((ContratAIV5) body, keycloakUser, traceId, idDeclarant);
    } else if (Constants.CONTRAT_VERSION_6.equals(version)) {
      processedData =
          consumerServicePrestationService.processV6(
              (ContratAIV6) body, keycloakUser, traceId, idDeclarant);
    }
    traceService.updateStatus(traceId, TraceStatus.SentToKafka, Constants.CONTRACT_TRACE);
    log.info("Send contract in version {}", version);
    producerConsumerApi.send(
        processedData.getContract(),
        processedData.getKey(),
        version,
        traceId,
        keycloakUser,
        idDeclarant);

    List<BenefAIV5> benefs = processedData.getBenefs();

    for (BenefAIV5 benef : benefs) {
      benefProducer.send(benef, Constants.ORIGINE_SERVICE_PRESTATION);
    }
  }

  private ContractProcessResponseV5 processV5(
      ContratAIV5 contractV5, String keycloakUser, String traceId, String idDeclarant)
      throws IdClientBoException {
    ContractProcessResponseV5 reponse = new ContractProcessResponseV5();
    contractV5.setTraceId(traceId);
    traceService.updateStatus(traceId, TraceStatus.Deserialized, Constants.CONTRACT_TRACE);

    consumerServicePrestationService.logTestPermission(
        idDeclarant, contractV5.getIdDeclarant(), keycloakUser);
    // Contrôle que l'idClientBo a le droit de créer/modifier des
    // données de l'AMC
    try {
      idClientBOService.controlIdClientBO(idDeclarant, contractV5.getIdDeclarant(), keycloakUser);
    } catch (IdClientBoException e) {
      eventService.sendObservabilityEventContractReceptionInvalid(contractV5, null);
      throw e;
    }

    ContractValidationBean contractValidationBean = new ContractValidationBean();
    validationService.validateContrat(contractV5, contractValidationBean);
    // Valorisation du contrat
    validationService.transformeContrat(contractV5);
    ContratAIV6 contractV6 = MapperContrat.mapV5toV6(contractV5);
    consumerServicePrestationService.manageSansEffetAssure(contractV6);
    consumerServicePrestationService.getContractProcessResponse(
        keycloakUser, traceId, reponse, contractV6);

    return reponse;
  }

  /**
   * Création d'un event Contrat en V5 avec contrôle du flux et en cas d'exception gestion du
   * provider
   *
   * @param body corps du message
   * @return résultat de création
   */
  @PostMapping(
      value = Constants.EVENT_CONTRAT_V5,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  @NewSpan
  public ResponseEntity<CreationResponse> createContractV5(
      @PathVariable("idDeclarant") @NonNull String idDeclarant, @RequestBody String body) {
    log.info("Création event contrat v5");
    return createContractV2(body, Constants.CONTRAT_VERSION_5, idDeclarant);
  }

  /**
   * Création d'un event Contrat en V6 avec contrôle du flux et en cas d'exception gestion du
   * provider
   *
   * @param body corps du message
   * @return résultat de création
   */
  @PostMapping(
      value = Constants.EVENT_CONTRAT_V6,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  @NewSpan
  public ResponseEntity<CreationResponse> createContractV6(
      @PathVariable("idDeclarant") @NonNull String idDeclarant, @RequestBody String body) {
    log.info("Création event contrat v6");
    return createContractV2(body, Constants.CONTRAT_VERSION_6, idDeclarant);
  }

  @DeleteMapping(value = Constants.DELETE_CONTRAT_V3)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  @NewSpan
  public ResponseEntity<CreationResponse> deleteContractV3(
      @PathVariable("idDeclarant") @NonNull String idDeclarant,
      @PathVariable("numeroContrat") @NonNull String numeroContrat) {
    String traceId =
        traceService.createTrace(
            "Suppression du contrat n°" + numeroContrat + " pour le declarant " + idDeclarant,
            Constants.CONTRACT_TRACE,
            idDeclarant);
    traceService.updateNumeroContrat(traceId, numeroContrat, null, Constants.CONTRACT_TRACE);
    CreationResponse response = new CreationResponse();
    response.setTraceid(traceId);

    String keycloakUser = this.authenticationFacade.getAuthenticationUserName();
    try {
      idClientBOService.controlIdClientBO(idDeclarant, idDeclarant, keycloakUser);
    } catch (IdClientBoException e) {
      exceptionService.manageIdClientBoException(e, response, Constants.CONTRACT_TRACE, traceId);
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    ContratAIV6 contrat = restContratService.deleteContrat(idDeclarant, numeroContrat, null);
    if (contrat == null) {
      traceService.updateStatus(traceId, TraceStatus.ContractNotFound, Constants.CONTRACT_TRACE);
      ArrayList<String> retErrorMessages = new ArrayList<>();
      retErrorMessages.add("Aucun contrat n'existe avec les informations fournies");
      response.setErrorMessages(retErrorMessages);
      response.setStatus(TraceStatus.ContractNotFound.toString());
      response.setProvider("BDDS");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    traceService.updateStatus(traceId, TraceStatus.ContractDeleted, Constants.CONTRACT_TRACE);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(value = Constants.DELETE_CONTRAT_V4)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  @NewSpan
  public ResponseEntity<CreationResponse> deleteContractV4(
      @PathVariable("idDeclarant") @NonNull String idDeclarant,
      @PathVariable("numeroContrat") @NonNull String numeroContrat,
      @PathVariable("subscriberId") @NonNull String subscriberId) {
    String traceId =
        traceService.createTrace(
            "Suppression du contrat n°"
                + numeroContrat
                + " pour le declarant "
                + idDeclarant
                + " et le subscriberId "
                + subscriberId,
            Constants.CONTRACT_TRACE,
            idDeclarant);
    traceService.updateNumeroContrat(
        traceId, numeroContrat, subscriberId, Constants.CONTRACT_TRACE);
    CreationResponse response = new CreationResponse();
    response.setTraceid(traceId);

    String keycloakUser = this.authenticationFacade.getAuthenticationUserName();
    try {
      idClientBOService.controlIdClientBO(idDeclarant, idDeclarant, keycloakUser);
    } catch (IdClientBoException e) {
      exceptionService.manageIdClientBoException(e, response, Constants.CONTRACT_TRACE, traceId);
      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    ContratAIV6 contrat =
        restContratService.deleteContrat(idDeclarant, numeroContrat, subscriberId);
    if (contrat == null) {
      traceService.updateStatus(traceId, TraceStatus.ContractNotFound, Constants.CONTRACT_TRACE);
      ArrayList<String> retErrorMessages = new ArrayList<>();
      retErrorMessages.add("Aucun contrat n'existe avec les informations fournies");
      response.setErrorMessages(retErrorMessages);
      response.setStatus(TraceStatus.ContractNotFound.toString());
      response.setProvider("BDDS");
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    traceService.updateStatus(traceId, TraceStatus.ContractDeleted, Constants.CONTRACT_TRACE);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
