package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceFormatDateTest {

  @Test
  void should_create_with_correct_code() {
    ExceptionServiceFormatDate service = new ExceptionServiceFormatDate();
    Assertions.assertEquals(
        service.getMessage(), ExceptionServiceCode.FORMAT_DATE_INCORRECT.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionServiceFormatDate service = new ExceptionServiceFormatDate(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionServiceFormatDate service =
        new ExceptionServiceFormatDate(ExceptionServiceCode.FORMAT_DATE_INCORRECT, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(
        service.getMessage(), ExceptionServiceCode.FORMAT_DATE_INCORRECT.name());
  }
}
