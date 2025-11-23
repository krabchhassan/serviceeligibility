package com.cegedim.next.serviceeligibility.core.soap.consultation.ws;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.RequestValidator.validateRequestBddstoBlb;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointService;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceTPImpl;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalIssuingCompanyCodeException;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.BddsPriorityTpContractToBlbResponse;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Domain;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.GenericRightDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointResponse;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BddsPriorityTpContractToBlbController {

  private final UniqueAccessPointService uniqueAccessPointServiceTPOnlineV5;
  private final UniqueAccessPointService uniqueAccessPointServiceTPOfflineV5;

  private final DeclarantBackendService declarantService;

  BeyondPropertiesService beyondPropertiesService;

  public BddsPriorityTpContractToBlbController(
      @Qualifier("beyondPropertiesService") final BeyondPropertiesService beyondPropertiesService,
      @Qualifier("uniqueAccessPointServiceTpOnlineV5")
          final UniqueAccessPointService uniqueAccessPointServiceTPOnlineV5,
      @Qualifier("uniqueAccessPointServiceTpOfflineV5")
          final UniqueAccessPointService uniqueAccessPointServiceTPOfflineV5,
      @Autowired final DeclarantBackendService declarantService) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.uniqueAccessPointServiceTPOnlineV5 = uniqueAccessPointServiceTPOnlineV5;
    this.uniqueAccessPointServiceTPOfflineV5 = uniqueAccessPointServiceTPOfflineV5;
    this.declarantService = declarantService;
  }

  @GetMapping(value = "/v1/priorityTpContract", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  @NewSpan
  public BddsPriorityTpContractToBlbResponse getBddsPriorityTpContractToBlb(
      @RequestParam() final String nirCode,
      @RequestParam() final String birthDate,
      @RequestParam() final String birthRank,
      @RequestParam() final String startDate,
      @RequestParam() final String context,
      @RequestParam(required = false) final String subscriberId,
      @RequestParam(required = false) final String domain) {
    return this.prepareAndExecuteRequest(
        nirCode, birthDate, birthRank, startDate, context, subscriberId, domain);
  }

  @Nullable
  private BddsPriorityTpContractToBlbResponse prepareAndExecuteRequest(
      @NotNull final String nirCode,
      @NotNull final String birthDate,
      @NotNull final String birthRank,
      @NotNull final String startDate,
      @NotNull final String context,
      @Nullable final String subscriberId,
      @Nullable final String domain) {
    final UniqueAccessPointRequestV5 request =
        new UniqueAccessPointRequestV5(
            nirCode,
            birthDate,
            birthRank,
            startDate,
            null,
            null,
            subscriberId,
            null,
            context,
            null,
            null,
            null,
            beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE),
            false);
    if (log.isDebugEnabled()) {
      log.debug("request : {}", request);
    }
    validateRequestBddstoBlb(request);
    final UniqueAccessPointResponse response;
    try {
      if (ContextConstants.TP_ONLINE.equals(request.getContext())) {
        response = this.uniqueAccessPointServiceTPOnlineV5.execute(request);
      } else {
        response = this.uniqueAccessPointServiceTPOfflineV5.execute(request);
      }
      if (response == null || response.getContracts().isEmpty()) {
        // 5911
        throw new UAPFunctionalIssuingCompanyCodeException(
            "Aucun contrat connu",
            HttpStatus.NOT_FOUND,
            RestErrorConstants.ERROR_CODE_PAU_SERVICE_PRESTATION_NOT_FOUND);
      }
      if (StringUtils.isNotBlank(domain)
          && !ContextConstants.TP_OFFLINE.equals(request.getContext())) {
        // 5911
        return this.filterDomain(response.getContracts(), domain).stream()
            .min(Comparator.comparing(GenericRightDto::getPrioritizationOrder))
            .map(this::convert)
            .orElseThrow(
                () ->
                    new UAPFunctionalIssuingCompanyCodeException(
                        String.format("Aucun contrat connu pour le domaine %s", domain),
                        HttpStatus.NOT_FOUND,
                        RestErrorConstants.ERROR_CODE_PAU_SERVICE_PRESTATION_NOT_FOUND));
      }
      return response.getContracts().stream()
          .filter(rightDto -> "1".equals(rightDto.getPrioritizationOrder()))
          .findFirst()
          .map(this::convert)
          .orElse(null);
    } catch (UAPFunctionalIssuingCompanyCodeException e) {
      // 5911
      Triple<String, String, String> result =
          ((UniqueAccessPointServiceTPImpl) this.uniqueAccessPointServiceTPOnlineV5)
              .executeBis(request);
      if (result != null) {
        return BddsPriorityTpContractToBlbResponse.builder()
            .issuingCompanyCode(result.getLeft())
            .insurerId(result.getMiddle())
            .beneficiaryId(result.getRight())
            .build();
      } else {
        throw new UAPFunctionalException(
            "Aucun contrat connu",
            HttpStatus.NOT_FOUND,
            RestErrorConstants.ERROR_CODE_PAU_SERVICE_PRESTATION_NOT_FOUND);
      }
    }
  }

  protected List<GenericRightDto> filterDomain(
      final List<GenericRightDto> contracts, final String domain) {
    return contracts.parallelStream()
        .filter(contrat -> this.filterContractByDomain(contrat, domain))
        .toList();
  }

  private boolean filterContractByDomain(final GenericRightDto contract, final String domain) {
    final Set<String> contractsDomiansList =
        contract.getInsured().getRights().stream()
            .filter(right -> CollectionUtils.isNotEmpty(right.getProducts()))
            .flatMap(right -> right.getProducts().stream())
            .filter(product -> CollectionUtils.isNotEmpty(product.getBenefitsType()))
            .flatMap(product -> product.getBenefitsType().stream())
            .filter(benefitType -> CollectionUtils.isNotEmpty(benefitType.getDomains()))
            .flatMap(benefitType -> benefitType.getDomains().stream())
            .map(Domain::getDomainCode)
            .collect(Collectors.toSet());
    return Util.stringToList(this.declarantService.transcodeDomain(contract.getInsurerId(), domain))
        .stream()
        .anyMatch(contractsDomiansList::contains);
  }

  private BddsPriorityTpContractToBlbResponse convert(final GenericRightDto rightDto) {
    return BddsPriorityTpContractToBlbResponse.builder()
        .beneficiaryId(rightDto.getInsured().getIdentity().getPersonNumber())
        .insurerId(rightDto.getInsurerId())
        .issuingCompanyCode(rightDto.getIssuingCompanyCode())
        .build();
  }
}
