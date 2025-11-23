package com.cegedim.next.serviceeligibility.core.utility.error;

public class ContractNotFoundException extends RuntimeException {

  public ContractNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public ContractNotFoundException(String msg) {
    super(msg);
  }
}
