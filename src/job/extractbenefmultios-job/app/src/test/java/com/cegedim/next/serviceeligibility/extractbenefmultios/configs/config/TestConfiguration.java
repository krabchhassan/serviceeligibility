package com.cegedim.next.serviceeligibility.extractbenefmultios.configs.config;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.OmuHelperImpl;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.extractbenefmultios.services.Processor;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
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

  @MockBean public MongoClient mongoClient;

  @MockBean public MongoTemplate mongoTemplate;

  @MockBean public CrexProducer crexProducer;

  @Bean
  public Processor processor(
      @Value("${OUTPUT_PATH:/workdir/extractbenefmultios/}") String workdir) {
    return new Processor(workdir, beneficiaryService);
  }

  @Bean
  public OmuHelper omuHelper() {
    return new OmuHelperImpl();
  }

  @MockBean public BeneficiaryService beneficiaryService;
}
