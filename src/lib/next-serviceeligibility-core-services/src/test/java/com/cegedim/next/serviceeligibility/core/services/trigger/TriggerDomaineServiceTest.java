package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.kafka.services.TriggerKafkaService;
import com.cegedim.next.serviceeligibility.core.model.domain.CodeRenvoiTP;
import com.cegedim.next.serviceeligibility.core.model.domain.ConventionTP;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOnlineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.CalculDroitsTPGenerationService;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.*;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerDomaineServiceTest {

  private final Logger logger = LoggerFactory.getLogger(TriggerDomaineServiceTest.class);
  private static final String TRIGGERED_BENEFICIARY_COLLECTION = "triggeredBeneficiary";
  private static final String TRIGGER_COLLECTION = "trigger";

  public static final String CODE_PRODUIT = "PDT_BASE1";
  public static final String CODE_OFFRE = "OFFRE1";
  public static final String CODE_OC = "OC";
  public static final String CONVENTIONNEMENT = "conventionnement";
  public static final String DATE_DEBUT = "2022-01-01";
  public static final String DATE_FIN = "2022-12-31";

  @Autowired MongoTemplate mongoTemplate;

  @Autowired private TriggerService triggerService;

  @Autowired private TriggerKafkaService triggerKafkaService;

  @Autowired private SasContratService sasContratService;

  @Autowired private TriggerDao triggerDao;

  @Autowired private TriggerDomaineServiceImpl triggerDomaineService;

  @Autowired private ParametreBddService parametreBddService;

  @MockBean CalculDroitsTPGenerationService calculDroitsTPGenerationService;

  @BeforeEach
  public void before() {
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
  void testManageBenefError() {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1(null);
    TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
        TriggeredBeneficiaryAnomaly.create(Anomaly.SAS_FOUND_FOR_THIS_CONTRACT);
    TriggerUtils.manageBenefError(true, tb, triggeredBeneficiaryAnomaly, true);
    Assertions.assertEquals(TriggeredBeneficiaryStatusEnum.Error, tb.getStatut());
    Assertions.assertThrows(
        TriggerException.class,
        () -> TriggerUtils.manageBenefError(false, tb, triggeredBeneficiaryAnomaly, true));
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
    benef1.setDerniereAnomalie(
        TriggeredBeneficiaryAnomaly.create(Anomaly.SAS_FOUND_FOR_THIS_CONTRACT));
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
    benef3.setDerniereAnomalie(
        TriggeredBeneficiaryAnomaly.create(Anomaly.SAS_FOUND_FOR_THIS_CONTRACT));
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
  void shouldGenerateConventionsSameOrder() throws TriggerParametersException {
    String codeConvBrut = "IS/IT/OP";
    mockParamBddServiceToWork();
    List<Conventionnement> conventionnements =
        triggerDomaineService.getListConventionnementsFromCode(codeConvBrut);
    Assertions.assertFalse(conventionnements.isEmpty());
    assertValidConventions(codeConvBrut, conventionnements);
  }

  @Test
  void shouldThrow() {
    String code = "NIMP";
    Mockito.doReturn(null)
        .when(parametreBddService)
        .findOneByType(Mockito.anyString(), Mockito.anyString());
    Assertions.assertThrows(
        TriggerParametersException.class,
        () -> triggerDomaineService.getListConventionnementsFromCode(code));
  }

  /**
   * Si la convention TP est saisie sur le domaine TP dans le paramétrage de génération de droits,
   * alors on prend cette convention sans tenir compte du réseau de soin de l’Atelier Produit ni le
   * paramétrage AMC
   */
  @Test
  void conventionDroitCompleted() throws TriggerParametersException {
    String codeConvDetailDroit = "IS/IT";
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setConvention(codeConvDetailDroit);
    mockParamBddServiceToWork();
    DomaineDroit domaineDroit = new DomaineDroit();
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    triggerDomaineService.populateConventions(
        detailDroit, domaineDroit, parametrageCarteTP, null, null, null);

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit, domaineDroit.getConventionnements());
  }

  @Test
  void conventionDroitCompletedDifferentConventions() throws TriggerParametersException {
    String codeConvDetailDroit1 = "VM";
    String codeConvDetailDroit2 = "CB/SP";
    String codeConvDetailDroit3 = "KA/IS";
    String codeDomaineDroit1 = "LPPS";
    String codeDomaineDroit2 = "AUDI";
    String codeDomaineDroit3 = "DENT";
    String codePw = "IT";
    mockParamBddServiceToWork();

    // Param Produit AUDI LPPS DENT OPTI => IT
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork(codePw);
    tpOnlineRightsDetails.setNature("AUDI LPPS DENT OPTI");

    // Param AMC
    // Réseau : IT - Domaine : AUDI - Convention cible : CB/SP - Non concaténée
    // Réseau : IT - Tout domaine - Convention cible : KA - Concaténée
    ConventionTP conventionTP1 = new ConventionTP();
    conventionTP1.setDomaineTP(codeDomaineDroit2);
    conventionTP1.setReseauSoin(codePw);
    conventionTP1.setConventionCible(codeConvDetailDroit2);
    conventionTP1.setDateDebut(LocalDateTime.now());
    conventionTP1.setConcatenation(false);

    ConventionTP conventionTP2 = new ConventionTP();
    conventionTP2.setReseauSoin(codePw);
    conventionTP2.setConventionCible("KA");
    conventionTP2.setDateDebut(LocalDateTime.now());
    conventionTP2.setConcatenation(true);

    // Param Carte TP : Convention d’entête IS / Domaine LPPS pour la convention VM
    // / Domaines AUDI et DENT sans saisir de convention
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("IS");
    List<DetailDroit> paramDetailDroits = new ArrayList<>();
    DetailDroit detailDroit1 = new DetailDroit();
    detailDroit1.setCodeDomaineTP(codeDomaineDroit1);
    detailDroit1.setConvention(codeConvDetailDroit1);
    paramDetailDroits.add(detailDroit1);
    DetailDroit detailDroit2 = new DetailDroit();
    detailDroit2.setCodeDomaineTP(codeDomaineDroit2);
    paramDetailDroits.add(detailDroit2);
    DetailDroit detailDroit3 = new DetailDroit();
    detailDroit3.setCodeDomaineTP(codeDomaineDroit3);
    paramDetailDroits.add(detailDroit3);
    parametrageDroitsCarteTP.setDetailsDroit(paramDetailDroits);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);

    // LPPS en VM
    DomaineDroit domaineDroit1 = new DomaineDroit();
    domaineDroit1.setCode(codeDomaineDroit1);

    triggerDomaineService.populateConventions(
        detailDroit1,
        domaineDroit1,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        null);

    Assertions.assertFalse(domaineDroit1.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit1, domaineDroit1.getConventionnements());

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode(codeDomaineDroit2);

    // AUDI en CB/SP
    triggerDomaineService.populateConventions(
        detailDroit2,
        domaineDroit2,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit2.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit2, domaineDroit2.getConventionnements());

    DomaineDroit domaineDroit3 = new DomaineDroit();
    domaineDroit3.setCode(codeDomaineDroit3);

    // DENT et OPTI en KA/IS
    triggerDomaineService.populateConventions(
        detailDroit3,
        domaineDroit3,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit3.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit3, domaineDroit3.getConventionnements());

    String codeDomaineDroit4 = "OPTI";
    DomaineDroit domaineDroit4 = new DomaineDroit();
    domaineDroit4.setCode(codeDomaineDroit4);

    triggerDomaineService.populateConventions(
        detailDroit3,
        domaineDroit4,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit4.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit3, domaineDroit4.getConventionnements());
  }

  @Test
  void conventionDroitCompletedOtherConventions() throws TriggerParametersException {
    String codeConvDetailDroit1 = "IS";
    String codeConvDetailDroit2 = "AL";
    String codeConvDetailDroit3 = "AL/IS";
    String codeDomaineDroit1 = "AUDI";
    String codeDomaineDroit2 = "DENT";
    String codeDomaineDroit3 = "OPTI";
    String codePw = "IT";
    mockParamBddServiceToWork();

    // Param Produit AUDI LPPS DENT OPTI => IT
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork(codePw);
    tpOnlineRightsDetails.setNature("AUDI LPPS DENT OPTI");

    // Param AMC
    // Réseau : IT - Domaine : AUDI - Convention cible : IS - Concaténée
    // Réseau : IT - Tout domaine - Convention cible : AL - Concaténée
    ConventionTP conventionTP1 = new ConventionTP();
    conventionTP1.setDomaineTP(codeDomaineDroit1);
    conventionTP1.setReseauSoin(codePw);
    conventionTP1.setConventionCible(codeConvDetailDroit1);
    conventionTP1.setDateDebut(LocalDateTime.now());
    conventionTP1.setConcatenation(true);

    ConventionTP conventionTP2 = new ConventionTP();
    conventionTP2.setReseauSoin(codePw);
    conventionTP2.setConventionCible(codeConvDetailDroit2);
    conventionTP2.setDateDebut(LocalDateTime.now());
    conventionTP2.setConcatenation(true);

    // Param Carte TP : Convention d’entête IS / Domaine LPPS pour la convention VM
    // / Domaines AUDI et DENT sans saisir de convention
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("IS");
    List<DetailDroit> paramDetailDroits = new ArrayList<>();
    DetailDroit detailDroit1 = new DetailDroit();
    detailDroit1.setCodeDomaineTP(codeDomaineDroit1);
    paramDetailDroits.add(detailDroit1);
    DetailDroit detailDroit2 = new DetailDroit();
    detailDroit2.setCodeDomaineTP(codeDomaineDroit2);
    paramDetailDroits.add(detailDroit2);
    parametrageDroitsCarteTP.setDetailsDroit(paramDetailDroits);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);

    DomaineDroit domaineDroit1 = new DomaineDroit();
    domaineDroit1.setCode(codeDomaineDroit1);

    // AUDI en IS (et pas IS/IS malgré la concaténation)
    triggerDomaineService.populateConventions(
        detailDroit1,
        domaineDroit1,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit1.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit1, domaineDroit1.getConventionnements());

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode(codeDomaineDroit2);

    // DENT et OPTI en IS (et pas IS/IS malgré la concaténation)
    triggerDomaineService.populateConventions(
        detailDroit2,
        domaineDroit2,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit2.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit3, domaineDroit2.getConventionnements());

    DomaineDroit domaineDroit3 = new DomaineDroit();
    domaineDroit3.setCode(codeDomaineDroit3);

    triggerDomaineService.populateConventions(
        detailDroit2,
        domaineDroit3,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP1, conventionTP2),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit3.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit3, domaineDroit3.getConventionnements());
  }

  @Test
  void conventionDroitCompletedWithNoSpecificParameters() throws TriggerParametersException {
    String codeDomaineDroit1 = "LPPS";
    String codeConvDetailDroit = "IS";
    mockParamBddServiceToWork();

    // Param Carte TP : Convention d’entête IS / Domaine LPPS pour la convention VM
    // / Domaines AUDI et DENT sans saisir de convention
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvDetailDroit);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);

    DetailDroit detailDroit1 = new DetailDroit();
    detailDroit1.setCodeDomaineTP(codeDomaineDroit1);

    DomaineDroit domaineDroit1 = new DomaineDroit();
    domaineDroit1.setCode(codeDomaineDroit1);

    // LPPS en IS
    triggerDomaineService.populateConventions(
        detailDroit1,
        domaineDroit1,
        parametrageCarteTP,
        new TpOnlineRightsDetails(),
        List.of(new ConventionTP()),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit1.getConventionnements().isEmpty());
    assertValidConventions(codeConvDetailDroit, domaineDroit1.getConventionnements());
  }

  @Test
  void conventionDroitCompletedWithClosedPeriod() throws TriggerParametersException {
    String codeConvDetailDroit = "KA/IS";
    String codeDomaineDroit = "DENT";
    String codePw = "IT";
    mockParamBddServiceToWork();

    // Param Produit AUDI LPPS DENT OPTI => IT
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork(codePw);
    tpOnlineRightsDetails.setNature("AUDI LPPS DENT OPTI");

    // Param AMC
    // Réseau : IT - Domaine : AUDI - Convention cible : CB/SP - Non concaténée
    // Réseau : IT - Tout domaine - Convention cible : KA - Concaténée
    ConventionTP conventionTP = new ConventionTP();
    conventionTP.setReseauSoin(codePw);
    conventionTP.setConventionCible(codeConvDetailDroit);
    conventionTP.setDateDebut(LocalDateTime.now().minusDays(1));
    conventionTP.setDateFin(LocalDateTime.now().minusDays(1));
    conventionTP.setConcatenation(false);

    // Param Carte TP : Convention d’entête IS / Domaine LPPS pour la convention VM
    // / Domaines AUDI et DENT sans saisir de convention
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("IS");
    List<DetailDroit> paramDetailDroits = new ArrayList<>();
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeDomaineDroit);
    paramDetailDroits.add(detailDroit);
    parametrageDroitsCarteTP.setDetailsDroit(paramDetailDroits);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);

    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode(codeDomaineDroit);

    // DENT et OPTI en KA/IS
    triggerDomaineService.populateConventions(
        detailDroit,
        domaineDroit,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions(codePw, domaineDroit.getConventionnements());
  }

  /** IS si aucun réseau dans AP et IS dans le paramétrage de génération de droits TP */
  @Test
  void noDroitNoAMCShouldTakeParamTPConv() throws TriggerParametersException {
    String codeConvTp = "IS";
    mockParamBddServiceToWork();
    DomaineDroit domaineDroit = new DomaineDroit();
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvTp);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);

    triggerDomaineService.populateConventions(
        null, domaineDroit, parametrageCarteTP, null, null, null);
    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions(codeConvTp, domaineDroit.getConventionnements());
  }

  /** IT/IS si IT dans AMC et IS dans le paramétrage de génération de droits TP */
  @Test
  void emptyDroitConvShouldTakeAMCConvAndParamTpConv() throws TriggerParametersException {
    String codeConvTp = "IS";
    String codePw = "IT";
    mockParamBddServiceToWork();
    DetailDroit detailDroit = new DetailDroit();
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("OP");
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvTp);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    ConventionTP conventionTP = new ConventionTP();
    conventionTP.setDomaineTP(domaineDroit.getCode());
    conventionTP.setReseauSoin(codePw);
    conventionTP.setConventionCible(codePw);
    conventionTP.setDateDebut(LocalDateTime.now());
    conventionTP.setConcatenation(true);
    triggerDomaineService.populateConventions(
        detailDroit,
        domaineDroit,
        parametrageCarteTP,
        null,
        List.of(conventionTP),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions(codePw + "/" + codeConvTp, domaineDroit.getConventionnements());
  }

  /** IS si vide */
  @Test
  void emptyDroitConvShouldTakeAMCConv() throws TriggerParametersException {
    String codeConvTp = "IS";
    String codePw = "IS";
    mockParamBddServiceToWork();
    DetailDroit detailDroit = new DetailDroit();
    DomaineDroit domaineDroit = new DomaineDroit();
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvTp);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    triggerDomaineService.populateConventions(
        detailDroit, domaineDroit, parametrageCarteTP, null, null, null);

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions(codePw, domaineDroit.getConventionnements());
  }

  /**
   * Si le paramétrage de génération de droits TP a la convention IS, et que l’atelier produit a le
   * réseau de soin OP, et la gestion AMC pour le domaineTP et le reseau OP pointe vers IT/IS on
   * devra générer les droits TP avec la convention IT/IS et non IS/IT/IS
   */
  @Test
  void emptyDroitConvShouldTakeAMCfromReseauConvSameWay() throws TriggerParametersException {
    String codeConvTp = "IS";
    String codePw = "OP";
    String codeCible = "IT/IS";
    mockParamBddServiceToWork();
    DetailDroit detailDroit = new DetailDroit();
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("pp");
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvTp);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork(codePw);
    ConventionTP conventionTP = new ConventionTP();
    conventionTP.setDomaineTP(domaineDroit.getCode());
    conventionTP.setReseauSoin(codePw);
    conventionTP.setConventionCible(codeCible);
    conventionTP.setDateDebut(LocalDateTime.now());
    conventionTP.setConcatenation(true);
    triggerDomaineService.populateConventions(
        detailDroit,
        domaineDroit,
        parametrageCarteTP,
        tpOnlineRightsDetails,
        List.of(conventionTP),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions(codeCible, domaineDroit.getConventionnements());
  }

  /**
   * Si le paramétrage de génération de droits TP a la convention IS/IT, et que l’atelier produit a
   * le réseau de soin IT, on devra générer les droits TP avec la convention IT/IS et non IT/IS/IT
   */
  @Test
  void emptyDroitConvShouldTakeAMCConvInvertedWay() throws TriggerParametersException {
    String codeConvTp = "IS/IT";
    String codeCible = "IT";
    mockParamBddServiceToWork();
    DetailDroit detailDroit = new DetailDroit();
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("OP");
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvTp);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    ConventionTP conventionTP = new ConventionTP();
    conventionTP.setDomaineTP(domaineDroit.getCode());
    conventionTP.setConventionCible(codeCible);
    conventionTP.setDateDebut(LocalDateTime.now());
    conventionTP.setConcatenation(true);
    triggerDomaineService.populateConventions(
        detailDroit,
        domaineDroit,
        parametrageCarteTP,
        null,
        List.of(conventionTP),
        LocalDate.now());

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions("IT/IS", domaineDroit.getConventionnements());
  }

  @Test
  void noDroitShouldTakeAMCConv() throws TriggerParametersException {
    String codeConvTp = "IS";
    String codeCible = "IT";
    mockParamBddServiceToWork();
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("OP");
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP(codeConvTp);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    ConventionTP conventionTP = new ConventionTP();
    conventionTP.setDomaineTP(domaineDroit.getCode());
    conventionTP.setConventionCible(codeCible);
    conventionTP.setDateDebut(LocalDateTime.now());
    conventionTP.setConcatenation(true);
    triggerDomaineService.populateConventions(
        null, domaineDroit, parametrageCarteTP, null, List.of(conventionTP), LocalDate.now());

    Assertions.assertFalse(domaineDroit.getConventionnements().isEmpty());
    assertValidConventions("IT/IS", domaineDroit.getConventionnements());
  }

  private void mockParamBddServiceToWork() {
    Mockito.doAnswer(
            ans -> {
              String code = ans.getArgument(0);
              ParametresDto parametresDto = new ParametresDto();
              parametresDto.setCode(code);
              parametresDto.setLibelle(code + "_lib");
              return parametresDto;
            })
        .when(parametreBddService)
        .findOneByType(Mockito.anyString(), Mockito.anyString());
  }

  private void assertValidConventions(
      String codeConvBrut, List<Conventionnement> conventionnements) {
    String[] codesBrut = codeConvBrut.split("/");
    for (int i = 0; i < codesBrut.length; i++) {
      String expectedCode = codesBrut[i];
      Conventionnement conv = conventionnements.get(i);
      Assertions.assertEquals(expectedCode, conv.getTypeConventionnement().getCode());
    }
  }

  /**
   * Si le paramétrage de génération des droits TP indique d’inhiber le code renvoie, il faudra
   * donc, dans ce cas, ne pas positionner de code de renvoi pour le domaine TP
   */
  @Test
  void shouldReturnNullInhiber() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.INHIBER);
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, null, debutDroit);
    Assertions.assertNull(domaineDroit.getCodeRenvoi());
  }

  /**
   * Si le paramétrage de génération des droits TP indique de garder le paramétrage, il faudra donc,
   * si l’atelier produit retourne un réseau de soin, positionner le code de renvoi issue du
   * paramétrage effectué dans la gestion des AMCs ayant ce même réseau de soin mais si on ne trouve
   * pas de paramétrage pour le réseau de soin donné par PW, on cherche s'il existe un paramétrage
   * sans réseau de soin et on positionne de codeRenvoi
   */
  @Test
  void shouldNotReturnAmcCodeRenvoiGarder() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.GARDER);
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, null, debutDroit);
    Assertions.assertNull(domaineDroit.getCodeRenvoi());
  }

  @Test
  void shouldReturnAmcCodeRenvoiGarder() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST_SANS_RESEAU");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin(null);
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    CodeRenvoiTP codeRenvoiTP2 = new CodeRenvoiTP();
    codeRenvoiTP2.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP2.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP2.setReseauSoin("RZO_TEST");
    codeRenvoiTP2.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.GARDER);
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP, codeRenvoiTP2), detailDroit, null, debutDroit);
    Assertions.assertNull(domaineDroit.getCodeRenvoi());

    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork("TEST");
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit,
        List.of(codeRenvoiTP, codeRenvoiTP2),
        detailDroit,
        tpOnlineRightsDetails,
        debutDroit);
    Assertions.assertEquals(domaineDroit.getCodeRenvoi(), codeRenvoiTP.getCodeRenvoi());
  }

  @Test
  void testCodeRenvoiGarderWithCareNetwork() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.GARDER);
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork("TEST");
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, tpOnlineRightsDetails, debutDroit);
    // Réseau de soin de PW différent du réseau de soin du code renvoi => pas de
    // code renvoi gardé
    Assertions.assertNull(domaineDroit.getCodeRenvoi());

    tpOnlineRightsDetails.setNetwork("RZO_TEST");
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, tpOnlineRightsDetails, debutDroit);
    // Réseau de soin de PW identique au réseau de soin du code renvoi => on garde
    // le code renvoi du param amc
    Assertions.assertEquals(codeRenvoiTP.getCodeRenvoi(), domaineDroit.getCodeRenvoi());
  }

  @Test
  void testCodeRenvoiGarderWithCareNetworkWithInvalidDate() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    codeRenvoiTP.setDateFin(LocalDate.of(1999, 12, 31).atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.GARDER);
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork("RZO_TEST");
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, tpOnlineRightsDetails, debutDroit);
    // Le code renvoi est sur une periode invalide (01/01/2000 au 31/12/1999) => pas
    // de code renvoi
    Assertions.assertNull(domaineDroit.getCodeRenvoi());
  }

  @Test
  void testCodeRenvoiGarderWithCareNetworkAndDates() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(LocalDate.of(2026, 12, 31).atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.GARDER);
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork("RZO_TEST");
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, tpOnlineRightsDetails, debutDroit);
    // Le code renvoi est sur une periode du futur (31/12/2026) => pas de code
    // renvoi
    Assertions.assertNull(domaineDroit.getCodeRenvoi());
  }

  @Test
  void testCodeRenvoiGarderWithCareNetworkAndDates2() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(LocalDate.of(2026, 12, 31).atStartOfDay());
    CodeRenvoiTP codeRenvoiTP2 = new CodeRenvoiTP();
    codeRenvoiTP2.setCodeRenvoi("CODE_TEST_2");
    codeRenvoiTP2.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP2.setReseauSoin("RZO_TEST");
    codeRenvoiTP2.setDateDebut(LocalDate.of(1999, 12, 31).atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.GARDER);
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork("RZO_TEST");
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit,
        List.of(codeRenvoiTP, codeRenvoiTP2),
        detailDroit,
        tpOnlineRightsDetails,
        debutDroit);
    Assertions.assertEquals(codeRenvoiTP2.getCodeRenvoi(), domaineDroit.getCodeRenvoi());
  }

  /**
   * Si le paramétrage de génération des droits TP indique de remplacer le code renvoie, il faudra
   * donc, dans ce cas, positionner le code de renvoi indiqué dans le paramétrage de génération des
   * droits TP
   */
  @Test
  void shouldReturnDomaineCodeRRemplacer() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.REMPLACER);
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, null, debutDroit);
    Assertions.assertEquals(domaineDroit.getCodeRenvoi(), detailDroit.getCodeRenvoi());
  }

  /**
   * Si le paramétrage de génération des droits TP indique de compléter le code renvoi, il faudra
   * donc, si l’atelier produit retourne un réseau de soin, positionner le code de renvoi déterminé
   * via le paramétrage effectué dans la gestion des AMC ou si on ne reçoit pas de réseau de soin de
   * la part de PW, on positionne le code renvoi pour lequel on n'a pas défini de réseau de soin
   * dans le paramétrage de l'AMC avec la possibilité de le concaténer avec le code renvoi indiqué
   * dans le paramétrage de génération des droits TP.
   */
  @Test
  void shouldReturnDomaineAndAmcCodeRCompleter() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.COMPLETER);
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, null, debutDroit);
    Assertions.assertEquals(domaineDroit.getCodeRenvoi(), detailDroit.getCodeRenvoi());
    Assertions.assertNull(domaineDroit.getCodeRenvoiAdditionnel());
  }

  @Test
  void shouldReturnDomaineAndAmcCodeRCompleterWithACareNetwork() {
    LocalDate debutDroit = LocalDate.of(2000, 1, 1);
    CodeRenvoiTP codeRenvoiTP = new CodeRenvoiTP();
    codeRenvoiTP.setCodeRenvoi("CODE_TEST");
    codeRenvoiTP.setDomaineTP("DOMAINE_TEST");
    codeRenvoiTP.setReseauSoin("RZO_TEST");
    codeRenvoiTP.setDateDebut(debutDroit.atStartOfDay());
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP(codeRenvoiTP.getDomaineTP());
    detailDroit.setCodeRenvoi("CODE_DETAIL");
    detailDroit.setCodeRenvoiAction(CodeRenvoiAction.COMPLETER);
    TpOnlineRightsDetails tpOnlineRightsDetails = new TpOnlineRightsDetails();
    tpOnlineRightsDetails.setNetwork("TEST");
    DomaineDroit domaineDroit = new DomaineDroit();
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, tpOnlineRightsDetails, debutDroit);
    Assertions.assertEquals(detailDroit.getCodeRenvoi(), domaineDroit.getCodeRenvoi());
    Assertions.assertNull(domaineDroit.getCodeRenvoiAdditionnel());

    tpOnlineRightsDetails.setNetwork("RZO_TEST");
    triggerDomaineService.populateCodeRenvoi(
        domaineDroit, List.of(codeRenvoiTP), detailDroit, tpOnlineRightsDetails, debutDroit);
    Assertions.assertEquals(codeRenvoiTP.getCodeRenvoi(), domaineDroit.getCodeRenvoi());
    Assertions.assertEquals(detailDroit.getCodeRenvoi(), domaineDroit.getCodeRenvoiAdditionnel());
  }

  @Test
  void shouldGenerateDomainsTest()
      throws CarenceException,
          PwException,
          BobbNotFoundException,
          TriggerWarningException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    ParametrageCarteTP parametrageCarteTp = TriggerDataForTesting.getParametrageCarteTP();
    Trigger trigger = new Trigger();
    trigger.setId("1234567");
    trigger.setDateEffet("2022-05-05");
    TriggeredBeneficiary triggeredBeneficiary =
        TriggerDataForTesting.getTriggeredBeneficiary(trigger, true);

    Mockito.when(
            calculDroitsTPGenerationService.callBobbIncludingIgnored(
                Mockito.any(DroitAssure.class), Mockito.any(Periode.class)))
        .thenReturn(getParametrageBobb());

    List<ParametrageAtelierProduit> parametrageAtelierProduits = new ArrayList<>();
    ParametrageAtelierProduit parametrageAtelierProduit = new ParametrageAtelierProduit();
    PAPNatureTags papNatureTags = new PAPNatureTags();
    papNatureTags.setNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    parametrageAtelierProduit.setNaturesTags(List.of(papNatureTags));
    parametrageAtelierProduits.add(parametrageAtelierProduit);
    Mockito.when(
            calculDroitsTPGenerationService.callPW(
                Mockito.any(DroitAssure.class), Mockito.any(ParametrageBobbProductElement.class)))
        .thenReturn(parametrageAtelierProduits);
    ParametresDto parametresDto = new ParametresDto();
    parametresDto.setLibelle(CONVENTIONNEMENT);
    Mockito.doReturn(parametresDto).when(parametreBddService).findOneByType(CONVENTIONNEMENT, "IS");

    List<DroitsTPExtended> extendedList = new ArrayList<>();
    DroitsTPExtended droitsTPExtended = new DroitsTPExtended();
    droitsTPExtended.setCodeOc(CODE_OC);
    droitsTPExtended.setCodeProduit(CODE_PRODUIT);
    droitsTPExtended.setCodeOffre(CODE_OFFRE);
    droitsTPExtended.setCodeDomaine("AMI");
    droitsTPExtended.setPeriodeProductElement(new Periode(DATE_DEBUT, DATE_FIN));
    droitsTPExtended.setDateDebut(DATE_DEBUT);
    droitsTPExtended.setDateFin(DATE_FIN);
    TpOfflineRightsDetails detailsOffline = new TpOfflineRightsDetails();
    detailsOffline.setNature(Constants.NATURE_PRESTATION_VIDE_BOBB);
    droitsTPExtended.setDetailsOffline(detailsOffline);
    extendedList.add(droitsTPExtended);
    Mockito.when(
            calculDroitsTPGenerationService.calculDroitsTP(
                Mockito.any(DroitAssure.class),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString()))
        .thenReturn(extendedList);
    GenerationDomaineResult generationDomaine =
        triggerDomaineService.generationDomaine(
            triggeredBeneficiary,
            parametrageCarteTp,
            trigger,
            "2022/06/30",
            false,
            new ArrayList<>());
    List<DomaineDroit> domaineDroitList = generationDomaine.getDomaineDroitList();
    Assertions.assertNotNull(domaineDroitList);
    Assertions.assertNotEquals(0, domaineDroitList.size());
    Assertions.assertEquals(CODE_OFFRE, domaineDroitList.get(0).getCodeOffre());
    Assertions.assertEquals(CODE_OC, domaineDroitList.get(0).getCodeOc());
    Assertions.assertEquals(CODE_PRODUIT, domaineDroitList.get(0).getCodeProduit());
    Assertions.assertEquals(
        "2022/01/01", domaineDroitList.get(0).getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        "2022/12/31", domaineDroitList.get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals(
        DATE_DEBUT, domaineDroitList.get(0).getPeriodeProductElement().getDebut());
    Assertions.assertEquals(DATE_FIN, domaineDroitList.get(0).getPeriodeProductElement().getFin());
    Assertions.assertEquals(
        CONVENTIONNEMENT,
        domaineDroitList
            .get(0)
            .getConventionnements()
            .get(0)
            .getTypeConventionnement()
            .getLibelle());
  }

  private static ParametrageBobb getParametrageBobb() {
    ParametrageBobb parametrageBobb = new ParametrageBobb();
    parametrageBobb.setCodeAssureur("BALOO");
    parametrageBobb.setCodeGarantie("GT");
    ParametrageBobbProductElement parametrageBobbProductElement =
        new ParametrageBobbProductElement();
    parametrageBobbProductElement.setCodeOc(CODE_OC);
    parametrageBobbProductElement.setCodeProduit(CODE_PRODUIT);
    parametrageBobbProductElement.setCodeOffre(CODE_OFFRE);
    ParametrageBobbNaturePrestation parametrageBobbNaturePrestation =
        new ParametrageBobbNaturePrestation();
    parametrageBobbNaturePrestation.setNaturePrestation(Constants.NATURE_PRESTATION_VIDE_BOBB);
    parametrageBobbNaturePrestation.setDateDebut("2020-01-01");
    parametrageBobbNaturePrestation.setDateFin("2050-01-01");
    parametrageBobbProductElement.setNaturePrestation(List.of(parametrageBobbNaturePrestation));
    parametrageBobb.setParametrageBobbProductElements(List.of(parametrageBobbProductElement));
    return parametrageBobb;
  }

  @Test
  void testGetDomainesTP() {
    LocalDate dateDebutTP = LocalDate.now();

    List<ConventionTP> conventionTPList = new ArrayList<>();
    ConventionTP conventionTP1 = new ConventionTP();
    conventionTP1.setDomaineTP("IS");
    conventionTP1.setReseauSoin("R");
    conventionTP1.setConventionCible("A");
    conventionTPList.add(conventionTP1);

    ConventionTP conventionTP2 = new ConventionTP();
    conventionTP2.setDomaineTP(null);
    conventionTP2.setReseauSoin("R");
    conventionTP2.setDateDebut(dateDebutTP.plusDays(1).atStartOfDay());
    conventionTP2.setConventionCible("B");
    conventionTPList.add(conventionTP2);

    ConventionTP conventionTP3 = new ConventionTP();
    conventionTP3.setDomaineTP(null);
    conventionTP3.setReseauSoin("R");
    conventionTP3.setDateFin(dateDebutTP.minusDays(1).atStartOfDay());
    conventionTP3.setConventionCible("C");
    conventionTPList.add(conventionTP3);

    ConventionTP conventionTP4 = new ConventionTP();
    conventionTP4.setDomaineTP(null);
    conventionTP4.setReseauSoin("R");
    conventionTP4.setConventionCible("D");
    conventionTPList.add(conventionTP4);

    String reseauPW = "R";
    Optional<ConventionTP> c =
        TriggerDomaineServiceImpl.getDefaultConventionTPByNetwork(
            conventionTPList, dateDebutTP, reseauPW);
    Assertions.assertTrue(c.isPresent());
    Assertions.assertEquals("D", c.get().getConventionCible());
  }
}
