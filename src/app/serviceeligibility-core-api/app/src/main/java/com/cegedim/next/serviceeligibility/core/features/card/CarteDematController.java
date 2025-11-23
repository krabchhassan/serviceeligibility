package com.cegedim.next.serviceeligibility.core.features.card;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.business.carte.service.RestCardService;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideService;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cards")
public class CarteDematController {
  @Autowired private RestCardService service;

  @Autowired private DeclarationConsolideService declarationConsolideService;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<CarteDemat>> getAll() {
    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(path = "{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<CarteDemat> getOne(@PathVariable("cardId") @NonNull final String id) {
    return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
  }

  @NewSpan
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<CarteDemat> create(@RequestBody @Valid CarteDemat carte) {
    service.create(carte);
    CarteDemat createdCarte = service.findById(carte.get_id());
    return new ResponseEntity<>(createdCarte, HttpStatus.CREATED);
  }
}
