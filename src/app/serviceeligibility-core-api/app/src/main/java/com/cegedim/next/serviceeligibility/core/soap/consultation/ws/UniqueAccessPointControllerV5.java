package com.cegedim.next.serviceeligibility.core.soap.consultation.ws;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireBackendDao;
import com.cegedim.next.serviceeligibility.core.dao.managementscope.ManagementScopeDao;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointResponse;
import io.micrometer.tracing.annotation.NewSpan;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UniqueAccessPointControllerV5 {

  private static final Logger LOGGER = LoggerFactory.getLogger(UniqueAccessPointControllerV5.class);
  BeyondPropertiesService beyondPropertiesService;
  UniqueAccessPointService uniqueAccessPointServiceHTPV5;
  UniqueAccessPointService uniqueAccessPointServiceTPOnlineV5;
  UniqueAccessPointService uniqueAccessPointServiceTPOfflineV5;

  BeneficiaireBackendDao beneficiaireTPOfflineBackendDao;
  BeneficiaireBackendDao beneficiaireTPOnlineBackendDao;
  BeneficiaireBackendDao beneficiaireHTPBackendDao;

  private DeclarantBackendService declarantService;

  private ManagementScopeDao managementScopeDao;

  private String clientType;

  public UniqueAccessPointControllerV5(
      @Qualifier("beyondPropertiesService") final BeyondPropertiesService beyondPropertiesService,
      @Qualifier("uniqueAccessPointServiceHTPV5")
          final UniqueAccessPointService uniqueAccessPointServiceHTPV5,
      @Qualifier("uniqueAccessPointServiceTpOnlineV5")
          final UniqueAccessPointService uniqueAccessPointServiceTPOnlineV5,
      @Qualifier("uniqueAccessPointServiceTpOfflineV5")
          final UniqueAccessPointService uniqueAccessPointServiceTPOfflineV5,
      @Qualifier("beneficiaireTPOfflineBackendDao")
          final BeneficiaireBackendDao beneficiaireTPOfflineBackendDao,
      @Qualifier("beneficiaireTPOnlineBackendDao")
          final BeneficiaireBackendDao beneficiaireTPOnlineBackendDao,
      @Qualifier("beneficiaireHTPBackendDao")
          final BeneficiaireBackendDao beneficiaireHTPBackendDao,
      DeclarantBackendService declarantService,
      ManagementScopeDao managementScopeDao) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.uniqueAccessPointServiceTPOnlineV5 = uniqueAccessPointServiceTPOnlineV5;
    this.uniqueAccessPointServiceTPOfflineV5 = uniqueAccessPointServiceTPOfflineV5;
    this.uniqueAccessPointServiceHTPV5 = uniqueAccessPointServiceHTPV5;
    this.beneficiaireTPOfflineBackendDao = beneficiaireTPOfflineBackendDao;
    this.beneficiaireTPOnlineBackendDao = beneficiaireTPOnlineBackendDao;
    this.beneficiaireHTPBackendDao = beneficiaireHTPBackendDao;
    this.declarantService = declarantService;
    this.managementScopeDao = managementScopeDao;
    this.clientType = beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE);
  }

  @NewSpan
  @PreAuthorize(READ_PERMISSION)
  @GetMapping(value = "/v5/beneficiaryContracts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UniqueAccessPointResponse> getDroitsV5(
      @RequestParam(required = false) final String startDate,
      @RequestParam(required = false) final String context,
      @RequestParam(required = false) final String insurerId,
      @RequestParam(required = false) final String nirCode,
      @RequestParam(required = false) final String birthDate,
      @RequestParam(required = false) final String birthRank,
      @RequestParam(required = false) final String endDate,
      @RequestParam(required = false) final String subscriberId,
      @RequestParam(required = false) final String contractNumber,
      @RequestParam(required = false) String domains,
      @RequestParam(required = false) final String beneficiaryId,
      @RequestParam(required = false) final String issuingCompanyCode,
      @RequestParam(required = false) final Boolean isForced) {
    LOGGER.info("Recherche PAU contrat v5");
    final UniqueAccessPointResponse uniqueAccessPointResponse =
        getUniqueAccessPointResponse(
            startDate,
            context,
            insurerId,
            nirCode,
            birthDate,
            birthRank,
            endDate,
            subscriberId,
            contractNumber,
            domains,
            beneficiaryId,
            issuingCompanyCode,
            isForced);
    return new ResponseEntity<>(uniqueAccessPointResponse, HttpStatus.OK);
  }

  private UniqueAccessPointResponse getUniqueAccessPointResponse(
      String startDate,
      String context,
      String insurerId,
      String nirCode,
      String birthDate,
      String birthRank,
      String endDate,
      String subscriberId,
      String contractNumber,
      String domains,
      String beneficiaryId,
      String issuingCompanyCode,
      Boolean isForced) {
    // BLUE-4837 - Transcode domaines TP pour PAU V5
    domains = this.declarantService.transcodeDomain(insurerId, domains);

    return prepareAndExecuteRequestV5(
        nirCode,
        birthDate,
        birthRank,
        startDate,
        endDate,
        insurerId,
        subscriberId,
        contractNumber,
        context,
        domains,
        beneficiaryId,
        issuingCompanyCode,
        isForced);
  }

  private UniqueAccessPointResponse prepareAndExecuteRequestV5(
      final String nirCode,
      final String birthDate,
      final String birthRank,
      final String startDate,
      final String endDate,
      final String insurerId,
      final String subscriberId,
      final String contractNumber,
      final String context,
      final String domains,
      final String beneficiaryId,
      String issuingCompanyCode,
      final Boolean isForced) {
    if (StringUtils.isNotBlank(issuingCompanyCode)
        && Constants.CLIENT_TYPE_OTP.equals(clientType)) {
      issuingCompanyCode = null;
    }
    final UniqueAccessPointRequestV5 request =
        new UniqueAccessPointRequestV5(
            nirCode,
            birthDate,
            birthRank,
            startDate,
            endDate,
            insurerId,
            subscriberId,
            contractNumber,
            context,
            domains,
            beneficiaryId,
            issuingCompanyCode,
            clientType,
            isForced != null ? isForced : false);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Request : {}", request.toString().replaceAll("[\n\r]", "_"));
    }
    RequestValidator.validateRequestUAP(request);
    final UniqueAccessPointResponse uniqueAccessPointResponse;
    if (ContextConstants.TP_ONLINE.equals(request.getContext())) {
      uniqueAccessPointResponse = this.uniqueAccessPointServiceTPOnlineV5.execute(request);
    } else if (ContextConstants.TP_OFFLINE.equals(request.getContext())) {
      uniqueAccessPointResponse = this.uniqueAccessPointServiceTPOfflineV5.execute(request);
    } else {
      uniqueAccessPointResponse = this.uniqueAccessPointServiceHTPV5.execute(request);
    }
    if (uniqueAccessPointResponse.getContracts() != null) {
      uniqueAccessPointResponse
          .getContracts()
          .forEach(genericRightDto -> genericRightDto.setIssuingCompanyCode(null));
    }
    return uniqueAccessPointResponse;
  }

  @NewSpan
  @GetMapping(value = "/v5/testPAUOTP", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UniqueAccessPointResponse> getPauOtp(
      @RequestParam(required = false) final String startDate,
      @RequestParam(required = false) final String context,
      @RequestParam(required = false) final String insurerId,
      @RequestParam(required = false) final String nirCode,
      @RequestParam(required = false) final String birthDate,
      @RequestParam(required = false) final String birthRank,
      @RequestParam(required = false) final String endDate,
      @RequestParam(required = false) final String subscriberId,
      @RequestParam(required = false) final String contractNumber,
      @RequestParam(required = false) String domains,
      @RequestParam(required = false) final String beneficiaryId,
      @RequestParam(required = false) final String issuingCompanyCode,
      @RequestParam(required = false) final Boolean isForced) {
    if ("true".equals(beyondPropertiesService.getPropertyOrThrowError(ALLOW_TESTING_ENDPOINTS))) {
      final UniqueAccessPointResponse uniqueAccessPointResponse =
          getUniqueAccessPointResponseTestOTP(
              startDate,
              context,
              insurerId,
              nirCode,
              birthDate,
              birthRank,
              endDate,
              subscriberId,
              contractNumber,
              domains,
              beneficiaryId,
              issuingCompanyCode,
              isForced);
      return new ResponseEntity<>(uniqueAccessPointResponse, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  private UniqueAccessPointResponse getUniqueAccessPointResponseTestOTP(
      String startDate,
      String context,
      String insurerId,
      String nirCode,
      String birthDate,
      String birthRank,
      String endDate,
      String subscriberId,
      String contractNumber,
      String domains,
      String beneficiaryId,
      String issuingCompanyCode,
      Boolean isForced) {
    // BLUE-4837 - Transcode domaines TP pour PAU V5
    domains = this.declarantService.transcodeDomain(insurerId, domains);
    final UniqueAccessPointResponse uniqueAccessPointResponse =
        this.prepareAndExecuteRequestV5TestOTP(
            nirCode,
            birthDate,
            birthRank,
            startDate,
            endDate,
            insurerId,
            subscriberId,
            contractNumber,
            context,
            domains,
            beneficiaryId,
            issuingCompanyCode,
            isForced);

    if (CollectionUtils.isNotEmpty(uniqueAccessPointResponse.getContracts())
        && ContextConstants.TP_OFFLINE.equals(context)) {
      uniqueAccessPointResponse
          .getContracts()
          .forEach(
              genericRightV4Dto ->
                  genericRightV4Dto
                      .getInsured()
                      .getRights()
                      .forEach(
                          rightV4 ->
                              rightV4
                                  .getProducts()
                                  .forEach(
                                      productV4 ->
                                          productV4
                                              .getBenefitsType()
                                              .forEach(
                                                  benefitType -> benefitType.setDomains(null)))));
    }
    return uniqueAccessPointResponse;
  }

  private UniqueAccessPointResponse prepareAndExecuteRequestV5TestOTP(
      final String nirCode,
      final String birthDate,
      final String birthRank,
      final String startDate,
      final String endDate,
      final String insurerId,
      final String subscriberId,
      final String contractNumber,
      final String context,
      final String domains,
      final String beneficiaryId,
      String issuingCompanyCode,
      final Boolean isForced) {
    if (StringUtils.isNotBlank(issuingCompanyCode)) {
      issuingCompanyCode = null;
    }
    final UniqueAccessPointRequestV5 request =
        new UniqueAccessPointRequestV5(
            nirCode,
            birthDate,
            birthRank,
            startDate,
            endDate,
            insurerId,
            subscriberId,
            contractNumber,
            context,
            domains,
            beneficiaryId,
            issuingCompanyCode,
            Constants.CLIENT_TYPE_OTP,
            isForced != null ? isForced : false);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Request : {}", request.toString().replaceAll("[\n\r]", "_"));
    }
    RequestValidator.validateRequestUAP(request);
    final UniqueAccessPointResponse uniqueAccessPointResponse;
    if (ContextConstants.TP_ONLINE.equals(request.getContext())) {
      uniqueAccessPointResponse = this.uniqueAccessPointServiceTPOnlineV5.execute(request);
    } else if (ContextConstants.TP_OFFLINE.equals(request.getContext())) {
      uniqueAccessPointResponse = this.uniqueAccessPointServiceTPOfflineV5.execute(request);
    } else {
      uniqueAccessPointResponse = this.uniqueAccessPointServiceHTPV5.execute(request);
    }
    if (uniqueAccessPointResponse.getContracts() != null) {
      uniqueAccessPointResponse
          .getContracts()
          .forEach(genericRightDto -> genericRightDto.setIssuingCompanyCode(null));
    }
    return uniqueAccessPointResponse;
  }
}
