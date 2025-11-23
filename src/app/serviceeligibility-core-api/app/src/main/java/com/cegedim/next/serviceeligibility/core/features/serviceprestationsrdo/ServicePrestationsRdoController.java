package com.cegedim.next.serviceeligibility.core.features.serviceprestationsrdo;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.dto.ServicePrestationsRdoDto;
import com.cegedim.next.serviceeligibility.core.dto.ServicePrestationsRdoV2Dto;
import com.cegedim.next.serviceeligibility.core.services.serviceprestationsrdo.RestServicePrestationsRdoService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServicePrestationsRdoController {

  private final RestServicePrestationsRdoService service;

  public ServicePrestationsRdoController(RestServicePrestationsRdoService service) {
    this.service = service;
  }

  @GetMapping(value = "/v1/servicePrestationsRdo", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ServicePrestationsRdoDto> getServicePrestationsRdo(
      @RequestParam(value = "insuredId") String idDeclarant,
      @RequestParam(value = "subscriberId", required = false) String numeroAdherent,
      @RequestParam(value = "nir") String nir,
      @RequestParam(value = "birthDate") String dateNaissance,
      @RequestParam(value = "birthRank") String rangNaissance,
      @RequestParam(value = "contractNumberList", required = false)
          List<String> contractNumberList) {
    List<String> errorMessages = new ArrayList<>();
    if (StringUtils.isBlank(idDeclarant)) {
      errorMessages.add("L'information insuredId est obligatoire");
    }
    if (StringUtils.isBlank(numeroAdherent) && CollectionUtils.isEmpty(contractNumberList)) {
      errorMessages.add("L'information subscriberId ou contractNumberList est obligatoire");
    } else if (StringUtils.isNotBlank(numeroAdherent)
        && !CollectionUtils.isEmpty(contractNumberList)) {
      errorMessages.add(
          "Les informations subscriberId et contractNumberList ne peuvent pas être renseigné en même temps");
    }
    if (StringUtils.isBlank(nir)) {
      errorMessages.add("L'information nir est obligatoire");
    }
    if (StringUtils.isBlank(dateNaissance)) {
      errorMessages.add("L'information birthDate est obligatoire");
    }
    if (StringUtils.isBlank(rangNaissance)) {
      errorMessages.add("L'information birthRank est obligatoire");
    }
    if (!errorMessages.isEmpty()) {
      throw new RequestValidationException(
          String.join(". ", errorMessages), HttpStatus.BAD_REQUEST);
    }
    ServicePrestationsRdoDto contrat =
        service.getServicePrestationsRdo(
            idDeclarant, numeroAdherent, dateNaissance, rangNaissance, nir, contractNumberList);

    if (contrat.getContrats().isEmpty()) {
      return new ResponseEntity<>(contrat, HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(contrat, HttpStatus.OK);
  }

  @PostMapping(value = "/v2/servicePrestationsRdo", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<ServicePrestationsRdoV2Dto>> getServicePrestationsRdo(
      @RequestBody List<String> body) {
    List<ServicePrestationsRdoV2Dto> results = service.getServicePrestationsRdoV2(body);
    return new ResponseEntity<>(results, HttpStatus.OK);
  }
}
