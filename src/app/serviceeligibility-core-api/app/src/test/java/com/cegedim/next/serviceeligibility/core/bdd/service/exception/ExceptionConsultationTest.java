package com.cegedim.next.serviceeligibility.core.bdd.service.exception;

import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultation;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreIncorrect;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreSegmentsManquent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionConsultationTest {

  @Test
  void should_create_exceptoin() {

    ExceptionConsultation exception = new ExceptionConsultationParametreIncorrect();
    Assertions.assertNull(exception.getMessage());

    ExceptionConsultation exception2 = new ExceptionConsultationParametreSegmentsManquent();
    Assertions.assertNull(exception2.getMessage());
  }
}
