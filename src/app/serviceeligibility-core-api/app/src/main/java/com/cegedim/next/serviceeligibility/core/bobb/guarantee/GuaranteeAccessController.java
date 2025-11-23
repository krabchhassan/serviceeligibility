package com.cegedim.next.serviceeligibility.core.bobb.guarantee;

import com.cegedim.next.serviceeligibility.core.bobb.*;
import com.cegedim.next.serviceeligibility.core.bobb.services.AccessTokenService;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/guarantee")
public class GuaranteeAccessController {
  private final AccessTokenService tokenService;

  public GuaranteeAccessController(AccessTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/access-url")
  @NewSpan
  public ResponseEntity<GuaranteeAccessResponse> createCorrespondenceAccess(
      @RequestBody @Valid GuaranteeAccessRequest guaranteeAccessRequest) {
    GuaranteeAccessResponse body = tokenService.getGuaranteeAccessInfo(guaranteeAccessRequest);
    if (body == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @PostMapping("/creation-access-url")
  @NewSpan
  public ResponseEntity<GuaranteeCreationAccessResponse> createCreationCorrespondenceAccess(
      @Valid @RequestBody GuaranteeAccessRequest guaranteeCreationAccessRequest) {
    GuaranteeCreationAccessResponse body =
        tokenService.getGuaranteeCreationAccessInfo(guaranteeCreationAccessRequest);
    if (body == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @GetMapping("/visualisation/{contextKey}")
  public ResponseEntity<GuaranteeVisualisationResponse> getGtIdByContextKey(
      @PathVariable String contextKey) {
    return ResponseEntity.ok(tokenService.getGtIdByContextKey(contextKey));
  }

  @GetMapping("/creation/{contextKey}")
  public ResponseEntity<GuaranteeCreationResponse> getGuaranteeInfoByContextKey(
      @PathVariable String contextKey) {
    return ResponseEntity.ok(tokenService.getGuaranteeInfoByContextKey(contextKey));
  }
}
