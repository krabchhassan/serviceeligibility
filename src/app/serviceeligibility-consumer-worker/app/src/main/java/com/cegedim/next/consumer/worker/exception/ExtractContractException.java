package com.cegedim.next.consumer.worker.exception;

public class ExtractContractException extends RuntimeException {

  public ExtractContractException(Exception e) {
    super(e.getMessage());
  }
}
