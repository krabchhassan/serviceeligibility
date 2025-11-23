package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class InvalidParameterException extends RuntimeException {

  public InvalidParameterException(String msg, Exception e) {
    super(msg, e);
  }

  public InvalidParameterException(String msg) {
    super(msg);
  }
}
