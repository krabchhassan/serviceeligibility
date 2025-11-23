package com.cegedim.next.serviceeligibility.core.features.transcoParametrage;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscoParamDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.TranscoParametrageService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1" + UriConstants.PARAM_TRANSCODAGE)
public class TranscoParametrageController {

  @Autowired TranscoParametrageService service;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<TranscoParamDto>> getAllTranscoParam() {
    return new ResponseEntity<>(service.findAllTranscoParametrage(), HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      value = UriConstants.PARAM_TRANSCODAGE_BY_CODE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<TranscoParamDto> getTranscoParamByCode(@PathVariable @NonNull String code) {
    return new ResponseEntity<>(service.findTranscoParametrage(code), HttpStatus.OK);
  }

  @NewSpan
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<TranscoParamDto> createTranscoParam(
      @RequestBody TranscoParamDto transcoParamDto) {
    return new ResponseEntity<>(service.saveOrUpdate(transcoParamDto), HttpStatus.CREATED);
  }

  @NewSpan
  @DeleteMapping(value = UriConstants.PARAM_TRANSCODAGE_BY_CODE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> delete(@PathVariable @NonNull String code) {
    service.delete(code);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @NewSpan
  @PutMapping(value = UriConstants.PARAM_TRANSCODAGE_BY_CODE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<TranscoParamDto> updateTranscoParam(
      @RequestBody TranscoParamDto transcoParamDto) {
    return new ResponseEntity<>(service.saveOrUpdate(transcoParamDto), HttpStatus.OK);
  }
}
