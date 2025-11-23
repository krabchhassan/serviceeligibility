package com.cegedim.next.serviceeligibility.core.restexceptions.v1.model;

import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.constants.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.restexceptions.v1.model.enums.ExceptionLevel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

/** This class defines a rest error for NEXT's project. */
public class RestError {

  /** Error code which must be in format {MODULE}-XXXXX [REQUIRED NOT NULL]. */
  @Expose
  @SerializedName(RestErrorConstants.PROPERTY_ERROR_CODE)
  private final String errorCode;

  /** Map containing the concerned datas [OPTIONAL]. */
  @Expose
  @SerializedName(RestErrorConstants.PROPERTY_DATA)
  private final Map<String, String> dataMap;

  /**
   * An english short explicit message of the error for developers, what might have cause it and
   * possibly a fixing proposal [REQUIRED NOT NULL].
   */
  @Expose
  @SerializedName(RestErrorConstants.PROPERTY_DEVELOPER_MESSAGE)
  private final String developerMessage;

  /** Level of the exception [REQUIRED NOT NULL]. */
  @Expose
  @SerializedName(RestErrorConstants.PROPERTY_LEVEL)
  private final ExceptionLevel level;

  /**
   * Constructor.
   *
   * @param errorCode error code which must be in format {MODULE}-XXXXX [REQUIRED NOT NULL].
   * @param developerMessage an english short explicit message of the error for developers, what
   *     might have cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @param exceptionLevel level of the exception [REQUIRED NOT NULL].
   */
  public RestError(
      final String errorCode, final String developerMessage, final ExceptionLevel exceptionLevel) {
    this(errorCode, new HashMap<>(), developerMessage, exceptionLevel);
  }

  /**
   * Constructor.
   *
   * @param errorCode error code which must be in format {MODULE}-XXXXX [REQUIRED NOT NULL].
   * @param dataMap map containing the concerned datas [OPTIONAL].
   * @param developerMessage an english short explicit message of the error for developers, what
   *     might have cause it and possibly a fixing proposal [REQUIRED NOT NULL].
   * @param level level of the exception [REQUIRED NOT NULL].
   */
  public RestError(
      final String errorCode,
      final Map<String, String> dataMap,
      final String developerMessage,
      final ExceptionLevel level) {
    this.errorCode = errorCode;
    this.dataMap = dataMap;
    this.developerMessage = developerMessage;
    this.level = level;
  }

  /**
   * @return the errorCode.
   */
  public String getErrorCode() {
    return this.errorCode;
  }

  /**
   * @return the dataMap.
   */
  public Map<String, String> getDataMap() {
    return this.dataMap;
  }

  /**
   * @return the developerMessage.
   */
  public String getDeveloperMessage() {
    return this.developerMessage;
  }

  /**
   * @return the level.
   */
  public ExceptionLevel getLevel() {
    return this.level;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("RestError {\n\terrorCode: ");
    builder.append(this.errorCode);
    builder.append("\n\tdataMap: ");
    builder.append(this.dataMap);
    builder.append("\n\tdeveloperMessage: ");
    builder.append(this.developerMessage);
    builder.append("\n\tlevel: ");
    builder.append(this.level);
    builder.append("\n}");
    return builder.toString();
  }
}
