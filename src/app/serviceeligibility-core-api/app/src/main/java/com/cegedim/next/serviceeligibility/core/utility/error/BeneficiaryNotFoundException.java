package com.cegedim.next.serviceeligibility.core.utility.error;

public class BeneficiaryNotFoundException extends RuntimeException {

  public BeneficiaryNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public BeneficiaryNotFoundException(String msg) {
    super(msg);
  }
}
