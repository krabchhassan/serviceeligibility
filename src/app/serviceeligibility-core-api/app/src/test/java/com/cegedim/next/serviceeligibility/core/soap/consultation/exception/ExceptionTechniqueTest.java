package com.cegedim.next.serviceeligibility.core.soap.consultation.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionTechniqueTest {

  @Test
  void should_create_with_code() {
    ExceptionTechnique service = new ExceptionTechnique("test");
    Assertions.assertEquals("test", service.getMessage());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionTechnique service = new ExceptionTechnique("test2", cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals("test2", service.getMessage());
  }
}
