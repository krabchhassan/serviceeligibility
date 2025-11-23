package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class TriggerException extends RuntimeException {

  public TriggerException(String msg, Exception e) {
    super(msg, e);
  }

  public TriggerException(String msg) {
    super(msg);
  }
}
