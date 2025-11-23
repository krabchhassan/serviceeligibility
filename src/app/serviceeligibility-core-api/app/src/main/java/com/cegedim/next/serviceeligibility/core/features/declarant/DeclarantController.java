package com.cegedim.next.serviceeligibility.core.features.declarant;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.ALLOW_CLEAN_SERVICES;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.RestDeclarantService;
import com.cegedim.next.serviceeligibility.core.features.utils.DeclarantUtils;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/declarants")
public class DeclarantController {

  private final BeyondPropertiesService beyondPropertiesService;

  private final RestDeclarantService service;

  private final DeclarantBackendService declarantBackendService;

  private final AuthenticationFacade authenticationFacade;

  public DeclarantController(
      BeyondPropertiesService beyondPropertiesService,
      RestDeclarantService service,
      DeclarantBackendService declarantBackendService,
      AuthenticationFacade authenticationFacade) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.service = service;
    this.declarantBackendService = declarantBackendService;
    this.authenticationFacade = authenticationFacade;
  }

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<DeclarantDto>> getAll() {
    return new ResponseEntity<>(service.findAllDto(), HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(path = "{declarantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Declarant> getOne(@PathVariable("declarantId") @NonNull final String id) {
    return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
  }

  @NewSpan
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Declarant> create(@RequestBody @Valid Declarant declarant) {
    service.create(declarant);
    Declarant createdDeclarant = service.findById(declarant.get_id());
    return new ResponseEntity<>(createdDeclarant, HttpStatus.CREATED);
  }

  /**
   * Resource REST qui renvoie les declarants de la base de donnees en fonction des parametres dans
   * HttpServletRequest.
   *
   * @return la reponse avec la liste des declarants ou un declarant.
   */
  @NewSpan
  @GetMapping(value = UriConstants.DECLARANTS_SEARCH, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<ServicesDeclarantDto>> getDeclarantsByCriteria(
      @RequestParam(value = "numero", required = false) String numero,
      @RequestParam(value = "nom", required = false) String nom,
      @RequestParam(value = "couloir", required = false) String couloir,
      @RequestParam(value = "service", required = false) String service) {

    return new ResponseEntity<>(
        declarantBackendService.findServicesDtoByCriteria(numero, nom, couloir, service),
        HttpStatus.OK);
  }

  /**
   * Resource REST qui renvoie les declarants de la base de donnees.
   *
   * @return la reponse avec la liste des declarants.
   */
  @NewSpan
  @GetMapping(value = UriConstants.DECLARANTS_LIGHT, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<DeclarantLightDto>> getAllDeclarantsLight() {
    return new ResponseEntity<>(declarantBackendService.findAllLightDto(), HttpStatus.OK);
  }

  /**
   * Resource REST qui renvoie les declarants de la base de donnees en fonction des parametres dans
   * HttpServletRequest.
   *
   * @return la reponse avec la liste des declarants ou un declarant.
   */
  @NewSpan
  @GetMapping(
      value = UriConstants.DECLARANTS_OPEN + "/{numAmc}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<DeclarantBackendDto> getDeclarant(@PathVariable("numAmc") String numAmc) {
    DeclarantBackendDto declarant = declarantBackendService.findDtoById(numAmc);
    if (declarant == null) {
      throw new RequestValidationException(
          "Declarant with id " + numAmc + " doesn't exist.", HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(declarant, HttpStatus.OK);
  }

  /**
   * Resource REST qui cree le declarant.
   *
   * @return la reponse
   * @throws ParseException L'exception.
   */
  @NewSpan
  @PostMapping(value = UriConstants.CREATE_DECLARANT, consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> createDeclarant(@RequestBody DeclarantRequestDto declarant) {
    if (!checkAMCNumberOK(declarant)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    List<String> errorMessages = checkUnicityOK(declarant);
    if (!errorMessages.isEmpty()) {
      throw new RequestValidationException(
          String.join(". ", errorMessages), HttpStatus.BAD_REQUEST);
    }
    declarant.setUser(this.authenticationFacade.getAuthenticationUserName());
    declarant.setPilotages(
        declarantBackendService.validationPilotagesRequestDto(declarant.getPilotages()));
    declarantBackendService.createDeclarant(declarant);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Resource REST - MAJ declarant.
   *
   * @return la reponse
   * @throws ParseException L'exception.
   */
  @NewSpan
  @PostMapping(value = UriConstants.UPDATE_DECLARANT, consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> updateDeclarant(@RequestBody DeclarantRequestDto declarant) {
    if (!checkAMCNumberOK(declarant)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    List<String> errorMessages = checkUnicityOK(declarant);
    if (!errorMessages.isEmpty()) {
      throw new RequestValidationException(
          String.join(". ", errorMessages), HttpStatus.BAD_REQUEST);
    }
    declarant.setUser(this.authenticationFacade.getAuthenticationUserName());
    declarant.setPilotages(
        declarantBackendService.validationPilotagesRequestDto(declarant.getPilotages()));
    declarantBackendService.updateDeclarant(declarant);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Resource REST qui renvoie les declarants-echanges de la base de donnees.
   *
   * @return la reponse avec la liste des declarants-echange
   */
  @NewSpan
  @GetMapping(value = UriConstants.DECLARANT_ECHANGES, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<DeclarantEchangeDto> getDeclarantEchanges() {
    return new ResponseEntity<>(declarantBackendService.findAllDeclarantsEchanges(), HttpStatus.OK);
  }

  /**
   * Resource REST qui renvoie les 10 declarants les plus recemment modifies par un utilisateur
   * envoye en parametre.
   *
   * @return la reponse avec la liste des declarants ou un declarant.
   */
  @NewSpan
  @GetMapping(
      value = UriConstants.DECLARANTS_LAST_MODIFIED,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_CONTRACT_PERMISSION)
  public ResponseEntity<List<ServicesDeclarantDto>> getLastUpdatedDeclarants() {
    String user = this.authenticationFacade.getAuthenticationUserName();
    return new ResponseEntity<>(
        declarantBackendService.findListDtoByUser(user, 0, 10), HttpStatus.OK);
  }

  private boolean checkAMCNumberOK(@RequestBody DeclarantRequestDto declarant) {
    String amcNumber = declarant.getNumero();
    if (StringUtils.isBlank(amcNumber)) {
      return false;
    }
    return amcNumber.length() == 10;
  }

  private List<String> checkUnicityOK(@RequestBody DeclarantRequestDto declarant) {
    List<String> errorMessages = new ArrayList<>();
    if (declarant.getConventionTP() != null
        && !DeclarantUtils.checkUnicityConventionOK(declarant.getConventionTP())) {
      errorMessages.add("Erreur: Convention TP non unique");
    }
    if (declarant.getCodeRenvoiTP() != null
        && !DeclarantUtils.checkUnicityCodesRenvoiOK(declarant.getCodeRenvoiTP())) {
      errorMessages.add("Erreur: Code renvoi non unique");
    }
    if (declarant.getRegroupementDomainesTP() != null
        && !DeclarantUtils.checkUnicityRegroupementsDomainesTPOK(
            declarant.getRegroupementDomainesTP())) {
      errorMessages.add("Erreur: Regroupement des domaines TP non unique");
    }
    if (declarant.getFondCarteTP() != null
        && !DeclarantUtils.checkUnicityFondCarteTPOK(declarant.getFondCarteTP())) {
      errorMessages.add("Erreur: Fond de carte non unique");
    }

    return errorMessages;
  }

  @NewSpan
  @DeleteMapping(value = "/{idDeclarant}/services/{service}")
  public ResponseEntity<List<String>> deletePrestations(
      @PathVariable("idDeclarant") String idDeclarant, @PathVariable("service") String serv) {
    if ("true".equals(beyondPropertiesService.getPropertyOrThrowError(ALLOW_CLEAN_SERVICES))) {
      List<String> compteRendu =
          service.deletePrestations(Arrays.asList(idDeclarant.split(",")), serv);
      return new ResponseEntity<>(compteRendu, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }
}
