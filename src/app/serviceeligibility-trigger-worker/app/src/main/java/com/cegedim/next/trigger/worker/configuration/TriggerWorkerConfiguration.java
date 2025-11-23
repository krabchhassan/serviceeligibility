package com.cegedim.next.trigger.worker.configuration;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.config.TriggerConfiguration;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.trigger.worker.service.TriggerWorkerProcessingService;
import com.cegedim.next.trigger.worker.service.TriggerWorkerRecyclingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class TriggerWorkerConfiguration extends TriggerConfiguration {

  public TriggerWorkerConfiguration(
      BeyondPropertiesService beyondPropertiesService,
      ObjectMapper objectMapper,
      RestConnector restConnector) {
    super(beyondPropertiesService, objectMapper, restConnector);
  }

  @Bean
  public TriggerWorkerRecyclingService triggerWorkerRecyclingService(
      Producer producer,
      MongoTemplate mongoTemplate,
      TriggerRecyclageService triggerRecyclageService,
      TriggerService triggerService,
      SasContratService sasContratService,
      AuthenticationFacade authenticationFacade,
      MongoClient mongoClient) {
    return new TriggerWorkerRecyclingService(
        triggerBuildDeclarationNewService(mongoTemplate, triggerService, sasContratService),
        triggerCreationService(
            mongoTemplate,
            authenticationFacade,
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
  public TriggerWorkerProcessingService triggerWorkerProcessingService(
      Producer producer,
      MongoTemplate mongoTemplate,
      TriggerRecyclageService triggerRecyclageService,
      TriggerService triggerService,
      SasContratService sasContratService,
      MongoClient mongoClient,
      EventService eventService) {
    return new TriggerWorkerProcessingService(
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
        triggerRecyclageService,
        eventService);
  }
}
