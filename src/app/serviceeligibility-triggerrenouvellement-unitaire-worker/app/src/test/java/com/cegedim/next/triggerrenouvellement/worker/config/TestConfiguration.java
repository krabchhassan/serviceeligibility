package com.cegedim.next.triggerrenouvellement.worker.config;

import com.cegedim.beyond.business.organisation.facade.OrganisationService;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.contracttp.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.triggerrenouvellement.worker.kafka.Consumer;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerProcessingService;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerRecyclingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestOperations;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {

  @MockBean public MongoClient mongoClient;

  @MockBean private BusinessEventProducer businessEventProducer;

  @MockBean public Producer producer;

  @MockBean public MongoTemplate mongoTemplate;

  @MockBean RestOperations apiKeyTokenRestTemplate;

  @Bean
  public TriggerUnitaireWorkerProcessingService triggerUnitaireWorkerProcessingService() {
    return new TriggerUnitaireWorkerProcessingService(
        triggerBuildDeclarationNewService(),
        triggerCreationService(),
        triggerService(),
        sasContratService(),
        servicePrestationDao(),
        producer,
        triggerRecyclageService());
  }

  @Bean
  public TriggerUnitaireWorkerRecyclingService triggerUnitaireWorkerRecyclingService() {
    return new TriggerUnitaireWorkerRecyclingService(
        triggerBuildDeclarationNewService(),
        triggerCreationService(),
        triggerService(),
        sasContratService(),
        servicePrestationDao(),
        producer,
        triggerRecyclageService());
  }

  @Bean
  public TriggerService triggerService() {
    return new TriggerService(triggerDao());
  }

  @MockBean public BeyondPropertiesService beyondPropertiesService;

  @MockBean public ContractElementService contractElementService;

  @Bean
  public TriggerDao triggerDao() {
    return new TriggerDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public Consumer consumer() {
    return new Consumer(
        beyondPropertiesService,
        triggerUnitaireWorkerProcessingService(),
        triggerUnitaireWorkerRecyclingService(),
        mongoClient,
        triggerCountService(),
        triggerService(),
        triggerRecyclageService(),
        triggerCSVService());
  }

  @Bean
  public TriggerRecyclageService triggerRecyclageService() {
    return new TriggerRecyclageService(
        triggerService(), eventService(), sasContratService(), beyondPropertiesService);
  }

  @MockBean public S3Service s3Service;

  @Bean
  public TriggerCSVService triggerCSVService() {
    return new TriggerCSVService(
        triggerRecyclageService(), triggerService(), s3Service, beyondPropertiesService);
  }

  @Bean
  public TriggerCountService triggerCountService() {
    return new TriggerCountService(new TriggerCountDaoImpl(mongoTemplate));
  }

  @Bean
  public PwCachedService pwCachedService() {
    return new PwCachedService(restConnector(), cacheManager, beyondPropertiesService);
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public ServicePrestationDao servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService() {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(), lotDao(), mapperParametrageCarteTP());
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean("bddAuth")
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public PwService pwService() {
    return new PwService(pwCachedService(), objectMapper(), beyondPropertiesService);
  }

  @Bean
  public SasContratService sasContratService() {
    return new SasContratService(sasContratDao());
  }

  @Bean
  public SasContratDao sasContratDao() {
    return new SasContratDaoImpl(mongoTemplate);
  }

  @Bean
  public EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(declarationDao());
  }

  @Bean
  public ContractElementService contractElementService() {
    return new ContractElementService(new ContractElementRepository());
  }

  @Bean
  public ProductElementService productElementService() {
    return new ProductElementService(contractElementService());
  }

  @Bean
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector(), beyondPropertiesService);
  }

  @Bean
  public ContractService contratService() {
    return new ContractService(contractDao());
  }

  @Bean
  public ContractTPService contractTPService(final ContractDao contractDao) {
    return new ContractTPService(contractDao, domaineTPService());
  }

  @Bean
  public DomaineTPService domaineTPService() {
    return new DomaineTPService(periodeDroitTPService());
  }

  @Bean
  PeriodeDroitTPService periodeDroitTPService() {
    return new PeriodeDroitTPService(
        periodeDroitTPStep1(), periodeDroitTPStep2(), periodeDroitTPStep3(), periodeDroitTPStep4());
  }

  @Bean
  PeriodeDroitTPStep1 periodeDroitTPStep1() {
    return new PeriodeDroitTPStep1();
  }

  @Bean
  PeriodeDroitTPStep2 periodeDroitTPStep2() {
    return new PeriodeDroitTPStep2();
  }

  @Bean
  PeriodeDroitTPStep3 periodeDroitTPStep3() {
    return new PeriodeDroitTPStep3();
  }

  @Bean
  PeriodeDroitTPStep4 periodeDroitTPStep4() {
    return new PeriodeDroitTPStep4();
  }

  @Bean
  public ElasticHistorisationContractService elasticHistorisationContractService() {
    return new ElasticHistorisationContractService(
        historiqueContratRepository, indexHistoContrat(), objectMapper(), restHighLevelClient);
  }

  @MockBean RestHighLevelClient restHighLevelClient;

  @Bean
  IndexHistoContrat indexHistoContrat() {
    return new IndexHistoContrat(beyondPropertiesService);
  }

  @MockBean HistoriqueContratRepository historiqueContratRepository;

  @Bean
  public RestitutionCarteDao restitutionCarteDao() {
    return new RestitutionCarteDaoImpl(mongoTemplate);
  }

  @Bean
  public TriggerBuildDeclarationNewService triggerBuildDeclarationNewService() {
    return new TriggerBuildDeclarationNewService(
        sasContratService(),
        parametrageCarteTPService(),
        triggerService(),
        suspensionService(),
        declarationService(),
        triggerDomaineService(),
        triggerMapper(),
        restitutionCarteDao());
  }

  @Bean
  public SuspensionService suspensionService() {
    return new SuspensionService();
  }

  @Bean
  public ContractDao contractDao() {
    return new ContractDaoImpl(mongoTemplate);
  }

  @Bean
  public TriggerMapper triggerMapper() {
    return new TriggerMapper(
        contractElementService, productElementService(), parametreBddService(), declarantService());
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public RestConnector restConnector() {
    return new RestConnector(apiKeyTokenRestTemplate);
  }

  @MockBean public CacheManager cacheManager;

  @Bean
  public TriggerDomaineService triggerDomaineService() {
    return new TriggerDomaineServiceImpl();
  }

  @Bean
  public TriggerCreationService triggerCreationService() {
    return new TriggerCreationService(
        parametrageCarteTPService(),
        sasContratService(),
        producer,
        triggerService(),
        mongoClient,
        triggerMapper(),
        servicePrestationService(),
        beyondPropertiesService);
  }

  @MockBean public OrganisationService organisationService;

  @Bean
  public ParametreBddService parametreBddService() {
    return new ParametreBddServiceImpl(parametreBddDao());
  }

  @Bean
  public ParametreBddDaoImpl parametreBddDao() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public CalculDroitsTPGenerationService calculDroitsTPGenerationService() {
    return new CalculDroitsTPGenerationService(pwService(), contractElementService, carenceService);
  }

  @MockBean public CarenceService carenceService;

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public ServicePrestationService servicePrestationService() {
    return new ServicePrestationService(servicePrestationDao());
  }

  @MockBean public EventInsuredTerminationService eventInsuredTerminationService;
}
