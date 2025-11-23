package com.cegedim.next.consumer.worker.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.serviceeligibility.common.config.OrganisationWrapperConfiguration;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.spring.starter.managementscope.ServletManagementScopeService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperPaymentRecipient;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceServiceImpl;
import com.cegedim.next.serviceeligibility.core.config.TransverseConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUAPRightEvent;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUAPRightTDB;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.ExtractContractsService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.contracttp.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceHTPImpl;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceV5HTPImpl;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.InstanceProperties;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTriHTP;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTriHtpV5Impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {BddsToBlbTrackingRepo.class})
@Import({TransverseConfiguration.class, OrganisationWrapperConfiguration.class})
@RequiredArgsConstructor
public class ConsumerConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;
  private final MessageProducerWithApiKey messageProducerWithApiKey;
  private final BddsToBlbTrackingRepo bddsToBlbTrackingRepo;
  private final MongoTemplate mongoTemplate;
  private final ServletManagementScopeService managementScopeService;
  private final CacheManager cacheManager;
  private final OrganisationServiceWrapper organisationServiceWrapper;
  private final AuthenticationFacade authenticationFacade;
  private final Producer producer;
  private final MongoClient mongoClient;
  private final ObjectMapper objectMapper;
  private final RestConnector restConnector;

  @Bean
  public boolean transactionnal() {
    return beyondPropertiesService
        .getBooleanProperty(InstanceProperties.TRANSACTIONNAL)
        .orElse(true);
  }

  @Bean
  public ServicePrestationDao servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public TriggerCreationService triggerCreationService(
      SasContratService sasContratService, TriggerService triggerService) {
    return new TriggerCreationService(
        parametrageCarteTPService(),
        sasContratService,
        producer,
        triggerService,
        mongoClient,
        triggerMapper(),
        servicePrestationService(),
        beyondPropertiesService);
  }

  @Bean
  public SuspensionService suspensionService() {
    return new SuspensionService();
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService() {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(), lotDao(), mapperParametrageCarteTP());
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade);
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean
  public ContractService contractService() {
    return new ContractService(new ContractDaoImpl(mongoTemplate));
  }

  @Bean
  public RestitutionCarteDao restitutionCarteDao() {
    return new RestitutionCarteDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(declarationDao(mongoTemplate));
  }

  @Bean
  public TriggerMapper triggerMapper() {
    return new TriggerMapper(
        contractElementService(),
        new ProductElementService(contractElementService()),
        parametreBddService(),
        declarantService());
  }

  @Bean
  public BobbCorrespondanceService bobbCorrespondanceService(MongoTemplate mongoTemplate) {
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
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public ParametreBddService parametreBddService() {
    return new ParametreBddServiceImpl(new ParametreBddDaoImpl(mongoTemplate));
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(new DeclarantDaoImpl(mongoTemplate));
  }

  @Bean
  public ProducerBenef producerBenef() {
    return new ProducerBenef(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public PersonService personService() {
    return new PersonService(benefInfos());
  }

  @Bean
  public TraceService traceService() {
    return new TraceService(traceDao());
  }

  @Bean
  public TraceDao traceDao() {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public ContratAivService contratAivService(EventService eventService) {
    return new ContratAivService(
        mongoTemplate,
        eventService,
        recipientMessageService(),
        suspensionService(),
        extractContractProducer(),
        eventInsuredTerminationService(),
        retentionService(eventService),
        objectMapper);
  }

  @Bean
  public RetentionService retentionService(EventService eventService) {
    return new RetentionServiceImpl(
        new RetentionDaoImpl(mongoTemplate), servicePrestationDao(), eventService);
  }

  @Bean
  public ExtractContractProducer extractContractProducer() {
    return new ExtractContractProducer(
        messageProducerWithApiKey, beyondPropertiesService, bddsToBlbTrackingRepo);
  }

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }

  @Bean
  public MessageService messageService() {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public RecipientMessageService recipientMessageService() {
    return new RecipientMessageService(messageService());
  }

  @Bean
  public ServicePrestationService servicePrestationService() {
    return new ServicePrestationService(servicePrestationDao());
  }

  @Bean
  public BeneficiaryService benefService() {
    return new BeneficiaryService(mongoTemplate, traceService(), beneficiaryDao(), benefInfos());
  }

  @Bean
  public BenefInfosService benefInfos() {
    return new BenefInfosService();
  }

  @Bean
  public BeneficiaryDao beneficiaryDao() {
    return new BeneficiaryDaoImpl(mongoTemplate);
  }

  @Bean
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector, beyondPropertiesService);
  }

  @Bean
  public ReferentielParametrageCarteTPService referentielParametrageCarteTPService() {
    return new ReferentielParametrageCarteTPService(referentielParametrageCarteTPDao());
  }

  @Bean
  public ReferentielParametrageCarteTPDao referentielParametrageCarteTPDao() {
    return new ReferentielParametrageCarteTPDaoImpl(mongoTemplate);
  }

  @Bean
  public ValidationInsuredService validationInsuredService() {
    return new ValidationInsuredService();
  }

  @Bean
  public UniqueAccessPointTriHTP uniqueAccessPointHtpTriV5() {
    return new UniqueAccessPointTriHtpV5Impl();
  }

  @Bean
  public UniqueAccessPointServiceHTPImpl uniqueAccessPointServiceHTPV5() {
    return new UniqueAccessPointServiceV5HTPImpl(
        uniqueAccessPointHtpTriV5(),
        mapperUniqueAccessPointServiceTP(),
        calculDroitsTPPAUService(),
        contractBackendDao(),
        beneficiaireHTPBackendDao());
  }

  @Bean
  public MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP() {
    return new MapperUniqueAccessPointServiceTP(
        ocService(), mapperUAPRightTDB(), mapperUAPRightEvent(), beyondPropertiesService);
  }

  @Bean
  public MapperUAPRightTDB mapperUAPRightTDB() {
    return new MapperUAPRightTDB(ipwService());
  }

  @Bean
  public MapperUAPRightEvent mapperUAPRightEvent() {
    return new MapperUAPRightEvent();
  }

  @Bean
  public OcService ocService() {
    return new OcService(organisationServiceWrapper);
  }

  @Bean
  public MapperPaymentRecipient mapperPaymentRecipient() {
    return new MapperPaymentRecipient();
  }

  @Bean("beneficiaireHTPBackendDao")
  public BeneficiaireBackendDao beneficiaireHTPBackendDao() {
    return new BeneficiaireHTPBackendDaoImpl(authorizationScopeHandler(), mongoTemplate);
  }

  @Bean
  public AuthorizationScopeHandler authorizationScopeHandler() {
    return new AuthorizationScopeHandler(managementScopeService, beyondPropertiesService);
  }

  @Bean
  public ContractBackendDao contractBackendDao() {
    return new ContractBackendDaoImpl(authorizationScopeHandler(), mongoTemplate);
  }

  @Bean
  public MemoryCacheService memoryCacheService() {
    return new MemoryCacheService(cacheManager);
  }

  @Bean
  public CalculDroitsTPPAUService calculDroitsTPPAUService() {
    return new CalculDroitsTPPAUService(
        ipwService(), contractElementService(), carenceService(), beyondPropertiesService);
  }

  @Bean
  public IPwService ipwService() {
    return new PwService(pwCachedService(), objectMapper, beyondPropertiesService);
  }

  @Bean
  public PwCachedService pwCachedService() {
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
  public ExtractContractsService extractContractsService() {
    return new ExtractContractsService(extractContractProducer());
  }

  @Bean
  public IdClientBOService idClientBOService() {
    return new IdClientBOService(declarantService());
  }

  @Bean
  public DeclarationDao declarationDao(MongoTemplate mongoTemplate) {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public ExceptionService exceptionService() {
    return new ExceptionService(traceService());
  }
}
