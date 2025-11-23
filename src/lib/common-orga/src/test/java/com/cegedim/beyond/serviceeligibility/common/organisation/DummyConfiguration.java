package com.cegedim.beyond.serviceeligibility.common.organisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DummyConfiguration {

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
