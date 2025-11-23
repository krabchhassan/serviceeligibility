package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.MotifEvenement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.TriggerTestPeriode;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametreSimple;
import com.cegedim.next.serviceeligibility.core.utils.*;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerNotFoundException;
import com.mongodb.client.ClientSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerBuildDeclarationServiceTest {

  private final Logger logger = LoggerFactory.getLogger(TriggerBuildDeclarationServiceTest.class);
  private static final String TRIGGERED_BENEFICIARY_COLLECTION = "triggeredBeneficiary";
  private static final String TRIGGER_COLLECTION = "trigger";

  @Autowired MongoTemplate mongoTemplate;

  @Autowired private TriggerService triggerService;

  @Autowired private TriggerKafkaService triggerKafkaService;

  @Autowired private TriggerBuildDeclarationNewService triggerBuildDeclarationNewService;

  @Autowired private SasContratService sasContratService;

  @Autowired private TriggerDao triggerDao;

  @Autowired private RestConnector restConnector;

  @Autowired private ContractElementService contractElementService;

  @SpyBean private DeclarationService declarationService;

  @Autowired private ProductElementService productElementService;

  @BeforeEach
  void before() {
    mongoTemplate.findAllAndRemove(new Query(), "parametragesCarteTP");
    mongoTemplate.findAllAndRemove(new Query(), "declarations");
    mongoTemplate.findAllAndRemove(new Query(), Constants.SAS_CONTRAT_COLLECTION);
    mongoTemplate.findAllAndRemove(new Query(), "triggeredBeneficiary");
    triggerService.removeAll();
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    ReflectionTestUtils.setField(triggerDao, "authenticationFacade", authenticationFacade);

    Mockito.when(mongoTemplate.save(Mockito.any(Trigger.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });

    Mockito.when(mongoTemplate.save(Mockito.any(Declaration.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });

    Mockito.when(mongoTemplate.save(Mockito.any(TriggeredBeneficiary.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });

    Trigger trigger = getTrigger();
    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(trigger);

    TriggeredBeneficiary triggeredBenef = TriggerDataForTesting.getTriggeredBenef1("UUID");
    Mockito.when(
            mongoTemplate.findById(Mockito.anyString(), Mockito.eq(TriggeredBeneficiary.class)))
        .thenReturn(triggeredBenef);

    ParametreSimple parametreSimple = new ParametreSimple("code", "codeValeur", "libelleValeur");
    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(Aggregation.class),
                Mockito.anyString(),
                Mockito.eq(ParametreSimple.class)))
        .thenReturn(new AggregationResults<>(List.of(parametreSimple), new Document()));
    ProductElementLight productElementLight = new ProductElementLight();
    productElementLight.setCodeAmc("TEST");
    productElementLight.setCodeProduct("PRODUIT");
    productElementLight.setCodeOffer("OFFRE");

    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(Aggregation.class),
                Mockito.anyString(),
                Mockito.eq(ProductElementLight.class)))
        .thenReturn(new AggregationResults<>(List.of(productElementLight), new Document()));

    mockBobb();

    ParametreBdd parametreBdd = new ParametreBdd();
    parametreBdd.setCode("CODE");

    final HashMap<String, Object> mapParameter = new LinkedHashMap<>();
    mapParameter.put("code", "SP");
    final HashMap<String, Object> mapParameter2 = new LinkedHashMap<>();
    mapParameter2.put("code", "IS");
    final List<Object> valueList = new ArrayList<>();
    valueList.add(mapParameter);
    valueList.add(mapParameter2);
    parametreBdd.setListeValeurs(valueList);
    Mockito.when(mongoTemplate.findOne(Mockito.any(Query.class), Mockito.eq(ParametreBdd.class)))
        .thenReturn(parametreBdd);

    TriggerDataForTesting.initializeProductElementLight(productElementService);
  }

  @Test
  void searchTrigger() {
    TriggerRequest request = getSimpleRequest();

    int perPage = 10;
    int page = 1;
    String sortBy = null;
    String direction = null;

    // Request simple
    TriggerResponse response =
        triggerService.getTriggers(perPage, page, sortBy, direction, request);
    Assertions.assertNotNull(response);

    // request complex with nir
    request = getComplexRequest(null);

    TriggerResponse expectedResponse = new TriggerResponse();
    PagingResponseModel paging = new PagingResponseModel();
    paging.setTotalElements(2);
    expectedResponse.setPaging(paging);

    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(Aggregation.class),
                Mockito.anyString(),
                Mockito.eq(TriggerResponse.class)))
        .thenReturn(new AggregationResults<>(List.of(expectedResponse), new Document()));
    response = triggerService.getTriggers(perPage, page, sortBy, direction, request);
    Assertions.assertNotNull(response);

    // request complex with nir and emitters
    List<TriggerEmitter> emitters = new ArrayList<>();
    emitters.add(TriggerEmitter.Renewal);
    request = getComplexRequest(emitters);
    response = triggerService.getTriggers(perPage, page, sortBy, direction, request);
    Assertions.assertNotNull(response);
  }

  @Test
  void searchTriggerWithIdDeclarantAndNumeroContrat() {
    TriggerRequest request = getComplexRequestWithNumContrat();

    int perPage = 10;
    int page = 1;
    String sortBy = null;
    String direction = null;

    TriggerResponse expectedResponse = new TriggerResponse();
    PagingResponseModel paging = new PagingResponseModel();
    paging.setTotalElements(2);
    expectedResponse.setPaging(paging);

    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(Aggregation.class),
                Mockito.anyString(),
                Mockito.eq(TriggerResponse.class)))
        .thenReturn(new AggregationResults<>(List.of(expectedResponse), new Document()));
    TriggerResponse response =
        triggerService.getTriggers(perPage, page, sortBy, direction, request);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(2, response.getPaging().getTotalElements());
  }

  @Test
  void testLog() {
    String log = TriggerUtils.getLog(getParametrageCarteTP());
    logger.info(log);
    Assertions.assertNotNull(log);
  }

  private TriggerRequest getSimpleRequest() {
    TriggerRequest request = new TriggerRequest();

    List<String> amcs = new ArrayList<>();
    amcs.add("1234567890");
    request.setAmcs(amcs);

    return request;
  }

  private TriggerRequest getComplexRequest(List<TriggerEmitter> emitters) {
    TriggerRequest request = getSimpleRequest();
    request.setNir("1234567890123");
    request.setEmitters(emitters);
    return request;
  }

  private TriggerRequest getComplexRequestWithNumContrat() {
    TriggerRequest request = getSimpleRequest();
    List<String> amcs = new ArrayList<>();
    amcs.add("1234567890");
    request.setAmcs(amcs);
    request.setNumeroContrat("C1");
    request.setIsContratIndividuel(true);
    return request;
  }

  private Trigger getTrigger() {
    Trigger t = new Trigger();

    t.setId("11111");
    t.setAmc("1234567890");
    t.setDateEffet("2021-06-21");
    t.setOrigine(TriggerEmitter.Renewal);
    t.setStatus(TriggerStatus.StandBy);

    t.setDateDebutTraitement(new Date());

    int count = createTriggeredBeneficiaries();
    t.setNbBenef(count);

    t.setStatus(TriggerStatus.ToProcess);

    return t;
  }

  private int createTriggeredBeneficiaries() {
    TriggeredBeneficiary tb1 = TriggerDataForTesting.getTriggeredBenef1("UUID");
    triggerService.createTriggeredBenef(tb1);

    TriggeredBeneficiary tb2 = TriggerDataForTesting.getTriggeredBenef2();
    triggerService.createTriggeredBenef(tb2);

    return 2;
  }

  @Test
  void buildDeclarationWithTriggerBenefeciaryWithError() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    tb.setStatut(TriggeredBeneficiaryStatusEnum.Error);
    tb.setDerniereAnomalie(TriggeredBeneficiaryAnomaly.create(Anomaly.SAS_FOUND_FOR_THIS_CONTRACT));
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    boolean declarationGenerated =
        triggerBuildDeclarationNewService.buildDeclarationsForBenef(
            createTrigger(), manageBenefsContract, tb, null, null);

    Assertions.assertFalse(declarationGenerated);
  }

  @Test
  void buildDeclarationWithPilotageBO() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    tb.setIsCarteDematerialisee(false);
    tb.setIsCartePapierAEditer(false);
    tb.setIsCartePapier(false);
    tb.getNewContract().getPeriodesDroitsCarte().setDebut("2025-01-01");
    tb.getNewContract().getPeriodesDroitsCarte().setFin("2025-12-31");
    tb.getNewContract()
        .getDroitsGaranties()
        .forEach(droit -> droit.getPeriode().setFin("2025-12-31"));

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());

    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.PilotageBO);
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(0);
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP
    ParametrageDroitsCarteTP parametrageDroitsCarteTP =
        TriggerDataForTesting.getParametrageDroitsCarteTP();
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);

    mockPW();

    boolean declarationGenerated =
        triggerBuildDeclarationNewService.buildDeclarationsForBenef(
            createTrigger(), manageBenefsContract, tb, parametrageCarteTP, null);

    Assertions.assertTrue(declarationGenerated);
    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    Assertions.assertEquals("HOSP", declaration.getDomaineDroits().get(0).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(0).getIsEditable());
    Assertions.assertEquals(
        "2021/01/01", declaration.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertEquals(
        "2025/12/31", declaration.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());

    tb.setIsCartePapierAEditer(true);
    manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());

    declarationGenerated =
        triggerBuildDeclarationNewService.buildDeclarationsForBenef(
            createTrigger(), manageBenefsContract, tb, parametrageCarteTP, null);
    Assertions.assertTrue(declarationGenerated);
    declaration = manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(0).getIsEditable());
  }

  @Test
  void buildDeclarationWithPilotageBO2() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());

    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.PilotageBO);
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);

    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, parametrageCarteTP, null);

    Assertions.assertTrue(manageBenefsContract.getNbBenefWarning() > 0);
  }

  @Test
  void buildDeclaration() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"), manageBenefsContract, tb, pc, null);

    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    Assertions.assertEquals("HOSP", declaration.getDomaineDroits().get(0).getCode());
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(0).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/03/31", declaration.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("HOSP", declaration.getDomaineDroits().get(1).getCode());
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(1).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(1).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertNull(declaration.getDomaineDroits().get(1).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("DENT", declaration.getDomaineDroits().get(2).getCode());
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(2).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(2).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/03/31", declaration.getDomaineDroits().get(2).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("DENT", declaration.getDomaineDroits().get(3).getCode());
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(3).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(3).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertNull(declaration.getDomaineDroits().get(3).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("RADI", declaration.getDomaineDroits().get(4).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(4).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(4).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/03/31", declaration.getDomaineDroits().get(4).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("RADI", declaration.getDomaineDroits().get(5).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(5).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(5).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertNull(declaration.getDomaineDroits().get(5).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("MEDE", declaration.getDomaineDroits().get(6).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(6).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(6).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/03/31", declaration.getDomaineDroits().get(6).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("MEDE", declaration.getDomaineDroits().get(7).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(7).getIsEditable());
    Assertions.assertEquals(
        "2021/03/02", declaration.getDomaineDroits().get(7).getPeriodeOnline().getPeriodeDebut());
    Assertions.assertNull(declaration.getDomaineDroits().get(7).getPeriodeOnline().getPeriodeFin());
  }

  @Test
  void buildDeclarationFermeeWithDateRestitution() {
    Declaration declarationAFermer = getDeclarationAFermer();
    mongoTemplate.save(declarationAFermer);

    Trigger trigger = new Trigger();
    trigger.setId("UUID");
    trigger.setStatus(TriggerStatus.StandBy);
    trigger.setDateRestitution("2021-09-30");
    trigger.setOrigine(TriggerEmitter.Event);
    mongoTemplate.save(trigger, TRIGGER_COLLECTION);

    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(trigger);

    TriggeredBeneficiary triggeredBenef1 = TriggerDataForTesting.getTriggeredBenef1("UUID");
    triggeredBenef1.setOldContract(
        triggeredBenef1.getNewContract()); // pour ne pas passer dans le cas où le benef
    // est radié direct.
    ParametrageCarteTP parametrageCarteTP = getParametrageCarteTP();
    mockPW();

    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(Declaration.class)))
        .thenReturn(List.of(declarationAFermer));

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    manageBenefsContract.setBenefs(List.of(triggeredBenef1));
    manageBenefsContract.setSasCree(false);
    manageBenefsContract.setSasContrat(null);
    manageBenefsContract.setErreurBenef(false);
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        trigger, manageBenefsContract, triggeredBenef1, parametrageCarteTP, null);

    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    Assertions.assertEquals(
        "2021/09/30", declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals(
        "2021/09/30", declaration.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());
  }

  @Test
  void buildDeclarationOuverteWithDateRestitution() {
    Declaration d = getDeclarationAFermer();
    mongoTemplate.save(d);

    Trigger t = new Trigger();
    t.setId("UUID");
    t.setStatus(TriggerStatus.StandBy);
    t.setDateRestitution("2021/09/30");
    t.setDateEffet("2021/02/01");
    mongoTemplate.save(t, TRIGGER_COLLECTION);

    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();

    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(t);

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        t, manageBenefsContract, tb, pc, null);

    Assertions.assertTrue(CollectionUtils.isEmpty(manageBenefsContract.getDeclarations()));
  }

  @Test
  void buildDeclarationOuverteWithDateFinPeriode() {
    Declaration d = getDeclarationAFermer();
    mongoTemplate.save(d);

    Trigger t = new Trigger();
    t.setId("UUID");
    t.setStatus(TriggerStatus.StandBy);
    t.setDateRestitution("2021/09/30");
    t.setDateEffet("2021-02-01");
    mongoTemplate.save(t, TRIGGER_COLLECTION);

    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");

    /** mise à jour periode assuré old : 01/01/2021 mise à jour periode assuré new : 01/03/2021 */
    ServicePrestationTriggerBenef servicePrestationTriggerBenef =
        new ServicePrestationTriggerBenef();
    servicePrestationTriggerBenef.setPeriodesDroitsCarte(new PeriodesDroitsCarte());
    tb.setOldContract(servicePrestationTriggerBenef);
    tb.getOldContract().setDroitsGaranties(tb.getNewContract().getDroitsGaranties());
    Periode periode = new Periode();
    periode.setDebut("2021-01-01");

    Periode periode2 = new Periode();
    periode2.setDebut("2021-01-01");
    periode2.setFin("2021-03-01");

    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();

    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(t);

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        t, manageBenefsContract, tb, pc, null);

    Assertions.assertTrue(CollectionUtils.isEmpty(manageBenefsContract.getDeclarations()));
  }

  @Test
  void buildDeclarationBenefRadiated() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    tb.getNewContract().setDateRadiation("2022-01-01");

    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Declaration declaration = manageBenefsContract.getDeclarations().get(0);
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/01/01");

    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline("2022/01/01");
    triggerTestPeriode.setDateFinOffline("2022/01/01");
    triggerTestPeriode.setMotifEvenement(MotifEvenement.RE.name());
    TriggerDataForTesting.checkPeriode(
        declaration.getDomaineDroits().get(0),
        triggerTestPeriode,
        declaration.getDateRestitution());
  }

  @Test
  void buildDeclarationBenefNotRadiated() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/10/29");
    triggerTestPeriode.setDateDebutFermeture(null);
    triggerTestPeriode.setDateFinFermeture(null);
    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline(null);
    triggerTestPeriode.setDateFinOffline("2022/10/29");
    triggerTestPeriode.setMotifEvenement("RE");

    TriggerDataForTesting.checkPeriode(
        declaration.getDomaineDroits().get(0),
        triggerTestPeriode,
        declaration.getDateRestitution());
  }

  @Test
  void buildDeclarationResiliatedContract() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    tb.getNewContract().setDateResiliation("2022-10-29");

    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    manageBenefsContract.setBenefs(List.of(tb));
    manageBenefsContract.setSasCree(false);
    manageBenefsContract.setSasContrat(null);
    manageBenefsContract.setErreurBenef(false);
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());
    Declaration declarationFermee = new ArrayList<>(manageBenefsContract.getDeclarations()).get(0);
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/10/29");

    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline("2022/10/29");
    triggerTestPeriode.setDateFinOffline("2022/10/29");
    triggerTestPeriode.setMotifEvenement(MotifEvenement.RE.name());

    TriggerDataForTesting.checkPeriode(
        declarationFermee.getDomaineDroits().get(0),
        triggerTestPeriode,
        declarationFermee.getDateRestitution());
  }

  @Test
  void buildDeclarationNewContractSuspensionNoEnd() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    TriggeredBeneficiarySuspensionPeriod suspensionPeriod =
        new TriggeredBeneficiarySuspensionPeriod();
    suspensionPeriod.setDateDebutSuspension("2021-03-01");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPWForBuildDeclarationNewContractSuspension();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/10/29");
    triggerTestPeriode.setDateDebutFermeture(null);
    triggerTestPeriode.setDateFinFermeture(null);
    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline(null);
    triggerTestPeriode.setDateFinOffline("2022/10/29");
    triggerTestPeriode.setMotifEvenement("RE");

    TriggerDataForTesting.checkPeriode(
        declaration.getDomaineDroits().get(0),
        triggerTestPeriode,
        declaration.getDateRestitution());
  }

  @Test
  void buildDeclarationWithCarenceWithWarning() {
    TriggeredBeneficiary triggeredBeneficiary =
        TriggerDataForTesting.getTriggeredBenef1WithSimpleCarence("UUID", "2021-06-30");
    ParametrageCarteTP parametrageCarteTP2 = getParametrageCarteTP2();
    mockPWForBuildDeclarationNewContractCarence(restConnector);

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-01-01"),
        manageBenefsContract,
        triggeredBeneficiary,
        parametrageCarteTP2,
        null);

    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();
    Assertions.assertEquals(10, declaration.getDomaineDroits().size());
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/07/01");
    triggerTestPeriode.setDateFin("2021/12/31");
    triggerTestPeriode.setDateDebutFermeture(null);
    triggerTestPeriode.setDateFinFermeture(null);
    triggerTestPeriode.setDateDebutOnline("2021/07/01");
    triggerTestPeriode.setDateFinOnline(null);
    triggerTestPeriode.setDateFinOffline("2021/12/31");
    triggerTestPeriode.setMotifEvenement("RE");
    DomaineDroit domaineDroit = declaration.getDomaineDroits().get(1);
    Assertions.assertEquals("HOSP", domaineDroit.getCode());
    Assertions.assertEquals("PHAR_GAR2", domaineDroit.getCodeGarantie());
    TriggerDataForTesting.checkPeriode(
        domaineDroit, triggerTestPeriode, declaration.getDateRestitution());

    TriggerTestPeriode triggerTestPeriodeRemplacement = new TriggerTestPeriode();
    triggerTestPeriodeRemplacement.setDateDebut("2021/01/01");
    triggerTestPeriodeRemplacement.setDateFin("2021/03/31");
    triggerTestPeriodeRemplacement.setDateDebutFermeture(null);
    triggerTestPeriodeRemplacement.setDateFinFermeture(null);
    triggerTestPeriodeRemplacement.setDateDebutOnline("2021/01/01");
    triggerTestPeriodeRemplacement.setDateFinOnline("2021/06/30");
    triggerTestPeriodeRemplacement.setDateFinOffline("2021/03/31");
    triggerTestPeriodeRemplacement.setMotifEvenement("RE");
    DomaineDroit domaineDroitRemplacement = declaration.getDomaineDroits().get(0);
    Assertions.assertEquals("HOSP", domaineDroitRemplacement.getCode());
    Assertions.assertEquals("GT_REMP", domaineDroitRemplacement.getCodeGarantie());
    TriggerDataForTesting.checkPeriode(
        domaineDroitRemplacement, triggerTestPeriodeRemplacement, declaration.getDateRestitution());

    TriggerTestPeriode triggerTestPeriodeRemplacement2 = new TriggerTestPeriode();
    triggerTestPeriodeRemplacement2.setDateDebut("2021/01/01");
    triggerTestPeriodeRemplacement2.setDateFin("2021/06/30");
    triggerTestPeriodeRemplacement2.setDateDebutFermeture(null);
    triggerTestPeriodeRemplacement2.setDateFinFermeture(null);
    triggerTestPeriodeRemplacement2.setDateDebutOnline("2021/01/01");
    triggerTestPeriodeRemplacement2.setDateFinOnline("2021/06/30");
    triggerTestPeriodeRemplacement2.setDateFinOffline("2021/06/30");
    triggerTestPeriodeRemplacement2.setMotifEvenement("RE");
    DomaineDroit domaineDroitRemplacement2 = declaration.getDomaineDroits().get(2);
    Assertions.assertEquals("HOSP", domaineDroitRemplacement2.getCode());
    Assertions.assertEquals("GT_REMP", domaineDroitRemplacement2.getCodeGarantie());
    TriggerDataForTesting.checkPeriode(
        domaineDroitRemplacement2,
        triggerTestPeriodeRemplacement2,
        declaration.getDateRestitution());

    TriggerTestPeriode triggerTestPeriode2 = new TriggerTestPeriode();
    triggerTestPeriode2.setDateDebut("2021/01/01");
    triggerTestPeriode2.setDateFin("2021/12/31");
    triggerTestPeriode2.setDateDebutFermeture(null);
    triggerTestPeriode2.setDateFinFermeture(null);
    triggerTestPeriode2.setDateDebutOnline("2021/01/01");
    triggerTestPeriode2.setDateFinOnline(null);
    triggerTestPeriode2.setDateFinOffline("2021/12/31");
    triggerTestPeriode2.setMotifEvenement("RE");
    DomaineDroit domaineDroitNonCarence = declaration.getDomaineDroits().get(7);
    Assertions.assertEquals("RADI", domaineDroitNonCarence.getCode());
    Assertions.assertEquals("PHAR_GAR2", domaineDroitNonCarence.getCodeGarantie());
    TriggerDataForTesting.checkPeriode(
        domaineDroitNonCarence, triggerTestPeriode2, declaration.getDateRestitution());
  }

  private static Trigger createTrigger() {
    return createTrigger("2021-10-15");
  }

  private static Trigger createTrigger(String dateEffet) {
    Trigger trigger = new Trigger();
    trigger.setAmc("AMC1");
    trigger.setNbBenef(1);
    trigger.setNbBenefKO(0);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setStatus(TriggerStatus.ToProcess);
    trigger.setDateEffet(dateEffet);
    return trigger;
  }

  @Test
  void buildDeclarationNewContractSuspensionStartAfterEnd() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    PeriodeSuspension suspensionPeriod = new PeriodeSuspension();
    suspensionPeriod.setPeriode(new Periode("2021-05-31", "2021-03-01"));
    ParametrageCarteTP pc = getParametrageCarteTP();
    tb.getNewContract().getPeriodesSuspension().add(suspensionPeriod);
    mockPWForBuildDeclarationNewContractSuspension();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());

    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/10/29");
    triggerTestPeriode.setDateDebutFermeture(null);
    triggerTestPeriode.setDateFinFermeture(null);
    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline(null);
    triggerTestPeriode.setDateFinOffline("2022/10/29");
    triggerTestPeriode.setMotifEvenement("RE");

    TriggerDataForTesting.checkPeriode(
        manageBenefsContract.getDeclarations().get(0).getDomaineDroits().get(0),
        triggerTestPeriode,
        manageBenefsContract.getDeclarations().get(0).getDateRestitution());
  }

  @Test
  void buildDeclarationNewContractSuspensionEndAfterStart() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    TriggeredBeneficiarySuspensionPeriod suspensionPeriod =
        new TriggeredBeneficiarySuspensionPeriod();
    suspensionPeriod.setDateDebutSuspension("2021-03-01");
    suspensionPeriod.setDateFinSuspension("2021-05-31");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPWForBuildDeclarationNewContractSuspension();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());

    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/10/29");
    triggerTestPeriode.setDateDebutFermeture(null);
    triggerTestPeriode.setDateFinFermeture(null);
    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline(null);
    triggerTestPeriode.setDateFinOffline("2022/10/29");
    triggerTestPeriode.setMotifEvenement("RE");

    TriggerDataForTesting.checkPeriode(
        manageBenefsContract.getDeclarations().get(0).getDomaineDroits().get(0),
        triggerTestPeriode,
        manageBenefsContract.getDeclarations().get(0).getDateRestitution());
  }

  @Test
  void buildDeclarationExistingContractSuspension() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    TriggeredBeneficiarySuspensionPeriod suspensionPeriod =
        new TriggeredBeneficiarySuspensionPeriod();
    suspensionPeriod.setDateDebutSuspension("2021-03-01");
    suspensionPeriod.setDateFinSuspension("2021-05-31");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPW();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Declaration declaration =
        manageBenefsContract.getDeclarations().stream().findFirst().orElseThrow();

    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2021/10/30");
    triggerTestPeriode.setDateFin("2022/10/29");
    triggerTestPeriode.setDateDebutFermeture(null);
    triggerTestPeriode.setDateFinFermeture(null);
    triggerTestPeriode.setDateDebutOnline("2021/10/30");
    triggerTestPeriode.setDateFinOnline(null);
    triggerTestPeriode.setDateFinOffline("2022/10/29");
    triggerTestPeriode.setMotifEvenement("RE");

    TriggerDataForTesting.checkPeriode(
        declaration.getDomaineDroits().get(0),
        triggerTestPeriode,
        declaration.getDateRestitution());
  }

  @Test
  void buildDeclarationFailed() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP pc = getParametrageCarteTP();
    mockPWEmpty();

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger(), manageBenefsContract, tb, pc, null);

    Assertions.assertEquals(0, manageBenefsContract.getDeclarations().size());
  }

  @Test
  void testManageBenefError() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1(null);
    TriggerUtils.manageBenefError(
        true, tb, TriggeredBeneficiaryAnomaly.create(Anomaly.SAS_FOUND_FOR_THIS_CONTRACT), true);
    Assertions.assertEquals(TriggeredBeneficiaryStatusEnum.Error, tb.getStatut());
    TriggeredBeneficiaryAnomaly anomaly =
        TriggeredBeneficiaryAnomaly.create(Anomaly.SAS_FOUND_FOR_THIS_CONTRACT);
    Assertions.assertThrows(
        TriggerException.class, () -> TriggerUtils.manageBenefError(false, tb, anomaly, true));
  }

  private Declaration getDeclarationAFermer() {
    Declaration d = new Declaration();
    d.set_id("id");
    d.setIdDeclarant("1234567890");
    d.setEffetDebut(new Date());
    d.setDateModification(new Date());
    BeneficiaireV2 ben = new BeneficiaireV2();
    ben.setNirBeneficiaire("1234567890123");
    ben.setDateNaissance("19800101");
    ben.setRangNaissance("1");
    ben.setNumeroPersonne("12");
    d.setBeneficiaire(ben);
    DomaineDroit domaineDroit1 = new DomaineDroit();
    domaineDroit1.setCode("PHAR");
    domaineDroit1.setCodeGarantie("PHAR_GAR");
    PeriodeDroit pd = new PeriodeDroit();
    pd.setPeriodeDebut("2021/01/01");
    pd.setPeriodeFin("2031/12/31");
    domaineDroit1.setPeriodeDroit(pd);
    PeriodeDroit pol = new PeriodeDroit();
    pol.setPeriodeDebut("2021/01/01");
    domaineDroit1.setPeriodeOnline(pol);
    PrioriteDroit prio = new PrioriteDroit();
    domaineDroit1.setPrioriteDroit(prio);

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode("PHAR");
    domaineDroit2.setCodeGarantie("PHAR_GAR2");
    PeriodeDroit pd2 = new PeriodeDroit();
    pd2.setPeriodeDebut("2021/01/01");
    pd2.setPeriodeFin("2021/03/31");
    domaineDroit2.setPeriodeDroit(pd2);
    PrioriteDroit prio2 = new PrioriteDroit();
    domaineDroit2.setPrioriteDroit(prio2);

    d.setDomaineDroits(List.of(domaineDroit1, domaineDroit2));
    Contrat contrat = new Contrat();
    contrat.setNumero("C1");
    contrat.setRangAdministratif("1");
    d.setContrat(contrat);
    return d;
  }

  private void mockPWEmpty() {
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any())).thenReturn(null);
  }

  private void mockPW() {
    TriggerDataForTesting.mockPWTpOfflineRights(restConnector);

    mockRestConnectorFetchArray(restConnector);
  }

  private void mockPWForBuildDeclarationNewContractSuspension() {
    TriggerDataForTesting.mockPWTpOfflineRights(restConnector);

    mockRestConnectorFetchArrayForBuildDeclarationNewContractSuspension();
  }

  private void mockPWForBuildDeclarationNewContractCarence(RestConnector restConnector) {
    TriggerDataForTesting.mockPWTpOfflineRights(restConnector);

    mockRestConnectorFetchArrayForBuildDeclarationNewContractCarence();
  }

  private static void mockRestConnectorFetchArray(RestConnector restConnector) {
    JSONObject productWorkshopResponse2 = null;
    JSONObject ocResponse = null;
    try {
      productWorkshopResponse2 = UtilsForTesting.parseJSONFile("src/test/resources/carences.json");
      ocResponse = UtilsForTesting.parseJSONFile("src/test/resources/oc.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(
            productWorkshopResponse2.getJSONArray("carences"), ocResponse.getJSONArray("oc"));
  }

  private void mockRestConnectorFetchArrayForBuildDeclarationNewContractSuspension() {
    JSONObject productWorkshopResponse2;
    JSONObject ocResponse;
    try {
      productWorkshopResponse2 = UtilsForTesting.parseJSONFile("src/test/resources/carences.json");
      ocResponse = UtilsForTesting.parseJSONFile("src/test/resources/oc.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JSONArray retourCarence = productWorkshopResponse2.getJSONArray("carences");
    JSONArray retourOc = ocResponse.getJSONArray("oc");
    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(
            retourCarence,
            retourOc,
            retourOc,
            retourOc,
            retourOc,
            retourCarence,
            retourOc,
            retourOc,
            retourOc,
            retourOc);
  }

  private void mockRestConnectorFetchArrayForBuildDeclarationNewContractCarence() {
    JSONObject productWorkshopResponse2;
    JSONObject ocResponse;
    try {
      productWorkshopResponse2 = UtilsForTesting.parseJSONFile("src/test/resources/carences.json");
      ocResponse = UtilsForTesting.parseJSONFile("src/test/resources/oc.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    JSONArray retourCarence = productWorkshopResponse2.getJSONArray("carences");
    JSONArray retourOc = ocResponse.getJSONArray("oc");
    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(
            retourCarence,
            retourCarence,
            retourOc,
            retourOc,
            retourOc,
            retourOc,
            retourOc,
            retourOc,
            retourOc);
  }

  private ParametrageCarteTP getParametrageCarteTP() {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setAmc("1234567890");
    param.setIdentifiantCollectivite("COLPARAM");
    param.setGroupePopulation("COLLEGE_PARAM");
    param.setCritereSecondaireDetaille("CSD_PARAM");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDateExecutionBatch("2021-01-01");
    // dateDeclenchementAutomatique;
    parametrageRenouvellement.setSeuilSecurite(1000);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Manuel);
    param.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP

    ParametrageDroitsCarteTP parametrageDroitsCarteTP =
        TriggerDataForTesting.getParametrageDroitsCarteTP();

    param.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    return param;
  }

  private ParametrageCarteTP getParametrageCarteTP2() {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setAmc("1234567890");
    param.setIdentifiantCollectivite("COLPARAM");
    param.setGroupePopulation("COLLEGE_PARAM");
    param.setCritereSecondaireDetaille("CSD_PARAM");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(0);
    // dateDeclenchementAutomatique;
    parametrageRenouvellement.setSeuilSecurite(1000);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Automatique);
    param.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP

    ParametrageDroitsCarteTP parametrageDroitsCarteTP =
        TriggerDataForTesting.getParametrageDroitsCarteTP();

    param.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    return param;
  }

  @Test
  void shouldReturnTriggeredBeneficiary() {
    TriggeredBeneficiary benef1 = new TriggeredBeneficiary();
    benef1.setIdTrigger("12");
    benef1.setNom("JUNIT1");
    mongoTemplate.save(benef1, TRIGGERED_BENEFICIARY_COLLECTION);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(List.of(benef1));
    List<TriggeredBeneficiary> benefs = triggerService.getTriggeredBeneficiaries("12");
    Assertions.assertEquals(1, benefs.size());

    TriggeredBeneficiary benef2 = new TriggeredBeneficiary();
    benef2.setIdTrigger("12");
    benef2.setNom("JUNIT2");
    mongoTemplate.save(benef2, TRIGGERED_BENEFICIARY_COLLECTION);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(List.of(benef1, benef2));
    benefs = triggerService.getTriggeredBeneficiaries("12");
    Assertions.assertEquals(2, benefs.size());

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(new ArrayList<>());
    mongoTemplate.findAllAndRemove(new Query(), TriggeredBeneficiary.class);
    benefs = triggerService.getTriggeredBeneficiaries("12");
    Assertions.assertEquals(0, benefs.size());
  }

  @Test
  void shouldReturnTriggeredBeneficiaryWithError() {
    TriggeredBeneficiary benef1 = new TriggeredBeneficiary();
    benef1.setIdTrigger("12");
    benef1.setNom("JUNIT1");
    benef1.setStatut(TriggeredBeneficiaryStatusEnum.Error);
    mongoTemplate.save(benef1, TRIGGERED_BENEFICIARY_COLLECTION);

    TriggeredBeneficiary benef2 = new TriggeredBeneficiary();
    benef2.setIdTrigger("12");
    benef2.setNom("JUNIT2");
    benef2.setStatut(TriggeredBeneficiaryStatusEnum.Processed);
    mongoTemplate.save(benef2, TRIGGERED_BENEFICIARY_COLLECTION);

    TriggeredBeneficiary benef3 = new TriggeredBeneficiary();
    benef3.setIdTrigger("12");
    benef3.setNom("JUNIT3");
    benef3.setStatut(TriggeredBeneficiaryStatusEnum.Error);
    mongoTemplate.save(benef3, TRIGGERED_BENEFICIARY_COLLECTION);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(List.of(benef3, benef1));
    TriggeredBeneficiaryResponse response =
        triggerService.getTriggeredBeneficiariesWithError(10, 1, "12", "ASC");
    List<TriggeredBeneficiary> benefs = response.getTriggeredBeneficiaries();
    Assertions.assertEquals(2, benefs.size());
    Assertions.assertEquals("JUNIT3", benefs.get(0).getNom());

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(TriggeredBeneficiary.class),
                Mockito.anyString()))
        .thenReturn(List.of(benef1, benef3));
    response = triggerService.getTriggeredBeneficiariesWithError(10, 1, "12", "DESC");
    benefs = response.getTriggeredBeneficiaries();
    Assertions.assertEquals(2, benefs.size());
    Assertions.assertEquals("JUNIT1", benefs.get(0).getNom());
  }

  @Test
  void shouldUpdateTriggerStatus() {
    // StandBy => ToProcess
    Trigger triggerToProcess = new Trigger();
    String id = "1234567890";
    triggerToProcess.setId(id);
    triggerToProcess.setStatus(TriggerStatus.StandBy);
    mongoTemplate.save(triggerToProcess, TRIGGER_COLLECTION);
    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(), Mockito.eq(Trigger.class), Mockito.anyString()))
        .thenReturn(triggerToProcess);
    triggerKafkaService.updateStatus(id, TriggerStatus.ToProcess);
    Trigger savedTrigger = mongoTemplate.findById(id, Trigger.class, TRIGGER_COLLECTION);
    Assertions.assertNotNull(savedTrigger);
    Assertions.assertEquals(TriggerStatus.ToProcess, savedTrigger.getStatus());

    // StandBy => Deleted
    Trigger triggerToDelete = new Trigger();
    id = "1112223334";
    triggerToDelete.setId(id);
    triggerToDelete.setStatus(TriggerStatus.StandBy);
    mongoTemplate.save(triggerToDelete, TRIGGER_COLLECTION);
    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(), Mockito.eq(Trigger.class), Mockito.anyString()))
        .thenReturn(triggerToDelete);
    triggerKafkaService.updateStatus(id, TriggerStatus.Deleted);
    savedTrigger = mongoTemplate.findById(id, Trigger.class, TRIGGER_COLLECTION);
    Assertions.assertNotNull(savedTrigger);
    Assertions.assertEquals(TriggerStatus.Deleted, savedTrigger.getStatus());

    // ProcessedWithError => Abandonned (sas mis à jour puis supprimé)
    Trigger triggerToAbandon = new Trigger();
    id = "1112223339";
    String id2 = "111122223333";
    triggerToAbandon.setId(id);
    triggerToAbandon.setStatus(TriggerStatus.ProcessedWithErrors);
    mongoTemplate.save(triggerToAbandon, TRIGGER_COLLECTION);

    SasContrat sas = new SasContrat();
    sas.setIdDeclarant("2523263210");
    sas.setNumeroContrat("JUNIT-001");
    sas.setNumeroAdherent("1212343455656");
    sas.setAnomalies(List.of("Une anomalie JUNIT"));
    sas.setServicePrestationId("SP1");
    List<TriggerBenefs> triggersBenefs = new ArrayList<>();
    TriggerBenefs triggerBenefs = new TriggerBenefs();
    triggerBenefs.setTriggerId(id2);
    BenefInfos benefInfos1 = new BenefInfos();
    benefInfos1.setBenefId("22224444");
    BenefInfos benefInfos2 = new BenefInfos();
    benefInfos2.setBenefId("33334444");
    triggerBenefs.setBenefsInfos(List.of(benefInfos1, benefInfos2));
    triggersBenefs.add(triggerBenefs);
    TriggerBenefs triggerBenefs2 = new TriggerBenefs();
    triggerBenefs2.setTriggerId(id);
    BenefInfos benefInfos3 = new BenefInfos();
    benefInfos3.setBenefId("12341234");
    BenefInfos benefInfos4 = new BenefInfos();
    benefInfos4.setBenefId("43214321");
    triggerBenefs.setBenefsInfos(List.of(benefInfos3, benefInfos4));
    triggersBenefs.add(triggerBenefs2);
    sas.setTriggersBenefs(triggersBenefs);
    sasContratService.save(sas);

    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(), Mockito.eq(Trigger.class), Mockito.anyString()))
        .thenReturn(triggerToAbandon);
    triggerKafkaService.updateStatus(id, TriggerStatus.Abandoning);
    sas.getTriggersBenefs().remove(triggerBenefs2);
    savedTrigger = mongoTemplate.findById(id, Trigger.class, TRIGGER_COLLECTION);
    Assertions.assertNotNull(savedTrigger);
    Assertions.assertEquals(TriggerStatus.Abandonned, savedTrigger.getStatus());

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(sas);
    SasContrat sasRes =
        sasContratService.getByFunctionalKey("2523263210", "JUNIT-001", "1212343455656");
    Assertions.assertEquals(1, sasRes.getTriggersBenefs().size());

    Trigger triggerToAbandon2 = new Trigger();
    triggerToAbandon2.setId(id2);
    triggerToAbandon2.setStatus(TriggerStatus.ProcessedWithErrors);
    mongoTemplate.save(triggerToAbandon2, TRIGGER_COLLECTION);
    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(), Mockito.eq(Trigger.class), Mockito.anyString()))
        .thenReturn(triggerToAbandon2);
    triggerKafkaService.updateStatus(id2, TriggerStatus.Abandoning);
    sas.getTriggersBenefs().remove(triggerBenefs);
    Trigger savedTrigger2 = mongoTemplate.findById(id2, Trigger.class, TRIGGER_COLLECTION);
    Assertions.assertNotNull(savedTrigger2);
    Assertions.assertEquals(TriggerStatus.Abandonned, savedTrigger2.getStatus());

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(SasContrat.class), Mockito.anyString()))
        .thenReturn(null);
    sasRes = sasContratService.getByFunctionalKey("2523263210", "JUNIT-001", "1212343455656");
    Assertions.assertNull(sasRes);

    // Exception Status
    try {
      Trigger triggerException = new Trigger();
      id = "555222555";
      triggerException.setId(id);
      triggerException.setStatus(TriggerStatus.StandBy);
      mongoTemplate.save(triggerException, TRIGGER_COLLECTION);
      Mockito.when(
              mongoTemplate.findById(
                  Mockito.anyString(), Mockito.eq(Trigger.class), Mockito.anyString()))
          .thenReturn(triggerException);
      triggerKafkaService.updateStatus(id, TriggerStatus.ProcessedWithErrors);
    } catch (TriggerException e) {
      Assertions.assertEquals(
          "La mise à jour du statut du déclencheur ne peut pas se faire de StandBy vers ProcessedWithErrors.",
          e.getLocalizedMessage());
    }
    // Exception NotFound
    try {
      triggerKafkaService.updateStatus("99998888", TriggerStatus.Deleted);
    } catch (TriggerNotFoundException e) {
      Assertions.assertEquals(
          "Aucun déclencheur trouvé avec les informations suivantes : id=99998888",
          e.getLocalizedMessage());
    }
  }

  @Test
  void shouldRecycleTrigger() {
    Trigger triggerException = new Trigger();
    String id = "755222557";
    triggerException.setId(id);
    triggerException.setStatus(TriggerStatus.ProcessedWithErrors);
    mongoTemplate.save(triggerException, TRIGGER_COLLECTION);
    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(), Mockito.eq(Trigger.class), Mockito.anyString()))
        .thenReturn(triggerException);
    triggerKafkaService.updateStatus(id, TriggerStatus.ToProcess);
    Trigger savedTrigger = mongoTemplate.findById(id, Trigger.class, TRIGGER_COLLECTION);
    Assertions.assertNotNull(savedTrigger);
    Assertions.assertEquals(TriggerStatus.ToProcess, savedTrigger.getStatus());
  }

  @Test
  void updateBenefs() {
    mongoTemplate.findAllAndRemove(new Query(), TRIGGERED_BENEFICIARY_COLLECTION);
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setId("UUID");
    benef.setPrenom("01");
    benef = mongoTemplate.save(benef, TRIGGERED_BENEFICIARY_COLLECTION);
    benefs.add(benef);
    benef = new TriggeredBeneficiary();
    benef.setPrenom("02");
    benef = mongoTemplate.save(benef, TRIGGERED_BENEFICIARY_COLLECTION);
    benefs.add(benef);

    benefs.get(0).setPrenom("01B");
    benefs.get(1).setPrenom("02B");
    triggerService.updateTriggeredBeneficiaries(benefs);
    Mockito.when(mongoTemplate.findAll(Mockito.eq(TriggeredBeneficiary.class), Mockito.anyString()))
        .thenReturn(benefs);
    benefs = mongoTemplate.findAll(TriggeredBeneficiary.class, TRIGGERED_BENEFICIARY_COLLECTION);

    Assertions.assertEquals("01B", benefs.get(0).getPrenom());
    Assertions.assertEquals("02B", benefs.get(1).getPrenom());

    Mockito.when(
            mongoTemplate.findById(Mockito.anyString(), Mockito.eq(TriggeredBeneficiary.class)))
        .thenReturn(benefs.get(0));
    TriggeredBeneficiary benefGet = triggerService.getTriggeredBenefById(benefs.get(0).getId());
    Assertions.assertEquals("01B", benefGet.getPrenom());
  }

  @Test
  void sasContrat() {
    // A implementer un jour
    Trigger trigger = new Trigger();
    trigger.setAmc("321032103");
    trigger.setId("triggerId");
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setNom("Test");
    benef.setNumeroAdherent("123");
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    benefs.add(benef);

    sasContratService.manageSasContrat(trigger, benefs, "321032103", "12", "34", "123");
    sasContratService.manageSasContrat(trigger, benefs, "321032103", "12", "34", "123");

    Assertions.assertTrue(true);
  }

  @Test
  void shouldManageBenef() {
    ParametrageCarteTP paramCarteTP = getParametrageCarteTP();
    paramCarteTP.setAmc("AMC1");

    ParametrageCarteTPService parametrageCarteTPService =
        Mockito.mock(ParametrageCarteTPService.class);
    Mockito.when(
            parametrageCarteTPService.getParametrageCarteTP(
                Mockito.any(TriggeredBeneficiary.class), Mockito.anyBoolean()))
        .thenReturn(paramCarteTP);
    ReflectionTestUtils.setField(
        triggerBuildDeclarationNewService, "paramCarteTPService", parametrageCarteTPService);

    Trigger trigger = new Trigger();
    trigger.setAmc("AMC1");
    trigger.setNbBenef(1);
    trigger.setNbBenefKO(0);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setStatus(TriggerStatus.ToProcess);
    Trigger savedTrigger = mongoTemplate.save(trigger, Constants.TRIGGER);
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef servicePrestationTriggerBenef =
        new ServicePrestationTriggerBenef();
    servicePrestationTriggerBenef.setDroitsGaranties(new ArrayList<>());
    benef.setNewContract(servicePrestationTriggerBenef);
    benef.setAmc("AMC1");
    benef.setNumeroContrat("CONTRAT1");
    benef.setNumeroAdherent("123");
    benef.setIdTrigger(savedTrigger.getId());
    benefs.add(benef);
    manageBenefsContract.setBenefs(benefs);
    manageBenefsContract.setSasCree(false);
    manageBenefsContract.setSasContrat(null);
    manageBenefsContract.setSasContratRecyclage(null);
    LinkedList<Declaration> declarations = new LinkedList<>();
    manageBenefsContract.setDeclarations(declarations);
    manageBenefsContract.setErreurBenef(false);
    triggerBuildDeclarationNewService.manageBenefs(
        trigger, manageBenefsContract, false, null); // parametrageCarteTP
    // NPE

    manageBenefsContract = new ManageBenefsContract();
    benefs = new ArrayList<>();
    benef = new TriggeredBeneficiary();
    benef.setAmc("AMC1");
    benef.setNumeroContrat("CONTRAT2");
    benefs.add(benef);
    manageBenefsContract.setBenefs(benefs);
    manageBenefsContract.setSasCree(true);
    SasContrat sasContrat = new SasContrat();
    List<String> anomalies = new ArrayList<>();
    anomalies.add("JUNIT");
    sasContrat.setAnomalies(anomalies);
    List<Date> dates = new ArrayList<>();
    dates.add(new Date());
    sasContrat.setDates(dates);
    sasContrat.setIdDeclarant("AMC1");
    sasContrat.setNumeroContrat("CONTRAT2");
    sasContrat.setNumeroAdherent("123");
    List<TriggerBenefs> triggersBenefs = new ArrayList<>();
    TriggerBenefs tb = new TriggerBenefs();
    tb.setTriggerId("3213213");
    List<BenefInfos> benefsInfos = new ArrayList<>();
    BenefInfos benefInfos = new BenefInfos();
    benefInfos.setBenefId("1231");
    benefsInfos.add(benefInfos);
    tb.setBenefsInfos(benefsInfos);
    triggersBenefs.add(tb);
    sasContrat.setTriggersBenefs(triggersBenefs);
    manageBenefsContract.setSasContrat(sasContrat);
    manageBenefsContract.setSasContratRecyclage(null);
    manageBenefsContract.setDeclarations(declarations);
    manageBenefsContract.setErreurBenef(false);

    triggerBuildDeclarationNewService.manageBenefs(trigger, manageBenefsContract, false, null);
    Assertions.assertFalse(manageBenefsContract.isErreurBenef());
  }

  @Test
  void update_status_null_trigger() {
    Trigger triggerToProcess = new Trigger();
    String id = "1234567890";
    triggerToProcess.setId(id);
    triggerToProcess.setStatus(TriggerStatus.StandBy);

    TriggerNotFoundException thrown =
        Assertions.assertThrows(
            TriggerNotFoundException.class,
            () -> triggerKafkaService.updateStatus(id, TriggerStatus.ToProcess),
            "Expected updateStatus() to throw, but it didn't");

    Assertions.assertTrue(
        thrown
            .getMessage()
            .contains("Aucun déclencheur trouvé avec les informations suivantes : id="));
  }

  private void mockBobb() {
    ContractElement contractElement = new ContractElement();
    contractElement.setCodeContractElement("GT_BASE");
    contractElement.setCodeInsurer("BALOO");
    List<ProductElement> productElementList = new ArrayList<>();
    ProductElement productElement = new ProductElement();
    productElement.setCodeProduct("PRODUIT");
    productElement.setCodeAmc("OC");
    productElement.setCodeOffer("Offre1");
    productElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElementList.add(productElement);
    contractElement.setProductElements(productElementList);

    Mockito.when(contractElementService.get("PHAR_GAR", "LOOBA", true)).thenReturn(contractElement);
    Mockito.when(contractElementService.get("PHAR_GAR2", "LOOBA", true))
        .thenReturn(contractElement);
    Mockito.when(contractElementService.get("PHAR_GAR3", "LOOBA", true))
        .thenReturn(contractElement);

    ContractElement contractElement1 = new ContractElement();
    contractElement1.setCodeContractElement("GT_REMP");
    contractElement1.setCodeInsurer("BALOO");
    List<ProductElement> productElementList1 = new ArrayList<>();
    ProductElement productElement1 = new ProductElement();
    productElement1.setCodeProduct("PRODUIT_REMP");
    productElement1.setCodeAmc("OC");
    productElement1.setCodeOffer("OffreReplace");
    productElement1.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    productElementList1.add(productElement1);
    contractElement1.setProductElements(productElementList1);

    Mockito.when(contractElementService.get("GT_REMP", "BALOO", true)).thenReturn(contractElement);
  }

  @Test
  /**
   * Portabilité 1
   *
   * <p>Contrat ouvert
   *
   * <p>On ferme tous leurs garanties
   *
   * <p>On ajoute la même garantie mais à une nouvelle date
   */
  void buildDeclarationTestPeriodeAssurePortabilite1() {
    TriggeredBeneficiary triggeredBenef1 = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP parametrageCarteTP = getParametrageCarteTP();
    mockPW();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"),
        manageBenefsContract,
        triggeredBenef1,
        parametrageCarteTP,
        null);

    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());
    Declaration declaration = manageBenefsContract.getDeclarations().get(0);
    Assertions.assertEquals("V", declaration.getCodeEtat());
    Assertions.assertEquals(
        "2021/03/31", declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFermetureFin());

    triggeredBenef1.setOldContract(
        new ServicePrestationTriggerBenef(triggeredBenef1.getNewContract()));
    triggeredBenef1.getNewContract().getDroitsGaranties().get(1).getPeriode().setFin("2021-08-01");
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"),
        manageBenefsContract,
        triggeredBenef1,
        parametrageCarteTP,
        null);

    Assertions.assertEquals(2, manageBenefsContract.getDeclarations().size());
    Declaration declaration2 = manageBenefsContract.getDeclarations().get(1);
    Assertions.assertEquals("V", declaration2.getCodeEtat());
    Assertions.assertEquals(
        "2021/03/31", declaration2.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration2.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertEquals(
        "2021/03/31", declaration2.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());

    triggeredBenef1.setOldContract(
        new ServicePrestationTriggerBenef(triggeredBenef1.getNewContract()));

    DroitAssure droitAssure2 = new DroitAssure();
    droitAssure2.setCode("PHAR_GAR2");
    droitAssure2.setCodeAssureur("LOOBA");
    Periode periode2 = new Periode();
    periode2.setDebut("2021-09-01");

    droitAssure2.setPeriode(periode2);
    triggeredBenef1.getNewContract().getDroitsGaranties().add(droitAssure2);
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"),
        manageBenefsContract,
        triggeredBenef1,
        parametrageCarteTP,
        null);

    Assertions.assertEquals(3, manageBenefsContract.getDeclarations().size());

    Declaration declaration4 = manageBenefsContract.getDeclarations().get(2);
    Assertions.assertEquals("V", declaration4.getCodeEtat());
    Assertions.assertEquals(
        "2021/03/02", declaration4.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/03/31", declaration4.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration4.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertEquals(
        "2021/03/31", declaration4.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());

    Assertions.assertEquals(
        "2021/03/02", declaration4.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/08/01", declaration4.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration4.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertEquals(
        "2021/08/01", declaration4.getDomaineDroits().get(1).getPeriodeOnline().getPeriodeFin());

    Assertions.assertEquals(
        "2021/09/01", declaration4.getDomaineDroits().get(2).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2022/03/01", declaration4.getDomaineDroits().get(2).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration4.getDomaineDroits().get(2).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertNull(
        declaration4.getDomaineDroits().get(2).getPeriodeOnline().getPeriodeFin());
  }

  @Test
  /**
   * Portabilité 2
   *
   * <p>Contrat ouvert
   *
   * <p>On ferme tous les périodes garanties
   *
   * <p>et on ouvre les garanties
   */
  void buildDeclarationTestPeriodeAssurePortabilite2() {
    TriggeredBeneficiary triggeredBenef1 = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP parametrageCarteTP = getParametrageCarteTP();
    mockPW();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"),
        manageBenefsContract,
        triggeredBenef1,
        parametrageCarteTP,
        null);

    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());
    Declaration declaration = manageBenefsContract.getDeclarations().get(0);
    Assertions.assertEquals("V", declaration.getCodeEtat());
    Assertions.assertEquals(
        "2021/03/31", declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertEquals(
        "2022/03/01", declaration.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFermetureFin());

    triggeredBenef1.setOldContract(
        new ServicePrestationTriggerBenef(triggeredBenef1.getNewContract()));

    triggeredBenef1.getNewContract().getDroitsGaranties().get(1).getPeriode().setFin("2021-08-01");
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"),
        manageBenefsContract,
        triggeredBenef1,
        parametrageCarteTP,
        null);

    Assertions.assertEquals(2, manageBenefsContract.getDeclarations().size());
    Declaration declaration2 = manageBenefsContract.getDeclarations().get(1);
    Assertions.assertEquals("V", declaration2.getCodeEtat());
    Assertions.assertEquals(
        "2021/03/31", declaration2.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration2.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertEquals(
        "2021/03/31", declaration2.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());

    triggeredBenef1.setOldContract(
        new ServicePrestationTriggerBenef(triggeredBenef1.getNewContract()));
    triggeredBenef1.getNewContract().getDroitsGaranties().get(1).getPeriode().setFinToNull();
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        createTrigger("2021-02-15"),
        manageBenefsContract,
        triggeredBenef1,
        parametrageCarteTP,
        null);

    Assertions.assertEquals(3, manageBenefsContract.getDeclarations().size());

    Declaration declaration4 = manageBenefsContract.getDeclarations().get(2);
    Assertions.assertEquals("V", declaration4.getCodeEtat());
    Assertions.assertEquals(
        "2022/03/01", declaration4.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration4.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertNull(
        declaration4.getDomaineDroits().get(1).getPeriodeOnline().getPeriodeFin());
  }

  /** ajout d'une periode de carte */
  @Test
  void buildDeclarationTestPeriodeCarte() {
    TriggeredBeneficiary triggeredBenef1 = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageCarteTP parametrageCarteTP = getParametrageCarteTP();
    mockPW();
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    Trigger trigger = createTrigger("2021-04-01");
    trigger.setOrigine(TriggerEmitter.Event);
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        trigger, manageBenefsContract, triggeredBenef1, parametrageCarteTP, null);

    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());
    Declaration declaration = manageBenefsContract.getDeclarations().get(0);
    declaration.set_id("id");
    Assertions.assertEquals("V", declaration.getCodeEtat());
    Assertions.assertEquals(
        "2022/03/31", declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertNull(
        declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFermetureFin());
    Assertions.assertNull(declaration.getDomaineDroits().get(0).getPeriodeOnline().getPeriodeFin());
    Assertions.assertEquals("HOSP", declaration.getDomaineDroits().get(0).getCode());
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(0).getIsEditable());
    Assertions.assertEquals("DENT", declaration.getDomaineDroits().get(1).getCode());
    Assertions.assertEquals(true, declaration.getDomaineDroits().get(1).getIsEditable());
    Assertions.assertEquals("RADI", declaration.getDomaineDroits().get(2).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(2).getIsEditable());
    Assertions.assertEquals("MEDE", declaration.getDomaineDroits().get(3).getCode());
    Assertions.assertEquals(false, declaration.getDomaineDroits().get(3).getIsEditable());
    triggeredBenef1.setOldContract(
        new ServicePrestationTriggerBenef(triggeredBenef1.getNewContract()));
    Mockito.doReturn(List.of(declaration))
        .when(declarationService)
        .findDeclarationsOfBenef(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.nullable(ClientSession.class));
    PeriodesDroitsCarte periodesDroitsCarte = new PeriodesDroitsCarte();
    periodesDroitsCarte.setDebut("2021-08-01");
    periodesDroitsCarte.setFin("2022-03-01");
    triggeredBenef1.getNewContract().setPeriodesDroitsCarte(periodesDroitsCarte);
    Trigger trigger2 = createTrigger("2021-04-01");
    trigger2.setOrigine(TriggerEmitter.Event);
    triggerBuildDeclarationNewService.buildDeclarationsForBenef(
        trigger2, manageBenefsContract, triggeredBenef1, parametrageCarteTP, null);

    Assertions.assertEquals(2, manageBenefsContract.getDeclarations().size());
    Declaration declaration2 = manageBenefsContract.getDeclarations().get(1);
    Assertions.assertEquals("V", declaration2.getCodeEtat());

    Assertions.assertEquals("HOSP", declaration2.getDomaineDroits().get(0).getCode());
    Assertions.assertEquals(
        "2021/04/01", declaration2.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/07/31", declaration2.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals(false, declaration2.getDomaineDroits().get(0).getIsEditable());

    Assertions.assertEquals("HOSP", declaration2.getDomaineDroits().get(1).getCode());
    Assertions.assertEquals(true, declaration2.getDomaineDroits().get(1).getIsEditable());
    Assertions.assertEquals(
        "2021/08/01", declaration2.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2022/03/01", declaration2.getDomaineDroits().get(1).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("HOSP", declaration2.getDomaineDroits().get(2).getCode());
    Assertions.assertEquals(false, declaration2.getDomaineDroits().get(2).getIsEditable());
    Assertions.assertEquals(
        "2022/03/02", declaration2.getDomaineDroits().get(2).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2022/03/31", declaration2.getDomaineDroits().get(2).getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals("DENT", declaration2.getDomaineDroits().get(3).getCode());
    Assertions.assertEquals(
        "2021/04/01", declaration2.getDomaineDroits().get(3).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2021/07/31", declaration2.getDomaineDroits().get(3).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals(false, declaration2.getDomaineDroits().get(3).getIsEditable());
    Assertions.assertEquals("DENT", declaration2.getDomaineDroits().get(4).getCode());
    Assertions.assertEquals(true, declaration2.getDomaineDroits().get(4).getIsEditable());
    Assertions.assertEquals(
        "2021/08/01", declaration2.getDomaineDroits().get(4).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2022/03/01", declaration2.getDomaineDroits().get(4).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("DENT", declaration2.getDomaineDroits().get(5).getCode());
    Assertions.assertEquals(false, declaration2.getDomaineDroits().get(5).getIsEditable());
    Assertions.assertEquals(
        "2022/03/02", declaration2.getDomaineDroits().get(5).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2022/03/31", declaration2.getDomaineDroits().get(5).getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals("RADI", declaration2.getDomaineDroits().get(6).getCode());
    Assertions.assertEquals(false, declaration2.getDomaineDroits().get(6).getIsEditable());
    Assertions.assertEquals("MEDE", declaration2.getDomaineDroits().get(7).getCode());
    Assertions.assertEquals(false, declaration2.getDomaineDroits().get(7).getIsEditable());
  }
}
