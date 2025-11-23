package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceCartePapier;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionServiceDeclarationNonTrouveeTest {

  @Test
  void should_create_with_correct_code() {
    ExceptionServiceCartePapier service = new ExceptionServiceCartePapier();
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.CARTE_PAPIER.name());
  }

  @Test
  void should_create_with_cause() {
    Throwable cause = new Throwable();
    ExceptionServiceCartePapier service = new ExceptionServiceCartePapier(cause);
    Assertions.assertNotNull(service.getCause());
  }

  @Test
  void should_create_with_cause_and_code() {
    Throwable cause = new Throwable();
    ExceptionServiceCartePapier service =
        new ExceptionServiceCartePapier(ExceptionServiceCode.CARTE_PAPIER, cause);
    Assertions.assertNotNull(service.getCause());
    Assertions.assertEquals(service.getMessage(), ExceptionServiceCode.CARTE_PAPIER.name());
  }
}
