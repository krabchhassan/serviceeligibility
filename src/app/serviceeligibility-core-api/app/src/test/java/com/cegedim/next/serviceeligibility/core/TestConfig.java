package com.cegedim.next.serviceeligibility.core;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.serviceeligibility.common.organisation.OrganisationServiceWrapper;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.*;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarationBackendService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarationBackendServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddServiceImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.VisioDroitsService;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.LotRepository;
import com.cegedim.next.serviceeligibility.core.bobb.lot.LotXlsxService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.LotService;
import com.cegedim.next.serviceeligibility.core.business.beneficiaire.dao.BeneficiaireDao;
import com.cegedim.next.serviceeligibility.core.business.carte.service.CarteDematServiceImpl;
import com.cegedim.next.serviceeligibility.core.business.consultationdroits.ConsultationDroitsSteps;
import com.cegedim.next.serviceeligibility.core.business.consultationdroits.idb.IDBService;
import com.cegedim.next.serviceeligibility.core.business.consultationdroits.idb.IDBSteps;
import com.cegedim.next.serviceeligibility.core.business.contrat.dao.ContratDao;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantServiceImpl;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.RestDeclarantService;
import com.cegedim.next.serviceeligibility.core.business.declaration.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.business.declaration.service.DeclarationServiceImpl;
import com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.elast.ElasticAuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.elast.IndexBenef;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.MapperContratToContractDto;
import com.cegedim.next.serviceeligibility.core.features.utils.ContractRightsByBeneficiaryService;
import com.cegedim.next.serviceeligibility.core.mapper.MapperBenefDetails;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMaille;
import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMailleImpl;
import com.cegedim.next.serviceeligibility.core.mapper.MapperDomaineDroitContractDto;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUAPRightEvent;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUAPRightTDB;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTP;
import com.cegedim.next.serviceeligibility.core.mapper.pau.MapperUniqueAccessPointServiceTPV5;
import com.cegedim.next.serviceeligibility.core.services.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.*;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPAgregationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pau.*;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.soap.carte.mapper.CarteDematMapper;
import com.cegedim.next.serviceeligibility.core.soap.carte.mapper.CarteDematMapperImpl;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.*;
import com.cegedim.next.serviceeligibility.core.soap.consultation.ws.BddsPriorityTpContractToBlbController;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacadeImpl;
import com.cegedim.next.serviceeligibility.core.utils.RequestValidator;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.webservices.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.mockito.Mockito;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("test")
@EnableMongoRepositories(
    basePackageClasses = {
      MapperAffiliationImpl.class,
      MapperCarteDematImpl.class,
      MapperContratImpl.class,
      MapperDomaineConventionImpl.class,
      MapperPeriodeDroitImpl.class,
      MapperBenefCarteDematImpl.class,
      MapperBeneficiaireCouvertureImpl.class,
      MapperPrestationImpl.class,
      MapperFormuleImpl.class,
      MapperFormuleMetierImpl.class,
      MapperParametreImpl.class,
      MapperAdresseImpl.class,
      MapperTypeAdresseImpl.class,
      MapperConventionImpl.class,
      MapperBeneficiaireImpl.class,
      MapperConventionnementImpl.class,
      MapperDeclarant.class,
      MapperDeclarantImpl.class,
      MapperDeclarantEchange.class,
      MapperDeclarationImpl.class,
      MapperDomaineDroitImpl.class,
      MapperPrioriteDroitImpl.class,
      MapperTypeConventionnementImpl.class,
      DeclarantBackendServiceImpl.class,
      DeclarantServiceImpl.class,
      DeclarationServiceImpl.class,
      HistoriqueDeclarantDao.class,
      MapperContractToDeclarationDto.class,
      MapperDroitsConsult.class,
      SettingsUIService.class,
      DeclarantBackendDao.class,
      ServiceDroitsDao.class
    })
public class TestConfig {

  @MockBean MongoTemplate mongoTemplate;

  @MockBean CacheManager cacheManager;

  @MockBean public OrganisationServiceWrapper organisationServiceWrapper;

  @MockBean BusinessEventProducer businessEventProducer;

  @MockBean public AuthorizationScopeHandler authorizationScopeHandler;

  @MockBean BeyondPropertiesService beyondPropertiesService;

  @MockBean RestConnector restConnector;

  @MockBean ParametrageCarteTPDaoImpl parametrageCarteTPDao;

