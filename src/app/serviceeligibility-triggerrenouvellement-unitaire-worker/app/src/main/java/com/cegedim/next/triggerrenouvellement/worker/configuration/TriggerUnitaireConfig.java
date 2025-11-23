package com.cegedim.next.triggerrenouvellement.worker.configuration;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.next.serviceeligibility.core.config.TransverseConfiguration;
import com.cegedim.next.serviceeligibility.core.config.TriggerConfiguration;
import com.cegedim.next.serviceeligibility.core.config.s3.S3Config;
import com.cegedim.next.serviceeligibility.core.dao.TriggerCountDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerCountService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerProcessingService;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerRecyclingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({S3Config.class, TransverseConfiguration.class})
public class TriggerUnitaireConfig extends TriggerConfiguration {

  public TriggerUnitaireConfig(
      BeyondPropertiesService beyondPropertiesService,
      ObjectMapper objectMapper,
      RestConnector restConnector) {
    super(beyondPropertiesService, objectMapper, restConnector);
  }

  @Bean
  public TriggerUnitaireWorkerRecyclingService triggerWorkerRecyclingService(
      Producer producer,
      MongoTemplate mongoTemplate,
      TriggerRecyclageService triggerRecyclageService,
      TriggerService triggerService,
      SasContratService sasContratService,
      MongoClient mongoClient) {
    return new TriggerUnitaireWorkerRecyclingService(
        triggerBuildDeclarationNewService(mongoTemplate, triggerService, sasContratService),
        triggerCreationService(
            mongoTemplate,
            authenticationFacade(),
            sasContratService,
            triggerService,
            mongoClient,
            producer),
        triggerService,
        sasContratService,
        servicePrestationDao(mongoTemplate),
        producer,
        triggerRecyclageService);
  }

  @Bean
  public TriggerUnitaireWorkerProcessingService triggerWorkerProcessingService(
      Producer producer,
      MongoTemplate mongoTemplate,
      TriggerRecyclageService triggerRecyclageService,
      TriggerService triggerService,
      SasContratService sasContratService,
      MongoClient mongoClient) {
    return new TriggerUnitaireWorkerProcessingService(
        triggerBuildDeclarationNewService(mongoTemplate, triggerService, sasContratService),
        triggerCreationService(
            mongoTemplate,
            authenticationFacade(),
            sasContratService,
            triggerService,
            mongoClient,
            producer),
        triggerService,
        sasContratService,
        servicePrestationDao(mongoTemplate),
        producer,
        triggerRecyclageService);
  }

  @Bean
  public TriggerCountService triggerCountService(MongoTemplate mongoTemplate) {
    return new TriggerCountService(new TriggerCountDaoImpl(mongoTemplate));
  }

  @Bean
  public TriggerCSVService triggerCSVService(
      S3StorageService bddS3StorageService,
      TriggerRecyclageService triggerRecyclageService,
      TriggerService triggerService,
      ObjectMapper objectMapper) {
    return new TriggerCSVService(
        triggerRecyclageService,
        triggerService,
        s3Service(bddS3StorageService, objectMapper),
        beyondPropertiesService);
  }

  @Bean
  public S3Service s3Service(S3StorageService bddS3StorageService, ObjectMapper objectMapper) {
    return new S3Service(bddS3StorageService, objectMapper, beyondPropertiesService);
  }
}
