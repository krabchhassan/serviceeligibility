package com.cegedim.next.serviceeligibility.core.cucumber.utils;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class TransformUtils {
  public static final Parser DEFAULT =
      parser("%%CURRENT_YEAR%%", Year.now().toString())
          .then("%%NEXT_YEAR%%", Year.now().plusYears(1).toString())
          .then("%%LAST_YEAR%%", Year.now().minusYears(1).toString())
          .then("%%LAST2_YEARS%%", Year.now().minusYears(2).toString())
          .then("%%2_YEAR%%", Year.now().plusYears(2).toString())
          .then("%%TODAY%%", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
          .then("%%TODAY_SLASHED%%", LocalDate.now().format(DateUtils.YYYY_MM_DD));

  public Parser parser(String parseKey, String toAdd) {
    return new Parser().then(parseKey, toAdd);
  }

  public Parser parser(Map<String, String> keyValues) {
    Parser parser = new Parser();
    keyValues.forEach(parser::then);
    return parser;
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Parser {
    private final LinkedHashMap<String, String> parsePairs = new LinkedHashMap<>();

    /**
     * Add the parsing pair. Order matter if using mathing String. ex: endBirthday and
     * endBirthdayOnNextYear, you need tu put the longest first
     */
    public Parser then(String parseKey, String toAdd) {
      parsePairs.put(parseKey, toAdd);
      return this;
    }

    public String parse(String parseable) {
      String res = parseable;
      for (Map.Entry<String, String> entry : parsePairs.entrySet()) {
        res = StringUtils.replace(res, entry.getKey(), entry.getValue());
      }
      return res;
    }

    public Map<String, String> parseMap(Map<String, String> parseable) {
      Map<String, String> res = new HashMap<>();
      for (Map.Entry<String, String> entry : parseable.entrySet()) {
        res.put(entry.getKey(), parse(entry.getValue()));
      }
      return res;
    }

    public List<Map<String, String>> parseMaps(List<Map<String, String>> parseables) {
      List<Map<String, String>> res = new ArrayList<>();
      parseables.forEach(parseable -> res.add(parseMap(parseable)));
      return res;
    }
  }
}
