package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateTimeTypeAdapter
    implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  @Override
  public JsonElement serialize(
      final LocalDateTime date, final Type typeOfSrc, final JsonSerializationContext context) {
    // TODO @NICOLAS ROUCH: quel format mettre ici ?
    String string = ZonedDateTime.of(date, ZoneId.of("Etc/GMT")).toString();
    return new JsonPrimitive(string);
  }

  @Override
  public LocalDateTime deserialize(
      final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {
    return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
  }
}
