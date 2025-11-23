package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CardRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import java.util.*;

public class CardUtils {
  public static CardRequest getValidCardRequest() {
    CardRequest request = new CardRequest();

    request.setNumeroAmc("0000401166");
    request.setDateReference("3024-01-24");
    request.setNumeroContrat("123456");

    return request;
  }

  public static List<CarteDemat> getRandomCardList(int listSize) {
    List<CarteDemat> cardList = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      cardList.add(getRandomCard());
    }

    return cardList;
  }

  public static CarteDemat getRandomCard() {

    CarteDemat card = new CarteDemat();
    int benefQuantity = 3;
    int domainQuantity = 5;

    card.setIdDeclarant(Util.getRandomString());
    card.setPeriodeDebut(getRandomDateAsString());
    card.setPeriodeFin(getRandomDateAsString());
    card.setIsLastCarteDemat(true);
    card.setAMC_contrat(Util.getRandomString());
    card.setDateCreation(new Date());
    card.setUserCreation(Util.getRandomString());
    card.setDateModification(new Date());
    card.setUserModification(Util.getRandomString());

    card.setContrat(getRandomContrat());
    card.setAdresse(getRandomAdresse());
    card.setBeneficiaires(getRandomBenefCarteDematList(benefQuantity));
    card.setDomainesConventions(getRandomDomaineConventionList(domainQuantity));

    return card;
  }

  private static Contrat getRandomContrat() {
    Contrat contrat = new Contrat();

    contrat.setNumero(Util.getRandomString());
    contrat.setDateSouscription(getRandomDateAsString());
    contrat.setDateResiliation(getRandomDateAsString());
    contrat.setType(Util.getRandomString());
    contrat.setNomPorteur(Util.getRandomString());
    contrat.setCivilitePorteur(Util.getRandomString());
    contrat.setNumeroAdherent(Util.getRandomString());
    contrat.setNumeroAdherentComplet(Util.getRandomString());
    contrat.setQualification(Util.getRandomString());
    contrat.setNumeroContratCollectif(Util.getRandomString());
    contrat.setRangAdministratif(Util.getRandomString());
    contrat.setRangAdministratif(Util.getRandomString());
    contrat.setIsContratResponsable(true);
    contrat.setIsContratCMU(true);
    contrat.setContratCMUC2S(Util.getRandomString());
    contrat.setDestinataire(Util.getRandomString());
    contrat.setIndividuelOuCollectif(Util.getRandomString());
    contrat.setSituationDebut(Util.getRandomString());
    contrat.setSituationFin(Util.getRandomString());
    contrat.setMotifFinSituation(Util.getRandomString());
    contrat.setLienFamilial(Util.getRandomString());
    contrat.setCategorieSociale(Util.getRandomString());
    contrat.setSituationParticuliere(Util.getRandomString());
    contrat.setTypeConvention(Util.getRandomString());
    contrat.setCritereSecondaireDetaille(Util.getRandomString());
    contrat.setCritereSecondaire(Util.getRandomString());
    contrat.setNumeroExterneContratIndividuel(Util.getRandomString());
    contrat.setNumeroExterneContratCollectif(Util.getRandomString());
    contrat.setModePaiementPrestations(Util.getRandomString());
    contrat.setGestionnaire(Util.getRandomString());
    contrat.setGroupeAssures(Util.getRandomString());
    contrat.setNumeroCarte(Util.getRandomString());
    contrat.setEditeurCarte(Util.getRandomString());
    contrat.setFondCarte(Util.getRandomString());
    contrat.setAnnexe1Carte(Util.getRandomString());
    contrat.setAnnexe2Carte(Util.getRandomString());
    contrat.setNumAMCEchange(Util.getRandomString());
    contrat.setNumOperateur(Util.getRandomString());
    contrat.setOrdrePriorisation(Util.getRandomString());
    contrat.setIdentifiantCollectivite(Util.getRandomString());
    contrat.setRaisonSociale(Util.getRandomString());
    contrat.setSiret(Util.getRandomString());
    contrat.setGroupePopulation(Util.getRandomString());
    contrat.setCodeRenvoi(Util.getRandomString());
    contrat.setLibelleCodeRenvoi(Util.getRandomString());

    return contrat;
  }

  private static List<Adresse> getRandomAdresseList(int adressQuantity) {
    List<Adresse> adresses = new ArrayList<>();

    for (int i = 0; i < adressQuantity; i++) {
      adresses.add(getRandomAdresse());
    }

    return adresses;
  }

  private static Adresse getRandomAdresse() {
    Adresse adresse = new Adresse();

    adresse.setLigne1(Util.getRandomString());
    adresse.setLigne2(Util.getRandomString());
    adresse.setLigne3(Util.getRandomString());
    adresse.setLigne4(Util.getRandomString());
    adresse.setLigne5(Util.getRandomString());
    adresse.setLigne6(Util.getRandomString());
    adresse.setLigne7(Util.getRandomString());
    adresse.setCodePostal(Util.getRandomString());
    adresse.setPays(Util.getRandomString());
    adresse.setEmail(Util.getRandomString());

    TypeAdresse typeAdresse = new TypeAdresse();
    typeAdresse.setLibelle(Util.getRandomString());
    typeAdresse.setType(Util.getRandomString());
    adresse.setTypeAdresse(typeAdresse);

    return adresse;
  }

  private static List<BenefCarteDemat> getRandomBenefCarteDematList(int listSize) {
    List<BenefCarteDemat> benefs = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      benefs.add(getRandomBenefCarteDemat());
    }

    return benefs;
  }

  private static BenefCarteDemat getRandomBenefCarteDemat() {
    BenefCarteDemat benef = new BenefCarteDemat();
    int domainQuantity = 6;

    benef.setLienContrat(getRandomLienContrat());
    benef.setBeneficiaire(getRandomBenef());
    benef.setDomainesCouverture(getRandomDomaineDroitList(domainQuantity));

    return benef;
  }

  private static LienContrat getRandomLienContrat() {
    LienContrat lien = new LienContrat();

    lien.setLienFamilial(Util.getRandomString());
    lien.setRangAdministratif(Util.getRandomString());
    lien.setModePaiementPrestations(Util.getRandomString());

    return lien;
  }

  private static Beneficiaire getRandomBenef() {
    Random random = new Random();
    Beneficiaire benef = new Beneficiaire();

    benef.setIdClientBO(Util.getRandomString());
    benef.setDateNaissance(getRandomDateAsString());
    benef.setRangNaissance(Util.getRandomString());
    benef.setNirBeneficiaire(Util.getRandomString());
    benef.setCleNirBeneficiaire(Util.getRandomString());
    benef.setNirOd1(Util.getRandomString());
    benef.setCleNirOd1(Util.getRandomString());
    benef.setNirOd2(Util.getRandomString());
    benef.setCleNirOd2(Util.getRandomString());
    benef.setInsc(Util.getRandomString());
    benef.setNumeroPersonne(Util.getRandomString());
    benef.setRefExternePersonne(Util.getRandomString());
    benef.setDateAdhesionMutuelle(getRandomDateAsString());
    benef.setDateDebutAdhesionIndividuelle(getRandomDateAsString());
    benef.setNumeroAdhesionIndividuelle(Util.getRandomString());
    benef.setDateRadiation(null);

    benef.setAffiliation(getRandomAffiliation());
    benef.setAdresses(getRandomAdresseList(random.nextInt(2) + 1));

    return benef;
  }

  private static Affiliation getRandomAffiliation() {
    Affiliation affiliation = new Affiliation();

    affiliation.setNom(Util.getRandomString());
    affiliation.setNomPatronymique(Util.getRandomString());
    affiliation.setNomMarital(Util.getRandomString());
    affiliation.setPrenom(Util.getRandomString());
    affiliation.setCivilite(Util.getRandomString());
    affiliation.setPeriodeDebut(getRandomDateAsString());
    affiliation.setPeriodeFin(getRandomDateAsString());
    affiliation.setQualite(Util.getRandomString());
    affiliation.setRegimeOD1(Util.getRandomString());
    affiliation.setCaisseOD1(Util.getRandomString());
    affiliation.setCentreOD1(Util.getRandomString());
    affiliation.setRegimeOD2(Util.getRandomString());
    affiliation.setCaisseOD2(Util.getRandomString());
    affiliation.setCentreOD2(Util.getRandomString());
    affiliation.setHasMedecinTraitant(true);
    affiliation.setRegimeParticulier(Util.getRandomString());
    affiliation.setIsBeneficiaireACS(true);
    affiliation.setIsTeleTransmission(true);
    affiliation.setTypeAssure(Util.getRandomString());

    return affiliation;
  }

  private static List<DomaineDroit> getRandomDomaineDroitList(int listSize) {
    List<DomaineDroit> domaines = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      domaines.add(getRandomDomaineDroit());
    }

    return domaines;
  }

  private static DomaineDroit getRandomDomaineDroit() {
    Random random = new Random();
    DomaineDroit domaine = new DomaineDroit();

    domaine.setCode(Util.getRandomString());
    domaine.setCodeExterne(Util.getRandomString());
    domaine.setLibelle(Util.getRandomString());
    domaine.setCodeExterneProduit(Util.getRandomString());
    domaine.setLibelleExterne(Util.getRandomString());
    domaine.setCodeOptionMutualiste(Util.getRandomString());
    domaine.setCodeProduit(Util.getRandomString());
    domaine.setLibelleProduit(Util.getRandomString());
    domaine.setCodeGarantie(Util.getRandomString());
    domaine.setLibelleGarantie(Util.getRandomString());
    domaine.setCodeProfil(Util.getRandomString());
    domaine.setCodeRenvoi(Util.getRandomString());
    domaine.setLibelleCodeRenvoi(Util.getRandomString());
    domaine.setTauxRemboursement(Util.getRandomString());
    domaine.setIsSuspension(false);
    domaine.setDateAdhesionCouverture(getRandomDateAsString());
    domaine.setUniteTauxRemboursement(Util.getRandomString());
    domaine.setReferenceCouverture(Util.getRandomString());
    domaine.setOrigineDroits(Util.getRandomString());
    domaine.setNoOrdreDroit(random.nextInt(15));
    domaine.setCategorie(Util.getRandomString());
    domaine.setFormulaMask(Util.getRandomString());
    domaine.setNaturePrestation(Util.getRandomString());
    domaine.setNaturePrestationOnline(Util.getRandomString());
    domaine.setCodeAssureurGarantie(Util.getRandomString());
    domaine.setCodeOffre(Util.getRandomString());
    domaine.setVersionOffre(Util.getRandomString());
    domaine.setCodeOc(Util.getRandomString());
    domaine.setIsEditable(true);
    domaine.setCodeCarence(Util.getRandomString());
    domaine.setCodeOrigine(Util.getRandomString());
    domaine.setCodeAssureurOrigine(Util.getRandomString());
    domaine.setCodeRenvoiAdditionnel(Util.getRandomString());
    domaine.setLibelleCodeRenvoiAdditionnel(Util.getRandomString());

    domaine.setPrioriteDroit(getRandomPrioriteDroit());
    domaine.setPrestations(getRandomPrestationList(random.nextInt(4)));
    domaine.setPeriodeDroit(getRandomOpenPeriodeDroit());
    domaine.setPeriodeOnline(getRandomOpenPeriodeDroit());
    domaine.setConventionnements(getRandomConventionnementList(random.nextInt(3)));
    domaine.setPeriodeProductElement(getRandomOpenPeriode());
    domaine.setPeriodeCarence(getRandomPeriodeCarence());

    return domaine;
  }

  private static PeriodeCarence getRandomPeriodeCarence() {
    PeriodeCarence periode = new PeriodeCarence();

    periode.setDebut(getRandomDateAsString());
    periode.setFin(getRandomDateAsString());

    return periode;
  }

  private static Periode getRandomOpenPeriode() {
    Periode periode = new Periode();

    periode.setDebut(getRandomDateAsString());
    periode.setFin(null);

    return periode;
  }

  private static List<Conventionnement> getRandomConventionnementList(int listSize) {
    List<Conventionnement> conventionnements = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      conventionnements.add(getRandomConventionnement());
    }

    return conventionnements;
  }

  private static Conventionnement getRandomConventionnement() {
    Random random = new Random();
    Conventionnement conventionnement = new Conventionnement();

    conventionnement.setPriorite(random.nextInt(15));

    TypeConventionnement typeConventionnement = new TypeConventionnement();
    typeConventionnement.setCode(Util.getRandomString());
    typeConventionnement.setLibelle(Util.getRandomString());
    conventionnement.setTypeConventionnement(typeConventionnement);

    return conventionnement;
  }

  private static PeriodeDroit getRandomOpenPeriodeDroit() {
    PeriodeDroit periode = new PeriodeDroit();

    periode.setPeriodeDebut(getRandomDateAsString());
    periode.setPeriodeFin(null);
    periode.setMotifEvenement(Util.getRandomString());
    periode.setLibelleEvenement(Util.getRandomString());
    periode.setModeObtention(Util.getRandomString());
    periode.setPeriodeFinInitiale(null);
    periode.setPeriodeFermetureDebut(null);
    periode.setPeriodeFermetureFin(null);

    return periode;
  }

  private static List<Prestation> getRandomPrestationList(int listSize) {
    List<Prestation> prestations = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      prestations.add(getRandomPrestation());
    }

    return prestations;
  }

  private static Prestation getRandomPrestation() {
    Prestation prestation = new Prestation();

    prestation.setCode(Util.getRandomString());
    prestation.setCodeRegroupement(Util.getRandomString());
    prestation.setLibelle(Util.getRandomString());
    prestation.setIsEditionRisqueCarte(true);
    prestation.setDateEffet(getRandomDateAsString());

    prestation.setFormule(getRandomFormule());

    FormuleMetier formuleMetier = new FormuleMetier();
    formuleMetier.setCode(Util.getRandomString());
    formuleMetier.setLibelle(Util.getRandomString());
    prestation.setFormuleMetier(formuleMetier);

    return prestation;
  }

  private static Formule getRandomFormule() {
    Random random = new Random();
    int parametreListSize = random.nextInt(6);

    Formule formule = new Formule();
    formule.setNumero(Util.getRandomString());
    formule.setLibelle(Util.getRandomString());

    List<Parametre> parametres = new ArrayList<>();

    for (int i = 0; i < parametreListSize; i++) {
      Parametre parametre = new Parametre();
      parametre.setNumero(Util.getRandomString());
      parametre.setLibelle(Util.getRandomString());
      parametre.setValeur(Util.getRandomString());
      parametres.add(parametre);
    }

    formule.setParametres(parametres);

    return formule;
  }

  private static PrioriteDroit getRandomPrioriteDroit() {
    PrioriteDroit prioriteDroit = new PrioriteDroit();

    prioriteDroit.setCode(Util.getRandomString());
    prioriteDroit.setLibelle(Util.getRandomString());
    prioriteDroit.setTypeDroit(Util.getRandomString());
    prioriteDroit.setPrioriteBO(Util.getRandomString());
    prioriteDroit.setNirPrio1(Util.getRandomString());
    prioriteDroit.setNirPrio2(Util.getRandomString());
    prioriteDroit.setPrioDroitNir1(Util.getRandomString());
    prioriteDroit.setPrioDroitNir2(Util.getRandomString());
    prioriteDroit.setPrioContratNir1(Util.getRandomString());
    prioriteDroit.setPrioContratNir2(Util.getRandomString());

    return prioriteDroit;
  }

  private static List<DomaineConvention> getRandomDomaineConventionList(int listSize) {
    List<DomaineConvention> domaines = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      domaines.add(getRandomDomaineConvention());
    }

    return domaines;
  }

  private static DomaineConvention getRandomDomaineConvention() {
    Random random = new Random();
    DomaineConvention domaine = new DomaineConvention();
    int conventionQuantity = 4;

    domaine.setCode(Util.getRandomString());
    domaine.setRang(random.nextInt(15));
    domaine.setConventions(getRandomConventionList(conventionQuantity));

    return domaine;
  }

  private static List<Convention> getRandomConventionList(int listSize) {
    List<Convention> conventions = new ArrayList<>();

    for (int i = 0; i < listSize; i++) {
      conventions.add(getRandomConvention());
    }

    return conventions;
  }

  private static Convention getRandomConvention() {
    Random random = new Random();
    Convention convention = new Convention();

    convention.setPriorite(random.nextInt(15));
    convention.setCode(Util.getRandomString());

    return convention;
  }

  private static String getRandomDateAsString() {
    Random random = new Random();
    GregorianCalendar calendar = new GregorianCalendar();

    // Randomly pick year
    int minYear = 1800;
    int maxYear = 3999;
    int randomYear = random.nextInt((maxYear - minYear) + 1) + minYear;
    calendar.set(GregorianCalendar.YEAR, randomYear);

    // Pick a random day in this year
    int maxDayOfYear = calendar.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);
    int randomDay = random.nextInt(maxDayOfYear + 1);
    calendar.set(GregorianCalendar.DAY_OF_YEAR, randomDay);

    // Format and return the result as yyyy/MM/dd
    return String.format(
        "%04d/%02d/%02d",
        calendar.get(GregorianCalendar.YEAR),
        (calendar.get(GregorianCalendar.MONTH) + 1),
        calendar.get(GregorianCalendar.DAY_OF_MONTH));
  }
}
