package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.dao.RequestParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitRemplacement;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class TriggerDataForTesting {

  public static TriggeredBeneficiary getTriggeredBenef1(String triggerId) {
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setId(triggerId);
    triggeredBeneficiary.setAmc("1234567890");

    triggeredBeneficiary.setIdTrigger(triggerId);
    triggeredBeneficiary.setParametrageCarteTPId("123");
    triggeredBeneficiary.setIsBeneficiaireACS(false);
    triggeredBeneficiary.setNir("1234567890123");
    triggeredBeneficiary.setNumeroPersonne("12");
    triggeredBeneficiary.setIsContratIndividuel(true);
    triggeredBeneficiary.setNumeroContrat("C1");
    triggeredBeneficiary.setNumeroAdherentComplet("005830210");
    triggeredBeneficiary.setCollectivite("Collect1");
    triggeredBeneficiary.setCollege("College1");
    triggeredBeneficiary.setDateNaissance("19800101");
    triggeredBeneficiary.setRangNaissance("1");
    triggeredBeneficiary.setCritereSecondaireDetaille("CSD1");
    triggeredBeneficiary.setRangAdministratif("1");
    AdresseAssure adresseAssure = new AdresseAssure();
    adresseAssure.setCodePostal("31000");
    adresseAssure.setLigne1("1 rue du test");
    triggeredBeneficiary.setAdresse(adresseAssure);

    DroitAssure droitAssure = new DroitAssure();
    droitAssure.setCode("PHAR_GAR");
    droitAssure.setCodeAssureur("LOOBA");
    droitAssure.setOrdrePriorisation("1");
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    periode1.setFin("2021-03-31");
    droitAssure.setPeriode(periode1);

    DroitAssure droitAssure2 = new DroitAssure();
    droitAssure2.setCode("PHAR_GAR2");
    droitAssure2.setCodeAssureur("LOOBA");
    DroitAssure tspd2 = new DroitAssure();
    tspd2.setCode("PHAR_GAR2");
    tspd2.setCodeAssureur("IS");
    Periode periode2 = new Periode();
    periode2.setDebut("2021-01-01");
    droitAssure2.setPeriode(periode2);

    Periode periodeAssure = new Periode();
    periodeAssure.setDebut("2021-01-01");

    ServicePrestationTriggerBenef servicePrestationTriggerBenef =
        new ServicePrestationTriggerBenef();
    servicePrestationTriggerBenef.setPeriodesDroitsCarte(new PeriodesDroitsCarte());

    triggeredBeneficiary.setNewContract(servicePrestationTriggerBenef);
    List<DroitAssure> droitAssureV3List = new ArrayList<>();
    droitAssureV3List.add(droitAssure);
    droitAssureV3List.add(droitAssure2);
    triggeredBeneficiary.getNewContract().setDroitsGaranties(droitAssureV3List);

    return triggeredBeneficiary;
  }

  public static TriggeredBeneficiary getTriggeredBenef1WithOneWarrantyWithoutEnd(String triggerId) {
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setId(triggerId);
    triggeredBeneficiary.setAmc("1234567890");

    triggeredBeneficiary.setIdTrigger(triggerId);
    triggeredBeneficiary.setParametrageCarteTPId("123");
    triggeredBeneficiary.setIsBeneficiaireACS(false);
    triggeredBeneficiary.setNir("1234567890123");
    triggeredBeneficiary.setNumeroPersonne("12");
    triggeredBeneficiary.setIsContratIndividuel(true);
    triggeredBeneficiary.setNumeroContrat("C1");
    triggeredBeneficiary.setNumeroAdherentComplet("005830210");
    triggeredBeneficiary.setCollectivite("Collect1");
    triggeredBeneficiary.setCollege("College1");
    triggeredBeneficiary.setDateNaissance("19800101");
    triggeredBeneficiary.setRangNaissance("1");
    triggeredBeneficiary.setCritereSecondaireDetaille("CSD1");
    AdresseAssure adresseAssure = new AdresseAssure();
    adresseAssure.setCodePostal("31000");
    adresseAssure.setLigne1("1 rue du test");
    triggeredBeneficiary.setAdresse(adresseAssure);

    DroitAssure droitAssure = new DroitAssure();
    droitAssure.setCode("PHAR_GAR2");
    droitAssure.setCodeAssureur("LOOBA");
    droitAssure.setOrdrePriorisation("1");
    Periode periode1 = new Periode();
    periode1.setDebut("2021-01-01");
    droitAssure.setPeriode(periode1);

    ServicePrestationTriggerBenef servicePrestationTriggerBenef =
        new ServicePrestationTriggerBenef();
    servicePrestationTriggerBenef.setPeriodesDroitsCarte(new PeriodesDroitsCarte());

    triggeredBeneficiary.setNewContract(servicePrestationTriggerBenef);
    List<DroitAssure> droitAssureV3List = new ArrayList<>();
    droitAssureV3List.add(droitAssure);
    triggeredBeneficiary.getNewContract().setDroitsGaranties(droitAssureV3List);

    return triggeredBeneficiary;
  }

  public static TriggeredBeneficiary getTriggeredBenef1WithSimpleCarence(
      String triggerId, String dateFinCarence) {
    TriggeredBeneficiary tb1 = getTriggeredBenef1(triggerId);
    CarenceDroit triggerServicePrestationCarence = new CarenceDroit();
    triggerServicePrestationCarence.setCode("CAR001");
    PeriodeCarence periode = new PeriodeCarence();
    periode.setDebut("2021-01-01");
    periode.setFin(dateFinCarence);
    triggerServicePrestationCarence.setPeriode(periode);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("GT_REMP");
    droitRemplacement.setLibelle("REMPLACEUR");
    droitRemplacement.setCodeAssureur("BALOO");
    triggerServicePrestationCarence.setDroitRemplacement(droitRemplacement);
    List<CarenceDroit> carenceDroitList = new ArrayList<>();
    carenceDroitList.add(triggerServicePrestationCarence);
    tb1.getNewContract().getDroitsGaranties().get(0).setCarences(carenceDroitList);
    tb1.getNewContract().getDroitsGaranties().get(1).setCarences(carenceDroitList);

    return tb1;
  }

  public static TriggeredBeneficiary getTriggeredBenef2() {
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setAmc("1234567890");
    triggeredBeneficiary.setIdTrigger("UUID");
    triggeredBeneficiary.setParametrageCarteTPId("123");
    triggeredBeneficiary.setNir("1234567890123");
    triggeredBeneficiary.setIsContratIndividuel(true);
    triggeredBeneficiary.setNumeroContrat("C2");
    triggeredBeneficiary.setCollectivite("Collect1");
    triggeredBeneficiary.setCollege("College1");
    triggeredBeneficiary.setDateNaissance("19800101");
    triggeredBeneficiary.setCritereSecondaireDetaille("CSD2");
    List<ExtendedOffreProduits> offersAndProducts = new ArrayList<>();
    ExtendedOffreProduits op = new ExtendedOffreProduits();
    op.setCode("O1");
    Produit p = new Produit();
    p.setCode("P1");
    op.setProduits(List.of(p));
    offersAndProducts.add(op);
    triggeredBeneficiary.setHistoriqueStatuts(new ArrayList<>());
    return triggeredBeneficiary;
  }

  public static ParametrageDroitsCarteTP getParametrageDroitsCarteTP() {
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
    detailDroit.setCodeDomaineTP("HOSP");
    detailDroit.setLibelleDomaineTP("Hospitalisation");
    detailDroit.setConvention("IS");
    detailsDroit.add(detailDroit);

    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(2);
    detailDroit.setCodeDomaineTP("EXTE");
    detailDroit.setLibelleDomaineTP("Externe");
    detailDroit.setConvention("SP");
    detailsDroit.add(detailDroit);

    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(3);
    detailDroit.setCodeDomaineTP("DENT");
    detailDroit.setLibelleDomaineTP("Dentaire");
    detailDroit.setConvention("SP");
    detailsDroit.add(detailDroit);

    parametrageDroitsCarteTP.setDetailsDroit(detailsDroit);
    return parametrageDroitsCarteTP;
  }

  public static TriggeredBeneficiary getTriggerBenef(
      String dateSous, String dateResil, String affDebut, String affFin) {
    TriggeredBeneficiary tb = new TriggeredBeneficiary();
    tb.setNewContract(new ServicePrestationTriggerBenef());
    tb.getNewContract().setPeriodesSuspension(new ArrayList<>());
    tb.getNewContract().setDateSouscription(dateSous);
    tb.getNewContract().setDateResiliation(dateResil);
    tb.setPeriodeDebutAffiliation(affDebut);
    tb.setPeriodeFinAffiliation(affFin);
    return tb;
  }

  public static TriggeredBeneficiary getTriggerBenefWithCarence(
      String dateSous, String dateResil, String affDebut, String affFin) {
    TriggeredBeneficiary tb = getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    DroitAssure triggerServicePrestationDroit = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2022-01-01");
    periode.setFin("2022-12-31");
    triggerServicePrestationDroit.setPeriode(periode);
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR01");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2022-01-01");
    periodeCarence.setFin("2022-03-31");
    carence.setPeriode(periodeCarence);
    triggerServicePrestationDroit.setCarences(List.of(carence));
    tb.getNewContract().setDroitsGaranties(List.of(triggerServicePrestationDroit));
    return tb;
  }

  public static ParametrageCarteTP getParam(
      ModeDeclenchementCarteTP mode,
      DateRenouvellementCarteTP typ,
      String declManuel,
      String echeance,
      int delai) {
    ParametrageCarteTP pc = new ParametrageCarteTP();

    ParametrageRenouvellement pr = new ParametrageRenouvellement();
    pr.setModeDeclenchement(mode);
    pr.setDateRenouvellementCarteTP(typ);
    pr.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    pr.setDateDeclenchementManuel(declManuel);
    pr.setDebutEcheance(echeance);
    pr.setDelaiDeclenchementCarteTP(delai);
    pr.setDelaiRenouvellement(delai);
    pc.setParametrageRenouvellement(pr);
    return pc;
  }

  public static ParametrageCarteTP getParamRdo(
      ModeDeclenchementCarteTP mode, DateRenouvellementCarteTP typ, String debutDroit) {
    ParametrageCarteTP pc = new ParametrageCarteTP();

    ParametrageRenouvellement pr = new ParametrageRenouvellement();
    pr.setModeDeclenchement(mode);
    pr.setDateRenouvellementCarteTP(typ);
    pr.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    pr.setDateDebutDroitTP(debutDroit);
    pr.setDelaiDeclenchementCarteTP(0);
    pc.setParametrageRenouvellement(pr);
    return pc;
  }

  public static ParametrageCarteTP getParamAnniv(
      ModeDeclenchementCarteTP mode,
      DateRenouvellementCarteTP typ,
      String dateExecBatch,
      String debutDroitsTP,
      int delai) {
    ParametrageCarteTP pc = new ParametrageCarteTP();

    ParametrageRenouvellement pr = new ParametrageRenouvellement();
    pr.setModeDeclenchement(mode);
    pr.setDateRenouvellementCarteTP(typ);
    pr.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    pr.setDateExecutionBatch(dateExecBatch);
    pr.setDateDebutDroitTP(debutDroitsTP);
    pr.setDelaiDeclenchementCarteTP(delai);
    pr.setDelaiRenouvellement(delai);
    pc.setParametrageRenouvellement(pr);
    return pc;
  }

  public static TriggeredBeneficiary getTriggeredBeneficiary(
      Trigger savedTrigger, boolean isClosed) {
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setAmc("AMC1");
    triggeredBeneficiary.setIdTrigger(savedTrigger.getId());
    triggeredBeneficiary.setParametrageCarteTPId("123");
    triggeredBeneficiary.setNir("1234567890123");
    triggeredBeneficiary.setIsContratIndividuel(true);
    triggeredBeneficiary.setNumeroContrat("C1");
    triggeredBeneficiary.setCollectivite("Collect1");
    triggeredBeneficiary.setCollege("College1");
    triggeredBeneficiary.setDateNaissance("19800101");
    triggeredBeneficiary.setCritereSecondaireDetaille("CSD1");
    List<DroitAssure> droits = new ArrayList<>();
    DroitAssure droitBase = new DroitAssure();
    droitBase.setCode("KC_PlatineBase");
    droitBase.setLibelle("KC_PlatineBase");
    droitBase.setCodeAssureur("KLESIA_CARCEPT");
    droitBase.setOrdrePriorisation("1");
    Periode periode1 = new Periode();
    periode1.setDebut("2023-01-01");
    periode1.setFin("2023-12-31");
    droitBase.setPeriode(periode1);
    droits.add(droitBase);
    DroitAssure droitOption = new DroitAssure();
    droitOption.setCode("KC_PlatineOption");
    droitOption.setLibelle("KC_PlatineOption");
    droitOption.setCodeAssureur("KLESIA_CARCEPT");
    droitOption.setOrdrePriorisation("2");
    Periode periode2 = new Periode();
    periode2.setDebut("2023-01-01");
    if (isClosed) {
      periode2.setFin("2023-10-01");
    }
    droitOption.setPeriode(periode2);
    droits.add(droitOption);
    ServicePrestationTriggerBenef triggerBenef = new ServicePrestationTriggerBenef();
    triggerBenef.setDroitsGaranties(droits);
    triggeredBeneficiary.setNewContract(triggerBenef);

    return triggeredBeneficiary;
  }

  public static ParametrageCarteTP getParametrageCarteTP() {
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    parametrageCarteTP.setAmc("AMC1");

    parametrageCarteTP.setId("0123456789");
    parametrageCarteTP.setAmcNom("AMC Test JUnit");
    parametrageCarteTP.setStatut(ParametrageCarteTPStatut.Actif);
    parametrageCarteTP.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    parametrageCarteTP.setDateDebutValidite("2020-01-01");

    parametrageCarteTP.setIdentifiantCollectivite("Collectivité Junit");
    parametrageCarteTP.setGroupePopulation("College Junit");
    parametrageCarteTP.setCritereSecondaireDetaille("Critere secondaire");

    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("ISanté");
    parametrageDroitsCarteTP.setCodeOperateurTP("IS");
    parametrageDroitsCarteTP.setIsCarteDematerialisee(false);
    parametrageDroitsCarteTP.setIsCarteEditablePapier(false);
    List<DetailDroit> detailDroitList = new ArrayList<>();
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP("AMI");
    detailDroit.setOrdreAffichage(1);
    detailDroit.setLibelleDomaineTP("Infirmier(e)");
    detailDroit.setConvention("IS");
    detailDroitList.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setCodeDomaineTP("AMM");
    detailDroit.setOrdreAffichage(2);
    detailDroit.setLibelleDomaineTP("Kinésithérapie");
    detailDroit.setConvention("IS");
    detailDroitList.add(detailDroit);
    parametrageDroitsCarteTP.setDetailsDroit(detailDroitList);
    parametrageCarteTP.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(
        DateRenouvellementCarteTP.AnniversaireContrat);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Automatique);
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    return parametrageCarteTP;
  }

  public static List<ParametrageCarteTP> getParametragesCarteTP() {
    ParametrageCarteTP paramEcheance = new ParametrageCarteTP();
    paramEcheance.setId("1");
    paramEcheance.setAmc("1234567890");
    paramEcheance.setAmcNom("AMC Test JUnit");
    paramEcheance.setStatut(ParametrageCarteTPStatut.Actif);
    paramEcheance.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    paramEcheance.setDateDebutValidite("2020-01-01");
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Automatique);
    paramEcheance.setParametrageRenouvellement(parametrageRenouvellement);

    ParametrageCarteTP paramAnniv = new ParametrageCarteTP();
    paramAnniv.setId("2");
    paramAnniv.setAmc("1234567890");
    paramAnniv.setAmcNom("AMC Test JUnit");
    paramAnniv.setStatut(ParametrageCarteTPStatut.Actif);
    paramAnniv.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    paramAnniv.setDateDebutValidite("2020-01-01");
    parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(
        DateRenouvellementCarteTP.AnniversaireContrat);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Automatique);
    paramAnniv.setParametrageRenouvellement(parametrageRenouvellement);
    return List.of(paramEcheance, paramAnniv);
  }

  public static TriggerGenerationRequest getTriggerGenerationRequest(TriggerEmitter event) {
    TriggerGenerationRequest request = new TriggerGenerationRequest();
    request.setDate("2020-01-01");
    request.setEmitter(event);
    request.setIdDeclarant("1234567890");
    request.setNumeroAdherent("123");
    request.setIndividualContractNumber("123123123");
    request.setIdParametrageCarteTP("12");
    return request;
  }

  public static List<ParametrageCarteTP> getParametrageCarteTPList() {
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    parametrageCarteTP.setId("11111111");
    List<ParametrageCarteTP> parametrageCarteTPList = new ArrayList<>();
    parametrageCarteTPList.add(parametrageCarteTP);
    return parametrageCarteTPList;
  }

  public static void mockPWTpOfflineRights(RestConnector restConnector) {
    JSONObject productWorkshopResponse = new JSONObject();
    productWorkshopResponse.put("offerCode", "Offre1");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("offerVersionCode", 1);
    jsonObject.put(Constants.PW_VALIDITY_DATE, "2021-01-01");
    jsonObject.put("engineVersion", "EngineVersion1");
    JSONArray array = new JSONArray();

    JSONArray domains = new JSONArray();
    domains.put(getDetails("DENTAIRE", "DENT"));
    domains.put(getDetails("HOSPITALISATION", "HOSP"));
    domains.put(getDetails("RADIOLOGIE", "RADI"));
    domains.put(getDetails("MEDECINE", "MEDE"));
    jsonObject.put("domains", domains);
    array.put(jsonObject);
    productWorkshopResponse.put("offerVersions", array);
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponse);
  }

  public static void mockWrongPWTpRights(RestConnector restConnector) {
    JSONObject productWorkshopResponse = new JSONObject();
    JSONArray array = new JSONArray();
    productWorkshopResponse.put("offerCode", "Offre1");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("offerVersionCode", 1);
    jsonObject.put("validityDate", "2021-01-01");
    jsonObject.put("engineVersion", "EngineVersion1");

    JSONArray variables = new JSONArray();
    JSONObject variable = new JSONObject();
    variable.put("variableCode", "$txBR.fPa_STS021_TR");
    variable.put("stsVariableNumber", 3);
    variable.put("value", "100");

    JSONObject doublevariable = new JSONObject();
    doublevariable.put("variableCode", "$txBR.fPa_STS021_TR");
    doublevariable.put("stsVariableNumber", 3);
    doublevariable.put("value", "100");
    variables.put(doublevariable);
    JSONObject detailByDomain = new JSONObject();
    detailByDomain.put("formulaCode", "fGl_STS021_TR");
    detailByDomain.put("formulaLabel", "STS021 : % TR");
    detailByDomain.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain.put("stsFormulaCode", "021");
    detailByDomain.put("natureCode", "DENTAIRE");
    detailByDomain.put("variables", variables);
    Map<String, JSONObject> details = new LinkedHashMap<>();
    details.put("PHAR", detailByDomain);

    JSONArray variables2 = new JSONArray();
    JSONObject variable2 = new JSONObject();
    variable2.put("variableCode", "$txBR.fPa_STS021_TR");
    variable2.put("stsVariableNumber", 4);
    variable2.put("value", "090");
    variables2.put(variable2);
    JSONObject detailByDomain2 = new JSONObject();
    detailByDomain2.put("formulaCode", "fGl_STS021_TR");
    detailByDomain2.put("formulaLabel", "STS021 : % TR");
    detailByDomain2.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain2.put("stsFormulaCode", "022");
    detailByDomain2.put("natureCode", "HOSPITALISATION");
    detailByDomain2.put("variables", variables2);
    details.put("DENT", detailByDomain2);

    JSONArray variables3 = new JSONArray();
    JSONObject variable3 = new JSONObject();
    variable3.put("variableCode", "$txBR.fPa_STS021_TR");
    variable3.put("stsVariableNumber", 5);
    variable3.put("value", "080");
    variables3.put(variable3);
    JSONObject detailByDomain3 = new JSONObject();
    detailByDomain3.put("formulaCode", "fGl_STS021_TR");
    detailByDomain3.put("formulaLabel", "STS021 : % TR");
    detailByDomain3.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain3.put("stsFormulaCode", "023");
    detailByDomain3.put("natureCode", "RADIOLOGIE");
    detailByDomain3.put("variables", variables3);
    details.put("CURE", detailByDomain3);

    JSONArray variables4 = new JSONArray();
    JSONObject variable4 = new JSONObject();
    variable4.put("variableCode", "$txBR.fPa_STS021_TR");
    variable4.put("stsVariableNumber", 6);
    variable4.put("value", "070");
    variables4.put(variable4);
    JSONObject detailByDomain4 = new JSONObject();
    detailByDomain4.put("formulaCode", "fGl_STS021_TR");
    detailByDomain4.put("formulaLabel", "STS021 : % TR");
    detailByDomain4.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain4.put("stsFormulaCode", "024");
    detailByDomain4.put("natureCode", "MEDECINE");
    detailByDomain4.put("variables", variables4);
    details.put("EXTE", detailByDomain4);

    jsonObject.put("detailsByDomain", (Object) details);
    array.put(jsonObject);
    productWorkshopResponse.put("offerVersions", array);
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponse);
  }

  public static void mockPWTpOfflineRightsWithReplacement(
      RestConnector restConnector, String pwOffre, String... pwReplacements) {
    try {
      JSONObject offre1 = UtilsForTesting.parseJSONFile("src/test/resources/" + pwOffre);
      List<JSONObject> replacements = new ArrayList<>();
      for (String pwReplacement : pwReplacements) {
        replacements.add(UtilsForTesting.parseJSONFile("src/test/resources/" + pwReplacement));
      }

      Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
          .thenReturn(offre1, replacements.toArray(new JSONObject[0]));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void mockPWV4TpRights(String jsonResponse, RestConnector restConnector) {
    try {
      JSONObject productWorkshopResponseV4 =
          UtilsForTesting.parseJSONFile("src/test/resources/" + jsonResponse);
      LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
      JSONArray jsonArray = (JSONArray) productWorkshopResponseV4.get("offerVersions");
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject itemArr = (JSONObject) jsonArray.get(i);
        String date =
            itemArr
                .get(Constants.PW_VALIDITY_DATE)
                .toString()
                .replaceAll("XXXX", String.valueOf(currentdate.getYear()));
        itemArr.put(Constants.PW_VALIDITY_DATE, date);
        if (itemArr.has(Constants.PW_END_VALIDITY_DATE)) {
          date =
              itemArr
                  .get(Constants.PW_END_VALIDITY_DATE)
                  .toString()
                  .replaceAll("XXXX", String.valueOf(currentdate.getYear()));
          itemArr.put(Constants.PW_END_VALIDITY_DATE, date);
        }
      }

      Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
          .thenReturn(productWorkshopResponseV4);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void mockPWTpOfflineRightsWithReplacementDentaire(RestConnector restConnector) {
    mockPWTpOfflineRightsWithReplacementDentaire(restConnector, "Offre1");
  }

  public static void mockPWTpOfflineRightsWithReplacementDentaire(
      RestConnector restConnector, String offre) {
    JSONObject productWorkshopResponse = new JSONObject();

    productWorkshopResponse.put("offerCode", offre);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("offerVersionCode", 1);
    jsonObject.put(Constants.PW_VALIDITY_DATE, "2021-01-01");
    jsonObject.put("engineVersion", "EngineVersion1");

    JSONObject productWorkshopResponseReplacement = new JSONObject();
    productWorkshopResponseReplacement.put("offerCode", "OffreReplace");
    JSONObject jsonObjectReplacement = new JSONObject();
    jsonObjectReplacement.put("offerVersionCode", 1);
    jsonObjectReplacement.put(Constants.PW_VALIDITY_DATE, "2021-01-01");
    jsonObjectReplacement.put("engineVersion", "EngineVersion1");
    JSONArray array = new JSONArray();
    JSONArray domains = putOfferVersions(jsonObject, "DENTAIRE", "DENT", array);
    productWorkshopResponse.put("offerVersions", array);
    JSONArray arrayReplacement = new JSONArray();
    jsonObjectReplacement.put("domains", domains);
    arrayReplacement.put(jsonObjectReplacement);
    productWorkshopResponseReplacement.put("offerVersions", arrayReplacement);
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponse, productWorkshopResponseReplacement);
  }

  private static JSONArray putOfferVersions(
      JSONObject jsonObject, String nature, String domaine, JSONArray array) {
    JSONArray variables = new JSONArray();
    JSONObject variable = new JSONObject();
    variable.put("variableCode", "$txBR.fPa_STS021_TR");
    variable.put("stsVariableNumber", 3);
    variable.put("value", "100");
    variables.put(variable);
    JSONObject detailByDomainoffline = new JSONObject();
    detailByDomainoffline.put("formulaCode", "fGl_STS021_TR");
    detailByDomainoffline.put("formulaLabel", "STS021 : % TR");
    detailByDomainoffline.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomainoffline.put("stsFormulaCode", "021");
    detailByDomainoffline.put("nature", nature);
    detailByDomainoffline.put("variables", variables);
    JSONObject details = new JSONObject();
    details.put("domainCode", domaine);
    details.put("tpOffline", detailByDomainoffline);
    JSONObject detailByDomainonline = new JSONObject();
    detailByDomainonline.put("nature", nature);
    details.put("tpOnline", detailByDomainonline);
    JSONArray domains = new JSONArray();
    domains.put(details);
    jsonObject.put("domains", domains);
    array.put(jsonObject);
    return domains;
  }

  private static JSONObject getDetails(String nature, String domaine) {
    JSONArray variables = new JSONArray();
    JSONObject variable = new JSONObject();
    variable.put("variableCode", "$txBR.fPa_STS021_TR");
    variable.put("stsVariableNumber", 3);
    variable.put("value", "100");
    variables.put(variable);
    JSONObject detailByDomainoffline = new JSONObject();
    detailByDomainoffline.put("formulaCode", "fGl_STS021_TR");
    detailByDomainoffline.put("formulaLabel", "STS021 : % TR");
    detailByDomainoffline.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomainoffline.put("stsFormulaCode", "021");
    detailByDomainoffline.put("nature", nature);
    detailByDomainoffline.put("variables", variables);
    JSONObject details = new JSONObject();
    details.put("domainCode", domaine);
    details.put("tpOffline", detailByDomainoffline);
    JSONObject detailByDomainonline = new JSONObject();
    detailByDomainonline.put("nature", nature);
    details.put("tpOnline", detailByDomainonline);
    return details;
  }

  public static void mockPWTpOfflineRightsReturnNull(RestConnector restConnector) {
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any())).thenReturn(null);
  }

  public static void initializeProductElementLight(ProductElementService productElementService) {
    // getOfferAndProduct
    ProductElementLight pel = new ProductElementLight();
    pel.setCodeOffer("OFFER1");
    pel.setCodeProduct("PRODUCT1");
    Mockito.when(
            productElementService.getOfferAndProduct(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(List.of(pel));
  }

  public static void mockPWTpOfflineRightsMultipleWithReplacement(RestConnector restConnector) {
    JSONObject productWorkshopResponse = new JSONObject();
    productWorkshopResponse.put("offerCode", "Offre1");
    productWorkshopResponse.put("offerVersionCode", 1);
    productWorkshopResponse.put(Constants.PW_VALIDITY_DATE, "2021-01-01");
    productWorkshopResponse.put("engineVersion", "EngineVersion1");

    JSONObject productWorkshopResponseReplacement = new JSONObject();
    productWorkshopResponseReplacement.put("offerCode", "OffreReplace");
    productWorkshopResponseReplacement.put("offerVersionCode", 1);
    productWorkshopResponseReplacement.put(Constants.PW_VALIDITY_DATE, "2021-01-01");
    productWorkshopResponseReplacement.put("engineVersion", "EngineVersion1");

    JSONArray variables = new JSONArray();
    JSONObject variable = new JSONObject();
    variable.put("variableCode", "$txBR.fPa_STS021_TR");
    variable.put("stsVariableNumber", 3);
    variable.put("value", "100");
    variables.put(variable);
    JSONObject detailByDomain = new JSONObject();
    detailByDomain.put("formulaCode", "fGl_STS021_TR");
    detailByDomain.put("formulaLabel", "STS021 : % TR");
    detailByDomain.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain.put("stsFormulaCode", "021");
    detailByDomain.put("natureCode", "DENTAIRE");
    detailByDomain.put("variables", variables);
    Map<String, JSONObject> details = new LinkedHashMap<>();
    Map<String, JSONObject> detailsReplacement = new LinkedHashMap<>();
    details.put("DENT", detailByDomain);

    JSONArray variables2 = new JSONArray();
    JSONObject variable2 = new JSONObject();
    variable2.put("variableCode", "$txBR.fPa_STS021_TR");
    variable2.put("stsVariableNumber", 4);
    variable2.put("value", "090");
    variables2.put(variable2);
    JSONObject detailByDomain2 = new JSONObject();
    detailByDomain2.put("formulaCode", "fGl_STS021_TR");
    detailByDomain2.put("formulaLabel", "STS021 : % TR");
    detailByDomain2.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain2.put("stsFormulaCode", "022");
    detailByDomain2.put("natureCode", "DENTAIRE");
    detailByDomain2.put("variables", variables2);
    details.put("APDE", detailByDomain2);

    JSONArray variables3 = new JSONArray();
    JSONObject variable3 = new JSONObject();
    variable3.put("variableCode", "$txBR.fPa_STS021_TR");
    variable3.put("stsVariableNumber", 5);
    variable3.put("value", "080");
    variables3.put(variable3);
    JSONObject detailByDomain3 = new JSONObject();
    detailByDomain3.put("formulaCode", "fGl_STS021_TR");
    detailByDomain3.put("formulaLabel", "STS021 : % TR");
    detailByDomain3.put("formulaMask", "$txBR.fPa_STS021_TR% TR");
    detailByDomain3.put("stsFormulaCode", "023");
    detailByDomain3.put("natureCode", "HOSPITALISATION");
    detailByDomain3.put("variables", variables3);
    details.put("HOSP", detailByDomain3);

    productWorkshopResponse.put("detailsByDomain", (Object) details);
    productWorkshopResponseReplacement.put("detailsByDomain", (Object) detailsReplacement);
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponse, productWorkshopResponseReplacement);
  }

  public static void checkPeriode(
      DomaineDroit domaine, TriggerTestPeriode triggerTestPeriode, String dateRestitution) {
    PeriodeDroit periodeDroit = domaine.getPeriodeDroit();
    Assertions.assertEquals(
        triggerTestPeriode.getMotifEvenement(), periodeDroit.getMotifEvenement());
    Assertions.assertEquals(triggerTestPeriode.getDateDebut(), periodeDroit.getPeriodeDebut());
    Assertions.assertEquals(triggerTestPeriode.getDateFin(), periodeDroit.getPeriodeFin());
    Assertions.assertEquals(
        triggerTestPeriode.getDateDebutFermeture(), periodeDroit.getPeriodeFermetureDebut());
    Assertions.assertEquals(
        triggerTestPeriode.getDateFinFermeture(), periodeDroit.getPeriodeFermetureFin());
    PeriodeDroit periodeOnline = domaine.getPeriodeOnline();
    Assertions.assertEquals(
        triggerTestPeriode.getDateDebutOnline(), periodeOnline.getPeriodeDebut());
    Assertions.assertEquals(triggerTestPeriode.getDateFinOnline(), periodeOnline.getPeriodeFin());
    // fin offline
    if (dateRestitution != null) {
      Assertions.assertEquals(triggerTestPeriode.getDateFinOffline(), periodeDroit.getPeriodeFin());
      // restitution carte date fin online = date fin offline
      Assertions.assertEquals(
          triggerTestPeriode.getDateFinOnline(), triggerTestPeriode.getDateFinOffline());
    } else {
      if (periodeDroit.getPeriodeFermetureFin() != null) {
        Assertions.assertEquals(
            triggerTestPeriode.getDateFinOffline(), periodeDroit.getPeriodeFermetureFin());
      } else {
        Assertions.assertEquals(
            triggerTestPeriode.getDateFinOffline(), periodeDroit.getPeriodeFin());
      }
    }
  }

  public static ContratAIV6 getContratAIV6() {
    ContratAIV6 contrat = new ContratAIV6();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    periode.setFin("2022-01-01");
    droitAssure.setPeriode(periode);
    Assure assure = new Assure();
    IdentiteContrat identite = new IdentiteContrat();
    identite.setNumeroPersonne("1");
    assure.setIdentite(identite);
    assure.setRangAdministratif("1");
    assure.setDroits(List.of(droitAssure));

    Assure assure2 = new Assure();
    assure2.setRangAdministratif("2");
    DroitAssure droitAssure2 = new DroitAssure();
    Periode periode2 = new Periode();
    periode2.setDebut("2020-01-01");
    droitAssure2.setPeriode(periode2);
    IdentiteContrat identite2 = new IdentiteContrat();
    identite2.setNumeroPersonne("2");
    assure2.setIdentite(identite2);
    assure2.setDroits(List.of(droitAssure2));

    Assure assure3 = new Assure();
    assure3.setRangAdministratif("3");
    DroitAssure droitAssure3 = new DroitAssure();
    Periode periode3 = new Periode();
    periode3.setDebut("2020-01-01");
    droitAssure3.setPeriode(periode3);
    IdentiteContrat identite3 = new IdentiteContrat();
    identite3.setNumeroPersonne("3");
    assure3.setIdentite(identite3);
    assure3.setDroits(List.of(droitAssure3));

    contrat.setAssures(List.of(assure, assure2, assure3));
    return contrat;
  }

  public static void setParametrageCarteTPServiceMock(
      ParametrageCarteTPService parametrageCarteTPService) {

    Mockito.doReturn(TriggerDataForTesting.getParametrageCarteTPList())
        .when(parametrageCarteTPService)
        .getByAmc(Mockito.any());

    ParametrageCarteTP paramCarte = getParametrageCarteTP11155511155();

    Mockito.doReturn(paramCarte)
        .when(parametrageCarteTPService)
        .getBestParametrage(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyList(),
            Mockito.any(RequestParametrageCarteTP.class));
  }

  public static ParametrageCarteTP getParametrageCarteTP11155511155() {
    ParametrageCarteTP paramCarte = new ParametrageCarteTP();
    paramCarte.setId("11155511155");
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(
        DateRenouvellementCarteTP.AnniversaireContrat);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Automatique);
    paramCarte.setParametrageRenouvellement(parametrageRenouvellement);
    return paramCarte;
  }
}
