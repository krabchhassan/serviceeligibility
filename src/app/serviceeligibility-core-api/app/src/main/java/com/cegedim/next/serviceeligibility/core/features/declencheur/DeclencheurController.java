package com.cegedim.next.serviceeligibility.core.features.declencheur;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.TARGET_ENV;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.client.exceptions.S3Exception;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1" + UriConstants.DECLENCHEURS)
public class DeclencheurController {

  private final BeyondPropertiesService beyondPropertiesService;
  private final TriggerCreationService triggerCreationService;
  private final TriggerService triggerService;
  private final TriggerKafkaService triggerKafkaService;
  private final SasContratService sasContratService;
  private final AuthenticationFacade authenticationFacade;
  private final TriggerCSVService triggerCSVService;

  public DeclencheurController(
      BeyondPropertiesService beyondPropertiesService,
      TriggerCreationService triggerCreationService,
      TriggerService triggerService,
      TriggerKafkaService triggerKafkaService,
      SasContratService sasContratService,
      @Qualifier("bddAuth") AuthenticationFacade authenticationFacade,
      TriggerCSVService triggerCSVService) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.triggerCreationService = triggerCreationService;
    this.triggerService = triggerService;
    this.triggerKafkaService = triggerKafkaService;
    this.sasContratService = sasContratService;
    this.authenticationFacade = authenticationFacade;
    this.triggerCSVService = triggerCSVService;
  }

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public TriggerResponse getTrigger(TriggerRequest request) {
    if ("true".equals(request.getOwner())) {
      request.setOwner(this.authenticationFacade.getAuthenticationUserName());
    } else {
      request.setOwner(null);
    }
    return triggerService.getTriggers(
        request.getPerPage(),
        request.getPage(),
        request.getSortBy(),
        request.getDirection(),
        request);
  }

  @NewSpan
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<List<Trigger>> generateTrigger(@RequestBody String requestObj) {
    ObjectMapper mapper = new ObjectMapper();
    TriggerGenerationRequest request;
    try {
      request = mapper.readValue(requestObj, TriggerGenerationRequest.class);
    } catch (JsonProcessingException e) {
      throw new RequestValidationException(
          String.format("La requête est erronée : %s", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    try {
      return new ResponseEntity<>(
          triggerCreationService.generateTriggers(request), HttpStatus.CREATED);
    } catch (TriggerException e) {
      throw new RequestValidationException(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @NewSpan(name = "exportTriggeredBeneficiaries")
  @GetMapping(value = "/{id}/export")
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<CSVPrinter> exportTriggeredBeneficiaries(
      @PathVariable("id") String idTrigger, HttpServletResponse response) {
    DateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
    String filename =
        "ExtractDeclencheur_"
            + beyondPropertiesService.getPropertyOrThrowError(TARGET_ENV)
            + "_"
            + sdf.format(new Date())
            + ".csv";
    response.setContentType("text/csv;charset=UTF-8");
    response.setHeader(
        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
    Trigger trigger = triggerService.getTriggerById(idTrigger);
    Iterator<TriggeredBeneficiary> results =
        triggerService.getTriggeredBeneficiariesStream(idTrigger);
    if (trigger == null || results == null || !results.hasNext()) {
      throw new RequestValidationException(
          String.format("Aucun déclencheur existant avec l'identifiant %s", idTrigger),
          HttpStatus.NOT_FOUND);
    }

    try {
      CSVPrinter file;
      if (TriggerEmitter.Renewal.equals(trigger.getOrigine())) {
        file = triggerCSVService.readTriggerBenefFileFromS3(response.getWriter(), idTrigger);
      } else {
        file = triggerCSVService.createCSV(response.getWriter(), results, false);
      }

      return new ResponseEntity<>(file, HttpStatus.OK);
    } catch (IOException | S3Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @NewSpan
  @GetMapping(value = "/beneficiaires")
  @PreAuthorize(READ_PERMISSION)
  public TriggeredBeneficiaryResponse getTriggeredBneneficiaryError(
      @RequestParam String idTrigger,
      @RequestParam String status,
      @RequestParam int perPage,
      @RequestParam int page,
      @RequestParam(required = false) String sortDirection) {
    if (!status.equals("Error") && !status.equals("Warning")) {
      TriggeredBeneficiaryResponse response = new TriggeredBeneficiaryResponse();
      PagingResponseModel paging = new PagingResponseModel();
      paging.setPage(0);
      paging.setPerPage(perPage);
      paging.setTotalElements(0);
      paging.setTotalPages(0);
      response.setPaging(paging);
      response.setTriggeredBeneficiaries(new ArrayList<>());
      return response;
    }
    return triggerService.getTriggeredBeneficiariesWithError(
        perPage, page, idTrigger, sortDirection);
  }

  @NewSpan
  @PutMapping(value = "/{id}/statut/{statut}")
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> updateStatus(
      @PathVariable("id") String id, @PathVariable TriggerStatus statut) {
    try {
      triggerKafkaService.updateStatus(id, statut);
    } catch (TriggerNotFoundException e) {
      throw new RequestValidationException(
          "Aucun déclencheur n'existe avec cet identifiant",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_DECLENCHEUR_CARTE_EXCEPTION);
    } catch (TriggerException e) {
      throw new RequestValidationException(
          e.getLocalizedMessage(),
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_DECLENCHEUR_CARTE_EXCEPTION);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
