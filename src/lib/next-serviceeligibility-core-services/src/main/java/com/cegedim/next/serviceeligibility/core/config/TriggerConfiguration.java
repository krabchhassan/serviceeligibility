package com.cegedim.next.serviceeligibility.core.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.contracttp.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = {BddsToBlbTrackingRepo.class})
@EnableElasticsearchRepositories(basePackageClasses = {HistoriqueContratRepository.class})
@Import({TransverseConfiguration.class})
@RequiredArgsConstructor
public class TriggerConfiguration {

  protected final BeyondPropertiesService beyondPropertiesService;

  private final ObjectMapper objectMapper;

  private final RestConnector restConnector;

  @Bean
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public TriggerBuildDeclarationNewService triggerBuildDeclarationNewService(
      MongoTemplate mongoTemplate,
      TriggerService triggerService,
      SasContratService sasContratService) {
    return new TriggerBuildDeclarationNewService(
        sasContratService,
        parametrageCarteTPService(mongoTemplate, authenticationFacade()),
        triggerService,
        suspensionService(),
        declarationService(mongoTemplate),
        triggerDomaineService(),
        triggerMapper(mongoTemplate),
        restitutionCarteDao(mongoTemplate));
  }

  @Bean
  ElasticHistorisationContractService elasticHistorisationContractService(
      HistoriqueContratRepository historiqueContratRepository,
      ObjectMapper objectMapper,
      RestHighLevelClient opensearchClient) {
    return new ElasticHistorisationContractService(
        historiqueContratRepository, indexHistoContrat(), objectMapper, opensearchClient);
  }

  @Bean
  IndexHistoContrat indexHistoContrat() {
    return new IndexHistoContrat(beyondPropertiesService);
  }

  @Bean
  ContractService contractService(MongoTemplate mongoTemplate) {
    return new ContractService(contractDao(mongoTemplate));
  }

  @Bean
  public ContractTPService contractTPService(MongoTemplate mongoTemplate) {
    return new ContractTPService(contractDao(mongoTemplate), domaineTPService());
  }

  @Bean
  public DomaineTPService domaineTPService() {
    return new DomaineTPService(periodeDroitTPService());
  }

  @Bean
  public PeriodeDroitTPService periodeDroitTPService() {
    return new PeriodeDroitTPService(
        periodeDroitTPStep1(), periodeDroitTPStep2(), periodeDroitTPStep3(), periodeDroitTPStep4());
  }

  @Bean
  public PeriodeDroitTPStep1 periodeDroitTPStep1() {
    return new PeriodeDroitTPStep1();
  }

  @Bean
  public PeriodeDroitTPStep2 periodeDroitTPStep2() {
    return new PeriodeDroitTPStep2();
  }

  @Bean
  public PeriodeDroitTPStep3 periodeDroitTPStep3() {
    return new PeriodeDroitTPStep3();
  }

  @Bean
  public PeriodeDroitTPStep4 periodeDroitTPStep4() {
    return new PeriodeDroitTPStep4();
  }

