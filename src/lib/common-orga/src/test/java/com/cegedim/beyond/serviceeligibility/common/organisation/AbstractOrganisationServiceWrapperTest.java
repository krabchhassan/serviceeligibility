package com.cegedim.beyond.serviceeligibility.common.organisation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public abstract class AbstractOrganisationServiceWrapperTest {

  abstract OrganisationServiceWrapper getOrganisationServiceWrapper();

  @Test
  void getOrganizationByAmcNumber_should_return_expected() {
    assertNull(getOrganisationServiceWrapper().getSecondaryOrganizationCodeByAmcNumber("fake"));
    assertNull(
        getOrganisationServiceWrapper().getSecondaryOrganizationCodeByAmcNumber("111111111"));

    var response =
        getOrganisationServiceWrapper().getSecondaryOrganizationCodeByAmcNumber("0000401166");
    assertNotNull(response);
    assertEquals("BALOO", response);
  }
}
