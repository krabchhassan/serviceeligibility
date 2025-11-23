package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirRattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.RattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class GenerateContract {

  public ContractTP getContrat() {
    return getContrat("2021-01-15", "2023-01-01");
  }

  public ContractTP getContrat(String start, String end) {
    final ContractTP contrat = new ContractTP();

    contrat.setIdDeclarant("0000003664");
    contrat.setNumeroContrat("1213");
    contrat.setNumeroExterneContratIndividuel("1213_EXT");
    contrat.setNumeroAdherent("ADH_1213");
    contrat.setNumeroAdherentComplet("ADH_1213COMPLET");
    contrat.setDateSouscription("2022/01/01");
    contrat.setIndividuelOuCollectif("1");
    contrat.setGestionnaire("GEST_BLUE");
    contrat.setQualification("QUAL_BLUE");
    contrat.setOrdrePriorisation("1");
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONTDB);
    contrat.setIsContratResponsable(true);
    contrat.setPeriodeResponsableOuverts(List.of(new PeriodeComparable(start, end)));
    contrat.setContratCMUC2S("1");
    PeriodeCMUOuvert perCmu = new PeriodeCMUOuvert();
    perCmu.setPeriode(new PeriodeComparable(start, end));
    perCmu.setCode("CMU");
    contrat.setPeriodeCMUOuverts(List.of(perCmu));
    contrat.setCritereSecondaire("CRITSECOND");
    contrat.setCritereSecondaireDetaille("CRITSECONDDETAIL");
    contrat.setNumeroContratCollectif("COLL");
    contrat.setNumeroExterneContratCollectif("EXTCOLL");

    final BeneficiaireContractTP beneficiaire = new BeneficiaireContractTP();
    beneficiaire.setAdresses(List.of(this.mapAdresse()));
    beneficiaire.setAffiliation(this.mapAffiliation());
    beneficiaire.setDateAdhesionMutuelle("2020/01/01");
    beneficiaire.setDateNaissance("19800605");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setDateDebutAdhesionIndividuelle("2020/02/01");
    beneficiaire.setNirBeneficiaire("1791062498047");
    beneficiaire.setCleNirBeneficiaire("45");
    beneficiaire.setNirOd1("1791062498047");
    beneficiaire.setCleNirOd1("45");
    beneficiaire.setNirOd2("2791062498047");
    beneficiaire.setCleNirOd2("44");
    beneficiaire.setInsc("INSC");
    beneficiaire.setNumeroAdhesionIndividuelle("CONTRATIND");
    beneficiaire.setNumeroPersonne("1213");
    beneficiaire.setRefExternePersonne("7209738ADF");
    beneficiaire.setRangAdministratif("RA");
    beneficiaire.setDateRadiation("9999/12/31");
    beneficiaire.setAffiliationsRO(mapAffiliationRO());
    beneficiaire.setTeletransmissions(mapTeletransmission(start, end));
    beneficiaire.setRegimesParticuliers(mapRegimesParticuliers(start, end));

    final List<DomaineDroitContractTP> listDDC = new ArrayList<>();
    listDDC.add(this.mapDomaineDroitHosp());
    listDDC.add(this.mapDomaineDroitOpti());
    beneficiaire.setDomaineDroits(listDDC);
    List<BeneficiaireContractTP> beneficiaireContractList = new ArrayList<>();
    beneficiaireContractList.add(beneficiaire);
    contrat.setBeneficiaires(beneficiaireContractList);
    return contrat;
  }

  private List<CodePeriodeDeclaration> mapRegimesParticuliers(String start, String end) {
    CodePeriodeDeclaration codePeriodeDeclaration = new CodePeriodeDeclaration();
    codePeriodeDeclaration.setCode("Special");
    codePeriodeDeclaration.setPeriode(new PeriodeComparable(start, end));
    return List.of(codePeriodeDeclaration);
  }

  private List<TeletransmissionDeclaration> mapTeletransmission(String start, String end) {
    PeriodeComparable periode = new PeriodeComparable(start, end);
    return List.of(new TeletransmissionDeclaration(periode, false));
  }

  private List<NirRattachementRODeclaration> mapAffiliationRO() {
    NirDeclaration nir1 = new NirDeclaration("1791062498047", "45");
    RattachementRODeclaration rattachementRODeclaration1 =
        new RattachementRODeclaration("03", "054", "6200");
    PeriodeComparable periode1 = new PeriodeComparable("2022-01-01", "2023-01-01");
    NirRattachementRODeclaration nirRattachementRODeclaration1 =
        new NirRattachementRODeclaration(nir1, rattachementRODeclaration1, periode1);

    NirDeclaration nir2 = new NirDeclaration("2791062498047", "44");
    RattachementRODeclaration rattachementRODeclaration2 =
        new RattachementRODeclaration("04", "044", "4200");
    PeriodeComparable periode2 = new PeriodeComparable("2022-01-01", "2023-01-01");
    NirRattachementRODeclaration nirRattachementRODeclaration2 =
        new NirRattachementRODeclaration(nir2, rattachementRODeclaration2, periode2);

    return List.of(nirRattachementRODeclaration1, nirRattachementRODeclaration2);
  }

  public ContractTP getContrat_testPriorityTpContract(
      String numeroContrat,
      String gestionnaire,
      String periodeDebut,
      String periodeFin,
      boolean isTpOnline) {
    final ContractTP contrat = new ContractTP();

    contrat.setIdDeclarant("0000003664");
    contrat.setNumeroContrat(numeroContrat);
    contrat.setNumeroExterneContratIndividuel("1213_EXT");
    contrat.setNumeroAdherent("ADH_1213");
    contrat.setNumeroAdherentComplet("ADH_1213COMPLET");
    contrat.setDateSouscription("2022/01/01");
    contrat.setIndividuelOuCollectif("1");
    contrat.setGestionnaire(gestionnaire);
    contrat.setQualification("QUAL_BLUE");
    contrat.setOrdrePriorisation("1");
    contrat.setOrigineDeclaration(Constants.ORIGINE_DECLARATIONTDB);
    contrat.setIsContratResponsable(true);
    contrat.setContratCMUC2S("1");
    contrat.setCritereSecondaire("CRITSECOND");
    contrat.setCritereSecondaireDetaille("CRITSECONDDETAIL");
    contrat.setNumeroContratCollectif("COLL");
    contrat.setNumeroExterneContratCollectif("EXTCOLL");

    final BeneficiaireContractTP beneficiaire = new BeneficiaireContractTP();
    beneficiaire.setAdresses(List.of(this.mapAdresse()));
    beneficiaire.setAffiliation(this.mapAffiliation());
    beneficiaire.setDateAdhesionMutuelle("2020/01/01");
    beneficiaire.setDateNaissance("19800605");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setDateDebutAdhesionIndividuelle("2020/02/01");
    beneficiaire.setNirBeneficiaire("1791062498047");
    beneficiaire.setCleNirBeneficiaire("45");
    beneficiaire.setNirOd1("1791062498047");
    beneficiaire.setCleNirOd1("45");
    beneficiaire.setNirOd2("2791062498047");
    beneficiaire.setCleNirOd2("44");
    beneficiaire.setInsc("INSC");
    beneficiaire.setNumeroAdhesionIndividuelle("CONTRATIND");
    beneficiaire.setNumeroPersonne("1213");
    beneficiaire.setRefExternePersonne("7209738ADF");
    beneficiaire.setRangAdministratif("RA");
    beneficiaire.setDateRadiation("9999/12/31");

    final List<DomaineDroitContractTP> listDDC = new ArrayList<>();
    listDDC.add(this.mapDomaineDroit_testPriorityTpContract(periodeDebut, periodeFin, isTpOnline));
    beneficiaire.setDomaineDroits(listDDC);
    List<BeneficiaireContractTP> beneficiaireContractList = new ArrayList<>();
    beneficiaireContractList.add(beneficiaire);
    contrat.setBeneficiaires(beneficiaireContractList);
    return contrat;
  }

  private Adresse mapAdresse() {
    final Adresse res = new Adresse();
    res.setCodePostal("codepostal");
    res.setEmail("email");
    res.setLigne1("ligne1");
    res.setLigne2("ligne2");
    res.setLigne3("ligne3");
    res.setLigne4("ligne4");
    res.setLigne5("ligne5");
    res.setLigne6("ligne6");
    res.setLigne7("ligne7");
    res.setEmail("email");
    res.setTelephone("000");
    final TypeAdresse typeAdresse = new TypeAdresse();
    typeAdresse.setType("AD");
    res.setTypeAdresse(typeAdresse);
    return res;
  }

  private Affiliation mapAffiliation() {
    final Affiliation aff = new Affiliation();
    aff.setNom("nom");
    aff.setNomPatronymique("nomPatro");
    aff.setNomMarital("nomMarital");
    aff.setPrenom("prenom");
    aff.setCivilite("M");
    aff.setPeriodeDebut("2022/01/01");
    aff.setQualite("C");
    aff.setRegimeOD1("03");
    aff.setCaisseOD1("054");
    aff.setCentreOD1("6200");

    aff.setRegimeOD2("04");
    aff.setCaisseOD2("044");
    aff.setCentreOD2("4200");

    aff.setHasMedecinTraitant(false);
    aff.setIsBeneficiaireACS(false);
    aff.setIsTeleTransmission(false);
    aff.setRegimeParticulier("Special");

    aff.setTypeAssure("Conjoint");
    return aff;
  }

  private Garantie createGarantie(
      String periodeDebut, String periodeFin, String periodeFinFermeture, boolean isTpOnline) {
    Garantie garantie = initializeGarantie("KC_PlatineComp", "KLESIA_CARCEPT");
    garantie.setDateAdhesionCouverture("2019/01/01");

    Produit produit = new Produit();
    produit.setCodeProduit("PlatineComplémentaire");
    produit.setCodeOffre("OFFER1");
    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setReferenceCouverture("toto");
    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setNaturePrestation("HOSPITALISATION");

    final List<PeriodeDroitContractTP> periodeDroitContractTPS = new ArrayList<>();
    final PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setTypePeriode(isTpOnline ? TypePeriode.ONLINE : TypePeriode.OFFLINE);
    periodeDroitContractTP.setPeriodeDebut(periodeDebut);
    periodeDroitContractTP.setPeriodeFin(periodeFin);
    periodeDroitContractTP.setPeriodeFinFermeture(periodeFinFermeture);
    naturePrestation.setPrioritesDroit(mapPrioriteDroit());

    final PeriodeDroitContractTP pdc1Online = new PeriodeDroitContractTP(periodeDroitContractTP);
    pdc1Online.setTypePeriode(TypePeriode.ONLINE);

    List<ConventionnementContrat> conventionnements = new ArrayList<>();
    ConventionnementContrat conventionnement1 = new ConventionnementContrat();
    ConventionnementContrat conventionnement2 = new ConventionnementContrat();
    TypeConventionnement typeConventionnement1 = new TypeConventionnement();
    TypeConventionnement typeConventionnement2 = new TypeConventionnement();
    typeConventionnement1.setCode("conventionCode1");
    typeConventionnement2.setCode("conventionCode2");
    conventionnement1.setPriorite(1);
    conventionnement1.setTypeConventionnement(typeConventionnement1);
    conventionnement2.setPriorite(2);
    conventionnement2.setTypeConventionnement(typeConventionnement2);
    conventionnements.add(conventionnement1);
    conventionnements.add(conventionnement2);
    naturePrestation.setConventionnements(conventionnements);
    periodeDroitContractTPS.add(periodeDroitContractTP);
    periodeDroitContractTPS.add(pdc1Online);
    naturePrestation.setPeriodesDroit(periodeDroitContractTPS);
    referenceCouverture.setNaturesPrestation(List.of(naturePrestation));
    produit.setReferencesCouverture(List.of(referenceCouverture));

    Produit produit2 = new Produit();
    produit2.setCodeOffre("OFFER2");
    produit2.setCodeProduit("PlatineComplémentaire2");
    PeriodeDroitContractTP periodeDroitContractTP2 = new PeriodeDroitContractTP();
    periodeDroitContractTP2.setTypePeriode(TypePeriode.OFFLINE);
    periodeDroitContractTP2.setPeriodeDebut("2021/01/02");
    periodeDroitContractTP2.setPeriodeFin("2023/02/01");
    periodeDroitContractTP2.setPeriodeFinFermeture("2023/02/02");
    naturePrestation.setPrioritesDroit(mapPrioriteDroit());

    PeriodeDroitContractTP periodeDroitContractTPOnline2 =
        new PeriodeDroitContractTP(periodeDroitContractTP2);
    periodeDroitContractTPOnline2.setTypePeriode(TypePeriode.ONLINE);
    final List<PeriodeDroitContractTP> periodeDroitContracts2TP = new ArrayList<>();
    periodeDroitContracts2TP.add(periodeDroitContractTP2);
    periodeDroitContracts2TP.add(periodeDroitContractTPOnline2);
    ReferenceCouverture referenceCouverture2 = new ReferenceCouverture();
    referenceCouverture2.setReferenceCouverture("toto");
    NaturePrestation naturePrestation2 = new NaturePrestation();
    naturePrestation2.setNaturePrestation("HOSPITALISATION");
    naturePrestation2.setPeriodesDroit(periodeDroitContracts2TP);
    referenceCouverture2.setNaturesPrestation(List.of(naturePrestation2));
    produit2.setReferencesCouverture(List.of(referenceCouverture2));
    garantie.setProduits(new ArrayList<>(List.of(produit, produit2)));

    return garantie;
  }

  private DomaineDroitContractTP mapDomaineDroitHosp() {
    final DomaineDroitContractTP ddc = new DomaineDroitContractTP();
    ddc.setCode("HOSP");

    ddc.setGaranties(List.of(createGarantie("2021/01/21", "2022/11/30", "2022/12/31", false)));

    return ddc;
  }

  private DomaineDroitContractTP mapDomaineDroit_testPriorityTpContract(
      String periodeDebut, String periodeFin, boolean isTpOnline) {
    final DomaineDroitContractTP ddc = new DomaineDroitContractTP();
    ddc.setCode("HOSP");

    ddc.setGaranties(List.of(createGarantie(periodeDebut, periodeFin, periodeFin, isTpOnline)));

    return ddc;
  }

  public static List<PrioriteDroitContrat> mapPrioriteDroit() {
    final PrioriteDroitContrat prioriteDroitContrat = new PrioriteDroitContrat();
    prioriteDroitContrat.setCode("01");
    prioriteDroitContrat.setLibelle("01");
    prioriteDroitContrat.setTypeDroit("01");
    prioriteDroitContrat.setPrioriteBO("01");
    List<Periode> periodes = new ArrayList<>();
    Periode periode = new Periode();
    periode.setDebut("2000/01/01");
    periodes.add(periode);
    prioriteDroitContrat.setPeriodes(periodes);
    return List.of(prioriteDroitContrat);
  }

  private DomaineDroitContractTP mapDomaineDroitOpti() {
    final DomaineDroitContractTP domaineDroitContract = new DomaineDroitContractTP();
    domaineDroitContract.setCode("OPTI");

    Garantie garantie = initializeGarantie("KC_PlatineBase", "KLESIA_CARCEPT");

    Produit produit = new Produit();
    produit.setCodeProduit("PlatineBase");
    produit.setCodeOffre("OFFER3");
    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setReferenceCouverture("toto");
    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setNaturePrestation("OPTIQUE");

    final List<PeriodeDroitContractTP> periodeDroitContractTPS = new ArrayList<>();
    final PeriodeDroitContractTP pdc1 = new PeriodeDroitContractTP();
    pdc1.setTypePeriode(TypePeriode.OFFLINE);
    pdc1.setPeriodeDebut("2021/02/01");
    pdc1.setPeriodeFin("2022/11/30");
    pdc1.setPeriodeFinFermeture("2022/12/31");
    naturePrestation.setPrioritesDroit(mapPrioriteDroit());

    final PeriodeDroitContractTP pdc1Online = new PeriodeDroitContractTP(pdc1);
    pdc1Online.setTypePeriode(TypePeriode.ONLINE);

    List<ConventionnementContrat> conventionnements = new ArrayList<>();
    ConventionnementContrat conventionnement1 = new ConventionnementContrat();
    ConventionnementContrat conventionnement2 = new ConventionnementContrat();
    TypeConventionnement typeConventionnement1 = new TypeConventionnement();
    TypeConventionnement typeConventionnement2 = new TypeConventionnement();
    typeConventionnement1.setCode("IT");
    typeConventionnement2.setCode("IS");
    conventionnement1.setPriorite(1);
    conventionnement1.setTypeConventionnement(typeConventionnement1);
    conventionnement2.setPriorite(2);
    conventionnement2.setTypeConventionnement(typeConventionnement2);
    conventionnements.add(conventionnement1);
    conventionnements.add(conventionnement2);
    naturePrestation.setConventionnements(conventionnements);

    periodeDroitContractTPS.add(pdc1);
    periodeDroitContractTPS.add(pdc1Online);
    naturePrestation.setPeriodesDroit(periodeDroitContractTPS);
    referenceCouverture.setNaturesPrestation(List.of(naturePrestation));
    produit.setReferencesCouverture(List.of(referenceCouverture));

    garantie.setProduits(List.of(produit));
    domaineDroitContract.setGaranties(List.of(garantie));
    return domaineDroitContract;
  }

  public Garantie initializeGarantie(String KC_PlatineBase, String codeAssureurGarantie) {
    Garantie garantie = new Garantie();
    garantie.setCodeGarantie(KC_PlatineBase);
    garantie.setCodeAssureurGarantie(codeAssureurGarantie);
    return garantie;
  }

  public Produit getProduit(
      String codeProduit,
      String dateDebut,
      String dateFin,
      String dateFinFermeture,
      boolean addOnline) {
    Produit produit2 = new Produit();
    produit2.setCodeProduit(codeProduit);
    ReferenceCouverture referenceCouverture2 = new ReferenceCouverture();
    referenceCouverture2.setReferenceCouverture("toto");
    NaturePrestation naturePrestation2 = new NaturePrestation();
    naturePrestation2.setNaturePrestation("DENTAIRE");

    PeriodeDroitContractTP periodeDroitContractTP2 = new PeriodeDroitContractTP();
    produit2.setCodeOffre("OFFER2");
    periodeDroitContractTP2.setPeriodeDebut(dateDebut);
    periodeDroitContractTP2.setPeriodeFin(dateFin);
    if (dateFinFermeture != null) {
      periodeDroitContractTP2.setPeriodeFinFermeture(dateFinFermeture);
    }
    produit2.setCodeOc("BABBO");
    PrioriteDroitContrat prioriteDroit = new PrioriteDroitContrat();
    prioriteDroit.setCode("1");
    naturePrestation2.setPrioritesDroit(List.of(prioriteDroit));
    periodeDroitContractTP2.setTypePeriode(TypePeriode.OFFLINE);
    List<PeriodeDroitContractTP> periodeDroitContractTPList2 = new ArrayList<>();
    periodeDroitContractTPList2.add(periodeDroitContractTP2);

    if (addOnline) {
      final PeriodeDroitContractTP periodeDroitContractTPOnline =
          new PeriodeDroitContractTP(periodeDroitContractTP2);
      periodeDroitContractTPOnline.setTypePeriode(TypePeriode.ONLINE);
      periodeDroitContractTPList2.add(periodeDroitContractTPOnline);
    }
    naturePrestation2.setPeriodesDroit(periodeDroitContractTPList2);
    referenceCouverture2.setNaturesPrestation(List.of(naturePrestation2));
    produit2.setReferencesCouverture(List.of(referenceCouverture2));
    return produit2;
  }
}
