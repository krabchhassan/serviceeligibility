package com.cegedim.next.serviceeligibility.core.business.consultationdroits;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionPriorisationGaranties;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheSegmentService;
import com.cegedim.next.serviceeligibility.core.features.consultationdroits.MapperContratToContractDto;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.GenerateContract;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
public class ConsultationDroitsStepsTest {

  @Autowired private GenerateContract contract;

  @Autowired ConsultationDroitsSteps consultationDroitsSteps;

  @Autowired private MapperContratToContractDto mapperContractDto;

  private void updateContract1With2Prio(ContractTP contrat, boolean withExceptionPriorisation) {
    for (DomaineDroitContractTP domaineDroitContract :
        contrat.getBeneficiaires().get(0).getDomaineDroits()) {
      updateDomaineContrat(domaineDroitContract, withExceptionPriorisation);
    }
  }

  private void updateContract2(ContractTP contrat) {
    contrat.setNumeroContrat("contrat2");
    contrat.setNumeroAdherent("contrat2");
    updateDomaineContrat2(contrat.getBeneficiaires().get(0).getDomaineDroits().get(0));
    updateDomaineContrat2(contrat.getBeneficiaires().get(0).getDomaineDroits().get(1));
  }

  private void updateDomaineContrat(
      DomaineDroitContractTP domaine, boolean withExceptionPriorisation) {
    Garantie garantie1 = new Garantie();
    Garantie garantie11 = new Garantie();
    Produit produit1 = new Produit();
    Produit produit11 = new Produit();
    ReferenceCouverture referenceCouvertureNOSEL = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture11 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    NaturePrestation naturePrestation2 = new NaturePrestation();
    NaturePrestation naturePrestation3 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode2 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode3 = new PeriodeDroitContractTP();

    if (withExceptionPriorisation) {
      naturePrestation3.setPrioritesDroit(
          mapPrioriteDroitWithPeriode("02", "2022/01/01", "2022/12/31"));
    } else {
      naturePrestation3.setPrioritesDroit(
          mapPrioriteDroitWithPeriode("01", "2022/06/01", "2022/12/31"));
    }
    naturePrestation1.setPrioritesDroit(mapPrioriteDroitPeriode1());
    naturePrestation2.setPrioritesDroit(
        mapPrioriteDroitWithPeriode("01", "2022/04/01", "2022/05/31"));

    periode1.setPeriodeDebut("2022/01/01");
    periode1.setPeriodeFin("2022/03/31");
    periode2.setPeriodeDebut("2022/04/01");
    periode2.setPeriodeFin("2022/06/30");
    periode3.setPeriodeDebut("2022/07/01");
    periode3.setPeriodeFin("2022/09/30");
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    naturePrestation2.setPeriodesDroit(List.of(periode2));
    naturePrestation3.setPeriodesDroit(List.of(periode3));
    referenceCouvertureNOSEL.setReferenceCouverture("NOSEL");
    referenceCouvertureNOSEL.setNaturesPrestation(List.of(naturePrestation1));
    referenceCouverture1.setReferenceCouverture("1");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation2));
    referenceCouverture11.setReferenceCouverture("11");
    referenceCouverture11.setNaturesPrestation(List.of(naturePrestation3));
    produit1.setReferencesCouverture(List.of(referenceCouvertureNOSEL, referenceCouverture1));
    produit11.setReferencesCouverture(List.of(referenceCouverture11));
    garantie1.setCodeGarantie("garantie 1");
    garantie1.setProduits(List.of(produit1));
    garantie11.setCodeGarantie("garantie 11");
    garantie11.setProduits(List.of(produit11));
    domaine.setGaranties(List.of(garantie1, garantie11));
  }

  private void updateDomaineContratWith3Garanties(DomaineDroitContractTP domaine) {
    Garantie garantie1 = new Garantie();
    Garantie garantie2 = new Garantie();
    Garantie garantie3 = new Garantie();
    Produit produit1 = new Produit();
    Produit produit2 = new Produit();
    Produit produit3 = new Produit();
    ReferenceCouverture referenceCouvertureNOSEL = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture11 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture2 = new ReferenceCouverture();
    ReferenceCouverture referenceCouverture3 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    NaturePrestation naturePrestation2 = new NaturePrestation();
    NaturePrestation naturePrestation3 = new NaturePrestation();
    NaturePrestation naturePrestation4 = new NaturePrestation();
    NaturePrestation naturePrestation5 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode2 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode3 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode4 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode5 = new PeriodeDroitContractTP();
    PeriodeDroitContractTP periode6 = new PeriodeDroitContractTP();

    naturePrestation1.setPrioritesDroit(mapPrioriteDroitPeriode1());
    periode1.setPeriodeDebut("2022/01/01");
    periode1.setPeriodeFin("2022/03/31");
    List<PrioriteDroitContrat> prioriteDroitContrats2 =
        mapPrioriteDroitWithPeriode("01", "2022/04/01", "2022/05/31");
    periode2.setPeriodeDebut("2022/04/01");
    periode2.setPeriodeFin("2022/05/31");
    List<PrioriteDroitContrat> prioriteDroitContrats3 =
        mapPrioriteDroitWithPeriode("03", "2022/01/01", "2022/12/31");
    prioriteDroitContrats2.addAll(prioriteDroitContrats3);
    naturePrestation2.setPrioritesDroit(prioriteDroitContrats2);
    periode3.setPeriodeDebut("2022/06/02");
    periode3.setPeriodeFin("2022/06/30");
    naturePrestation3.setPrioritesDroit(
        mapPrioriteDroitWithPeriode("04", "2022/07/01", "2022/09/30"));
    periode4.setPeriodeDebut("2022/07/01");
    periode4.setPeriodeFin("2022/09/30");
    naturePrestation4.setPrioritesDroit(
        mapPrioriteDroitWithPeriode("05", "2022/07/01", "2022/10/31"));
    periode5.setPeriodeDebut("2022/07/01");
    periode5.setPeriodeFin("2022/10/31");
    naturePrestation5.setPrioritesDroit(
        mapPrioriteDroitWithPeriode("05", "2022/07/01", "2022/10/31"));
    periode6.setPeriodeDebut("2022/07/01");
    periode6.setPeriodeFin("2022/10/31");
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    naturePrestation2.setPeriodesDroit(List.of(periode2, periode3));
    naturePrestation3.setPeriodesDroit(List.of(periode4));
    naturePrestation4.setPeriodesDroit(List.of(periode5));
    naturePrestation5.setPeriodesDroit(List.of(periode6));

    referenceCouvertureNOSEL.setReferenceCouverture("NOSEL");
    referenceCouvertureNOSEL.setNaturesPrestation(List.of(naturePrestation1));
    referenceCouverture1.setReferenceCouverture("1");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation2));
    referenceCouverture11.setReferenceCouverture("11");
    referenceCouverture11.setNaturesPrestation(List.of(naturePrestation3));
    referenceCouverture2.setNaturesPrestation(List.of(naturePrestation4));
    referenceCouverture3.setNaturesPrestation(List.of(naturePrestation5));
    produit1.setReferencesCouverture(
        List.of(referenceCouvertureNOSEL, referenceCouverture1, referenceCouverture11));
    produit2.setReferencesCouverture(List.of(referenceCouverture2));
    produit3.setReferencesCouverture(List.of(referenceCouverture3));
    garantie1.setCodeGarantie("garantie 1");
    garantie1.setProduits(List.of(produit1));
    garantie2.setCodeGarantie("garantie 2");
    garantie2.setProduits(List.of(produit2));
    garantie3.setCodeGarantie("garantie 3");
    garantie3.setProduits(List.of(produit3));
    domaine.setGaranties(List.of(garantie1, garantie2, garantie3));
  }

  public static List<PrioriteDroitContrat> mapPrioriteDroitPeriode1() {
    Periode periode1 = new Periode("2022/01/01", "2022/01/31");
    Periode periode2 = new Periode("2022/02/01", "2022/02/28");
    Periode periode3 = new Periode("2022/03/01", "2022/03/31");
    final PrioriteDroitContrat pD = new PrioriteDroitContrat();
    pD.setCode("01");
    pD.setLibelle("01");
    pD.setTypeDroit("01");
    pD.setPrioriteBO("01");
    pD.setPeriodes(List.of(periode1, periode3));
    final PrioriteDroitContrat pD2 = new PrioriteDroitContrat();
    pD2.setCode("02");
    pD2.setLibelle("02");
    pD2.setTypeDroit("02");
    pD2.setPrioriteBO("02");
    pD2.setPeriodes(List.of(periode2));
    return List.of(pD, pD2);
  }

  public static List<PrioriteDroitContrat> mapPrioriteDroitWithPeriode(
      String code, String debut, String fin) {
    final PrioriteDroitContrat prioriteDroitContrat = new PrioriteDroitContrat();
    prioriteDroitContrat.setCode(code);
    prioriteDroitContrat.setLibelle(code);
    prioriteDroitContrat.setTypeDroit(code);
    prioriteDroitContrat.setPrioriteBO(code);
    prioriteDroitContrat.setPeriodes(List.of(new Periode(debut, fin)));
    List<PrioriteDroitContrat> periodeDroitContrats = new ArrayList<>();
    periodeDroitContrats.add(prioriteDroitContrat);
    return periodeDroitContrats;
  }

  public static List<PrioriteDroitContrat> mapPrioriteDroit() {
    final PrioriteDroitContrat pD = new PrioriteDroitContrat();
    pD.setCode("01");
    pD.setLibelle("01");
    pD.setTypeDroit("01");
    pD.setPrioriteBO("01");
    return List.of(pD);
  }

  private void updateDomaineContrat2(DomaineDroitContractTP domaine) {
    Garantie garantie1 = new Garantie();
    Produit produit1 = new Produit();
    ReferenceCouverture referenceCouverture1 = new ReferenceCouverture();
    NaturePrestation naturePrestation1 = new NaturePrestation();
    PeriodeDroitContractTP periode1 = new PeriodeDroitContractTP();

    naturePrestation1.setPrioritesDroit(mapPrioriteDroit());
    periode1.setPeriodeDebut("2022/10/01");
    periode1.setPeriodeFin("2023/12/31");
    naturePrestation1.setPeriodesDroit(List.of(periode1));
    referenceCouverture1.setReferenceCouverture("2");
    referenceCouverture1.setNaturesPrestation(List.of(naturePrestation1));
    produit1.setReferencesCouverture(List.of(referenceCouverture1));
    garantie1.setCodeGarantie("garantie 2");
    garantie1.setProduits(List.of(produit1));
    domaine.setGaranties(List.of(garantie1));
  }

  @Test
  void test_exceptionPriorisationGaranties() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1With2Prio(contrat, true);
    DemandeInfoBeneficiaire demandeInfoBeneficiaire = new DemandeInfoBeneficiaire();
    demandeInfoBeneficiaire.setTypeRechercheSegment(TypeRechercheSegmentService.MONO_SEGMENT);
    demandeInfoBeneficiaire.setSegmentRecherche("HOSP");
    demandeInfoBeneficiaire.setDateReference(
        DateUtils.parseDate("2022-01-01", DateUtils.YYYY_MM_DD));
    demandeInfoBeneficiaire.setDateFin(DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD));

    try {
      ConsultationDroitsSteps.getContractsWithFilteredDomains(
          List.of(contrat), true, demandeInfoBeneficiaire);
    } catch (final ExceptionPriorisationGaranties e) {
      Assertions.assertEquals("PRIORISATION_INCORRECTE", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void test_exceptionServiceDroitNonOuvert_DemandeAvant() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1With2Prio(contrat, false);
    DemandeInfoBeneficiaire demandeInfoBeneficiaire = new DemandeInfoBeneficiaire();
    demandeInfoBeneficiaire.setTypeRechercheSegment(TypeRechercheSegmentService.MONO_SEGMENT);
    demandeInfoBeneficiaire.setSegmentRecherche("HOSP");
    demandeInfoBeneficiaire.setDateReference(
        DateUtils.parseDate("2020-01-01", DateUtils.YYYY_MM_DD));
    demandeInfoBeneficiaire.setDateFin(DateUtils.parseDate("2020-07-20", DateUtils.YYYY_MM_DD));

    try {
      ConsultationDroitsSteps.getContractsWithFilteredDomains(
          List.of(contrat), true, demandeInfoBeneficiaire);
    } catch (final ExceptionServiceDroitNonOuvert e) {
      Assertions.assertEquals("DROIT_NON_OUVERT", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void test_exceptionServiceDroitNonOuvert_DemandeApres() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1With2Prio(contrat, false);
    DemandeInfoBeneficiaire demandeInfoBeneficiaire = new DemandeInfoBeneficiaire();
    demandeInfoBeneficiaire.setTypeRechercheSegment(TypeRechercheSegmentService.MONO_SEGMENT);
    demandeInfoBeneficiaire.setSegmentRecherche("HOSP");
    demandeInfoBeneficiaire.setDateReference(
        DateUtils.parseDate("2024-01-01", DateUtils.YYYY_MM_DD));
    demandeInfoBeneficiaire.setDateFin(DateUtils.parseDate("2024-07-20", DateUtils.YYYY_MM_DD));
    try {
      ConsultationDroitsSteps.getContractsWithFilteredDomains(
          List.of(contrat), true, demandeInfoBeneficiaire);
    } catch (final ExceptionServiceDroitNonOuvert e) {
      Assertions.assertEquals("DROIT_NON_OUVERT", e.getMessage());
      return;
    }
    Assertions.fail();
  }

  @Test
  void test_limitGaranties() {
    ContractTP contrat = this.contract.getContrat();
    updateDomaineContratWith3Garanties(contrat.getBeneficiaires().get(0).getDomaineDroits().get(0));
    updateDomaineContratWith3Garanties(contrat.getBeneficiaires().get(0).getDomaineDroits().get(1));
    DemandeInfoBeneficiaire demandeInfoBeneficiaire = new DemandeInfoBeneficiaire();
    demandeInfoBeneficiaire.setTypeRechercheSegment(TypeRechercheSegmentService.MONO_SEGMENT);
    demandeInfoBeneficiaire.setSegmentRecherche("HOSP");
    demandeInfoBeneficiaire.setDateReference(
        DateUtils.parseDate("2022-01-01", DateUtils.YYYY_MM_DD));
    demandeInfoBeneficiaire.setDateFin(DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD));

    List<ContractTP> contracts =
        ConsultationDroitsSteps.getContractsWithFilteredDomains(
            List.of(contrat), true, demandeInfoBeneficiaire);
    List<DomaineDroitContractTP> domaines =
        contracts.get(0).getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(1, domaines.size());
    Assertions.assertEquals("HOSP", domaines.get(0).getCode());
    Assertions.assertEquals(1, domaines.get(0).getGaranties().size());
    Assertions.assertEquals("garantie 1", domaines.get(0).getGaranties().get(0).getCodeGarantie());
  }

  @Test
  void test_limitGaranties2() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1With2Prio(contrat, false);
    DemandeInfoBeneficiaire demandeInfoBeneficiaire = new DemandeInfoBeneficiaire();
    demandeInfoBeneficiaire.setTypeRechercheSegment(TypeRechercheSegmentService.LISTE_SEGMENT);
    demandeInfoBeneficiaire.setListeSegmentRecherche(List.of("HOSP", "OPTI"));
    demandeInfoBeneficiaire.setDateReference(
        DateUtils.parseDate("2022-01-01", DateUtils.YYYY_MM_DD));
    demandeInfoBeneficiaire.setDateFin(DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD));

    List<ContractTP> contracts =
        ConsultationDroitsSteps.getContractsWithFilteredDomains(
            List.of(contrat), true, demandeInfoBeneficiaire);
    List<DomaineDroitContractTP> domaines =
        contracts.get(0).getBeneficiaires().get(0).getDomaineDroits();
    Assertions.assertEquals(2, domaines.size());
    Assertions.assertEquals("HOSP", domaines.get(0).getCode());
    Assertions.assertEquals(2, domaines.get(0).getGaranties().size());
    Assertions.assertEquals("garantie 1", domaines.get(0).getGaranties().get(0).getCodeGarantie());
    Assertions.assertEquals("garantie 11", domaines.get(0).getGaranties().get(1).getCodeGarantie());
    Assertions.assertEquals("OPTI", domaines.get(1).getCode());
    Assertions.assertEquals(2, domaines.get(1).getGaranties().size());
    Assertions.assertEquals("garantie 1", domaines.get(1).getGaranties().get(0).getCodeGarantie());
    Assertions.assertEquals("garantie 11", domaines.get(1).getGaranties().get(1).getCodeGarantie());
  }

  @Test
  void test_checkValiditePeriodeDroit() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1With2Prio(contrat, false);
    ContractTP contrat2 = this.contract.getContrat();
    updateContract2(contrat2);
    List<ContractDto> contractDtoList =
        mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2022-04-05");
    Assertions.assertTrue(
        consultationDroitsSteps.checkValiditePeriodeDroit(
            contractDtoList,
            DateUtils.parseDate("2022-04-05", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD),
            false));
    Assertions.assertEquals(1, contractDtoList.size());
    contractDtoList = mapperContractDto.entityListToDtoList(List.of(contrat2), "2022-04-05");
    Assertions.assertFalse(
        consultationDroitsSteps.checkValiditePeriodeDroit(
            contractDtoList,
            DateUtils.parseDate("2022-04-05", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-07-20", DateUtils.YYYY_MM_DD),
            false));
    Assertions.assertEquals(0, contractDtoList.size());
  }

  @Test
  void test_checkValiditePeriodeDroit_demandeACheval() {
    ContractTP contrat = this.contract.getContrat();
    updateContract1With2Prio(contrat, false);
    ContractTP contrat2 = this.contract.getContrat();
    updateContract2(contrat2);
    List<ContractDto> contractDtoList =
        mapperContractDto.entityListToDtoList(List.of(contrat, contrat2), "2021-12-15");
    Assertions.assertTrue(
        consultationDroitsSteps.checkValiditePeriodeDroit(
            contractDtoList,
            DateUtils.parseDate("2021-12-15", DateUtils.YYYY_MM_DD),
            DateUtils.parseDate("2022-11-20", DateUtils.YYYY_MM_DD),
            false));
    Assertions.assertEquals(2, contractDtoList.size());
  }

  @Test
  void test_contratLeMemeBenef2FoisAvecNumPersDifferent() {
    ContractTP contrat = this.contract.getContrat();
    BeneficiaireContractTP beneficiaireContractTP =
        new BeneficiaireContractTP(contrat.getBeneficiaires().get(0));
    updateContract1With2Prio(contrat, false);
    beneficiaireContractTP.setNumeroPersonne("777");
    contrat.getBeneficiaires().add(beneficiaireContractTP);
    DemandeInfoBeneficiaire demandeInfoBeneficiaire = new DemandeInfoBeneficiaire();
    demandeInfoBeneficiaire.setTypeRechercheSegment(TypeRechercheSegmentService.MONO_SEGMENT);
    demandeInfoBeneficiaire.setSegmentRecherche("HOSP");
    demandeInfoBeneficiaire.setDateReference(
        DateUtils.parseDate("2021-01-01", DateUtils.YYYY_MM_DD));
    demandeInfoBeneficiaire.setDateFin(DateUtils.parseDate("2021-07-20", DateUtils.YYYY_MM_DD));

    List<ContractTP> contractTPFiltered =
        ConsultationDroitsSteps.getContractsWithFilteredDomains(
            List.of(contrat), true, demandeInfoBeneficiaire);
    Assertions.assertEquals(1, contractTPFiltered.size());
    Assertions.assertEquals(1, contractTPFiltered.get(0).getBeneficiaires().size());
    Assertions.assertEquals(
        "777", contractTPFiltered.get(0).getBeneficiaires().get(0).getNumeroPersonne());
    Assertions.assertEquals(
        1, contractTPFiltered.get(0).getBeneficiaires().get(0).getDomaineDroits().size());
  }
}
