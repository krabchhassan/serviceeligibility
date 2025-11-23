package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionPriorisationGaranties extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionPriorisationGaranties() {
    super(ExceptionServiceCode.PRIORISATION_INCORRECTE.name());
  }

  public ExceptionPriorisationGaranties(final Throwable cause) {
    super(cause);
  }

  public ExceptionPriorisationGaranties(
      final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
