package com.cegedim.next.serviceeligibility.core.bobb.contract;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.ALLOW_TESTING_ENDPOINTS;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.GT_SEARCH_LIMIT;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_CONTRACT_PERMISSION;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTResult;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/contractelement")
public class ContractElementController {

  public static final String ACCESS_TO_THIS_ENDPOINT_IS_FORBIDDEN =
      "Access to this endpoint is forbidden.";
  private final BeyondPropertiesService beyondPropertiesService;
  private final ContractElementService service;
  private final AuthenticationFacade authenticationFacade;

  public ContractElementController(
      BeyondPropertiesService beyondPropertiesService,
      ContractElementService service,
      AuthenticationFacade authenticationFacade) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.service = service;
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  @NewSpan
  public void create(@RequestBody final ContractElement element) {
    if ("true".equals(beyondPropertiesService.getPropertyOrThrowError(ALLOW_TESTING_ENDPOINTS))) {
      try {
        String keycloakUser = authenticationFacade.getAuthenticationUserName();
        element.setOrigine("API");
        element.setUser(keycloakUser);
        service.create(element);
      } catch (Exception ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
      }
    } else {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_TO_THIS_ENDPOINT_IS_FORBIDDEN);
    }
  }

  @Deprecated
  @GetMapping(value = "/gtlist", produces = MediaType.APPLICATION_JSON_VALUE)
  @NewSpan
  @PreAuthorize(READ_CONTRACT_PERMISSION)
  public ResponseEntity<List<GTResult>> getGTs(@RequestBody final List<GTElement> elements) {
    return new ResponseEntity<>(service.getGTResultList(elements), HttpStatus.OK);
  }

  @GetMapping(value = "/garanties")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public List<GarantieTechnique> getAllGarantieTechniques(
      @RequestParam(required = false) String search) {
    return service.getAllGarantieTechniques(
        search, beyondPropertiesService.getLongPropertyOrThrowError(GT_SEARCH_LIMIT));
  }
}
