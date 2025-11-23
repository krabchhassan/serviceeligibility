package com.cegedim.next.serviceeligibility.core.restexceptions.v1.model;

import com.cegedim.common.base.rest.exceptions.model.constants.RestExceptionConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/** This class defines REST exceptions for NEXT's project. */
public class RestException extends RuntimeException {

  /** Serial version UID. */
  private static final long serialVersionUID = 8279132596761166322L;

  /** Status code HTTP. */
  @SerializedName(RestExceptionConstants.PROPERTY_STATUS_CODE)
  private final HttpStatusCode statusCode;

  /**
   * All instances of {@link RestError} which defines the instance of {@link RestException}
   * [REQUIRED NOT NULL].
   */
  @Expose
  @SerializedName(RestExceptionConstants.PROPERTY_ERRORS)
  private final List<RestError> restErrors;

  /**
   * Constructor.
   *
   * @param message an english short explicit message of the error for developers, what might have
   *     cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @param restError an instance of {@link RestError} [REQUIRED NOT NULL].
   * @param statusCode status code HTTP [VALID STATUS CODE REQUIRED].
   */
  public RestException(final String message, final RestError restError, final int statusCode) {
    this(message, restError, HttpStatus.valueOf(statusCode));
  }

  /**
   * Constructor.
   *
   * @param message an english short explicit message of the error for developers, what might have
   *     cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @param restError an instance of {@link RestError} [REQUIRED NOT NULL].
   * @param statusCode status code HTTP [REQUIRED NOT NULL].
   */
  public RestException(
      final String message, final RestError restError, final HttpStatusCode statusCode) {
    this(message, Arrays.asList(restError), statusCode);
  }

  /**
   * Constructor.
   *
   * @param message an english short explicit message of the error for developers, what might have
   *     cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @param restErrors a list of eventually instances of {@link RestError} [REQUIRED NOT NULL].
   * @param statusCode status code HTTP [VALID STATUS CODE REQUIRED].
   */
  public RestException(
      final String message, final List<RestError> restErrors, final int statusCode) {
    this(message, restErrors, HttpStatus.valueOf(statusCode));
  }

  /**
   * Constructor.
   *
   * @param message an english short explicit message of the error for developers, what might have
   *     cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @param restErrors a list of eventually instances of {@link RestError} [REQUIRED NOT NULL].
   * @param statusCode status code HTTP [REQUIRED NOT NULL].
   */
  public RestException(
      final String message, final List<RestError> restErrors, final HttpStatusCode statusCode) {
    super(message);

    if (restErrors == null) {
      throw new IllegalArgumentException(
          "[RestException] => The parameter [restErrors] is required not null.");
    }

    if (statusCode == null) {
      throw new IllegalArgumentException(
          "[RestException] => The parameter [statusCode] is required not null and must be a valid HTTP status code.");
    }

    this.restErrors = restErrors;

    this.statusCode = statusCode;
  }

  /**
   * Allows to get a JSON representation of the current instance of {@link RestException}.
   *
   * @return an instance of {@link String}.
   */
  public String toJson() {
    final GsonBuilder builder = new GsonBuilder();
    builder.serializeNulls();
    builder.excludeFieldsWithoutExposeAnnotation();

    final Gson gson = builder.create();

    return gson.toJson(this);
  }

  /**
   * @return the statusCode
   */
  public HttpStatusCode getStatusCode() {
    return this.statusCode;
  }

  /**
   * @return the restErrors.
   */
  public List<RestError> getRestErrors() {
    return this.restErrors;
  }
}
