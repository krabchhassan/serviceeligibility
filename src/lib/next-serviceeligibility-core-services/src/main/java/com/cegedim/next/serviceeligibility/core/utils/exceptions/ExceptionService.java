package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class ExceptionService extends RuntimeException {

  /** generated serial verion UID. */
  private static final long serialVersionUID = -4963178826647736642L;

  public ExceptionService(final ExceptionServiceCode codeErreur) {
    super(codeErreur.name());
  }

  public ExceptionService(final Throwable cause) {
    super(cause);
  }

  public ExceptionService(final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
