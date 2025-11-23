package com.cegedim.next.serviceeligibility.core.features.circuit;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CircuitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class CircuitController {
  @Autowired private CircuitService circuitService;

  @NewSpan
  @GetMapping(value = UriConstants.ALL_CIRCUITS, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<CircuitDto>> getAllCircuits() {
    return new ResponseEntity<>(circuitService.findAllDtoCircuits(), HttpStatus.OK);
  }
}
