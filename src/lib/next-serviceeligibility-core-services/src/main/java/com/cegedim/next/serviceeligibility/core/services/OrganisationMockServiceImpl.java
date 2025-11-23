package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.business.organisation.exceptions.OrganisationConfigurationException;
import com.cegedim.beyond.business.organisation.facade.OrganisationService;
import com.cegedim.beyond.business.organisation.model.IssuingCompany;
import com.cegedim.beyond.business.organisation.model.MlsConfiguration;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

// mettre en primary et service pour le local
public class OrganisationMockServiceImpl implements OrganisationService {

  @Override
  public boolean checkIssuingCompanyByDateAndCode(LocalDate date, String code) {
    return false;
  }

  @Override
  public boolean isOrgaAttached(String code, String amc) {
    return true;
  }

  @Override
  public String getOrganizationShortCode(String code, boolean isMain) {
    return "";
  }

  @Override
  public Organisation getOrganizationByNumAmc(String numAmc) {
    Organisation organisation = new Organisation();
    organisation.setMain(true);
    organisation.setCode("s3OrgaCode");
    organisation.setFullName("s3OrgaFullName");
    organisation.setCommercialName("s3OrgaCommercialName");
    return organisation;
  }

  @Override
  public Organisation getOrganizationByCode(String code) {
    Organisation organization = new Organisation();
    organization.setMain(true);
    organization.setCode("s3OrgaCode");
    organization.setFullName("s3OrgaFullName");
    organization.setCommercialName("s3OrgaCommercialName");
    return organization;
  }

  @Override
  public MlsConfiguration getMlsConfiguration(String code)
      throws OrganisationConfigurationException {
    return null;
  }

  @Override
  public List<IssuingCompany> findIssuingCompanies(LocalDate date, String code) {
    return Collections.emptyList();
  }

  @Override
  public List<IssuingCompany> findIssuingCompanies(LocalDate date) {
    return Collections.emptyList();
  }
}
