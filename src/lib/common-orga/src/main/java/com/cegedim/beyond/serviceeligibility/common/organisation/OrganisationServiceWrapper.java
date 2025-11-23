package com.cegedim.beyond.serviceeligibility.common.organisation;

import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public interface OrganisationServiceWrapper {

  @Nullable
  Organisation getOrganizationByAmcNumber(@NotNull String amcCode);

  @Nullable
  String getSecondaryOrganizationCodeByAmcNumber(@NotNull String amcCode);

  @Nullable
  boolean isOrgaAttached(String code, String amc);

  @Nullable
  Organisation getOrganisationByCode(String code);
}
