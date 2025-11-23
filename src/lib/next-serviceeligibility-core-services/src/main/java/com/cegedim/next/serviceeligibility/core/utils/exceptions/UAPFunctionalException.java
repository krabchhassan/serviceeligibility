package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/** This class defines a request validation exception. */
public class UAPFunctionalException extends RuntimeException {

  /** Seria version UID. */
  private static final long serialVersionUID = 1L;

  /** The status code. */
  @Getter private final HttpStatus statusCode;

  @Getter private final String customErrorCode;

  /**
   * Constructor.
   *
   * @param message message of the exception.
   * @param statusCode the status code.
   * @param customErrorCode the custom error code.
   */
  public UAPFunctionalException(
      final String message, final HttpStatus statusCode, final String customErrorCode) {
    super(message);
    this.statusCode = statusCode;
    this.customErrorCode = customErrorCode;
  }
}
