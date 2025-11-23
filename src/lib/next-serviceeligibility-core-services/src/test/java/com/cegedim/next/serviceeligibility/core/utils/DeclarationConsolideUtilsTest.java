package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeclarationConsolideUtilsTest {
  Declarant declarantCarteEditable;
  Declarant declarantCarteNonEditable;

  Declarant declarantCarteEditableCarteTpFerme;
  Declarant declarantCarteEditableCarteDematFerme;
  Declarant declarantCarteEditableServicesFermes;

  @BeforeEach
  void init() {
    declarantCarteEditable = getDeclarant("123456", true, true, false, false);
    declarantCarteNonEditable = getDeclarant("123456", false, true, false, false);
    declarantCarteEditableCarteTpFerme = getDeclarant("123456", true, true, true, false);
    declarantCarteEditableCarteDematFerme = getDeclarant("123456", true, true, false, true);
    declarantCarteEditableServicesFermes = getDeclarant("123456", true, true, true, true);
  }

  @Test
  void testConsoServicesDeclarationV3Editable1() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "1", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3Editable2() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "2", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3Editable3() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "3", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(1));
  }

  @Test
  void testConsoServicesDeclarationV3Editable4() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "4", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3Editable0() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "0", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void testConsoServicesDeclarationV3NonEditableSansDateSynchro() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "1", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, getDeclarant("123456", false, false, false, false));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3NonEditable1() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "1", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void testConsoServicesDeclarationV3NonEditable2() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "2", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3NonEditable3() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "3", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3NonEditable4() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "4", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV3NonEditable0() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "0", Constants.V_3_1));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void testConsoServicesDeclarationV2Editable1() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "1", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(1));
  }

  @Test
  void testConsoServicesDeclarationV2Editable2() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "2", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(1));
  }

  @Test
  void testConsoServicesDeclarationV2Editable3() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "3", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(1));
  }

  @Test
  void testConsoServicesDeclarationV2Editable4() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "4", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(Constants.CARTE_TP, result.get(0));
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(1));
  }

  @Test
  void testConsoServicesDeclarationV2NonEditable1() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "1", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV2NonEditable2() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "2", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV2NonEditable3() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "3", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  @Test
  void testConsoServicesDeclarationV2NonEditable4() {
    List<Declaration> declarationList = new ArrayList<>();
    declarationList.add(getDeclaration("123456", "NUMEROCONTRAT", "4", "V2"));

    List<String> result =
        DeclarationConsolideUtils.consolidateServicesDeclarations(
            declarationList, declarantCarteNonEditable);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(Constants.CARTE_DEMATERIALISEE, result.get(0));
  }

  private Declaration getDeclaration(
      String amc, String numerocontrat, String carteTPaEditerOuDigitale, String version) {
    Declaration declaration = new Declaration();
    declaration.setCodeEtat(Constants.CODE_ETAT_VALIDE);
    declaration.setVersionDeclaration(version);
    declaration.setCarteTPaEditerOuDigitale(carteTPaEditerOuDigitale);
    declaration.setIdDeclarant(amc);
    declaration.setIsCarteTPaEditer(Boolean.TRUE);
    Contrat contrat = new Contrat();
    contrat.setNumero(numerocontrat);
    declaration.setContrat(contrat);
    return declaration;
  }

  private Declarant getDeclarant(
      String idDeclarant,
      boolean isEditable,
      boolean hasSynchroDate,
      boolean closeCarteTP,
      boolean closeCarteDemat) {
    Declarant declarant = new Declarant();
    declarant.set_id(idDeclarant);
    List<Pilotage> pilotageList = new ArrayList<>();
    pilotageList.add(getPilotage("Priorisation"));
    pilotageList.add(getPilotage("Visiodroit"));
    pilotageList.add(getPilotage("Dclben"));
    pilotageList.add(getPilotage("Tpgis"));
    pilotageList.add(getPilotage("Tpgsp"));
    pilotageList.add(getPilotage("Almv3"));
    pilotageList.add(getPilotage(Constants.CARTE_TP, isEditable, hasSynchroDate, closeCarteTP));
    pilotageList.add(getPilotage(Constants.CARTE_DEMATERIALISEE, true, false, closeCarteDemat));
    pilotageList.add(getPilotage("Roc"));
    pilotageList.add(getPilotage("Seltp"));
    declarant.setPilotages(pilotageList);
    return declarant;
  }

  private Pilotage getPilotage(String codeService) {
    Pilotage pilotage = new Pilotage();
    pilotage.setCodeService(codeService);
    return pilotage;
  }

  private Pilotage getPilotage(
      String codeService, boolean isEditable, boolean hasSynchroDate, boolean closed) {
    Pilotage pilotage = new Pilotage();
    pilotage.setCodeService(codeService);
    pilotage.setServiceOuvert(true);
    pilotage.setIsCarteEditable(isEditable);
    if (hasSynchroDate) {
      pilotage.setDateSynchronisation(new Date());
    }
    if (closed) {
      pilotage.setDateOuverture(DateUtils.parseDate("2034/04/01", DateUtils.FORMATTERSLASHED));
    } else {
      pilotage.setDateOuverture(DateUtils.parseDate("2022/01/01", DateUtils.FORMATTERSLASHED));
    }
    return pilotage;
  }

  @Test
  void testPeriodesWithoutSuspensions1() {
    // Cas pas de suspension
    List<Periode> periodesSuspension = List.of(new Periode("2024/01/01", "2024/01/31"));
    Periode periodeDecl = new Periode("2024/03/01", "2024/03/31");
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            periodesSuspension, periodeDecl);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/03/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/03/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions2() {
    // Cas suspension sur une journée (début période)
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/01/01", "2024/02/01")),
            new Periode("2024/02/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/02/02", result.get(0).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions3() {
    // Cas suspension en début de période (et avant)
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/01/01", "2024/02/15")),
            new Periode("2024/02/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/02/16", result.get(0).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions4() {
    // Cas suspension en début de période
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/01/01", "2024/01/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/02/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions5() {
    // Cas suspension en milieu de période
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/02/01", "2024/02/29")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
    Assertions.assertEquals("2024/03/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(1).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions6() {
    // Cas suspension en fin de période
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/02/01", "2024/03/01")),
            new Periode("2024/01/01", "2024/03/01"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions7() {
    // Cas suspension sur la fin de période et plus
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/02/01", "2024/03/31")),
            new Periode("2024/01/01", "2024/03/01"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions8() {
    // Cas suspension sur une journée (periodeFin)
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/03/01", "2024/03/15")),
            new Periode("2024/01/01", "2024/03/01"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/02/29", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions9() {
    // Cas pas de suspensions
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/03/01", "2024/03/31")),
            new Periode("2024/01/01", "2024/01/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions10() {
    // Cas suspension complete
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/01/01", "2024/01/31")),
            new Periode("2024/01/01", "2024/01/31"));
    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void testPeriodesWithoutSuspensions11() {
    // Cas multiples suspensions
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(
                new Periode("2024/01/01", "2024/01/31"), new Periode("2024/03/01", "2024/03/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("2024/02/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/02/29", result.get(0).getFin());
    Assertions.assertEquals("2024/04/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(1).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions12() {
    // Cas multiples suspensions
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(
                new Periode("2024/02/01", "2024/02/29"), new Periode("2024/03/01", "2024/03/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
    Assertions.assertEquals("2024/04/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(1).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions13() {
    // Cas multiples suspensions
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(
                new Periode("2024/02/01", "2024/02/29"), new Periode("2024/05/01", "2024/05/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
    Assertions.assertEquals("2024/03/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/04/30", result.get(1).getFin());
    Assertions.assertEquals("2024/06/01", result.get(2).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(2).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions14() {
    // Cas multiples suspensions
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(
                new Periode("2024/02/01", "2024/02/29"),
                new Periode("2024/05/01", "2024/05/31"),
                new Periode("2024/12/01", "2024/12/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
    Assertions.assertEquals("2024/03/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/04/30", result.get(1).getFin());
    Assertions.assertEquals("2024/06/01", result.get(2).getDebut());
    Assertions.assertEquals("2024/11/30", result.get(2).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions15() {
    // Cas multiples suspensions
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(
                new Periode("2024/02/01", "2024/02/29"),
                new Periode("2024/05/01", "2024/05/31"),
                new Periode("2024/12/01", "2024/12/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
    Assertions.assertEquals("2024/03/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/04/30", result.get(1).getFin());
    Assertions.assertEquals("2024/06/01", result.get(2).getDebut());
    Assertions.assertEquals("2024/11/30", result.get(2).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions16() {
    // Cas multiples suspensions + une sans date de fin
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(
                new Periode("2024/02/01", "2024/02/29"),
                new Periode("2024/05/01", null),
                new Periode("2024/12/01", "2024/12/31")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
    Assertions.assertEquals("2024/03/01", result.get(1).getDebut());
    Assertions.assertEquals("2024/04/30", result.get(1).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions17() {
    // Cas suspension sans date de fin
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/02/01", null)), new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/01/31", result.get(0).getFin());
  }

  @Test
  void testPeriodesWithoutSuspensions18() {
    // Cas suspension sans date de fin
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/01/01", null)), new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void testPeriodesWithoutSuspensions19() {
    // Cas suspension avec dates inversees
    List<Periode> result =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            List.of(new Periode("2024/03/01", "2024/02/29")),
            new Periode("2024/01/01", "2024/12/31"));
    Assertions.assertTrue(CollectionUtils.isNotEmpty(result));
    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals("2024/01/01", result.get(0).getDebut());
    Assertions.assertEquals("2024/12/31", result.get(0).getFin());
  }
}
