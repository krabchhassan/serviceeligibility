package com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.configuration;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.beyond.spring.starter.managementscope.ServletManagementScopeService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.FluxDaoImpl;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.elast.ElasticAuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.elast.IndexBenef;
import com.cegedim.next.serviceeligibility.core.mapper.MapperParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapper;
import com.cegedim.next.serviceeligibility.core.mapper.serviceprestationsrdo.AssureRdoServicePrestationsMapperImpl;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.RDOServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.ReferentielParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import({OmuHelperConfiguration.class})
@RequiredArgsConstructor
public class RdoPurgeConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public BeneficiaryService benefService(MongoTemplate template) {
    return new BeneficiaryService(
        template, traceService(template), beneficiaryDao(template), benefInfos());
  }

  @Bean
  public BenefInfosService benefInfos() {
    return new BenefInfosService();
  }

  @Bean
  public BeneficiaryDao beneficiaryDao(MongoTemplate mongoTemplate) {
    return new BeneficiaryDaoImpl(mongoTemplate);
  }

  @Bean
  public TraceService traceService(MongoTemplate mongoTemplate) {
    return new TraceService(traceDao(mongoTemplate));
  }

  @Bean
  public TraceDao traceDao(MongoTemplate mongoTemplate) {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public ServicePrestationService servicePrestationService(MongoTemplate mongoTemplate) {
    return new ServicePrestationService(servicePrestationDao(mongoTemplate));
  }

  @Bean
  public ServicePrestationDao servicePrestationDao(MongoTemplate mongoTemplate) {
    return new ServicePrestationDaoImpl(
        mongoTemplate, lotDao(mongoTemplate), beyondPropertiesService);
  }

  @Bean
  public LotDao lotDao(MongoTemplate mongoTemplate) {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public EventService eventService(BusinessEventProducer businessEventProducer) {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public TriggerService triggerService(MongoTemplate mongoTemplate) {
    return new TriggerService(new TriggerDaoImpl(mongoTemplate, bddAuth()));
  }

  @Bean
  public ContractService contratService(MongoTemplate mongoTemplate) {
    return new ContractService(new ContractDaoImpl(mongoTemplate));
  }

  @Bean
  public DeclarationService declarationService(MongoTemplate mongoTemplate) {
    return new DeclarationService(new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService));
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public ElasticAuthorizationScopeHandler elasticAuthorizationScopeHandler(
      ServletManagementScopeService servletManagementScopeService) {
    return new ElasticAuthorizationScopeHandler(
        servletManagementScopeService, beyondPropertiesService);
  }

  @Bean
  public BenefElasticService benefElasticService(
      ElasticsearchOperations elastic,
      RestHighLevelClient client,
      ElasticAuthorizationScopeHandler elasticAuthorizationScopeHandler) {
    return new BenefElasticService(indexBenef(), elastic, client, elasticAuthorizationScopeHandler);
  }

  @Bean
  public IndexBenef indexBenef() {
    return new IndexBenef(beyondPropertiesService);
  }

  @Bean
  public RestBeneficiaireService restBeneficiaireService(MongoTemplate mongoTemplate) {
    return new RestBeneficiaireService(new BeneficiaireConsultationHistoryDaoImpl(mongoTemplate));
  }

  @Bean
  public SasContratService sasContratService(MongoTemplate mongoTemplate) {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public ParametrageCarteTPService parametrageCarteTPService(MongoTemplate mongoTemplate) {
    return new ParametrageCarteTPService(
        parametrageCarteTPDao(mongoTemplate), lotDao(mongoTemplate), mapperParametrageCarteTP());
  }

  @Bean
  public ParametrageCarteTPDao parametrageCarteTPDao(MongoTemplate mongoTemplate) {
    return new ParametrageCarteTPDaoImpl(mongoTemplate, bddAuth());
  }

  @Bean
  public MapperParametrageCarteTP mapperParametrageCarteTP() {
    return new MapperParametrageCarteTP();
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
  public AuthenticationFacade bddAuth() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public DeclarationConsolideDao declarationConsolideDao(MongoTemplate mongoTemplate) {
    return new DeclarationConsolideDaoImpl(mongoTemplate);
  }

  @Bean
  public CarteDematDao carteDematDao(MongoTemplate mongoTemplate) {
    return new CarteDematDaoImpl(mongoTemplate);
  }

  @Bean
  public CartePapierDao cartePapierDao(MongoTemplate mongoTemplate) {
    return new CartePapierDaoImpl(mongoTemplate);
  }

  @Bean
  public FluxDao fluxDao(MongoTemplate mongoTemplate) {
    return new FluxDaoImpl(mongoTemplate);
  }

  @Bean
  public HistoriqueExecutionsDao historiqueExecutionsDao(MongoTemplate mongoTemplate) {
    return new HistoriqueExecutionsDaoImpl(mongoTemplate);
  }

  @Bean
  public HistoriqueExecutionsRenouvellementDao historiqueExecutionsRenouvellementDao(
      MongoTemplate mongoTemplate) {
    return new HistoriqueExecutionsRenouvellementDaoImpl(mongoTemplate);
  }

  @Bean
  public RDOServicePrestationService rdoServicePrestationService(MongoTemplate mongoTemplate) {
    return new RDOServicePrestationService(
        rdoServicePrestationDao(mongoTemplate),
        assureRdoServicePrestationsMapper(),
        beyondPropertiesService);
  }

  @Bean
  public RDOServicePrestationDAO rdoServicePrestationDao(MongoTemplate mongoTemplate) {
    return new RDOServicePrestationDAOImpl(mongoTemplate);
  }

  @Bean
  public AssureRdoServicePrestationsMapper assureRdoServicePrestationsMapper() {
    return new AssureRdoServicePrestationsMapperImpl();
  }
}
