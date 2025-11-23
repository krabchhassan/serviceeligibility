package com.cegedim.next.serviceeligibility.core.features.contract;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.business.declarant.service.RestContratService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import io.micrometer.tracing.annotation.NewSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/contrats")
public class ContractController {

  @Autowired private RestContratService service;

  /**
   * Resource REST qui renvoie le contrat de la collection contrats en fonction de l'id.
   *
   * @return la reponse avec le contrat.
   */
  @NewSpan
  @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ContractTP> getContratById(
      @RequestParam(value = "id", required = false) String id) {
    return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
  }
}
