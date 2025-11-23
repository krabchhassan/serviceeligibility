package com.cegedim.beyond.serviceeligibility.common.exception;

public class OrganisationWrapperException extends RuntimeException {
  public OrganisationWrapperException(Exception e) {
    super(e);
  }

  public OrganisationWrapperException(String message) {
    super(message);
  }

  public OrganisationWrapperException(String message, Exception e) {
    super(message, e);
  }
}
