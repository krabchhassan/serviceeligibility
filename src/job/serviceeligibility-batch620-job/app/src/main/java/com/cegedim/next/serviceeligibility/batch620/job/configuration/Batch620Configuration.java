package com.cegedim.next.serviceeligibility.batch620.job.configuration;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.ARL_FOLDER;
import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.MAXIMUM_REJECT_LIST_SIZE;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.CircuitMapper;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperParametresFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.*;
import com.cegedim.next.serviceeligibility.core.config.s3.S3Config;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDaoImpl;
import com.cegedim.next.serviceeligibility.core.mapper.carte.MapperCartePapier;
import com.cegedim.next.serviceeligibility.core.services.CartesService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DomaineService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DomaineServiceImpl;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.carte.ProcessorCartesService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideServiceImpl;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.ProcessorConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation.InvalidationCarteService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.AdresseService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({S3Config.class, OmuHelperConfiguration.class})
public class Batch620Configuration {
  private final MongoTemplate mongoTemplate;
  private final String arlFolder;
  private final int tracesBulkSize;
  private final BeyondPropertiesService beyondPropertiesService;

  public Batch620Configuration(
      MongoTemplate mongoTemplate, BeyondPropertiesService beyondPropertiesService) {
    this.mongoTemplate = mongoTemplate;
    this.arlFolder = beyondPropertiesService.getPropertyOrThrowError(ARL_FOLDER);
    this.tracesBulkSize =
        beyondPropertiesService.getIntegerProperty(MAXIMUM_REJECT_LIST_SIZE).orElse(5000);
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public S3Service s3Service(S3StorageService bddS3StorageService) {
    return new S3Service(bddS3StorageService, new ObjectMapper(), beyondPropertiesService);
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
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(mongoTemplate);
  }

  @Bean
  public HistoriqueExecutionsDao historiqueExecutionsDao() {
    return new HistoriqueExecutionsDaoImpl(mongoTemplate);
  }

  @Bean
  public EventService eventService(BusinessEventProducer businessEventProducer) {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public DeclarationConsolideDao declarationConsolideDao() {
    return new DeclarationConsolideDaoImpl(mongoTemplate);
  }

  @Bean
  public DomaineService domaineService() {
    return new DomaineServiceImpl();
  }

  @Bean
  public InvalidationCarteService invalidationCarteService() {
    return new InvalidationCarteService();
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
  public DeclarationConsolideService declarationConsolideService(
      BusinessEventProducer businessEventProducer, S3StorageService bddS3StorageService) {
    return new DeclarationConsolideServiceImpl(
        declarationConsolideDao(),
        eventService(businessEventProducer),
        traceConsolidationService(),
        declarationService(),
        domaineService(),
        invalidationCarteService(),
        carteDematService(businessEventProducer, bddS3StorageService));
  }

  @Bean
  public AdresseService adresseService() {
    return new AdresseService(beyondPropertiesService);
  }

  @Bean
  public MapperCartePapier mapperCartePapier() {
    return new MapperCartePapier(parametreBddService());
  }

  @Bean
  public ParametreBddServiceImpl parametreBddService() {
    return new ParametreBddServiceImpl(new ParametreBddDaoImpl(mongoTemplate));
  }

  @Bean
  public ProcessorCartesService carteDematService(
      BusinessEventProducer businessEventProducer, S3StorageService bddS3StorageService) {
    return new ProcessorCartesService(
        adresseService(),
        mapperCartePapier(),
        eventService(businessEventProducer),
        s3Service(bddS3StorageService),
        parametreBddService(),
        cartesService(),
        beyondPropertiesService);
  }

  @Bean
  public CartesService cartesService() {
    return new CartesService(carteDematDao(), cartePapierDao(), declarationService());
  }

  @Bean
  public ProcessorConsolidationService processorConsolidationService(
      MongoClient mongoClient,
      S3StorageService bddS3StorageService,
      BusinessEventProducer businessEventProducer) {
    return new ProcessorConsolidationService(
        declarantService(),
        declarationService(),
        historiqueExecutionsDao(),
        eventService(businessEventProducer),
        declarationConsolideService(businessEventProducer, bddS3StorageService),
        arlService(),
        invalidationCarteService(),
        s3Service(bddS3StorageService),
        cartesService(),
        traceExtractionConsoService(),
        beyondPropertiesService,
        mongoClient);
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
        arlFolder,
        tracesBulkSize,
        Constants.NUMERO_BATCH_620);
  }
}
