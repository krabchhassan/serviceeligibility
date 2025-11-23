package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.TRANSACTIONNAL;
import static org.mockito.Mockito.when;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.Produit;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.CorrespondanceBobb;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.services.CalculDroitsTPGenerationService;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.WaitingExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.OcGenericException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.TriggerException;
import com.mongodb.client.result.UpdateResult;
import java.text.ParseException;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.stream.Streams;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@RequiredArgsConstructor
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerCreationServiceTest {

  public static final String DROIT_REMP = "DROIT_REMP";

  @Autowired MongoTemplate mongoTemplate;

  @Autowired private TriggerService triggerService;

  @Autowired private TriggerCreationService triggerCreationService;

  private CalculDroitsTPGenerationService calculDroitsTPGenerationService;

  @Autowired private ProductElementService productElementService;

  @SpyBean private OcService ocService;

  @SpyBean private ParametrageCarteTPService parametrageCarteTPService;

  @SpyBean private ServicePrestationService contractService;

  @SpyBean private TriggerMapper triggerMapper;

  @SpyBean private AuthenticationFacade authenticationFacade;

  @SpyBean private BeyondPropertiesService beyondPropertiesService;

  @BeforeEach
  void before() {
    mongoTemplate.findAllAndRemove(new Query(), Trigger.class);
    mongoTemplate.findAllAndRemove(new Query(), TriggeredBeneficiary.class);
    mongoTemplate.findAllAndRemove(new Query(), ParametrageCarteTP.class);
    mongoTemplate.findAllAndRemove(new Query(), ContratAIV6.class);

    Mockito.doReturn(false)
        .when(beyondPropertiesService)
        .getBooleanPropertyOrThrowError(TRANSACTIONNAL);

    initializeMocks();

    Mockito.doReturn("JUNIT").when(authenticationFacade).getAuthenticationUserName();

    Mockito.doAnswer(
            invocation -> {
              Trigger item = invocation.getArgument(0);
              item.setId("ID 1");
              return item;
            })
        .when(mongoTemplate)
        .save(Mockito.any(Trigger.class), Mockito.anyString());

    when(mongoTemplate.save(Mockito.any(Declaration.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              Declaration d = (Declaration) args[0];
              d.set_id("idOuvert");
              return d;
            });

    when(mongoTemplate.save(Mockito.any(TriggeredBeneficiary.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });

    TriggeredBeneficiary triggeredBenef = TriggerDataForTesting.getTriggeredBenef1("UUID");
    when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(TriggeredBeneficiary.class)))
        .thenReturn(triggeredBenef);

    createTrigger();

    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setCodeCarence("CAR01");
    parametrageCarence.setDateDebutParametrage("2020-01-01");
    parametrageCarenceList.add(parametrageCarence);
    try {
      Mockito.doReturn(parametrageCarenceList)
          .when(calculDroitsTPGenerationService)
          .getParametrageCarenceList(
              Mockito.any(),
              Mockito.any(),
              Mockito.any(),
              Mockito.any(),
              Mockito.any(),
              Mockito.any(),
              Mockito.eq(false));
    } catch (CarenceException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void generateTriggerUI() {
    ParametrageCarteTPService parametrageCarteTPService =
        Mockito.mock(ParametrageCarteTPService.class);

    TriggerDataForTesting.setParametrageCarteTPServiceMock(parametrageCarteTPService);
    ContratAIV6 contrat = getContrat();
    mongoTemplate.save(contrat);

    mongoTemplate.save(TriggerDataForTesting.getParametragesCarteTP());

    TriggerGenerationRequest request =
        TriggerDataForTesting.getTriggerGenerationRequest(TriggerEmitter.Request);
    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(request.getIdDeclarant());
    contratAIV6.setNumero(request.getIndividualContractNumber());
    contratAIV6.setNumeroAdherent(request.getNumeroAdherent());
    when(contractService.getContratByUK(
            request.getIdDeclarant(),
            request.getIndividualContractNumber(),
            request.getNumeroAdherent()))
        .thenReturn(contrat);

    ReflectionTestUtils.setField(
        triggerCreationService, "paramCarteTPService", parametrageCarteTPService);

    List<Trigger> triggers = triggerCreationService.generateTriggers(request);
    Assertions.assertNotNull(triggers);
  }

  @Test
  void generateTriggerEvent() {
    ContratAIV6 contrat = getContrat();
    mongoTemplate.save(contrat);

    mongoTemplate.save(TriggerDataForTesting.getParametragesCarteTP());

    TriggerGenerationRequest request =
        TriggerDataForTesting.getTriggerGenerationRequest(TriggerEmitter.Event);
    List<Trigger> triggers = new ArrayList<>();
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");

    ParametrageCarteTPService parametrageCarteTPService =
        Mockito.mock(ParametrageCarteTPService.class);
    TriggerDataForTesting.setParametrageCarteTPServiceMock(parametrageCarteTPService);
    ReflectionTestUtils.setField(
        triggerCreationService, "paramCarteTPService", parametrageCarteTPService);

    triggerCreationService.manageTriggerForContract(request, triggers, contrat, null, false);
    Assertions.assertNotNull(triggers);

    when(mongoTemplate.find(
            Mockito.any(Query.class), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(List.of(TriggerDataForTesting.getParametrageCarteTP()));

    String idTrigger =
        triggerCreationService.generateTriggerFromContracts(contrat, contrat, false, null, true);
    Assertions.assertNotNull(idTrigger);
  }

  @Test
  void generateTriggerEventWithCarenceAndReplacementRights() {
    ContratAIV6 contrat = getContratWithCarenceWithRemplacementRights(false);
    mongoTemplate.save(contrat);

    mongoTemplate.save(TriggerDataForTesting.getParametragesCarteTP());

    TriggerGenerationRequest request =
        TriggerDataForTesting.getTriggerGenerationRequest(TriggerEmitter.Event);
    List<Trigger> triggers = new ArrayList<>();
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");

    ReflectionTestUtils.setField(triggerCreationService, "triggerMapper", triggerMapper);

    triggerCreationService.manageTriggerForContract(request, triggers, contrat, null, false);
    Assertions.assertNotNull(triggers);

    when(mongoTemplate.find(
            Mockito.any(Query.class), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(List.of(TriggerDataForTesting.getParametrageCarteTP()));

    String idTrigger =
        triggerCreationService.generateTriggerFromContracts(contrat, contrat, false, null, true);
    Assertions.assertNotNull(idTrigger);

    List<TriggeredBeneficiary> triggeredBeneficiaryList =
        triggerService.getTriggeredBeneficiaries(idTrigger);
    for (TriggeredBeneficiary beneficiary : triggeredBeneficiaryList) {
      List<DroitAssure> droits = beneficiary.getNewContract().getDroitsGaranties();
      Assertions.assertTrue(droits.stream().anyMatch(droit -> DROIT_REMP.equals(droit.getCode())));
    }
  }

  @Test
  void generateTriggerEventWithTwoCarencesAndReplacementRights() {
    ContratAIV6 contrat = getContratWithCarenceWithRemplacementRights(true);
    mongoTemplate.save(contrat);
    mongoTemplate.save(TriggerDataForTesting.getParametragesCarteTP());

    TriggerGenerationRequest request =
        TriggerDataForTesting.getTriggerGenerationRequest(TriggerEmitter.Event);
    List<Trigger> triggers = new ArrayList<>();
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    triggerCreationService.manageTriggerForContract(request, triggers, contrat, null, false);
    Assertions.assertNotNull(triggers);

    List<ParametrageCarteTP> parametrageCarteTPList =
        TriggerDataForTesting.getParametrageCarteTPList();
    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(parametrageCarteTPList);
    String idTrigger =
        triggerCreationService.generateTriggerFromContracts(contrat, contrat, false, null, true);

    Assertions.assertNotNull(idTrigger);
    DroitAssure triggerServicePrestationDroit = new DroitAssure();
    List<DroitAssure> triggerServicePrestationDroitList = new ArrayList<>();
    triggerServicePrestationDroitList.add(triggerServicePrestationDroit);
    triggerServicePrestationDroitList.add(triggerServicePrestationDroit);
    triggerServicePrestationDroit.setCode(DROIT_REMP);

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setNewContract(new ServicePrestationTriggerBenef());
    triggeredBeneficiary.getNewContract().setDroitsGaranties(triggerServicePrestationDroitList);
    List<TriggeredBeneficiary> triggeredBenefList = new ArrayList<>();
    triggeredBenefList.add(triggeredBeneficiary);
    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(TriggeredBeneficiary.class), Mockito.anyString()))
        .thenReturn(triggeredBenefList);

    List<TriggeredBeneficiary> triggeredBeneficiaryList =
        triggerService.getTriggeredBeneficiaries(idTrigger);

    Assertions.assertNotEquals(0, triggeredBeneficiaryList.size());
    for (TriggeredBeneficiary beneficiary : triggeredBeneficiaryList) {
      List<DroitAssure> droits = beneficiary.getNewContract().getDroitsGaranties();
      Assertions.assertEquals(
          2, droits.stream().filter(droit -> DROIT_REMP.equals(droit.getCode())).count());
    }
  }

  @Test
  void generateTriggerEventWithCarenceWithoutReplacementRights() {
    ContratAIV6 contrat = getContratWithCarenceWithoutRemplacementRights();
    mongoTemplate.save(contrat);

    mongoTemplate.save(TriggerDataForTesting.getParametragesCarteTP());

    TriggerGenerationRequest request =
        TriggerDataForTesting.getTriggerGenerationRequest(TriggerEmitter.Event);
    List<Trigger> triggers = new ArrayList<>();
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    triggerCreationService.manageTriggerForContract(request, triggers, contrat, null, false);
    Assertions.assertNotNull(triggers);

    when(mongoTemplate.find(
            Mockito.any(Query.class), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(List.of(TriggerDataForTesting.getParametrageCarteTP()));
    String idTrigger =
        triggerCreationService.generateTriggerFromContracts(contrat, contrat, false, null, true);

    Assertions.assertNotNull(idTrigger);

    DroitAssure triggerServicePrestationDroit = new DroitAssure();
    List<DroitAssure> triggerServicePrestationDroitList = new ArrayList<>();
    triggerServicePrestationDroitList.add(triggerServicePrestationDroit);

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setNewContract(new ServicePrestationTriggerBenef());
    triggeredBeneficiary.getNewContract().setDroitsGaranties(triggerServicePrestationDroitList);
    List<TriggeredBeneficiary> triggeredBenefList = new ArrayList<>();
    triggeredBenefList.add(triggeredBeneficiary);
    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(TriggeredBeneficiary.class), Mockito.anyString()))
        .thenReturn(triggeredBenefList);
    List<TriggeredBeneficiary> triggeredBeneficiaryList =
        triggerService.getTriggeredBeneficiaries(idTrigger);

    Assertions.assertNotEquals(0, triggeredBeneficiaryList.size());
    for (TriggeredBeneficiary beneficiary : triggeredBeneficiaryList) {
      List<DroitAssure> droits = beneficiary.getNewContract().getDroitsGaranties();
      List<DroitAssure> droitList =
          droits.stream().filter(droit -> DROIT_REMP.equals(droit.getCode())).toList();
      Assertions.assertEquals(0, droitList.size());
    }
  }

  @Test
  void dontgenerateTriggerRdoWithNoTpCardParameters() {
    ContratAIV6 contrat = getContratWithCarenceWithoutRemplacementRights();
    mongoTemplate.save(contrat);
    try {
      triggerCreationService.generateTriggerFromContracts(contrat, null, false, "toto.json", true);
    } catch (TriggerException e) {
      Assertions.assertEquals("Aucun paramétrage de carte TP trouvé", e.getMessage());
    }
  }

  @Test
  void generateTriggerEventErrorWhenOcNotResponding() {
    ContratAIV6 contrat = getContrat();
    mongoTemplate.save(contrat);

    mongoTemplate.save(TriggerDataForTesting.getParametragesCarteTP());

    TriggerGenerationRequest request =
        TriggerDataForTesting.getTriggerGenerationRequest(TriggerEmitter.Event);
    List<Trigger> triggers = new ArrayList<>();
    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    triggerCreationService.manageTriggerForContract(request, triggers, contrat, null, false);
    Assertions.assertNotNull(triggers);
  }

  // TODO @Test
  void generateTriggerBatch() {
    ContratAIV6 contrat = getContrat();
    mockMongoForBatch(contrat);

    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    TriggerGenerationRequest request = new TriggerGenerationRequest();

    List<ParametrageCarteTP> parametrageCarteTPList =
        TriggerDataForTesting.getParametragesCarteTP();
    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(parametrageCarteTPList);

    request.setDate("2021-06-16");
    request.setEmitter(TriggerEmitter.Renewal);
    request.setIdParametrageCarteTP("12");
    List<Trigger> triggers = triggerCreationService.generateTriggers(request);
    Assertions.assertFalse(triggers.isEmpty());

    request.setDate(null);
    request.setEmitter(TriggerEmitter.Renewal);
    request.setIdParametrageCarteTP("12");
    triggerCreationService.generateTriggers(request);
  }

  @Test
  void generateTriggerWithSuspension() throws ParseException {
    ContratAIV6 contrat = getContratWithSuspensionPeriod();
    mongoTemplate.save(contrat);

    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    TriggerGenerationRequest request = new TriggerGenerationRequest();

    mockMongoForBatch(contrat);

    List<ParametrageCarteTP> parametrageCarteTPList =
        TriggerDataForTesting.getParametragesCarteTP();
    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(parametrageCarteTPList);

    request.setDate("2021-06-16");
    request.setEmitter(TriggerEmitter.Renewal);
    request.setIdParametrageCarteTP("12");
    triggerCreationService.generateTriggers(request);
    request.setDate(null);
    request.setEmitter(TriggerEmitter.Renewal);
    request.setIdParametrageCarteTP("12");
    List<Trigger> triggers = triggerCreationService.generateTriggers(request);
    Assertions.assertNotNull(triggers);
  }

  @Test
  void generateTriggerBatchWithoutOc() throws ParseException {
    ContratAIV6 contrat = getContrat();
    mongoTemplate.save(contrat);

    mockMongoForBatch(contrat);

    List<ParametrageCarteTP> parametrageCarteTPList =
        TriggerDataForTesting.getParametragesCarteTP();
    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(parametrageCarteTPList);

    mockMongoForBatch(contrat);

    TriggerGenerationRequest request = new TriggerGenerationRequest();
    request.setDate("2021-06-16");
    request.setEmitter(TriggerEmitter.Renewal);
    request.setIdParametrageCarteTP("12");
    triggerCreationService.generateTriggers(request);

    when(ocService.getOC(Mockito.anyString())).thenThrow(new OcGenericException("Erreur JUNIT"));
  }

  @Test
  void invalidRequest2() {

    TriggerGenerationRequest request = new TriggerGenerationRequest();
    request.setDate("2021-06-21");
    request.setEmitter(TriggerEmitter.Renewal);
    request.setIdParametrageCarteTP("12");

    mockMongoForBatch(null);

    try {
      triggerCreationService.generateTriggers(request);
    } catch (TriggerException e) {
      Assertions.assertEquals("La requête de création du trigger est incomplète", e.getMessage());
    }
  }

  @Test
  void manageTriggerCounter() {
    Trigger trigger = new Trigger();
    trigger.setAmc("321032103");
    trigger.setNbBenef(10);
    trigger.setNbBenefKO(0);
    trigger.setNbBenefToProcess(10);
    Trigger savedTrigger = triggerService.createTrigger(trigger);
    triggerService.manageBenefCounter(savedTrigger.getId(), 1, 0, -1);

    trigger.setNbBenefKO(1);
    trigger.setNbBenefToProcess(9);
    when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(trigger);
    trigger = triggerService.getTriggerById(savedTrigger.getId());
    Assertions.assertEquals(1, trigger.getNbBenefKO());
    Assertions.assertEquals(9, trigger.getNbBenefToProcess());
  }

  private void createTrigger() {
    Trigger t = getTrigger();

    t = triggerService.createTrigger(t);
    int count = createTriggeredBeneficiaries(t.getId());
    t.setNbBenef(count);
    t.setStatus(TriggerStatus.ToProcess);

    mongoTemplate.save(t);
  }

  private Trigger getTrigger() {
    Trigger t = new Trigger();

    t.setId("0123456789");
    t.setAmc("1234567890");
    t.setDateEffet("2021-06-21");
    t.setOrigine(TriggerEmitter.Renewal);
    t.setStatus(TriggerStatus.StandBy);

    t.setDateDebutTraitement(new Date());

    return t;
  }

  private int createTriggeredBeneficiaries(String triggerId) {
    TriggeredBeneficiary tb1 = new TriggeredBeneficiary();
    tb1.setAmc("1234567890");

    tb1.setIdTrigger(triggerId);
    tb1.setParametrageCarteTPId("123");
    tb1.setNir("1234567890123");
    tb1.setIsContratIndividuel(true);
    tb1.setNumeroContrat("C1");
    tb1.setCollectivite("Collect1");
    tb1.setCollege("College1");
    tb1.setDateNaissance("19800101");
    tb1.setCritereSecondaireDetaille("CSD1");
    triggerService.createTriggeredBenef(tb1);

    TriggeredBeneficiary tb2 = new TriggeredBeneficiary();
    tb2.setAmc("1234567890");
    tb2.setIdTrigger(triggerId);
    tb2.setParametrageCarteTPId("123");
    tb2.setNir("1234567890123");
    tb2.setIsContratIndividuel(true);
    tb2.setNumeroContrat("C2");
    tb2.setCollectivite("Collect1");
    tb2.setCollege("College1");
    tb2.setDateNaissance("19800101");
    tb2.setCritereSecondaireDetaille("CSD2");
    triggerService.createTriggeredBenef(tb2);

    return 2;
  }

  private ContratAIV6 getContrat() {
    ContratAIV6 contrat = new ContratAIV6();
    contrat.setId("65367a8a0cb9242019492fc7");
    contrat.setIdDeclarant("1234567890");
    contrat.setNumero("123123123");
    contrat.setNumeroAdherent("123");
    contrat.setCritereSecondaireDetaille("Critere secondaire");
    contrat.setContexteTiersPayant(getContexteTP());
    contrat.setContratCollectif(getContratCollectif());
    Assure assure = getAssure(false, false, false);
    contrat.setAssures(List.of(assure));
    contrat.setDateResiliation("9999-12-31");
    contrat.setDateSouscription("2021-06-01");
    return contrat;
  }

  private ContratAIV6 getContratWithSuspensionPeriod() {
    ContratAIV6 contrat = getContrat();
    PeriodeSuspension periodeSuspension1 = new PeriodeSuspension();
    Periode period1 = new Periode();
    period1.setDebut("2022-10-01");
    periodeSuspension1.setPeriode(period1);
    PeriodeSuspension periodeSuspension2 = new PeriodeSuspension();
    Periode period2 = new Periode();
    period2.setDebut("2022-09-01");
    period2.setFin("2022-09-30");
    periodeSuspension2.setPeriode(period2);
    contrat.setPeriodesSuspension(new ArrayList<>());
    contrat.getPeriodesSuspension().add(periodeSuspension1);
    contrat.getPeriodesSuspension().add(periodeSuspension2);
    return contrat;
  }

  private ContratAIV6 getContratWithCarenceWithRemplacementRights(boolean hasTwoCarences) {
    ContratAIV6 contrat = new ContratAIV6();
    contrat.setCodeOc("oc");

    contrat.setIdDeclarant("1234567890");
    contrat.setNumero("123123123");
    contrat.setNumeroAdherent("123123123");
    contrat.setCritereSecondaireDetaille("Critere secondaire");
    contrat.setContexteTiersPayant(getContexteTP());
    contrat.setContratCollectif(getContratCollectif());
    Assure assure = getAssure(true, true, hasTwoCarences);
    contrat.setAssures(List.of(assure));
    contrat.setDateResiliation("9999-12-31");
    contrat.setDateSouscription("2021-06-01");
    return contrat;
  }

  private ContratAIV6 getContratWithCarenceWithoutRemplacementRights() {
    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("1234567890");
    contrat.setNumero("123123123");
    contrat.setNumeroAdherent("123123123");
    contrat.setCritereSecondaireDetaille("Critere secondaire");
    contrat.setContexteTiersPayant(getContexteTP());
    contrat.setContratCollectif(getContratCollectif());
    Assure assure = getAssure(true, false, false);
    contrat.setAssures(List.of(assure));
    contrat.setDateResiliation("9999-12-31");
    contrat.setDateSouscription("2021-06-01");
    return contrat;
  }

  private DataAssure getDataAssure() {
    DataAssure data = new DataAssure();
    data.setNom(getNomAssure());
    data.setAdresse(getAdresseAssure());
    return data;
  }

  private void initializeMocks() {
    calculDroitsTPGenerationService = Mockito.mock(CalculDroitsTPGenerationService.class);
    ocService = Mockito.mock(OcService.class);
    parametrageCarteTPService = Mockito.mock(ParametrageCarteTPService.class);

    TriggerDataForTesting.initializeProductElementLight(productElementService);
    TriggerDataForTesting.setParametrageCarteTPServiceMock(parametrageCarteTPService);
    WaitingExtendedOffreProduits pel = new WaitingExtendedOffreProduits();
    ExtendedOffreProduits extendedOffreProduits = new ExtendedOffreProduits();
    pel.setOffersAndProducts(extendedOffreProduits);
    extendedOffreProduits.setCode("OFFER1");
    Produit produit = new Produit();
    produit.setCode("PRODUCT1");
    extendedOffreProduits.setProduits(List.of(produit));
    Mockito.doReturn(List.of(pel)).when(triggerMapper).getOffers(Mockito.any());
  }

  private CarenceDroit getCarence(boolean withRemplacementRights) {
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("OPTI");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2020-01-01");
    periodeCarence.setFin("2020-03-31");
    carence.setPeriode(periodeCarence);
    if (withRemplacementRights) {
      getDroitRemplacement(carence);
    }
    return carence;
  }

  private void getDroitRemplacement(CarenceDroit carence) {
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode(DROIT_REMP);
    droitRemplacement.setLibelle("LIBELLE");
    droitRemplacement.setCodeAssureur("CODE_ASSUREUR");
    carence.setDroitRemplacement(droitRemplacement);
  }

  private List<CodePeriode> getRegimesParticuliers() {
    List<CodePeriode> regimesParticuliers = new ArrayList<>();
    regimesParticuliers.add(new CodePeriode("JUN", new Periode("2020-01-01", null)));
    return regimesParticuliers;
  }

  private List<CodePeriode> getSituationsParticulieres() {
    List<CodePeriode> situationsParticulieres = new ArrayList<>();
    situationsParticulieres.add(new CodePeriode("CMUP", new Periode("2020-01-01", null)));
    return situationsParticulieres;
  }

  private ContexteTPV6 getContexteTP() {
    return new ContexteTPV6();
  }

  private List<NirRattachementRO> getAffiliationsRO() {
    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nrr = new NirRattachementRO();
    nrr.setNir(new Nir("1791062654953", "11"));
    nrr.setPeriode(new Periode("2020-01-01", null));
    nrr.setRattachementRO(new RattachementRO("01", "124", "13213"));
    affiliationsRO.add(nrr);
    return affiliationsRO;
  }

  private List<DestinatairePrestations> getDestinatairePaiements() {
    List<DestinatairePrestations> destPrests = new ArrayList<>();
    DestinatairePrestations dest = new DestinatairePrestations();
    dest.setAdresse(getAdresse());
    dest.setPeriode(new PeriodeDestinataire("2020-01-01", null));
    dest.setModePaiementPrestations(new ModePaiement("JUN", "JJ", "Junit"));
    destPrests.add(dest);
    return destPrests;
  }

  private Adresse getAdresse() {
    Adresse adrDestPrest = new Adresse();
    adrDestPrest.setLigne1("Ligne 1");
    adrDestPrest.setLigne4("Ligne 4");
    adrDestPrest.setLigne6("Ligne 6");
    return adrDestPrest;
  }

  private DigitRelation getDigitRelation() {
    DigitRelation digitRelation = new DigitRelation();
    List<Teletransmission> teletransmissions = new ArrayList<>();
    teletransmissions.add(new Teletransmission(new Periode("2020-01-01", null), true));
    digitRelation.setTeletransmissions(teletransmissions);
    return digitRelation;
  }

  private ContratCollectifV6 getContratCollectif() {
    ContratCollectifV6 cc = new ContratCollectifV6();
    cc.setNumero("CC1");
    cc.setNumeroExterne("CCE1");
    cc.setIdentifiantCollectivite("Collectivité Junit");
    cc.setGroupePopulation("College Junit");
    return cc;
  }

  private IdentiteContrat getIdentiteContrat() {
    IdentiteContrat identite = new IdentiteContrat();
    identite.setDateNaissance("1979-04-15");
    identite.setNir(new Nir("1791062654953", "11"));
    identite.setNumeroPersonne("560560465");
    identite.setAffiliationsRO(getAffiliationsRO());
    return identite;
  }

  private AdresseAssure getAdresseAssure() {
    AdresseAssure adresse = new AdresseAssure();
    adresse.setLigne1("Ligne 1");
    adresse.setLigne4("Ligne 4");
    adresse.setLigne6("Ligne 6");
    return adresse;
  }

  private List<DroitAssure> getDroitAssure(
      boolean hasCarence, boolean hasReplacementRights, boolean hasTwoCarences) {
    List<DroitAssure> droits = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    droit.setCode("CODE");
    droit.setCodeAssureur("CODE_ASSUREUR");
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    droit.setPeriode(periode);
    if (hasCarence) {
      List<CarenceDroit> carenceList = new ArrayList<>();
      carenceList.add(getCarence(hasReplacementRights));
      droit.setCarences(carenceList);
    }
    droits.add(droit);
    droit = new DroitAssure();
    droit.setCode("CODE2");
    droit.setCodeAssureur("CODE_ASSUREUR2");
    droit.setPeriode(periode);
    if (hasTwoCarences) {
      List<CarenceDroit> carenceList = new ArrayList<>();
      carenceList.add(getCarence(hasReplacementRights));
      droit.setCarences(carenceList);
    }
    CorrespondanceBobb bobb = new CorrespondanceBobb();
    bobb.setCodeOffre("codeOffre");
    bobb.setCodeProduit("codeProduit");

    droits.add(droit);
    return droits;
  }

  private List<Periode> getPeriodesMedecin() {
    List<Periode> periodesMedecin = new ArrayList<>();
    periodesMedecin.add(new Periode("2020-01-01", null));
    return periodesMedecin;
  }

  private NomAssure getNomAssure() {
    NomAssure nom = new NomAssure();
    nom.setNomFamille("JUNIT");
    nom.setPrenom("Test");
    nom.setCivilite("JT");
    return nom;
  }

  private Assure getAssure(
      boolean hasCarence, boolean hasReplacementRights, boolean hasTwoCarences) {
    Assure assure = new Assure();
    assure.setQualite(new QualiteAssure("A", "Assureur"));
    DataAssure data = getDataAssure();
    data.setDestinatairesPaiements(getDestinatairePaiements());
    assure.setData(data);
    assure.setIdentite(getIdentiteContrat());
    assure.setDroits(getDroitAssure(hasCarence, hasReplacementRights, hasTwoCarences));
    assure.setIsSouscripteur(true);
    assure.setDateRadiation("9999-12-31");
    assure.setPeriodesMedecinTraitantOuvert(getPeriodesMedecin());
    assure.setRegimesParticuliers(getRegimesParticuliers());
    assure.setSituationsParticulieres(getSituationsParticulieres());
    assure.setDigitRelation(getDigitRelation());
    assure.setRangAdministratif("1");
    return assure;
  }

  private void mockMongoForBatch(ContratAIV6 contract) {
    List<ParametrageCarteTP> parametrageCarteTPList = new ArrayList<>();
    parametrageCarteTPList.add(TriggerDataForTesting.getParametrageCarteTP());
    when(mongoTemplate.aggregate(
            Mockito.any(Aggregation.class),
            Mockito.anyString(),
            Mockito.eq(ParametrageCarteTP.class)))
        .thenReturn(new AggregationResults<>(parametrageCarteTPList, new Document()));

    when(mongoTemplate.find(
            Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(parametrageCarteTPList);

    List<ContratAIV6> contratAIV6List = new ArrayList<>();
    if (contract != null) {
      contratAIV6List.add(contract);
    }
    when(mongoTemplate.aggregate(
            Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.eq(ContratAIV6.class)))
        .thenReturn(new AggregationResults<>(contratAIV6List, new Document()));
    AggregationResults<ContratAIV6> aggregationResults =
        new AggregationResults<>(contratAIV6List, new Document());
    Mockito.when(
            mongoTemplate.aggregateStream(
                Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.eq(ContratAIV6.class)))
        .thenReturn(Streams.of(aggregationResults.iterator()));

    when(mongoTemplate.updateMulti(
            Mockito.any(Query.class),
            Mockito.any(UpdateDefinition.class),
            Mockito.eq(ContratAIV6.class)))
        .thenReturn(UpdateResult.acknowledged(1, 1L, null));
  }

  @Test
  void generateNothingIfContractsNull() {
    String response = "";

    try {
      response = triggerCreationService.generateTriggerFromContracts(null, null, false, null, true);
      Assertions.assertNull(response);
    } catch (Exception exception) {
      Assertions.assertEquals("", response);
    }
  }

  @Test
  void invalidRequest() {
    TriggerGenerationRequest request = new TriggerGenerationRequest();
    String idDeclarant = "132";
    String numero = "12";
    String numeroAdherent = "123";
    request.setDate("2020-01-001");
    request.setEmitter(TriggerEmitter.Request);
    request.setIdDeclarant(idDeclarant);
    request.setIndividualContractNumber(numero);
    request.setNumeroAdherent(numeroAdherent);
    ContratAIV6 contratAIV6 = new ContratAIV6();
    contratAIV6.setIdDeclarant(idDeclarant);
    contratAIV6.setNumero(numero);
    contratAIV6.setNumeroAdherent(numeroAdherent);
    when(contractService.getContratByUK(idDeclarant, numero, numeroAdherent))
        .thenReturn(contratAIV6);

    try {
      triggerCreationService.generateTriggers(request);
    } catch (TriggerException e) {
      Assertions.assertEquals(
          "La date 2020-01-001 n'est pas une date au format yyyy-MM-dd", e.getMessage());
    }

    TriggerGenerationRequest requestBatch = new TriggerGenerationRequest();
    requestBatch.setDate("2020-01-001");
    requestBatch.setEmitter(TriggerEmitter.Renewal);
    try {
      triggerCreationService.generateTriggers(requestBatch);
    } catch (TriggerException e) {
      Assertions.assertEquals(
          "La date 2020-01-001 n'est pas une date au format yyyy-MM-dd", e.getMessage());
    }

    TriggerGenerationRequest requestIncomplete = new TriggerGenerationRequest();
    requestIncomplete.setDate("2020-01-01");
    try {
      triggerCreationService.generateTriggers(requestIncomplete);
    } catch (TriggerException e) {
      Assertions.assertEquals("La requête de création du trigger est incomplète !", e.getMessage());
    }

    requestIncomplete = new TriggerGenerationRequest();
    requestIncomplete.setDate("2020-01-01");
    requestIncomplete.setEmitter(TriggerEmitter.Request);
    try {
      triggerCreationService.generateTriggers(requestIncomplete);
    } catch (TriggerException e) {
      Assertions.assertEquals("La requête de création du trigger est incomplète !", e.getMessage());
    }
  }

  @Test
  void noParamsShouldReturnNull() {
    ContratAIV6 contrat = getContrat();

    ParametrageCarteTPService previous = triggerCreationService.paramCarteTPService;

    ParametrageCarteTPService parametrageCarteTPServiceMock =
        Mockito.mock(ParametrageCarteTPService.class);
    ReflectionTestUtils.setField(
        triggerCreationService, "paramCarteTPService", parametrageCarteTPServiceMock);
    Mockito.doReturn(Collections.emptyList())
        .when(parametrageCarteTPServiceMock)
        .getByAmc(Mockito.any());

    TriggerGenerationRequest request = new TriggerGenerationRequest();
    request.setDate("2020-01-01");
    request.setEmitter(TriggerEmitter.Request);
    List<Trigger> triggers = new ArrayList<>();
    boolean createTriggeredBenefWhenError = false; // doit être à false en Request

    Assertions.assertThrows(
        TriggerException.class,
        () ->
            triggerCreationService.manageTriggerForContract(
                request,
                triggers,
                createTriggeredBenefWhenError,
                contrat,
                null,
                false,
                null,
                false));

    Assertions.assertNotNull(triggers);
    Assertions.assertEquals(0, triggers.size());

    // Obligé sinon des tests cassent
    ReflectionTestUtils.setField(triggerCreationService, "paramCarteTPService", previous);
  }

  @Test
  void testgetAssureList() {

    ContratAIV6 contrat = TriggerDataForTesting.getContratAIV6();

    Periode periodeFromParametrage = new Periode("2024-01-01", "2024-12-31");

    Assure assureSouscripteur =
        TriggerUtils.getAssureForRenouvellement(contrat, periodeFromParametrage);

    List<Assure> assures =
        triggerCreationService.getAssureList(contrat, assureSouscripteur, periodeFromParametrage);
    Assertions.assertEquals(2, assures.size());
    Assertions.assertEquals("3", assures.get(0).getIdentite().getNumeroPersonne());
    Assertions.assertEquals("2", assures.get(1).getIdentite().getNumeroPersonne());
  }
}
