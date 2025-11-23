package com.cegedim.next.serviceeligibility.core.bobb.error;

public class ContractNotFoundException extends RuntimeException {

  public ContractNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public ContractNotFoundException(String msg) {
    super(msg);
  }
}
