package com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.constants;

/**
 * This class defines some constants for instances of {@link
 * com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.RestError}.
 */
@SuppressWarnings("javadoc")
public final class RestErrorConstants {

  public static final String PROPERTY_ERROR_CODE = "error_code";
  public static final String PROPERTY_DATA = "data";
  public static final String PROPERTY_DEVELOPER_MESSAGE = "message";
  public static final String PROPERTY_LEVEL = "level";

  public static final String SUFFIX_UNDEFINED_INTERNAL_SERVER_ERROR = "00000";
  public static final String SUFFIX_INVALID_PARAMETER_EXCEPTION = "00001";
  public static final String SUFFIX_UNDEFINED_REST_CLIENT_EXCEPTION = "00002";
  public static final String SUFFIX_INVALID_REST_PARAMETER_EXCEPTION = "00003";
  public static final String SUFFIX_ERROR_REST_RESOURCE_NOT_FOUND = "00004";

  /** Private constructor. */
  private RestErrorConstants() {}
}
