package com.cegedim.next.common.excel.configuration;

import com.cegedim.next.common.excel.features.readdocument.service.ReadDocumentService;
import com.cegedim.next.common.excel.features.writedocument.service.WriteDocumentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {

  @Bean
  WriteDocumentService writeDocumentService() {
    return new WriteDocumentService();
  }

  @Bean
  ReadDocumentService readDocumentService() {
    return new ReadDocumentService();
  }
}
