package com.cegedim.next.prestij.worker.exception;

@SuppressWarnings("serial")
public class KafkaSendingException extends Exception {
  private String local;

  public KafkaSendingException(String errorMessage) {
    super(errorMessage);
    local = errorMessage;
  }

  @Override
  public String getLocalizedMessage() {
    return local;
  }
}
