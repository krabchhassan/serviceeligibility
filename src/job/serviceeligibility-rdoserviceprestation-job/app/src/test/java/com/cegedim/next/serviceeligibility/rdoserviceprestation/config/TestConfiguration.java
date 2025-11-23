package com.cegedim.next.serviceeligibility.rdoserviceprestation.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
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
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.services.BeneficiaryKafkaService;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapperImpl;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.ContratAivService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.kafkabenef.ProducerBenefForBatch;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo.RequestFileTask;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.BeneficiaryKafkaServiceForBatch;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.FileService;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.ProcessFileTask;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.ValidationRdoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.client.MongoClient;
import java.util.ArrayList;
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
  @MockBean MessageProducerWithApiKey messageProducerWithApiKey;

  @MockBean CacheManager cacheManager;

  @MockBean public MongoTemplate mongoTemplate;

  @MockBean public RestConnector restConnector;

  @MockBean public MongoClient mongoClient;

  @MockBean public OmuHelper omuHelper;

  @MockBean BddsToBlbTrackingRepo blbTrackingDao;

  @MockBean private ExtractContractProducer extractContractProducer;

  @MockBean public OrganizationService organizationService;

  @MockBean RestOperations apiKeyTokenRestTemplate;

  @Bean
  public RestConnector restConnector() {
    return new RestConnector(apiKeyTokenRestTemplate);
  }

  @Bean
  public BeneficiaryKafkaServiceForBatch beneficiaryKafkaServiceForBatch() {
    return new BeneficiaryKafkaServiceForBatch(producerBenefForBatch);
  }

  @Bean
  public BeneficiaryKafkaService beneficiaryKafkaService() {
    return new BeneficiaryKafkaService();
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
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
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public ValidationRdoService validationRdoService() {
    return new ValidationRdoService(
        organizationService,
        contractElementService,
        referentialService(),
        eventService(),
        beyondPropertiesService);
  }

  @Bean
  public BeneficiaryService beneficiaryService() {
    return new BeneficiaryService(
        mongoTemplate, traceService(), beneficiaryDao(), benefInfosService());
  }

  @Bean
  public BenefInfosService benefInfosService() {
    return new BenefInfosService();
  }

  @Bean
  public BeneficiaryDao beneficiaryDao() {
    return new BeneficiaryDaoImpl(mongoTemplate);
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
  public ContratAivService contractService(ObjectMapper objectMapper) {
    return new ContratAivService(
        mongoTemplate,
        eventService(),
        recipientMessageService(),
        suspensionService(),
        extractContractProducer,
        eventInsuredTerminationService(),
        retentionService(),
        objectMapper);
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
  public ReferentielParametrageCarteTPService referentielParametrageCarteTPService() {
    return new ReferentielParametrageCarteTPService(
        new ReferentielParametrageCarteTPDaoImpl(mongoTemplate));
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public ReferentielParametrageCarteTPDao dao() {
    return new ReferentielParametrageCarteTPDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public FileService fileService(ObjectMapper objectMapper) {
    return new FileService(
        traceService(),
        fileFlowMetadataService,
        validationRdoService(),
        contractService(objectMapper),
        servicePrestationService(),
        referentielParametrageCarteTPService(),
        beneficiaryService(),
        beneficiaryKafkaServiceForBatch(),
        triggerCreationService(),
        triggerDao(),
        parametrageCarteTPService(),
        rdoServicePrestationService(),
        1,
        objectMapper(),
        eventService());
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @MockBean public FileFlowMetadataService fileFlowMetadataService;

  @MockBean public BeyondPropertiesService beyondPropertiesService;

  @Bean
  public FileFlowMetadataDao fileFlowMetadataDao() {
    return new FileFlowMetadataDaoImpl();
  }

  @Bean
  EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  MessageService messageService() {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  SuspensionService suspensionService() {
    return new SuspensionService();
  }

  @MockBean Producer producer;

  @MockBean ProducerBenef producerBenef;

  @MockBean ProducerBenefForBatch producerBenefForBatch;

  @MockBean BusinessEventProducer businessEventProducer;

  @MockBean ParametrageCarteTPDaoImpl parametrageCarteTPDao;

  @MockBean AlmerysProductReferentialRepository almerysProductReferentialRepository;

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
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
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector(), beyondPropertiesService);
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

  @Bean
  public TriggerService triggerService() {
    return new TriggerService(triggerDao());
  }

  @Bean
  public TriggerMapper triggerMapper() {
    return new TriggerMapper(
        contractElementService, productElementService(), parametreBddService(), declarantService());
  }

  @Bean
  public CalculDroitsTPGenerationService calculDroitsTPGenerationService() {
    return new CalculDroitsTPGenerationService(
        pwService(), contractElementService, carenceService());
  }

  @Bean
  public CarenceService carenceService() {
    return new CarenceServiceImpl();
  }

  @Bean
  public SettingsUIService settingsUIService() {
    return new SettingsUIService(restConnector(), beyondPropertiesService);
  }

  @Bean
  public TriggerDao triggerDao() {
    return new TriggerDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public SasContratService sasContratService() {
    return new SasContratService(sasContratDao());
  }

  @Bean
  public SasContratDao sasContratDao() {
    return new SasContratDaoImpl(mongoTemplate);
  }

  @Bean("bddAuth")
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade());
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

  @Bean
  public PwService pwService() {
    return new PwService(pwCachedService(), objectMapper(), beyondPropertiesService);
  }

  @Bean
  public PwCachedService pwCachedService() {
    return new PwCachedService(restConnector(), cacheManager, beyondPropertiesService);
  }

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  public ParametreBddDao parametreBddDao() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public ParametreBddService parametreBddService() {
    return new ParametreBddServiceImpl(parametreBddDao());
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

  @MockBean public ContractElementService contractElementService;

  @Bean
  public ProcessFileTask processFileTask(ObjectMapper objectMapper) {
    return new ProcessFileTask(
        fileFlowMetadataService,
        traceService(),
        validationRdoService(),
        contractService(objectMapper),
        servicePrestationService(),
        referentielParametrageCarteTPService(),
        beneficiaryService(),
        beneficiaryKafkaServiceForBatch(),
        triggerCreationService(),
        triggerDao(),
        requestFileTask,
        new ArrayList<>(),
        rdoServicePrestationService(),
        objectMapper(),
        eventService());
  }

  @MockBean public RequestFileTask requestFileTask;

  @Bean
  public EventInsuredTerminationService eventInsuredTerminationService() {
    return new EventInsuredTerminationService();
  }

  @Bean
  public RDOServicePrestationService rdoServicePrestationService() {
    return new RDOServicePrestationService(
        rdoServicePrestationDAO, mapper(), beyondPropertiesService);
  }

  @MockBean public RDOServicePrestationDAO rdoServicePrestationDAO;

  @Bean
  public AssureRdoServicePrestationsMapper mapper() {
    return new AssureRdoServicePrestationsMapperImpl();
  }
}
