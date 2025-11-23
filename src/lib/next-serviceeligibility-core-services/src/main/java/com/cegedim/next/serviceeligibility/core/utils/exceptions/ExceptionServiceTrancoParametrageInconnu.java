package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class ExceptionServiceTrancoParametrageInconnu extends RuntimeException {

  private static final Long serialVersionUID = 1L;

  public ExceptionServiceTrancoParametrageInconnu() {
    super(ExceptionServiceCode.TRANSCO_PARAMETRAGE_INCONNU.name());
  }

  public ExceptionServiceTrancoParametrageInconnu(Throwable cause) {
    super(cause);
  }

  public ExceptionServiceTrancoParametrageInconnu(
      ExceptionServiceCode codeErreur, Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
