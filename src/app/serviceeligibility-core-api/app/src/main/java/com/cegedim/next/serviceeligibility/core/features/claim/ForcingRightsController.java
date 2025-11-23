package com.cegedim.next.serviceeligibility.core.features.claim;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractByBeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractsByBeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.dto.ContractRightsByBeneficiaryRequestDto;
import com.cegedim.next.serviceeligibility.core.features.utils.ContractRightsByBeneficiaryService;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestException;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import com.cegedim.next.serviceeligibility.core.services.claim.ForcingRightsService;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointResponse;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1")
public class ForcingRightsController {
  public static final String PAS_DE_CONTRAT_TROUVE = "Pas de contrat trouvé";

  private final ForcingRightsService forcingRightsService;
  private final String clientType;
  private final ContractRightsByBeneficiaryService contractRightsByBeneficiaryService;

  public ForcingRightsController(
      ForcingRightsService forcingRightsService,
      @Value("${CLIENT_TYPE:INSURER}") String clientType,
      ContractRightsByBeneficiaryService contractRightsByBeneficiaryService) {
    this.forcingRightsService = forcingRightsService;
    this.clientType = clientType;
    this.contractRightsByBeneficiaryService = contractRightsByBeneficiaryService;
  }

  @NewSpan
  @GetMapping("/contractsByBeneficiary")
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<ContractsByBeneficiaryDto> contractsByBeneficiary(
      @RequestParam final String beneficiaryId, @RequestParam final String context) {
    log.info(
        "Récupération des contrats d'un bénéficiaire pour forcer une liquidation de prestation");
    String[] benefIdSplitted =
        RequestValidator.validateRequestAndExtractBenefId(beneficiaryId, context);
    String insurerId = benefIdSplitted[0];
    String personNumber = benefIdSplitted[1];
    List<ContractByBeneficiaryDto> contractsByBeneficiaryDtos;

    if (ContextConstants.HTP.equals(context)) {
      contractsByBeneficiaryDtos =
          forcingRightsService.getContractsByBenefForHTP(insurerId, personNumber);
    } else {
      contractsByBeneficiaryDtos =
          forcingRightsService.getContractsByBenefForTP(
              insurerId, personNumber, context, clientType);
    }
    if (CollectionUtils.isEmpty(contractsByBeneficiaryDtos)) {
      final RestError restError =
          new RestError(
              RestErrorConstants.ERROR_CONTRACTS_BY_BENEF_NOT_FOUND,
              PAS_DE_CONTRAT_TROUVE,
              ExceptionLevel.ERROR);
      throw new RestException(PAS_DE_CONTRAT_TROUVE, restError, HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(new ContractsByBeneficiaryDto(contractsByBeneficiaryDtos));
  }

  @NewSpan
  @PostMapping("/contractRightsByBeneficiary")
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<UniqueAccessPointResponse> contractRightsByBeneficiary(
      @Valid @RequestBody final ContractRightsByBeneficiaryRequestDto requete) {
    return new ResponseEntity<>(
        contractRightsByBeneficiaryService.getContractRightsByBeneficiaryResponse(requete),
        HttpStatus.OK);
  }
}
