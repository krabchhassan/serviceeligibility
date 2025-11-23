package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class OcGenericException extends RuntimeException {

  public OcGenericException(String msg, Exception e) {
    super(msg, e);
  }

  public OcGenericException(String msg) {
    super(msg);
  }
}
