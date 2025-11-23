package com.cegedim.next.serviceeligibility.mergealmerys.configuration;

import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperParametresFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
@Import({OmuHelperConfiguration.class})
public class MergeAlmerysConfiguration {

  private final MongoTemplate mongoTemplate;

  @Bean
  public TraceExtractionConsoService traceExtractionConsoService() {
    return new TraceExtractionConsoService(traceExtractionConsoDao());
  }

  @Bean
  public FluxService fluxService() {
    return new FluxServiceImpl(fluxDao());
  }

  @Bean
  public FluxDao fluxDao() {
    return new FluxDaoImpl(mongoTemplate);
  }

  @Bean
  public TraceExtractionConsoDao traceExtractionConsoDao() {
    return new TraceExtractionConsoDaoImpl(mongoTemplate);
  }

  @Bean
  public MapperFlux mapperFlux() {
    return new MapperFlux();
  }

  @Bean
  public MapperParametresFlux mapperParametresFlux() {
    return new MapperParametresFlux();
  }
}
