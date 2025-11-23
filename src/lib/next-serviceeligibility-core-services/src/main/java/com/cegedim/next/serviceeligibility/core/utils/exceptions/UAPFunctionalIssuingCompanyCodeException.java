package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UAPFunctionalIssuingCompanyCodeException extends UAPFunctionalException {

  /**
   * Constructor.
   *
   * @param message message of the exception.
   * @param statusCode the status code.
   * @param customErrorCode the custom error code.
   */
  public UAPFunctionalIssuingCompanyCodeException(
      String message, HttpStatus statusCode, String customErrorCode) {
    super(message, statusCode, customErrorCode);
  }
}
