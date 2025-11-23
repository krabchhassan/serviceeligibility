package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.organisation.Organisation;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class OcServiceMockTest {
  @Autowired OcService ocService;

  @Autowired OrganisationServiceWrapper organisationServiceWrapper;

  @Test
  void shouldReturnNull() {
    Mockito.when(ocService.getOC(Mockito.anyString())).thenReturn(null);

    Oc oc = ocService.getOC("1");
    Assertions.assertNull(oc);
    oc = ocService.getOC("1");
    Assertions.assertNull(oc);
  }

  @Test
  void shouldReturnOC() {

    Organisation oc = new Organisation();
    oc.setMain(false);
    oc.setCode("OC");
    oc.setFullName("LABEL");
    oc.setCommercialName("LABEL");

    Mockito.when(organisationServiceWrapper.getOrganizationByAmcNumber(Mockito.anyString()))
        .thenReturn(oc);

    Oc responseOc = ocService.getOC("OC");
    Assertions.assertNotNull(responseOc);
    Assertions.assertEquals("OC", responseOc.getCode());
    Assertions.assertEquals("LABEL", responseOc.getLibelle());
  }
}
