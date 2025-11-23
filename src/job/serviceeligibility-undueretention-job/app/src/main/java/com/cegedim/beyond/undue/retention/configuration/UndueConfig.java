package com.cegedim.beyond.undue.retention.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.undue.retention.services.RetentionMessageService;
import com.cegedim.beyond.undue.retention.services.UndueRetentionService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.common.organisation.configuration.OrganizationSettingsAutoConfiguration;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class, OrganizationSettingsAutoConfiguration.class})
@RequiredArgsConstructor
public class UndueConfig {

  private final BeyondPropertiesService beyondPropertiesService;

  private final OrganizationService organizationService;

  @Bean
  public EventService eventService(BusinessEventProducer producer) {
    return new EventService(producer, beyondPropertiesService);
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public DeclarantDao declarantDao(MongoTemplate mongoTemplate) {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public RetentionDao retentionDao(MongoTemplate mongoTemplate) {
    return new RetentionDaoImpl(mongoTemplate);
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao(MongoTemplate mongoTemplate) {
    return new ServicePrestationDaoImpl(
        mongoTemplate, lotDao(mongoTemplate), beyondPropertiesService);
  }

  @Bean
  public RetentionService retentionService(
      MongoTemplate mongoTemplate, BusinessEventProducer producer) {
    return new RetentionServiceImpl(
        retentionDao(mongoTemplate), servicePrestationDao(mongoTemplate), eventService(producer));
  }

  @Bean
  public UndueRetentionService undueRetentionService(
      MongoTemplate mongoTemplate,
      BusinessEventProducer producer,
      MessageProducerWithApiKey messageProducerWithApiKey) {
    return new UndueRetentionService(
        retentionDao(mongoTemplate),
        retentionMessageService(messageProducerWithApiKey),
        eventService(producer));
  }

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }

  @Bean
  public RetentionMessageService retentionMessageService(
      MessageProducerWithApiKey messageProducerWithApiKey) {
    return new RetentionMessageService(
        messageProducerWithApiKey, beyondPropertiesService, organizationService);
  }

  @Bean
  public LotDao lotDao(MongoTemplate mongoTemplate) {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public String spanName(@Value("${JOB_SPAN_NAME:default_span}") final String spanName) {
    return spanName;
  }
}
