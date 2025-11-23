package com.cegedim.beyond.blb.recalcul.utils;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbServicePrestation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

public class Utils {

  private static ObjectMapper objectMapper;

  public static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.registerModule(new ParameterNamesModule());
      objectMapper.registerModule(getLocalDateTimeModule());
    }
    return objectMapper;
  }

  private static SimpleModule getLocalDateTimeModule() {
    final JavaTimeModule javaTimeModule = new JavaTimeModule();
    return javaTimeModule
        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_TIME))
        .addSerializer(
            LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
  }

  public static void setupEmbeddedMongo(final MongoTemplate mongoTemplate) throws IOException {
    mongoTemplate.insertAll(
        getObjectMapper()
            .readValue(
                TypeReference.class.getResourceAsStream("/servicePrestation.json"),
                new TypeReference<List<BddsToBlbServicePrestation>>() {}));
  }

  public static <T> boolean checkEntity(final T document) {
    try (final ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      return factory.getValidator().validate(document).isEmpty();
    }
  }
}
