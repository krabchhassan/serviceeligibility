package com.cegedim.next.prestij.worker.controller;

import static com.cegedim.next.prestij.worker.utils.PermissionConstants.CREATE_PERMISSION;
import static com.cegedim.next.prestij.worker.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.prestij.worker.exception.IdClientBoException;
import com.cegedim.next.prestij.worker.kafka.Producer;
import com.cegedim.next.prestij.worker.service.PrestIJValidationService;
import com.cegedim.next.prestij.worker.service.TraceService;
import com.cegedim.next.prestij.worker.service.pojo.PrestIJPojo;
import com.cegedim.next.prestij.worker.utils.Constants;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Oc;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.services.RestPrestIJService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PrestIJController {

  private final ObjectMapper mapper;

  private final Producer producer;

  private final PrestIJValidationService validationService;

  private final TraceService traceService;

  private final RestPrestIJService service;

  private final ProducerBenef benefProducer;

  private final AuthenticationFacade authenticationFacade;

  private final DeclarantService declarantService;

  private final EventService eventService;

  @PostMapping(value = "/v2/prestijs", consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_PERMISSION)
  @NewSpan
  public ResponseEntity<CreationResponse> createV2(@RequestBody PrestIJPojo body)
      throws JsonProcessingException {
    log.info("Création PrestiJ traceId : {}", body.getTraceId());
    PrestIJ prestIJ = new PrestIJ();
    prestIJ.setTraceId(body.getTraceId());
    prestIJ.setOc(body.getOc());
    prestIJ.setContrat(body.getContrat());
    prestIJ.setEntreprise(body.getEntreprise());
    prestIJ.setAssures(body.getAssures());
    return createPrestIJ(prestIJ);
  }

  private ResponseEntity<CreationResponse> createPrestIJ(PrestIJ prestIJ)
      throws JsonProcessingException {
    CreationResponse response = new CreationResponse();
    String traceId =
        traceService.createTrace(mapper.writeValueAsString(prestIJ), Constants.PRESTIJ_TRACE);
    response.setTraceid(traceId);
    prestIJ.setTraceId(traceId);
    eventService.sendObservabilityEventPrestijReception(prestIJ);

    try {
      String keycloakUser = this.authenticationFacade.getAuthenticationUserName();
      getOc(prestIJ, keycloakUser);
      validationService.validate(prestIJ);
      traceService.updateStatus(traceId, TraceStatus.MappingSucceeded, Constants.PRESTIJ_TRACE);

      String id = prestIJ.getContrat().getNumero() + "-" + prestIJ.getOc().getIdentifiant();
      boolean exists = service.getPrestIJ(id) != null;

      producer.send(prestIJ);

      // Send events if everything went well
      if (exists) {
        eventService.sendObservabilityEventPrestijModification(prestIJ);
      } else {
        eventService.sendObservabilityEventPrestijCreation(prestIJ);
      }

      // Enoie des personnes de la PrestIJ vers les topic benef
      List<BenefAIV5> benefs = service.prestIJMapping(prestIJ);
      for (BenefAIV5 benef : benefs) {
        benefProducer.send(
            benef, com.cegedim.next.serviceeligibility.core.utils.Constants.ORIGINE_PREST_IJ);
      }

      traceService.updateStatus(traceId, TraceStatus.SentToKafka, Constants.PRESTIJ_TRACE);
      response.setStatus(TraceStatus.SentToKafka.toString());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (ValidationException e) {
      response.setStatus(TraceStatus.ValidationFailed.toString());
      List<String> errorMessages = new ArrayList<>();
      errorMessages = splitMessages(e, errorMessages);
      ArrayList<String> retErrorMessages = new ArrayList<>(errorMessages);
      traceService.updateStatusError(
          traceId, TraceStatus.ValidationFailed, retErrorMessages, Constants.PRESTIJ_TRACE);
      manageV2Provider(response);
      response.setErrorMessages(retErrorMessages);
      eventService.sendObservabilityEventPrestijReceptionInvalid(prestIJ, e.getMessage());
    } catch (IdClientBoException e) {
      response.setStatus(TraceStatus.IdClientBOInvalid.toString());
      ArrayList<String> retErrorMessages = new ArrayList<>();
      retErrorMessages.add(e.getLocalizedMessage());
      traceService.updateStatusError(
          traceId, TraceStatus.IdClientBOInvalid, retErrorMessages, Constants.PRESTIJ_TRACE);
      manageV2Provider(response);
      response.setErrorMessages(retErrorMessages);
      eventService.sendObservabilityEventPrestijReceptionInvalid(prestIJ, e.getMessage());
    } catch (InterruptedException e) {
      log.warn("Interrupted!", e);
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      response.setStatus(TraceStatus.UnexpectedBddsException.toString());
      ArrayList<String> retErrorMessages = new ArrayList<>();
      retErrorMessages.add(e.getLocalizedMessage());
      traceService.updateStatusError(
          traceId, TraceStatus.UnexpectedBddsException, retErrorMessages, Constants.PRESTIJ_TRACE);
      response.setErrorMessages(retErrorMessages);
      eventService.sendObservabilityEventPrestijReceptionInvalid(prestIJ, e.getMessage());
    }

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private List<String> splitMessages(ValidationException e, List<String> errorMessages) {
    if (e.getMessage() != null) {
      errorMessages = Arrays.asList(e.getMessage().split("\n"));
    }
    return errorMessages;
  }

  private void manageV2Provider(CreationResponse response) {
    response.setProvider(Constants.ERROR_PROVIDER);
  }

  private void getOc(PrestIJ prestIJ, String keycloakUser) throws IdClientBoException {
    Oc oc = prestIJ.getOc();
    if (oc != null) {
      oc.setIdClientBO(keycloakUser);
      String identifiant = oc.getIdentifiant();
      if (StringUtils.isNotBlank(identifiant)) {
        try {
          String idClientBO = declarantService.getIdClientBo(identifiant);
          if (!idClientBO.equals(keycloakUser)) {
            throw new IdClientBoException(
                "L'identifiant Back Office "
                    + keycloakUser
                    + " ne permet pas d'accéder aux données du déclarant "
                    + identifiant);
          }
        } catch (ValidationException e) {
          throw new IdClientBoException(e.getMessage());
        }
      }
    }
  }

  @GetMapping(value = "/v1/prestijs/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<PrestIJ>> getPrestIJList(
      @RequestParam(value = "idDeclarant", required = false) String idDeclarant,
      @RequestParam(value = "numeroContrat", required = false) String numeroContrat,
      @RequestParam(value = "nirCode") String nirCode,
      @RequestParam(value = "nirCle") String nirCle,
      @RequestParam(value = "dateNaissance") String dateNaissance,
      @RequestParam(value = "rangNaissance") String rangNaissance) {
    log.info("Liste de PrestIJ suivant des critères de recherche");
    return new ResponseEntity<>(
        service.findByBeneficiary(
            idDeclarant, numeroContrat, nirCode, nirCle, dateNaissance, rangNaissance),
        HttpStatus.OK);
  }
}
