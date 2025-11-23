package com.cegedim.next.common.excel.util.error;

import com.cegedim.common.base.rest.error.ErrorCodeFactory;
import com.cegedim.common.base.rest.exceptions.dto.ExceptionDto;
import com.cegedim.common.base.rest.exceptions.handler.DefaultErrorRestHandlerAbstract;
import com.cegedim.common.base.rest.exceptions.model.RestError;
import com.cegedim.common.base.rest.exceptions.model.RestException;
import com.cegedim.common.base.rest.exceptions.model.enums.ExceptionLevel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Basic error handler specialized for servicedirectory core api. */
@ControllerAdvice
public class ErrorHandler extends DefaultErrorRestHandlerAbstract {

  protected ErrorHandler(ErrorCodeFactory errorCodeFactory) {
    super(errorCodeFactory);
  }

  /**
   * When an instance of {@link RequestValidationException} is thrown by the controller, we complete
   * the response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link RequestValidationException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(RequestValidationException.class)
  public ResponseEntity<ExceptionDto> handleRestExceptions(
      @NonNull final RequestValidationException ex) {
    HttpStatus statusCode = ex.getStatusCode();
    String customStatusCode = ex.getCustomErrorCode();
    if (StringUtils.isBlank(customStatusCode)) {
      customStatusCode = RestErrorConstants.ERROR_REQUEST_VALIDATION_EXCEPTION;
    }
    return buildResponseEntity(ex, statusCode, customStatusCode, ex.getMessage());
  }

  /**
   * When an instance of {@link BindException} is thrown by the controller, we complete the response
   * entity body with only {@link RestException}'s properties.
   *
   * @param exception instance of {@link BindException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler({BindException.class})
  public ResponseEntity<String> handleRestExceptions(@NonNull final BindException exception) {
    return handleBindException(exception, ex -> buildRestErrors(ex.getBindingResult()));
  }

  private <T extends Exception> ResponseEntity<String> handleBindException(
      T exception, Function<T, RestError> restErrorsGetter) {

    HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    RestError restErrors = restErrorsGetter.apply(exception);
    return buildResponseEntity(statusCode, restErrors, exception.getMessage());
  }

  /**
   * When an instance of {@link MethodArgumentNotValidException} is thrown by the controller, we
   * complete the response entity body with only {@link RestException}'s properties.
   *
   * @param exception instance of {@link MethodArgumentNotValidException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleRestExceptions(
      @NonNull final MethodArgumentNotValidException exception) {
    return handleBindException(exception, ex -> buildRestErrors(ex.getBindingResult()));
  }

  /**
   * When an instance of {@link HttpMessageNotReadableException} is thrown by the controller, we
   * complete the response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link HttpMessageNotReadableException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ExceptionDto> handleRestExceptions(
      @NonNull final HttpMessageNotReadableException ex) {
    HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    return buildResponseEntity(
        ex, statusCode, RestErrorConstants.ERROR_MESSAGE_NOT_READABLE_EXCEPTION, ex.getMessage());
  }

  /**
   * Allows to build an instance of {@link ResponseEntity}.
   *
   * @param statusCode status code HTTP [VALID STATUS CODE REQUIRED].
   * @param restErrors a list of eventually instances of {@link RestError} [REQUIRED NOT NULL].
   * @param message an english short explicit message of the error for developers, what might have
   *     cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity} which describes the occured problem.
   */
  private ResponseEntity<String> buildResponseEntity(
      @NonNull final HttpStatus statusCode,
      @NonNull final RestError restErrors,
      @NonNull final String message) {
    RestException restException = new RestException(message, restErrors, statusCode);
    String jsonResponseEntityBody = restException.getMessage();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

    return new ResponseEntity<>(jsonResponseEntityBody, headers, statusCode);
  }

  @ExceptionHandler(MalformedBodyException.class)
  public ResponseEntity<ExceptionDto> handleRestExceptions(
      @NonNull final MalformedBodyException ex) {
    final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    return buildResponseEntity(ex, statusCode, "", ex.getMessage());
  }

  /**
   * Allows to build {@link RestError} instances from an instance of {@link BindingResult}.
   *
   * @param bindingResult an instance of {@link BindingResult} [OPTIONAL].
   * @return an instance of {@link List} which contains {@link RestError} instances if found else an
   *     empty {@link List}.
   */
  private RestError buildRestErrors(final BindingResult bindingResult) {

    if (bindingResult != null) {
      Map<String, String> dataMap = new HashMap<>();
      List<ObjectError> objectsErrors = bindingResult.getAllErrors();
      String errorCode = null;
      String developperMessage = null;
      if (!objectsErrors.isEmpty() && objectsErrors.get(0) != null) {
        String errorMessage = objectsErrors.get(0).getDefaultMessage();

        if (StringUtils.isNotBlank(errorMessage)) {
          String[] errorMessageArray = errorMessage.split(";");

          if (errorMessageArray.length == 2) {
            errorCode = errorMessageArray[0];
            developperMessage = errorMessageArray[1];
          }
        }
      }
      RestError error = new RestError(errorCode, dataMap, developperMessage, ExceptionLevel.ERROR);
      for (ObjectError objectError : objectsErrors) {
        buildAndAddRestError(objectError, dataMap);
      }
      return error;
    }

    return null;
  }

  private void buildAndAddRestError(
      @NonNull final ObjectError objectError, Map<String, String> dataMap) {
    if (objectError instanceof FieldError fieldError) {
      dataMap.put(
          fieldError.getObjectName() + "." + fieldError.getField(),
          (String) fieldError.getRejectedValue());
    }
  }
}
