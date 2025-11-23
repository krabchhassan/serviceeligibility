package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionNumeroAdherentAbsent extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionNumeroAdherentAbsent() {
    super(ExceptionServiceCode.NUM_ADHERENT_ABSENT.name());
  }

  public ExceptionNumeroAdherentAbsent(final Throwable cause) {
    super(cause);
  }

  public ExceptionNumeroAdherentAbsent(
      final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
