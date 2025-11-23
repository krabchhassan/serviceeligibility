package com.cegedim.next.triggerrenouvellement.worker.config;

import com.cegedim.beyond.business.organisation.facade.OrganisationService;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.kafka.dao.TriggerKafkaDao;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.triggerrenouvellement.worker.kafka.ConsumerDemandeRenouv;
import com.cegedim.next.triggerrenouvellement.worker.kafka.ConsumerUpdateRenouv;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestOperations;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {

  @MockBean public MongoClient mongoClient;

  @MockBean public BusinessEventProducer businessEventProducer;

  @MockBean public Producer producer;

  @MockBean public MongoTemplate mongoTemplate;

  @MockBean public OrganisationService organisationService;

  @MockBean public BeyondPropertiesService beyondPropertiesService;

  @MockBean public RestOperations apiKeyTokenRestTemplate;

  @Bean
  public TriggerService triggerService() {
    return new TriggerService(triggerDao());
  }

  @Bean
  public TriggerDao triggerDao() {
    return new TriggerDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public ConsumerDemandeRenouv consumerDemandeRenouv() {
    return new ConsumerDemandeRenouv(
        triggerService(), triggerKafkaService(), triggerCountService());
  }

  @Bean
  public TriggerCountService triggerCountService() {
    return new TriggerCountService(new TriggerCountDaoImpl(mongoTemplate));
  }

  @Bean
  public ConsumerUpdateRenouv consumerUpdateRenouv() {
    return new ConsumerUpdateRenouv(
        triggerService(), triggerRecyclageService(), triggerCSVService());
  }

  @MockBean(name = "bddsCacheManager")
  CacheManager cacheManager;

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public ServicePrestationDao servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService() {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(), lotDao(), mapperParametrageCarteTP());
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean("bddAuth")
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public SasContratService sasContratService() {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public ContractElementService contractElementService() {
    return new ContractElementService(new ContractElementRepository());
  }

  @Bean
  public ProductElementService productElementService() {
    return new ProductElementService(contractElementService());
  }

  @Bean
  public ServicePrestationService contratService() {
    return new ServicePrestationService(servicePrestationDao());
  }

  @Bean
  public RestConnector restConnector() {
    return new RestConnector(apiKeyTokenRestTemplate);
  }

  @Bean
  public TriggerCreationService triggerCreationService() {
    return new TriggerCreationService(
        parametrageCarteTPService(),
        sasContratService(),
        producer,
        triggerService(),
        mongoClient,
        triggerMapper(),
        contratService(),
        beyondPropertiesService);
  }

  @Bean
  public TriggerKafkaService triggerKafkaService() {
    return new TriggerKafkaService();
  }

  @Bean
  public TriggerKafkaDao triggerKafkaDao() {
    return new TriggerKafkaDao();
  }

  @Bean
  public TriggerRecyclageService triggerRecyclageService() {
    return new TriggerRecyclageService(
        triggerService(), eventService(), sasContratService(), beyondPropertiesService);
  }

  @MockBean public S3Service s3Service;

  @Bean
  public TriggerCSVService triggerCSVService() {
    return new TriggerCSVService(
        triggerRecyclageService(), triggerService(), s3Service, beyondPropertiesService);
  }

  @Bean
  public TriggerMapper triggerMapper() {
    return new TriggerMapper(
        contractElementService, productElementService(), parametreBddService(), declarantService());
  }

  @MockBean public ContractElementService contractElementService;

  @Bean
  public ParametreBddDaoImpl parametreBddDao() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public ParametreBddService parametreBddService() {
    return new ParametreBddServiceImpl(parametreBddDao());
  }

  @MockBean public ParametreBddService parametreBddService;

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }
}
