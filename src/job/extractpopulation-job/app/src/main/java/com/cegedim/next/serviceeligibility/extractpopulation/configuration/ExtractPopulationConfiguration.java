package com.cegedim.next.serviceeligibility.extractpopulation.configuration;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.LotDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.extractpopulation.services.CsvFileService;
import com.cegedim.next.serviceeligibility.extractpopulation.services.JsonFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
@RequiredArgsConstructor
public class ExtractPopulationConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public ServicePrestationService servicePrestationService(MongoTemplate mongoTemplate) {
    return new ServicePrestationService(
        new ServicePrestationDaoImpl(
            mongoTemplate, new LotDaoImpl(mongoTemplate), beyondPropertiesService));
  }

  @Bean
  public JsonFileService jsonFileService(MongoTemplate mongoTemplate) {
    return new JsonFileService(servicePrestationService(mongoTemplate));
  }

  @Bean
  public CsvFileService csvFileService() {
    return new CsvFileService();
  }
}
