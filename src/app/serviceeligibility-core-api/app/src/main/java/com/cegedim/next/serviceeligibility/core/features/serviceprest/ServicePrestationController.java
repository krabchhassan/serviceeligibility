package com.cegedim.next.serviceeligibility.core.features.serviceprest;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.business.serviceprestation.service.RestServicePrestationServiceImpl;
import com.cegedim.next.serviceeligibility.core.model.entity.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
public class ServicePrestationController {

  private static final String NIR_OR_ADHERENT_NEEDED =
      "Veuillez renseigner le NIR ou le n° d'adhérent.";

  private static final String NO_BENEF_FOUND_PARAMS =
      "Les éléments fournis ne permettent pas d'identifier un bénéficiaire.";

  private static final String ID_NEEDED = "Veuillez renseigner l'id.'";

  private static final String NO_SERVICE_PRESTATION_FOUND_PARAMS =
      "L'id ne correspond pas à un service prestation.";

  private final RestServicePrestationServiceImpl service;

  @NewSpan
  @GetMapping(value = "/v6/servicePrestations", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ContratV6> getServicePrestationV6(
      @RequestParam(value = "idDeclarant", required = false) String idDeclarant,
      @RequestParam(value = "numeroAdherent", required = false) String numeroAdherent,
      @RequestParam(value = "dateNaissance") String dateNaissance,
      @RequestParam(value = "rangNaissance", required = false) String rangNaissance,
      @RequestParam(value = "debutPeriodeSoin", required = false) String debutPeriodeSoin,
      @RequestParam(value = "finPeriodeSoin", required = false) String finPeriodeSoin,
      @RequestParam(value = "nir", required = false) String nir) {
    log.info("Recherche contrat v4");
    // On recherche au minimum par nir ou n° d'adhérent
    if (StringUtils.isBlank(nir) && StringUtils.isBlank(numeroAdherent)) {
      throw new RequestValidationException(NIR_OR_ADHERENT_NEEDED, HttpStatus.BAD_REQUEST);
    }
    ContratV6 contrat =
        service.findServicePrestationV6(
            idDeclarant,
            numeroAdherent,
            dateNaissance,
            rangNaissance,
            debutPeriodeSoin,
            finPeriodeSoin,
            nir);

    if (contrat.getContrats().isEmpty()) {
      throw new RequestValidationException(NO_BENEF_FOUND_PARAMS, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(contrat, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/v5/servicePrestations", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ContratV5> getServicePrestationV5(
      @RequestParam(value = "idDeclarant", required = false) String idDeclarant,
      @RequestParam(value = "numeroAdherent", required = false) String numeroAdherent,
      @RequestParam(value = "dateNaissance") String dateNaissance,
      @RequestParam(value = "rangNaissance", required = false) String rangNaissance,
      @RequestParam(value = "debutPeriodeSoin", required = false) String debutPeriodeSoin,
      @RequestParam(value = "finPeriodeSoin", required = false) String finPeriodeSoin,
      @RequestParam(value = "nir", required = false) String nir) {
    log.info("Recherche contrat v5");
    // On recherche au minimum par nir ou n° d'adhérent
    if (StringUtils.isBlank(nir) && StringUtils.isBlank(numeroAdherent)) {
      throw new RequestValidationException(NIR_OR_ADHERENT_NEEDED, HttpStatus.BAD_REQUEST);
    }
    ContratV5 contrat =
        service.findServicePrestationV5(
            idDeclarant,
            numeroAdherent,
            dateNaissance,
            rangNaissance,
            debutPeriodeSoin,
            finPeriodeSoin,
            nir);

    if (contrat.getContrats().isEmpty()) {
      throw new RequestValidationException(NO_BENEF_FOUND_PARAMS, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(contrat, HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(value = "/v6/servicePrestations/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ServicePrestationV6>> searchPrestationsV6(
      @RequestParam(value = "idDeclarant") String idDeclarant,
      @RequestParam(value = "numeroPersonne") String numeroPersonne) {
    List<ServicePrestationV6> contrats =
        service.findServicePrestationV6(idDeclarant, numeroPersonne);

    return new ResponseEntity<>(contrats, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/v5/servicePrestations/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<ServicePrestationV5>> searchPrestationsV5(
      @RequestParam(value = "idDeclarant") String idDeclarant,
      @RequestParam(value = "numeroPersonne") String numeroPersonne) {
    log.info("Liste les contrats v5");
    List<ServicePrestationV5> contrats =
        service.findServicePrestationV5(idDeclarant, numeroPersonne);

    return new ResponseEntity<>(contrats, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/v6/servicePrestations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ContratAIV6> getServicePrestationV6(@PathVariable(value = "id") String id) {
    // On recherche par id
    if (StringUtils.isBlank(id)) {
      throw new RequestValidationException(ID_NEEDED, HttpStatus.BAD_REQUEST);
    }
    ContratAIV6 contrat = service.findServicePrestationV6(id);

    if (contrat == null) {
      throw new RequestValidationException(
          NO_SERVICE_PRESTATION_FOUND_PARAMS, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(contrat, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/v5/servicePrestations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ContratAIV5> getServicePrestationV5(@PathVariable(value = "id") String id) {
    log.info("Recherche de contrat v5 avec id : " + id);
    // On recherche par id
    if (StringUtils.isBlank(id)) {
      throw new RequestValidationException(ID_NEEDED, HttpStatus.BAD_REQUEST);
    }
    ContratAIV5 contrat = service.findServicePrestationV5(id);

    if (contrat == null) {
      throw new RequestValidationException(
          NO_SERVICE_PRESTATION_FOUND_PARAMS, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(contrat, HttpStatus.OK);
  }
}
