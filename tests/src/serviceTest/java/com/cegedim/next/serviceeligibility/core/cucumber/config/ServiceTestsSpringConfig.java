package com.cegedim.next.serviceeligibility.core.cucumber.config;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.ARL_FOLDER;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.MAXIMUM_REJECT_LIST_SIZE;

import com.cegedim.beyond.serviceeligibility.common.config.OrganisationWrapperConfiguration;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestConsumerWorkerProcessingService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMaille;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMailleImpl;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapperImpl;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import({OmuHelperConfiguration.class, OrganisationWrapperConfiguration.class})
@EnableElasticsearchRepositories(basePackageClasses = HistoriqueContratRepository.class)
public class ServiceTestsSpringConfig {

  private final BeyondPropertiesService beyondPropertiesService;

  public ServiceTestsSpringConfig(BeyondPropertiesService beyondPropertiesService) {
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public MapperContractTPMaille mapperContractTPMaille() {
    return new MapperContractTPMailleImpl();
  }

  @Bean
  public AssureRdoServicePrestationsMapper assureRdoServicePrestationsMapper() {
    return new AssureRdoServicePrestationsMapperImpl();
  }

  @Bean
  public RestTemplate restTemplate(RestTemplate apiKeyTokenRestTemplate) {
    return apiKeyTokenRestTemplate;
  }

  // @TEMP
  @MockBean public RedisTemplate<String, Object> redisTemplate;

  @Bean
  public TriggerCountDao triggerCountDao(MongoTemplate mongoTemplate) {
    return new TriggerCountDaoImpl(mongoTemplate);
  }

  @Bean
  public TestConsumerWorkerProcessingService testConsumerWorkerProcessingService(
      TriggerBuildDeclarationNewService triggerBuildDeclarationNewService,
      ServicePrestationDao servicePrestationDao,
      Producer producer,
      TriggerRecyclageService triggerRecyclageService,
      SasContratService sasContratService,
      TriggerService triggerService,
      TriggerCreationService triggerCreationService) {
    return new TestConsumerWorkerProcessingService(
        triggerBuildDeclarationNewService,
        triggerCreationService,
        triggerService,
        sasContratService,
        servicePrestationDao,
        producer,
        triggerRecyclageService);
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
