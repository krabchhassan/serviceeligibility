package com.cegedim.next.serviceeligibility.core.features.contract;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.SUPPORT_BDDS;

import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/consolidation")
@RequiredArgsConstructor
public class ConsolidationController {

  private final DeclarationService declarationService;

  private final ContractTPService contractTPService;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  private final ContractService contractService;

  @PostMapping("/{idDeclarant}/{numContrat}/{numAdherent}")
  @NewSpan
  @PreAuthorize(SUPPORT_BDDS)
  public ResponseEntity<String> consolideContrat(
      @PathVariable String idDeclarant,
      @PathVariable String numContrat,
      @PathVariable String numAdherent) {
    List<Declaration> declarations =
        declarationService.findDeclarationsOfContratBenefAMC(idDeclarant, numAdherent, numContrat);
    if (!declarations.isEmpty()) {
      ContractTP oldContrat = contractService.getContract(idDeclarant, numContrat, numAdherent);

      for (Declaration declaration : declarations) {
        try {
          int res = contractTPService.processDeclaration(declaration);

          if (res == -2) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
      }

      if (oldContrat != null) {
        elasticHistorisationContractService.putContractHistoryOnElastic(oldContrat);
      }
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
