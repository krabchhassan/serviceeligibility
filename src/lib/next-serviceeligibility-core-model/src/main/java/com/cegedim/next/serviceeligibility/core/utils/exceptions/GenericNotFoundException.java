package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class GenericNotFoundException extends RuntimeException {

  public GenericNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public GenericNotFoundException(String msg) {
    super(msg);
  }
}
