package com.cegedim.next.triggerrenouvellement.worker.configuration;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.next.serviceeligibility.core.config.TransverseConfiguration;
import com.cegedim.next.serviceeligibility.core.config.s3.S3Config;
import com.cegedim.next.serviceeligibility.core.dao.TriggerCountDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.dao.TriggerKafkaDao;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerCountService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCSVService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({S3Config.class, TransverseConfiguration.class})
@RequiredArgsConstructor
public class TriggerRenouvConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public TriggerKafkaService triggerKafkaService() {
    return new TriggerKafkaService();
  }

  @Bean
  public TriggerKafkaDao triggerKafkaDao() {
    return new TriggerKafkaDao();
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

  @Bean
  public TriggerCountService triggerCountService(MongoTemplate mongoTemplate) {
    return new TriggerCountService(new TriggerCountDaoImpl(mongoTemplate));
  }
}
