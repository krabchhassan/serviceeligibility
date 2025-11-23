package com.cegedim.next.serviceeligibility.core.bobb.api.contract;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.bobb.lot.LotXlsxService;
import com.cegedim.next.serviceeligibility.core.bobb.model.export.Content;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.utility.error.UnexpectedFileException;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
public class LotXlsxServiceTest {

  private static final String CODE_ASS = "ASSU00983";
  private static final String CODE_GARANTIE = "GT_ASS_100";
  private static final String LOT_LABEL = "Mon lot 1000";
  private static final String CODE_LOT = "code_lot";
  private static final String LIBELLE_LOT = "libelle_lot";
  private static final String CODE_GT = "code_gt";
  private static final String CODE_ASSUREUR = "code_assureur";
  private static final String LINE_NUMBER = "lineNumber";
  private static final String SHEET_NAME = "export_lot";

  private final LocalDateTime currentDate = LocalDateTime.now();
  private final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
  private String now = currentDate.format(formatter);

  @Autowired private LotXlsxService lotXlsxService;

  @Autowired private ContractElementService contractElementService;

  @Test
  void emptyCodeLotTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "");
    stringObjectMap.put(LIBELLE_LOT, LOT_LABEL);
    stringObjectMap.put(CODE_GT, CODE_GARANTIE);
    stringObjectMap.put(CODE_ASSUREUR, CODE_ASS);
    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(thrown.getMessage().contains("Ligne 2, le code lot est obligatoire"));
  }

  @Test
  void lotWithSpacesTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, " Lot_Avec_Espaces ");
    stringObjectMap.put(LIBELLE_LOT, " Libelle avec espaces ");
    stringObjectMap.put(CODE_GT, " GT_123 ");
    stringObjectMap.put(CODE_ASSUREUR, " Code_Assu ");
    ContractElement contractElement = new ContractElement();
    Mockito.when(
            contractElementService.get(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(contractElement);

    lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0);

    Assertions.assertTrue(lotCodeWithObject.containsKey("Lot_Avec_Espaces"));
    Assertions.assertEquals(
        "Libelle avec espaces", lotCodeWithObject.get("Lot_Avec_Espaces").getLibelle());
    Assertions.assertNotNull(lotCodeWithObject.get("Lot_Avec_Espaces").getGarantieTechniques());
    List<GarantieTechnique> garantieTechniques =
        lotCodeWithObject.get("Lot_Avec_Espaces").getGarantieTechniques();
    Assertions.assertEquals(1, garantieTechniques.size());
    Assertions.assertEquals("GT_123", garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals("Code_Assu", garantieTechniques.get(0).getCodeAssureur());
  }

  @Test
  void emptyLibelleLotTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, "");
    stringObjectMap.put(CODE_GT, CODE_GARANTIE);
    stringObjectMap.put(CODE_ASSUREUR, CODE_ASS);
    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(thrown.getMessage().contains("Ligne 2, le libelle lot est obligatoire"));
  }

  @Test
  void emptyCodeGTTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, LIBELLE_LOT);
    stringObjectMap.put(CODE_GT, "");
    stringObjectMap.put(CODE_ASSUREUR, CODE_ASS);
    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(thrown.getMessage().contains("Ligne 2, le code GT est obligatoire"));
  }

  @Test
  void emptyCodeAssureurTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, LIBELLE_LOT);
    stringObjectMap.put(CODE_GT, CODE_GARANTIE);
    stringObjectMap.put(CODE_ASSUREUR, "");
    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(
        thrown.getMessage().contains("Ligne 2, le code assureur est obligatoire"));
  }

  @Test
  void processExcelLineNonExistingGTTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, LOT_LABEL);
    stringObjectMap.put(CODE_GT, CODE_GARANTIE);
    stringObjectMap.put(CODE_ASSUREUR, CODE_ASS);
    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(
        thrown.getMessage().contains("Ligne 2, la garantie technique n'existe pas"));
  }

  @Test
  void processExcelLineNewLotTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, LOT_LABEL);
    stringObjectMap.put(CODE_GT, CODE_GARANTIE);
    stringObjectMap.put(CODE_ASSUREUR, CODE_ASS);

    ContractElement contractElement = new ContractElement();
    Mockito.when(contractElementService.get(CODE_GARANTIE, CODE_ASS, true))
        .thenReturn(contractElement);

    lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0);
    Assertions.assertTrue(lotCodeWithObject.containsKey("LOT_1000"));
    Assertions.assertEquals(LOT_LABEL, lotCodeWithObject.get("LOT_1000").getLibelle());
    Assertions.assertNotNull(lotCodeWithObject.get("LOT_1000").getGarantieTechniques());
    List<GarantieTechnique> garantieTechniques =
        lotCodeWithObject.get("LOT_1000").getGarantieTechniques();
    Assertions.assertEquals(1, garantieTechniques.size());
    Assertions.assertEquals(CODE_GARANTIE, garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals(CODE_ASS, garantieTechniques.get(0).getCodeAssureur());
  }

  @Test
  void processExcelLineAlreadyProcessedLotTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Lot lot = createLot();
    lotCodeWithObject.put(lot.getCode(), lot);

    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, LOT_LABEL);
    stringObjectMap.put(CODE_GT, "GT_ASS_102");
    stringObjectMap.put(CODE_ASSUREUR, "ASSU00983");

    ContractElement contractElement = new ContractElement();
    Mockito.when(
            contractElementService.get(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(contractElement);

    lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0);

    Assertions.assertTrue(lotCodeWithObject.containsKey("LOT_1000"));
    Assertions.assertEquals(LOT_LABEL, lotCodeWithObject.get("LOT_1000").getLibelle());
    Assertions.assertNotNull(lotCodeWithObject.get("LOT_1000").getGarantieTechniques());
    List<GarantieTechnique> garantieTechniques =
        lotCodeWithObject.get("LOT_1000").getGarantieTechniques();
    Assertions.assertEquals(3, garantieTechniques.size());
    Assertions.assertEquals(CODE_GARANTIE, garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals(CODE_ASS, garantieTechniques.get(0).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_101", garantieTechniques.get(1).getCodeGarantie());
    Assertions.assertEquals("ASSU00982", garantieTechniques.get(1).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_102", garantieTechniques.get(2).getCodeGarantie());
    Assertions.assertEquals("ASSU00983", garantieTechniques.get(2).getCodeAssureur());
  }

  @Test
  void processExcelLineSameGTTwiceTest() {
    final Map<String, Lot> lotCodeWithObject = new HashMap<>();
    Lot lot = createLot();
    lotCodeWithObject.put(lot.getCode(), lot);

    Map<String, Object> stringObjectMap = new HashMap<>();
    stringObjectMap.put(CODE_LOT, "LOT_1000");
    stringObjectMap.put(LIBELLE_LOT, LOT_LABEL);
    stringObjectMap.put(CODE_GT, "GT_ASS_101");
    stringObjectMap.put(CODE_ASSUREUR, "ASSU00982");

    ContractElement contractElement = new ContractElement();
    Mockito.when(
            contractElementService.get(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(contractElement);

    lotXlsxService.getOrCreateLotElement(lotCodeWithObject, stringObjectMap, 0);

    Assertions.assertTrue(lotCodeWithObject.containsKey("LOT_1000"));
    Assertions.assertEquals(LOT_LABEL, lotCodeWithObject.get("LOT_1000").getLibelle());
    Assertions.assertNotNull(lotCodeWithObject.get("LOT_1000").getGarantieTechniques());
    List<GarantieTechnique> garantieTechniques =
        lotCodeWithObject.get("LOT_1000").getGarantieTechniques();
    Assertions.assertEquals(2, garantieTechniques.size());
    Assertions.assertEquals(CODE_GARANTIE, garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals(CODE_ASS, garantieTechniques.get(0).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_101", garantieTechniques.get(1).getCodeGarantie());
    Assertions.assertEquals("ASSU00982", garantieTechniques.get(1).getCodeAssureur());
  }

  @Test
  void createLinesTest() {
    List<Map<String, Object>> list = new ArrayList<>();
    Lot lot = createLot();
    final AtomicInteger counter = new AtomicInteger(1);
    lotXlsxService.createLotLines(list, lot, counter);
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals("LOT_1000", list.get(0).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, list.get(0).get(LIBELLE_LOT));
    Assertions.assertEquals(CODE_GARANTIE, list.get(0).get(CODE_GT));
    Assertions.assertEquals(CODE_ASS, list.get(0).get(CODE_ASSUREUR));
    Assertions.assertEquals("LOT_1000", list.get(1).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, list.get(1).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_101", list.get(1).get(CODE_GT));
    Assertions.assertEquals("ASSU00982", list.get(1).get(CODE_ASSUREUR));
  }

  private static Lot createLot() {
    Lot lot = new Lot();
    lot.setCode("LOT_1000");
    lot.setLibelle(LOT_LABEL);
    List<GarantieTechnique> garantieTechniqueList = new ArrayList<>();
    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie(CODE_GARANTIE);
    garantieTechnique1.setCodeAssureur(CODE_ASS);
    garantieTechniqueList.add(garantieTechnique1);
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_101");
    garantieTechnique2.setCodeAssureur("ASSU00982");
    garantieTechniqueList.add(garantieTechnique2);
    lot.setGarantieTechniques(garantieTechniqueList);
    return lot;
  }

  @Test
  void createLinesWithSuspendedLotsTest() {
    List<Map<String, Object>> list = new ArrayList<>();
    Lot lot = createLot();

    GarantieTechnique garantieTechnique3 = new GarantieTechnique();
    garantieTechnique3.setCodeGarantie("GT_ASS_102");
    garantieTechnique3.setCodeAssureur("ASSU00984");
    lot.getGarantieTechniques().add(garantieTechnique3);
    GarantieTechnique garantieTechnique4 = new GarantieTechnique();
    garantieTechnique4.setCodeGarantie("GT_ASS_103");
    garantieTechnique4.setCodeAssureur("ASSU00985");

    garantieTechnique4.setDateSuppressionLogique(now);
    lot.getGarantieTechniques().add(garantieTechnique4);

    final AtomicInteger counter = new AtomicInteger(1);
    LotXlsxService.createLotLines(list, lot, counter);
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(LOT_LABEL, list.get(0).get(LIBELLE_LOT));
    Assertions.assertEquals(CODE_GARANTIE, list.get(0).get(CODE_GT));
    Assertions.assertEquals(CODE_ASS, list.get(0).get(CODE_ASSUREUR));
    Assertions.assertEquals("LOT_1000", list.get(1).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, list.get(1).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_101", list.get(1).get(CODE_GT));
    Assertions.assertEquals("ASSU00982", list.get(1).get(CODE_ASSUREUR));
    Assertions.assertEquals("LOT_1000", list.get(2).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, list.get(2).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_102", list.get(2).get(CODE_GT));
    Assertions.assertEquals("ASSU00984", list.get(2).get(CODE_ASSUREUR));
  }

  @Test
  void buildLotContentTest() {
    final AtomicInteger counter = new AtomicInteger(1);
    Content content = lotXlsxService.buildLotContent(SHEET_NAME, createLot(), counter);
    Assertions.assertEquals(SHEET_NAME, content.getSheetName());
    Assertions.assertEquals(2, content.getSheetLinesNumber());
    List<Map<String, Object>> sheetContent = content.getSheetContent();
    Assertions.assertEquals("LOT_1000", sheetContent.get(0).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, sheetContent.get(0).get(LIBELLE_LOT));
    Assertions.assertEquals(CODE_GARANTIE, sheetContent.get(0).get(CODE_GT));
    Assertions.assertEquals(CODE_ASS, sheetContent.get(0).get(CODE_ASSUREUR));
    Assertions.assertEquals(1, sheetContent.get(0).get(LINE_NUMBER));
    Assertions.assertEquals("LOT_1000", sheetContent.get(1).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, sheetContent.get(1).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_101", sheetContent.get(1).get(CODE_GT));
    Assertions.assertEquals("ASSU00982", sheetContent.get(1).get(CODE_ASSUREUR));
    Assertions.assertEquals(2, sheetContent.get(1).get(LINE_NUMBER));
  }

  @Test
  void buildLotListContentTest() {
    final AtomicInteger counter = new AtomicInteger(1);
    List<Lot> lotList = new ArrayList<>();
    lotList.add(createLot());
    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_103");
    garantieTechnique2.setCodeAssureur("ASSU00985");

    lot.setGarantieTechniques(List.of(garantieTechnique1, garantieTechnique2));
    lotList.add(lot);

    Content content = lotXlsxService.buildLotListContent(SHEET_NAME, lotList, counter);
    Assertions.assertEquals(SHEET_NAME, content.getSheetName());
    Assertions.assertEquals(4, content.getSheetLinesNumber());
    List<Map<String, Object>> sheetContent = content.getSheetContent();
    Assertions.assertEquals("LOT_1000", sheetContent.get(0).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, sheetContent.get(0).get(LIBELLE_LOT));
    Assertions.assertEquals(CODE_GARANTIE, sheetContent.get(0).get(CODE_GT));
    Assertions.assertEquals(CODE_ASS, sheetContent.get(0).get(CODE_ASSUREUR));
    Assertions.assertEquals(1, sheetContent.get(0).get(LINE_NUMBER));
    Assertions.assertEquals("LOT_1000", sheetContent.get(1).get(CODE_LOT));
    Assertions.assertEquals(LOT_LABEL, sheetContent.get(1).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_101", sheetContent.get(1).get(CODE_GT));
    Assertions.assertEquals("ASSU00982", sheetContent.get(1).get(CODE_ASSUREUR));
    Assertions.assertEquals(2, sheetContent.get(1).get(LINE_NUMBER));
    Assertions.assertEquals("LOT_1001", sheetContent.get(2).get(CODE_LOT));
    Assertions.assertEquals("Mon lot 1001", sheetContent.get(2).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_102", sheetContent.get(2).get(CODE_GT));
    Assertions.assertEquals("ASSU00984", sheetContent.get(2).get(CODE_ASSUREUR));
    Assertions.assertEquals(3, sheetContent.get(2).get(LINE_NUMBER));
    Assertions.assertEquals("LOT_1001", sheetContent.get(3).get(CODE_LOT));
    Assertions.assertEquals("Mon lot 1001", sheetContent.get(3).get(LIBELLE_LOT));
    Assertions.assertEquals("GT_ASS_103", sheetContent.get(3).get(CODE_GT));
    Assertions.assertEquals("ASSU00985", sheetContent.get(3).get(CODE_ASSUREUR));
    Assertions.assertEquals(4, sheetContent.get(3).get(LINE_NUMBER));
  }

  @Test
  void createNewLotFailsTest() {

    List<Lot> importedList = new ArrayList<>();

    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");

    lot.setGarantieTechniques(List.of(garantieTechnique1));

    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.createNewLot(importedList, now, lot),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(
        thrown.getMessage().contains("Il faut au moins 2 garanties techniques par lot"));
  }

  @Test
  void createNewLotTest() {
    List<Lot> importedList = new ArrayList<>();

    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_103");
    garantieTechnique2.setCodeAssureur("ASSU00985");

    lot.setGarantieTechniques(List.of(garantieTechnique1, garantieTechnique2));

    lotXlsxService.createNewLot(importedList, now, lot);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(importedList));
    Assertions.assertEquals(1, importedList.size());
    Assertions.assertEquals("LOT_1001", importedList.get(0).getCode());
    Assertions.assertEquals("Mon lot 1001", importedList.get(0).getLibelle());
    List<GarantieTechnique> garantieTechniques = importedList.get(0).getGarantieTechniques();
    Assertions.assertNotNull(garantieTechniques);
    Assertions.assertEquals("GT_ASS_102", garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals("ASSU00984", garantieTechniques.get(0).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_103", garantieTechniques.get(1).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(1).getCodeAssureur());
  }

  @Test
  void updateExistingLotWarrantyAlreadyExistsTest() {
    List<Lot> importedList = new ArrayList<>();

    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    List<GarantieTechnique> garantieTechniquesLot = new ArrayList<>();
    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");
    garantieTechniquesLot.add(garantieTechnique1);
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_103");
    garantieTechnique2.setCodeAssureur("ASSU00985");
    garantieTechniquesLot.add(garantieTechnique2);
    lot.setGarantieTechniques(garantieTechniquesLot);

    Lot savedLot = new Lot();
    savedLot.setCode("LOT_1001");
    savedLot.setLibelle("Mon lot 1001 bis");

    List<GarantieTechnique> garantieTechniquesSavedLot = new ArrayList<>();
    GarantieTechnique garantieTechnique3 = new GarantieTechnique();
    garantieTechnique3.setCodeGarantie("GT_ASS_102");
    garantieTechnique3.setCodeAssureur("ASSU00984");
    garantieTechniquesSavedLot.add(garantieTechnique3);
    GarantieTechnique garantieTechnique4 = new GarantieTechnique();
    garantieTechnique4.setCodeGarantie("GT_ASS_103");
    garantieTechnique4.setCodeAssureur("ASSU00985");
    garantieTechnique4.setDateSuppressionLogique("2023/05/06");
    garantieTechniquesSavedLot.add(garantieTechnique4);
    GarantieTechnique garantieTechnique5 = new GarantieTechnique();
    garantieTechnique5.setCodeGarantie("GT_ASS_104");
    garantieTechnique5.setCodeAssureur("ASSU00986");
    garantieTechniquesSavedLot.add(garantieTechnique5);
    savedLot.setGarantieTechniques(garantieTechniquesSavedLot);

    lotXlsxService.updateExistingLot(importedList, now, lot, savedLot);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(importedList));
    Assertions.assertEquals(1, importedList.size());
    Assertions.assertEquals("LOT_1001", importedList.get(0).getCode());
    Assertions.assertEquals("Mon lot 1001", importedList.get(0).getLibelle());
    List<GarantieTechnique> garantieTechniques = importedList.get(0).getGarantieTechniques();
    Assertions.assertNotNull(garantieTechniques);
    Assertions.assertEquals(4, garantieTechniques.size());
    Assertions.assertEquals("GT_ASS_102", garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals("ASSU00984", garantieTechniques.get(0).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_103", garantieTechniques.get(1).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(1).getCodeAssureur());
    Assertions.assertNotNull(garantieTechniques.get(1).getDateSuppressionLogique());
    Assertions.assertEquals("GT_ASS_104", garantieTechniques.get(2).getCodeGarantie());
    Assertions.assertEquals("ASSU00986", garantieTechniques.get(2).getCodeAssureur());
    Assertions.assertNotNull(garantieTechniques.get(2).getDateSuppressionLogique());
    Assertions.assertEquals("GT_ASS_103", garantieTechniques.get(3).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(3).getCodeAssureur());
    Assertions.assertNull(garantieTechniques.get(3).getDateSuppressionLogique());
  }

  @Test
  void updateExistingLotWarrantyDoesNotExistTest() {
    List<Lot> importedList = new ArrayList<>();

    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    List<GarantieTechnique> garantieTechniqueList = new ArrayList<>();
    GarantieTechnique garantieTechnique3 = new GarantieTechnique();
    garantieTechnique3.setCodeGarantie("GT_ASS_102");
    garantieTechnique3.setCodeAssureur("ASSU00984");
    garantieTechniqueList.add(garantieTechnique3);
    GarantieTechnique garantieTechnique4 = new GarantieTechnique();
    garantieTechnique4.setCodeGarantie("GT_ASS_103");
    garantieTechnique4.setCodeAssureur("ASSU00985");
    garantieTechniqueList.add(garantieTechnique4);
    GarantieTechnique garantieTechnique5 = new GarantieTechnique();
    garantieTechnique5.setCodeGarantie("GT_ASS_104");
    garantieTechnique5.setCodeAssureur("ASSU00986");
    garantieTechniqueList.add(garantieTechnique5);
    lot.setGarantieTechniques(garantieTechniqueList);

    Lot existingLot = new Lot();
    existingLot.setCode("LOT_1001");
    existingLot.setLibelle("Mon lot 1001 bis");

    List<GarantieTechnique> garantieTechniquesExistingLot = new ArrayList<>();
    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");
    garantieTechniquesExistingLot.add(garantieTechnique1);
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_103");
    garantieTechnique2.setCodeAssureur("ASSU00985");
    garantieTechniquesExistingLot.add(garantieTechnique2);
    existingLot.setGarantieTechniques(garantieTechniquesExistingLot);

    lotXlsxService.updateExistingLot(importedList, now, lot, existingLot);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(importedList));
    Assertions.assertEquals(1, importedList.size());
    Assertions.assertEquals("LOT_1001", importedList.get(0).getCode());
    Assertions.assertEquals("Mon lot 1001", importedList.get(0).getLibelle());
    List<GarantieTechnique> garantieTechniques = importedList.get(0).getGarantieTechniques();
    Assertions.assertNotNull(garantieTechniques);
    Assertions.assertEquals(3, garantieTechniques.size());
    Assertions.assertEquals("GT_ASS_102", garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals("ASSU00984", garantieTechniques.get(0).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_103", garantieTechniques.get(1).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(1).getCodeAssureur());
    Assertions.assertNull(garantieTechniques.get(1).getDateSuppressionLogique());
    Assertions.assertEquals("GT_ASS_104", garantieTechniques.get(2).getCodeGarantie());
    Assertions.assertEquals("ASSU00986", garantieTechniques.get(2).getCodeAssureur());
    Assertions.assertNull(garantieTechniques.get(2).getDateSuppressionLogique());
    Assertions.assertNotNull(garantieTechniques.get(2).getDateAjout());
  }

  @Test
  void updateExistingLotNotEnoughWarrantyTest() {
    List<Lot> importedList = new ArrayList<>();

    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    List<GarantieTechnique> garantieTechniques = new ArrayList<>();
    GarantieTechnique garantieTechnique = new GarantieTechnique();
    garantieTechnique.setCodeGarantie("GT_ASS_102");
    garantieTechnique.setCodeAssureur("ASSU00984");
    garantieTechniques.add(garantieTechnique);
    lot.setGarantieTechniques(garantieTechniques);

    Lot existingLot = new Lot();
    existingLot.setCode("LOT_1001");
    existingLot.setLibelle("Mon lot 1001 bis");

    List<GarantieTechnique> garantieTechniquesLot = new ArrayList<>();
    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");
    garantieTechniquesLot.add(garantieTechnique1);
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_103");
    garantieTechnique2.setCodeAssureur("ASSU00985");
    garantieTechniquesLot.add(garantieTechnique2);
    existingLot.setGarantieTechniques(garantieTechniquesLot);

    UnexpectedFileException thrown =
        Assertions.assertThrows(
            UnexpectedFileException.class,
            () -> lotXlsxService.updateExistingLot(importedList, now, lot, existingLot),
            "Expected UnexpectedFileException to be thrown but it didn't");

    Assertions.assertTrue(
        thrown.getMessage().contains("Il faut au moins 2 garanties techniques actives par lot"));
  }

  @Test
  void updateExistingLotNoDuplicatesTest() {
    List<Lot> importedList = new ArrayList<>();

    Lot lot = new Lot();
    lot.setCode("LOT_1001");
    lot.setLibelle("Mon lot 1001");

    // Lot avec ajout de la gt GT_ASS_105 et suppression logique de la GT_ASS_104
    List<GarantieTechnique> garantieTechniquesLot = new ArrayList<>();
    GarantieTechnique garantieTechnique1 = new GarantieTechnique();
    garantieTechnique1.setCodeGarantie("GT_ASS_102");
    garantieTechnique1.setCodeAssureur("ASSU00984");
    garantieTechniquesLot.add(garantieTechnique1);
    GarantieTechnique garantieTechnique2 = new GarantieTechnique();
    garantieTechnique2.setCodeGarantie("GT_ASS_103");
    garantieTechnique2.setCodeAssureur("ASSU00985");
    garantieTechniquesLot.add(garantieTechnique2);
    GarantieTechnique garantieTechnique7 = new GarantieTechnique();
    garantieTechnique7.setCodeGarantie("GT_ASS_105");
    garantieTechnique7.setCodeAssureur("ASSU00985");
    garantieTechniquesLot.add(garantieTechnique7);
    lot.setGarantieTechniques(garantieTechniquesLot);

    Lot savedLot = new Lot();
    savedLot.setCode("LOT_1001");
    savedLot.setLibelle("Mon lot 1001");

    List<GarantieTechnique> garantieTechniquesSavedLot = new ArrayList<>();
    GarantieTechnique garantieTechnique3 = new GarantieTechnique();
    garantieTechnique3.setCodeGarantie("GT_ASS_102");
    garantieTechnique3.setCodeAssureur("ASSU00984");
    garantieTechniquesSavedLot.add(garantieTechnique3);
    GarantieTechnique garantieTechnique4 = new GarantieTechnique();
    garantieTechnique4.setCodeGarantie("GT_ASS_103");
    garantieTechnique4.setCodeAssureur("ASSU00985");
    garantieTechnique4.setDateSuppressionLogique("2023/05/06");
    garantieTechniquesSavedLot.add(garantieTechnique4);
    GarantieTechnique garantieTechnique5 = new GarantieTechnique();
    garantieTechnique5.setCodeGarantie("GT_ASS_104");
    garantieTechnique5.setCodeAssureur("ASSU00986");
    garantieTechniquesSavedLot.add(garantieTechnique5);
    GarantieTechnique garantieTechnique6 = new GarantieTechnique();
    garantieTechnique6.setCodeGarantie("GT_ASS_103");
    garantieTechnique6.setCodeAssureur("ASSU00985");
    garantieTechniquesSavedLot.add(garantieTechnique6);
    savedLot.setGarantieTechniques(garantieTechniquesSavedLot);

    lotXlsxService.updateExistingLot(importedList, now, lot, savedLot);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(importedList));
    Assertions.assertEquals(1, importedList.size());
    Assertions.assertEquals("LOT_1001", importedList.get(0).getCode());
    Assertions.assertEquals("Mon lot 1001", importedList.get(0).getLibelle());
    List<GarantieTechnique> garantieTechniques = importedList.get(0).getGarantieTechniques();
    Assertions.assertNotNull(garantieTechniques);
    Assertions.assertEquals(5, garantieTechniques.size());
    Assertions.assertEquals("GT_ASS_102", garantieTechniques.get(0).getCodeGarantie());
    Assertions.assertEquals("ASSU00984", garantieTechniques.get(0).getCodeAssureur());
    Assertions.assertEquals("GT_ASS_103", garantieTechniques.get(1).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(1).getCodeAssureur());
    Assertions.assertNotNull(garantieTechniques.get(1).getDateSuppressionLogique());
    Assertions.assertEquals("GT_ASS_104", garantieTechniques.get(2).getCodeGarantie());
    Assertions.assertEquals("ASSU00986", garantieTechniques.get(2).getCodeAssureur());
    Assertions.assertNotNull(garantieTechniques.get(2).getDateSuppressionLogique());
    Assertions.assertEquals("GT_ASS_103", garantieTechniques.get(3).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(3).getCodeAssureur());
    Assertions.assertNull(garantieTechniques.get(3).getDateSuppressionLogique());
    Assertions.assertEquals("GT_ASS_105", garantieTechniques.get(4).getCodeGarantie());
    Assertions.assertEquals("ASSU00985", garantieTechniques.get(4).getCodeAssureur());
    Assertions.assertNull(garantieTechniques.get(4).getDateSuppressionLogique());
  }
}
