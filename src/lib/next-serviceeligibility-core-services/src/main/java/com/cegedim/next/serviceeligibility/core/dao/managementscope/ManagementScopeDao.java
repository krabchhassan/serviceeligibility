package com.cegedim.next.serviceeligibility.core.dao.managementscope;

import com.cegedim.beyond.spring.starter.managementscope.ManagementScopeConstant;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import java.security.SecureRandom;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementScopeDao {

  protected final AuthorizationScopeHandler authorizationScopeHandler;

  protected void applyIssuingCompanyFilter(
      String code, Criteria criteria, String field, String clientType, boolean authNeeded) {

    if (clientType.equals(authorizationScopeHandler.getClientType())) {

      boolean codeNotBlank = StringUtils.isNotBlank(code);

      if (authNeeded) {
        addCriteriaWithManagementScope(code, criteria, field, clientType, codeNotBlank);
      } else if (codeNotBlank) {
        criteria.and(field).is(code);
      } else {
        log.debug("Skipping criteria because issuing company is empty");
      }
    } else {
      log.debug(
          "Skipping criteria because not wanted for client type {}",
          authorizationScopeHandler.getClientType());
    }
  }

  private void addCriteriaWithManagementScope(
      String code, Criteria criteria, String field, String clientType, boolean codeNotBlank) {
    authorizationScopeHandler.executeForAuthorizedIssuingCompanies(
        clientType,
        authorizedIssuingCompanies -> {
          boolean authorizedAll = authorizedIssuingCompanies.contains(ManagementScopeConstant.ALL);
          if (isEqualToCode(codeNotBlank, authorizedAll, authorizedIssuingCompanies, code)) {
            criteria.and(field).is(code);
          } else if (!authorizedAll) {
            if (codeNotBlank) {
              SecureRandom rdm = new SecureRandom();
              criteria.and(field).is("NOT_AUTHORIZED_" + rdm.nextInt());
            } else {
              criteria.and(field).in(authorizedIssuingCompanies);
            }
          } else {
            log.debug(
                "Skipping criteria because everything is authorized but issuing company is empty");
          }
        });
  }

  private static boolean isEqualToCode(
      boolean companyNotBlank,
      boolean authorizedAll,
      Set<String> authorizedIssuingCompanies,
      String issuingCompanyCode) {
    return companyNotBlank
        && (authorizedAll || authorizedIssuingCompanies.contains(issuingCompanyCode));
  }

  protected void applyIssuingCompanyFilter(
      String issuingCompanyCode, Criteria criteria, String issuingCompanyField, String clientType) {
    applyIssuingCompanyFilter(issuingCompanyCode, criteria, issuingCompanyField, clientType, true);
  }
}
