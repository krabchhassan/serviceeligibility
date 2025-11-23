package com.cegedim.beyond.serviceeligibility.common.organisation;

import static com.cegedim.beyond.business.organisation.exceptions.ScopeConfigurationException.ScopeConfigurationError.ONLY_PORTFOLIO_PARAMETER_GIVEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import com.cegedim.beyond.business.organisation.exceptions.ScopeConfigurationException;
import com.cegedim.beyond.business.organisation.facade.ScopeService;
import com.cegedim.beyond.spring.configuration.instance.InstanceResolver;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"multi"})
@SpringBootTest
class FrameworkOrganisationServiceWrapperTest extends AbstractOrganisationServiceWrapperTest {
  @Getter @Autowired private OrganisationServiceWrapper organisationServiceWrapper;
  @SpyBean private ScopeService scopeService;

  @BeforeEach
  public void setup() throws ScopeConfigurationException {
    InstanceResolver.setInstance("htp-es15");

    doThrow(new ScopeConfigurationException(ONLY_PORTFOLIO_PARAMETER_GIVEN))
        .when(scopeService)
        .findScopes(eq("aa"), eq("bb"), any(), any());
  }
}
