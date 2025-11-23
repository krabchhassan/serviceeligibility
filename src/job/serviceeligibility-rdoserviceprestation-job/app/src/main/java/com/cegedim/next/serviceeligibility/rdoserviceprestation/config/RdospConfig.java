package com.cegedim.next.serviceeligibility.rdoserviceprestation.config;

import com.cegedim.beyond.messaging.api.MessageProducerWithApiKey;
import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.common.organisation.configuration.OrganizationSettingsAutoConfiguration;
import com.cegedim.common.organisation.service.OrganizationService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDaoImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapperImpl;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.message.MessageService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.ValidationRdoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestOperations;

@Configuration
@EnableMongoRepositories(basePackageClasses = {BddsToBlbTrackingRepo.class})
@Import({OmuHelperConfiguration.class, OrganizationSettingsAutoConfiguration.class})
@RequiredArgsConstructor
public class RdospConfig {

  private final BeyondPropertiesService beyondPropertiesService;

  private final OrganizationService organizationService;

  private final BusinessEventProducer businessEventProducer;

  private final MessageProducerWithApiKey messageProducerWithApiKey;

  private final MongoTemplate mongoTemplate;

  private final BddsToBlbTrackingRepo bddsToBlbTrackingRepo;

  private final ObjectMapper objectMapper;

  private final MongoClient mongoClient;

  private final RestOperations apiKeyTokenRestTemplate;

  @Bean
  public RestConnector restConnector() {
    return new RestConnector(apiKeyTokenRestTemplate);
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public MessageService messageService() {
    return new MessageService(messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public String spanName(@Value("${JOB_SPAN_NAME:default_span}") final String spanName) {
    return spanName;
  }

  @Bean
  public FileFlowMetadataService fileFlowMetadataService() {
    return new FileFlowMetadataService();
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
  public FileFlowMetadataDao fileFlowMetadataDao() {
    return new FileFlowMetadataDaoImpl();
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
  public ValidationRdoService validationRdoService() {
    return new ValidationRdoService(
        organizationService,
        contractElementService(),
        referentialService(),
        eventService(),
        beyondPropertiesService);
  }

  @Bean
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector(), beyondPropertiesService);
  }

  @Bean
  public ContratAivService contratAivService() {
    return new ContratAivService(
        mongoTemplate,
        eventService(),
        recipientMessageService(),
        suspensionService(),
        extractContractProducer(),
        eventInsuredTerminationService(),
        retentionService(),
        objectMapper);
  }

  @Bean
  public RetentionService retentionService() {
    return new RetentionServiceImpl(retentionDao(), servicePrestationDao(), eventService());
  }

  @Bean
  public RetentionDao retentionDao() {
    return new RetentionDaoImpl(mongoTemplate);
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
  EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public RecipientMessageService recipientMessageService() {
    return new RecipientMessageService(messageService());
  }

  @Bean
  SuspensionService suspensionService() {
    return new SuspensionService();
  }

  @Bean
  public ServicePrestationService servicePrestationService() {
    return new ServicePrestationService(servicePrestationDao());
  }

  @Bean
  public ServicePrestationDaoImpl servicePrestationDao() {
    return new ServicePrestationDaoImpl(mongoTemplate, lotDao(), beyondPropertiesService);
  }

  @Bean
  public ReferentielParametrageCarteTPService referentielParametrageCarteTPService(
      MongoTemplate mongoTemplate) {
    return new ReferentielParametrageCarteTPService(
        referentielParametrageCarteTPDao(mongoTemplate));
  }

  @Bean
  public ReferentielParametrageCarteTPDao referentielParametrageCarteTPDao(
      MongoTemplate mongoTemplate) {
    return new ReferentielParametrageCarteTPDaoImpl(mongoTemplate);
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
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao() {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public RDOServicePrestationService rdoServicePrestationService() {
    return new RDOServicePrestationService(
        rdoServicePrestationDAO(), assureRdoServicePrestationsMapper(), beyondPropertiesService);
  }

  @Bean
  public RDOServicePrestationDAO rdoServicePrestationDAO() {
    return new RDOServicePrestationDAOImpl(mongoTemplate);
  }

  @Bean
  public AssureRdoServicePrestationsMapper assureRdoServicePrestationsMapper() {
    return new AssureRdoServicePrestationsMapperImpl();
  }

  @Bean
  public TriggerCreationService triggerCreationService() {
    return new TriggerCreationService(
        parametrageCarteTPService(),
        sasContratService(),
        producer(),
        triggerService(),
        mongoClient,
        triggerMapper(),
        servicePrestationService(),
        beyondPropertiesService);
  }

  @Bean
  public TriggerDao triggerDao() {
    return new TriggerDaoImpl(mongoTemplate, authenticationFacade());
  }

  @Bean
  public BeneficiaryService bservice() {
    return new BeneficiaryService(
        mongoTemplate,
        traceService(),
        new BeneficiaryDaoImpl(mongoTemplate),
        new BenefInfosService());
  }

  @Bean
  public SasContratService sasContratService() {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public Producer producer() {
    return new Producer(
        triggerService(), sasContratService(), messageProducerWithApiKey, beyondPropertiesService);
  }

  @Bean
  public TriggerService triggerService() {
    return new TriggerService(triggerDao());
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
  public ParametreBddService parametreBddService() {
    return new ParametreBddServiceImpl(parametreBddDao());
  }

  @Bean
  public ParametreBddDao parametreBddDao() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }
}
