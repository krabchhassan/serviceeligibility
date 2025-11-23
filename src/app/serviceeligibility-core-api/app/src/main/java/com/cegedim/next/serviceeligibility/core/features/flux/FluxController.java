package com.cegedim.next.serviceeligibility.core.features.flux;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.FluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux.ParametresFluxDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import io.micrometer.tracing.annotation.NewSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Consommateur des appels clients REST pour les Flux. */
@RestController
@RequestMapping("/v1")
public class FluxController {
  @Autowired private FluxService fluxService;

  /**
   * Renvoie en fonction des criteres passes en parametre un objet FluxDto qui contient une liste de
   * Flux et la taille de la liste.
   *
   * @param parametresflux Les criteres
   * @return {@link ResponseEntity}
   */
  @NewSpan
  @PostMapping(
      value = UriConstants.FLUX,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<FluxDto> getFlux(@RequestBody ParametresFluxDto parametresflux) {
    return new ResponseEntity<>(fluxService.getFlux(parametresflux), HttpStatus.OK);
  }
}
