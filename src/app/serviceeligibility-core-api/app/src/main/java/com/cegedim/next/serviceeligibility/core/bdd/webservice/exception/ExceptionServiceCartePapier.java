package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionServiceCartePapier extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionServiceCartePapier() {
    super(ExceptionServiceCode.CARTE_PAPIER.name());
  }

  public ExceptionServiceCartePapier(final Throwable cause) {
    super(cause);
  }

  public ExceptionServiceCartePapier(final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
