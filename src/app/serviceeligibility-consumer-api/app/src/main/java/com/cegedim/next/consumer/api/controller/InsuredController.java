package com.cegedim.next.consumer.api.controller;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.INSURED_DATA_TRACE;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;

import com.cegedim.next.consumer.api.exception.ContractNotFound;
import com.cegedim.next.consumer.api.service.InsuredService;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.bdd.ExceptionService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InsuredController {

  private final ObjectMapper mapper;
  private final TraceService traceService;
  private final InsuredService insuredService;
  private final ExceptionService exceptionService;

  @PutMapping(
      value =
          "/v5/declarants/{idDeclarant}/contracts/{numeroContrat}/insureds/{numeroPersonne}/data",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  @NewSpan
  public ResponseEntity<CreationResponse> updateDataInsuredV5(
      @PathVariable(value = "idDeclarant") String idDeclarant,
      @PathVariable(value = "numeroContrat") String numeroContrat,
      @PathVariable(value = "numeroPersonne") String numeroPersonne,
      @RequestBody InsuredDataV5 body)
      throws JsonProcessingException {
    log.info("Modification contrat v5");
    String traceId =
        traceService.createTrace(mapper.writeValueAsString(body), INSURED_DATA_TRACE, idDeclarant);

    CreationResponse response = new CreationResponse();
    response.setTraceid(traceId);

    try {
      String sendResponse =
          manageInsuredSendingV5(idDeclarant, numeroContrat, numeroPersonne, body, traceId);

      if (StringUtils.isBlank(sendResponse)) {
        traceService.updateStatus(traceId, TraceStatus.SuccesfullyProcessed, INSURED_DATA_TRACE);
        response.setStatus(TraceStatus.SuccesfullyProcessed.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
      }
    } catch (ContractNotFound e) {
      exceptionService.manageContractNotFoundException(
          e, response, INSURED_DATA_TRACE, traceId, Constants.CONTRAT_VERSION_5);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    } catch (ValidationException e) {
      exceptionService.manageValidationException(e, response, INSURED_DATA_TRACE, traceId);
    } catch (IdClientBoException e) {
      exceptionService.manageIdClientBoException(e, response, INSURED_DATA_TRACE, traceId);
    } catch (InterruptedException e) {
      log.warn("Interrupted!", e);
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      exceptionService.manageException(e, response, INSURED_DATA_TRACE, traceId);
    }
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  protected String manageInsuredSendingV5(
      String idDeclarant,
      String numeroContrat,
      String numeroPersonne,
      InsuredDataV5 insuredDataAI,
      String traceId)
      throws ContractNotFound, IdClientBoException, InterruptedException, KafkaSendingException {
    traceService.updateStatus(traceId, TraceStatus.Deserialized, INSURED_DATA_TRACE);

    // Checking all mandatory fields
    checkMandatoryField(idDeclarant, "idDeclarant");
    checkMandatoryField(numeroContrat, "numeroContrat");
    checkMandatoryField(numeroPersonne, "numeroPersonne");

    return insuredService.updateInsured(
        insuredDataAI, idDeclarant, numeroContrat, numeroPersonne, traceId);
  }

  /**
   * Allows to check a string mandatory field
   *
   * @param field Field to check
   */
  private void checkMandatoryField(String field, String name) {
    if (StringUtils.isEmpty(field)) {
      throw new ValidationException("Champ requis: " + name);
    }
  }
}
