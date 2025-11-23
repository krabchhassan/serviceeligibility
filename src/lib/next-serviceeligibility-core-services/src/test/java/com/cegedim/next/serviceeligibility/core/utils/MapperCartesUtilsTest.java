package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RejetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MapperCartesUtilsTest {

  @Test
  void testDecoupageConsosPeriodes() {
    String stringToday = "2024/03/07";
    List<DeclarationConsolide> inDeclarationConsolideList = new ArrayList<>();
    inDeclarationConsolideList.add(
        getDeclarationConsolide("2024/01/01", "2024/12/31", "123456", "NUMEROCONTRAT"));
    inDeclarationConsolideList.add(
        getDeclarationConsolide("2024/03/03", "2024/11/30", "123457", "NUMEROCONTRAT"));
    DeclarationConsolide declarationConsolide2 =
        getDeclarationConsolide("2025/01/01", "2025/05/31", "123456", "NUMEROCONTRAT");
    inDeclarationConsolideList.add(declarationConsolide2);
    String minDebut =
        inDeclarationConsolideList.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .min(String::compareTo)
            .orElse("");

    List<DeclarationConsolide> declarationConsolideList =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            inDeclarationConsolideList, minDebut, stringToday, false, new Periode());
    Assertions.assertEquals(4, declarationConsolideList.size());

    assertPeriodEquals(
        getDeclarationConsolide("2024/01/01", "2024/11/30", "123456", "NUMEROCONTRAT"),
        declarationConsolideList.get(0));
    assertPeriodEquals(
        getDeclarationConsolide("2024/03/03", "2024/11/30", "123457", "NUMEROCONTRAT"),
        declarationConsolideList.get(1));
    assertPeriodEquals(
        getDeclarationConsolide("2024/12/01", "2024/12/31", "123456", "NUMEROCONTRAT"),
        declarationConsolideList.get(2));
    assertPeriodEquals(
        getDeclarationConsolide("2025/01/01", "2025/05/31", "123456", "NUMEROCONTRAT"),
        declarationConsolideList.get(3));

    // bizarrement, c'est pas vraiment isDroitOuvert mais juste supérieur à la date
    // du jour !
    // stringToday = "2023/01/07";
    // declarationConsolideList = MapperCartesUtils
    // .decoupageConsosPeriodes(inDeclarationConsolideList, minDebut, stringToday);
    // Assertions.assertEquals(0, declarationConsolideList.size());

    stringToday = "2025/01/07";
    declarationConsolideList =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            inDeclarationConsolideList, minDebut, stringToday, false, new Periode());
    Assertions.assertEquals(1, declarationConsolideList.size());
    assertPeriodEquals(
        getDeclarationConsolide("2025/01/01", "2025/05/31", "123456", "NUMEROCONTRAT"),
        declarationConsolideList.get(0));
  }

  @Test
  void testDecoupageConsosPeriodesWithSuspensionsPeriods_INSURE() {
    String stringToday = "2024/03/07";
    List<DeclarationConsolide> inDeclarationConsolideList = new ArrayList<>();
    DeclarationConsolide declarationConsolide =
        getDeclarationConsolide("2024/01/01", "2024/12/31", "123456", "NUMEROCONTRAT");
    PeriodeSuspensionDeclaration periodeSuspension1 = new PeriodeSuspensionDeclaration();
    periodeSuspension1.setDebut("2023/12/01");
    periodeSuspension1.setFin("2024/01/31");
    PeriodeSuspensionDeclaration periodeSuspension2 = new PeriodeSuspensionDeclaration();
    periodeSuspension2.setDebut("2024/03/01");
    periodeSuspension2.setFin("2024/05/31");
    PeriodeSuspensionDeclaration periodeSuspension3 = new PeriodeSuspensionDeclaration();
    periodeSuspension3.setDebut("2024/11/01");
    declarationConsolide
        .getContrat()
        .setPeriodeSuspensions(List.of(periodeSuspension1, periodeSuspension2, periodeSuspension3));
    inDeclarationConsolideList.add(declarationConsolide);

    String minDebut =
        inDeclarationConsolideList.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .min(String::compareTo)
            .orElse("");

    List<DeclarationConsolide> declarationConsolideList =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            inDeclarationConsolideList, minDebut, stringToday, true, new Periode());
    Assertions.assertEquals(2, declarationConsolideList.size());
  }

  @Test
  void testDecoupageConsosPeriodesWithSuspensionsPeriods_OTP() {
    String stringToday = "2024/03/07";
    List<DeclarationConsolide> inDeclarationConsolideList = new ArrayList<>();
    DeclarationConsolide declarationConsolide =
        getDeclarationConsolide("2024/01/01", "2024/12/31", "123456", "NUMEROCONTRAT");
    PeriodeSuspensionDeclaration periodeSuspension1 = new PeriodeSuspensionDeclaration();
    periodeSuspension1.setDebut("2023/12/01");
    periodeSuspension1.setFin("2024/01/31");
    PeriodeSuspensionDeclaration periodeSuspension2 = new PeriodeSuspensionDeclaration();
    periodeSuspension2.setDebut("2024/03/01");
    periodeSuspension2.setFin("2024/05/31");
    PeriodeSuspensionDeclaration periodeSuspension3 = new PeriodeSuspensionDeclaration();
    periodeSuspension3.setDebut("2024/11/01");
    declarationConsolide
        .getContrat()
        .setPeriodeSuspensions(List.of(periodeSuspension1, periodeSuspension2, periodeSuspension3));
    inDeclarationConsolideList.add(declarationConsolide);

    String minDebut =
        inDeclarationConsolideList.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .min(String::compareTo)
            .orElse("");

    List<DeclarationConsolide> declarationConsolideList =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            inDeclarationConsolideList, minDebut, stringToday, false, new Periode());
    Assertions.assertEquals(1, declarationConsolideList.size());
  }

  @Test
  void testDecoupageConsosPeriodesWithSuspensionsPeriods2() {
    String stringToday = "2024/03/07";
    List<DeclarationConsolide> inDeclarationConsolideList = new ArrayList<>();
    DeclarationConsolide declarationConsolide =
        getDeclarationConsolide("2024/01/01", "2024/12/31", "123456", "NUMEROCONTRAT");
    PeriodeSuspensionDeclaration periodeSuspension1 = new PeriodeSuspensionDeclaration();
    periodeSuspension1.setDebut("2023/12/01");
    periodeSuspension1.setFin("2024/01/31");
    PeriodeSuspensionDeclaration periodeSuspension2 = new PeriodeSuspensionDeclaration();
    periodeSuspension2.setDebut("2024/02/01");
    periodeSuspension2.setFin("2024/05/31");
    PeriodeSuspensionDeclaration periodeSuspension3 = new PeriodeSuspensionDeclaration();
    periodeSuspension3.setDebut("2024/11/01");
    declarationConsolide
        .getContrat()
        .setPeriodeSuspensions(List.of(periodeSuspension1, periodeSuspension2, periodeSuspension3));
    inDeclarationConsolideList.add(declarationConsolide);

    String minDebut =
        inDeclarationConsolideList.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .min(String::compareTo)
            .orElse("");

    List<DeclarationConsolide> declarationConsolideList =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            inDeclarationConsolideList, minDebut, stringToday, true, new Periode());
    Assertions.assertEquals(1, declarationConsolideList.size());
  }

  @Test
  void testDecoupageConsosPeriodesWithSuspensionsPeriods3() {
    String stringToday = "2024/03/07";
    List<DeclarationConsolide> inDeclarationConsolideList = new ArrayList<>();
    DeclarationConsolide declarationConsolide =
        getDeclarationConsolide("2024/01/01", "2024/12/31", "123456", "NUMEROCONTRAT");
    PeriodeSuspensionDeclaration periodeSuspension1 = new PeriodeSuspensionDeclaration();
    periodeSuspension1.setDebut("2023/12/01");
    declarationConsolide.getContrat().setPeriodeSuspensions(List.of(periodeSuspension1));
    inDeclarationConsolideList.add(declarationConsolide);

    String minDebut =
        inDeclarationConsolideList.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .min(String::compareTo)
            .orElse("");

    List<DeclarationConsolide> declarationConsolideList =
        MapperCartesUtils.splitAllConsosPeriodesByEndDate(
            inDeclarationConsolideList, minDebut, stringToday, true, new Periode());
    Assertions.assertTrue(CollectionUtils.isEmpty(declarationConsolideList));
  }

  private void assertPeriodEquals(
      DeclarationConsolide periodeAssert, DeclarationConsolide periodeToTest) {
    Assertions.assertEquals(periodeAssert.getPeriodeDebut(), periodeToTest.getPeriodeDebut());
    Assertions.assertEquals(periodeAssert.getPeriodeFin(), periodeToTest.getPeriodeFin());
    Assertions.assertEquals(
        periodeAssert.getContrat().getNumero(), periodeToTest.getContrat().getNumero());
    Assertions.assertEquals(
        periodeAssert.getBeneficiaire().getNumeroPersonne(),
        periodeToTest.getBeneficiaire().getNumeroPersonne());
  }

  private static DeclarationConsolide getDeclarationConsolide(
      String debut, String fin, String numeroPersonne, String numeroContrat) {
    DeclarationConsolide declarationConsolide = new DeclarationConsolide();
    declarationConsolide.setPeriodeDebut(debut);
    declarationConsolide.setPeriodeFin(fin);
    Beneficiaire beneficiaire = new Beneficiaire();
    beneficiaire.setNumeroPersonne(numeroPersonne);
    declarationConsolide.setBeneficiaire(beneficiaire);
    Contrat contrat = new Contrat();
    contrat.setNumero(numeroContrat);
    declarationConsolide.setContrat(contrat);
    DomaineDroit produit = new DomaineDroit();
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut(debut);
    periodeDroit.setPeriodeFin(fin);
    produit.setPeriodeDroit(periodeDroit);
    declarationConsolide.setProduit(produit);
    List<DomaineDroit> domaineDroitList = new ArrayList<>();
    domaineDroitList.add(produit);
    declarationConsolide.setDomaineDroits(domaineDroitList);
    return declarationConsolide;
  }

  private DomaineDroit createDomaineDroit(
      String code, String tauxRemboursement, String uniteTaux, int noOrdre) {
    DomaineDroit domaine = new DomaineDroit();
    domaine.setCode(code);
    domaine.setTauxRemboursement(tauxRemboursement);
    domaine.setUniteTauxRemboursement(uniteTaux);
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2024/01/01");
    domaine.setPeriodeDroit(periodeDroit);
    domaine.setNoOrdreDroit(noOrdre);
    return domaine;
  }

  private DomaineDroit createDomaineDroitWithDetails(
      String code,
      String tauxRemboursement,
      int noOrdre,
      String codeExterneProduit,
      String codeProduit,
      String codeGarantie,
      String prioriteCode,
      String codeRenvoi,
      String codeRenvoiAdditionnel) {
    DomaineDroit domaine = createDomaineDroit(code, tauxRemboursement, "2024/01/01", noOrdre);
    domaine.setCodeExterneProduit(codeExterneProduit);
    domaine.setCodeProduit(codeProduit);
    domaine.setCodeGarantie(codeGarantie);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode(prioriteCode);
    domaine.setPrioriteDroit(prioriteDroit);
    domaine.setCodeRenvoi(codeRenvoi);
    domaine.setCodeRenvoiAdditionnel(codeRenvoiAdditionnel);
    return domaine;
  }

  @Test
  void noRegroupement() throws RejetException {
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP("Test");
    regroupement.setCodesDomainesTP(List.of("TE", "ST"));
    regroupement.setNiveauRemboursementIdentique(true);

    String code1 = "PIPOZ";
    String taux1 = "100";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, null, 1);

    String code2 = "PEPOZ";
    String taux2 = "200";
    DomaineDroit domaine2 = createDomaineDroit(code2, taux2, null, 2);

    List<DomaineCarte> domaineCartes =
        MapperCartesUtils.createDomainesCarte(List.of(domaine1, domaine2), List.of(regroupement));

    Assertions.assertEquals(2, domaineCartes.size());
    DomaineCarte domRes1 = domaineCartes.get(0);
    Assertions.assertEquals(code1, domRes1.getCode());
    Assertions.assertEquals(taux1, domRes1.getTaux());
    DomaineCarte domRes2 = domaineCartes.get(1);
    Assertions.assertEquals(code2, domRes2.getCode());
    Assertions.assertEquals(taux2, domRes2.getTaux());
  }

  @Test
  void concat() throws RejetException {
    String code1 = "PIPOZ";
    String code2 = "PEPOZ";
    String codeRegroup = "FEUR";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(false);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "100";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, null, 1);

    String taux2 = "200";
    DomaineDroit domaine2 = createDomaineDroit(code2, taux2, null, 2);

    List<DomaineCarte> domaineCartes =
        MapperCartesUtils.createDomainesCarte(List.of(domaine1, domaine2), List.of(regroupement));

    Assertions.assertEquals(1, domaineCartes.size());
    DomaineCarte domRes1 = domaineCartes.get(0);
    Assertions.assertEquals(codeRegroup, domRes1.getCode());
    Assertions.assertEquals(taux1 + "/" + taux2, domRes1.getTaux());
    Assertions.assertEquals(2, domRes1.getRegroupement().size());
    DomaineCarte subDom1 = domRes1.getRegroupement().get(0);
    DomaineCarte subDom2 = domRes1.getRegroupement().get(1);
    Assertions.assertEquals(code1, subDom1.getCode());
    Assertions.assertEquals(taux1, subDom1.getTaux());
    Assertions.assertEquals(code2, subDom2.getCode());
    Assertions.assertEquals(taux2, subDom2.getTaux());
  }

  @Test
  void concat_2() throws RejetException {
    String code1 = "PHCO";
    String code2 = "PHNO";
    String code3 = "LABO";
    String code4 = "RADL";
    String code5 = "HOSP";
    String codeRegroup = "PHAR";
    String codeRegroup2 = "LARA";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(false);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));
    RegroupementDomainesTP regroupement2 = new RegroupementDomainesTP();
    regroupement2.setDomaineRegroupementTP(codeRegroup2);
    regroupement2.setCodesDomainesTP(List.of(code3, code4));
    regroupement2.setNiveauRemboursementIdentique(false);
    regroupement2.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "100";
    DomaineDroit domaine1 =
        createDomaineDroitWithDetails(
            code1,
            taux1,
            1,
            "CODE_EXT_D1",
            "CODE_PRODUIT_D1",
            "CODE_GARANTIE_D1",
            "PRIO_D1",
            "CODE_RENVOI",
            "CODE_RENV_ADD");

    String taux2 = "80";
    DomaineDroit domaine2 =
        createDomaineDroitWithDetails(
            code2,
            taux2,
            4,
            "CODE_EXT_D2",
            "CODE_PRODUIT_D2",
            "CODE_GARANTIE_D2",
            "PRIO_D2",
            "CODE_RENVOI",
            "CODE_RENV_ADD");

    String taux3 = "50";
    DomaineDroit domaine3 =
        createDomaineDroitWithDetails(
            code3,
            taux3,
            5,
            "CODE_EXT_D3",
            "CODE_PRODUIT_D3",
            "CODE_GARANTIE_D3",
            "PRIO_D3",
            "CODE_RENVOI_V2",
            "CODE_RENV_ADD_V2");

    String taux4 = "50";
    DomaineDroit domaine4 =
        createDomaineDroitWithDetails(
            code4,
            taux4,
            3,
            "CODE_EXT_D4",
            "CODE_PRODUIT_D4",
            "CODE_GARANTIE_D4",
            "PRIO_D4",
            "CODE_RENVOI_V2",
            "CODE_RENV_ADD_V2");

    String taux5 = "90";
    DomaineDroit domaine5 =
        createDomaineDroitWithDetails(
            code5,
            taux5,
            2,
            "CODE_EXT_D5",
            "CODE_PRODUIT_D5",
            "CODE_GARANTIE_D5",
            "PRIO_D5",
            "CODE_RENVOI_V3",
            "CODE_RENV_ADD_V3");

    List<DomaineCarte> domaineCartes =
        MapperCartesUtils.createDomainesCarte(
            List.of(domaine1, domaine2, domaine3, domaine4, domaine5),
            List.of(regroupement, regroupement2));

    Assertions.assertEquals(3, domaineCartes.size());
    DomaineCarte domRes1 = domaineCartes.get(0);
    Assertions.assertEquals(codeRegroup, domRes1.getCode());
    Assertions.assertEquals(taux1 + "/" + taux2, domRes1.getTaux());
    Assertions.assertEquals(1, domRes1.getRang());
    Assertions.assertEquals(2, domRes1.getRegroupement().size());
    DomaineCarte subDom1 = domRes1.getRegroupement().get(0);
    DomaineCarte subDom2 = domRes1.getRegroupement().get(1);
    Assertions.assertEquals(code1, subDom1.getCode());
    Assertions.assertEquals(taux1, subDom1.getTaux());
    Assertions.assertEquals(1, subDom1.getRang());
    Assertions.assertEquals(code2, subDom2.getCode());
    Assertions.assertEquals(taux2, subDom2.getTaux());
    Assertions.assertEquals(4, subDom2.getRang());
    Assertions.assertEquals(domaine1.getCodeExterneProduit(), domRes1.getCodeExterneProduit());
    Assertions.assertEquals(domaine1.getCodeProduit(), domRes1.getCodeProduit());
    Assertions.assertEquals(domaine1.getCodeGarantie(), domRes1.getCodeGarantie());
    Assertions.assertEquals(domaine1.getPrioriteDroit(), domRes1.getPrioriteDroits());
    Assertions.assertEquals(domaine1.getCodeRenvoi(), domRes1.getCodeRenvoi());
    Assertions.assertEquals(
        domaine1.getCodeRenvoiAdditionnel(), domRes1.getCodeRenvoiAdditionnel());

    DomaineCarte domRes2 = domaineCartes.get(1);
    Assertions.assertEquals(code5, domRes2.getCode());
    Assertions.assertEquals(taux5, domRes2.getTaux());
    Assertions.assertEquals(2, domRes2.getRang());
    Assertions.assertNull(domRes2.getRegroupement());
    Assertions.assertEquals(domaine5.getCodeExterneProduit(), domRes2.getCodeExterneProduit());
    Assertions.assertEquals(domaine5.getCodeProduit(), domRes2.getCodeProduit());
    Assertions.assertEquals(domaine5.getCodeGarantie(), domRes2.getCodeGarantie());
    Assertions.assertEquals(domaine5.getPrioriteDroit(), domRes2.getPrioriteDroits());
    Assertions.assertEquals(domaine5.getCodeRenvoi(), domRes2.getCodeRenvoi());
    Assertions.assertEquals(
        domaine5.getCodeRenvoiAdditionnel(), domRes2.getCodeRenvoiAdditionnel());

    DomaineCarte domRes3 = domaineCartes.get(2);
    Assertions.assertEquals(codeRegroup2, domRes3.getCode());
    Assertions.assertEquals(taux3 + "/" + taux4, domRes3.getTaux());
    Assertions.assertEquals(3, domRes3.getRang());
    Assertions.assertEquals(2, domRes3.getRegroupement().size());
    DomaineCarte subDom3 = domRes3.getRegroupement().get(0);
    DomaineCarte subDom4 = domRes3.getRegroupement().get(1);
    Assertions.assertEquals(code4, subDom3.getCode());
    Assertions.assertEquals(taux4, subDom3.getTaux());
    Assertions.assertEquals(3, subDom3.getRang());
    Assertions.assertEquals(code3, subDom4.getCode());
    Assertions.assertEquals(taux3, subDom4.getTaux());
    Assertions.assertEquals(5, subDom4.getRang());
    Assertions.assertEquals(domaine3.getCodeExterneProduit(), domRes3.getCodeExterneProduit());
    Assertions.assertEquals(domaine3.getCodeProduit(), domRes3.getCodeProduit());
    Assertions.assertEquals(domaine3.getCodeGarantie(), domRes3.getCodeGarantie());
    Assertions.assertEquals(domaine3.getPrioriteDroit(), domRes3.getPrioriteDroits());
    Assertions.assertEquals(domaine3.getCodeRenvoi(), domRes3.getCodeRenvoi());
    Assertions.assertEquals(
        domaine3.getCodeRenvoiAdditionnel(), domRes3.getCodeRenvoiAdditionnel());
  }

  @Test
  void merge() throws RejetException {
    String code1 = "PIPOZ";
    String code2 = "PEPOZ";
    String codeRegroup = "FEUR";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(true);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "100";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, null, 1);

    DomaineDroit domaine2 = createDomaineDroit(code2, taux1, null, 2);

    List<DomaineCarte> domaineCartes =
        MapperCartesUtils.createDomainesCarte(List.of(domaine1, domaine2), List.of(regroupement));

    Assertions.assertEquals(1, domaineCartes.size());
    DomaineCarte domRes1 = domaineCartes.get(0);
    Assertions.assertEquals(codeRegroup, domRes1.getCode());
    Assertions.assertEquals(taux1, domRes1.getTaux());
    Assertions.assertEquals(2, domRes1.getRegroupement().size());
    DomaineCarte subDom1 = domRes1.getRegroupement().get(0);
    DomaineCarte subDom2 = domRes1.getRegroupement().get(1);
    Assertions.assertEquals(code1, subDom1.getCode());
    Assertions.assertEquals(taux1, subDom1.getTaux());
    Assertions.assertEquals(code2, subDom2.getCode());
    Assertions.assertEquals(taux1, subDom2.getTaux());
  }

  @Test
  void cantMerge() {
    String code1 = "PIPOZ";
    String code2 = "PEPOZ";
    String codeRegroup = "FEUR";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(true);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "100";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, null, 1);

    String taux2 = "200";
    DomaineDroit domaine2 = createDomaineDroit(code2, taux2, null, 2);

    Assertions.assertThrows(
        RejetException.class,
        () ->
            MapperCartesUtils.createDomainesCarte(
                List.of(domaine1, domaine2), List.of(regroupement)));
  }

  @Test
  void notSameConventionnement() {
    String code1 = "PIPOZ";
    String code2 = "PEPOZ";
    String codeRegroup = "FEUR";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(true);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "100";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, null, 1);
    Conventionnement conventionnement1 = new Conventionnement();
    conventionnement1.setPriorite(1);
    TypeConventionnement type1 = new TypeConventionnement();
    type1.setCode("1");
    type1.setLibelle("1");
    conventionnement1.setTypeConventionnement(type1);
    domaine1.setConventionnements(List.of(conventionnement1));
    domaine1.setNoOrdreDroit(1);

    String taux2 = "100";
    DomaineDroit domaine2 = createDomaineDroit(code2, taux2, null, 2);
    Conventionnement conventionnement2 = new Conventionnement();
    conventionnement2.setPriorite(1);
    TypeConventionnement type2 = new TypeConventionnement();
    type2.setCode("2");
    type2.setLibelle("2");
    conventionnement2.setTypeConventionnement(type2);
    domaine2.setConventionnements(new ArrayList<>(List.of(conventionnement1, conventionnement2)));
    domaine2.setNoOrdreDroit(2);

    Assertions.assertThrows(
        RejetException.class,
        () ->
            MapperCartesUtils.createDomainesCarte(
                List.of(domaine1, domaine2), List.of(regroupement)));
  }

  @Test
  void concatNonCouvert() throws RejetException {
    String code1 = "PIPOZ";
    String code2 = "PEPOZ";
    String codeRegroup = "FEUR";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(false);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "PEC";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, TauxConstants.U_TEXT, 1);

    List<DomaineCarte> domaineCartes =
        MapperCartesUtils.createDomainesCarte(List.of(domaine1), List.of(regroupement));

    Assertions.assertEquals(1, domaineCartes.size());
    DomaineCarte domRes1 = domaineCartes.get(0);
    Assertions.assertEquals(codeRegroup, domRes1.getCode());
    Assertions.assertEquals(taux1 + "/" + TauxConstants.T_NON_COUVERT, domRes1.getTaux());
    Assertions.assertEquals(2, domRes1.getRegroupement().size());
    DomaineCarte subDom1 = domRes1.getRegroupement().get(0);
    DomaineCarte subDom2 = domRes1.getRegroupement().get(1);
    Assertions.assertEquals(code2, subDom1.getCode());
    Assertions.assertEquals(TauxConstants.T_NON_COUVERT, subDom1.getTaux());
    Assertions.assertEquals(0, subDom1.getRang());
    Assertions.assertEquals(code1, subDom2.getCode());
    Assertions.assertEquals(taux1, subDom2.getTaux());
    Assertions.assertEquals(1, subDom2.getRang());
  }

  @Test
  void cantMergeNonCouvert() throws RejetException {
    String code1 = "PIPOZ";
    String code2 = "PEPOZ";
    String codeRegroup = "FEUR";
    RegroupementDomainesTP regroupement = new RegroupementDomainesTP();
    regroupement.setDomaineRegroupementTP(codeRegroup);
    regroupement.setCodesDomainesTP(List.of(code1, code2));
    regroupement.setNiveauRemboursementIdentique(true);
    regroupement.setDateDebut(
        LocalDateTime.parse("2024-01-01T00:00:00.000", DateTimeFormatter.ISO_DATE_TIME));

    String taux1 = "PEC";
    DomaineDroit domaine1 = createDomaineDroit(code1, taux1, TauxConstants.U_TEXT, 1);

    List<DomaineCarte> domaineCartes =
        MapperCartesUtils.createDomainesCarte(List.of(domaine1), List.of(regroupement));
    Assertions.assertEquals(1, domaineCartes.size());
    DomaineCarte domRes1 = domaineCartes.get(0);
    Assertions.assertEquals(code1, domRes1.getCode());
    Assertions.assertEquals(taux1, domRes1.getTaux());
    Assertions.assertEquals(TauxConstants.U_TEXT, domRes1.getUnite());
  }
}
