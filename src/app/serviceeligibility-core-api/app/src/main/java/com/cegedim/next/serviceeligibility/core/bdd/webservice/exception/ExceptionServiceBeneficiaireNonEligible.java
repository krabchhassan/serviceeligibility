package com.cegedim.next.serviceeligibility.core.bdd.webservice.exception;

import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceCode;

public class ExceptionServiceBeneficiaireNonEligible extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ExceptionServiceBeneficiaireNonEligible() {
    super(ExceptionServiceCode.BENEF_NON_ELIGIBLE.name());
  }

  public ExceptionServiceBeneficiaireNonEligible(final Throwable cause) {
    super(cause);
  }

  public ExceptionServiceBeneficiaireNonEligible(
      final ExceptionServiceCode codeErreur, final Throwable cause) {
    super(codeErreur.name(), cause);
  }
}
