package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceDroitNonOuvertTest {

  @Test
  void should_create_with_correct_code() {
    ExceptionServiceDroitNonOuvert service = new ExceptionServiceDroitNonOuvert();
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.DROIT_NON_OUVERT.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionServiceDroitNonOuvert service = new ExceptionServiceDroitNonOuvert(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionServiceDroitNonOuvert service =
        new ExceptionServiceDroitNonOuvert(ExceptionServiceCode.DROIT_NON_OUVERT, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.DROIT_NON_OUVERT.name());
  }
}
