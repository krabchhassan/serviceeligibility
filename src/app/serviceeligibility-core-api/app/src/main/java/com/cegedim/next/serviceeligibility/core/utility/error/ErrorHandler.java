package com.cegedim.next.serviceeligibility.core.utility.error;

import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.exceptions.ContractElementNotFoundException;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDematExceptionCode;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.handler.DefaultErrorRestHandlerAbstract;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestException;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/** Basic error handler specialized for servicedirectory core api. */
@ControllerAdvice
@Slf4j
public class ErrorHandler extends DefaultErrorRestHandlerAbstract {

  private static final String IS_REQUIRED_IN_THE_BODY = " is required in the body";

  @Override
  public String getPrefixErrorCode() {
    return RestErrorConstants.PREFIX_CODE;
  }

  /**
   * When an instance of {@link RequestValidationException} is thrown by the controller, we complete
   * the response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link RequestValidationException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(RequestValidationException.class)
  public ResponseEntity<String> handleRestExceptions(@NonNull final RequestValidationException ex) {
    HttpStatus statusCode = ex.getStatusCode();
    String customStatusCode = ex.getCustomErrorCode();
    if (StringUtils.isBlank(customStatusCode)) {
      customStatusCode = RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION;
    }
    doLog(ex, statusCode);

    return buildResponseEntity(ex, statusCode, customStatusCode, ex.getMessage());
  }

  /**
   * When an instance of {@link UAPFunctionalException} is thrown by the controller, we complete the
   * response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link UAPFunctionalException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(UAPFunctionalException.class)
  public ResponseEntity<String> handleRestExceptions(@NonNull final UAPFunctionalException ex) {
    HttpStatus statusCode = ex.getStatusCode();
    String customStatusCode = ex.getCustomErrorCode();
    if (StringUtils.isBlank(customStatusCode)) {
      customStatusCode = RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION;
    }
    log.info(
        "UAP : A functionnal error has been catched, {} is thrown, Exception cause is {}",
        statusCode,
        ex.getMessage());

    return buildResponseEntity(ex, statusCode, customStatusCode, ex.getMessage());
  }

  @ExceptionHandler(RequestValidationListException.class)
  public ResponseEntity<String> handleRestListExceptions(
      @NonNull final RequestValidationListException ex) {
    HttpStatus statusCode = ex.getStatusCode();
    String customStatusCode = ex.getCustomErrorCode();
    if (StringUtils.isBlank(customStatusCode)) {
      customStatusCode = RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION;
    }
    doLog(ex, statusCode);

    return buildResponseEntity(ex, statusCode, customStatusCode, ex.getMessage());
  }

  public ResponseEntity<String> buildResponseEntity(
      final RequestValidationListException ex,
      final HttpStatus statusCode,
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

    List<RestError> errors = new ArrayList<>();
    for (RequestValidationException e : ex.getExceptions()) {
      String customErrorCode = e.getCustomErrorCode() != null ? e.getCustomErrorCode() : errorCode;
      RestError error =
          new RestError(customErrorCode, new HashMap<>(), e.getMessage(), ExceptionLevel.ERROR);
      errors.add(error);
    }

    RestException restException = new RestException(messageRestException, errors, statusCode);

    String jsonResponseEntityBody = restException.toJson();

    return new ResponseEntity<>(jsonResponseEntityBody, headers, statusCode);
  }

  @ExceptionHandler(BeneficiaryNotFoundException.class)
  public ResponseEntity<String> handleExceptions(final BeneficiaryNotFoundException exception) {

    final HttpStatus status = HttpStatus.NOT_FOUND;

    doLog(exception, status);

    return buildResponseEntity(exception, status, getErrorCode("00054"), status.toString());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleExceptions(final HttpMessageNotReadableException exception) {

    final HttpStatus status = HttpStatus.BAD_REQUEST;

    doLog(exception, status);

    Throwable cause = exception.getCause();
    if (cause instanceof JsonMappingException jsonEx
        && cause.getMessage().contains("La date de validité")) {
      String cleanMessage = jsonEx.getOriginalMessage();
      String body =
          "{ \"errors\": [ { \"error_code\": \"NEXT-SERVICEELIGIBILITY-CORE-00091\", "
              + "\"message\": [\""
              + cleanMessage
              + "\"], "
              + "\"level\": \"ERROR\" } ] }";
      return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    return buildResponseEntity(exception, status, getErrorCode("00055"), status.toString());
  }

  @ExceptionHandler(ContractNotFoundException.class)
  public ResponseEntity<String> handleExceptions(final ContractNotFoundException exception) {

    final HttpStatus status = HttpStatus.BAD_REQUEST;

    doLog(exception, status);

    return buildResponseEntity(exception, status, getErrorCode("00056"), status.toString());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<String> handleExceptions(final ResponseStatusException exception) {

    final HttpStatusCode status = exception.getStatusCode();

    doLog(exception, status);

    return buildResponseEntity(exception, status, getErrorCode("00057"), status.toString());
  }

  @ExceptionHandler(UnexpectedFileException.class)
  public ResponseEntity<String> handleExceptions(final UnexpectedFileException exception) {

    final HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

    doLog(exception, status);

    return buildResponseEntity(exception, status, getErrorCode("00058"), status.toString());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleExceptions(final AccessDeniedException exception) {

    final HttpStatus status = HttpStatus.FORBIDDEN;

    doLog(exception, status);

    return buildResponseEntity(exception, status, getErrorCode("00000"), status.toString());
  }

  @ExceptionHandler(ElasticIndexAliasException.class)
  public ResponseEntity<String> handleExceptions(final ElasticIndexAliasException exception) {

    final HttpStatus status = exception.getStatusCode();

    doLog(exception, status);

    return buildResponseEntity(
        exception, status, exception.getCustomErrorCode(), status.toString());
  }

  /**
   * When an instance of {@link CarteDematException} is thrown by the controller, we complete the
   * response entity body with only {@link RestException}'s properties.
   *
   * @param ex instance of {@link CarteDematException} [REQUIRED NOT NULL].
   * @return an instance of {@link ResponseEntity}.
   */
  @ExceptionHandler(CarteDematException.class)
  public ResponseEntity<String> handleRestExceptions(@NonNull final CarteDematException ex) {
    HttpStatus statusCode = null;
    String customStatusCode = null;
    String libelle = null;

    if (ex.getExceptionCode() != null) {
      statusCode = getStatus(ex.getExceptionCode());
      customStatusCode = ex.getExceptionCode().getCode();

      libelle = ex.getExceptionCode().getLibelle();
      if (StringUtils.isNotBlank(ex.getCommentaire())) {
        libelle += " - " + ex.getCommentaire();
      }
    }

    if (StringUtils.isBlank(customStatusCode)) {
      customStatusCode = RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION;
    }

    log.info(
        "Carte demat' : A functionnal error has been catched, {} is thrown, Exception cause is {} - {}",
        statusCode,
        ex.getMessage(),
        ex.getCommentaire());

    return buildResponseEntity(ex, statusCode, customStatusCode, libelle);
  }

