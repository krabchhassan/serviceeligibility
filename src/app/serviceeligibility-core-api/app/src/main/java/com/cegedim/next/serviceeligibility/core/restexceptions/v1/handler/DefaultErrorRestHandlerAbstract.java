package com.cegedim.next.serviceeligibility.core.restexceptions.v1.handler;

import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestException;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.constants.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;

/** This abstract class defines default rest error handler. */
public abstract class DefaultErrorRestHandlerAbstract extends DefaultResponseErrorHandler {

  /** The logger. */
  private Logger logger = LoggerFactory.getLogger(DefaultErrorRestHandlerAbstract.class);

  /**
   * Allows to get the prefix error code which will be use to build instances of {@link RestError}.
   *
   * @return an instance of {@link String}.
   */
  public abstract String getPrefixErrorCode();

  /**
   * When an instance of {@link Exception} is thrown by the controller, we complete the response
   * entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link Exception} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleExceptions(final Exception ex) {
    if (ex == null) {
      throw new IllegalArgumentException(
          "[ErrorRestHandler#handleExceptions] => The parameter [ex] is required not null.");
    }

    HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    // TODO Fix with proper logger. see US #AIN-272
    doLog(ex, statusCode);

    String errorCode = getErrorCode(RestErrorConstants.SUFFIX_UNDEFINED_INTERNAL_SERVER_ERROR);

    return buildResponseEntity(ex, statusCode, errorCode, "An error occurs.");
  }

  /**
   * When an instance of {@link IllegalArgumentException} is thrown by the controller, we complete
   * the response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link IllegalArgumentException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentExceptions(final IllegalArgumentException ex) {
    if (ex == null) {
      throw new IllegalArgumentException(
          "[ErrorRestHandler#handleExceptions] => The parameter [ex] is required not null.");
    }

    HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    // TODO Fix with proper logger. see US #AIN-272
    doLog(ex, statusCode);

    String errorCode = getErrorCode(RestErrorConstants.SUFFIX_INVALID_PARAMETER_EXCEPTION);

    return buildResponseEntity(
        ex, statusCode, errorCode, "An error occurs on the validation of methods parameters.");
  }

  /**
   * When an instance of {@link RestException} is thrown by the controller, we complete the response
   * entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link RestException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(RestException.class)
  public ResponseEntity<String> handleRestExceptions(final RestException ex) {
    if (ex == null) {
      throw new IllegalArgumentException(
          "[ErrorRestHandler#handleRestExceptions] => The parameter [ex] is required not null.");
    }

    // TODO Fix with proper logger. see US #AIN-272
    doLog(ex, ex.getStatusCode());

    final String jsonResponseEntityBody = ex.toJson();

    return new ResponseEntity<>(jsonResponseEntityBody, ex.getStatusCode());
  }

  /**
   * When an instance of {@link HttpStatusCodeException} is thrown by the controller, we complete
   * the response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link HttpStatusCodeException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(HttpStatusCodeException.class)
  public ResponseEntity<String> handleHttpStatusCodeException(final HttpStatusCodeException ex) {
    if (ex == null) {
      throw new IllegalArgumentException(
          "[ErrorRestHandler#handleHttpStatusCodeException] => The parameter [ex] is required not null.");
    }

    HttpStatusCode statusCode = ex.getStatusCode();

    if (HttpStatus.resolve(statusCode.value()) == null) {
      statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    // TODO Fix with proper logger. see US #AIN-272
    doLog(ex, statusCode);

    String errorCode = getErrorCode(RestErrorConstants.SUFFIX_UNDEFINED_REST_CLIENT_EXCEPTION);

    return buildResponseEntity(ex, statusCode, errorCode, "An error occurs on HTTP requests.");
  }

  /**
   * When an instance of {@link ServletRequestBindingException} is thrown by the controller, we
   * complete the response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link ServletRequestBindingException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(ServletRequestBindingException.class)
  public ResponseEntity<String> handleServletRequestBindingException(
      final ServletRequestBindingException ex) {
    if (ex == null) {
      throw new IllegalArgumentException(
          "[ErrorRestHandler#handleServletRequestBindingException] => The parameter [ex] is required not null.");
    }

    HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    // TODO Fix with proper logger. see US #AIN-272
    doLog(ex, statusCode);

    String errorCode = getErrorCode(RestErrorConstants.SUFFIX_INVALID_REST_PARAMETER_EXCEPTION);

    return buildResponseEntity(ex, statusCode, errorCode, "Missing request parameter.");
  }

  /**
   * We consider that any ResourceNotFoundException is considered as a server exception as the SDK
   * handle all network issue.
   *
   * @param exception ResourceNotFoundException
   * @return ResponseEntity<String>
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleExceptions(ResourceNotFoundException exception) {
    return buildResponseError(
        exception, RestErrorConstants.SUFFIX_ERROR_REST_RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
  }

  /**
   * Allows to build an instance of ResponseEntity based on an {@link Exception}.
   *
   * @param exception instance of {@link Exception} which defines the problem [REQUIRED NOT NULL].
   * @param errorCodeValue rest error code for the instance of {@link RestException} which will be
   *     generated [REQUIRED NOT NULL].
   * @param statusCode HTTP status code [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity} which describes the occured problem.
   */
  private ResponseEntity<String> buildResponseError(
      ResourceNotFoundException exception, String errorCodeValue, HttpStatus statusCode) {
    return buildResponseError(exception, errorCodeValue, statusCode, exception.getMessage());
  }

