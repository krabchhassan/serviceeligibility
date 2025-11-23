package com.cegedim.next.serviceeligibility.exporttriggerinfos.configuration;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceServiceImpl;
import com.cegedim.next.serviceeligibility.core.config.s3.S3Config;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.SasContratDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDaoImpl;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({S3Config.class, OmuHelperConfiguration.class})
public class ExportTriggerInfosConfiguration {
  private final BeyondPropertiesService beyondPropertiesService;

  public ExportTriggerInfosConfiguration(BeyondPropertiesService beyondPropertiesService) {
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public S3Service s3Service(S3StorageService bddS3StorageService) {
    return new S3Service(bddS3StorageService, new ObjectMapper(), beyondPropertiesService);
  }

  @Bean
  public TriggerCSVService triggerCSVService(
      MongoTemplate mongoTemplate,
      BusinessEventProducer businessEventProducer,
      S3StorageService bddS3StorageService) {
    return new TriggerCSVService(
        triggerRecyclageService(mongoTemplate, businessEventProducer),
        triggerService(mongoTemplate),
        s3Service(bddS3StorageService),
        beyondPropertiesService);
  }

  @Bean
  public TriggerRecyclageService triggerRecyclageService(
      MongoTemplate mongoTemplate, BusinessEventProducer businessEventProducer) {
    return new TriggerRecyclageService(
        triggerService(mongoTemplate),
        eventService(businessEventProducer),
        new SasContratService(new SasContratDaoImpl(mongoTemplate)),
        beyondPropertiesService);
  }

  @Bean
  public EventService eventService(BusinessEventProducer businessEventProducer) {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public TriggerService triggerService(MongoTemplate mongoTemplate) {
    return new TriggerService(new TriggerDaoImpl(mongoTemplate, bddAuth()));
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
  public ParametreBddService parametreBddService(MongoTemplate mongoTemplate) {
    return new ParametreBddServiceImpl(new ParametreBddDaoImpl(mongoTemplate));
  }

  @Bean
  public DeclarantService declarantService(MongoTemplate mongoTemplate) {
    return new DeclarantService(new DeclarantDaoImpl(mongoTemplate));
  }

  @Bean
  public BobbCorrespondanceService bobbCorrespondanceService() {
    return new BobbCorrespondanceServiceImpl(contractElementRepository(), versionsRepository());
  }

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public VersionsRepository versionsRepository() {
    return new VersionsRepository();
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
  public AuthenticationFacade bddAuth() {
    return new AuthenticationFacadeImpl();
  }
}