  @MockBean AlmerysProductReferentialRepository almerysProductReferentialRepository;

  @Bean
  public GenerateContract generateContract() {
    return new GenerateContract();
  }

  @Bean
  public LotXlsxService lotXlsxService() {
    return new LotXlsxService(
        beyondPropertiesService, objectMapper(), lotService(), contractElementService());
  }

  @Bean
  public MapperBenefCarteDematImpl mapperBenefCarteDematImpl() {
    return new MapperBenefCarteDematImpl();
  }

  @Bean
  public MapperBeneficiaireCouvertureImpl mapperBeneficiaireCouvertureImpl() {
    return new MapperBeneficiaireCouvertureImpl();
  }

  @Bean
  public MapperBeneficiaryPaymentRecipientsImpl mapperBeneficiaryPaymentRecipients() {
    return new MapperBeneficiaryPaymentRecipientsImpl();
  }

  @Bean
  public PwCachedService pwCachedService() {
    return new PwCachedService(restConnector, cacheManager, beyondPropertiesService);
  }

  @Bean
  public PwService pwService() {
    return new PwService(pwCachedService(), objectMapper(), beyondPropertiesService);
  }

  @Bean
  public SettingsUIService settingsUIService() {
    return Mockito.mock(SettingsUIService.class);
  }

  @Bean
  public MapperCarteDematImpl mapperCarteDematImpl() {
    return new MapperCarteDematImpl();
  }

  @Bean
  public MapperConventionImpl mapperConventionImpl() {
    return new MapperConventionImpl();
  }

  @Bean
  public MapperDomaineConventionImpl mapperDomaineConventionImpl() {
    return new MapperDomaineConventionImpl();
  }

  @Bean
  public MapperAdresseImpl mapperAdresseImpl() {
    return new MapperAdresseImpl();
  }

  @Bean
  public MapperTypeAdresseImpl mapperTypeAdresse() {
    return new MapperTypeAdresseImpl();
  }

  @Bean
  public MapperAdresseAvecFixeImpl mapperAdresseAvecFixeImpl() {
    return new MapperAdresseAvecFixeImpl(mapperTypeAdresse());
  }

  @Bean
  public MapperAffiliationImpl mapperAffiliationImpl() {
    return new MapperAffiliationImpl();
  }

  @Bean
  public MapperBeneficiaireImpl mapperBeneficiaireImpl() {
    return new MapperBeneficiaireImpl(mapperAdresseAvecFixeImpl(), mapperAffiliationImpl());
  }

  @Bean
  public MapperContratImpl mapperContratImpl() {
    return new MapperContratImpl();
  }

  @Bean
  public MapperConventionnementImpl mapperConventionnementImpl() {
    return new MapperConventionnementImpl();
  }

  @Bean
  public MapperDeclarant mapperDeclarant() {
    return new MapperDeclarant();
  }

  @Bean
  public MapperDeclarantImpl mapperDeclarantImpl() {
    return new MapperDeclarantImpl();
  }

  @Bean
  public MapperDeclarantEchange mapperDeclarantEchange() {
    return new MapperDeclarantEchange();
  }

  @Bean
  public MapperDeclarationImpl mapperDeclarationImpl() {
    return new MapperDeclarationImpl();
  }

  @Bean
  public MapperDomaineDroitImpl mapperDomaineDroitImpl() {
    return new MapperDomaineDroitImpl();
  }

  @Bean
  public MapperDomaineDroitContractDto mapperDomaineDroitContractDto() {
    return new MapperDomaineDroitContractDto();
  }

  @Bean
  public MapperFormuleImpl mapperFormuleImpl() {
    return new MapperFormuleImpl();
  }

  @Bean
  public MapperFormuleMetierImpl mapperFormuleMetierImpl() {
    return new MapperFormuleMetierImpl();
  }

  @Bean
  public MapperParametreImpl mapperParametreImpl() {
    return new MapperParametreImpl();
  }

