package com.cegedim.next.serviceeligibility.core.features.declaration;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.*;
import com.cegedim.next.serviceeligibility.core.business.declaration.service.RestDeclarationService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/declarations")
public class DeclarationController {
  private final RestDeclarationService service;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<DeclarationDto>> getAll() {
    return new ResponseEntity<>(service.findAllDto(), HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(path = "{declarationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Declaration> getOne(
      @PathVariable("declarationId") @NonNull final String declarationId) {
    return new ResponseEntity<>(service.findById(declarationId), HttpStatus.OK);
  }
}
