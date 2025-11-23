package com.cegedim.next.common.excel.error;

/** This class defines a specific exception for JUnit tests. */
public class TestException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   *
   * @param cause an instance of Throwable.
   */
  public TestException(final Throwable cause) {
    super(cause);
  }
}
