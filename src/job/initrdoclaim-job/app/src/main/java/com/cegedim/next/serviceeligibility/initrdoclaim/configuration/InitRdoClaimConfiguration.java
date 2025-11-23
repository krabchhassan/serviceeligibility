package com.cegedim.next.serviceeligibility.initrdoclaim.configuration;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.LotDao;
import com.cegedim.next.serviceeligibility.core.dao.LotDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.RDOServicePrestationDAOImpl;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDaoImpl;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapperImpl;
import com.cegedim.next.serviceeligibility.core.services.RDOServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
@RequiredArgsConstructor
public class InitRdoClaimConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public MongoTemplate template(MongoDatabaseFactory mongoDbFactory) {
    return new MongoTemplate(mongoDbFactory);
  }

  @Bean
  public RDOServicePrestationService rdoServicePrestationService(MongoTemplate mongoTemplate) {
    return new RDOServicePrestationService(
        new RDOServicePrestationDAOImpl(mongoTemplate),
        assureRdoServicePrestationsMapper(),
        beyondPropertiesService);
  }

  @Bean
  public ServicePrestationService servicePrestationService(MongoTemplate mongoTemplate) {
    return new ServicePrestationService(servicePrestationDao(mongoTemplate));
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao(MongoTemplate mongoTemplate) {
    return new ServicePrestationDaoImpl(
        mongoTemplate, lotDao(mongoTemplate), beyondPropertiesService);
  }

  @Bean
  public LotDao lotDao(MongoTemplate mongoTemplate) {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  AssureRdoServicePrestationsMapper assureRdoServicePrestationsMapper() {
    return new AssureRdoServicePrestationsMapperImpl();
  }
}
