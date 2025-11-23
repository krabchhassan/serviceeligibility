package com.cegedim.next.serviceeligibility.core.business.serviceprestation.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV5;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeContratCMUOuvert;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratCollectifV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapperServicePrestationTest {
  @Test
  void should_map_contratV6_to_V5() {
    ContratAIV5 contratV5 = getContratV5();
    ContratAIV6 contratV6 = getContratV6();
    ContratAIV5 contratV5Map = MapperServicePrestation.mapContratAIV6ToV5(contratV6);

    Assertions.assertEquals(contratV5, contratV5Map);
  }

  private ContratAIV5 getContratV5() {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ContratAIV5 contrat = new ContratAIV5();
    contrat.setIdDeclarant("0000000001");
    contrat.setSocieteEmettrice("ABC");
    contrat.setNumero("12");
    contrat.setNumeroAdherent("42");
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("MileSafe");
    contrat.setQualification("A");
    contrat.setOrdrePriorisation("1");

    Assure assure = getAssureV5();
    DataAssure data = getDataAssureV5();
    assure.setData(data);

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    contrat.setDateSouscription("2020-01-15");
    contrat.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    PeriodeContratCMUOuvert periodeContratCMUOuvert = new PeriodeContratCMUOuvert();
    periodeContratCMUOuvert.setCode("CMU");
    periodeContratCMUOuvert.setPeriode(p);
    lp.add(periodeContratCMUOuvert);
    List<Periode> lpTestErreur = new ArrayList<>();
    lpTestErreur.add(pTestErreur);
    contrat.setPeriodesContratResponsableOuvert(lpTestErreur);
    contrat.setPeriodesContratCMUOuvert(lp);

    ContratCollectif contratCollectif = new ContratCollectif();
    contratCollectif.setNumero("numContrat1");
    contrat.setContratCollectif(contratCollectif);

    return contrat;
  }

  private ContratAIV6 getContratV6() {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("0000000001");
    contrat.setSocieteEmettrice("ABC");
    contrat.setNumero("12");
    contrat.setNumeroAdherent("42");
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("MileSafe");
    contrat.setQualification("A");
    contrat.setOrdrePriorisation("1");

    Assure assure = getAssureV5FroContractV6();
    assure.getDigitRelation().getTeletransmissions().get(0).getPeriode().setDebut("2020-01-01");
    assure.getDigitRelation().getTeletransmissions().get(0).getPeriode().setFin(null);
    DataAssure data = getDataAssureV5();
    assure.setData(data);

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    contrat.setDateSouscription("2020-01-15");
    contrat.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    PeriodeContratCMUOuvert pc = new PeriodeContratCMUOuvert();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    pc.setPeriode(p);
    pc.setCode("CMU");
    lp.add(pc);

    List<Periode> lpTestErreur = new ArrayList<>();
    lpTestErreur.add(pTestErreur);
    contrat.setPeriodesContratResponsableOuvert(lpTestErreur);
    contrat.setPeriodesContratCMUOuvert(lp);

    ContratCollectifV6 contratCollectifV6 = new ContratCollectifV6();
    contratCollectifV6.setNumero("numContrat1");
    contratCollectifV6.setSiret(null);
    contratCollectifV6.setRaisonSociale(Constants.N_A);
    contratCollectifV6.setIdentifiantCollectivite(Constants.N_A);
    contratCollectifV6.setGroupePopulation(Constants.N_A);
    contrat.setContratCollectif(contratCollectifV6);

    return contrat;
  }

  private Assure getAssureV5() {
    Assure assure = new Assure();

    assure.setIsSouscripteur(true);
    assure.setRangAdministratif("A");

    IdentiteContrat id = new IdentiteContrat();
    id.setDateNaissance("19871224");

    id.setRangNaissance("1");
    id.setNumeroPersonne("36");

    Nir nir = new Nir();
    nir.setCode("1840197416357");
    nir.setCle("85");
    id.setNir(nir);

    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRO = new Nir();
    nirRO.setCode("1840197124511");
    nirRO.setCle("58");
    Periode pNirRO = new Periode();
    pNirRO.setDebut("2020-01-01");
    pNirRO.setFin("2020-06-30");
    nirRattRO.setNir(nirRO);
    nirRattRO.setPeriode(pNirRO);
    affiliationsRO.add(nirRattRO);
    id.setAffiliationsRO(affiliationsRO);

    assure.setIdentite(id);

    assure.setDateAdhesionMutuelle("2020-04-04");

    DigitRelation digit = new DigitRelation();
    Teletransmission teletransmission = new Teletransmission();
    Periode pTeletrans = new Periode();
    pTeletrans.setDebut("2020-01-01");
    teletransmission.setPeriode(pTeletrans);
    teletransmission.setIsTeletransmission(true);
    List<Teletransmission> teletrans = new ArrayList<>();
    teletrans.add(teletransmission);
    digit.setTeletransmissions(teletrans);
    assure.setDigitRelation(digit);

    List<Periode> lp = new ArrayList<>();
    Periode p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    Periode p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");

    lp.add(p1);
    lp.add(p2);

    assure.setPeriodes(lp);

    List<CodePeriode> lc = new ArrayList<>();
    CodePeriode c1 = new CodePeriode();
    c1.setCode("code1");
    p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    c1.setPeriode(p1);
    CodePeriode c2 = new CodePeriode();
    c2.setCode("code2");
    p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");
    c2.setPeriode(p2);
    lc.add(c2);
    lc.add(c1);
    assure.setRegimesParticuliers(lc);

    QualiteAssure qual = new QualiteAssure();
    qual.setCode("code");
    qual.setLibelle("libelle");
    assure.setQualite(qual);

    DroitAssure droit = new DroitAssure();
    droit.setCode("c");
    droit.setCodeAssureur("ca");
    droit.setDateAncienneteGarantie("2019-12-12");
    droit.setLibelle("l");
    droit.setOrdrePriorisation("1");

    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    droit.setPeriode(p);
    droit.setType("t");

    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("TRM");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2020-02-02");
    periodeCarence.setFin("2020-03-03");
    carence.setPeriode(periodeCarence);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("REM");
    droitRemplacement.setCodeAssureur("RAS");
    droitRemplacement.setLibelle("Droit remplacé");
    carence.setDroitRemplacement(droitRemplacement);

    carences.add(carence);
    droit.setCarences(carences);

    List<DroitAssure> ld = new ArrayList<>();
    ld.add(droit);
    assure.setDroits(ld);

    return assure;
  }

  private Assure getAssureV5FroContractV6() {
    Assure assure = new Assure();

    assure.setIsSouscripteur(true);
    assure.setRangAdministratif("A");

    IdentiteContrat id = new IdentiteContrat();
    id.setDateNaissance("19871224");

    id.setRangNaissance("1");
    id.setNumeroPersonne("36");

    Nir nir = new Nir();
    nir.setCode("1840197416357");
    nir.setCle("85");
    id.setNir(nir);

    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattRO = new NirRattachementRO();
    Nir nirRO = new Nir();
    nirRO.setCode("1840197124511");
    nirRO.setCle("58");
    Periode pNirRO = new Periode();
    pNirRO.setDebut("2020-01-01");
    pNirRO.setFin("2020-06-30");
    nirRattRO.setNir(nirRO);
    nirRattRO.setPeriode(pNirRO);
    affiliationsRO.add(nirRattRO);
    id.setAffiliationsRO(affiliationsRO);

    assure.setIdentite(id);

    assure.setDateAdhesionMutuelle("2020-04-04");

    DigitRelation digit = new DigitRelation();
    Teletransmission teletransmission = new Teletransmission();
    Periode pTeletrans = new Periode();
    pTeletrans.setDebut("2020-01-01");
    teletransmission.setPeriode(pTeletrans);
    teletransmission.setIsTeletransmission(true);
    List<Teletransmission> teletrans = new ArrayList<>();
    teletrans.add(teletransmission);
    digit.setTeletransmissions(teletrans);
    assure.setDigitRelation(digit);

    List<Periode> lp = new ArrayList<>();
    Periode p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    Periode p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");

    lp.add(p1);
    lp.add(p2);

    assure.setPeriodes(lp);

    List<CodePeriode> lc = new ArrayList<>();
    CodePeriode c1 = new CodePeriode();
    c1.setCode("code1");
    p1 = new Periode();
    p1.setDebut("2020-01-01");
    p1.setFin("2020-06-30");
    c1.setPeriode(p1);
    CodePeriode c2 = new CodePeriode();
    c2.setCode("code2");
    p2 = new Periode();
    p2.setDebut("2020-07-01");
    p2.setFin("2020-12-31");
    c2.setPeriode(p2);
    lc.add(c2);
    lc.add(c1);
    assure.setRegimesParticuliers(lc);

    QualiteAssure qual = new QualiteAssure();
    qual.setCode("code");
    qual.setLibelle("libelle");
    assure.setQualite(qual);

    DroitAssure droit = new DroitAssure();
    droit.setCode("c");
    droit.setCodeAssureur("ca");
    droit.setDateAncienneteGarantie("2019-12-12");
    droit.setLibelle("l");
    droit.setOrdrePriorisation("1");

    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    droit.setPeriode(p);
    droit.setType("t");

    List<CarenceDroit> carences = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("TRM");
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2020-02-02");
    periodeCarence.setFin("2020-03-03");
    carence.setPeriode(periodeCarence);
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("REM");
    droitRemplacement.setCodeAssureur("RAS");
    droitRemplacement.setLibelle("Droit remplacé");
    carence.setDroitRemplacement(droitRemplacement);

    carences.add(carence);
    droit.setCarences(carences);

    List<DroitAssure> ld = new ArrayList<>();
    ld.add(droit);
    assure.setDroits(ld);

    return assure;
  }

  private DataAssure getDataAssureV5() {
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
    nomDestPrest.setNomFamille("Z");
    nomDestPrest.setPrenom("L");
    nomDestPrest.setCivilite("M.");
    NomDestinataire nomDestPrest2 = new NomDestinataire();
    nomDestPrest2.setRaisonSociale("Destinataire groupe");

    DestinatairePrestations d1 = new DestinatairePrestations();
    d1.setIdDestinatairePaiements("6633663322");
    d1.setNom(nomDestPrest);
    d1.setModePaiementPrestations(m);
    d1.setPeriode(p1);
    RibAssure rib1 = new RibAssure("SOGEFRPP", "FR8130003000708267412316T42");
    d1.setRib(rib1);

    DestinatairePrestations d2 = new DestinatairePrestations();
    d2.setIdDestinatairePaiements("7788552244");
    d2.setNom(nomDestPrest2);
    d2.setModePaiementPrestations(m2);
    d2.setPeriode(p2);
    RibAssure rib2 = new RibAssure("SOGEFRPP", "FR9030003000505386195363B25");
    d2.setRib(rib2);

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
    nomDestRelevePrest.setNomFamille("Z");
    nomDestRelevePrest.setPrenom("L");
    nomDestRelevePrest.setCivilite("M.");
    dr1.setIdDestinataireRelevePrestations("42-36-DestRelevePrest-2");
    dr1.setIdBeyondDestinataireRelevePrestations("42-36-DestRelevePrest-2-0000000001");
    dr1.setNom(nomDestRelevePrest);
    dr1.setPeriode(p1);
    AdresseAssure adresseDr1 = new AdresseAssure();
    adresseDr1.setLigne1("Ligne adresse 1");
    adresseDr1.setLigne4("Ligne adresse 4");
    adresseDr1.setLigne6("09450 MA VILLE");
    dr1.setAdresse(adresseDr1);
    Dematerialisation demat = new Dematerialisation();
    demat.setIsDematerialise(false);
    dr1.setDematerialisation(demat);

    DestinataireRelevePrestations dr2 = new DestinataireRelevePrestations();
    NomDestinataire nomDestRelevePrest2 = new NomDestinataire();
    nomDestRelevePrest2.setRaisonSociale("Dest prest");
    dr2.setIdDestinataireRelevePrestations("42-36-DestRelevePrest-1");
    dr2.setIdBeyondDestinataireRelevePrestations("42-36-DestRelevePrest-1-0000000001");
    dr2.setNom(nomDestRelevePrest2);
    dr2.setPeriode(p2);
    AdresseAssure adresseDr2 = new AdresseAssure();
    adresseDr2.setLigne1("Ligne adresse 1");
    adresseDr2.setLigne4("Ligne adresse 4");
    adresseDr2.setLigne6("35150 MA VILLE");
    dr2.setAdresse(adresseDr2);
    dr2.setDematerialisation(demat);

    List<DestinataireRelevePrestations> ldr = new ArrayList<>();
    ldr.add(dr2);
    ldr.add(dr1);
    data.setDestinatairesRelevePrestations(ldr);

    return data;
  }

  @Test
  void should_map_servicePrestationV6_to_V5() {
    ServicePrestationV5 servicePrestationV5 = getServicePrestationV5();
    ServicePrestationV6 servicePrestationV6 = getSercvicePrestationV6();
    ServicePrestationV5 servicePrestationV5ToMap =
        MapperServicePrestation.mapServicePrestationV6ToV5(servicePrestationV6);

    Assertions.assertNotNull(servicePrestationV5ToMap);
    Assertions.assertEquals(servicePrestationV5.getAssure(), servicePrestationV5ToMap.getAssure());
    Assertions.assertEquals(
        servicePrestationV5.getDateResiliation(), servicePrestationV5ToMap.getDateResiliation());
    Assertions.assertEquals(
        servicePrestationV5.getDateSouscription(), servicePrestationV5ToMap.getDateSouscription());
    Assertions.assertEquals(
        servicePrestationV5.getNumeroAdherent(), servicePrestationV5ToMap.getNumeroAdherent());
    Assertions.assertEquals(
        servicePrestationV5.getNumeroAdherentComplet(),
        servicePrestationV5ToMap.getNumeroAdherentComplet());
    Assertions.assertEquals(servicePrestationV5.getNumero(), servicePrestationV5ToMap.getNumero());
    Assertions.assertEquals(
        servicePrestationV5.getContratCollectif(), servicePrestationV5ToMap.getContratCollectif());
    Assertions.assertEquals(
        servicePrestationV5.getContexteTiersPayant(),
        servicePrestationV5ToMap.getContexteTiersPayant());
    Assertions.assertEquals(
        servicePrestationV5.getPeriodesContratCMUOuvert(),
        servicePrestationV5ToMap.getPeriodesContratCMUOuvert());
    Assertions.assertEquals(
        servicePrestationV5.getPeriodesSuspension(),
        servicePrestationV5ToMap.getPeriodesSuspension());
    Assertions.assertEquals(
        servicePrestationV5.getApporteurAffaire(), servicePrestationV5ToMap.getApporteurAffaire());
    Assertions.assertEquals(
        servicePrestationV5.getGestionnaire(), servicePrestationV5ToMap.getGestionnaire());
    Assertions.assertEquals(
        servicePrestationV5.getCritereSecondaire(),
        servicePrestationV5ToMap.getCritereSecondaire());
    Assertions.assertEquals(
        servicePrestationV5.getCritereSecondaireDetaille(),
        servicePrestationV5ToMap.getCritereSecondaireDetaille());
    Assertions.assertEquals(
        servicePrestationV5.getIdDeclarant(), servicePrestationV5ToMap.getIdDeclarant());
    Assertions.assertEquals(
        servicePrestationV5.getIsContratIndividuel(),
        servicePrestationV5ToMap.getIsContratIndividuel());
  }

  private ServicePrestationV5 getServicePrestationV5() {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ServicePrestationV5 servicePrestationV5 = new ServicePrestationV5();
    servicePrestationV5.setIdDeclarant("0000000001");
    servicePrestationV5.setSocieteEmettrice("ABC");
    servicePrestationV5.setNumero("12");
    servicePrestationV5.setNumeroAdherent("42");
    servicePrestationV5.setIsContratIndividuel(true);
    servicePrestationV5.setGestionnaire("MileSafe");
    servicePrestationV5.setQualification("A");
    servicePrestationV5.setOrdrePriorisation("1");

    Assure assure = getAssureV5();
    DataAssure data = getDataAssureV5();
    assure.setData(data);

    servicePrestationV5.setAssure(assure);

    servicePrestationV5.setDateSouscription("2020-01-15");
    servicePrestationV5.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    PeriodeContratCMUOuvert periodeContratCMUOuvert = new PeriodeContratCMUOuvert();
    periodeContratCMUOuvert.setCode("CMU");
    periodeContratCMUOuvert.setPeriode(p);
    lp.add(periodeContratCMUOuvert);
    // List<Periode> lpTestErreur = new ArrayList<>();
    // lpTestErreur.add(pTestErreur);
    servicePrestationV5.setPeriodesContratResponsableOuvert(Arrays.asList(pTestErreur));
    servicePrestationV5.setPeriodesContratCMUOuvert(lp);

    ContratCollectif contratCollectif = new ContratCollectif();
    contratCollectif.setNumero("numContrat1");
    servicePrestationV5.setContratCollectif(contratCollectif);

    return servicePrestationV5;
  }

  private ServicePrestationV6 getSercvicePrestationV6() {
    Periode pTestErreur = new Periode();
    pTestErreur.setDebut("2020-02-02");
    pTestErreur.setFin("2020-03-03");

    ServicePrestationV6 servicePrestationV6 = new ServicePrestationV6();
    servicePrestationV6.setIdDeclarant("0000000001");
    servicePrestationV6.setSocieteEmettrice("ABC");
    servicePrestationV6.setNumero("12");
    servicePrestationV6.setNumeroAdherent("42");
    servicePrestationV6.setIsContratIndividuel(true);
    servicePrestationV6.setGestionnaire("MileSafe");
    servicePrestationV6.setQualification("A");
    servicePrestationV6.setOrdrePriorisation("1");

    Assure assure = getAssureV5FroContractV6();
    assure.getDigitRelation().getTeletransmissions().get(0).getPeriode().setDebut("2020-01-01");
    assure.getDigitRelation().getTeletransmissions().get(0).getPeriode().setFin(null);
    DataAssure data = getDataAssureV5();
    assure.setData(data);

    servicePrestationV6.setAssure(assure);

    servicePrestationV6.setDateSouscription("2020-01-15");
    servicePrestationV6.setDateResiliation("2020-11-15");

    List<PeriodeContratCMUOuvert> lp = new ArrayList<>();
    PeriodeContratCMUOuvert pc = new PeriodeContratCMUOuvert();
    Periode p = new Periode();
    p.setDebut("2020-02-02");
    p.setFin("2020-03-03");
    pc.setPeriode(p);
    pc.setCode("CMU");
    lp.add(pc);

    List<Periode> lpTestErreur = new ArrayList<>();
    lpTestErreur.add(pTestErreur);
    servicePrestationV6.setPeriodesContratResponsableOuvert(lpTestErreur);
    servicePrestationV6.setPeriodesContratCMUOuvert(lp);

    ContratCollectifV6 contratCollectifV6 = new ContratCollectifV6();
    contratCollectifV6.setNumero("numContrat1");
    contratCollectifV6.setSiret(null);
    contratCollectifV6.setRaisonSociale(Constants.N_A);
    contratCollectifV6.setIdentifiantCollectivite(Constants.N_A);
    contratCollectifV6.setGroupePopulation(Constants.N_A);
    servicePrestationV6.setContratCollectif(contratCollectifV6);

    return servicePrestationV6;
  }
}
