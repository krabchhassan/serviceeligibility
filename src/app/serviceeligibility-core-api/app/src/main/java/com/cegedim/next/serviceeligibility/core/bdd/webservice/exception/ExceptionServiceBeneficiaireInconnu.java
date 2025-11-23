package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionServiceBeneficiaireInconnu extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionServiceBeneficiaireInconnu() {
    super(ExceptionServiceCode.BENEFICIAIRE_INCONNU.name());
  }

  public ExceptionServiceBeneficiaireInconnu(final Throwable cause) {
    super(cause);
  }

  public ExceptionServiceBeneficiaireInconnu(
      final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
