package com.cegedim.next.serviceeligibility.core.elast;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.Version;
import com.fasterxml.jackson.annotation.JsonView;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1")
public class AdvancedElasticController {

  @Autowired private BenefElasticService benefElasticService;

  @NewSpan
  @PostMapping(value = "/advancedSearchBeneficiaries")
  @JsonView(Version.Advanced.class)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<BenefElasticPageResult> search(
      @RequestBody BenefAdvancedSearchRequest request) {
    try {
      return new ResponseEntity<>(benefElasticService.search(request), HttpStatus.OK);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }
}