  /**
   * Allows to build an instance of ResponseEntity based on an {@link Exception}.
   *
   * @param exception instance of {@link Exception} which defines the problem [REQUIRED NOT NULL].
   * @param errorCodeValue rest error code for the instance of {@link RestException} which will be
   *     generated [REQUIRED NOT NULL].
   * @param statusCode HTTP status code [REQUIRED NOT NULL].
   * @param message message for the instance of {@link RestException} which will be generated
   *     [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity} which describes the occured problem.
   */
  private ResponseEntity<String> buildResponseError(
      ResourceNotFoundException exception,
      String errorCodeValue,
      HttpStatus statusCode,
      String message) {
    if (exception == null) {
      throw new IllegalArgumentException(
          "[ErrorRestHandler#handleServletRequestBindingException] => The parameter [ex] is required not null.");
    }

    // TODO Fix with proper logger. see US #AIN-272
    doLog(exception, statusCode);

    String errorCode = getErrorCode(errorCodeValue);

    return buildResponseEntity(exception, statusCode, errorCode, message);
  }

  /**
   * Allows to build an instance of ResponseEntity based on an {@link Exception}.
   *
   * @param ex instance of {@link Exception} which defines the problem [REQUIRED NOT NULL].
   * @param statusCode HTTP status code [REQUIRED NOT NULL].
   * @param errorCode rest error code for the instance of {@link RestException} which will be
   *     generated [REQUIRED NOT NULL].
   * @param messageRestException message for the instance of {@link RestException} which will be
   *     generated [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity} which describes the occured problem.
   */
  public ResponseEntity<String> buildResponseEntity(
      final Exception ex,
      final HttpStatusCode statusCode,
      final String errorCode,
      final String messageRestException) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

    if (ex == null) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#buildResponseEntity] => The parameter [ex] is required not null.");
    }

    if (statusCode == null) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#buildResponseEntity] => The parameter [statusCode] is required not null.");
    }

    if (errorCode == null) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#buildResponseEntity] => The parameter [errorCode] is required not null.");
    }

    if (messageRestException == null) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#buildResponseEntity] => The parameter [messageRestException] is required not null.");
    }

    RestError restError = new RestError(errorCode, ex.getMessage(), ExceptionLevel.ERROR);

    RestException restException = new RestException(messageRestException, restError, statusCode);

    String jsonResponseEntityBody = restException.toJson();

    return new ResponseEntity<>(jsonResponseEntityBody, headers, statusCode);
  }

  /**
   * Allows to get a complete error code from suffix error code.
   *
   * <p>The error code will be built in the format {PrefixErrorCode}{suffixErrorCode}.
   *
   * <p>Example :<br>
   * "PRODUCT-BOUNDED_CONTEXT-MODULE-00000".
   *
   * @param suffixErrorCode suffix error code [REQUIRED NOT BLANK].
   * @return an instance of {@link String}.
   */
  protected String getErrorCode(final String suffixErrorCode) {
    if ((suffixErrorCode == null) || suffixErrorCode.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#getErrorCode] => The parameter [suffixErrorCode] must be not blank.");
    }

    return getPrefixErrorCode() + "-" + suffixErrorCode;
  }

  /**
   * Allows to print in the "standard" error output stream the exception before to fix with proper
   * logger. See US #AIN-272.
   *
   * @param ex instance of {@link Exception} [REQUIRED NOT NULL].
   * @param statusCode status code HTTP [REQUIRED NOT NULL].
   */
  protected void doLog(final Exception ex, final HttpStatusCode statusCode) {
    if (ex == null) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#errorPrintln] => The parameter [ex] is required not null.");
    }

    if (statusCode == null) {
      throw new IllegalArgumentException(
          "[DefaultErrorRestHandlerAbstract#errorPrintln] => The parameter [statusCode] is required not null.");
    }

    if (logger.isErrorEnabled()) {
      logger.error(
          String.format(
              "An error %s has been catched, %s is thrown, Exception cause is %s",
              ex.getClass(), statusCode, ex.getMessage()));
    }
  }
}
