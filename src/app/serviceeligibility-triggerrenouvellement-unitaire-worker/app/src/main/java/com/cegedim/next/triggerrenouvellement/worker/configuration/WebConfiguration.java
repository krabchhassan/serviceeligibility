package com.cegedim.next.triggerrenouvellement.worker.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** This class allows to specify configuration related to the Web MVC part. */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  private static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  /**
   * Allows to configure the Jackson mapper.
   *
   * @param objectMapper an instance of {@link ObjectMapper}.
   */
  @Autowired(required = true)
  public void configureJacksonMapper(final ObjectMapper objectMapper) {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(buildTimeModule());
  }

  /**
   * Allows to build a custom SimpleModule for the Jackson object mapper.
   *
   * @return an instance of {@link SimpleModule}.
   */
  private SimpleModule buildTimeModule() {
    final SimpleModule simpleModule = new SimpleModule();

    simpleModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
    simpleModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);

    simpleModule.addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
    simpleModule.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);

    return simpleModule;
  }

  /**
   * Allows to define a {@link LocalDate} {@link Formatter}.
   *
   * @return an instance of {@link Formatter} typed for {@link LocalDate}.
   */
  @Bean
  public Formatter<LocalDate> localDateFormatter() {
    return new Formatter<>() {
      @Override
      public LocalDate parse(final String text, final Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN));
      }

      @Override
      public String print(final LocalDate localDate, final Locale locale) {
        return DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN).format(localDate);
      }
    };
  }

  /**
   * Allows to define a {@link LocalDateTime} {@link Formatter}.
   *
   * @return an instance of {@link Formatter} typed for {@link LocalDateTime}.
   */
  @Bean
  public Formatter<LocalDateTime> localDateTimeFormatter() {
    return new Formatter<>() {
      @Override
      public LocalDateTime parse(final String text, final Locale locale) throws ParseException {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN));
      }

      @Override
      public String print(final LocalDateTime localDateTime, final Locale locale) {
        return DateTimeFormatter.ofPattern(DATETIME_FORMAT_PATTERN).format(localDateTime);
      }
    };
  }
}
