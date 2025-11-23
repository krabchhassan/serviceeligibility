package com.cegedim.next.serviceeligibility.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonConfiguration {
  @Bean
  @Qualifier("authRestTemplate")
  public RestTemplate RestTemplate() {
    return new RestTemplate();
  }
}
