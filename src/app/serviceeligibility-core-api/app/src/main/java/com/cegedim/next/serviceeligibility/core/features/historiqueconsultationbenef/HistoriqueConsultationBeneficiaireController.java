package com.cegedim.next.serviceeligibility.core.features.historiqueconsultationbenef;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.RechercheInfosBeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.services.bdd.RestBeneficiaireService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/historiqueBeneficiaires")
public class HistoriqueConsultationBeneficiaireController {
  @Autowired private RestBeneficiaireService service;

  @Autowired
  @Qualifier("bddAuth")
  private AuthenticationFacade authenticationFacade;

  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<RechercheInfosBeneficiaireDto> getHistory() {
    String keycloakUser = this.authenticationFacade.getAuthenticationUserName();

    List<Pair<Boolean, String>> benefHistory = service.findIdHistory(keycloakUser);
    RechercheInfosBeneficiaireDto infosBeneficiaires = new RechercheInfosBeneficiaireDto();
    infosBeneficiaires.setBeneficiaires(benefHistory);
    infosBeneficiaires.setTotalBeneficiaires(benefHistory.size());

    return new ResponseEntity<>(infosBeneficiaires, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(
      path = "{elasticSearchBeneficiaryId}/{env}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<Void> saveOne(
      @PathVariable("elasticSearchBeneficiaryId") @NonNull final String id,
      @PathVariable("env") @NonNull final String env) {
    String keycloakUser = this.authenticationFacade.getAuthenticationUserName();
    service.addHistory(id, env, keycloakUser);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
