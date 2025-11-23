package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class OcNotFoundException extends RuntimeException {

  public OcNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public OcNotFoundException(String msg) {
    super(msg);
  }
}
