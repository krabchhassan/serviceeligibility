package com.cegedim.beyond.serviceeligibility.common.organisation.impl;

import com.cegedim.beyond.serviceeligibility.common.exception.OrganisationWrapperException;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.cegedim.common.organisation.dto.OrganizationDto;
import com.cegedim.common.organisation.exception.OrganizationIndexInitException;
import com.cegedim.common.organisation.exception.OrganizationNotFoundException;
import com.cegedim.common.organisation.service.OrganizationService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LegacyOrganisationServiceWrapperImpl implements OrganisationServiceWrapper {

  private final OrganizationService organizationService;

  @Nullable
  @Override
  public Organisation getOrganizationByAmcNumber(@NotNull String amcCode) {
    OrganizationDto organizationDto = getOrganisationByAmc(amcCode);
    if (organizationDto != null) {
      Organisation organisation = new Organisation();
      organisation.setCode(organizationDto.getCode());
      organisation.setFullName(organizationDto.getFullName());
      organisation.setMain(organizationDto.getIsMainType());
      return organisation;
    }
    return null;
  }

  @Override
  public String getSecondaryOrganizationCodeByAmcNumber(@NotNull String amcCode) {
    OrganizationDto dto = getOrganisationByAmc(amcCode);
    if (dto != null && Boolean.FALSE.equals(dto.getIsMainType())) {
      return dto.getCode();
    }
    return null;
  }

  @Override
  public boolean isOrgaAttached(String code, String amc) {
    try {
      return organizationService.isOrgaAttached(code, amc);
    } catch (OrganizationNotFoundException e) {
      throw new OrganisationWrapperException(e);
    }
  }

  @Override
  public Organisation getOrganisationByCode(String code) {
    OrganizationDto organizationDto;
    try {
      organizationDto = organizationService.getOrganizationByCode(code);
    } catch (OrganizationNotFoundException e) {
      throw new OrganisationWrapperException(e);
    }
    if (organizationDto != null) {
      Organisation organisation = new Organisation();
      organisation.setCode(organizationDto.getCode());
      organisation.setFullName(organizationDto.getFullName());
      organisation.setMain(organizationDto.getIsMainType());
      return organisation;
    }
    return null;
  }

  @Nullable
  private OrganizationDto getOrganisationByAmc(@NotNull String amcCode) {
    try {
      return organizationService.getOrganizationByAmcNumber(amcCode);
    } catch (OrganizationIndexInitException | OrganizationNotFoundException ex) {
      // do nothing
      // secondaryOrganizationCode is null => return error later.
      log.warn("An exception occurred with orga lib, issuingCompanyCode: {}", amcCode, ex);
    }
    return null;
  }
}
