package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionService;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceTest {

  @Test
  void should_create_with_code() {
    ExceptionService service = new ExceptionService(ExceptionServiceCode.BENEF_NON_ELIGIBLE);
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.BENEF_NON_ELIGIBLE.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionService service = new ExceptionService(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionService service = new ExceptionService(ExceptionServiceCode.BENEF_NON_ELIGIBLE, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.BENEF_NON_ELIGIBLE.name());
  }
}
