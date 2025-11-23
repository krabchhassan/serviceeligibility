package com.cegedim.next.serviceeligibility.batch635.job.configuration;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.listener.ServiceEligibilityListener;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.CustomContratsRepository;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.CustomContratsRepositoryImpl;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.DeclarantsRepository;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.DeclarantsService;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.DeclarantsServiceImpl;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.ExtractionService;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.ExtractionServiceImpl;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.FileService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {

  @Bean
  FileService fileService() {
    return new FileService();
  }

  @Bean
  ExtractionService extractionService() {
    return new ExtractionServiceImpl(customContratsRepository(), declarantsService());
  }

  @Bean
  ServiceEligibilityListener serviceEligibilityListener() {
    return new ServiceEligibilityListener();
  }

  @MockBean MongoTemplate mongoTemplate;

  @Bean
  CustomContratsRepository customContratsRepository() {
    return new CustomContratsRepositoryImpl(20000, mongoTemplate);
  }

  @Bean
  DeclarantsService declarantsService() {
    return new DeclarantsServiceImpl(declarantsRepository);
  }

  @MockBean DeclarantsRepository declarantsRepository;
}
