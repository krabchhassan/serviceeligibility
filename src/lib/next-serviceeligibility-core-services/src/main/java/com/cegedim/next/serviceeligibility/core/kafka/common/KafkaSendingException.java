package com.cegedim.next.serviceeligibility.core.kafka.common;

@SuppressWarnings("serial")
public class KafkaSendingException extends RuntimeException {
  private final String local;

  public KafkaSendingException(String errorMessage) {
    super(errorMessage);
    local = errorMessage;
  }

  @Override
  public String getLocalizedMessage() {
    return local;
  }
}
