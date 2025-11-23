package com.cegedim.next.consumer.api.configuration;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.serviceeligibility.common.config.OrganisationWrapperConfiguration;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.spring.starter.managementscope.ServletManagementScopeService;
import com.cegedim.next.consumer.api.repositories.ContratAIRepository;
import com.cegedim.next.consumer.api.service.InsuredService;
import com.cegedim.next.consumer.api.service.TestDeclarantService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.config.TransverseConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.ExtractContractsService;
import com.cegedim.next.serviceeligibility.core.services.ValidationContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {BddsToBlbTrackingRepo.class})
@Import({TransverseConfiguration.class, OrganisationWrapperConfiguration.class})
public class ConsumerAPIConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  public ConsumerAPIConfiguration(
      BeyondPropertiesService beyondPropertiesService,
      MongoTemplate mongoTemplate,
      ObjectMapper objectMapper,
      AuthenticationFacade authenticationFacade,
      BddsToBlbTrackingRepo bddsToBlbTrackingRepo,
      MessageProducerWithApiKey messageProducerWithApiKey,
      ServletManagementScopeService servletManagementScopeService,
      OrganisationServiceWrapper organisationServiceWrapper,
      EventService eventService,
      TriggerService triggerService,
      Producer producer,
      SasContratService sasContratService,
      MongoClient mongoClient,
      RestConnector restConnector) {
    this.beyondPropertiesService = beyondPropertiesService;
    this.mongoTemplate = mongoTemplate;
    this.objectMapper = objectMapper;
    this.authenticationFacade = authenticationFacade;
    this.bddsToBlbTrackingRepo = bddsToBlbTrackingRepo;
    this.messageProducerWithApiKey = messageProducerWithApiKey;
    this.servletManagementScopeService = servletManagementScopeService;
    this.organisationServiceWrapper = organisationServiceWrapper;
    this.eventService = eventService;
    this.triggerService = triggerService;
    this.producer = producer;
    this.sasContratService = sasContratService;
    this.mongoClient = mongoClient;
    this.restConnector = restConnector;
  }

  private final MongoTemplate mongoTemplate;
  private final ObjectMapper objectMapper;
  private final AuthenticationFacade authenticationFacade;
  private final BddsToBlbTrackingRepo bddsToBlbTrackingRepo;
  private final MessageProducerWithApiKey messageProducerWithApiKey;
  private final ServletManagementScopeService servletManagementScopeService;
  private final OrganisationServiceWrapper organisationServiceWrapper;
  private final EventService eventService;
  private final TriggerService triggerService;
  private final Producer producer;
  private final SasContratService sasContratService;
  private final MongoClient mongoClient;
  private final RestConnector restConnector;

  @Bean
  public ServicePrestationDao servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public TriggerCreationService triggerCreationService() {
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
  public RestitutionCarteDao restitutionCarteDao() {
    return new RestitutionCarteDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService));
  }

  @Bean
  public TriggerMapper triggerMapper() {
    return new TriggerMapper(
        contractElementService(),
        productElementService(),
        parametreBddService(),
        declarantService());
  }

  @Bean
  public ProductElementService productElementService() {
    return new ProductElementService(contractElementService());
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
  public ContratAivService contratAivService() {
    return new ContratAivService(
        mongoTemplate,
        eventService,
        recipientMessageService(),
        suspensionService(),
        extractContractProducer(),
        eventInsuredTerminationService(),
        retentionService(),
        objectMapper);
  }

  @Bean
  public RetentionService retentionService() {
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
  public ValidationContratService validationContratService() {
    return new ValidationContratService(
        organisationServiceWrapper,
        contractElementService(),
        referentialService(),
        eventService,
        beyondPropertiesService);
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

  @Bean("beneficiaireHTPBackendDao")
  public BeneficiaireBackendDao beneficiaireHTPBackendDao() {
    return new BeneficiaireHTPBackendDaoImpl(authorizationScopeHandler(), mongoTemplate);
  }

  @Bean
  public AuthorizationScopeHandler authorizationScopeHandler() {
    return new AuthorizationScopeHandler(servletManagementScopeService, beyondPropertiesService);
  }

  @Bean
  public ContractBackendDao contractBackendDao() {
    return new ContractBackendDaoImpl(authorizationScopeHandler(), mongoTemplate);
  }

  @Bean
  public MemoryCacheService memoryCacheService(CacheManager bddsCacheManager) {
    return new MemoryCacheService(bddsCacheManager);
  }

  @Bean
  public ExtractContractsService extractContractsService() {
    return new ExtractContractsService(extractContractProducer());
  }

  @Bean
  public ExceptionService exceptionService() {
    return new ExceptionService(traceService());
  }

  @Bean
  public IdClientBOService idClientBOService() {
    return new IdClientBOService(declarantService());
  }

  @Bean
  public TestDeclarantService testDeclarantService() {
    return new TestDeclarantService(beyondPropertiesService, mongoTemplate);
  }

  @Bean
  public InsuredService insuredService() {
    return new InsuredService(
        contratAIRepository(),
        recipientMessageService(),
        validationInsuredService(),
        authenticationFacade,
        personService(),
        producerBenef(),
        idClientBOService(),
        mongoTemplate,
        eventService);
  }

  @Bean
  public ContratAIRepository contratAIRepository() {
    return new ContratAIRepository(mongoTemplate);
  }

  @Bean
  public ConsumerServicePrestationService consumerServicePrestationService() {
    return new ConsumerServicePrestationService(
        validationContratService(),
        idClientBOService(),
        eventService,
        servicePrestationService(),
        referentielParametrageCarteTPService(),
        personService(),
        traceService(),
        benefService(),
        benefInfos(),
        eventInsuredTerminationService(),
        declarationDao());
  }

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }
}
