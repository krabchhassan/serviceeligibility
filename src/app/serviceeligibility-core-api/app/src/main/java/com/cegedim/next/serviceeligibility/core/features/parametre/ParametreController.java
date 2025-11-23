package com.cegedim.next.serviceeligibility.core.features.parametre;

import static com.cegedim.next.serviceeligibility.core.features.utils.ControllerUtils.prepareOutData;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.PARAMETER_CACHE_NAME;
import static com.cegedim.next.serviceeligibility.core.utils.ParametreControllerUtils.prepareInData;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CodesRenvoiDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresPrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CacheService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.utility.ParametersEnum;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.model.entity.Cache;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ParametreController {

  private static final String PROCESSUS = "processus";
  private static final String TYPE_FICHIERS = "typeFichiers";
  private static final String CONVENTIONNEMENT = "conventionnement";
  private static final String V2 = "/v2";

  private final BeyondPropertiesService beyondPropertiesService;
  private final ParametreBddService service;
  private final CacheService cacheService;

  @Qualifier("bddAuth")
  private final AuthenticationFacade authenticationFacade;

  @NewSpan
  @GetMapping(value = V2 + UriConstants.ALL_PROCESSUS, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Object> getProcesV2() {
    List<ParametresDto> parametres = service.findByType(PROCESSUS);
    String jsonResponse = prepareOutData(parametres);
    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      value = V2 + UriConstants.ALL_TYPE_FICHIER,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Object> getTypeFichierV2() {
    List<ParametresDto> parametres = service.findByType(TYPE_FICHIERS);
    String jsonResponse = prepareOutData(parametres);
    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      value = V2 + UriConstants.ALL_CONVENTIONS,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Object> getConventionsV2() {
    List<ParametresDto> parametres = service.findByType(CONVENTIONNEMENT);
    String jsonResponse = prepareOutData(parametres);
    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      value = V2 + UriConstants.PARAMETERS_BY_TYPE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Object> getParametersByTypeV2(
      @PathVariable("type") String type,
      @RequestParam(value = "domaine", required = false) String domaine) {
    if (type.equals("prestations") && StringUtils.isNotBlank(domaine)) {
      List<ParametresPrestationDto> parametres = new ArrayList<>();
      try {
        parametres = service.findPrestationsByDomaine(type, domaine);
      } catch (ValidationException | ExceptionService e) {
        throw new RequestValidationException(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(parametres, HttpStatus.OK);
    } else {
      List<ParametresDto> parametres;
      parametres = service.findByType(type);
      String jsonResponse = prepareOutData(parametres);
      return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
  }

  @NewSpan
  @GetMapping(
      value = V2 + UriConstants.ONE_PARAMETER_BY_TYPE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Object> getOneParameterByTypeV2(
      @PathVariable("type") String type, @PathVariable("code") String code) {
    ParametresDto parametre = service.findOneByType(type, code);
    if (parametre == null) {
      throw new RequestValidationException(
          "Le paramètre " + code + " n'existe pas!", HttpStatus.NOT_FOUND);
    }
    String jsonResponse = prepareOutData(parametre);
    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = V2 + UriConstants.ALL_REJETS, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Object> getRejetsV2() {
    Map<String, ErreursDto> rejets = service.findRejets();
    String jsonResponse = prepareOutData(rejets);
    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      value = V2 + UriConstants.ALL_CODES_RENVOI,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getCodesRenvoiV2() {
    Map<String, CodesRenvoiDto> codesRenvoi = service.findCodesRenvoi();
    String jsonResponse = prepareOutData(codesRenvoi);
    return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
  }

  private void saveOrUpdateCache(String type) {
    if (ParametersEnum.DOMAINE.getType().equals(type)
        || ParametersEnum.PRESTATIONS.getType().equals(type)) {
      Cache cache = new Cache();
      String user = this.authenticationFacade.getAuthenticationUserName();
      cache.setUserId(user);
      cache.setCacheName(beyondPropertiesService.getPropertyOrThrowError(PARAMETER_CACHE_NAME));
      cache.setChangeDate(new Date());
      cacheService.saveOrUpdate(cache);
    }
  }

  /**
   * Création de paramétrage en fonction du type
   *
   * @param type Type de paramètre
   * @param json Le body de création
   * @return Le paramètre créé
   * @throws IOException
   */
  @NewSpan
  @PostMapping(
      value = V2 + UriConstants.PARAMETERS_BY_TYPE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<ParametresDto> createV2(
      @PathVariable("type") String type, @RequestBody String json) throws IOException {
    ParametresDto parametresDto = prepareInData(type, json);
    try {
      service.saveOrUpdate(type, parametresDto, false, "V2");
      saveOrUpdateCache(type);
    } catch (ValidationException | ExceptionService e) {
      throw new RequestValidationException(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(parametresDto, HttpStatus.OK);
  }

  /**
   * Modification de paramétrage en fonction du type
   *
   * @param type Type de paramètre
   * @param json Le body de création
   * @return Le paramètre créé
   * @throws IOException
   */
  @NewSpan
  @PutMapping(
      value = V2 + UriConstants.PARAMETERS_BY_TYPE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Object> updateV2(
      @PathVariable("type") String type, @RequestBody String json) throws IOException {
    ParametresDto parametresDto = prepareInData(type, json);
    try {
      service.saveOrUpdate(type, parametresDto, true, "V2");
      saveOrUpdateCache(type);
    } catch (ValidationException e) {
      throw new RequestValidationException(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (ExceptionService e) {
      throw new RequestValidationException(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(parametresDto, HttpStatus.OK);
  }

  /**
   * Supression de paramétrage en fonction du type
   *
   * @param type Type de paramètre
   * @param code Le code du paramètre à supprimer
   * @return NO_CONTENT
   * @throws IOException
   */
  @NewSpan
  @DeleteMapping(
      value = V2 + UriConstants.ONE_PARAMETER_BY_TYPE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Object> deleteV2(
      @PathVariable("type") String type, @PathVariable("code") String code) {
    try {
      service.remove(type, code, "V2");
    } catch (ValidationException e) {
      throw new RequestValidationException(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (ExceptionService e) {
      throw new RequestValidationException(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
