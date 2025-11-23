package com.cegedim.next.prestij.worker.service;

import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.ContratIJ;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Oc;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.services.GlobalValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ValidationTest {

  @Test
  void should_fail() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    ContratIJ c = new ContratIJ();
    c.setNumero("qsdf");
    c.setNumeroAdherent("sdqdf");
    c.setDateSouscription("qsdf");
    // pattern=DateUtils.YYYY_MM_DD

    Oc oc = new Oc();
    Set<ConstraintViolation<Oc>> violations = validator.validate(oc);
    violations.forEach(a -> System.out.println(a.getMessage()));

    System.out.println(violations.size());
  }

  @Test
  void should_throw_validation_exception() {
    PrestIJValidationService vs = new PrestIJValidationService();
    GlobalValidationService validationService = new GlobalValidationService();
    vs.globalValidationService = validationService;
    validationService.validator = Validation.buildDefaultValidatorFactory().getValidator();
    try {
      PrestIJ p = new PrestIJ();
      ContratIJ c = new ContratIJ();
      p.setContrat(c);

      c.setNumero("qsdf");
      c.setNumeroAdherent("sdqdf");
      c.setDateSouscription("qsdf");
      vs.validate(p);

    } catch (ValidationException e) {
      String message = e.getMessage();
      System.out.println(message);
    }
  }
}
