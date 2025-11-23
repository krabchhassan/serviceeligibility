package com.cegedim.next.serviceeligibility.core.bobb.contract;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_CONTRACT_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bobb.gt.GTElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTResult;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContractElementControllerV2 {

  private final ContractElementService service;

  @PostMapping(
      value = Constants.GTLIST_V2,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @NewSpan
  @PreAuthorize(READ_CONTRACT_PERMISSION)
  public ResponseEntity<List<GTResult>> getGTs(@RequestBody final List<GTElement> elements) {
    return new ResponseEntity<>(service.getGTResultList(elements), HttpStatus.OK);
  }
}
