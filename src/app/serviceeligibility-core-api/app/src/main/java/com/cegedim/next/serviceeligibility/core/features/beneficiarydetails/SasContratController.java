package com.cegedim.next.serviceeligibility.core.features.beneficiarydetails;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sasContrats")
public class SasContratController {
  private final SasContratService sasContratService;

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(value = "/getSasContrats/{personNumber}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<String>> getSasContrats(
      @PathVariable("personNumber") @NonNull final String personNumber) {
    List<String> allSasContrats = sasContratService.getByPersonNumber(personNumber);
    return new ResponseEntity<>(allSasContrats, HttpStatus.OK);
  }
}
