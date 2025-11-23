package com.cegedim.beyond.serviceeligibility.common.config;

import com.cegedim.beyond.business.organisation.facade.OrganisationService;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.serviceeligibility.common.organisation.impl.FrameworkOrganisationServiceWrapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Separate file for ConditionalOnClass to work on OrganizationService and do not cause a
 * classNotFound un multi tenant.
 */
@AutoConfiguration
public class OrganisationWrapperConfiguration {

  static class FrameworkOrganisationConfiguration {
    @Bean
    public OrganisationServiceWrapper organisationServiceWrapper(
        OrganisationService organisationService, @NotNull ObjectMapper objectMapper) {
      return new FrameworkOrganisationServiceWrapperImpl(organisationService, objectMapper);
    }
  }
}
