package com.cegedim.next.consumer.worker;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestTools {

  private final ObjectMapper objectMapper =
      JsonMapper.builder() //
          .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false) //
          .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true) //
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) //
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) //
          .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true) //
          .serializationInclusion(JsonInclude.Include.NON_NULL) //
          .addModules(dateModules())
          .nodeFactory(JsonNodeFactory.withExactBigDecimals(true)) //
          .build();

  private SimpleModule dateModules() {
    final JavaTimeModule javaTimeModule = new JavaTimeModule();
    return javaTimeModule
        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_TIME))
        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ISO_DATE_TIME))
        .addDeserializer(LocalDate.class, new LocalDateDeserializer(ISO_DATE))
        .addSerializer(LocalDate.class, new LocalDateSerializer(ISO_DATE));
  }

  // --------------------
  // METHODS
  // --------------------
  public <T> T readAs(String ressourceFile, Class<T> clazz) throws IOException {
    try (InputStream is = TestTools.class.getResourceAsStream(ressourceFile)) {
      return objectMapper.readValue(is, clazz);
    }
  }
}
