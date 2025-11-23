package config;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDao;
import com.cegedim.next.serviceeligibility.almerys608.dao.CommonDaoImpl;
import com.cegedim.next.serviceeligibility.almerys608.dao.ProduitsAlmerysExclusDao;
import com.cegedim.next.serviceeligibility.almerys608.dao.ProduitsAlmerysExclusDaoImpl;
import com.cegedim.next.serviceeligibility.almerys608.services.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Profile("test")
@Configuration
public class Test608Configuration {
  @MockBean public MongoTemplate template;
  private final String arlFolder;
  private final int tracesBulkSize;

  public Test608Configuration(
      MongoTemplate template,
      @Value("${ARL_FOLDER:/tmp/ARL_ALMV3/}") String arlFolder,
      @Value("${MAXIMUM_REJECT_LIST_SIZE:5000}") int tracesBulkSize) {
    this.template = template;
    this.arlFolder = arlFolder;
    this.tracesBulkSize = tracesBulkSize;
  }

  @MockBean BeyondPropertiesService beyondPropertiesService;

  @Bean
  public SouscripteurService souscripteurService() {
    return new SouscripteurService();
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
  public MapperAlmerysAdresse mapperAlmerysAdresse() {
    return new MapperAlmerysAdresse();
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
  public MapperAlmerysRattachement mapperAlmerysRattachement() {
    return new MapperAlmerysRattachement();
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
        transcodageDao(),
        produitsAlmerysExclusDao());
  }

  @Bean
  public TranscodageDao transcodageDao() {
    return new TranscodageDaoImpl(template);
  }

  @Bean
  public ProduitsAlmerysExclusDao produitsAlmerysExclusDao() {
    return new ProduitsAlmerysExclusDaoImpl(template);
  }

  @Bean
  public CircuitMapper circuitMapper() {
    return new CircuitMapper();
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
  public DomainService domainService() {
    return new DomainService(commonDao(), utilService());
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
        Constants.NUMERO_BATCH_608);
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
  public TraceConsolidationDao traceConsolidationDao() {
    return new TraceConsolidationDaoImpl(template);
  }

  @Bean
  public TraceConsolidationService traceConsolidationService() {
    return new TraceConsolidationService(traceConsolidationDao());
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
  public MapperParametresFlux mapperParametresFlux() {
    return new MapperParametresFlux();
  }

  @Bean
  public DeclarantService declarantService() {
    return new DeclarantService(declarantDao());
  }

  @Bean
  public DeclarantDao declarantDao() {
    return new DeclarantDaoImpl(template);
  }

  @Bean
  public CreationFileService creationFileService() {
    return new CreationFileService(retrieveTmpObjectsService(), "");
  }

  @Bean
  public RetrieveTmpObjectsService retrieveTmpObjectsService() {
    return new RetrieveTmpObjectsService(commonDao());
  }

  @Bean
  public ProcessorService processorService() {
    return new ProcessorService(mappersAlmerys(), domainService(), souscripteurService());
  }

  @Bean
  public CommonDao commonDao() {
    return new CommonDaoImpl(template);
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
  public HistoriqueExecutionsDao historiqueExecutionsDao() {
    return new HistoriqueExecutionsDaoImpl(template);
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(declarationDao());
  }

  @Bean
  public DeclarationDao declarationDao() {
    return new DeclarationDaoImpl(template, beyondPropertiesService);
  }
}
