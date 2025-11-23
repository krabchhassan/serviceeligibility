package com.cegedim.next.serviceeligibility.core.services.scopeManagement;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.CLIENT_TYPE;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.spring.starter.managementscope.ServletManagementScopeService;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import java.util.Set;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationScopeHandler {

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
      log.info(
          "Performing action for authorized issuing companies: {}", authorizedIssuingCompanies);
      action.accept(authorizedIssuingCompanies);
    }
  }

  public boolean isAuthorized(ServicePrestationV6 servicePrestationV6) {
    String issuingCompany = getIssuingCompany(servicePrestationV6);
    if (issuingCompany != null) {
      log.debug("Authorizing ServicePrestationV6 for issuing company: {}", issuingCompany);

      boolean isAuthorized = managementScopeService.isAuthorizedForIssuingCompany(issuingCompany);
      log.debug("ServicePrestationV6 is authorized: {}", isAuthorized);
      return isAuthorized;
    }
    return false;
  }

  private String getIssuingCompany(ServicePrestationV6 servicePrestationV6) {
    log.debug("Getting issuing company for ServicePrestationV6: {}", servicePrestationV6);

    String result = null;
    if (CLIENT_TYPE_OTP.equals(beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
      result = servicePrestationV6.getIdDeclarant();
      log.debug("Client type is OTP, issuing company: {}", result);
    } else if (CLIENT_TYPE_INSURER.equals(
        beyondPropertiesService.getPropertyOrThrowError(CLIENT_TYPE))) {
      result = servicePrestationV6.getSocieteEmettrice();
      log.debug("Client type is INSURER, issuing company: {}", result);
    }

    log.debug("Issuing company determined: {}", result);
    return result;
  }
}
