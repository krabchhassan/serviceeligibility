package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class PeriodeTPServiceTest {

  @Autowired private PeriodeDroitTPService contractTPService;

  @Test
  void f1Cas1() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Empty contrat
    DomaineDroitContractTP domaineDroitContract = new DomaineDroitContractTP();
    domaineDroitContract.setGaranties(new ArrayList<>());

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodeDroitContractTPS.size());
    Assertions.assertEquals("2000/01/01", periodeDroitContractTPS.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodeDroitContractTPS.get(0).getPeriodeFin());
  }

  @Test
  void f1Cas2() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract = createDomainContrats("2000/01/01", "2020/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFin());
  }

  @Test
  void f1Cas3() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract = createDomainContrats("2000/01/01", "2010/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFin());
  }

  @Test
  void f1Cas4() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("2000/01/01", "2010/01/01", "2011/01/01", "2018/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFin());
  }

  @Test
  void f1Cas5() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("1998/01/01", "1999/06/01", "2011/01/01", "2019/01/02");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(2, periodeDroitContractTPS.size());
    Assertions.assertEquals("1998/01/01", periodeDroitContractTPS.get(0).getPeriodeDebut());
    Assertions.assertEquals("1999/06/01", periodeDroitContractTPS.get(0).getPeriodeFin());
    Assertions.assertEquals("2000/01/01", periodeDroitContractTPS.get(1).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodeDroitContractTPS.get(1).getPeriodeFin());
  }

  @Test
  void f1Cas6() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("1997/01/01", "1998/06/01", "1998/01/01", "1999/06/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(3, periodes.size());
    Assertions.assertEquals("1997/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("1998/06/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("1998/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("1999/06/01", periodes.get(1).getPeriodeFin());
    Assertions.assertEquals("2000/01/01", periodes.get(2).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodes.get(2).getPeriodeFin());
  }

  @Test
  void f1Cas7() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("2000/01/01", "2010/01/01", "2021/01/01", "2028/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2021/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("2028/01/01", periodes.get(1).getPeriodeFin());
  }

  @Test
  void f2Cas1() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2019/01/01", "2020/01/01");

    // Empty contrat
    DomaineDroitContractTP domaineDroitContract = new DomaineDroitContractTP();
    domaineDroitContract.setGaranties(new ArrayList<>());

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2019/01/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFinFermeture());
  }

  @Test
  void f2Cas2() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2019/01/01", "2020/01/01");

    // Contrat existant
    // DomaineDroitContractTP domaineDroitContract =
    // createDomainContrats("2000/01/01", "2020/01/01");
    DomaineDroitContractTP domaineDroitContract = new DomaineDroitContractTP();
    domaineDroitContract.setGaranties(new ArrayList<>());

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2019/01/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFinFermeture());
  }

  @Test
  void f2Cas3() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2019/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract = createDomainContrats("2000/01/01", "2010/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2019/01/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFinFermeture());
  }

  @Test
  void f2Cas4() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2019/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("2000/01/01", "2010/01/01", "2011/01/01", "2018/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2019/01/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFinFermeture());
  }

  @Test
  void f2Cas5() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2019/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("1998/01/01", "1999/06/01", "2011/01/01", "2019/01/02");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());
    Assertions.assertEquals("1998/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("1999/06/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2000/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("2019/01/02", periodes.get(1).getPeriodeFin());
    Assertions.assertEquals("2020/01/01", periodes.get(1).getPeriodeFinFermeture());
  }

  @Test
  void f2Cas6() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2019/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("1997/01/01", "1998/06/01", "1998/01/01", "1999/06/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    Assertions.assertEquals(3, periodes.size());
    Assertions.assertEquals("1997/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("1998/06/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("1998/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("1999/06/01", periodes.get(1).getPeriodeFin());
    Assertions.assertEquals("2000/01/01", periodes.get(2).getPeriodeDebut());
    Assertions.assertEquals("2019/01/01", periodes.get(2).getPeriodeFin());
    Assertions.assertEquals("2020/01/01", periodes.get(2).getPeriodeFinFermeture());
  }

  @Test
  void f3Cas1() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "1999/12/31");

    // Empty contrat
    DomaineDroitContractTP domaineDroitContract = new DomaineDroitContractTP();
    domaineDroitContract.setGaranties(new ArrayList<>());

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    Assertions.assertTrue(domaineDroitContract.getGaranties().isEmpty());
  }

  @Test
  void f3Cas2() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "1999/12/31");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract = createDomainContrats("2000/01/01", "2020/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    Assertions.assertTrue(domaineDroitContract.getGaranties().isEmpty());
  }

  @Test
  void f3Cas3() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "1999/12/31");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract = createDomainContrats("2000/01/01", "2010/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));

    Assertions.assertTrue(domaineDroitContract.getGaranties().isEmpty());
  }

  @Test
  void f3Cas4() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "1999/12/31");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("2000/01/01", "2010/01/01", "2011/01/01", "2018/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> peridoes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(1, peridoes.size());
    Assertions.assertEquals("2011/01/01", peridoes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2018/01/01", peridoes.get(0).getPeriodeFin());
  }

  @Test
  void f3Cas5() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "1999/12/31");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("1998/01/01", "1999/06/01", "2011/01/01", "2019/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodes.size());
    Assertions.assertEquals("1998/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("1999/06/01", periodes.get(0).getPeriodeFin());
    Assertions.assertEquals("2011/01/01", periodes.get(1).getPeriodeDebut());
    Assertions.assertEquals("2019/01/01", periodes.get(1).getPeriodeFin());
  }

  @Test
  void f3Cas6() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "1999/12/31");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract =
        createDomainContrats("1997/01/01", "1998/06/01", "1998/01/01", "1999/06/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(2, periodeDroitContractTPS.size());
    Assertions.assertEquals("1997/01/01", periodeDroitContractTPS.get(0).getPeriodeDebut());
    Assertions.assertEquals("1998/06/01", periodeDroitContractTPS.get(0).getPeriodeFin());
    Assertions.assertEquals("1998/01/01", periodeDroitContractTPS.get(1).getPeriodeDebut());
    Assertions.assertEquals("1999/06/01", periodeDroitContractTPS.get(1).getPeriodeFin());
  }

  @Test
  void changementNaturePrestation() {
    // Declaration
    DomaineDroitForConsolidation domaineDroitForConsolidation =
        createDomaine("2000/01/01", "2020/01/01");

    // Contrat existant
    DomaineDroitContractTP domaineDroitContract = createDomainContrats("2000/01/01", "2010/01/01");

    contractTPService.consolidatePeriods(
        domaineDroitContract, Set.of(domaineDroitForConsolidation));
    List<PeriodeDroitContractTP> periodes =
        domaineDroitContract
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();

    Assertions.assertEquals(1, periodes.size());
    Assertions.assertEquals("2000/01/01", periodes.get(0).getPeriodeDebut());
    Assertions.assertEquals("2020/01/01", periodes.get(0).getPeriodeFin());
  }

  private DomaineDroitForConsolidation createDomaine(String debut, String fin) {
    return createDomaine(debut, fin, null);
  }

  private DomaineDroitForConsolidation createDomaine(
      String debut, String fin, String finFermeture) {
    String test = "TEST";
    DomaineDroitForConsolidation domaineDroitForConsolidation = new DomaineDroitForConsolidation();

    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setTypePeriode(TypePeriode.OFFLINE);
    domaineDroitForConsolidation.setPeriodeDroitContractTP(periodeDroitContractTP);

    Garantie garantie = new Garantie();
    garantie.setCodeGarantie(test);
    garantie.setLibelleGarantie(test);
    garantie.setCodeAssureurGarantie(test);
    garantie.setProduits(new ArrayList<>());
    domaineDroitForConsolidation.setGarantie(garantie);

    Produit produit = new Produit();
    produit.setCodeProduit(test);
    produit.setLibelleProduit(test);
    produit.setCodeExterneProduit(test);
    produit.setReferencesCouverture(new ArrayList<>());
    domaineDroitForConsolidation.setProduit(produit);

    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setReferenceCouverture(test);
    referenceCouverture.setNaturesPrestation(new ArrayList<>());
    domaineDroitForConsolidation.setReferenceCouverture(referenceCouverture);

    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setNaturePrestation(test);
    naturePrestation.setPeriodesDroit(new ArrayList<>());
    domaineDroitForConsolidation.setNaturePrestation(naturePrestation);

    DomaineDroit domaineDroit = new DomaineDroit();
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut(debut);
    periodeDroit.setPeriodeFin(fin);
    periodeDroit.setPeriodeFermetureFin(finFermeture);
    domaineDroit.setPeriodeDroit(periodeDroit);
    domaineDroit.setPrioriteDroit(new PrioriteDroit());
    domaineDroit.setPrestations(Collections.emptyList());
    domaineDroit.setConventionnements(Collections.emptyList());
    domaineDroitForConsolidation.setDomaineDroit(domaineDroit);

    return domaineDroitForConsolidation;
  }

  private DomaineDroitContractTP createDomainContrats(String... dates) {
    String test = "TEST";
    DomaineDroitContractTP domaineDroitContract = new DomaineDroitContractTP();

    Garantie garantie = new Garantie();
    garantie.setCodeGarantie(test);
    garantie.setLibelleGarantie(test);
    garantie.setCodeAssureurGarantie(test);

    Produit produit = new Produit();
    produit.setCodeProduit(test);
    produit.setLibelleProduit(test);
    produit.setCodeExterneProduit(test);

    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setReferenceCouverture(test);

    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setNaturePrestation(test);
    naturePrestation.setPeriodesDroit(new ArrayList<>());

    naturePrestation.setPrioritesDroit(new ArrayList<>());
    naturePrestation.setRemboursements(new ArrayList<>());
    naturePrestation.setConventionnements(new ArrayList<>());
    naturePrestation.setPrestations(new ArrayList<>());

    for (int i = 0; i < dates.length; i = i + 2) {
      String debut = dates[i];
      String fin = dates[i + 1];
      PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
      periodeDroitContractTP.setTypePeriode(TypePeriode.OFFLINE);
      periodeDroitContractTP.setPeriodeDebut(debut);
      periodeDroitContractTP.setPeriodeFin(fin);

      naturePrestation.getPeriodesDroit().add(periodeDroitContractTP);
    }

    referenceCouverture.setNaturesPrestation(new ArrayList<>(List.of(naturePrestation)));
    produit.setReferencesCouverture(new ArrayList<>(List.of(referenceCouverture)));
    garantie.setProduits(new ArrayList<>(List.of(produit)));
    domaineDroitContract.setGaranties(new ArrayList<>(List.of(garantie)));

    return domaineDroitContract;
  }
}
