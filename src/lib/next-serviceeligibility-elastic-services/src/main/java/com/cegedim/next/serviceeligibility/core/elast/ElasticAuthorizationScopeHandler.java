package com.cegedim.next.serviceeligibility.core.elast;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.spring.starter.managementscope.ManagementScopeConstant;
import com.cegedim.beyond.spring.starter.managementscope.ServletManagementScopeService;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticAuthorizationScopeHandler {

  private final ServletManagementScopeService managementScopeService;
  private final BeyondPropertiesService beyondPropertiesService;

  public String getClientType() {
    return beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE);
  }

  public void executeForAuthorizedIssuingCompanies(
      String clientType, Consumer<Set<String>> action) {
    if (!clientType.equals(beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
      log.info(
          "Client type is {} != {}, skipping this check",
          beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE),
          clientType);
    } else {
      Set<String> authorizedIssuingCompanies = managementScopeService.getAuthorizedIssuingCompany();

      if (authorizedIssuingCompanies.contains(ManagementScopeConstant.ALL)) {
        log.debug("Authorization scope includes 'ALL' companies. No filtering will be applied.");
      } else {
        log.debug(
            "Performing action for authorized issuing companies: {}", authorizedIssuingCompanies);
        action.accept(authorizedIssuingCompanies);
      }
    }
  }

  public boolean isAuthorized(BenefAIV5 beneficiary) {
    log.debug("Authorization check for BenefAIV5: {}", beneficiary);

    if (CLIENT_TYPE_OTP.equals(beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))
        && beneficiary.getAmc() != null) {
      log.debug(
          "Client type is OTP, checking authorization for AMC ID: {}",
          beneficiary.getAmc().getIdDeclarant());
      boolean isAuthorized =
          managementScopeService.isAuthorizedForIssuingCompany(
              beneficiary.getAmc().getIdDeclarant());
      log.info(
          "Authorization for AMC ID {}: {}", beneficiary.getAmc().getIdDeclarant(), isAuthorized);
      return isAuthorized;
    } else if (CLIENT_TYPE_INSURER.equals(
        beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
      return filterContracts(beneficiary);
    }

    log.warn("Authorization failed for BenefAIV5.");
    return false;
  }

  private boolean filterContracts(BenefAIV5 beneficiary) {
    log.debug("Filtering contracts for BenefAIV5: {}", beneficiary);

    List<ContratV5> contracts = beneficiary.getContrats();
    if (CollectionUtils.isNotEmpty(contracts)) {
      List<ContratV5> authorizedContracts =
          contracts.stream()
              .filter(
                  contratV5 ->
                      managementScopeService.isAuthorizedForIssuingCompany(
                          contratV5.getSocieteEmettrice()))
              .toList();

      beneficiary.setContrats(authorizedContracts);
      boolean hasAuthorizedContracts = CollectionUtils.isNotEmpty(beneficiary.getContrats());
      log.debug("Authorized contracts found: {}", hasAuthorizedContracts);
      return hasAuthorizedContracts;
    }

    log.warn("No contracts available for filtering.");
    return false;
  }
}
