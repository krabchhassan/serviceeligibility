package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.kafka.AdresseAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.ContratCollectif;
import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.QualiteAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.RattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContexteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.utils.CarenceDifference;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CarenceDifferenceTest {
  public static final String OPTI = "OPTI";

  @Test
  void checkRemoveCarenceTest() {
    ContratAIV5 oldContract = getContratWithCarence(OPTI, "2020-03-31", false, 3);
    ContratAIV5 newContract = getContratWithCarence(OPTI, "2020-03-31", false, 2);
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasDifferentCarencesCarences = carenceDifference.checkHasDifferentCarences();
    Assertions.assertTrue(hasDifferentCarencesCarences);
  }

  @Test
  void checkHasDifferentCarencesTest() {
    ContratAIV5 oldContract = getContrat();
    ContratAIV5 newContract = getContratWithCarence(OPTI, "2020-03-31", false, 1);
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasDifferentCarencesCarences = carenceDifference.checkHasDifferentCarences();
    Assertions.assertTrue(hasDifferentCarencesCarences);
  }

  @Test
  void addNewCarenceOnExistingRightTest() {
    ContratAIV5 oldContract = getContrat();
    ContratAIV5 newContract = getContratWithCarence(OPTI, "2020-03-31", false, 1);
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasNewCarences = carenceDifference.hasNewCarences();
    Assertions.assertTrue(hasNewCarences);
  }

  @Test
  void changeCarencesPeriodTest() {
    ContratAIV5 oldContract = getContratWithCarence(OPTI, "2020-03-31", false, 1);
    ContratAIV5 newContract = getContratWithCarence(OPTI, "2020-02-28", false, 1);
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasChangedCarences = carenceDifference.hasChangedCarences();
    Assertions.assertTrue(hasChangedCarences);
  }

  @Test
  void deleteCarencesTest() {
    ContratAIV5 oldContract = getContratWithCarence(OPTI, "2020-03-31", false, 1);
    ContratAIV5 newContract = getContrat();
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasDeletedCarences = carenceDifference.hasDeletedCarences();
    Assertions.assertTrue(hasDeletedCarences);
  }

  @Test
  void replaceCarencesTest() {
    ContratAIV5 oldContract = getContratWithCarence("OPTI1", "2020-03-31", false, 1);
    ContratAIV5 newContract = getContratWithCarence("OPTI2", "2020-02-28", false, 1);
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasChangedCarences = carenceDifference.hasChangedCarences();
    Assertions.assertTrue(hasChangedCarences);
  }

  @Test
  void deleteRightWithCarenceTest() {
    ContratAIV5 oldContract = getContratWithCarence("OPTI1", "2020-03-31", true, 1);
    ContratAIV5 newContract = getContrat();
    CarenceDifference carenceDifference = new CarenceDifference(oldContract, newContract);
    boolean hasDeletedCarences = carenceDifference.hasDeletedCarences();
    Assertions.assertTrue(hasDeletedCarences);
  }

  private ContratAIV5 getContrat() {
    ContratAIV5 contrat = new ContratAIV5();
    contrat.setIdDeclarant("1234567890");
    contrat.setNumero("123123123");
    contrat.setCritereSecondaireDetaille("crt_junit");
    contrat.setContexteTiersPayant(getContexteTP());
    contrat.setContratCollectif(getContratCollectif());
    Assure assure = getAssure(0, null, null, false);
    contrat.setAssures(List.of(assure));
    contrat.setDateResiliation("9999-12-31");
    contrat.setDateSouscription("2021-06-01");
    return contrat;
  }

  private ContratAIV5 getContratWithCarence(
      String carenceCode, String periodeFin, boolean rightToDelete, int nbCarences) {
    ContratAIV5 contrat = new ContratAIV5();
    contrat.setIdDeclarant("1234567890");
    contrat.setNumero("123123123");
    contrat.setCritereSecondaireDetaille("crt_junit");
    contrat.setContexteTiersPayant(getContexteTP());
    contrat.setContratCollectif(getContratCollectif());
    Assure assure = getAssure(nbCarences, carenceCode, periodeFin, rightToDelete);
    contrat.setAssures(List.of(assure));
    contrat.setDateResiliation("9999-12-31");
    contrat.setDateSouscription("2021-06-01");
    return contrat;
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

  private ContexteTP getContexteTP() {
    ContexteTP contexteTiersPayant = new ContexteTP();
    contexteTiersPayant.setCollectivite("Collectivté Junit");
    contexteTiersPayant.setCollege("College Junit");
    return contexteTiersPayant;
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
    dest.setModePaiementPrestations(new ModePaiement("JUN", "Junit", "JJ"));
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

  private ContratCollectif getContratCollectif() {
    ContratCollectif cc = new ContratCollectif();
    cc.setNumero("CC1");
    cc.setNumeroExterne("CCE1");
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
      int nbCarence, String carenceCode, String periodeFin, boolean rightToDelete) {
    List<DroitAssure> droits = new ArrayList<>();
    DroitAssure droit = new DroitAssure();
    if (rightToDelete) {
      droit.setCode("OPTI1");
    } else {
      droit.setCode(OPTI);
    }
    droit.setCodeAssureur("CODE_ASSUREUR");
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    droit.setPeriode(periode);
    if (nbCarence != 0) {
      List<CarenceDroit> carenceList = new ArrayList<>();
      for (int i = 0; i < nbCarence; i++) {
        carenceList.add(getCarence(carenceCode + i, periodeFin));
      }
      droit.setCarences(carenceList);
    }
    droits.add(droit);
    droit = new DroitAssure();
    droit.setCode("CODE2");
    droit.setCodeAssureur("CODE_ASSUREUR2");
    droit.setPeriode(periode);
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
      int nbCarence, String carenceCode, String periodeFin, boolean rightToDelete) {
    Assure assure = new Assure();
    assure.setQualite(new QualiteAssure("A", "Assureur"));
    DataAssure data = getDataAssure();
    data.setDestinatairesPaiements(getDestinatairePaiements());
    assure.setData(data);
    assure.setIdentite(getIdentiteContrat());
    assure.setDroits(getDroitAssure(nbCarence, carenceCode, periodeFin, rightToDelete));
    assure.setIsSouscripteur(true);
    assure.setDateRadiation("9999-12-31");
    assure.setPeriodesMedecinTraitantOuvert(getPeriodesMedecin());
    assure.setRegimesParticuliers(getRegimesParticuliers());
    assure.setSituationsParticulieres(getSituationsParticulieres());
    assure.setDigitRelation(getDigitRelation());
    return assure;
  }

  private DataAssure getDataAssure() {
    DataAssure data = new DataAssure();
    data.setNom(getNomAssure());
    data.setAdresse(getAdresseAssure());
    return data;
  }

  static CarenceDroit getCarence(String carenceCode, String periodeFin) {
    CarenceDroit carence = new CarenceDroit();
    carence.setCode(carenceCode);
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2020-01-01");
    periodeCarence.setFin(periodeFin);
    carence.setPeriode(periodeCarence);
    carence.setDroitRemplacement(getDroitRemplacement());
    return carence;
  }

  private static DroitRemplacement getDroitRemplacement() {
    DroitRemplacement droitRemplacement = new DroitRemplacement();
    droitRemplacement.setCode("DROIT_REMP");
    droitRemplacement.setLibelle("LIBELLE");
    droitRemplacement.setCodeAssureur("CODE_ASSUREUR");
    return droitRemplacement;
  }
}
