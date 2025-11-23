package com.cegedim.next.serviceeligibility.core.services;

import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalValidationService {

  @Autowired public Validator validator;

  @ContinueSpan(log = "validate global")
  public <T> Set<ConstraintViolation<T>> validate(T object) {
    return validator.validate(object);
  }
}
