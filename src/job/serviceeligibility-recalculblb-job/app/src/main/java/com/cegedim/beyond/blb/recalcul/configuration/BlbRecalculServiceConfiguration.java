package com.cegedim.beyond.blb.recalcul.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbServicePrestationRepo;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbTrackingRepo;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
    basePackageClasses = {BddsToBlbTrackingRepo.class, BddsToBlbServicePrestationRepo.class})
@RequiredArgsConstructor
public class BlbRecalculServiceConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public EventService eventService(BusinessEventProducer producer) {
    return new EventService(producer, beyondPropertiesService);
  }

  @Bean
  public ExtractContractProducer extractContractProducer(
      MessageProducerWithApiKey messageProducer, BddsToBlbTrackingRepo bddsToBlbTrackingRepo) {
    return new ExtractContractProducer(
        messageProducer, beyondPropertiesService, bddsToBlbTrackingRepo);
  }
}
