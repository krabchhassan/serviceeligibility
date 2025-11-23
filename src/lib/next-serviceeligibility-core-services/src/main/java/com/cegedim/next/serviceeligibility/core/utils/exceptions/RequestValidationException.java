package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/** This class defines a request validation exception. */
public class RequestValidationException extends RuntimeException {

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
   */
  public RequestValidationException(final String message, final HttpStatus statusCode) {
    this(message, statusCode, null);
  }

  public RequestValidationException(final String message) {
    this(message, HttpStatus.BAD_REQUEST, null);
  }

  /**
   * Constructor.
   *
   * @param cause eventual cause of the exception.
   * @param statusCode the status code.
   */
  public RequestValidationException(final Throwable cause, final HttpStatus statusCode) {
    super(cause);
    this.statusCode = statusCode;
    this.customErrorCode = null;
  }

  /**
   * Constructor.
   *
   * @param message message of the exception.
   * @param statusCode the status code.
   * @param customErrorCode the custom error code.
   */
  public RequestValidationException(
      final String message, final HttpStatus statusCode, final String customErrorCode) {
    super(message);
    this.statusCode = statusCode;
    this.customErrorCode = customErrorCode;
  }

  /**
   * Constructor.
   *
   * @param message message of the exception.
   * @param cause eventual cause of the exception.
   * @param statusCode the status code.
   */
  public RequestValidationException(
      final String message, final Throwable cause, final HttpStatus statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
    this.customErrorCode = null;
  }
}
