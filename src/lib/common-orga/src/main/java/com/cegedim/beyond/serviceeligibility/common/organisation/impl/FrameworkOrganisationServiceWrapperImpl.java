package com.cegedim.beyond.serviceeligibility.common.organisation.impl;

import com.cegedim.beyond.business.organisation.facade.OrganisationService;
import com.cegedim.beyond.serviceeligibility.common.exception.OrganisationWrapperException;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.integrationpoints.PublishedCatalog;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Implementation specific to new OrganisationService from beyond framework */
@Slf4j
@RequiredArgsConstructor
public class FrameworkOrganisationServiceWrapperImpl implements OrganisationServiceWrapper {

  private final OrganisationService organisationService;
  private final ObjectMapper objectMapper;

  @Nullable
  @Override
  public Organisation getOrganizationByAmcNumber(@NotNull String amcCode) {
    return organisationService.getOrganizationByNumAmc(amcCode);
  }

  @Override
  public String getSecondaryOrganizationCodeByAmcNumber(@NotNull String amcCode) {
    Organisation organizationByNumAmc = organisationService.getOrganizationByNumAmc(amcCode);
    if (organizationByNumAmc != null && !organizationByNumAmc.isMain()) {
      return organizationByNumAmc.getCode();
    } else {
      return null;
    }
  }

  @Override
  public boolean isOrgaAttached(String code, String amc) {
    return organisationService.isOrgaAttached(code, amc);
  }

  @Override
  public Organisation getOrganisationByCode(String code) {
    return organisationService.getOrganizationByCode(code);
  }

  @NotNull
  private String getConfiguration(
      Set<PublishedCatalog> publishedCatalogs,
      @NotNull String catalogCode,
      @Nullable String organisationCode) {
    return publishedCatalogs.stream()
        .filter(
            publishedCatalogInfo ->
                catalogCode.equals(publishedCatalogInfo.getIntegrationPointCode()))
        .findFirst()
        .map(
            publishedCatalogInfo -> {
              try {
                return objectMapper.readValue(
                    publishedCatalogInfo.getConfiguration(),
                    new TypeReference<HashMap<String, Object>>() {});
              } catch (JsonProcessingException e) {
                throw new OrganisationWrapperException(
                    "invalid catalog format for key %s and organization %s"
                        .formatted(catalogCode, organisationCode),
                    e);
              }
            })
        .map(map -> (String) map.get("url"))
        .orElseThrow(
            () ->
                new OrganisationWrapperException(
                    "no catalog %s for organization %s".formatted(catalogCode, organisationCode)));
  }
}
