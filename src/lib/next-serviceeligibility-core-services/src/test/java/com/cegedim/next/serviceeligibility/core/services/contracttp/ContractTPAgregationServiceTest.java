package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.ContractTPMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.MailleDomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.ContractTPMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.MailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.ContractTPMailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.MailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.ContractTPMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.MailleReferenceCouverture;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractTPAgregationServiceTest {

  @Autowired private GenerateContract contract;

  @Autowired private ContractTPAgregationService service;

  /**
   * 1 contrat avec 2 naturesPrestation (contigues) Période 1ère naturesPrestation :
   * 2021/01/21-2022/11/30 Période 2nde naturesPrestation : 2022/12/01-2023/03/31
   */
  @Test
  void test_agregationMailleRefCouverture_periodesContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");

    ContractTPMailleRefCouv contractTPMailleRefCouv =
        service.agregationMailleReferenceCouverture(contrat);
    MailleReferenceCouverture mailleReferenceCouverture =
        contractTPMailleRefCouv
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0);
    // Départ : 2 périodes OFFLINE et 2 ONLINE (sur des natures prestations
    // différentes)
    // Resultat : 1 période OFFLINE et 1 ONLINE
    Assertions.assertEquals(2, mailleReferenceCouverture.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleReferenceCouverture.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleReferenceCouverture.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/03/31", mailleReferenceCouverture.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertNull(
        mailleReferenceCouverture.getPeriodesDroit().get(0).getPeriodeFinFermeture());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleReferenceCouverture.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleReferenceCouverture.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/03/31", mailleReferenceCouverture.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertNull(
        mailleReferenceCouverture.getPeriodesDroit().get(1).getPeriodeFinFermeture());
  }

  /**
   * 1 contrat avec 2 naturesPrestation (non-contigues) Période 1ère naturesPrestation :
   * 2021/01/21-2022/11/30 Période 2nde naturesPrestation : 2022/12/25-2023/03/31
   */
  @Test
  void test_agregationMailleRefCouverture_periodesNonContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/25");

    ContractTPMailleRefCouv contractTPMailleRefCouv =
        service.agregationMailleReferenceCouverture(contrat);
    MailleReferenceCouverture mailleReferenceCouverture =
        contractTPMailleRefCouv
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0)
            .getReferencesCouverture()
            .get(0);
    // Départ : 2 périodes OFFLINE et 2 ONLINE (sur des natures prestations
    // différentes)
    // Resultat : 2 périodes OFFLINE et 2 ONLINE
    Assertions.assertEquals(3, mailleReferenceCouverture.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleReferenceCouverture.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleReferenceCouverture.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals(
        "2022/11/30", mailleReferenceCouverture.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertEquals(
        "2022/12/31", mailleReferenceCouverture.getPeriodesDroit().get(0).getPeriodeFinFermeture());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleReferenceCouverture.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2022/12/25", mailleReferenceCouverture.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/03/31", mailleReferenceCouverture.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertNull(
        mailleReferenceCouverture.getPeriodesDroit().get(1).getPeriodeFinFermeture());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleReferenceCouverture.getPeriodesDroit().get(2).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleReferenceCouverture.getPeriodesDroit().get(2).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/03/31", mailleReferenceCouverture.getPeriodesDroit().get(2).getPeriodeFin());
    Assertions.assertNull(
        mailleReferenceCouverture.getPeriodesDroit().get(2).getPeriodeFinFermeture());
  }

  /**
   * 1 contrat avec 2 referencesCouverture (contigues) Périodes 1ère referenceCouverture :
   * 2021/01/21-2022/11/30, 2022/12/01-2023/03/31 Périodes 2nde referenceCouverture :
   * 2023/04/01-2023/05/31, 2023/06/01-2023/06/30
   */
  @Test
  void test_agregationMailleProduit_periodesContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");
    // Création de 2 références de couverture pour un produit
    Produit produit =
        contrat
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0);
    ReferenceCouverture ref = produit.getReferencesCouverture().get(0);
    ReferenceCouverture ref2 = new ReferenceCouverture(ref);
    ref2.setReferenceCouverture("tata");
    PeriodeDroitContractTP p =
        createPeriodesDroitsContractTP(
            "2023/04/01", "2023/05/31", "2023/05/31", TypePeriode.OFFLINE);
    PeriodeDroitContractTP p1 =
        createPeriodesDroitsContractTP(
            "2023/04/01", "2023/05/31", "2023/05/31", TypePeriode.ONLINE);
    PeriodeDroitContractTP p2 =
        createPeriodesDroitsContractTP(
            "2023/06/01", "2023/06/30", "2023/07/31", TypePeriode.OFFLINE);
    PeriodeDroitContractTP p3 =
        createPeriodesDroitsContractTP(
            "2023/06/01", "2023/06/30", "2023/07/31", TypePeriode.ONLINE);
    PeriodeDroitContractTP p4 =
        createPeriodesDroitsContractTP("2023/07/01", "2023/08/31", null, TypePeriode.OFFLINE);
    PeriodeDroitContractTP p5 =
        createPeriodesDroitsContractTP("2023/07/01", "2023/08/31", null, TypePeriode.ONLINE);
    ref2.getNaturesPrestation().get(0).setPeriodesDroit(Arrays.asList(p, p1));
    ref2.getNaturesPrestation().get(1).setPeriodesDroit(Arrays.asList(p2, p3, p4, p5));
    ConventionnementContrat c = createConventionnement("conventionCode1", 1);
    ConventionnementContrat c2 = createConventionnement("conventionCode2", 2);
    c.setPeriodes(
        Arrays.asList(
            new Periode("2023/04/01", "2023/04/20"), new Periode("2023/05/01", "2023/05/20")));
    c2.setPeriodes(
        Arrays.asList(
            new Periode("2023/04/21", "2023/04/30"), new Periode("2023/05/21", "2023/05/31")));
    ref2.getNaturesPrestation().get(0).setConventionnements(Arrays.asList(c, c2));
    ConventionnementContrat c3 = createConventionnement("conventionCode1", 1);
    ConventionnementContrat c4 = createConventionnement("conventionCode2", 2);
    c3.setPeriodes(Arrays.asList(new Periode("2023/07/01", "2023/08/31")));
    c4.setPeriodes(Arrays.asList(new Periode("2023/06/01", "2023/06/30")));
    ref2.getNaturesPrestation().get(1).setConventionnements(Arrays.asList(c3, c4));
    produit.setReferencesCouverture(Arrays.asList(ref, ref2));

    ContractTPMailleProduit contractTPMailleProduit = service.agregationMailleProduit(contrat);
    MailleProduit mailleProduit =
        contractTPMailleProduit
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0);
    Assertions.assertEquals(2, mailleProduit.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleProduit.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleProduit.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals("2023/08/31", mailleProduit.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertNull(mailleProduit.getPeriodesDroit().get(0).getPeriodeFinFermeture());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleProduit.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleProduit.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals("2023/08/31", mailleProduit.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertNull(mailleProduit.getPeriodesDroit().get(1).getPeriodeFinFermeture());

    Assertions.assertEquals(2, mailleProduit.getConventionnements().size());
    Assertions.assertEquals(
        "2022/05/01", mailleProduit.getConventionnements().get(1).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/11/30", mailleProduit.getConventionnements().get(1).getPeriodes().get(0).getFin());
    Assertions.assertEquals(
        "2023/04/21", mailleProduit.getConventionnements().get(1).getPeriodes().get(1).getDebut());
    Assertions.assertEquals(
        "2023/04/30", mailleProduit.getConventionnements().get(1).getPeriodes().get(1).getFin());
    Assertions.assertEquals(
        "2023/05/21", mailleProduit.getConventionnements().get(1).getPeriodes().get(2).getDebut());
    Assertions.assertEquals(
        "2023/06/30", mailleProduit.getConventionnements().get(1).getPeriodes().get(2).getFin());
  }

  /**
   * 1 contrat avec 2 referencesCouverture (non-contigues) Périodes 1ère referenceCouverture :
   * 2021/01/21-2022/11/30, 2022/12/01-2023/03/31 Périodes 2nde referenceCouverture :
   * 2023/04/01-2023/04/25, 2023/06/01-2023/06/30
   */
  @Test
  void test_agregationMailleProduit_periodesNonContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");
    // Création de 2 références de couverture pour un produit
    Produit produit =
        contrat
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0);
    ReferenceCouverture ref = produit.getReferencesCouverture().get(0);
    ReferenceCouverture ref2 = new ReferenceCouverture(ref);
    ref2.setReferenceCouverture("tata");
    PeriodeDroitContractTP p =
        createPeriodesDroitsContractTP(
            "2023/04/01", "2023/04/25", "2023/05/31", TypePeriode.OFFLINE);
    PeriodeDroitContractTP p1 =
        createPeriodesDroitsContractTP(
            "2023/04/01", "2023/04/25", "2023/05/31", TypePeriode.ONLINE);
    PeriodeDroitContractTP p2 =
        createPeriodesDroitsContractTP(
            "2023/06/01", "2023/06/30", "2023/07/31", TypePeriode.OFFLINE);
    PeriodeDroitContractTP p3 =
        createPeriodesDroitsContractTP(
            "2023/06/01", "2023/06/30", "2023/07/31", TypePeriode.ONLINE);
    PeriodeDroitContractTP p4 =
        createPeriodesDroitsContractTP("2023/07/01", "2023/08/31", null, TypePeriode.OFFLINE);
    PeriodeDroitContractTP p5 =
        createPeriodesDroitsContractTP("2023/07/01", "2023/08/31", null, TypePeriode.ONLINE);
    ref2.getNaturesPrestation().get(0).setPeriodesDroit(Arrays.asList(p, p1));
    ref2.getNaturesPrestation().get(1).setPeriodesDroit(Arrays.asList(p2, p3, p4, p5));
    produit.setReferencesCouverture(Arrays.asList(ref, ref2));

    ContractTPMailleProduit contractTPMailleProduit = service.agregationMailleProduit(contrat);
    MailleProduit mailleProduit =
        contractTPMailleProduit
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(0);
    Assertions.assertEquals(3, mailleProduit.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleProduit.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleProduit.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals("2023/08/31", mailleProduit.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertNull(mailleProduit.getPeriodesDroit().get(0).getPeriodeFinFermeture());

    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleProduit.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleProduit.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals("2023/04/25", mailleProduit.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertEquals(
        "2023/05/31", mailleProduit.getPeriodesDroit().get(1).getPeriodeFinFermeture());

    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleProduit.getPeriodesDroit().get(2).getTypePeriode());
    Assertions.assertEquals(
        "2023/06/01", mailleProduit.getPeriodesDroit().get(2).getPeriodeDebut());
    Assertions.assertEquals("2023/08/31", mailleProduit.getPeriodesDroit().get(2).getPeriodeFin());
    Assertions.assertNull(mailleProduit.getPeriodesDroit().get(2).getPeriodeFinFermeture());
  }

  /**
   * 1 contrat avec 2 produits (contigus) Périodes 1er produit : 2021/01/21-2022/11/30,
   * 2022/12/01-2023/03/31 Période 2nd produit : 2021/01/02-2023/02/01
   */
  @Test
  void test_agregationMailleGarantie_periodesContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");

    ContractTPMailleGarantie contractTPMailleGarantie = service.agregationMailleGarantie(contrat);
    MailleGarantie mailleGarantie =
        contractTPMailleGarantie
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0);
    Assertions.assertEquals(2, mailleGarantie.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleGarantie.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/02", mailleGarantie.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals("2023/03/31", mailleGarantie.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertNull(mailleGarantie.getPeriodesDroit().get(0).getPeriodeFinFermeture());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleGarantie.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/02", mailleGarantie.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals("2023/03/31", mailleGarantie.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertNull(mailleGarantie.getPeriodesDroit().get(1).getPeriodeFinFermeture());

    Assertions.assertEquals(2, mailleGarantie.getConventionnements().size());
    Assertions.assertEquals(1, mailleGarantie.getConventionnements().get(0).getPeriodes().size());
    Assertions.assertEquals(
        "2021/01/21", mailleGarantie.getConventionnements().get(0).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/04/30", mailleGarantie.getConventionnements().get(0).getPeriodes().get(0).getFin());
    Assertions.assertEquals(1, mailleGarantie.getConventionnements().get(1).getPeriodes().size());
    Assertions.assertEquals(
        "2022/05/01", mailleGarantie.getConventionnements().get(1).getPeriodes().get(0).getDebut());
    Assertions.assertEquals(
        "2022/11/30", mailleGarantie.getConventionnements().get(1).getPeriodes().get(0).getFin());
  }

  /**
   * 1 contrat avec 2 produits (non contigues) Périodes 1er produit : 2021/01/21-2022/11/30,
   * 2022/12/01-2023/03/31 Période 2nd produit : 2023/05/01-2023/06/30
   */
  @Test
  void test_agregationMailleGarantie_periodesNonContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");
    List<PeriodeDroitContractTP> periodes =
        contrat
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0)
            .getProduits()
            .get(1)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    periodes.get(0).setPeriodeDebut("2023/05/01");
    periodes.get(0).setPeriodeFin("2023/06/30");
    periodes.get(1).setPeriodeDebut("2023/05/01");
    periodes.get(1).setPeriodeFin("2023/06/30");

    ContractTPMailleGarantie contractTPMailleGarantie = service.agregationMailleGarantie(contrat);
    MailleGarantie mailleGarantie =
        contractTPMailleGarantie
            .getBeneficiaires()
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getGaranties()
            .get(0);
    Assertions.assertEquals(4, mailleGarantie.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleGarantie.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleGarantie.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals("2023/03/31", mailleGarantie.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleGarantie.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2023/05/01", mailleGarantie.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals("2023/06/30", mailleGarantie.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleGarantie.getPeriodesDroit().get(2).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/21", mailleGarantie.getPeriodesDroit().get(2).getPeriodeDebut());
    Assertions.assertEquals("2023/03/31", mailleGarantie.getPeriodesDroit().get(2).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleGarantie.getPeriodesDroit().get(3).getTypePeriode());
    Assertions.assertEquals(
        "2023/05/01", mailleGarantie.getPeriodesDroit().get(3).getPeriodeDebut());
    Assertions.assertEquals("2023/06/30", mailleGarantie.getPeriodesDroit().get(3).getPeriodeFin());
  }

  /**
   * 1 contrat avec 2 garanties (contigues) Périodes 1ère garantie : 2021/01/02-2023/02/01,
   * 2021/01/21-2022/11/30, 2022/12/01-2023/03/31 Période 2nde garantie : 2023/04/01-2023/05/31
   */
  @Test
  void test_agregationMailleDomaine_periodesContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");

    Garantie garantie =
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(0).getGaranties().get(0);
    Garantie garantie2 = new Garantie(garantie);
    garantie2.setCodeGarantie("gar2");
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        garantie2
            .getProduits()
            .get(1)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    periodeDroitContractTPS.get(0).setPeriodeDebut("2023/04/01");
    periodeDroitContractTPS.get(0).setPeriodeFin("2023/05/31");
    periodeDroitContractTPS.get(1).setPeriodeDebut("2023/04/01");
    periodeDroitContractTPS.get(1).setPeriodeFin("2023/05/31");
    contrat
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .setGaranties(List.of(garantie, garantie2));

    ContractTPMailleDomaine contractTPMailleDomaine = service.agregationMailleDomaine(contrat);
    MailleDomaineDroit mailleDomaineDroit =
        contractTPMailleDomaine.getBeneficiaires().get(0).getDomaineDroits().get(0);
    Assertions.assertEquals(2, mailleDomaineDroit.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleDomaineDroit.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/02", mailleDomaineDroit.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/05/31", mailleDomaineDroit.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleDomaineDroit.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/02", mailleDomaineDroit.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/05/31", mailleDomaineDroit.getPeriodesDroit().get(1).getPeriodeFin());
  }

  /**
   * 1 contrat avec 2 garanties (periodes non contigues) Périodes 1ère garantie :
   * 2021/01/02-2023/02/01, 2021/01/21-2022/11/30, 2022/12/01-2023/03/31 Période 2nde garantie :
   * 2023/04/15-2023/05/31
   */
  @Test
  void test_agregationMailleDomaine_periodesNonContigues() {
    ContractTP contrat = this.contract.getContrat();
    completeContractData(contrat, "2022/12/01");

    Garantie garantie =
        contrat.getBeneficiaires().get(0).getDomaineDroits().get(0).getGaranties().get(0);
    Garantie garantie2 = new Garantie(garantie);
    garantie2.setCodeGarantie("gar2");
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        garantie2
            .getProduits()
            .get(1)
            .getReferencesCouverture()
            .get(0)
            .getNaturesPrestation()
            .get(0)
            .getPeriodesDroit();
    periodeDroitContractTPS.get(0).setPeriodeDebut("2023/04/15");
    periodeDroitContractTPS.get(0).setPeriodeFin("2023/05/31");
    periodeDroitContractTPS.get(1).setPeriodeDebut("2023/04/15");
    periodeDroitContractTPS.get(1).setPeriodeFin("2023/05/31");
    contrat
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .setGaranties(List.of(garantie, garantie2));

    ContractTPMailleDomaine contractTPMailleDomaine = service.agregationMailleDomaine(contrat);
    MailleDomaineDroit mailleDomaineDroit =
        contractTPMailleDomaine.getBeneficiaires().get(0).getDomaineDroits().get(0);
    Assertions.assertEquals(4, mailleDomaineDroit.getPeriodesDroit().size());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleDomaineDroit.getPeriodesDroit().get(0).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/02", mailleDomaineDroit.getPeriodesDroit().get(0).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/03/31", mailleDomaineDroit.getPeriodesDroit().get(0).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.OFFLINE, mailleDomaineDroit.getPeriodesDroit().get(1).getTypePeriode());
    Assertions.assertEquals(
        "2023/04/15", mailleDomaineDroit.getPeriodesDroit().get(1).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/05/31", mailleDomaineDroit.getPeriodesDroit().get(1).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleDomaineDroit.getPeriodesDroit().get(2).getTypePeriode());
    Assertions.assertEquals(
        "2021/01/02", mailleDomaineDroit.getPeriodesDroit().get(2).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/03/31", mailleDomaineDroit.getPeriodesDroit().get(2).getPeriodeFin());
    Assertions.assertEquals(
        TypePeriode.ONLINE, mailleDomaineDroit.getPeriodesDroit().get(3).getTypePeriode());
    Assertions.assertEquals(
        "2023/04/15", mailleDomaineDroit.getPeriodesDroit().get(3).getPeriodeDebut());
    Assertions.assertEquals(
        "2023/05/31", mailleDomaineDroit.getPeriodesDroit().get(3).getPeriodeFin());
  }

  private static PeriodeDroitContractTP createPeriodesDroitsContractTP(
      String dateDebut, String dateFin, String dateFinFermeture, TypePeriode typePeriode) {
    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setPeriodeDebut(dateDebut);
    periodeDroitContractTP.setPeriodeFin(dateFin);
    periodeDroitContractTP.setPeriodeFinFermeture(dateFinFermeture);
    periodeDroitContractTP.setTypePeriode(typePeriode);
    return periodeDroitContractTP;
  }

  private static ConventionnementContrat createConventionnement(String code, Integer priorite) {
    ConventionnementContrat conventionnementContrat = new ConventionnementContrat();
    TypeConventionnement typeConventionnement = new TypeConventionnement();
    typeConventionnement.setCode(code);
    conventionnementContrat.setTypeConventionnement(typeConventionnement);
    conventionnementContrat.setPriorite(priorite);
    return conventionnementContrat;
  }

  private static void completeContractData(ContractTP contrat, String periodeDebut) {
    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setNaturePrestation("HOSPIT");
    List<PeriodeDroitContractTP> periodeDroitContractTPS = new ArrayList<>();
    PeriodeDroitContractTP periodeDroitContractTP = new PeriodeDroitContractTP();
    periodeDroitContractTP.setTypePeriode(TypePeriode.OFFLINE);
    periodeDroitContractTP.setPeriodeDebut(periodeDebut);
    periodeDroitContractTP.setPeriodeFin("2023/03/31");
    PeriodeDroitContractTP periodeDroitContractTP2 =
        new PeriodeDroitContractTP(periodeDroitContractTP);
    periodeDroitContractTP2.setTypePeriode(TypePeriode.ONLINE);
    periodeDroitContractTPS.add(periodeDroitContractTP);
    periodeDroitContractTPS.add(periodeDroitContractTP2);
    naturePrestation.setPeriodesDroit(periodeDroitContractTPS);
    List<NaturePrestation> newNaturePrestations =
        new ArrayList<>(
            contrat
                .getBeneficiaires()
                .get(0)
                .getDomaineDroits()
                .get(0)
                .getGaranties()
                .get(0)
                .getProduits()
                .get(0)
                .getReferencesCouverture()
                .get(0)
                .getNaturesPrestation());
    newNaturePrestations.add(naturePrestation);
    contrat
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .setNaturesPrestation(newNaturePrestations);

    contrat
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getConventionnements()
        .get(0)
        .setPeriodes(
            Arrays.asList(
                new Periode("2021/01/21", "2022/02/28"), new Periode("2022/03/01", "2022/04/30")));
    contrat
        .getBeneficiaires()
        .get(0)
        .getDomaineDroits()
        .get(0)
        .getGaranties()
        .get(0)
        .getProduits()
        .get(0)
        .getReferencesCouverture()
        .get(0)
        .getNaturesPrestation()
        .get(0)
        .getConventionnements()
        .get(1)
        .setPeriodes(
            Arrays.asList(
                new Periode("2022/05/01", "2022/08/31"), new Periode("2022/09/01", "2022/11/30")));
  }
}
