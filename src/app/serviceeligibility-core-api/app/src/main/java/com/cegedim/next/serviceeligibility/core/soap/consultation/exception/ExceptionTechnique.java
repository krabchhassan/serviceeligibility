package com.cegedim.next.serviceeligibility.core.soap.consultation.exception;

public class ExceptionTechnique extends RuntimeException {

  /** generated serial version UID. */
  private static final long serialVersionUID = 8770024721449605607L;

  public ExceptionTechnique(final String message) {
    super(message);
  }

  public ExceptionTechnique(final String message, final Throwable cause) {
    super(message, cause);
  }
}
