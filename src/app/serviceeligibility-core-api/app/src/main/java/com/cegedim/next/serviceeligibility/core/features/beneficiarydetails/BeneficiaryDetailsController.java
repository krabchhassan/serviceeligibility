package com.cegedim.next.serviceeligibility.core.features.beneficiarydetails;

import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.AttestationsContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.BeneficiaryDetailsDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.ConsolidatedContractHistory;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.DeclarationDetailsDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryDetailsService;
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
@RequestMapping("/v1")
public class BeneficiaryDetailsController {
  @Autowired private BeneficiaryDetailsService beneficiaryDetailsService;

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(
      value = UriConstants.BENEFICIARY_TP_DETAILS,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BeneficiaryDetailsDto> getBeneficiaryTpDetails(
      String personNumber,
      String insurerId,
      @RequestParam(required = false) List<String> declarationsIds,
      String contractNumber,
      String subscriberNumber,
      String environment) {
    beneficiaryDetailsService.validateRequestTP(
        personNumber, insurerId, declarationsIds, contractNumber);

    BeneficiaryDetailsDto beneficiaryDetails =
        beneficiaryDetailsService.getResponse(
            personNumber, insurerId, declarationsIds, contractNumber, subscriberNumber);

    return new ResponseEntity<>(beneficiaryDetails, HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(
      value = UriConstants.BENEFICIARY_TP_DETAILS + UriConstants.NEXT_CERTIFICATIONS,
      produces = MediaType.APPLICATION_JSON_VALUE)
  // params 'environment' et 'requestId' utilisés par la FAAS otp-detail
  public ResponseEntity<AttestationsContractDto> getNextCertifications(
      int startIndex,
      String idDeclarant,
      String numContrat,
      String personNumber,
      @RequestParam(required = false) String environment,
      @RequestParam(required = false) String requestId) {

    AttestationsContractDto nextCertifications =
        beneficiaryDetailsService.getNextCertifications(
            startIndex, idDeclarant, numContrat, personNumber);

    return new ResponseEntity<>(nextCertifications, HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(
      value = UriConstants.BENEFICIARY_TP_DETAILS + UriConstants.NEXT_CONSOLIDATED_CONTRATS_TP,
      produces = MediaType.APPLICATION_JSON_VALUE)
  // param 'requestId' utilisé par la FAAS otp-detail
  public ResponseEntity<ConsolidatedContractHistory> getNextConsolidatedContratsTP(
      int startIndex,
      String amcId,
      String contractNumber,
      String subscriberId,
      String personNumber,
      String environment,
      @RequestParam(required = false) String requestId) {
    ConsolidatedContractHistory nextConsolidatedServicePrest =
        beneficiaryDetailsService.getNextConsolidatedContratsTP(
            startIndex, amcId, contractNumber, subscriberId, personNumber);

    return new ResponseEntity<>(nextConsolidatedServicePrest, HttpStatus.OK);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(
      value = UriConstants.BENEFICIARY_TP_DETAILS + UriConstants.NEXT_DECLARATIONS,
      produces = MediaType.APPLICATION_JSON_VALUE)
  // params 'environment' et 'requestId' utilisés par la FAAS otp-detail
  public ResponseEntity<DeclarationDetailsDto> getNextDeclarations(
      int startIndex,
      int startIndexRestit,
      String idDeclarant,
      String numContrat,
      String subscriberNumber,
      String personNumber,
      @RequestParam(required = false) String environment,
      @RequestParam(required = false) String requestId) {

    DeclarationDetailsDto nextDeclarations =
        beneficiaryDetailsService.getNextDeclarations(
            startIndex, startIndexRestit, idDeclarant, numContrat, subscriberNumber, personNumber);

    return new ResponseEntity<>(nextDeclarations, HttpStatus.OK);
  }
}