  @Bean
  public MapperPeriodeDroitImpl mapperPeriodeDroitImpl() {
    return new MapperPeriodeDroitImpl();
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
  public MapperConventionTP mapperConventionTP() {
    return new MapperConventionTP();
  }

  @Bean
  public MapperCodeRenvoiTP mapperCodeRenvoiTP() {
    return new MapperCodeRenvoiTP();
  }

  @Bean
  public MapperFondCarteTP mapperFondCarteTP() {
    return new MapperFondCarteTP();
  }

  @Bean
  public MapperRegroupementDomainesTP mapperRegroupementDomainesTP() {
    return new MapperRegroupementDomainesTP();
  }

  @Bean
  public MapperPrestationImpl mapperPrestationImpl() {
    return new MapperPrestationImpl();
  }

  @Bean
  public MapperPrioriteDroitImpl mapperPrioriteDroitImpl() {
    return new MapperPrioriteDroitImpl();
  }

  @Bean
  public MapperTypeAdresseImpl mapperTypeAdresseImpl() {
    return new MapperTypeAdresseImpl();
  }

  @Bean
  public MapperTypeConventionnementImpl mapperTypeConventionnementImpl() {
    return new MapperTypeConventionnementImpl();
  }

  @Bean
  public DeclarantBackendServiceImpl declarantBackendService() {
    return new DeclarantBackendServiceImpl(
        this.declarantBackendDao(),
        this.mapperDeclarant(),
        this.mapperPilotage(),
        this.mapperTranscodageDomaineTP(),
        this.mapperConventionTP(),
        this.mapperCodeRenvoiTP(),
        this.mapperRegroupementDomainesTP(),
        this.mapperFondCarteTP(),
        this.mapperDeclarantEchange(),
        this.historiqueDeclarantDao(),
        this.serviceDroitsDao(),
        this.eventService());
  }

  @Bean
  public EventService eventService() {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }

  @Bean
  public ReferentielParametrageCarteTPDao referentielParametrageCarteTPDao() {
    return new ReferentielParametrageCarteTPDaoImpl(mongoTemplate);
  }

  @Bean
  public ReferentielParametrageCarteTPService referentielParametrageCarteTPService() {
    return new ReferentielParametrageCarteTPService(this.referentielParametrageCarteTPDao());
  }

  @Bean
  public MapperContratToContractDto mapperContratToContractDto() {
    return new MapperContratToContractDto(
        beyondPropertiesService,
        this.declarantService(),
        this.mapperDeclarantImpl(),
        this.mapperAdresseImpl(),
        this.mapperAffiliationImpl(),
        this.contractToDeclarationMapper(),
        this.referentielParametrageCarteTPService(),
        this.mapperContratTPToContratDto());
  }

  @Bean
  public ConsultationDroitsSteps consultationDroitsSteps() {
    return new ConsultationDroitsSteps(this.contratDao, this.mapperContratToContractDto());
  }

  @Bean
  public DeclarantServiceImpl declarantServiceImpl() {
    return new DeclarantServiceImpl();
  }

  @Bean
  public RestDeclarantService restDeclarantService() {
    return new RestDeclarantService();
  }

  @MockBean public BeneficiaireDao beneficiaireDao;

  @MockBean public PrestIJDao prestIJDao;

  @MockBean public ServicePrestationMongoDao servicePrestationMongoDao;

  @Bean
  public ServicePrestationTraceDao servicePrestationTraceDao() {
    return new ServicePrestationTraceDaoImpl(mongoTemplate);
  }

  @Bean
  public ServicePrestIJTraceDao servicePrestIJTraceDao() {
    return new ServicePrestIJTraceDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarantBackendDao declarantBackendDao() {
    return new DeclarantBackendDaoImpl(mongoTemplate);
  }

  @Bean
  public CarteDematServiceImpl carteDematService() {
    return new CarteDematServiceImpl(this.carteDematDao);
  }

  @MockBean public CarteDematDaoImpl carteDematDao;

  @Bean
  public RestBeneficiaireService restBeneficiaireService() {
    return new RestBeneficiaireService(beneficiaireConsultationHistoryDao());
  }

  @Bean
  public BeneficiaireConsultationHistoryDao beneficiaireConsultationHistoryDao() {
    return new BeneficiaireConsultationHistoryDaoImpl(mongoTemplate);
  }

  @MockBean public DeclarantDao declarantDao;

  @MockBean public DeclarationDao declarationDao;

  @Bean
  @Primary
  public DeclarationServiceImpl declarationServiceImpl() {
    return new DeclarationServiceImpl(declarationDao);
  }

  @MockBean public ContratDao contratDao;

  @Bean
  @Primary
  public VisioDroitsService visioDroitsService() {
    return Mockito.mock(VisioDroitsService.class);
  }

  @Bean
  @Primary
  public ParametreBddServiceImpl parametreBddServiceImpl() {
    return Mockito.mock(ParametreBddServiceImpl.class);
  }

  @Bean
  @Primary
  public MapperContratTPToContratDto mapperContratTPToContratDto() {
    return new MapperContratTPToContratDto(beyondPropertiesService, contractToDeclarationMapper());
  }

  @Bean
  @Primary
  public MapperContractToDeclarationDto mapperContractToDeclarationDto() {
    return new MapperContractToDeclarationDto(
        this.contractToDeclarationMapper(),
        this.mapperAdresseImpl(),
        mapperDeclarantImpl(),
        this.declarantServiceImpl(),
        this.mapperAffiliationImpl(),
        this.mapperContratTPToContratDto());
  }

  @Bean
  @Primary
  public MapperDroitsConsult mapperDroitsConsult() {
    return new MapperDroitsConsult(
        parametreBddDaoImpl(), parametreBddServiceImpl(), beyondPropertiesService);
  }

  @Bean
  public ParametreBddDaoImpl parametreBddDaoImpl() {
    return new ParametreBddDaoImpl(mongoTemplate);
  }

  @Bean
  @Primary
  public DeclarantService declarantService() {
    return new DeclarantServiceImpl();
  }

  @Bean
  public IDBSteps idbSteps() {
    return new IDBSteps();
  }

  @Bean
  public IDBService idbService() {
    return new IDBService(this.idbSteps());
  }

  @Bean
  public IssuingCompanyCodeService issuingCompanyCodeService() {
    return new IssuingCompanyCodeService();
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
  public DeclarationBackendDao declarationBackendDao() {
    return new DeclarationBackendDaoImpl(mongoTemplate);
  }

  @Bean
  public ContractBackendDao contractBackendDao() {
    return new ContractBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean
  public OcService ocService() {
    return new OcService(organisationServiceWrapper);
  }

  @Bean
  public ContractElementService contractElementService() {
    return new ContractElementService(this.contractElementRepository());
  }

  @MockBean public ElasticAuthorizationScopeHandler elasticAuthorizationScopeHandler;

  @MockBean public ContractElementService contractElementServiceMock;

  @Bean
  public ContractElementRepository contractElementRepository() {
    return new ContractElementRepository();
  }

  @Bean
  public MapperUniqueAccessPointServiceTP mapperUniqueAccessPointServiceTP() {
    return new MapperUniqueAccessPointServiceTP(
        ocService(), mapperUAPRightTDBV4(), mapperUAPRightEventV4(), beyondPropertiesService);
  }

  @Bean
  public MapperUniqueAccessPointServiceTPV5 mapperUniqueAccessPointServiceTPV5() {
    return new MapperUniqueAccessPointServiceTPV5(
        ocService(), mapperUAPRightTDBV4(), mapperUAPRightEventV4(), beyondPropertiesService);
  }

  @Bean
  public CalculDroitsTPPAUService calculDroitsTPPAUService() {
    return new CalculDroitsTPPAUService(
        pwService(), contractElementServiceMock, carenceService(), beyondPropertiesService);
  }

  @Bean
  public CarenceService carenceService() {
    return Mockito.mock(CarenceService.class);
  }

  @Bean("uniqueAccessPointTriV5TpOnline")
  public UniqueAccessPointTriTP uniqueAccessPointTpOnlineTriV5Impl() {
    return new UniqueAccessPointTriTpOnlineV5Impl();
  }

  @Bean("uniqueAccessPointTriV5TpOffline")
  public UniqueAccessPointTriTP uniqueAccessPointTpOfflineTriV5Impl() {
    return new UniqueAccessPointTriTpOfflineV5Impl();
  }

  @Bean
  public UniqueAccessPointTpSortSubscriber uniqueAccessPointTpOnlineSortV5Impl() {
    return new UniqueAccessPointTpOnlineSortSubscriberV5Impl();
  }

  @Bean
  public UniqueAccessPointTpOnlineSortRightsV5Impl uniqueAccessPointTpOnlineSortRightsV5Impl() {
    return new UniqueAccessPointTpOnlineSortRightsV5Impl();
  }

  @Bean
  public MapperUAPRightEvent mapperUAPRightEventV4() {
    return new MapperUAPRightEvent();
  }

  @Bean
  public MapperUAPRightTDB mapperUAPRightTDBV4() {
    return new MapperUAPRightTDB(pwService());
  }

  @Bean("beneficiaireHTPBackendDao")
  public BeneficiaireBackendDao beneficiaireHTPBackendDao() {
    return new BeneficiaireHTPBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean("beneficiaireTPOnlineBackendDao")
  public BeneficiaireBackendDao beneficiaireTPOnlineBackendDao() {
    return new BeneficiaireTPOnlineBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean("beneficiaireTPOfflineBackendDao")
  public BeneficiaireBackendDao beneficiaireTPOfflineBackendDao() {
    return new BeneficiaireTPOfflineBackendDaoImpl(authorizationScopeHandler, mongoTemplate);
  }

  @Bean("historiqueDeclarantDao")
  public HistoriqueDeclarantDao historiqueDeclarantDao() {
    return new HistoriqueDeclarantDaoImpl(mongoTemplate);
  }

  @Bean("serviceDroitsDao")
  public ServiceDroitsDao serviceDroitsDao() {
    return new ServiceDroitsDaoImpl(mongoTemplate);
  }

  @Bean
  public GenerateContratAIV5 generateContratAIV5() {
    return new GenerateContratAIV5();
  }

  @Bean
  public RequestValidator requestValidator() {
    return new RequestValidator();
  }

  @Bean
  public GlobalValidationService validationService() {
    return new GlobalValidationService();
  }

  @Bean
  public Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Bean
  public ReferentialService referentialService() {
    return new ReferentialService(restConnector, beyondPropertiesService);
  }

  @Bean
  public BddsPriorityTpContractToBlbController bddsPriorityTpContractToBlbController() {
    return new BddsPriorityTpContractToBlbController(
        beyondPropertiesService,
        uniqueAccessPointServiceV5TPOnlineImpl(),
        uniqueAccessPointServiceV5TPOfflineImpl(),
        declarantBackendService());
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public LotDao lotDao() {
    return new LotDaoImpl(mongoTemplate);
  }

  @Bean
  public LotRepository lotRepository() {
    return new LotRepository(mongoTemplate);
  }

  @Bean
  public LotService lotService() {
    return new LotService(
        this.lotRepository(),
        lotDao(),
        parametrageCarteTPDao,
        almerysProductReferentialRepository,
        contractElementRepository());
  }

  @Bean
  public UAPForceService uapForceService() {
    return new UAPForceService();
  }
  ;

  @MockBean ElasticsearchOperations elastic;

  @Bean
  public BeneficiaryDetailsService beneficiaryDetailsService() {
    return new BeneficiaryDetailsService(
        beneficiaryService(),
        declarationBackendService(),
        contractService(),
        mapperBenefDetails(),
        elasticHistorisationContractService(),
        restitutionCarteDao(),
        sasContratService());
  }

  @Bean
  public ContractService contractService() {
    return new ContractService(contractDao());
  }

  @Bean
  public ContractDao contractDao() {
    return new ContractDaoImpl(mongoTemplate);
  }

  @Bean
  public CartesService cartesService() {
    return new CartesService(carteDematDao, cartePapierDao(), declarationService());
  }

  @Bean
  public DeclarationService declarationService() {
    return new DeclarationService(new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService));
  }

  @Bean
  public CartePapierDao cartePapierDao() {
    return new CartePapierDaoImpl(mongoTemplate);
  }

  @Bean
  public MapperBenefDetails mapperBenefDetails() {
    return new MapperBenefDetails(
        cartesService(), contractTPAgregationService(), beyondPropertiesService);
  }

  @Bean
  public BeneficiaryService beneficiaryService() {
    return new BeneficiaryService(
        mongoTemplate, traceService(), beneficiaryDao(), benefInfosService());
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
  public TraceDao traceDao() {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public BenefInfosService benefInfosService() {
    return new BenefInfosService();
  }

  @Bean
  public DeclarationBackendService declarationBackendService() {
    return new DeclarationBackendServiceImpl(
        declarationBackendDao(),
        mapperDeclaration(),
        mapperHistoriqueDeclarations(),
        mapperDroits(),
        carteDematDao,
        declarantBackendDao());
  }

  @Bean
  public MapperDeclaration mapperDeclaration() {
    return new MapperDeclaration();
  }

  @Bean
  public MapperContractDto mapperContract() {
    return new MapperContractDto();
  }

  @Bean
  public MapperHistoriqueDeclarations mapperHistoriqueDeclarations() {
    return new MapperHistoriqueDeclarations();
  }

  @Bean
  public MapperDroits mapperDroits() {
    return new MapperDroits();
  }

  @Bean
  public MapperIdentificationAssure mapperIdentificationAssure() {
    return new MapperIdentificationAssure();
  }

  @Bean
  public MapperAttestation mapperAttestation() {
    return new MapperAttestation();
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
  public MapperAdresse mapperAdresse() {
    return new MapperAdresse();
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
  public MapperAttestationDetail mapperAttestationDetail() {
    return new MapperAttestationDetail();
  }

  @Bean
  public MapperTracePriorisation mapperTracePriorisation() {
    return new MapperTracePriorisation();
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

  @Bean("bddAuth")
  public AuthenticationFacade authenticationFacade() {
    return new AuthenticationFacadeImpl();
  }

  @Bean
  public BenefElasticService benefElasticService() {
    return new BenefElasticService(indexBenef, elastic, client, elasticAuthorizationScopeHandler);
  }

  @MockBean IndexBenef indexBenef;

  @MockBean RestHighLevelClient client;

  @MockBean public S3Service s3Service;

  @Bean
  public ContractToDeclarationMapper contractToDeclarationMapper() {
    return new ContractToDeclarationMapperImpl();
  }

  @Bean
  public BaseDroitMapper baseDroitMapper() {
    return new BaseDroitMapperImpl();
  }

  @Bean
  public CarteDematMapper carteDematMapper() {
    return new CarteDematMapperImpl();
  }

  @Bean
  public ElasticHistorisationContractService elasticHistorisationContractService() {
    return new ElasticHistorisationContractService(
        historiqueContratRepository, indexHistoContrat(), objectMapper(), client);
  }

  @Bean
  IndexHistoContrat indexHistoContrat() {
    return new IndexHistoContrat(beyondPropertiesService);
  }

  @MockBean HistoriqueContratRepository historiqueContratRepository;

  @Bean
  public MapperContractTPMaille mapperContractTPMaille() {
    return new MapperContractTPMailleImpl();
  }

  @Bean
  public ContractTPAgregationService contractTPAgregationService() {
    return new ContractTPAgregationService(mapperContractTPMaille());
  }

  @Bean
  public SasContratService sasContratService() {
    return new SasContratService(new SasContratDaoImpl(mongoTemplate));
  }

  @Bean
  public ContractRightsByBeneficiaryService contractRightsByBeneficiaryService() {
    return new ContractRightsByBeneficiaryService(
        contractBackendDao(),
        mapperUniqueAccessPointServiceTP(),
        uniqueAccessPointTpOnlineSortRightsV5Impl(),
        contractRightsByBeneficiaryTriHTP(),
        contractRightsByBeneficiaryTriTP(),
        uniqueAccessPointServiceHTPV5());
  }

  @Bean
  public UniqueAccessPointTpOfflineSortRightsV5Impl uniqueAccessPointTpOfflineSortRightsV5Impl() {
    return new UniqueAccessPointTpOfflineSortRightsV5Impl();
  }

  @Bean
  public UniqueAccessPointTriTpOfflineV5Impl uniqueAccessPointTriTpOfflineV5Impl() {
    return new UniqueAccessPointTriTpOfflineV5Impl();
  }

  @Bean
  public UniqueAccessPointTriTpOnlineV5Impl uniqueAccessPointTriTpOnlineV5Impl() {
    return new UniqueAccessPointTriTpOnlineV5Impl();
  }

  @Bean
  public UniqueAccessPointTriHtpV5Impl uniqueAccessPointHtpTriV5() {
    return new UniqueAccessPointTriHtpV5Impl();
  }

  @Bean
  public UniqueAccessPointServiceV5HTPImpl uniqueAccessPointServiceHTPV5() {
    return new UniqueAccessPointServiceV5HTPImpl(
        uniqueAccessPointHtpTriV5(),
        mapperUniqueAccessPointServiceTP(),
        calculDroitsTPPAUService(),
        contractBackendDao(),
        beneficiaireHTPBackendDao());
  }

  @Bean
  public MapperPaymentRecipient mapperPaymentRecipient() {
    return new MapperPaymentRecipient();
  }

  @Bean
  public ContractRightsByBeneficiaryTriHTPImpl contractRightsByBeneficiaryTriHTP() {
    return new ContractRightsByBeneficiaryTriHTPImpl();
  }

  @Bean
  public ContractRightsByBeneficiaryTriTPImpl contractRightsByBeneficiaryTriTP() {
    return new ContractRightsByBeneficiaryTriTPImpl();
  }
}
