package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionServiceDroitResilie extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionServiceDroitResilie() {
    super(ExceptionServiceCode.DROIT_RESILIE.name());
  }

  public ExceptionServiceDroitResilie(final Throwable cause) {
    super(cause);
  }

  public ExceptionServiceDroitResilie(
      final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
