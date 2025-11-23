package com.cegedim.next.serviceeligibility.core.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CircuitDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CircuitDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarantBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarantBackendDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarationBackendDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.DeclarationBackendDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.HistoriqueDeclarantDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.HistoriqueDeclarantDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ServiceDroitsDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ServiceDroitsDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TracesDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TracesDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.CircuitMapper;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperCodeRenvoiTP;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperConventionTP;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperDeclarant;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperDeclarantEchange;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperFondCarteTP;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperParametresFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperPaymentRecipient;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperPilotage;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperRegroupementDomainesTP;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperTranscodageDomaineTP;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperAdresse;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperAttestation;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperAttestationDetail;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperConvention;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperCouverture;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDeclaration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDomaineDroit;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDroits;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperFormule;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperHistoriqueDeclarations;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperHistoriqueInfoDeclaration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperIdentificationAssure;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperParametreFormule;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperPrestation;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperPriorites;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperTrace;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperTraceConsolidation;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperTraceExtraction;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperTracePriorisation;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperTraceRetour;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarationBackendService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarationBackendServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.LotRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.LotService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.forcingrights.ForcingRightsDao;
import com.cegedim.next.serviceeligibility.core.dao.forcingrights.ForcingRightsDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDaoImpl;
import com.cegedim.next.serviceeligibility.core.elast.IndexBenef;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.kafka.dao.TriggerKafkaDao;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.services.BeneficiaryKafkaService;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.mapper.ExtractedContractMapper;
import com.cegedim.next.serviceeligibility.core.mapper.MapperBenefDetails;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMaille;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMailleImpl;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperCartePapier;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperWebServiceCardV4;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperWebServiceCardV4Impl;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUAPRightEvent;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUAPRightTDB;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTP;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTPV5;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.almerysProductRef.AlmerysProductReferentialService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.carte.ProcessorCartesService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideServiceImpl;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.ProcessorConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.ws.CardService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.ws.CardServiceV4;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.ws.LimiteDomaineRestitutionService;
import com.cegedim.next.serviceeligibility.core.services.claim.ForcingRightsService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.AdresseService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPAgregationService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.DomaineTPService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep1;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep2;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep3;
import com.cegedim.next.serviceeligibility.core.services.contracttp.PeriodeDroitTPStep4;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.pau.IssuingCompanyCodeService;
import com.cegedim.next.serviceeligibility.core.services.pau.UAPForceService;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceV5HTPImpl;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceV5TPOfflineTPImpl;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceV5TPOnlineTPImpl;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerBuildDeclarationNewService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerDomaineService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerDomaineServiceImpl;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.webservices.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("test")
@Configuration
@EnableMongoRepositories
public class TestConfiguration {
  @MockBean MessageProducerWithApiKey messageProducerWithApiKey;

  @MockBean MongoTemplate mongoTemplate;

  @MockBean BddsToBlbTrackingRepo blbTrackingDao;

  @MockBean ExtractContractProducer extractContractProducer;

  @MockBean ProducerBenef producerBenef;

  @MockBean BusinessEventProducer businessEventProducer;

  @MockBean Producer producer;

  @MockBean public OrganisationServiceWrapper organisationServiceWrapper;

  @MockBean public S3StorageService s3StorageService;

  @MockBean public CacheManager cacheManager;

  @MockBean public AuthorizationScopeHandler authorizationScopeHandler;

  @MockBean ParametrageCarteTPDaoImpl parametrageCarteTPDao;

  @MockBean AlmerysProductReferentialRepository almerysProductReferentialRepository;

