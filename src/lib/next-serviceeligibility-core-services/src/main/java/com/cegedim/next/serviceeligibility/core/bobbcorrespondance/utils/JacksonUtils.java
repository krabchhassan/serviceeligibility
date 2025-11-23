package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JacksonUtils {

  private static final String LOCAL_DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  public static final SimpleModule JAVA_TIME_MODULE =
      new JavaTimeModule()
          .addSerializer(
              LocalDateTime.class,
              new LocalDateTimeSerializer(
                  DateTimeFormatter.ofPattern(LOCAL_DATETIME_FORMAT_PATTERN)));
  private static final ObjectMapper objectMapper =
      JsonMapper.builder()
          .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
          .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
          .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
          .serializationInclusion(JsonInclude.Include.NON_NULL)
          .addModules(new Jdk8Module(), JAVA_TIME_MODULE)
          .nodeFactory(JsonNodeFactory.withExactBigDecimals(true))
          .build();

  public static ObjectMapper objectMapper() {
    return objectMapper;
  }
}
