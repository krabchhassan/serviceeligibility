package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class ExceptionServiceFormatDate extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionServiceFormatDate() {
    super(ExceptionServiceCode.FORMAT_DATE_INCORRECT.name());
  }

  public ExceptionServiceFormatDate(final Throwable cause) {
    super(cause);
  }

  public ExceptionServiceFormatDate(final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
