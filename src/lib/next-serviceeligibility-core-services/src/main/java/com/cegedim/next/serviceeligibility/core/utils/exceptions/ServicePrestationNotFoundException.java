package com.cegedim.next.serviceeligibility.core.utils.exceptions;

public class ServicePrestationNotFoundException extends RuntimeException {

  public ServicePrestationNotFoundException(String msg, Exception e) {
    super(msg, e);
  }

  public ServicePrestationNotFoundException(String msg) {
    super(msg);
  }
}
