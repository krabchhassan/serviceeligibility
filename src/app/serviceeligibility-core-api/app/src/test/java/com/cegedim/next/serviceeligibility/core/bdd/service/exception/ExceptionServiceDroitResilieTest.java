package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitResilie;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceDroitResilieTest {

  @Test
  void should_create_with_correct_code() {
    ExceptionServiceDroitResilie service = new ExceptionServiceDroitResilie();
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.DROIT_RESILIE.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionServiceDroitResilie service = new ExceptionServiceDroitResilie(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionServiceDroitResilie service =
        new ExceptionServiceDroitResilie(ExceptionServiceCode.DROIT_RESILIE, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.DROIT_RESILIE.name());
  }
}
