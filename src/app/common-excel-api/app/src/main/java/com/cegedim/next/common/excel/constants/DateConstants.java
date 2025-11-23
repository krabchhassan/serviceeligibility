package com.cegedim.next.common.excel.constants;

import org.apache.commons.lang3.time.FastDateFormat;

/** This class defines date constants. */
public final class DateConstants {
  /** Pattern which defines datetime format pattern. */
  public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  /** Simple date format using datetime format pattern. */
  public static final FastDateFormat DATETIME_FORMAT =
      FastDateFormat.getInstance(DATETIME_FORMAT_PATTERN);

  /** Private constructor. */
  private DateConstants() {}
}
