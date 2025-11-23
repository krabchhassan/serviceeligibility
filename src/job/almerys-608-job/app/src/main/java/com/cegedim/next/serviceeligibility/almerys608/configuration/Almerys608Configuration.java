package com.cegedim.next.serviceeligibility.almerys608.configuration;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDao;
import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDaoImpl;
import com.cegedim.next.serviceeligibility.almerys608.dao.ProduitsAlmerysExclusDaoImpl;
import com.cegedim.next.serviceeligibility.almerys608.services.*;
import com.cegedim.next.serviceeligibility.almerys608.services.MapperAlmerysContrat;
import com.cegedim.next.serviceeligibility.almerys608.services.MapperAlmerysEntreprise;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.CircuitMapper;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperParametresFlux;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CircuitServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.FluxServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.DeclarationsConsolideesAlmerysService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.ARLService;
import com.cegedim.next.serviceeligibility.core.services.common.batch.AlmerysJobsService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
public class Almerys608Configuration {

  private final MongoTemplate template;
  private final BeyondPropertiesService beyondPropertiesService;

  public Almerys608Configuration(
      MongoTemplate template, BeyondPropertiesService beyondPropertiesService) {
    this.template = template;
    this.beyondPropertiesService = beyondPropertiesService;
  }

  @Bean
  public MapperAlmerysContrat mapperAlmerysContrat() {
    return new MapperAlmerysContrat();
  }

  @Bean
  public MapperAlmerysEntreprise mapperAlmerysEntreprise() {
    return new MapperAlmerysEntreprise();
  }

  @Bean
  public MapperAlmerysAdresse mapperAlmerysAdresse() {
    return new MapperAlmerysAdresse();
  }

  @Bean
  public MapperAlmerysRattachement mapperAlmerysRattachement() {
    return new MapperAlmerysRattachement();
  }

  @Bean
  public MapperAlmerysServiceTP mapperAlmerysServiceTP() {
    return new MapperAlmerysServiceTP(declarationService());
  }

  @Bean
  public MapperAlmerysMembreContrat mapperAlmerysMembreContrat() {
    return new MapperAlmerysMembreContrat();
  }

  @Bean
  public MapperAlmerysProduit mapperAlmerysProduit() {
    return new MapperAlmerysProduit(utilService());
  }

  @Bean
  public MapperAlmerysCarence mapperAlmerysCarence() {
    return new MapperAlmerysCarence();
  }

  @Bean
  public MapperAlmerysBeneficiaire mapperAlmerysBeneficiaire() {
    return new MapperAlmerysBeneficiaire(utilService());
  }

  @Bean
  public UtilService utilService() {
    return new UtilService(
        almerysJobsService(),
        declarationsConsolideesAlmerysService(),
        historiqueExecutionsDao(),
        new TranscodageDaoImpl(template),
        new ProduitsAlmerysExclusDaoImpl(template));
  }

  @Bean
  public ProcessorService processorService() {
    return new ProcessorService(mappersAlmerys(), domainService(), souscripteurService());
  }

  @Bean
  public DomainService domainService() {
    return new DomainService(commonDao(), utilService());
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public MappersAlmerys mappersAlmerys() {
    return new MappersAlmerys(
        mapperAlmerysContrat(),
        mapperAlmerysEntreprise(),
        mapperAlmerysServiceTP(),
        mapperAlmerysMembreContrat(),
        mapperAlmerysProduit(),
        mapperAlmerysCarence(),
        mapperAlmerysAdresse(),
        mapperAlmerysRattachement(),
        mapperAlmerysBeneficiaire());
  }

  @Bean
  public ProcessorAlmerys608 processor() {
    return new ProcessorAlmerys608(
        declarantService(),
        mappersAlmerys(),
        creationFileService(),
        domainService(),
        utilService(),
        processorService(),
        arlService(),
        traceExtractionConsoService());
  }

  @Bean
  public ARLService arlService() {
    return new ARLService(
        fluxService(),
        circuitService(),
        traceConsolidationService(),
        traceExtractionConsoService(),
        beyondPropertiesService.getPropertyOrThrowError(ARL_FOLDER),
        beyondPropertiesService.getIntegerPropertyOrThrowError(MAXIMUM_REJECT_LIST_SIZE),
        Constants.NUMERO_BATCH_608);
  }

  @Bean
  public TraceConsolidationDao traceConsolidationDao() {
    return new TraceConsolidationDaoImpl(template);
  }

  @Bean
  public TraceConsolidationService traceConsolidationService() {
    return new TraceConsolidationService(traceConsolidationDao());
  }

  @Bean
  public TraceExtractionConsoDao traceExtractionConsoDao() {
    return new TraceExtractionConsoDaoImpl(template);
  }

  @Bean
  public TraceExtractionConsoService traceExtractionConsoService() {
    return new TraceExtractionConsoService(traceExtractionConsoDao());
  }

  @Bean
  public FluxDao fluxDao() {
    return new FluxDaoImpl(template);
  }

  @Bean
  public FluxService fluxService() {
    return new FluxServiceImpl(fluxDao());
  }

  @Bean
  public MapperFlux mapperFlux() {
    return new MapperFlux();
  }

  @Bean
  public CircuitMapper circuitMapper() {
    return new CircuitMapper();
  }

  @Bean
  public MapperParametresFlux mapperParametresFlux() {
    return new MapperParametresFlux();
  }

  @Bean
  public CircuitDao circuitDao() {
    return new CircuitDaoImpl(template);
  }

  @Bean
  public CircuitService circuitService() {
    return new CircuitServiceImpl(circuitDao());
  }

  @Bean
  public CommonDao commonDao() {
    return new CommonDaoImpl(template);
  }

  @Bean
  public HistoriqueExecutionsDao historiqueExecutionsDao() {
    return new HistoriqueExecutionsDaoImpl(template);
  }

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(template, beyondPropertiesService);
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(template);
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public AlmerysJobsService almerysJobsService() {
    return new AlmerysJobsService();
  }

  @Bean
  public DeclarationsConsolideesAlmerysDao declarationsConsolideesAlmerysDao() {
    return new DeclarationsConsolideesAlmerysDaoImpl(template);
  }

  @Bean
  public DeclarationsConsolideesAlmerysService declarationsConsolideesAlmerysService() {
    return new DeclarationsConsolideesAlmerysService(declarationsConsolideesAlmerysDao());
  }

  @Bean
  public CreationFileService creationFileService() {
    return new CreationFileService(
        retrieveTmpObjectsService(),
        beyondPropertiesService.getProperty(OUTPUT_FOLDER).orElse("/tmp/mtn"));
  }

  @Bean
  public RetrieveTmpObjectsService retrieveTmpObjectsService() {
    return new RetrieveTmpObjectsService(commonDao());
  }

  @Bean
  public SouscripteurService souscripteurService() {
    return new SouscripteurService();
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(declarationDao());
  }
}
