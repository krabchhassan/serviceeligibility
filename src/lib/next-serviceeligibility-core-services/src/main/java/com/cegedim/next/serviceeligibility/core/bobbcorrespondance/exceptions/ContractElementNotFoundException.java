package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.exceptions;

public class ContractElementNotFoundException extends RuntimeException {
  public ContractElementNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public ContractElementNotFoundException(String msg) {
    super(msg);
  }
}
