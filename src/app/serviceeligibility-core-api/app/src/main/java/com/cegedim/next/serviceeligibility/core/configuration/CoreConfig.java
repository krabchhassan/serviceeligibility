package com.cegedim.next.serviceeligibility.core.configuration;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** fichier à supprimer après la migration des tests de service */
@Configuration
public class CoreConfig {

  private final BeyondPropertiesService beyondPropertiesService;

  public CoreConfig(BeyondPropertiesService beyondPropertiesService) {
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public ARLService arlService(
      FluxService fluxService,
      CircuitService circuitService,
      TraceConsolidationService traceConsolidationService,
      TraceExtractionConsoService traceExtractionConsoService) {
    return new ARLService(
        fluxService,
        circuitService,
        traceConsolidationService,
        traceExtractionConsoService,
        beyondPropertiesService.getProperty(ARL_FOLDER).orElse("/tmp/ARL_RDO_SP/"),
        beyondPropertiesService.getIntegerProperty(MAXIMUM_REJECT_LIST_SIZE).orElse(5000),
        Constants.NUMERO_BATCH_620);
  }
}
