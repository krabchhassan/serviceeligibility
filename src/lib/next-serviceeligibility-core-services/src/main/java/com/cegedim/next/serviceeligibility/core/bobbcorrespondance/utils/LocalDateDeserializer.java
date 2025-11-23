package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

  @Override
  public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String dateText = jsonParser.getText();
    try {
      LocalDateTime dateTime = LocalDateTime.parse(dateText, dateTimeFormatter);
      return dateTime.toLocalDate();
    } catch (DateTimeParseException e) {
      try {
        return LocalDate.parse(dateText, dateFormatter);
      } catch (DateTimeParseException ex) {
        throw new IOException("Failed to parse date: " + dateText, ex);
      }
    }
  }
}
