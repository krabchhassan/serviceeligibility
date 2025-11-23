package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireInconnu;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceBeneficiaireNonEligibleTest {

  @Test
  void should_create_with_correct_code() {
    ExceptionServiceBeneficiaireInconnu service = new ExceptionServiceBeneficiaireInconnu();
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.BENEFICIAIRE_INCONNU.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionServiceBeneficiaireInconnu service = new ExceptionServiceBeneficiaireInconnu(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionServiceBeneficiaireInconnu service =
        new ExceptionServiceBeneficiaireInconnu(ExceptionServiceCode.BENEF_NON_ELIGIBLE, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.BENEF_NON_ELIGIBLE.name());
  }
}
