package com.cegedim.next.serviceeligibility.core.services.claim;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractByBeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractWithOrdrePrio;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.NirDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.NomDto;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContexteTPV6;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TestingDataForValidationService;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ForcingRightsServiceTest {

  @Autowired private ForcingRightsService forcingRightsService;

  @Test
  void testTri1() {
    List<ContractWithOrdrePrio> contracts = new ArrayList<>();
    NirDto nir = getNirDto();
    NomDto nom = getNomDto();
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "A", "A", "", new Period("2024-01-01", null), nir, nom, true),
            ""));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "B", "B", "", new Period("2025-01-01", "2025-03-31"), nir, nom, true),
            ""));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "C", "C", "", new Period("2025-04-01", null), nir, nom, true),
            ""));
    forcingRightsService.triContracts(contracts);
    Assertions.assertEquals("C", contracts.get(0).contract().contractNumber());
    Assertions.assertEquals("B", contracts.get(1).contract().contractNumber());
    Assertions.assertEquals("A", contracts.get(2).contract().contractNumber());
  }

  @Test
  void testTri2() {
    List<ContractWithOrdrePrio> contracts = new ArrayList<>();
    NirDto nir = getNirDto();
    NomDto nom = getNomDto();
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "A", "A", "", new Period("2024-01-01", null), nir, nom, true),
            "2"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "B", "B", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    forcingRightsService.triContracts(contracts);
    Assertions.assertEquals("B", contracts.get(0).contract().contractNumber());
    Assertions.assertEquals("A", contracts.get(1).contract().contractNumber());
  }

  @Test
  void testTri3() {
    List<ContractWithOrdrePrio> contracts = new ArrayList<>();
    NirDto nir = getNirDto();
    NomDto nom = getNomDto();
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "A", "AA", "", new Period("2024-01-01", null), nir, nom, true),
            "2"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "B", "AA", "", new Period("2024-01-01", "2024-12-12"), nir, nom, true),
            "1"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "C", "CC", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    forcingRightsService.triContracts(contracts);
    Assertions.assertEquals("C", contracts.get(0).contract().contractNumber());
    Assertions.assertEquals("A", contracts.get(1).contract().contractNumber());
    Assertions.assertEquals("B", contracts.get(2).contract().contractNumber());
  }

  @Test
  void testTri4() {
    List<ContractWithOrdrePrio> contracts = new ArrayList<>();
    NirDto nir = getNirDto();
    NomDto nom = getNomDto();
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "A", "AA", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "B", "AA", "", new Period("2024-01-01", "2024-12-12"), nir, nom, true),
            "2"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "C", "CC", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    forcingRightsService.triContracts(contracts);
    Assertions.assertEquals("A", contracts.get(0).contract().contractNumber());
    Assertions.assertEquals("C", contracts.get(1).contract().contractNumber());
    Assertions.assertEquals("B", contracts.get(2).contract().contractNumber());
  }

  @Test
  void testTri5() {
    List<ContractWithOrdrePrio> contracts = new ArrayList<>();
    NirDto nir = getNirDto();
    NomDto nom = getNomDto();
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "A", "AA", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "B", "AA", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "C", "AA", "", new Period("2024-01-01", null), nir, nom, true),
            "1"));
    forcingRightsService.triContracts(contracts);
    Assertions.assertEquals("A", contracts.get(0).contract().contractNumber());
    Assertions.assertEquals("B", contracts.get(1).contract().contractNumber());
    Assertions.assertEquals("C", contracts.get(2).contract().contractNumber());
  }

  @Test
  void testTri6() {
    List<ContractWithOrdrePrio> contracts = new ArrayList<>();
    NirDto nir = getNirDto();
    NomDto nom = getNomDto();
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "A", "AA", "", new Period("2024-01-01", "2024-12-31"), nir, nom, true),
            "1"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "B", "AA", "", new Period("2024-01-01", "2024-11-30"), nir, nom, true),
            "1"));
    contracts.add(
        new ContractWithOrdrePrio(
            new ContractByBeneficiaryDto(
                "", "C", "AA", "", new Period("2024-01-01", "2024-10-30"), nir, nom, true),
            "1"));
    forcingRightsService.triContracts(contracts);
    Assertions.assertEquals("A", contracts.get(0).contract().contractNumber());
    Assertions.assertEquals("B", contracts.get(1).contract().contractNumber());
    Assertions.assertEquals("C", contracts.get(2).contract().contractNumber());
  }

  @Test
  void testPeriodesOfflines() {
    List<PeriodeDroitContractTP> periodeDroitContractTPList = new ArrayList<>();
    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP.setPeriodeFin("2024/12/31");

    PeriodeDroitContractTP periodeDroitContractTP1 = new PeriodeDroitContractTP();
    periodeDroitContractTP1.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP1.setPeriodeFin("2024/12/31");
    periodeDroitContractTP1.setPeriodeFinFermeture("2025/12/31");

    periodeDroitContractTPList.add(periodeDroitContractTP);
    periodeDroitContractTPList.add(periodeDroitContractTP1);
    List<Periode> result =
        forcingRightsService.getPeriodesOfflines(periodeDroitContractTPList, null);

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2025/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2025/12/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesOfflinesWithDateRestit() {
    List<PeriodeDroitContractTP> periodeDroitContractTPList = new ArrayList<>();
    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP.setPeriodeFin("2025/09/30");

    PeriodeDroitContractTP periodeDroitContractTP1 = new PeriodeDroitContractTP();
    periodeDroitContractTP1.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP1.setPeriodeFin("2025/07/31");
    periodeDroitContractTP1.setPeriodeFinFermeture("2025/12/31");

    periodeDroitContractTPList.add(periodeDroitContractTP);
    periodeDroitContractTPList.add(periodeDroitContractTP1);
    List<Periode> result =
        forcingRightsService.getPeriodesOfflines(periodeDroitContractTPList, "2025/08/08");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("2025/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2025/08/08", result.get(0).getFin());
    Assertions.assertEquals("2025/01/01", result.get(1).getDebut());
    Assertions.assertEquals("2025/08/08", result.get(1).getFin());
  }

  @Test
  void testPeriodesOnlines() {
    List<PeriodeDroitContractTP> periodeDroitContractTPList = new ArrayList<>();
    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP.setPeriodeFin("2024/12/31");

    PeriodeDroitContractTP periodeDroitContractTP1 = new PeriodeDroitContractTP();
    periodeDroitContractTP1.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP1.setPeriodeFin(null);

    periodeDroitContractTPList.add(periodeDroitContractTP);
    periodeDroitContractTPList.add(periodeDroitContractTP1);
    List<Periode> result = forcingRightsService.getPeriodesOnlines(periodeDroitContractTPList);

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2025/01/01", result.get(0).getDebut());
    Assertions.assertNull(result.get(0).getFin());
  }

  @Test
  void testGetPeriodeDroitContractTPList() {
    ContractTP contractTP = getContractTP(false, "1");

    List<PeriodeDroitContractTP> periodeDroitContractTPList =
        forcingRightsService.getPeriodeDroitContractTPList(
            "TP_OFFLINE", Constants.CLIENT_TYPE_INSURER, contractTP, "1");
    Assertions.assertEquals(1, periodeDroitContractTPList.size());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, periodeDroitContractTPList.get(0).getTypePeriode());

    periodeDroitContractTPList =
        forcingRightsService.getPeriodeDroitContractTPList(
            "TP_OFFLINE", Constants.CLIENT_TYPE_INSURER, contractTP, "8");
    Assertions.assertEquals(0, periodeDroitContractTPList.size());

    periodeDroitContractTPList =
        forcingRightsService.getPeriodeDroitContractTPList(
            "TP_ONLINE", Constants.CLIENT_TYPE_INSURER, contractTP, "1");
    Assertions.assertEquals(1, periodeDroitContractTPList.size());
    Assertions.assertEquals(TypePeriode.ONLINE, periodeDroitContractTPList.get(0).getTypePeriode());

    periodeDroitContractTPList =
        forcingRightsService.getPeriodeDroitContractTPList(
            "TP_ONLINE", Constants.CLIENT_TYPE_OTP, contractTP, "1");
    Assertions.assertEquals(2, periodeDroitContractTPList.size());
    periodeDroitContractTPList =
        forcingRightsService.getPeriodeDroitContractTPList(
            "TP_OFFLINE", Constants.CLIENT_TYPE_OTP, contractTP, "1");
    Assertions.assertEquals(2, periodeDroitContractTPList.size());
  }

  @Test
  void getNirHTPBenefTest() {
    NirDto nir = forcingRightsService.getNirHTP("36", this.getContratV6(true, true));
    Assertions.assertEquals("1840197416357", nir.getCode());
    Assertions.assertEquals("85", nir.getKey());
  }

  @Test
  void getNirHTPAffiliationROTest() {
    NirDto nir = forcingRightsService.getNirHTP("36", this.getContratV6(false, true));
    Assertions.assertEquals("1840197124511", nir.getCode());
    Assertions.assertEquals("58", nir.getKey());
  }

  @Test
  void getNirTPBenefTest() {
    NirDto nir = forcingRightsService.getNirTP("1", getContractTP(true, "1"));
    Assertions.assertEquals("2610225056125", nir.getCode());
    Assertions.assertEquals("89", nir.getKey());
  }

  @Test
  void getNirTPOd1Test() {
    NirDto nir = forcingRightsService.getNirTP("1", getContractTP(false, "1"));
    Assertions.assertEquals("2540797412090", nir.getCode());
    Assertions.assertEquals("48", nir.getKey());
  }

  @Test
  void getSubscriberHTPDataTest() {
    NomDto nom = forcingRightsService.getSubscriberHTP(getContratV6(true, true));
    Assertions.assertEquals("M.", nom.getCivility());
    Assertions.assertEquals("Z", nom.getLastname());
    Assertions.assertEquals("L", nom.getFirstname());
  }

  @Test
  void getSubscriberHTPNoSubscriberTest() {
    NomDto nom = forcingRightsService.getSubscriberHTP(this.getContratV6(true, false));
    Assertions.assertNull(nom.getCivility());
    Assertions.assertNull(nom.getLastname());
    Assertions.assertNull(nom.getCommonName());
    Assertions.assertNull(nom.getFirstname());
  }

  @Test
  void getSubscriberTPTest() {
    NomDto nom = forcingRightsService.getSubscriberTP(getContractTP(false, "1"));
    Assertions.assertEquals("Mme", nom.getCivility());
    Assertions.assertEquals("Loulou", nom.getLastname());
    Assertions.assertEquals("Lucian", nom.getFirstname());
  }

  @Test
  void getIsIndividualContract1TPTest() {
    boolean isIndividualContract =
        forcingRightsService.isIndividualContractTP(getContractTP(false, "1"));
    Assertions.assertEquals(true, isIndividualContract);
  }

  @Test
  void getIsIndividualContract2TPTest() {
    boolean isIndividualContract =
        forcingRightsService.isIndividualContractTP(getContractTP(false, "2"));
    Assertions.assertEquals(false, isIndividualContract);
  }

  @NotNull
  private static NirDto getNirDto() {
    NirDto nir = new NirDto();
    nir.setCode("1041062498044");
    nir.setKey("37");
    return nir;
  }

  @NotNull
  private static NomDto getNomDto() {
    NomDto nom = new NomDto();
    nom.setCivility("M");
    nom.setLastname("Loulou");
    nom.setCommonName("Lulu");
    nom.setFirstname("Lucian");
    return nom;
  }

  private ContratAIV6 getContratV6(boolean nirBenef, boolean isSubscriber) {
    ContratAIV6 contrat = new ContratAIV6();
    contrat.setIdDeclarant("0000000001");
    contrat.setSocieteEmettrice("ABC");
    contrat.setNumero("12");
    contrat.setNumeroAdherent("42");
    contrat.setIsContratIndividuel(true);
    contrat.setGestionnaire("MileSafe");
    contrat.setQualification("A");
    contrat.setOrdrePriorisation("1");

    Assure assure = getAssureV5FroContractV6(nirBenef, isSubscriber);
    DataAssure data = TestingDataForValidationService.getDataAssureV5();
    assure.setData(data);

    List<Assure> la = new ArrayList<>();
    la.add(assure);
    contrat.setAssures(la);

    contrat.setDateSouscription("2020-01-15");
    contrat.setDateResiliation("2020-11-15");

    ContexteTPV6 cont = new ContexteTPV6();
    PeriodesDroitsCarte per2 = new PeriodesDroitsCarte();
    per2.setDebut("2020-02-02");
    per2.setFin("2020-03-03");
    cont.setPeriodesDroitsCarte(per2);
    contrat.setContexteTiersPayant(cont);

    return contrat;
  }

  private Assure getAssureV5FroContractV6(boolean nirBenef, boolean isSubscriber) {
    Assure assure = new Assure();

    assure.setIsSouscripteur(isSubscriber);
    assure.setRangAdministratif("A");

    IdentiteContrat id = new IdentiteContrat();
    id.setDateNaissance("19871224");

    id.setRangNaissance("1");
    id.setNumeroPersonne("36");

    if (nirBenef) {
      Nir nir = new Nir();
      nir.setCode("1840197416357");
      nir.setCle("85");
      id.setNir(nir);
    }

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

    List<DroitAssure> ld = new ArrayList<>();
    ld.add(droit);
    assure.setDroits(ld);

    return assure;
  }

  private static ContractTP getContractTP(boolean nirBenef, String individualContract) {
    ContractTP contractTP = new ContractTP();
    BeneficiaireContractTP beneficiaire = new BeneficiaireContractTP();
    if (nirBenef) {
      beneficiaire.setNirBeneficiaire("2610225056125");
      beneficiaire.setCleNirBeneficiaire("89");
    } else {
      beneficiaire.setNirOd1("2540797412090");
      beneficiaire.setCleNirOd1("48");
    }
    beneficiaire.setNumeroPersonne("1");
    DomaineDroitContractTP domaineDroitContractTP = new DomaineDroitContractTP();
    Garantie garantie = new Garantie();
    Produit produit = new Produit();
    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    NaturePrestation naturePrestation = new NaturePrestation();

    PeriodeDroitContractTP periodeDroitContractTP1 = new PeriodeDroitContractTP();
    periodeDroitContractTP1.setTypePeriode(TypePeriode.ONLINE);
    periodeDroitContractTP1.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP1.setPeriodeFin(null);

    PeriodeDroitContractTP periodeDroitContractTP2 = new PeriodeDroitContractTP();
    periodeDroitContractTP2.setTypePeriode(TypePeriode.OFFLINE);
    periodeDroitContractTP2.setPeriodeDebut("2025/01/01");
    periodeDroitContractTP2.setPeriodeFin("2025/12/31");
    naturePrestation.setPeriodesDroit(
        Arrays.asList(periodeDroitContractTP1, periodeDroitContractTP2));
    referenceCouverture.setNaturesPrestation(List.of(naturePrestation));
    produit.setReferencesCouverture(List.of(referenceCouverture));
    garantie.setProduits(List.of(produit));
    domaineDroitContractTP.setGaranties(List.of(garantie));
    beneficiaire.setDomaineDroits(List.of(domaineDroitContractTP));
    contractTP.setBeneficiaires(List.of(beneficiaire));
    contractTP.setNomPorteur("Loulou");
    contractTP.setPrenomPorteur("Lucian");
    contractTP.setCivilitePorteur("Mme");
    contractTP.setIndividuelOuCollectif(individualContract);
    return contractTP;
  }
}
