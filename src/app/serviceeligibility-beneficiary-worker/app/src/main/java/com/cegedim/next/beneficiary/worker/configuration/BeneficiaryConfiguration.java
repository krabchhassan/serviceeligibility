package com.cegedim.next.beneficiary.worker.configuration;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaryDao;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaryDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.MemoryCacheService;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class BeneficiaryConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public TraceService traceService(MongoTemplate mongoTemplate) {
    return new TraceService(traceDao(mongoTemplate));
  }

  @Bean
  public TraceDao traceDao(MongoTemplate mongoTemplate) {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public BenefInfosService benefInfos() {
    return new BenefInfosService();
  }

  @Bean
  public BeneficiaryService benefService(MongoTemplate template) {
    return new BeneficiaryService(
        template, traceService(template), beneficiaryDao(template), benefInfos());
  }

  @Bean
  public BeneficiaryDao beneficiaryDao(MongoTemplate mongoTemplate) {
    return new BeneficiaryDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationDao declarationDao(MongoTemplate mongoTemplate) {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public MemoryCacheService memoryCacheService(CacheManager bddsCacheManager) {
    return new MemoryCacheService(bddsCacheManager);
  }

  @Bean
  public EventService eventService(BusinessEventProducer producer) {
    return new EventService(producer, beyondPropertiesService);
  }
}
