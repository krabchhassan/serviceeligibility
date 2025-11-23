package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5.InsuredDataV5;
import java.util.ArrayList;
import java.util.List;

public class TestingDataForValidationService {

  public static DataAssure getDataAssureV5ForContractV5() {
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

  public static DataAssure getDataAssureV5() {
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

  public static InsuredDataV5 getInsuredDataV5() {
    InsuredDataV5 data = new InsuredDataV5();

    Nir nir = new Nir();
    nir.setCode("2661645135232");
    nir.setCode("58");

    data.setRangNaissance("1");
    data.setDateNaissance("19660101");

    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    Nir nirRo = new Nir();
    nirRo.setCode("1791062498048");
    nirRo.setCle("44");
    NirRattachementRO rattRo = new NirRattachementRO();
    rattRo.setNir(nirRo);
    rattRo.setPeriode(new Periode("2020-12-01", null));
    rattRo.setRattachementRO(new RattachementRO("01", "595", "1236"));
    affiliationsRO.add(rattRo);
    data.setAffiliationsRO(affiliationsRO);

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
}
