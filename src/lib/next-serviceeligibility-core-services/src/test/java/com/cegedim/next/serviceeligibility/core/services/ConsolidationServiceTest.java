package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.consolidation.DeclarationConsolideService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ConsolidationServiceTest {

  @Autowired private DeclarationConsolideService declarationConsolideService;

  private static final String DECLARATION_PATH = "src/test/resources/620-declarations/";
  private static final String DECLARATION_CONSO_PATH =
      "src/test/resources/620-declarationConsolidees/";

  private static Calendar calendar;

  @BeforeAll
  static void initCalendar() {
    calendar = Calendar.getInstance();
    calendar.set(2024, Calendar.JANUARY, 1);
  }

  /**
   * Cas 1-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection declaration 3, une
   * declarationConsolidee
   */
  @Test
  void cas1_2() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas1-3.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas1-2.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /**
   * Cas 1-3 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 -> selection declaration
   * 4, une declarationConsolidee
   */
  @Test
  void cas1_3() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas1-4.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas1-3.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /**
   * Cas 2-1 Declaration 1 + Declaration 2 -> selection Declaration 2, pas de declarationConsolidee.
   * Cas 2-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection * Declaration 2, pas de
   * declarationConsolidee. Cas 2-3 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 ->
   * selection Declaration 2, pas de declarationConsolidee
   */
  @Test
  void cas2_1_cas2_2_cas2_3() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas2-2.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            true);
    Assertions.assertTrue(declarationConsolides.isEmpty());
  }

  /**
   * Cas 2-4 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 + Declaration 5 ->
   * selection Declaration 2 + declaration 5, 1 declarationConsolide
   */
  @Test
  void cas2_4() throws IOException {
    Declaration toProcess1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas2-2.json", Declaration.class);
    toProcess1.set_id("toto");
    Declaration toProcess2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas2-5.json", Declaration.class);
    toProcess2.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess1, toProcess2),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            true);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    // TODO test egalite avec resultat du talend
  }

  /** Cas 3-1 Declaration 1 + Declaration 2 -> selection Declaration 2, 1 declarationConso */
  @Test
  void cas3_1() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas3-2.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas3-1.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  private static DataForJob620 getDataForJob() {
    DataForJob620 dataForJob620 = new DataForJob620();
    dataForJob620.setIdentifiant("toto");
    dataForJob620.setToday(calendar.getTime());
    return dataForJob620;
  }

  /**
   * Cas 3-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection Declaration 2 et Declaration
   * 3, 2 declarationsConso
   */
  @Test
  void cas3_2() throws IOException {
    Declaration toProcess1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas3-2.json", Declaration.class);
    toProcess1.set_id("toto");
    Declaration toProcess2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas3-3.json", Declaration.class);
    toProcess2.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess1, toProcess2),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(2, declarationConsolides.size());

    DeclarationConsolide wanted1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas3-1.json", DeclarationConsolide.class);
    DeclarationConsolide wanted2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas3-2.json", DeclarationConsolide.class);

    checkSame(wanted1, declarationConsolides.get(0));
    checkSame(wanted2, declarationConsolides.get(1));
  }

  /** Cas 4-1 Declaration 1 -> Declaration 1, pas de declarationConso */
  @Test
  void cas4_1_cas4bis_1() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas4-1.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas4-1.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /**
   * Cas 4-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection Declaration 3, pas de
   * declarationConso
   */
  @Test
  void cas4_2() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas4-3.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas4-3.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /**
   * Cas 4-3 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 + Declaration 5 ->
   * selection Declaration 5, pas de declarationConso
   */
  @Test
  void cas4_3() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas4-3.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas4-3.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /** Cas 4-3 Declaration 2 -> selection Declaration 2, pas de declarationConso */
  @Test
  void cas4bis_2() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas4-2.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            true);
    Assertions.assertTrue(declarationConsolides.isEmpty());
  }

  /** Cas 5-1 Declaration 1 -> selection Declaration 1 */
  @Test
  void cas5_1() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-1.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas5-1.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /** Cas 5-2 Declaration 1 + Declaration 2 -> selection Declaration 2 */
  @Test
  void cas5_2() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-2.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas5-2.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  /** Cas 6-1 Déclaration avec 2 domaines et des dates différentes, 1 declarationConso */
  @Test
  void cas6_1() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas6-1.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas6-1.json", DeclarationConsolide.class);

    checkSame(wanted, declarationConsolides.get(0));
  }

  @Test
  void cas7_1() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas7-1.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(2, declarationConsolides.size());

    DeclarationConsolide wanted1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas7-1.json", DeclarationConsolide.class);
    DeclarationConsolide wanted2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas7-2.json", DeclarationConsolide.class);

    checkSame(wanted1, declarationConsolides.get(0));
    checkSame(wanted2, declarationConsolides.get(1));
  }

  @Test
  void cas8_1() throws IOException {
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas8-1.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(3, declarationConsolides.size());

    DeclarationConsolide wanted1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas8-1.json", DeclarationConsolide.class);
    DeclarationConsolide wanted2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas8-2.json", DeclarationConsolide.class);
    DeclarationConsolide wanted3 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas8-3.json", DeclarationConsolide.class);

    checkSame(wanted1, declarationConsolides.get(0));
    checkSame(wanted2, declarationConsolides.get(1));
    checkSame(wanted3, declarationConsolides.get(2));
  }

  @Test
  void cas_NC() throws IOException {
    // AUDI - NC XX - prio1
    // AUDI - NC XX - prio2
    // MEDE - NC XX - prio1
    // MEDE - PEC XX - prio2
    // DENT - 100 PO - prio1
    // DENT - NC XX - prio2
    // HOSP - 100 PO - prio2
    // HOSP - NC XX - prio3
    // HOSP - NC XX - prio1
    // SVIL - NC XX - prio1

    // carte demat générée avec :
    // SVIL - NC XX - prio1
    // HOSP - 100 PO - prio3
    // AUDI - NC XX - prio2
    // DENT - 100 PO - prio2
    // MEDE - PEC XX - prio2
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas_NC.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas_NC.json", DeclarationConsolide.class);

    checkSame(wanted1, declarationConsolides.get(0));
  }

  @Test
  void cas_NC_2() throws IOException {
    // HOSP - NC XX - prio1 => HOSP - 150 PO - prio4
    // HOSP - 100 PO - prio2
    // HOSP - NC XX - prio3
    // HOSP - 50 PO - prio4
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas_NC_2.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas_NC_2.json", DeclarationConsolide.class);

    checkSame(wanted1, declarationConsolides.get(0));
  }

  @Test
  void cas_DifferentUnits() throws IOException {
    // HOSP - 100 PO - prio1 => Error
    // HOSP - 50 FO - prio2
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas_DifferentUnits.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertTrue(declarationConsolides.isEmpty());
  }

  @Test
  void cas_DifferentUnits_withNC() throws IOException {
    // HOSP - 100 PO - prio1 => Error
    // HOSP - NC XX - prio2
    // HOSP - 50 FO - prio3
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas_DifferentUnits_withNC.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertTrue(declarationConsolides.isEmpty());
  }

  @Test
  void cas_NC_WithDelta() throws IOException {
    // HOSP - NC XX - prio1 - DELTA => HOSP - 100 PO - prio4
    // HOSP - 100 PO - prio2 - INCLU
    // HOSP - NC XX - prio3 - INCLU
    // HOSP - 50 PO - prio4 - INCLU
    Declaration toProcess =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas_NC_WithDelta.json", Declaration.class);
    toProcess.set_id("toto");
    List<DeclarationConsolide> declarationConsolides =
        declarationConsolideService.generateDeclarationConsolides(
            List.of(toProcess),
            getDataForJob(),
            List.of(Constants.CARTE_DEMATERIALISEE),
            new BulkActions(),
            false);
    Assertions.assertFalse(declarationConsolides.isEmpty());
    Assertions.assertEquals(1, declarationConsolides.size());

    DeclarationConsolide wanted1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas_NC_WithDelta.json",
            DeclarationConsolide.class);

    checkSame(wanted1, declarationConsolides.get(0));
  }

  private void checkSame(DeclarationConsolide wanted, DeclarationConsolide result) {
    wanted.setIdDeclarations(result.getIdDeclarations());
    wanted.setDateConsolidation(result.getDateConsolidation());
    wanted.setDateCreation(result.getDateCreation());
    wanted.setDateModification(result.getDateModification());
    wanted.setProduit(result.getProduit());

    Assertions.assertEquals(wanted, result);
  }
}
