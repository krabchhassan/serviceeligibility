package com.cegedim.next.serviceeligibility.core.utility.error;

public class InvalidParameterException extends RuntimeException {

  public InvalidParameterException(String msg, Exception e) {
    super(msg, e);
  }

  public InvalidParameterException(String msg) {
    super(msg);
  }
}
