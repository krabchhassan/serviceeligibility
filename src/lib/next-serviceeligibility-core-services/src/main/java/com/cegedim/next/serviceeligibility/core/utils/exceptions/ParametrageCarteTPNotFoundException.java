package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class ParametrageCarteTPNotFoundException extends RuntimeException {

  public ParametrageCarteTPNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public ParametrageCarteTPNotFoundException(String msg) {
    super(msg);
  }
}
