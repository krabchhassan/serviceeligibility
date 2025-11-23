package com.cegedim.next.serviceeligibility.core.features.parametragecartetp;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp.ParametrageCarteTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTPRequest;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTPResponseDto;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametrageCarteTPResponse;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ParametrageCarteTPNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ServicePrestationNotFoundException;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/v1" + UriConstants.PARAMETRAGE_CARTE_TP)
public class ParametrageCarteTPController {

  private final ParametrageCarteTPService parametrageCarteTPService;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ParametrageCarteTPResponse> getByParams(ParametrageCarteTPRequest request) {
    return new ResponseEntity<>(
        parametrageCarteTPService.getByParams(
            request.getPerPage(),
            request.getPage(),
            request.getSortBy(),
            request.getDirection(),
            request),
        HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(path = "{paramId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ParametrageCarteTPDto> getOne(
      @PathVariable("paramId") @NonNull final String paramId) {
    return new ResponseEntity<>(parametrageCarteTPService.getById(paramId), HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      value =
          "/amcs/{amc}/contratsIndividuels/{numeroContratIndividuel}/numeroAdherent/{numeroAdherent}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ParametrageCarteTP> getParametrageForContrat(
      @PathVariable("amc") String amc,
      @PathVariable("numeroContratIndividuel") String numeroContratIndividuel,
      @PathVariable("numeroAdherent") String numeroAdherent,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    try {
      return new ResponseEntity<>(
          parametrageCarteTPService.getParametrageCarteTPForUI(
              amc, numeroContratIndividuel, numeroAdherent),
          HttpStatus.OK);
    } catch (ServicePrestationNotFoundException e) {
      throw new RequestValidationException(
          "Aucun contrat trouvé sur ces critères",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_SERVICE_PRESTATION_NOT_FOUND_EXCEPTION);
    } catch (ParametrageCarteTPNotFoundException e) {
      throw new RequestValidationException(
          "Aucun paramétrage TP n'est présent pour ce contrat",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PARAMETRAGE_CARTE_TP_NOT_FOUND_EXCEPTION);
    }
  }

  @NewSpan
  @PostMapping
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<ParametrageCarteTP> create(
      @RequestBody ParametrageCarteTP parametrageCarteTp) {
    if (StringUtils.isBlank(parametrageCarteTp.getAmc())) {
      throw new RequestValidationException("Le champ amc est obligatoire", HttpStatus.BAD_REQUEST);
    }
    parametrageCarteTPService.create(parametrageCarteTp);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @NewSpan
  @PutMapping(value = "/{id}/statut")
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> updateStatus(
      @PathVariable("id") String id, @RequestBody ParametrageCarteTPStatut statut) {
    try {
      parametrageCarteTPService.updateStatus(id, statut);
    } catch (ParametrageCarteTPNotFoundException e) {
      throw new RequestValidationException(
          "Aucun paramétrage TP n'existe avec cet identifiant",
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CODE_PARAMETRAGE_CARTE_TP_NOT_FOUND_EXCEPTION);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/priorite/{amc}")
  public ResponseEntity<List<Integer>> getParamsPriority(@PathVariable("amc") String amc) {
    return new ResponseEntity<>(parametrageCarteTPService.getPriorityByAmc(amc), HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      path = "guarantees/{guaranteeCode}/insurers/{insurerCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ParametrageCarteTPResponseDto> getParametrageByGuaranteeCodeAndInsurerCode(
      @PathVariable("guaranteeCode") @NonNull final String guaranteeCode,
      @PathVariable("insurerCode") @NonNull final String insurerCode) {
    return new ResponseEntity<>(
        parametrageCarteTPService.getByGuaranteeCodeAndInsurerCode(guaranteeCode, insurerCode),
        HttpStatus.OK);
  }
}
