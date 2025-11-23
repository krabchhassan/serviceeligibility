package com.cegedim.next.trigger.worker.kafka;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.DetailDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.BenefInfos;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.TriggerBenefs;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.TriggerId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerBuildDeclarationNewService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.InstanceProperties;
import com.cegedim.next.trigger.worker.service.TriggerWorkerProcessingService;
import com.cegedim.next.trigger.worker.service.TriggerWorkerRecyclingService;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ConsumerTest {

  private Consumer consumer;

  @Mock private TriggerBuildDeclarationNewService triggerBuildDeclarationService;

  @Mock private TriggerCreationService triggerCreationService;

  @Mock private TriggerService triggerService;

  @Mock private ServicePrestationDao servicePrestationDao;

  @Mock private EventService eventService;

  @Mock private Producer producer;

  @Mock private TriggerRecyclageService triggerRecyclageService;

  @Mock private SasContratService sasContratService;

  private TriggerWorkerRecyclingService triggerRecyclingService;

  private TriggerWorkerProcessingService triggerProcessingService;

  private BeyondPropertiesService beyondPropertiesService;

  String triggerId = null;

  @BeforeEach
  void setUp() {
    triggerProcessingService =
        new TriggerWorkerProcessingService(
            triggerBuildDeclarationService,
            triggerCreationService,
            triggerService,
            sasContratService,
            servicePrestationDao,
            producer,
            triggerRecyclageService,
            eventService);
    triggerRecyclingService =
        new TriggerWorkerRecyclingService(
            triggerBuildDeclarationService,
            triggerCreationService,
            triggerService,
            sasContratService,
            servicePrestationDao,
            producer,
            triggerRecyclageService);
    beyondPropertiesService = Mockito.mock(BeyondPropertiesService.class);
    Mockito.when(beyondPropertiesService.getBooleanProperty(InstanceProperties.TRANSACTIONNAL))
        .thenReturn(Optional.of(false));
    consumer =
        new Consumer(
            triggerService,
            triggerProcessingService,
            triggerRecyclingService,
            null,
            beyondPropertiesService);
  }

  @Test
  void shouldProcess() {
    triggerId = "id";
    Trigger trigger = new Trigger();
    trigger.setId(triggerId);
    trigger.setDateDebutTraitement(new Date());
    trigger.setOrigine(TriggerEmitter.Event);
    triggerService.saveTrigger(trigger);

    TriggeredBeneficiary databaseTriggeredBeneficiary = new TriggeredBeneficiary();
    databaseTriggeredBeneficiary.setIdTrigger(triggerId);
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setDeclarations(new LinkedList<>());
    List<TriggeredBeneficiary> bl = new ArrayList<>();
    bl.add(databaseTriggeredBeneficiary);
    manageBenefsContract.setBenefs(bl);

    Mockito.when(triggerService.getTriggerById(triggerId)).thenReturn(trigger);

    trigger.setStatus(TriggerStatus.Processed);
    Mockito.when(triggerService.updateStatutTrigger(manageBenefsContract, trigger, false, 0L))
        .thenReturn(trigger);
    Mockito.when(triggerService.getTriggeredBeneficiaries(triggerId)).thenReturn(bl);
    consumer.processMessage(new TriggerId(triggerId), "false", null);
    Trigger tdb = triggerService.getTriggerById(triggerId);
    Assertions.assertNotNull(tdb);
    Assertions.assertEquals(TriggerStatus.Processed, tdb.getStatus());
  }

  @Test
  void shouldNotProcess() {
    triggerId = "id";
    Trigger trigger = new Trigger();
    trigger.setId(triggerId);
    trigger.setDateDebutTraitement(new Date());
    trigger.setOrigine(TriggerEmitter.Request);
    trigger.setDateFinTraitement(new Date());
    triggerService.saveTrigger(trigger);

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setIdTrigger(triggerId);
    triggeredBeneficiary.setStatut(TriggeredBeneficiaryStatusEnum.ToProcess);
    triggeredBeneficiary.setId("tbId");

    List<TriggeredBeneficiary> triggeredBeneficiaries = new ArrayList<>();
    triggeredBeneficiaries.add(triggeredBeneficiary);
    Mockito.when(triggerService.getTriggeredBeneficiaries(triggerId))
        .thenReturn(triggeredBeneficiaries);
    Mockito.when(triggerService.getTriggerById(triggerId)).thenReturn(trigger);

    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    manageBenefsContract.setErreurBenef(true);
    List<TriggeredBeneficiary> list = new ArrayList<>();
    list.add(triggeredBeneficiary);
    manageBenefsContract.setBenefs(list);
    SasContrat sas = new SasContrat();
    sas.setAnomalies(new ArrayList<>());
    manageBenefsContract.setSasContrat(sas);
    List<TriggerBenefs> ben = new ArrayList<>();
    TriggerBenefs b = new TriggerBenefs();
    BenefInfos benefInfos = new BenefInfos();
    benefInfos.setBenefId(triggeredBeneficiary.getId());
    benefInfos.setNumeroPersonne("1234");
    b.setBenefsInfos(List.of(benefInfos));
    b.setTriggerId(trigger.getId());
    ben.add(b);
    sas.setTriggersBenefs(ben);
    sas.setRecycling(true);
    Mockito.doAnswer(
            invocation -> {
              ManageBenefsContract item = invocation.getArgument(1);
              item.setErreurBenef(true);
              item.setBenefs(list);
              item.setSasContrat(sas);
              return null;
            })
        .when(triggerBuildDeclarationService)
        .manageBenefs(
            Mockito.any(Trigger.class), Mockito.any(), Mockito.anyBoolean(), Mockito.any());
    trigger.setStatus(TriggerStatus.ProcessedWithErrors);
    Mockito.when(
            triggerService.updateStatutTrigger(
                Mockito.any(ManageBenefsContract.class),
                Mockito.any(Trigger.class),
                Mockito.anyBoolean(),
                Mockito.anyLong()))
        .thenReturn(trigger);
    consumer.processMessage(new TriggerId(triggerId), "false", null);
    Trigger triggerById = triggerService.getTriggerById(triggerId);
    Assertions.assertNotNull(triggerById);
    Assertions.assertEquals(TriggerStatus.ProcessedWithErrors, triggerById.getStatus());
    Assertions.assertFalse(sas.isRecycling());
  }

  Trigger databaseTrigger = null;
  ParametrageCarteTP databaseParametrageCarteTP = null;
  ContratAIV5 databaseContract = null;
  SasContrat databaseSasContrat = null;
  TriggeredBeneficiary databaseTriggeredBeneficiary = null;

  private void init() {
    databaseTrigger = null;
    databaseParametrageCarteTP = null;
    databaseContract = null;

    databaseTrigger = new Trigger();
    databaseTrigger.setAmc("1234567890");
    databaseTrigger.setNbBenef(1);
    databaseTrigger.setNbBenefToProcess(1);
    databaseTrigger.setNbBenefKO(0);
    databaseTrigger.setOrigine(TriggerEmitter.Renewal);
    databaseTrigger.setStatus(TriggerStatus.ToProcess);
    databaseTrigger.setId("savedTriggerId");

    triggerId = databaseTrigger.getId();

    databaseParametrageCarteTP = getParametrageCarteTP("1234567890");
  }

  @Test
  void shouldRecycle() {
    init();
    databaseContract = prepareContract();
    databaseContract.setNumero("CONTRAT1");
    databaseContract.setIdDeclarant("1234567890");
    databaseContract.setNumeroAdherent("123");
    databaseContract.setId("databaseContractid");

    databaseSasContrat = new SasContrat();
    databaseSasContrat.setIdDeclarant("1234567890");
    databaseSasContrat.setNumeroContrat("CONTRAT1");
    databaseSasContrat.setNumeroAdherent("123");

    List<TriggerBenefs> sasBenefs = new ArrayList<>();
    TriggerBenefs triggerBenefs = new TriggerBenefs();
    BenefInfos benefInfos = new BenefInfos();
    benefInfos.setBenefId(getBenefs().get(0).getId());
    benefInfos.setNumeroPersonne("1234");
    triggerBenefs.setBenefsInfos(List.of(benefInfos));
    triggerBenefs.setTriggerId(triggerId);
    sasBenefs.add(triggerBenefs);
    databaseSasContrat.setTriggersBenefs(sasBenefs);
    databaseSasContrat.setDates(new ArrayList<>());
    databaseSasContrat.setAnomalies(new ArrayList<>());
    databaseSasContrat.setServicePrestationId(databaseContract.getId());

    Trigger triggerById = triggerService.getTriggerById(triggerId);

    RequestTriggerProcessing requestTriggerProcessing = new RequestTriggerProcessing();
    requestTriggerProcessing.setIdTrigger(triggerId);
    triggerRecyclingService.processTrigger(requestTriggerProcessing);

    SasContrat sasRes = sasContratService.getByFunctionalKey("1234567890", "CONTRAT1", "123");
    Assertions.assertNull(sasRes);
  }

  ContratAIV5 prepareContract() {
    ContratAIV5 contratAIV5 = new ContratAIV5();
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
    contratAIV5.setAssures(list);

    return contratAIV5;
  }

  private ParametrageCarteTP getParametrageCarteTP(String amc) {
    ParametrageCarteTP param = new ParametrageCarteTP();
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
    // dateDeclenchementAutomatique;
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

  private List<TriggeredBeneficiary> getBenefs() {
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    databaseTriggeredBeneficiary = new TriggeredBeneficiary();
    databaseTriggeredBeneficiary.setAmc("1234567890");
    databaseTriggeredBeneficiary.setNumeroContrat("CONTRAT1");
    databaseTriggeredBeneficiary.setIdTrigger(triggerId);
    databaseTriggeredBeneficiary.setId("databaseTriggeredBeneficiaryId");
    benefs.add(databaseTriggeredBeneficiary);
    return benefs;
  }
}
