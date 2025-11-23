package com.cegedim.next.serviceeligibility.core.services.cartedemat.carte;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class CartesServiceTest {

  @Autowired DeclarationService declarationService;

  @Autowired ProcessorCartesService cartesService;

  @Autowired ObjectMapper objectMapper;

  private static final DataForJob620 DATA_FOR_JOB = new DataForJob620();

  private static final String DECLARATION_CONSO_PATH =
      "src/test/resources/620-declarationConsolidees/";
  private static final String CARTE_DEMAT_PATH = "src/test/resources/620-CarteDemat/";

  @BeforeAll
  static void initDataForJob() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2024, Calendar.JANUARY, 1);
    DATA_FOR_JOB.setToday(calendar.getTime());
    DATA_FOR_JOB.setClientType(Constants.CLIENT_TYPE_INSURER);
  }

  private Declaration createDeclarationFromJson(String fileName) throws IOException {
    String filePath = "src/test/resources/620-declarations/";
    return UtilsForTesting.createTFromJson(filePath + fileName, Declaration.class);
  }

  /** Cas1-1 Declaration 1 + Declaration 2 -> aucune selection */
  @Test
  void cas1_1() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas1-1.json"));
    declarations.add(createDeclarationFromJson("declarationCas1-2.json"));
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertTrue(found.isEmpty());
  }

  /** Cas1-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection declaration 3 */
  @Test
  void cas1_2() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas1-1.json"));
    declarations.add(createDeclarationFromJson("declarationCas1-2.json"));
    Declaration declaration3 = createDeclarationFromJson("declarationCas1-3.json");
    declarations.add(declaration3);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration3, found.get(0));
  }

  /**
   * Cas1-3 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 -> selection declaration 4
   */
  @Test
  void cas1_3() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas1-1.json"));
    declarations.add(createDeclarationFromJson("declarationCas1-2.json"));
    declarations.add(createDeclarationFromJson("declarationCas1-3.json"));
    Declaration declaration4 = createDeclarationFromJson("declarationCas1-4.json");
    declarations.add(declaration4);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration4, found.get(0));
  }

  /** Cas2-1 Declaration 1 + Declaration 2 -> selection Declaration 2 */
  @Test
  void cas2_1() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas2-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas2-2.json");
    declarations.add(declaration2);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration2, found.get(0));
  }

  /** Cas2-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection Declaration 2 */
  @Test
  void cas2_2() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas2-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas2-2.json");
    declarations.add(declaration2);
    declarations.add(createDeclarationFromJson("declarationCas2-3.json"));
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration2, found.get(0));
  }

  /**
   * Cas2-3 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 -> selection Declaration 2
   */
  @Test
  void cas2_3() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas2-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas2-2.json");
    declarations.add(declaration2);
    declarations.add(createDeclarationFromJson("declarationCas2-3.json"));
    declarations.add(createDeclarationFromJson("declarationCas2-4.json"));
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration2, found.get(0));
  }

  /**
   * Cas2-4 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 + Declaration 5 ->
   * selection Declaration 2 + declaration 5
   */
  @Test
  void cas2_4() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas2-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas2-2.json");
    declarations.add(declaration2);
    declarations.add(createDeclarationFromJson("declarationCas2-3.json"));
    declarations.add(createDeclarationFromJson("declarationCas2-4.json"));
    Declaration declaration5 = createDeclarationFromJson("declarationCas2-5.json");
    declarations.add(declaration5);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(2, found.size());
    Assertions.assertEquals(declaration5, found.get(0));
    Assertions.assertEquals(declaration2, found.get(1));
  }

  /** Cas3-1 Declaration 1 + Declaration 2 -> selection Declaration 2 */
  @Test
  void cas3_1() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas3-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas3-2.json");
    declarations.add(declaration2);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration2, found.get(0));
  }

  /**
   * Cas3-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection Declaration 2 et Declaration
   * 3
   */
  @Test
  void cas3_2() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas3-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas3-2.json");
    declarations.add(declaration2);
    Declaration declaration3 = createDeclarationFromJson("declarationCas3-3.json");
    declarations.add(declaration3);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(2, found.size());
    Assertions.assertEquals(declaration3, found.get(0));
    Assertions.assertEquals(declaration2, found.get(1));
  }

  /** Cas4-1 Declaration 1 -> Declaration 1 */
  @Test
  void cas4_1() throws IOException {
    Declaration declaration1 = createDeclarationFromJson("declarationCas4-1.json");
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            List.of(declaration1), DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration1, found.get(0));
  }

  /** Cas4-2 Declaration 1 + Declaration 2 + Declaration 3 -> selection Declaration 3 */
  @Test
  void cas4_2() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas4-1.json"));
    declarations.add(createDeclarationFromJson("declarationCas4-2.json"));
    Declaration declaration3 = createDeclarationFromJson("declarationCas4-3.json");
    declarations.add(declaration3);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(declaration3, found.get(0));
  }

  /**
   * Cas4-3 Declaration 1 + Declaration 2 + Declaration 3 + Declaration 4 + Declaration 5 ->
   * selection Declaration 3
   */
  @Test
  void cas4_3() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas4-1.json"));
    declarations.add(createDeclarationFromJson("declarationCas4-2.json"));
    Declaration declaration3 = createDeclarationFromJson("declarationCas4-3.json");
    declarations.add(createDeclarationFromJson("declarationCas4-4.json"));
    declarations.add(createDeclarationFromJson("declarationCas4-5.json"));
    declarations.add(declaration3);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            declarations, DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(found.get(0), declaration3);
  }

  /** Cas5-2 Declaration 1 + Declaration 2 -> selection Declaration 2 */
  @Test
  void cas5_2() throws IOException {
    List<Declaration> declarations = new ArrayList<>();
    declarations.add(createDeclarationFromJson("declarationCas5-1.json"));
    Declaration declaration2 = createDeclarationFromJson("declarationCas5-2.json");
    declarations.add(declaration2);
    declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
    List<Declaration> found =
        declarationService.filterLatestDeclarationsForConso(
            List.of(declaration2), DATA_FOR_JOB.getToday(), DATA_FOR_JOB.getClientType());
    Assertions.assertEquals(1, found.size());
    Assertions.assertEquals(found.get(0), declaration2);
  }

  ////////////////////////
  //// Creation Cartes////
  ////////////////////////

  @Test
  void carte_cas6() throws IOException {
    List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
    DeclarationConsolide conso =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas6-1.json", DeclarationConsolide.class);
    declarationConsolides.add(conso);

    CarteDemat wanted =
        UtilsForTesting.createTFromJson(CARTE_DEMAT_PATH + "carteDematCas6.json", CarteDemat.class);
    List<CarteDemat> cartes =
        cartesService.processConsosContrat(
            declarationConsolides, DATA_FOR_JOB, getDeclarant(), new BulkActions(), new Periode());

    Assertions.assertEquals(1, cartes.size());
    checkSame(wanted, cartes.get(0));
  }

  private static Declarant getDeclarant() {
    Declarant declarant = new Declarant();
    declarant.setRegroupementDomainesTP(Collections.emptyList());
    return declarant;
  }

  @Test
  void carte_cas7() throws IOException {
    List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
    DeclarationConsolide conso1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas7-1.json", DeclarationConsolide.class);
    declarationConsolides.add(conso1);
    DeclarationConsolide conso2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas7-2.json", DeclarationConsolide.class);
    declarationConsolides.add(conso2);

    CarteDemat wanted1 =
        UtilsForTesting.createTFromJson(
            CARTE_DEMAT_PATH + "carteDematCas7-1.json", CarteDemat.class);
    CarteDemat wanted2 =
        UtilsForTesting.createTFromJson(
            CARTE_DEMAT_PATH + "carteDematCas7-2.json", CarteDemat.class);
    List<CarteDemat> cartes =
        cartesService.processConsosContrat(
            declarationConsolides, DATA_FOR_JOB, getDeclarant(), new BulkActions(), new Periode());

    Assertions.assertEquals(2, cartes.size());
    checkSame(wanted1, cartes.get(0));
    checkSame(wanted2, cartes.get(1));
  }

  @Test
  void carte_cas8() throws IOException {
    List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
    DeclarationConsolide conso1 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas8-1.json", DeclarationConsolide.class);
    declarationConsolides.add(conso1);
    DeclarationConsolide conso2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas8-2.json", DeclarationConsolide.class);
    declarationConsolides.add(conso2);
    DeclarationConsolide conso3 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-Cas8-3.json", DeclarationConsolide.class);
    declarationConsolides.add(conso3);

    CarteDemat wanted1 =
        UtilsForTesting.createTFromJson(
            CARTE_DEMAT_PATH + "carteDematCas8-1.json", CarteDemat.class);
    CarteDemat wanted2 =
        UtilsForTesting.createTFromJson(
            CARTE_DEMAT_PATH + "carteDematCas8-2.json", CarteDemat.class);
    CarteDemat wanted3 =
        UtilsForTesting.createTFromJson(
            CARTE_DEMAT_PATH + "carteDematCas8-3.json", CarteDemat.class);
    List<CarteDemat> cartes =
        cartesService.processConsosContrat(
            declarationConsolides, DATA_FOR_JOB, getDeclarant(), new BulkActions(), new Periode());

    Assertions.assertEquals(3, cartes.size());
    checkSame(wanted1, cartes.get(0));
    checkSame(wanted2, cartes.get(1));
    checkSame(wanted3, cartes.get(2));
  }

  private void checkSame(CarteDemat wanted, CarteDemat result) {
    wanted.setIdDeclarations(result.getIdDeclarations());
    wanted.setDateCreation(result.getDateCreation());
    wanted.setDateModification(result.getDateModification());
    wanted.setDateConsolidation(result.getDateConsolidation());
    // TODO Correction temporaire, changer les doc .json pour avoir le bon resultat
    result.getBeneficiaires().forEach(benef -> benef.setDomainesRegroup(null));
    wanted.setAdresse(result.getAdresse());
    result.setIdDeclarationsConsolides(wanted.getIdDeclarationsConsolides());

    String wantedJ = UtilsForTesting.toJson(wanted);
    String resJ = UtilsForTesting.toJson(result);
    Assertions.assertEquals(wanted, result);
  }

  @Test
  void testProcessConsosContrat() throws IOException {

    List<DeclarationConsolide> declarationConsolides = new ArrayList<>();
    DeclarationConsolide conso =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-CasFamilleDifferenteDate.json",
            DeclarationConsolide.class);
    DeclarationConsolide conso2 =
        UtilsForTesting.createTFromJson(
            DECLARATION_CONSO_PATH + "declaConso-CasFamilleDifferenteDate2.json",
            DeclarationConsolide.class);
    declarationConsolides.add(conso);
    declarationConsolides.add(conso2);
    List<CarteDemat> cartes =
        cartesService.processConsosContrat(
            declarationConsolides, DATA_FOR_JOB, getDeclarant(), new BulkActions(), new Periode());

    Assertions.assertEquals(2, cartes.size());
    // On doit avoir 2 cartes demat
    // 1 avec le benef 1 du 01/01/ au 28/02
    // 1 avec les 2 autres benefs du 01/03 au 31/12
  }
}
