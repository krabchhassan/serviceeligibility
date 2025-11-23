package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireNonEligible;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceCartePapierTest {

  @Test
  void should_create_with_correct_code() {
    ExceptionServiceBeneficiaireNonEligible service = new ExceptionServiceBeneficiaireNonEligible();
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.BENEF_NON_ELIGIBLE.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionServiceBeneficiaireNonEligible service =
        new ExceptionServiceBeneficiaireNonEligible(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionServiceBeneficiaireNonEligible service =
        new ExceptionServiceBeneficiaireNonEligible(ExceptionServiceCode.BENEF_NON_ELIGIBLE, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.BENEF_NON_ELIGIBLE.name());
  }
}
