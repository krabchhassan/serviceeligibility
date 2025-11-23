package com.cegedim.next.serviceeligibility.core.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.SasContratDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.client.RestOperations;

@Configuration
@RequiredArgsConstructor
public class TransverseConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  private final RestOperations apiKeyTokenRestTemplate;

  @Bean
  public RestConnector restConnector() {
    return new RestConnector(apiKeyTokenRestTemplate);
  }

  @Bean
  public Producer producer(
      MessageProducerWithApiKey messageProducerWithApiKey, MongoTemplate mongoTemplate) {
    return new Producer(
        triggerService(mongoTemplate),
        sasContratService(mongoTemplate),
        messageProducerWithApiKey,
        beyondPropertiesService);
  }

  @Bean
  public SasContratService sasContratService(MongoTemplate mongoTemplate) {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public TriggerService triggerService(MongoTemplate mongoTemplate) {
    return new TriggerService(triggerDao(mongoTemplate));
  }

  @Bean
  public EventService eventService(BusinessEventProducer businessEventProducer) {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public TriggerDao triggerDao(MongoTemplate mongoTemplate) {
    return new TriggerDaoImpl(mongoTemplate, bddAuth());
  }

  @Bean
  public AuthenticationFacade bddAuth() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public TriggerRecyclageService triggerRecyclageService(
      BusinessEventProducer businessEventProducer, MongoTemplate mongoTemplate) {
    return new TriggerRecyclageService(
        triggerService(mongoTemplate),
        eventService(businessEventProducer),
        sasContratService(mongoTemplate),
        beyondPropertiesService);
  }
}
