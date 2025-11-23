package com.cegedim.next.serviceeligibility.core.features.servicedroit;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ServiceDroitsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ServiceDroitsService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ServiceDroitController {

  @Autowired private ServiceDroitsService service;

  @NewSpan
  @GetMapping(value = UriConstants.ALL_SERVICES_TRIES, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<ServiceDroitsDto>> getAllServicesNonFictif() {
    return new ResponseEntity<>(service.findCodesServiceTries(), HttpStatus.OK);
  }

  @NewSpan
  @PostMapping(
      value = UriConstants.ALL_SERVICES_TRIES,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<ServiceDroitsDto> save(@RequestBody ServiceDroitsDto serviceDroitsDto) {
    return new ResponseEntity<>(service.saveOrUpdate(serviceDroitsDto), HttpStatus.CREATED);
  }

  @NewSpan
  @PutMapping(
      value = UriConstants.SERVICE_BY_ID,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<ServiceDroitsDto> update(
      @PathVariable("id") @NonNull String id, @RequestBody ServiceDroitsDto serviceDroitsDto) {
    serviceDroitsDto.setId(id);
    return new ResponseEntity<>(service.saveOrUpdate(serviceDroitsDto), HttpStatus.OK);
  }

  @NewSpan
  @DeleteMapping(value = UriConstants.SERVICES_BY_NAME)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> deleteByName(@PathVariable("name") @NonNull String name) {
    ServiceDroitsDto serviceDroitsDto = service.findOneByCode(name);

    if (serviceDroitsDto != null && serviceDroitsDto.getNom() != null) {
      service.delete(serviceDroitsDto.getNom());
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
