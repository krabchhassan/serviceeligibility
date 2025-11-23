package com.cegedim.next.serviceeligibility.reprisebeneficiaires.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.PersonService;
import com.cegedim.next.serviceeligibility.core.services.RestPrestIJService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
@RequiredArgsConstructor
public class RepriseBenefConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public RestPrestIJService restPrestIJService(MongoTemplate mongoTemplate) {
    return new RestPrestIJService(mongoTemplate);
  }

  @Bean
  public ProducerBenef producerBenef(MessageProducerWithApiKey messageProducer) {
    return new ProducerBenef(messageProducer, beyondPropertiesService);
  }

  @Bean
  public PersonService personService() {
    return new PersonService(new BenefInfosService());
  }
}
