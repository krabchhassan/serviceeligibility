package com.cegedim.next.consumer.worker.utils;

import org.apache.commons.lang3.StringUtils;

public final class Constants {

  /** /** Private constructor. */
  private Constants() {}

  public static Integer stringToInt(String s) {
    try {
      if (StringUtils.isNotBlank(s)) {
        String numericString = s.replaceAll("[^0-9.]", "");
        if (numericString.length() > 6) {
          numericString = numericString.substring(0, 6);
        }
        return Integer.valueOf(numericString);
      }
    } catch (NumberFormatException e) {
      return 0;
    }
    return 0;
  }
}
