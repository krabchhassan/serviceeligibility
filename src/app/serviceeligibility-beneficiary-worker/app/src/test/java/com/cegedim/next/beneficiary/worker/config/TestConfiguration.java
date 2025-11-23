package com.cegedim.next.beneficiary.worker.config;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.beneficiary.worker.dao.BeneficiaryDao;
import com.cegedim.next.beneficiary.worker.dao.BeneficiaryDaoImpl;
import com.cegedim.next.beneficiary.worker.service.BenefV5Service;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Profile("test")
@Configuration
public class TestConfiguration {
  @MockBean private BusinessEventProducer businessEventProducer;

  @MockBean MongoTemplate mongoTemplate;

  @MockBean BeyondPropertiesService beyondPropertiesService;

  @Bean
  public BenefInfosService benefInfosService() {
    return new BenefInfosService();
  }

  @Bean
  public BenefV5Service benefV5Service() {
    return new BenefV5Service();
  }

  @Bean
  public BeneficiaryDao beneficiaryDao() {
    return new BeneficiaryDaoImpl(mongoTemplate);
  }

  @Bean
  public TraceService traceService() {
    return new TraceService(traceDao());
  }

  @Bean
  public TraceDao traceDao() {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }
}
