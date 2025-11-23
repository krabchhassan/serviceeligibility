package com.cegedim.next.serviceeligibility.core.features.parametragecartetp;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ReferentielParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.services.ReferentielParametrageCarteTPService;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
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
@RequestMapping("/v1" + UriConstants.REFERENTIEL_PARAMETRAGE_CARTE_TP)
public class ReferentielParametrageCarteTPController {
  @Autowired ReferentielParametrageCarteTPService service;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<ReferentielParametrageCarteTP>> getByAmcs(
      @RequestParam List<String> amcs) {
    return new ResponseEntity<>(service.getByAmcs(amcs), HttpStatus.OK);
  }
}