  @Bean
  public IndexBenef indexBenef() {
    return new IndexBenef(beyondPropertiesService);
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
  public TriggerKafkaDao triggerKafkaDao() {
    return new TriggerKafkaDao();
  }

  @Bean
  public TriggerKafkaService triggerKafkaService() {
    return new TriggerKafkaService();
  }

  @Bean
  public BenefInfosService benefInfosService() {
    return new BenefInfosService();
  }

  @Bean
  public BeneficiaryKafkaService benefKafkaService() {
    return new BeneficiaryKafkaService();
  }

  @Bean
  public PersonService personService() {
    return new PersonService(benefInfosService());
  }

  @MockBean RestConnector restConnector;

  @Bean
  public MongoClient client() {
    return MongoClients.create(MongoClientSettings.builder().build());
  }

  @Bean
  public BeneficiaryDao beneficiaryDao() {
    return new BeneficiaryDaoImpl(mongoTemplate);
  }

  @MockBean public EventService eventService;

  @MockBean public BeyondPropertiesService beyondPropertiesService;

  @Bean
  public SuspensionService suspensionService() {
    return new SuspensionService();
  }

  @Bean
  public TriggerDao triggerDao() {
    return new TriggerDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public ContractDao contractDao() {
    return new ContractDaoImpl(mongoTemplate);
  }

  @Bean
  public TriggerRecyclageService triggerRecyclageService() {
    return new TriggerRecyclageService(
        triggerService(), eventService, sasContratService(), beyondPropertiesService);
  }

  @Bean
  public RestPrestIJService restPrestIJService() {
    return new RestPrestIJService(mongoTemplate);
  }

  @Bean
  public ValidationInsuredService validationInsuredService() {
    return new ValidationInsuredService();
  }

  @Bean
  public BeneficiaryService beneficiaryService() {
    return new BeneficiaryService(
        mongoTemplate, traceService(), beneficiaryDao(), benefInfosService());
  }

  @Bean
  public RetentionDao retentionDao() {
    return new RetentionDaoImpl(mongoTemplate);
  }

  @Bean
  public RetentionService retentionService() {
    return new RetentionServiceImpl(retentionDao(), servicePrestationDao(), eventService);
  }

  @Bean
  public MessageService messageService() {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public ContratAivService contractService() {
    return new ContratAivService(
        mongoTemplate,
        eventService,
        recipientMessageService(),
        suspensionService(),
        extractContractProducer,
        eventInsuredTerminationService(),
        retentionService(),
        objectMapper());
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
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
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
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
  }

  @Bean
  public ReferentielParametrageCarteTPService referentielParametrageCarteTPService() {
    return new ReferentielParametrageCarteTPService(referentielParametrageCarteTPDao());
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public ReferentielParametrageCarteTPDao referentielParametrageCarteTPDao() {
    return new ReferentielParametrageCarteTPDaoImpl(mongoTemplate);
  }

  @Bean("bddAuth")
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public FileFlowMetadataDao fileFlowMetadataDao() {
    return new FileFlowMetadataDaoImpl();
  }

  @Bean
  public FileFlowMetadataService fileFlowMetadataService() {
    return new FileFlowMetadataService();
  }

  @Bean
  public TriggerService triggerService() {
    return new TriggerService(triggerDao());
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
  public TriggerBuildDeclarationNewService triggerBuildDeclarationService() {
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
  public TriggerMapper triggerMapper() {
    return new TriggerMapper(
        contractElementService, productElementService, parametreBddService, declarantService());
  }

  @Bean
  public OcService ocService() {
    return new OcService(organisationServiceWrapper);
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
  public ServiceDroitsDCLBENService serviceDroitsDCLBENService() {
    return new ServiceDroitsDCLBENService();
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public HistoriqueExecutionsRenouvellementDao historiqueExecutionsRenouvellementDao() {
    return new HistoriqueExecutionsRenouvellementDaoImpl(mongoTemplate);
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
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public AlmerysProductReferentialRepository almerysProductReferentialRepository() {
    return new AlmerysProductReferentialRepository(mongoTemplate);
  }

  @MockBean public ContractElementService contractElementService;

  @MockBean public ProductElementService productElementService;

  @Bean
  public AlmerysProductReferentialService almerysProductReferentialService() {
    return new AlmerysProductReferentialService(almerysProductReferentialRepository(), lotDao());
  }

  @Bean
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector, beyondPropertiesService);
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
  public CarenceService carenceService() {
    return new CarenceServiceImpl();
  }

  @Bean
  public CalculDroitsTPGenerationService calculDroitsTPGenerationService() {
    return new CalculDroitsTPGenerationService(
        pwService(), contractElementService, carenceService());
  }

  @Bean
  public CalculDroitsTPPAUService calculDroitsTPPAUService() {
    return new CalculDroitsTPPAUService(
        pwService(), contractElementService, carenceService(), beyondPropertiesService);
  }

  @Bean
  public TriggerDomaineService triggerDomaineService() {
    return new TriggerDomaineServiceImpl();
  }

  @Bean("uniqueAccessPointHtpTriV5")
  public UniqueAccessPointTriHTP uniqueAccessPointTriHtpV5Impl() {
    return new UniqueAccessPointTriHtpV5Impl();
  }

  @Bean
  public GenerateContratAIV5 generateContratAIV5() {
    return new GenerateContratAIV5();
  }

  @Bean
  public GenerateContract generateContract() {
    return new GenerateContract();
  }

  @Bean("beneficiaireHTPBackendDao")
  public BeneficiaireBackendDao beneficiaireHTPBackendDao() {
    return new BeneficiaireHTPBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean
  public ContractBackendDao contractBackendDao() {
    return new ContractBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean
  public MapperBenefDetails mapperBenefDetails() {
    return new MapperBenefDetails(
        cartesService(), contractTPAgregationService(), beyondPropertiesService);
  }

  @Bean
  public MapperUAPRightEvent mapperUAPRightEventV5() {
    return new MapperUAPRightEvent();
  }

  @Bean
  public MapperUAPRightTDB mapperUAPRightTDBV5() {
    return new MapperUAPRightTDB(pwService());
  }

  @Bean
  public MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP() {
    return new MapperUniqueAccessPointServiceTP(
        ocService(), mapperUAPRightTDBV5(), mapperUAPRightEventV5(), beyondPropertiesService);
  }

  @Bean
  public MapperUniqueAccessPointServiceTPV5 mapperUniqueAccessPointServiceTPV5() {
    return new MapperUniqueAccessPointServiceTPV5(
        ocService(), mapperUAPRightTDBV5(), mapperUAPRightEventV5(), beyondPropertiesService);
  }

  @Bean
  public UniqueAccessPointServiceV5HTPImpl uniqueAccessPointServiceV5Impl() {
    return new UniqueAccessPointServiceV5HTPImpl(
        uniqueAccessPointTriHtpV5Impl(),
        mapperUniqueAccessPointServiceTP(),
        calculDroitsTPPAUService(),
        contractBackendDao(),
        beneficiaireHTPBackendDao());
  }

  @Bean("beneficiaireTPOnlineBackendDao")
  public BeneficiaireBackendDao beneficiaireTPOnlineBackendDao() {
    return new BeneficiaireTPOnlineBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean
  public UniqueAccessPointTpSortSubscriber uniqueAccessPointTpOnlineSortV5Impl() {
    return new UniqueAccessPointTpOnlineSortSubscriberV5Impl();
  }

  @Bean
  public UniqueAccessPointTpSortRights uniqueAccessPointTpOnlineSortRightsV5Impl() {
    return new UniqueAccessPointTpOnlineSortRightsV5Impl();
  }

  @Bean("uniqueAccessPointTriV5TpOnline")
  public UniqueAccessPointTriTP uniqueAccessPointTpOnlineTriV5Impl() {
    return new UniqueAccessPointTriTpOnlineV5Impl();
  }

  @Bean
  public UniqueAccessPointServiceV5TPOnlineTPImpl uniqueAccessPointServiceV5TPOnlineImpl() {
    return new UniqueAccessPointServiceV5TPOnlineTPImpl(
        this.beneficiaireTPOnlineBackendDao(),
        this.contractBackendDao(),
        this.mapperUniqueAccessPointServiceTPV5(),
        this.uniqueAccessPointTpOnlineSortV5Impl(),
        this.uniqueAccessPointTpOnlineSortRightsV5Impl(),
        this.uniqueAccessPointTpOnlineTriV5Impl(),
        this.issuingCompanyCodeService(),
        this.uapForceService());
  }

  @Bean("beneficiaireTPOfflineBackendDao")
  public BeneficiaireBackendDao beneficiaireTPOfflineBackendDao() {
    return new BeneficiaireTPOfflineBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean("uniqueAccessPointTriV5TpOffline")
  public UniqueAccessPointTriTP uniqueAccessPointTpOfflineTriV5Impl() {
    return new UniqueAccessPointTriTpOfflineV5Impl();
  }

  @Bean
  public UniqueAccessPointServiceV5TPOfflineTPImpl uniqueAccessPointServiceV5TPOfflineImpl() {
    return new UniqueAccessPointServiceV5TPOfflineTPImpl(
        this.beneficiaireTPOfflineBackendDao(),
        this.contractBackendDao(),
        this.mapperUniqueAccessPointServiceTPV5(),
        this.uniqueAccessPointTpOnlineSortV5Impl(),
        this.uniqueAccessPointTpOnlineSortRightsV5Impl(),
        this.uniqueAccessPointTpOfflineTriV5Impl(),
        this.issuingCompanyCodeService(),
        this.uapForceService());
  }

  @Bean
  public Validator validator() {
    return Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();
  }

  @Bean
  public ParametreBddDaoImpl parametreBddDao() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @SpyBean public ParametreBddServiceImpl parametreBddService;

  @Bean
  public DeclarationBackendDao declarationBackendDao() {
    return new DeclarationBackendDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationBackendService declarationBackendService() {
    return new DeclarationBackendServiceImpl(
        declarationBackendDao(),
        mapperDeclaration(),
        mapperHistoriqueDeclarations(),
        mapperDroits(),
        carteDematDao(),
        declarantBackendDao());
  }

  @Bean
  public DeclarantBackendDao declarantBackendDao() {
    return new DeclarantBackendDaoImpl(mongoTemplate);
  }

  @Bean
  public DomaineService domaineService() {
    return new DomaineServiceImpl();
  }

  @Bean
  public MapperDeclaration mapperDeclaration() {
    return new MapperDeclaration();
  }

  @Bean
  public MapperHistoriqueDeclarations mapperHistoriqueDeclarations() {
    return new MapperHistoriqueDeclarations();
  }

  @Bean
  public MapperIdentificationAssure mapperIdentificationAssure() {
    return new MapperIdentificationAssure();
  }

  @Bean
  public MapperContractDto mapperContract() {
    return new MapperContractDto();
  }

  @Bean
  public MapperAdresse mapperAdresse() {
    return new MapperAdresse();
  }

  @Bean
  public MapperDroits mapperDroits() {
    return new MapperDroits();
  }

  @Bean
  public MapperDomaineDroit mapperDomaineDroit() {
    return new MapperDomaineDroit();
  }

  @Bean
  public MapperCouverture mapperCouverture() {
    return new MapperCouverture();
  }

  @Bean
  public MapperConvention mapperConvention() {
    return new MapperConvention();
  }

  @Bean
  public MapperPriorites mapperPriorites() {
    return new MapperPriorites();
  }

  @Bean
  public MapperPrestation mapperPrestation() {
    return new MapperPrestation();
  }

  @Bean
  public MapperFormule mapperFormule() {
    return new MapperFormule();
  }

  @Bean
  public MapperParametreFormule mapperParametreFormule() {
    return new MapperParametreFormule();
  }

  @Bean
  public MapperAttestation mapperAttestation() {
    return new MapperAttestation();
  }

  @Bean
  public MapperAttestationDetail mapperAttestationDetail() {
    return new MapperAttestationDetail();
  }

  @Bean
  public MapperTrace mapperTrace() {
    return new MapperTrace();
  }

  @Bean
  public MapperTraceConsolidation mapperTraceConsolidation() {
    return new MapperTraceConsolidation();
  }

  @Bean
  public MapperTraceExtraction mapperTraceExtraction() {
    return new MapperTraceExtraction();
  }

  @Bean
  public MapperTraceRetour mapperTraceRetour() {
    return new MapperTraceRetour();
  }

  @Bean
  public MapperTracePriorisation mapperTracePriorisation() {
    return new MapperTracePriorisation();
  }

  @Bean
  public DeclarantBackendService declarantBackendService() {
    return new DeclarantBackendServiceImpl(
        declarantBackendDao(),
        mapperDeclarant(),
        mapperPilotage(),
        mapperTranscodageDomaineTP(),
        mapperConventionTP(),
        mapperCodeRenvoiTP(),
        mapperRegroupementDomainesTP(),
        mapperFondCarteTP(),
        mapperDeclarantEchange(),
        historiqueDeclarantDao(),
        serviceDroitsDao(),
        eventService);
  }

  @Bean
  public MapperDeclarant mapperDeclarant() {
    return new MapperDeclarant();
  }

  @Bean
  public MapperFondCarteTP mapperFondCarteTP() {
    return new MapperFondCarteTP();
  }

  @Bean
  public MapperPilotage mapperPilotage() {
    return new MapperPilotage();
  }

  @Bean
  public MapperTranscodageDomaineTP mapperTranscodageDomaineTP() {
    return new MapperTranscodageDomaineTP();
  }

  @Bean
  public MapperDeclarantEchange mapperDeclarantEchange() {
    return new MapperDeclarantEchange();
  }

  @Bean
  public HistoriqueDeclarantDao historiqueDeclarantDao() {
    return new HistoriqueDeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public ServiceDroitsDao serviceDroitsDao() {
    return new ServiceDroitsDaoImpl(mongoTemplate);
  }

  @Bean
  public TracesDao tracesDao() {
    return new TracesDaoImpl(mongoTemplate);
  }

  @Bean
  public MapperHistoriqueInfoDeclaration mapperHistoriqueInfoDeclaration() {
    return new MapperHistoriqueInfoDeclaration();
  }

  @Bean
  public RestitutionCarteDao restitutionCarteDao() {
    return new RestitutionCarteDaoImpl(mongoTemplate);
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
  public RenouvellementService renouvellementService() {
    return new RenouvellementService();
  }

  @Bean
  public MapperConventionTP mapperConventionTP() {
    return new MapperConventionTP();
  }

  @Bean
  public MapperCodeRenvoiTP mapperCodeRenvoiTP() {
    return new MapperCodeRenvoiTP();
  }

  @Bean
  public MapperRegroupementDomainesTP mapperRegroupementDomainesTP() {
    return new MapperRegroupementDomainesTP();
  }

  @Bean
  public CardService cardService() {
    return new CardService();
  }

  @Bean
  public LimiteDomaineRestitutionService limiteDomaineRestitutionService() {
    return new LimiteDomaineRestitutionService();
  }

  @Bean
  public CardDao cardDao() {
    return new CardDaoImpl();
  }

  @Bean
  public CartesService cartesService() {
    return new CartesService(carteDematDao(), cartePapierDao(), declarationService());
  }

  @Bean
  public ProcessorCartesService carteDematService() {
    return new ProcessorCartesService(
        adresseService(),
        mapperCartePapier(),
        eventService,
        s3Service(),
        parametreBddService,
        cartesService(),
        beyondPropertiesService);
  }

  @Bean
  public CarteDematDao carteDematDao() {
    return new CarteDematDaoImpl(mongoTemplate);
  }

  @Bean
  public CartePapierDao cartePapierDao() {
    return new CartePapierDaoImpl(mongoTemplate);
  }

  @Bean
  public CircuitMapper circuitMapper() {
    return new CircuitMapper();
  }

  @Bean
  public MapperFlux mapperFlux() {
    return new MapperFlux();
  }

  @Bean
  public MapperParametresFlux mapperParametresFlux() {
    return new MapperParametresFlux();
  }

  @Bean
  public DeclarationConsolideDao declarationConsolideDao() {
    return new DeclarationConsolideDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationConsolideService declarationConsolideService() {
    return new DeclarationConsolideServiceImpl(
        declarationConsolideDao(),
        eventService,
        traceConsolidationService(),
        declarationService(),
        domaineService(),
        invalidationCarteService(),
        carteDematService());
  }

  @Bean
  public HistoriqueExecutionsDao historiqueExecutionsDao() {
    return new HistoriqueExecutionsDaoImpl(mongoTemplate);
  }

  @Bean
  public InvalidationCarteService invalidationCarteService() {
    return new InvalidationCarteService();
  }

  @Bean
  public ProcessorConsolidationService consolidationService() {
    return new ProcessorConsolidationService(
        declarantService(),
        declarationService(),
        historiqueExecutionsDao(),
        eventService,
        declarationConsolideService(),
        arlService(),
        invalidationCarteService(),
        s3Service(),
        cartesService(),
        traceExtractionConsoService(),
        beyondPropertiesService,
        client());
  }

  @Bean
  public AdresseService adresseService() {
    return new AdresseService(beyondPropertiesService);
  }

  @Bean
  public MapperCartePapier mapperCartePapier() {
    return new MapperCartePapier(parametreBddService);
  }

  @Bean
  public S3Service s3Service() {
    return new S3Service(s3StorageService, objectMapper(), beyondPropertiesService);
  }

  @Bean
  public IssuingCompanyCodeService issuingCompanyCodeService() {
    return new IssuingCompanyCodeService();
  }

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }

  @Bean
  public UAPForceService uapForceService() {
    return new UAPForceService();
  }

  @Bean
  public ContractTPAgregationService contractTPAgregationService() {
    return new ContractTPAgregationService(mapperContractTPMaille());
  }

  @Bean
  public MapperContractTPMaille mapperContractTPMaille() {
    return new MapperContractTPMailleImpl();
  }

  @Bean
  public RetentionDao retentionDaoImpl() {
    return new RetentionDaoImpl(mongoTemplate);
  }

  @Bean
  public MapperPaymentRecipient mapperPaymentRecipient() {
    return new MapperPaymentRecipient();
  }

  @Bean
  public BulkExtractContractService bulkExtractContractService(
      ContractDao contractDao, ContractTPAgregationService contractTPAgregationService) {
    return new BulkExtractContractService(
        contractDao, new ExtractedContractMapper(), contractTPAgregationService);
  }

  @Bean
  public ForcingRightsDao forcingRightsDao() {
    return new ForcingRightsDaoImpl<>(mongoTemplate, authorizationScopeHandler);
  }

  @Bean
  public ForcingRightsService forcingRightsService() {
    return new ForcingRightsService(forcingRightsDao());
  }

  @Bean
  public MapperWebServiceCardV4 mapperCardV4() {
    return new MapperWebServiceCardV4Impl();
  }

  @Bean
  public CardServiceV4 cardServiceV4() {
    return new CardServiceV4(mapperCardV4());
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
  public FluxDao fluxDao() {
    return new FluxDaoImpl(mongoTemplate);
  }

  @Bean
  public FluxService fluxService() {
    return new FluxServiceImpl(fluxDao());
  }

  @Bean
  public CircuitDao circuitDao() {
    return new CircuitDaoImpl(mongoTemplate);
  }

  @Bean
  public CircuitService circuitService() {
    return new CircuitServiceImpl(circuitDao());
  }

  @Bean
  public TraceConsolidationDao traceConsolidationDao() {
    return new TraceConsolidationDaoImpl(mongoTemplate);
  }

  @Bean
  public TraceConsolidationService traceConsolidationService() {
    return new TraceConsolidationService(traceConsolidationDao());
  }

  @Bean
  public TraceExtractionConsoDao traceExtractionConsoDao() {
    return new TraceExtractionConsoDaoImpl(mongoTemplate);
  }

  @Bean
  public TraceExtractionConsoService traceExtractionConsoService() {
    return new TraceExtractionConsoService(traceExtractionConsoDao());
  }

  @Bean
  public ARLService arlService() {
    return new ARLService(
        fluxService(),
        circuitService(),
        traceConsolidationService(),
        traceExtractionConsoService(),
        "/tmp/arl/",
        5000,
        Constants.NUMERO_BATCH_620);
  }

  @Bean
  public ValidationContratService validationContratService() {
    return new ValidationContratService(
        organisationServiceWrapper,
        contractElementService,
        referentialService(),
        eventService,
        beyondPropertiesService);
  }
}