  @ExceptionHandler(ContractElementNotFoundException.class)
  public ResponseEntity<String> handleExceptions(final ContractElementNotFoundException exception) {

    final HttpStatus status = HttpStatus.NOT_FOUND;

    doLog(exception, status);

    return buildResponseEntity(
        exception, status, RestErrorConstants.ERROR_CONTRACT_ELEMENT_NOT_FOUND, status.toString());
  }

  // Determine HttpStatus from CarteDematException
  private HttpStatus getStatus(CarteDematExceptionCode cardExceptionCode)
      throws ValidationException {
    int intCode = Integer.parseInt(cardExceptionCode.getCode());

    // code == 0
    if (intCode == 0) {
      return HttpStatus.OK;
    }
    // 1 <= code <= 9999
    else if (intCode <= 9999) {
      return HttpStatus.BAD_REQUEST;
    }

    // Default, should never happen
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  /**
   * missing attribute : voir handleMissingValueInternal dans RequestParamMethodArgumentResolver
   * (ligne 217)
   */
  @Override
  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ResponseEntity<String> handleExceptions(Exception exception) {
    final HttpStatus status = HttpStatus.BAD_REQUEST;

    doLog(exception, status);

    return buildResponseEntity(exception, status, getErrorCode("00054"), status.toString());
  }

  @Override
  public String getErrorCode(String suffixErrorCode) {
    return this.getPrefixErrorCode() + "-" + suffixErrorCode;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("error_code", RestErrorConstants.ERROR_CODE_REQUEST_VALIDATION_EXCEPTION);
    errorDetails.put("level", "ERROR");

    List<String> messages =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error -> {
                  String field = error.getField();
                  String fieldName = field.substring(field.lastIndexOf('.') + 1);

                  return switch (fieldName) {
                    case "number" -> "The number " + field + IS_REQUIRED_IN_THE_BODY;
                    case "subscriberId" -> "The subscriberId " + field + IS_REQUIRED_IN_THE_BODY;
                    case "contractNumber" ->
                        "The contractNumber " + field + IS_REQUIRED_IN_THE_BODY;
                    default -> error.getDefaultMessage();
                  };
                })
            .toList();

    errorDetails.put("message", messages);

    return ResponseEntity.badRequest().body(Map.of("errors", List.of(errorDetails)));
  }

  @ExceptionHandler(GuaranteeException.class)
  public ResponseEntity<Map<String, Object>> handleExceptions(final GuaranteeException exception) {

    final HttpStatus status = exception.getStatusCode();
    Map<String, Object> errors = new HashMap<>();
    errors.put("error_code", RestErrorConstants.ERROR_GUARANTEE);
    errors.put("level", "ERROR");
    errors.put("message", List.of(exception.getMessage()));

    doLog(exception, status);

    return ResponseEntity.badRequest().body(Map.of("errors", List.of(errors)));
  }

  @ExceptionHandler(UserNotAuthorizedForGTException.class)
  public ResponseEntity<Map<String, Object>> handleExceptions(
      final UserNotAuthorizedForGTException exception) {

    final HttpStatus status = exception.getStatusCode();
    Map<String, Object> errors = new HashMap<>();
    errors.put("error_code", RestErrorConstants.ERROR_USER_NOT_AUTHORIZED);
    errors.put("level", "ERROR");
    errors.put("message", List.of(exception.getMessage()));

    doLog(exception, status);

    return ResponseEntity.unprocessableEntity().body(Map.of("errors", List.of(errors)));
  }
}
