package com.cegedim.next.serviceeligibility.core.configuration;

import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperWebServiceCardV4;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperWebServiceCardV4Impl;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.ws.CardServiceV4;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardWebServiceConfiguration {
  @Bean
  public MapperWebServiceCardV4 mapperCardV4() {
    return new MapperWebServiceCardV4Impl();
  }

  @Bean
  public CardServiceV4 cardServiceV4() {
    return new CardServiceV4(mapperCardV4());
  }
}
