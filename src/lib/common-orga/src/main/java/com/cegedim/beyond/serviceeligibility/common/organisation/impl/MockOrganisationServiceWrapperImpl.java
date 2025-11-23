package com.cegedim.beyond.serviceeligibility.common.organisation.impl;

import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
/** mettre ce fichier dans OrganisationWrapperConfiguration pour tester en local */
public class MockOrganisationServiceWrapperImpl implements OrganisationServiceWrapper {

  @Nullable
  @Override
  public Organisation getOrganizationByAmcNumber(@NotNull String amcCode) {
    Organisation organisation = new Organisation();
    organisation.setCode("organizationDto.getCode()");
    organisation.setFullName("organizationDto.getFullName()");
    organisation.setMain(true);
    return organisation;
  }

  @Override
  public String getSecondaryOrganizationCodeByAmcNumber(@NotNull String amcCode) {
    return "code";
  }

  @Override
  public boolean isOrgaAttached(String code, String amc) {
    return true;
  }

  @Override
  public Organisation getOrganisationByCode(String code) {
    Organisation organisation = new Organisation();
    organisation.setCode("organizationDto.getCode()");
    organisation.setFullName("organizationDto.getFullName()");
    organisation.setMain(true);
    return organisation;
  }
}