  @Bean
  public ContractDao contractDao(MongoTemplate mongoTemplate) {
    return new ContractDaoImpl(mongoTemplate);
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService(
      MongoTemplate mongoTemplate, AuthenticationFacade authenticationFacade) {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(mongoTemplate, authenticationFacade),
        lotDao(mongoTemplate),
        mapperParametrageCarteTP());
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao(
      MongoTemplate mongoTemplate, AuthenticationFacade authenticationFacade) {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade);
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean
  public ServicePrestationService servicePrestationService(MongoTemplate mongoTemplate) {
    return new ServicePrestationService(servicePrestationDao(mongoTemplate));
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao(MongoTemplate mongoTemplate) {
    return new ServicePrestationDaoImpl(
        mongoTemplate, lotDao(mongoTemplate), beyondPropertiesService);
  }

  @Bean
  public TriggerMapper triggerMapper(MongoTemplate mongoTemplate) {
    return new TriggerMapper(
        contractElementService(),
        productElementService(),
        parametreBddService(mongoTemplate),
        declarantService(mongoTemplate));
  }

  @Bean
  public BobbCorrespondanceService bobbCorrespondanceService() {
    return new BobbCorrespondanceServiceImpl(contractElementRepository(), versionsRepository());
  }

  @Bean
  public VersionsRepository versionsRepository() {
    return new VersionsRepository();
  }

  @Bean
  public ContractElementService contractElementService() {
    return new ContractElementService(contractElementRepository());
  }

  @Bean
  public ProductElementService productElementService() {
    return new ProductElementService(contractElementService());
  }

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public ParametreBddService parametreBddService(MongoTemplate mongoTemplate) {
    return new ParametreBddServiceImpl(parametreBddDao(mongoTemplate));
  }

  @Bean
  public ParametreBddDao parametreBddDao(MongoTemplate mongoTemplate) {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public SuspensionService suspensionService() {
    return new SuspensionService();
  }

  @Bean
  public RetentionDao retentionDao(MongoTemplate mongoTemplate) {
    return new RetentionDaoImpl(mongoTemplate);
  }

  @Bean
  public RetentionService retentionService(MongoTemplate mongoTemplate, EventService eventService) {
    return new RetentionServiceImpl(
        retentionDao(mongoTemplate), servicePrestationDao(mongoTemplate), eventService);
  }

  @Bean
  public MessageService messageService(MessageProducerWithApiKey messageProducerWithApiKey) {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public ContratAivService contratAivService(
      MongoTemplate mongoTemplate,
      BddsToBlbTrackingRepo bddsToBlbTrackingRepo,
      MessageProducerWithApiKey messageProducerWithApiKey,
      ObjectMapper objectMapper,
      EventService eventService) {
    return new ContratAivService(
        mongoTemplate,
        eventService,
        recipientMessageService(messageService(messageProducerWithApiKey)),
        suspensionService(),
        extractContractProducer(messageProducerWithApiKey, bddsToBlbTrackingRepo),
        eventInsuredTerminationService(),
        retentionService(mongoTemplate, eventService),
        objectMapper);
  }

  @Bean
  public RecipientMessageService recipientMessageService(MessageService messageService) {
    return new RecipientMessageService(messageService);
  }

  @Bean
  public ExtractContractProducer extractContractProducer(
      MessageProducerWithApiKey messageProducer, BddsToBlbTrackingRepo bddsToBlbTrackingRepo) {
    return new ExtractContractProducer(
        messageProducer, beyondPropertiesService, bddsToBlbTrackingRepo);
  }

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }

  @Bean
  public DeclarationService declarationService(MongoTemplate mongoTemplate) {
    return new DeclarationService(declarationDao(mongoTemplate));
  }

  @Bean
  public DeclarationDao declarationDao(MongoTemplate mongoTemplate) {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public TriggerDomaineService triggerDomaineService() {
    return new TriggerDomaineServiceImpl();
  }

  @Bean
  public CalculDroitsTPGenerationService calculDroitsTPGenerationService(
      CacheManager cacheManager) {
    return new CalculDroitsTPGenerationService(
        ipwService(cacheManager), contractElementService(), carenceService());
  }

  @Bean
  public IPwService ipwService(CacheManager cacheManager) {
    return new PwService(pwCachedService(cacheManager), objectMapper, beyondPropertiesService);
  }

  @Bean
  public PwCachedService pwCachedService(CacheManager cacheManager) {
    return new PwCachedService(restConnector, cacheManager, beyondPropertiesService);
  }

  @Bean
  public CarenceService carenceService() {
    return new CarenceServiceImpl();
  }

  @Bean
  public SettingsUIService settingsUIService() {
    return new SettingsUIService(restConnector, beyondPropertiesService);
  }

  @Bean
  public DeclarantService declarantService(MongoTemplate mongoTemplate) {
    return new DeclarantService(declarantDao(mongoTemplate));
  }

  @Bean
  public DeclarantDao declarantDao(MongoTemplate mongoTemplate) {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public LotDao lotDao(MongoTemplate mongoTemplate) {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public TriggerCreationService triggerCreationService(
      MongoTemplate mongoTemplate,
      AuthenticationFacade authenticationFacade,
      SasContratService sasContratService,
      TriggerService triggerService,
      MongoClient mongoClient,
      Producer producer) {
    return new TriggerCreationService(
        parametrageCarteTPService(mongoTemplate, authenticationFacade),
        sasContratService,
        producer,
        triggerService,
        mongoClient,
        triggerMapper(mongoTemplate),
        servicePrestationService(mongoTemplate),
        beyondPropertiesService);
  }

  @Bean
  public MemoryCacheService memoryCacheService(CacheManager bddsCacheManager) {
    return new MemoryCacheService(bddsCacheManager);
  }

  @Bean
  public RestitutionCarteDao restitutionCarteDao(MongoTemplate mongoTemplate) {
    return new RestitutionCarteDaoImpl(mongoTemplate);
  }
}
