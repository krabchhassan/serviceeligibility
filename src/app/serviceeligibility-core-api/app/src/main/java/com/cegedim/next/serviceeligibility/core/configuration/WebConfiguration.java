package com.cegedim.next.serviceeligibility.core.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** This class allows to specify configuration related to the Web MVC part. */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Bean
  public Module springDataPageModule() {
    return new SimpleModule()
        .addSerializer(
            Page.class,
            new JsonSerializer<>() {
              @Override
              public void serialize(
                  final Page page,
                  final JsonGenerator jsonGenerator,
                  final SerializerProvider serializers)
                  throws IOException {

                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectField("data", page.getContent());
                jsonGenerator.writeObjectFieldStart("paging");
                jsonGenerator.writeNumberField("page", page.getNumber());
                jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
                jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
                jsonGenerator.writeNumberField("perPage", page.getSize());
                jsonGenerator.writeEndObject();
              }
            });
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
    final PageableHandlerMethodArgumentResolver resolver =
        new PageableHandlerMethodArgumentResolver();
    resolver.setOneIndexedParameters(true);
    argumentResolvers.add(resolver);
  }
}
