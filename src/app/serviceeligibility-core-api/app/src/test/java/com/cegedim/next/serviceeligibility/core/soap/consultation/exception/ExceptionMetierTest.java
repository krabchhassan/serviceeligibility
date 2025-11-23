package com.cegedim.next.serviceeligibility.core.soap.consultation.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionMetierTest {

  @Test
  void should_create_exception_with_correct_data() {
    ExceptionMetier ex = new ExceptionMetier(CodeReponse.BENEF_NON_ELIGIBLE, 123);
    Assertions.assertEquals(CodeReponse.BENEF_NON_ELIGIBLE, ex.getCodeReponse());
    Assertions.assertEquals(123, ex.getParams()[0]);
  }
}
