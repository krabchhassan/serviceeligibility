package com.cegedim.next.serviceeligibility.extractbenefmultios.configuration;

import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaryDao;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaryDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.bdd.BeneficiaryService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
public class MultosConfiguration {

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
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
  public TraceService traceService(MongoTemplate mongoTemplate) {
    return new TraceService(traceDao(mongoTemplate));
  }

  @Bean
  public TraceDao traceDao(MongoTemplate mongoTemplate) {
    return new TraceDaoImpl(mongoTemplate);
  }
}
