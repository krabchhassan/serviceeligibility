package com.cegedim.next.serviceeligibility.core.model.kafka.exception;

@SuppressWarnings("serial")
public class UnexpectedBddsException extends Exception {
  public UnexpectedBddsException(String errorMessage) {
    super(errorMessage);
  }
}
