package com.cegedim.next.consumer.api.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.consumer.api.controller.ContractController;
import com.cegedim.next.consumer.api.controller.InsuredController;
import com.cegedim.next.consumer.api.kafka.ProducerConsumerApi;
import com.cegedim.next.consumer.api.repositories.ContratAIRepository;
import com.cegedim.next.consumer.api.service.InsuredService;
import com.cegedim.next.consumer.api.service.RestContratService;
import com.cegedim.next.consumer.api.service.TestDeclarantService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.LotRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.LotService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.ContratAivService;
import com.cegedim.next.serviceeligibility.core.services.ValidationContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.trigger.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {
  @MockBean MessageProducerWithApiKey messageProducerWithApiKey;

  @MockBean private Producer producer;

  @MockBean CacheManager cacheManager;

  @MockBean BddsToBlbTrackingRepo blbTrackingDao;

  @MockBean private ExtractContractProducer extractContractProducer;

  @MockBean private BusinessEventProducer businessEventProducer;

  @MockBean public ProducerConsumerApi producerConsumerApi;

  @MockBean public ProducerBenef benefProducer;

  @MockBean ClientSession clientSession;

  @MockBean MongoTemplate mongoTemplate;

  @MockBean public OrganisationServiceWrapper organisationServiceWrapper;

  @MockBean public BeyondPropertiesService beyondPropertiesService;

  @MockBean ParametrageCarteTPDaoImpl parametrageCarteTPDao;

  @MockBean AlmerysProductReferentialRepository almerysProductReferentialRepository;

  @Bean
  public Validator validator() {
    return new LocalValidatorFactoryBean();
  }

  @MockBean RestConnector restConnector;

  @Bean
  public MongoClient client() {
    return MongoClients.create(MongoClientSettings.builder().build());
  }

  @Bean
  public ExtractContractsService extractContractsService() {
    return new ExtractContractsService(extractContractProducer);
  }

  @Bean
  public EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public MessageService messageService() {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public SuspensionService suspensionService() {
    return new SuspensionService();
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
  public ContractController contractController() {
    return new ContractController(
        objectMapper(),
        producerConsumerApi,
        benefProducer,
        traceService(),
        restContratService(),
        validationContratService(),
        authenticationFacade,
        exceptionService(),
        idClientBOService(),
        eventService(),
        consumerServicePrestationService());
  }

  @Bean
  public InsuredController insuredController() {
    return new InsuredController(
        objectMapper(), traceService(), insuredService(), exceptionService());
  }

  @Bean
  public TraceService traceService() {
    return new TraceService(traceDao());
  }

  @Bean
  public BeneficiaryDao beneficiaryDao() {
    return new BeneficiaryDaoImpl(mongoTemplate);
  }

  @Bean
  public RestContratService restContratService() {
    return new RestContratService(
        mongoTemplate,
        servicePrestationDao(),
        triggerCreationService(),
        eventService(),
        extractContractsService(),
        eventInsuredTerminationService(),
        consumerServicePrestationService(),
        retentionService(),
        contratAivService(),
        declarationDao());
  }

  @Bean
  public ConsumerServicePrestationService consumerServicePrestationService() {
    return new ConsumerServicePrestationService(
        validationContratService(),
        idClientBOService(),
        eventService(),
        servicePrestationService(),
        referentielParametrageCarteTPService(),
        personService(),
        traceService(),
        benefService(),
        benefInfosService(),
        eventInsuredTerminationService(),
        declarationDao());
  }

  @Bean
  public BenefInfosService benefInfosService() {
    return new BenefInfosService();
  }

  @Bean
  public BeneficiaryService benefService() {
    return new BeneficiaryService(
        mongoTemplate, traceService(), beneficiaryDao(), benefInfosService());
  }

  @Bean
  public RetentionDao retentionDao() {
    return new RetentionDaoImpl(mongoTemplate);
  }

  @Bean
  public RetentionService retentionService() {
    return new RetentionServiceImpl(retentionDao(), servicePrestationDao(), eventService());
  }

  @Bean
  public ContratAivService contratAivService() {
    return new ContratAivService(
        mongoTemplate,
        eventService(),
        recipientMessageService(),
        suspensionService(),
        extractContractProducer,
        eventInsuredTerminationService(),
        retentionService(),
        objectMapper());
  }

  @Bean
  public TriggerDao triggerDao() {
    return new TriggerDaoImpl(mongoTemplate, authenticationFacade);
  }

  @Bean
  public TraceDao traceDao() {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public ValidationContratService validationContratService() {
    return new ValidationContratService(
        organisationServiceWrapper,
        contractElementService(),
        referentialService(),
        eventService(),
        beyondPropertiesService);
  }

  @Bean
  public PersonService personService() {
    return new PersonService(benefInfosService());
  }

  @Bean
  public InsuredService insuredService() {
    return new InsuredService(
        contratAIRepository(),
        recipientMessageService(),
        validationInsuredService(),
        authenticationFacade,
        personService(),
        benefProducer,
        idClientBOService(),
        mongoTemplate,
        eventService());
  }

  @Bean
  public RecipientMessageService recipientMessageService() {
    return new RecipientMessageService(messageService());
  }

  @Bean
  public ValidationInsuredService validationInsuredService() {
    return new ValidationInsuredService();
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public TestDeclarantService declarantWorkerService() {
    return new TestDeclarantService(beyondPropertiesService, mongoTemplate);
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService() {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(), lotDao(), mapperParametrageCarteTP());
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade);
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean
  public ReferentielParametrageCarteTPService referentielParametrageCarteTPService() {
    return new ReferentielParametrageCarteTPService(referentielParametrageCarteTPDao());
  }

  @Bean
  public ReferentielParametrageCarteTPDao referentielParametrageCarteTPDao() {
    return new ReferentielParametrageCarteTPDaoImpl(mongoTemplate);
  }

  @MockBean AuthenticationFacade authenticationFacade;

  @Bean
  public TriggerService triggerService() {
    return new TriggerService(triggerDao());
  }

  @Bean
  public ParametreBddDaoImpl parametreBddDao() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public ParametreBddService parametreBddService() {
    return new ParametreBddServiceImpl(parametreBddDao());
  }

  @Bean
  public TriggerCreationService triggerCreationService() {
    return new TriggerCreationService(
        parametrageCarteTPService(),
        sasContratService(),
        producer,
        triggerService(),
        client(),
        triggerMapper(),
        servicePrestationService(),
        beyondPropertiesService);
  }

  @Bean
  public TriggerRecyclageService triggerRecyclageService() {
    return new TriggerRecyclageService(
        triggerService(), eventService(), sasContratService(), beyondPropertiesService);
  }

  @Bean
  public ContractService contratService() {
    return new ContractService(contractDao());
  }

  @Bean
  public ContractTPService contractTPService() {
    return new ContractTPService(contractDao(), domaineTPService());
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

  @MockBean HistoriqueContratRepository historiqueContratRepository;

  @Bean
  IndexHistoContrat indexHistoContrat() {
    return new IndexHistoContrat(beyondPropertiesService);
  }

  @Bean
  public ContractDao contractDao() {
    return new ContractDaoImpl(mongoTemplate);
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
  public PwService pwService() {
    return new PwService(pwCachedService(), objectMapper(), beyondPropertiesService);
  }

  @Bean
  public SettingsUIService settingsUIService() {
    return new SettingsUIService(restConnector, beyondPropertiesService);
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(declarationDao());
  }

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public SasContratService sasContratService() {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public PwCachedService pwCachedService() {
    return new PwCachedService(restConnector, cacheManager, beyondPropertiesService);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public ContratAIRepository contratAIRepository() {
    return new ContratAIRepository(mongoTemplate);
  }

  @Bean
  public ContractElementService contractElementService() {
    return new ContractElementService(this.contractElementRepository());
  }

  @Bean
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector, beyondPropertiesService);
  }

  @Bean
  public CarenceService carenceService() {
    return new CarenceServiceImpl();
  }

  @Bean
  public CalculDroitsTPService calculDroitsTPService() {
    return new CalculDroitsTPService(
        contractElementService(), pwService(), carenceService(), beyondPropertiesService);
  }

  @Bean
  public ServicePrestationService servicePrestationService() {
    return new ServicePrestationService(servicePrestationDao());
  }

  @Bean
  public LotRepository lotRepository() {
    return new LotRepository(mongoTemplate);
  }

  @Bean
  public LotService lotService() {
    return new LotService(
        lotRepository(),
        lotDao(),
        parametrageCarteTPDao,
        almerysProductReferentialRepository,
        contractElementRepository());
  }

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }

  @Bean
  public RestitutionCarteDao restitutionCarteDao() {
    return new RestitutionCarteDaoImpl(mongoTemplate);
  }
}
