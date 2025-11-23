package com.cegedim.next.serviceeligibility.core.bdd.backend.utility;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * This class defines a single Rest object.
 *
 * @param <T> Type of the content of the object.
 */
public class RestObjectSingle<T> {

  /** Content of the object. */
  @Valid
  @NotNull(message = "Missing data object")
  @Getter
  @Setter
  private T data;

  /** Constructor */
  public RestObjectSingle() {}

  /**
   * Constructor
   *
   * @param content content to be inserted in data part of the object
   */
  public RestObjectSingle(final T content) {
    this.data = content;
  }
}
