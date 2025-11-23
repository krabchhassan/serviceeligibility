package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import lombok.Getter;

public class RejetException extends Exception {

  @Getter private String codeRejet;

  public RejetException(String message, String codeRejet) {
    super(message);
    this.codeRejet = codeRejet;
  }
}
