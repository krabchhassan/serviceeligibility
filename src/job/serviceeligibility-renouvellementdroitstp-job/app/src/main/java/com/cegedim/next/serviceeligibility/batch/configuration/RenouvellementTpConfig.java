package com.cegedim.next.serviceeligibility.batch.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {BddsToBlbTrackingRepo.class})
@Import({OmuHelperConfiguration.class})
@RequiredArgsConstructor
public class RenouvellementTpConfig {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public TriggerService triggerService(MongoTemplate mongoTemplate) {
    return new TriggerService(new TriggerDaoImpl(mongoTemplate, bddAuth()));
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService(MongoTemplate mongoTemplate) {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(mongoTemplate), lotDao(mongoTemplate), mapperParametrageCarteTP());
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao(MongoTemplate mongoTemplate) {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, bddAuth());
  }

  @Bean
  public AuthenticationFacade bddAuth() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean
  public SasContratService sasContratService(MongoTemplate mongoTemplate) {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public Producer producer(MessageProducerWithApiKey messageProducer, MongoTemplate mongoTemplate) {
    return new Producer(
        triggerService(mongoTemplate),
        sasContratService(mongoTemplate),
        messageProducer,
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
  public TriggerMapper triggerMapper(MongoTemplate mongoTemplate) {
    return new TriggerMapper(
        contractElementService(),
        productElementService(),
        parametreBddService(mongoTemplate),
        declarantService(mongoTemplate));
  }

  @Bean
  public ContractElementService contractElementService() {
    return new ContractElementService(contractElementRepository());
  }

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public ProductElementService productElementService() {
    return new ProductElementService(contractElementService());
  }

  @Bean
  public ParametreBddService parametreBddService(MongoTemplate mongoTemplate) {
    return new ParametreBddServiceImpl(parametreBddDao(mongoTemplate));
  }

  @Bean
  public ParametreBddDao parametreBddDao(MongoTemplate mongoTemplate) {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarantService declarantService(MongoTemplate mongoTemplate) {
    return new DeclarantService(declarantDao(mongoTemplate));
  }

  @Bean
  public DeclarantDao declarantDao(MongoTemplate mongoTemplate) {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public LotDao lotDao(MongoTemplate mongoTemplate) {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public TriggerCreationService triggerCreationService(
      MongoTemplate mongoTemplate,
      MessageProducerWithApiKey messageProducer,
      MongoClient mongoClient) {
    return new TriggerCreationService(
        parametrageCarteTPService(mongoTemplate),
        sasContratService(mongoTemplate),
        producer(messageProducer, mongoTemplate),
        triggerService(mongoTemplate),
        mongoClient,
        triggerMapper(mongoTemplate),
        servicePrestationService(mongoTemplate),
        beyondPropertiesService);
  }

  @Bean
  public HistoriqueExecutionsRenouvellementDao historiqueExecutionsRenouvellementDao(
      MongoTemplate mongoTemplate) {
    return new HistoriqueExecutionsRenouvellementDaoImpl(mongoTemplate);
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }
}
