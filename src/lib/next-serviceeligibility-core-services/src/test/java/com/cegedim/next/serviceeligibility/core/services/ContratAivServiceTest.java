package com.cegedim.next.serviceeligibility.core.services;

import static org.junit.jupiter.api.Assertions.fail;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.ParametrageCarteTPDao;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.DetailDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerRequest;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerResponse;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.EventChangeCheck;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContratAivServiceTest {

  private static final String CONTRAT = "servicePrestation";

  @Autowired private ContratAivService service;

  @Autowired private MongoTemplate template;

  @Autowired private TriggerService triggerService;

  @MockBean private DeclarantService declarantService;

  @Autowired private TriggerCreationService triggerToDeclarationService;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  public void before() {
    template.dropCollection(CONTRAT);
  }

  ContratAIV6 prepareContract() {
    Periode p = new Periode();
    p.setDebut("2021-01-01");
    p.setFin("2121-12-31");

    ContratAIV6 c = new ContratAIV6();
    c.setIdDeclarant("idDeclarant");
    c.setNumeroExterne("numero externe");

    Assure assure = new Assure();
    List<Periode> periodes = new ArrayList<>();
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    periode1.setFin("");
    Periode periode2 = new Periode();
    periode2.setDebut("2021-02-11");
    periode2.setFin("2021-03-15");
    Periode periode3 = new Periode();
    periode3.setDebut("2021-04-21");
    periode3.setFin("");
    periodes.add(periode1);
    periodes.add(periode2);
    periodes.add(periode3);
    assure.setPeriodes(periodes);
    DroitAssure droit = new DroitAssure();
    droit.setCode("PHAR");
    droit.setCodeAssureur("codeAssureur");
    droit.setPeriode(p);
    assure.setDroits(List.of(droit));
    assure.setIsSouscripteur(true);
    IdentiteContrat ic = new IdentiteContrat();
    Nir nir = new Nir();
    nir.setCode("011111111111111");
    nir.setCle("11");
    ic.setNir(nir);
    ic.setDateNaissance("01-01-1901");
    ic.setRangNaissance("1");
    ic.setNumeroPersonne("12");
    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO affiliationRO = new NirRattachementRO();
    affiliationRO.setPeriode(periode1);
    affiliationRO.setNir(nir);
    affiliationsRO.add(affiliationRO);
    ic.setAffiliationsRO(affiliationsRO);
    assure.setIdentite(ic);
    DigitRelation digitRelation = new DigitRelation();
    List<Teletransmission> teletransmissions = new ArrayList<>();
    Teletransmission teletransmission = new Teletransmission();
    teletransmission.setPeriode(periode3);
    teletransmissions.add(teletransmission);
    digitRelation.setTeletransmissions(teletransmissions);
    assure.setDigitRelation(digitRelation);
    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setNomFamille("nomFam");
    nom.setCivilite("M");
    nom.setNomUsage("nomUsa");
    nom.setPrenom("prenom");
    data.setNom(nom);
    assure.setData(data);
    List<Assure> list = new ArrayList<>();
    list.add(assure);
    c.setAssures(list);
    return c;
  }

  @Test
  void stage1_should_create_contract_and_trigger() {
    RestConnector restConnector = Mockito.mock(RestConnector.class);
    JSONObject oc1 = new JSONObject();
    oc1.put("codeOffer", "OFFER1");
    oc1.put("codeProduct", "PRODUCT1");
    JSONArray resultOC = new JSONArray();
    resultOC.put(oc1);
    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any())).thenReturn(resultOC);
    ParametrageCarteTPDao dao = Mockito.mock(ParametrageCarteTPDao.class);
    ParametrageCarteTP parametrage = getParametrage();
    Mockito.when(dao.getByAmc(Mockito.any())).thenReturn(List.of(parametrage));

    OcService ocService = Mockito.mock(OcService.class);
    ParametrageCarteTPService parametrageCarteTPService =
        Mockito.mock(ParametrageCarteTPService.class);

    Oc oc = new Oc();
    oc.setCode("OC1");
    oc.setLibelle("libelle OC1");
    Mockito.when(ocService.getOC(Mockito.anyString())).thenReturn(oc);

    ParametrageCarteTP paramCarte = TriggerDataForTesting.getParametrageCarteTP11155511155();
    Mockito.when(
            parametrageCarteTPService.getBestParametrage(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(paramCarte);

    template.dropCollection(ParametrageCarteTP.class);
    template.dropCollection(ContratAIV6.class);
    template.dropCollection(Trigger.class);
    template.dropCollection(TriggeredBeneficiary.class);
    parametrageCarteTPService.create(getParametrageCarteTP("idDeclarant"));

    List<ContratAIV6> dbContracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(0, dbContracts.size());
    ReflectionTestUtils.setField(
        triggerToDeclarationService, "paramCarteTPService", parametrageCarteTPService);

    ContratAIV6 contract = prepareContract();
    Mockito.when(template.save(Mockito.any(ContratAIV6.class))).thenReturn(contract);
    try {
      service.process(contract);
    } catch (KafkaSendingException ke) {
      fail(ke);
    }
    Mockito.when(template.findAll(ContratAIV6.class)).thenReturn(List.of(contract));
    dbContracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(1, dbContracts.size());

    int perPage = 10;
    int page = 1;
    String sortBy = null;
    String direction = null;
    TriggerRequest request = getSimpleRequest();
    TriggerResponse response =
        triggerService.getTriggers(perPage, page, sortBy, direction, request);
    Assertions.assertNotNull(response);
  }

  @Test
  void removeEmptyEndDateFromPeriodsTest() {
    List<ContratAIV6> dbContracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(0, dbContracts.size());

    ContratAIV6 contract = prepareContract();
    Mockito.when(template.save(Mockito.any(ContratAIV6.class))).thenReturn(contract);
    try {
      service.process(contract);
    } catch (KafkaSendingException ke) {
      fail(ke);
    }
    Mockito.when(template.findAll(ContratAIV6.class)).thenReturn(List.of(contract));
    dbContracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(1, dbContracts.size());
    Assure assure = dbContracts.get(0).getAssures().get(0);
    Periode periodesTeletransmission =
        assure.getDigitRelation().getTeletransmissions().get(0).getPeriode();
    Assertions.assertNull(assure.getPeriodes().get(0).getFin());
    Assertions.assertNull(assure.getPeriodes().get(2).getFin());
    Assertions.assertNull(periodesTeletransmission.getFin());
  }

  private ParametrageCarteTP getParametrage() {
    ParametrageCarteTP parametrage = new ParametrageCarteTP();
    parametrage.setId("ID1");
    parametrage.setAmc("idDeclarant");
    parametrage.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));

    return parametrage;
  }

  private TriggerRequest getSimpleRequest() {
    TriggerRequest request = new TriggerRequest();

    List<String> amcs = new ArrayList<>();
    amcs.add("idDeclarant");
    request.setAmcs(amcs);

    return request;
  }

  private ParametrageCarteTP getParametrageCarteTP(String amc) {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setAmc(amc);
    param.setIdentifiantCollectivite("COLPARAM");
    param.setGroupePopulation("COLLEGE_PARAM");
    param.setCritereSecondaireDetaille("CSD_PARAM");

    // Paramétrage de renouvellement
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
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

  @Test
  void stage1_should_modify_contract() {
    RestConnector s = Mockito.mock(RestConnector.class);
    JSONObject oc1 = new JSONObject();
    oc1.put("codeOffer", "OFFER1");
    oc1.put("codeProduct", "PRODUCT1");
    JSONArray resultOC = new JSONArray();
    resultOC.put(oc1);
    Mockito.when(s.fetchArray(Mockito.anyString(), Mockito.any())).thenReturn(null);
    Mockito.when(s.fetchArray(Mockito.anyString(), Mockito.any())).thenReturn(resultOC);
    ParametrageCarteTPDao dao = Mockito.mock(ParametrageCarteTPDao.class);
    ParametrageCarteTP parametrage = getParametrage();
    Mockito.when(dao.getByAmc(Mockito.any())).thenReturn(List.of(parametrage));

    OcService ocService = Mockito.mock(OcService.class);
    ParametrageCarteTPService parametrageCarteTPService =
        Mockito.mock(ParametrageCarteTPService.class);

    Oc oc = new Oc();
    oc.setCode("OC1");
    oc.setLibelle("libelle OC1");
    Mockito.when(ocService.getOC(Mockito.anyString())).thenReturn(oc);

    ParametrageCarteTP paramCarte = TriggerDataForTesting.getParametrageCarteTP11155511155();
    Mockito.when(
            parametrageCarteTPService.getBestParametrage(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(paramCarte);

    template.dropCollection(ParametrageCarteTP.class);
    template.dropCollection(ContratAIV6.class);
    template.dropCollection(Trigger.class);
    template.dropCollection(TriggeredBeneficiary.class);
    parametrageCarteTPService.create(getParametrageCarteTP("idDeclarant"));

    String oldNumero = "oldNumero";
    ContratAIV6 oldContract = getContratV5(false, false, false);
    oldContract.setNumeroExterne(oldNumero);
    template.save(oldContract);
    Mockito.when(template.findAll(ContratAIV6.class)).thenReturn(List.of(oldContract));
    List<ContratAIV6> dbContracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(1, dbContracts.size());
    Assertions.assertEquals(oldNumero, dbContracts.get(0).getNumeroExterne());
    ReflectionTestUtils.setField(
        triggerToDeclarationService, "paramCarteTPService", parametrageCarteTPService);

    ContratAIV6 newContract = getContratV5(true, true, false);
    Mockito.when(template.save(Mockito.any(ContratAIV6.class))).thenReturn(newContract);
    Mockito.when(template.findOne(Mockito.any(Query.class), Mockito.eq(ContratAIV6.class)))
        .thenReturn(oldContract);
    List<ContratAIV6> contracts = null;
    try {
      contracts = service.process(newContract);
    } catch (KafkaSendingException e) {
      fail(e);
    }
    Assertions.assertNotNull(contracts.get(1));

    dbContracts = template.findAll(ContratAIV6.class);
    Assertions.assertEquals(1, dbContracts.size());
    Assertions.assertEquals("123576767", dbContracts.get(0).getNumeroExterne());
  }

  @Test
  void check_new_data_test_new_contrat() {
    ContratAIV6 existingContrat = new ContratAIV6();
    ContratAIV6 newContrat = getContratV5(false, false, false);
    EventChangeCheck eventChangeCheck = service.checkNewData(existingContrat, newContrat);
    Assertions.assertTrue(eventChangeCheck.isPaymentChange());
    Assertions.assertTrue(eventChangeCheck.isBenefitChange());
  }

  @Test
  void check_new_data_test_new_assure() {
    ContratAIV6 existingContrat = getContratV5(false, false, false);
    ContratAIV6 newContrat = getContratV5(false, false, true);
    EventChangeCheck eventChangeCheck = service.checkNewData(existingContrat, newContrat);
    Assertions.assertTrue(eventChangeCheck.isPaymentChange());
    Assertions.assertTrue(eventChangeCheck.isBenefitChange());
  }

  @Test
  void check_new_data_test_change_destPaiement() {
    ContratAIV6 existingContrat = getContratV5(false, false, false);
    ContratAIV6 newContrat = getContratV5(true, false, false);
    EventChangeCheck eventChangeCheck = service.checkNewData(existingContrat, newContrat);
    Assertions.assertTrue(eventChangeCheck.isPaymentChange());
    Assertions.assertFalse(eventChangeCheck.isBenefitChange());
  }

  @Test
  void check_new_data_test_change_destRelPrest() {
    ContratAIV6 existingContrat = getContratV5(false, false, false);
    ContratAIV6 newContrat = getContratV5(false, true, false);
    EventChangeCheck eventChangeCheck = service.checkNewData(existingContrat, newContrat);
    Assertions.assertFalse(eventChangeCheck.isPaymentChange());
    Assertions.assertTrue(eventChangeCheck.isBenefitChange());
  }

  @Test
  void checkPeriodesSuspensions() {
    ContratAIV6 contract1 = getContratV5(false, false, false);
    ContratAIV6 contract2 = getContratV5(false, false, false);

    Periode p1 = new Periode();
    p1.setDebut("2021-03-01");
    p1.setFin("2021-04-30");

    Periode p2 = new Periode();
    p2.setDebut("2021-05-01");
    p2.setFin("2021-06-30");

    PeriodeSuspension ps1 = new PeriodeSuspension();
    contract1.setPeriodesSuspension(new ArrayList<>());

    PeriodeSuspension ps2 = new PeriodeSuspension();
    contract2.setPeriodesSuspension(new ArrayList<>());

    Assertions.assertFalse(service.periodesSuspensionsContratsEgales(contract1, contract2));

    contract1.getPeriodesSuspension().add(ps1);
    contract2.getPeriodesSuspension().add(ps2);

    ps1.setPeriode(p1);
    ps2.setPeriode(p1);
    Assertions.assertTrue(service.periodesSuspensionsContratsEgales(contract1, contract2));

    ps1.setPeriode(p1);
    ps2.setPeriode(p2);
    Assertions.assertFalse(service.periodesSuspensionsContratsEgales(contract1, contract2));

    ps1.setPeriode(null);
    ps2.setPeriode(p2);
    Assertions.assertFalse(service.periodesSuspensionsContratsEgales(contract1, contract2));

    ps1.setPeriode(p1);
    ps2.setPeriode(null);
    Assertions.assertFalse(service.periodesSuspensionsContratsEgales(contract1, contract2));

    ps1.setPeriode(null);
    ps2.setPeriode(null);
    Assertions.assertFalse(service.periodesSuspensionsContratsEgales(contract1, contract2));
  }

  private ContratAIV6 getContratV5(
      boolean changeDestPaiement, boolean changeDestRelPrest, boolean newAssure) {

    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("amc1");
    contrat.setNumero("contrat1");
    contrat.setNumeroExterne("123576767");

    Assure assure = getAssureV5(newAssure);
    DataAssure data = getDataAssure(changeDestPaiement, changeDestRelPrest);
    assure.setData(data);
    List<Periode> periodes = new ArrayList<>();
    Periode periode = new Periode();
    periode.setDebut("2021-02-11");
    periode.setFin("2021-03-15");
    periodes.add(periode);
    assure.setPeriodes(periodes);
    DigitRelation digitRelation = new DigitRelation();
    assure.setDigitRelation(digitRelation);
    Teletransmission teletransmission = new Teletransmission();
    teletransmission.setPeriode(periode);
    digitRelation.setTeletransmissions(List.of(teletransmission));
    assure.setDigitRelation(digitRelation);
    DroitAssure droit = new DroitAssure();
    droit.setCode("PHAR");
    droit.setCodeAssureur("codeAssureur");
    droit.setPeriode(periode);
    assure.setDroits(List.of(droit));

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    return contrat;
  }

  private Assure getAssureV5(boolean newAssure) {
    Assure assure = new Assure();
    IdentiteContrat id = new IdentiteContrat();
    id.setDateNaissance(newAssure ? "19971230" : "19871224");

    id.setRangNaissance("1");
    id.setNumeroPersonne(newAssure ? "37" : "36");

    Nir nir = new Nir();
    nir.setCode(newAssure ? "1970197416392" : "1840197416357");
    nir.setCle("85");
    id.setNir(nir);

    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRO = new Nir();
    nirRO.setCode(newAssure ? "1970197416392" : "1840197124511");
    nirRO.setCle("58");
    Periode pNirRO = new Periode();
    pNirRO.setDebut("2020-01-01");
    pNirRO.setFin("2020-06-30");
    nirRattRO.setNir(nirRO);
    nirRattRO.setPeriode(pNirRO);
    affiliationsRO.add(nirRattRO);
    id.setAffiliationsRO(affiliationsRO);

    assure.setIdentite(id);

    assure.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));

    return assure;
  }

  private DataAssure getDataAssure(boolean changeDestPaiement, boolean changeDestRelPrest) {
    DataAssure data = new DataAssure();
    NomAssure nom = new NomAssure();
    nom.setNomFamille("Z");
    nom.setPrenom("L");
    nom.setCivilite("M.");
    data.setNom(nom);
    AdresseAssure adr = new AdresseAssure();
    adr.setLigne1("ici");
    data.setAdresse(adr);

    PeriodeDestinataire p1 = new PeriodeDestinataire();
    p1.setDebut("2020-02-02");
    p1.setFin("2020-03-03");
    PeriodeDestinataire p2 = new PeriodeDestinataire();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");

    ModePaiement m = new ModePaiement();
    m.setCode("C");
    m.setLibelle("L");
    m.setCodeMonnaie("Y");

    ModePaiement m2 = new ModePaiement();
    m2.setCode("C");
    m2.setLibelle("L");
    m2.setCodeMonnaie("Y");

    NomDestinataire nomDestPrest = new NomDestinataire();
    nomDestPrest.setNomFamille(changeDestPaiement ? "A" : "Z");
    nomDestPrest.setPrenom("L");
    nomDestPrest.setCivilite("M.");
    NomDestinataire nomDestPrest2 = new NomDestinataire();
    nomDestPrest2.setNomFamille(changeDestPaiement ? "A" : "Z");
    nomDestPrest2.setPrenom("L");
    nomDestPrest2.setCivilite("M.");

    DestinatairePrestations d1 = new DestinatairePrestations();
    d1.setNom(nomDestPrest);
    d1.setModePaiementPrestations(m);
    d1.setPeriode(p1);
    RibAssure rib1 = new RibAssure("SOGEFRPP", "FR8130003000708267412316T42");
    d1.setRib(rib1);
    d1.setIdDestinatairePaiements("42-36-DestPaiement-2");
    d1.setIdBeyondDestinatairePaiements("42-36-DestPaiement-2-0000000001");

    DestinatairePrestations d2 = new DestinatairePrestations();
    d2.setNom(nomDestPrest2);
    d2.setModePaiementPrestations(m2);
    d2.setPeriode(p2);
    RibAssure rib2 = new RibAssure("SOGEFRPP", "FR9030003000505386195363B25");
    d2.setRib(rib2);
    d2.setIdDestinatairePaiements("42-36-DestPaiement-1");
    d2.setIdBeyondDestinatairePaiements("42-36-DestPaiement-1-0000000001");

    List<DestinatairePrestations> ld = new ArrayList<>();
    ld.add(d2);
    ld.add(d1);
    data.setDestinatairesPaiements(ld);

    DestinataireRelevePrestations dr1 = new DestinataireRelevePrestations();
    p1 = new PeriodeDestinataire();
    p1.setDebut("2020-02-02");
    p1.setFin("2020-03-03");
    p2 = new PeriodeDestinataire();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");
    NomDestinataire nomDestRelevePrest = new NomDestinataire();
    nomDestRelevePrest.setNomFamille(changeDestRelPrest ? "A" : "Z");
    nomDestRelevePrest.setPrenom("L");
    nomDestRelevePrest.setCivilite("M.");
    dr1.setNom(nomDestRelevePrest);
    dr1.setPeriode(p1);
    AdresseAssure adresseDr1 = new AdresseAssure();
    adresseDr1.setLigne1("Ligne adresse 1");
    adresseDr1.setLigne4("Ligne adresse 4");
    adresseDr1.setLigne6("09450 MA VILLE");
    dr1.setAdresse(adresseDr1);
    dr1.setIdDestinataireRelevePrestations("42-36-DestRelevePrest-2");
    dr1.setIdBeyondDestinataireRelevePrestations("42-36-DestRelevePrest-2-0000000001");
    Dematerialisation demat = new Dematerialisation();
    demat.setIsDematerialise(false);
    dr1.setDematerialisation(demat);

    DestinataireRelevePrestations dr2 = new DestinataireRelevePrestations();
    NomDestinataire nomDestRelevePrest2 = new NomDestinataire();
    nomDestRelevePrest2.setNomFamille(changeDestRelPrest ? "A" : "Z");
    nomDestRelevePrest2.setPrenom("L");
    nomDestRelevePrest2.setCivilite("M.");
    dr2.setNom(nomDestRelevePrest2);
    dr2.setPeriode(p2);
    AdresseAssure adresseDr2 = new AdresseAssure();
    adresseDr2.setLigne1("Ligne adresse 1");
    adresseDr2.setLigne4("Ligne adresse 4");
    adresseDr2.setLigne6("35150 MA VILLE");
    dr2.setAdresse(adresseDr2);
    dr2.setIdDestinataireRelevePrestations("42-36-DestRelevePrest-1");
    dr2.setIdBeyondDestinataireRelevePrestations("42-36-DestRelevePrest-1-0000000001");
    dr2.setDematerialisation(demat);

    List<DestinataireRelevePrestations> ldr = new ArrayList<>();
    ldr.add(dr2);
    ldr.add(dr1);
    data.setDestinatairesRelevePrestations(ldr);

    return data;
  }
}
