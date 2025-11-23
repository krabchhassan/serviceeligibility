package com.cegedim.next.serviceeligibility.core.cucumber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {
  public static final LocalDate TODAY_DATE = LocalDate.now();
  public static final LocalDateTime TODAY_DATE_TIME = LocalDateTime.now();
  public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  public static String stringDateFromDelta(int d) {
    LocalDate plusOne = TODAY_DATE.plusDays(d);
    return plusOne.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static String formatDateWithoutTimestamp(LocalDate date) {
    date = date != null ? date : TODAY_DATE;
    return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }
}
