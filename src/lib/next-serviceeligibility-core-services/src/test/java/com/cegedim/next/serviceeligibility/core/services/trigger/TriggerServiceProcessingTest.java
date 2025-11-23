package com.cegedim.next.serviceeligibility.core.services.trigger;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.TRANSACTIONNAL;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.TriggerDao;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.DetailDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerServiceProcessingTest {

  @Autowired TriggerService triggerService;

  @Autowired TriggerCreationService triggerToDeclarationService;

  @SpyBean TestTriggerProcessing triggerProcessingService;

  @Autowired MongoTemplate mongoTemplate;

  @SpyBean TriggerDao triggerDao;

  @SpyBean SasContratService sasContratService;

  @Autowired DeclarationDao declarationDao;

  @SpyBean EventService eventService;

  @SpyBean TriggerRecyclageService triggerRecyclageService;

  @SpyBean private BeyondPropertiesService beyondPropertiesService;

  private static final String TRIGGERS = "triggers";
  private static final String TRIGGERED_BENEFICIARY = "triggeredBeneficiary";
  private static final String PARAMETRAGES_CARTE_TP = "parametragesCarteTP";
  private static final String SERVICE_PRESTATION = "servicePrestation";
  private static final String DECLARATION = "declarations";
  private static final String SAS_CONTRAT = "sasContrat";

  private String triggerId;

  @BeforeEach
  public void before() {
    mongoTemplate.findAllAndRemove(new Query(), Trigger.class, TRIGGERS);
    mongoTemplate.findAllAndRemove(new Query(), TriggeredBeneficiary.class, TRIGGERED_BENEFICIARY);
    mongoTemplate.findAllAndRemove(new Query(), ParametrageCarteTP.class, PARAMETRAGES_CARTE_TP);
    mongoTemplate.findAllAndRemove(new Query(), SERVICE_PRESTATION);
    mongoTemplate.findAllAndRemove(new Query(), DECLARATION);
    mongoTemplate.findAllAndRemove(new Query(), SAS_CONTRAT);

    Mockito.doReturn(false)
        .when(beyondPropertiesService)
        .getBooleanPropertyOrThrowError(TRANSACTIONNAL);

    AuthenticationFacade authenticationFacade = Mockito.mock(AuthenticationFacade.class);
    Mockito.when(authenticationFacade.getAuthenticationUserName()).thenReturn("JUNIT");
    ReflectionTestUtils.setField(triggerDao, "authenticationFacade", authenticationFacade);

    Mockito.when(mongoTemplate.save(Mockito.any(), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
    Mockito.when(mongoTemplate.save(Mockito.any()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
    init();

    ParametrageCarteTP pc = getParametrageCarteTP("1234567890");
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(List.of(pc));
  }

  private void init() {
    Trigger savedTrigger =
        mongoTemplate.save(getTrigger(TriggerStatus.ToProcess), Constants.TRIGGER);

    triggerId = savedTrigger.getId();
  }

  private Trigger getTrigger(TriggerStatus triggerStatus, List<String> troRecycle) {
    Trigger trigger = new Trigger();
    trigger.setId("UUID");
    trigger.setAmc("1234567890");
    trigger.setNbBenef(1);
    trigger.setNbBenefToProcess(1);
    trigger.setNbBenefKO(0);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setStatus(triggerStatus);
    trigger.setBenefsToRecycle(troRecycle);
    return trigger;
  }

  private Trigger getTrigger(TriggerStatus triggerStatus) {
    return getTrigger(triggerStatus, Collections.emptyList());
  }

  private LinkedList<TriggeredBeneficiary> getBenefs() {
    LinkedList<TriggeredBeneficiary> benefs = new LinkedList<>();
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setId("UUID");
    benef.setAmc("1234567890");
    benef.setNumeroContrat("CONTRAT1");
    benef.setNumeroAdherent("123");
    benef.setIdTrigger(triggerId);

    List<DroitAssure> triggerServicePrestationDroitList = new ArrayList<>();

    benef.setNewContract(new ServicePrestationTriggerBenef());
    benef.getNewContract().setDroitsGaranties(triggerServicePrestationDroitList);
    benef = mongoTemplate.save(benef);
    benefs.add(benef);
    return benefs;
  }

  @Test
  void shouldWork() {
    TriggerBatchUnitaire tbu = new TriggerBatchUnitaire();
    tbu.setServicePrestationId("XYZ");
    tbu.setTriggerId(triggerId);
    tbu.setTriggeredBeneficiaries(getBenefs());

    Trigger trigger = getTrigger(TriggerStatus.ToProcess);
    trigger.setStatus(TriggerStatus.Processed);
    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(trigger);
    triggerProcessingService.manageMessageContrat(
        triggerId,
        null,
        true,
        getBenefs(),
        Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE,
        RandomUtils.nextLong(),
        null);
    Trigger triggerResult = triggerService.getTriggerById(triggerId);
    Assertions.assertEquals(TriggerStatus.Processed, triggerResult.getStatus());

    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(Declaration.class)))
        .thenReturn(List.of(new Declaration()));
    List<Declaration> d =
        declarationDao.findDeclarationsOfBenef(
            "1234567890", "CONTRAT1", null, null, null, null, null);
    Assertions.assertEquals(1, d.size());
  }

  private void mockManageBenefCounterTrigger(Trigger trigger) {
    Mockito.doAnswer(
            invocation -> {
              int nbKO = invocation.getArgument(1, Integer.class);
              int nbWarning = invocation.getArgument(2, Integer.class);
              int nbToProcess = invocation.getArgument(3, Integer.class);
              trigger.setNbBenefKO(trigger.getNbBenefKO() + nbKO);
              trigger.setNbBenefWarning(trigger.getNbBenefWarning() + nbWarning);
              trigger.setNbBenefToProcess(trigger.getNbBenefToProcess() + nbToProcess);
              return trigger.getNbBenefToProcess();
            })
        .when(triggerDao)
        .manageBenefCounter(
            Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
  }

  private void disableSendEvent() {
    Mockito.doAnswer(invocation -> Collections.emptyMap())
        .when(eventService)
        .sendObservabilityEventTriggerFinished(Mockito.any(Trigger.class));
    Mockito.doAnswer(invocation -> Collections.emptyMap())
        .when(eventService)
        .sendObservabilityEventTriggerBeneficiaryFinished(Mockito.any(TriggeredBeneficiary.class));
  }

  @Test
  void shouldGetTrigger() {
    Trigger expectedTrigger = getTrigger(TriggerStatus.ToProcess);
    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(expectedTrigger);
    Trigger gotTrigger = triggerProcessingService.getTriggerAndUpdateItToProcessing("UUID");
    Assertions.assertEquals(expectedTrigger, gotTrigger);
  }

  private ParametrageCarteTP getParametrageCarteTP(String amc) {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setId("UUID");
    param.setAmc(amc);
    param.setStatut(ParametrageCarteTPStatut.Actif);
    param.setDateDebutValidite("2020-01-01");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDateDeclenchementManuel("2001-01-01");

    parametrageRenouvellement.setSeuilSecurite(1000);
    param.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP

    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("SP");
    parametrageDroitsCarteTP.setCodeOperateurTP("OPS");
    parametrageDroitsCarteTP.setIsCarteEditablePapier(true);
    parametrageDroitsCarteTP.setIsCarteDematerialisee(true);
    parametrageDroitsCarteTP.setRefFondCarte("fondCarte1");
    parametrageDroitsCarteTP.setCodeAnnexe1("Annexe1");
    parametrageDroitsCarteTP.setCodeAnnexe2("Annexe2");
    List<DetailDroit> detailsDroit = new ArrayList<>();
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(1);
    detailDroit.setCodeDomaineTP("PHAR");
    detailDroit.setLibelleDomaineTP("Pharmacie");
    detailDroit.setConvention("IS");

    detailsDroit.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(2);
    detailDroit.setCodeDomaineTP("DENT");
    detailDroit.setLibelleDomaineTP("Dentaire");
    detailDroit.setConvention("IS");
    detailsDroit.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(3);
    detailDroit.setCodeDomaineTP("OPTI");
    detailDroit.setLibelleDomaineTP("Optique");
    detailDroit.setConvention("SP");
    detailsDroit.add(detailDroit);
    parametrageDroitsCarteTP.setDetailsDroit(detailsDroit);

    param.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    return param;
  }

  ContratAIV6 prepareContract() {
    ContratAIV6 c = new ContratAIV6();
    Assure ass = new Assure();

    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setNomUsage("bob");
    data.setNom(nom);
    Contact contact = new Contact();
    contact.setEmail("mymail");
    data.setContact(contact);
    ass.setData(data);
    IdentiteContrat id = new IdentiteContrat();
    id.setNumeroPersonne("numero personn");
    ass.setIdentite(id);
    List<Assure> list = new ArrayList<>();
    list.add(ass);
    c.setAssures(list);

    return c;
  }

  @Test
  void shouldNotSendTriggerBenefFinishedRenouv() {
    testManageMessageContrat(true, false, false, true);
  }

  @Test
  void shouldSendTriggerBenefFinishedRenouv() {
    testManageMessageContrat(true, true, true, false);
  }

  @Test
  void shouldSendTriggerBenefFinishedEvent() {
    testManageMessageContrat(false, true, false, false);
  }

  void testManageMessageContrat(
      boolean isReneouv, boolean withSas, boolean withRecyclingBenefs, boolean shouldBeEmpty) {
    List<TriggeredBeneficiary> triggeredBeneficiaries = getBenefs();
    TriggerBatchUnitaire tbu = new TriggerBatchUnitaire();
    tbu.setServicePrestationId("XYZ");
    tbu.setTriggerId(triggerId);
    tbu.setTriggeredBeneficiaries(triggeredBeneficiaries);
    SasContrat sasContrat = new SasContrat();
    TriggerBenefs triggerBenefs = new TriggerBenefs();
    triggerBenefs.setTriggerId(triggerId);
    List<BenefInfos> benefInfos = new ArrayList<>();
    for (TriggeredBeneficiary triggeredBenef : triggeredBeneficiaries) {
      BenefInfos benefInfo = new BenefInfos();
      benefInfo.setBenefId(triggeredBenef.getId());
      benefInfo.setNumeroPersonne(triggeredBenef.getNumeroPersonne());
      benefInfos.add(benefInfo);
    }
    triggerBenefs.setBenefsInfos(benefInfos);
    sasContrat.setTriggersBenefs(List.of(triggerBenefs));
    sasContrat.setAnomalies(new ArrayList<>());

    Trigger trigger =
        getTrigger(
            TriggerStatus.ToProcess,
            withRecyclingBenefs
                ? triggerBenefs.getBenefsInfos().stream().map(BenefInfos::getBenefId).toList()
                : Collections.emptyList());
    mockManageBenefCounterTrigger(trigger);
    disableSendEvent();

    Mockito.doAnswer(
            inv -> {
              List<TriggeredBeneficiary> toSend = inv.getArgument(1);
              Assertions.assertEquals(toSend.isEmpty(), shouldBeEmpty);
              return inv;
            })
        .when(triggerRecyclageService)
        .launchFinishedEvents(Mockito.any(), Mockito.any());

    Mockito.when(mongoTemplate.findById(Mockito.anyString(), Mockito.eq(Trigger.class)))
        .thenReturn(trigger);
    triggerProcessingService.manageMessageContrat(
        triggerId,
        withSas ? sasContrat : null,
        isReneouv,
        triggeredBeneficiaries,
        Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE,
        0L,
        null);
  }
}
