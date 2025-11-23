package com.cegedim.next.serviceeligibility.facturation.htp.configuration;

import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.dao.ContractDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
public class FacturationHTPConfig {
  @Bean
  public DeclarantService declarantService(MongoTemplate template) {
    return new DeclarantService(declarantDao(template));
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public ContractDao contractDao(MongoTemplate template) {
    return new ContractDaoImpl(template);
  }

  @Bean
  public DeclarantDao declarantDao(MongoTemplate template) {
    return new DeclarantDaoImpl(template);
  }

  @Bean
  public ContractService contractService(ContractDao contractDao) {
    return new ContractService(contractDao);
  }
}
