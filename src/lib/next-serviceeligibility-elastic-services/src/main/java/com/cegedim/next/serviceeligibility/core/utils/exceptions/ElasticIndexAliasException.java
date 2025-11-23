package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ElasticIndexAliasException extends RuntimeException {
  /** Serial version UID. */
  private static final long serialVersionUID = 1L;

  /** The status code. */
  @Getter private final HttpStatus statusCode;

  @Getter private final String customErrorCode;

  /**
   * Constructor.
   *
   * @param message message of the exception.
   */
  public ElasticIndexAliasException(final String message) {
    super(message);
    this.statusCode = HttpStatus.BAD_REQUEST;
    this.customErrorCode = RestErrorConstants.ERROR_INDEX_ALIAS_ELASTIC;
  }
}
