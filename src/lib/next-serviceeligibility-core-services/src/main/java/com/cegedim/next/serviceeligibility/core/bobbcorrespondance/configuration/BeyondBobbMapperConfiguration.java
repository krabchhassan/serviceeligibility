package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.configuration;

import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeyondBobbMapperConfiguration {
  @Bean
  public BobbCorrespondanceService bobbCorrespondanceService(
      ContractElementRepository contractElementRepository, VersionsRepository versionsRepository) {
    return new BobbCorrespondanceServiceImpl(contractElementRepository, versionsRepository);
  }
}
