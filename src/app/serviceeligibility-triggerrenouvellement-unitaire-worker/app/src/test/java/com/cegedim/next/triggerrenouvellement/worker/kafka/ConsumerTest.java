package com.cegedim.next.triggerrenouvellement.worker.kafka;

import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DetailsByDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflinePW;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.Variable;
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
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.PwService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import com.cegedim.next.triggerrenouvellement.worker.config.TestConfiguration;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerProcessingService;
import com.cegedim.next.triggerrenouvellement.worker.service.TriggerUnitaireWorkerRecyclingService;
import java.util.*;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class ConsumerTest {

  @Autowired private TriggerService triggerService;

  @Autowired private TriggerUnitaireWorkerProcessingService triggerUnitaireWorkerProcessingService;

  @Autowired private TriggerUnitaireWorkerRecyclingService triggerUnitaireWorkerRecyclingService;

  @SpyBean private MongoTemplate mongoTemplate;

  @SpyBean private AuthenticationFacade authenticationFacade;

  @SpyBean private OcService ocService;

  @SpyBean private PwService pwService;

  private String triggerId = "triggerId";
  List<TriggeredBeneficiary> databaseTriggeredBeneficiary = new ArrayList<>();
  List<ParametrageCarteTP> databaseParametrageCarteTP = new ArrayList<>();
  List<Declaration> databaseDeclaration = new ArrayList<>();
  List<SasContrat> databaseSas = new ArrayList<>();

  @SpyBean private EventService eventService;

  @BeforeEach
  public void before() throws PwException {
    databaseDeclaration.clear();
    Mockito.doAnswer(
            invocation -> {
              Declaration item = invocation.getArgument(0);
              databaseDeclaration.add(item);
              return item;
            })
        .when(mongoTemplate)
        .save(Mockito.any(), Mockito.eq(Constants.DECLARATION_COLLECTION));

    Mockito.doReturn("JUNIT").when(authenticationFacade).getAuthenticationUserName();

    Oc oc = new Oc();
    oc.setCode("CONTRAT1");
    Mockito.doReturn(oc).when(ocService).getOC(Mockito.any());

    DroitsTPOfflinePW dtpo = new DroitsTPOfflinePW();
    DetailsByDomain dd = new DetailsByDomain();
    dd.setFormulaCode("formulaCode");
    Variable v = new Variable();
    v.setValue("0");
    v.setStsVariableNumber(2);
    List<Variable> vs = new ArrayList<>();
    vs.add(v);
    dd.setVariables(vs);
    HashMap<String, DetailsByDomain> mdd = new HashMap<>();
    mdd.put("key", dd);
    dtpo.setDetailsByDomain(mdd);
    Mockito.doReturn(List.of(dtpo))
        .when(pwService)
        .getDroitsProductsWorkshop(Mockito.any(), Mockito.any(), Mockito.any());
    init();
  }

  Trigger databaseTrigger = null;

  private void init() {
    databaseTrigger = new Trigger();
    databaseTrigger.setAmc("1234567890");
    databaseTrigger.setNbBenef(1);
    databaseTrigger.setNbBenefToProcess(1);
    databaseTrigger.setNbBenefKO(0);
    databaseTrigger.setOrigine(TriggerEmitter.Renewal);
    databaseTrigger.setStatus(TriggerStatus.ToProcess);
    databaseTrigger.setId(triggerId);
    databaseTrigger.setBenefsToRecycle(Collections.emptyList());
    Mockito.when(mongoTemplate.findById(triggerId, Trigger.class)).thenReturn(databaseTrigger);

    databaseParametrageCarteTP.clear();
    databaseParametrageCarteTP.add(getParametrageCarteTP("1234567890"));

    Mockito.when(mongoTemplate.findById(Mockito.any(), Mockito.eq(ParametrageCarteTP.class)))
        .thenReturn(databaseParametrageCarteTP.get(0));

    Mockito.when(
            mongoTemplate.find(Mockito.any(), Mockito.eq(ParametrageCarteTP.class), Mockito.any()))
        .thenReturn(databaseParametrageCarteTP);
    Mockito.when(mongoTemplate.stream(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(Streams.of(Collections.emptyIterator()));
    Trigger tt = new Trigger();
    tt.setNbBenefToProcess(1);
    Mockito.when(mongoTemplate.findAndModify(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(tt);

    Mockito.doReturn(Collections.emptyMap())
        .when(eventService)
        .sendObservabilityEventTriggerBeneficiaryFinished(Mockito.any());
    Mockito.doReturn(Collections.emptyMap())
        .when(eventService)
        .sendObservabilityEventTriggerFinished(Mockito.any());
  }

  private List<TriggeredBeneficiary> getBenefs() {
    databaseTriggeredBeneficiary.clear();
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setAmc("1234567890");
    benef.setNumeroContrat("CONTRAT1");
    benef.setIdTrigger(triggerId);
    benef.setId("benefId");
    benef.setNewContract(createContrat());
    benef.setNir("1234567890");
    ExtendedOffreProduits op = new ExtendedOffreProduits();
    op.setCode("A");
    Produit p = new Produit();
    p.setCode("B");
    p.setPeriodeDebutDroit("20210101");
    List<Produit> ps = new ArrayList<>();
    ps.add(p);
    op.setProduits(ps);
    List<ExtendedOffreProduits> ops = new ArrayList<>();
    ops.add(op);
    benefs.add(benef);
    databaseTriggeredBeneficiary = benefs;
    return benefs;
  }

  private ServicePrestationTriggerBenef createContrat() {
    ServicePrestationTriggerBenef contrat = new ServicePrestationTriggerBenef();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode("2021-01-01", "2021-12-31");
    droitAssure.setPeriode(periode);
    contrat.setDroitsGaranties(List.of(droitAssure));
    return contrat;
  }

  // @Test
  void shouldDetectSas() {
    SasContrat sas = new SasContrat();
    sas.setIdDeclarant("1234567890");
    sas.setNumeroContrat("CONTRAT1");
    sas.setTriggersBenefs(new ArrayList<>());
    sas.setDates(new ArrayList<>());
    sas.setAnomalies(new ArrayList<>());
    sas.setId("sasId");

    List<TriggeredBeneficiary> benefs = getBenefs();

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(),
                Mockito.eq(SasContrat.class),
                Mockito.eq(Constants.SAS_CONTRAT_COLLECTION)))
        .thenReturn(sas);

    RequestTriggerProcessing requestTriggerProcessing = new RequestTriggerProcessing();
    requestTriggerProcessing.setUpdateTrigger(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE);
    requestTriggerProcessing.setIdTrigger(triggerId);
    requestTriggerProcessing.setRandomRecyclingId(RandomUtils.nextLong());
    requestTriggerProcessing.setBenefs(benefs);
    triggerUnitaireWorkerProcessingService.processTrigger(requestTriggerProcessing);
    Trigger triggerResult = triggerService.getTriggerById(triggerId);
    Assertions.assertEquals(TriggerStatus.ProcessedWithErrors, triggerResult.getStatus());
    Assertions.assertEquals(TriggeredBeneficiaryStatusEnum.Error, benefs.get(0).getStatut());
    Assertions.assertEquals(1, sas.getTriggersBenefs().size());
    Assertions.assertEquals(0, databaseDeclaration.size());
  }

  // @Test
  void shouldRecycle() {
    ContratAIV6 c = prepareContract();
    c.setNumero("CONTRAT1");
    c.setIdDeclarant("1234567890");
    c.setNumeroAdherent("123");
    c.setId("contractId");

    SasContrat sas = new SasContrat();
    sas.setIdDeclarant("1234567890");
    sas.setNumeroContrat("CONTRAT1");
    sas.setNumeroAdherent("123");
    List<TriggerBenefs> sasBenefs = new ArrayList<>();
    TriggerBenefs stb = new TriggerBenefs();
    BenefInfos benefInfos = new BenefInfos();
    benefInfos.setBenefId(getBenefs().get(0).getId());
    benefInfos.setNumeroPersonne(getBenefs().get(0).getNumeroPersonne());
    stb.setBenefsInfos(List.of(benefInfos));
    stb.setTriggerId(triggerId);
    sasBenefs.add(stb);
    sas.setTriggersBenefs(sasBenefs);
    sas.setDates(new ArrayList<>());
    sas.setAnomalies(new ArrayList<>());
    sas.setServicePrestationId(c.getId());
    sas.setId("sasId");

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(),
                Mockito.eq(SasContrat.class),
                Mockito.eq(Constants.SAS_CONTRAT_COLLECTION)))
        .thenReturn(sas);
    Mockito.when(mongoTemplate.findById("benefId", TriggeredBeneficiary.class))
        .thenReturn(databaseTriggeredBeneficiary.get(0));

    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(),
                Mockito.eq(ContratAIV6.class),
                Mockito.eq(Constants.SERVICE_PRESTATION_COLLECTION)))
        .thenReturn(c);

    Mockito.doAnswer(invocation -> null)
        .when(mongoTemplate)
        .findAndRemove(
            Mockito.any(),
            Mockito.eq(SasContrat.class),
            Mockito.eq(Constants.SAS_CONTRAT_COLLECTION));

    RequestTriggerProcessing requestTriggerProcessing = new RequestTriggerProcessing();
    requestTriggerProcessing.setUpdateTrigger(Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE);
    requestTriggerProcessing.setIdTrigger(triggerId);
    requestTriggerProcessing.setRandomRecyclingId(RandomUtils.nextLong());
    requestTriggerProcessing.setServicePrestationId(c.getId());
    triggerUnitaireWorkerRecyclingService.processTrigger(requestTriggerProcessing);

    Assertions.assertEquals(1, databaseDeclaration.size());
  }

  private ParametrageCarteTP getParametrageCarteTP(String amc) {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setAmc(amc);
    param.setStatut(ParametrageCarteTPStatut.Actif);
    param.setDateDebutValidite("2020-01-01");
    param.setId("paramId");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDateDeclenchementManuel("2001-01-01"); //
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
}
