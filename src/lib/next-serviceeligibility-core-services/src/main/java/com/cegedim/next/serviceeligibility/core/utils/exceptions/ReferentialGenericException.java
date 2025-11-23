package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class ReferentialGenericException extends RuntimeException {

  public ReferentialGenericException(String msg, Exception e) {
    super(msg, e);
  }

  public ReferentialGenericException(String msg) {
    super(msg);
  }
}
