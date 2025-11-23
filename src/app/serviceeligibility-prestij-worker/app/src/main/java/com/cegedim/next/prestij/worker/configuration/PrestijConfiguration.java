package com.cegedim.next.prestij.worker.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.services.GlobalValidationService;
import com.cegedim.next.serviceeligibility.core.services.RestPrestIJService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class PrestijConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public EventService eventService(BusinessEventProducer businessEventProducer) {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public RestPrestIJService restPrestIJService(MongoTemplate mongoTemplate) {
    return new RestPrestIJService(mongoTemplate);
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
  public GlobalValidationService globalValidationService() {
    return new GlobalValidationService();
  }

  @Bean
  public ProducerBenef producerBenef(MessageProducerWithApiKey messageProducer) {
    return new ProducerBenef(messageProducer, beyondPropertiesService);
  }
}
