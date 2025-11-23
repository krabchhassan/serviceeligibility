package com.cegedim.next.serviceeligibility.rdoserviceprestation.exceptions;

@SuppressWarnings("serial")
public class JsonMalformedException extends Exception {
  public JsonMalformedException(String errorMessage) {
    super(errorMessage);
  }
}
