package com.cegedim.beyond.undue.retention.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.utils.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {

  @MockBean public MongoTemplate mongoTemplate;

  @MockBean public OmuHelper omuHelper;

  @MockBean public MessageProducerWithApiKey messageProducerWithApiKey;

  @MockBean ParametrageCarteTPDaoImpl parametrageCarteTPDao;

  @MockBean AlmerysProductReferentialRepository almerysProductReferentialRepository;

  @Bean
  MessageService messageService() {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public RetentionDao retentionDao() {
    return new RetentionDaoImpl(mongoTemplate);
  }

  @Bean
  public RetentionService retentionService() {
    return new RetentionServiceImpl(retentionDao(), servicePrestationDao(), eventService());
  }

  @Bean
  public ServicePrestationService servicePrestationService() {
    return new ServicePrestationService(servicePrestationDao());
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @MockBean public BeyondPropertiesService beyondPropertiesService;

  @Bean
  EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @MockBean Producer producer;

  @MockBean BusinessEventProducer businessEventProducer;

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }
}
