package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/** This class defines a request validation exception. */
public class RequestValidationListException extends RuntimeException {

  /** Seria version UID. */
  private static final long serialVersionUID = 1L;

  /** The status code. */
  @Getter private final HttpStatus statusCode;

  @Getter private final String customErrorCode;

  @Getter private List<RequestValidationException> exceptions;

  /**
   * Constructor.
   *
   * @param message message of the exception.
   * @param statusCode the status code.
   */
  public RequestValidationListException(
      final String message,
      final HttpStatus statusCode,
      List<RequestValidationException> exceptions) {
    this(message, statusCode, "");
    this.exceptions = exceptions;
  }

  public RequestValidationListException(
      final String message, List<RequestValidationException> exceptions) {
    this(message, HttpStatus.BAD_REQUEST, "");
    this.exceptions = exceptions;
  }

  /**
   * Constructor.
   *
   * @param cause eventual cause of the exception.
   * @param statusCode the status code.
   */
  public RequestValidationListException(final Throwable cause, final HttpStatus statusCode) {
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
  public RequestValidationListException(
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
  public RequestValidationListException(
      final String message, final Throwable cause, final HttpStatus statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
    this.customErrorCode = null;
  }
}
