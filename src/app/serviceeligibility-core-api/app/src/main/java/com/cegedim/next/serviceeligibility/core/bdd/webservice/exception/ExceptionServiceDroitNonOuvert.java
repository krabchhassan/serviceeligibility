package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionServiceDroitNonOuvert extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionServiceDroitNonOuvert() {
    super(ExceptionServiceCode.DROIT_NON_OUVERT.name());
  }

  public ExceptionServiceDroitNonOuvert(final Throwable cause) {
    super(cause);
  }

  public ExceptionServiceDroitNonOuvert(
      final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
