package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class TriggerNotFoundException extends RuntimeException {

  public TriggerNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public TriggerNotFoundException(String msg) {
    super(msg);
  }
}
