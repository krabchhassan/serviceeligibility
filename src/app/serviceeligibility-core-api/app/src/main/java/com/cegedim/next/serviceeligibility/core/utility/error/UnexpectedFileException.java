package com.cegedim.next.serviceeligibility.core.utility.error;

public class UnexpectedFileException extends RuntimeException {

  public UnexpectedFileException(String msg, Exception e) {
    super(msg, e);
  }

  public UnexpectedFileException(String msg) {
    super(msg);
  }
}
