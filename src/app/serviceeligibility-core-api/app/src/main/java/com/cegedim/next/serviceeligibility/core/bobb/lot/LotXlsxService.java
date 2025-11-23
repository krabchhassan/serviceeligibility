package com.cegedim.next.serviceeligibility.core.bobb.lot;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.EXCEL_API;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.bobb.XlsxUtils;
import com.cegedim.next.serviceeligibility.core.bobb.model.export.Content;
import com.cegedim.next.serviceeligibility.core.bobb.model.export.Document;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.LotService;
import com.cegedim.next.serviceeligibility.core.utility.error.InvalidParameterException;
import com.cegedim.next.serviceeligibility.core.utility.error.UnexpectedFileException;
import com.cegedim.next.serviceeligibility.core.utility.rest.RestConnectorUtils;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LotXlsxService {

  protected static final String CODE_LOT = "code_lot";
  protected static final String LIBELLE_LOT = "libelle_lot";
  protected static final String CODE_GT = "code_gt";
  protected static final String CODE_ASSUREUR = "code_assureur";

  private static final String[] LOT_COLUMNS = {CODE_LOT, LIBELLE_LOT, CODE_GT, CODE_ASSUREUR};

  public static final String LINE_NUMBER = "lineNumber";
  protected static final String SHEET_NAME = "export_lot";

  private final String xlsApiBaseURL;
  private final ObjectMapper objectMapper;
  private final LotService lotService;
  private final ContractElementService contractElementService;

  public LotXlsxService(
      BeyondPropertiesService beyondPropertiesService,
      ObjectMapper objectMapper,
      LotService lotService,
      ContractElementService contractElementService) {
    xlsApiBaseURL =
        beyondPropertiesService.getProperty(EXCEL_API).orElse("http://next-common-excel-api:8080");
    this.objectMapper = objectMapper;
    this.lotService = lotService;
    this.contractElementService = contractElementService;
  }

  @ContinueSpan(log = "exportLots")
  public byte[] exportLots(final String filename, String authHeader) {

    final AtomicInteger counter = new AtomicInteger(1);
    final Document document =
        XlsxUtils.buildXlsxDocument(
            filename,
            SHEET_NAME,
            LOT_COLUMNS,
            buildLotListContent(SHEET_NAME, lotService.findAll(), counter));

    final ResponseEntity<byte[]> result =
        RestConnectorUtils.postForRawContent(
            xlsApiBaseURL + "/documents/excel", new HashMap<>(), document, authHeader);

    return result.getBody();
  }

  @ContinueSpan(log = "exportLot")
  public byte[] exportLot(String codeLot, final String filename, String authHeader) {

    final AtomicInteger counter = new AtomicInteger(1);
    final Document document =
        XlsxUtils.buildXlsxDocument(
            filename,
            SHEET_NAME,
            LOT_COLUMNS,
            buildLotContent(SHEET_NAME, lotService.getByCode(codeLot), counter));

    final ResponseEntity<byte[]> result =
        RestConnectorUtils.postForRawContent(
            xlsApiBaseURL + "/documents/excel", new HashMap<>(), document, authHeader);

    return result.getBody();
  }

  public Content buildLotListContent(
      String sheetName, Collection<Lot> lots, final AtomicInteger counter) {
    final Content lotContent = new Content();
    lotContent.setSheetName(sheetName);

    final List<Map<String, Object>> list = new ArrayList<>();
    lots.forEach(lot -> createLotLines(list, lot, counter));

    lotContent.setSheetContent(list);
    lotContent.setSheetLinesNumber(counter.getAndIncrement() - 1);

    return lotContent;
  }

  public Content buildLotContent(String sheetName, Lot lot, final AtomicInteger counter) {
    final Content lotContent = new Content();
    lotContent.setSheetName(sheetName);

    final List<Map<String, Object>> list = new ArrayList<>();
    createLotLines(list, lot, counter);

    lotContent.setSheetContent(list);
    lotContent.setSheetLinesNumber(counter.getAndIncrement() - 1);

    return lotContent;
  }

  public static void createLotLines(
      List<Map<String, Object>> list, Lot lot, final AtomicInteger counter) {
    List<GarantieTechnique> garantieTechniques = lot.getGarantieTechniques();
    if (CollectionUtils.isNotEmpty(garantieTechniques)) {
      for (GarantieTechnique garantieTechnique : garantieTechniques) {
        if (garantieTechnique.getDateSuppressionLogique() == null) {
          final AtomicInteger counterColumn = new AtomicInteger(0);
          final Map<String, Object> values = new HashMap<>();
          values.put(LOT_COLUMNS[counterColumn.getAndIncrement()], lot.getCode());
          values.put(LOT_COLUMNS[counterColumn.getAndIncrement()], lot.getLibelle());
          values.put(
              LOT_COLUMNS[counterColumn.getAndIncrement()], garantieTechnique.getCodeGarantie());
          values.put(
              LOT_COLUMNS[counterColumn.getAndIncrement()], garantieTechnique.getCodeAssureur());
          values.put(LINE_NUMBER, counter.getAndIncrement());
          list.add(values);
        }
      }
    }
  }

  @ContinueSpan(log = "import lots")
  public List<Lot> importLot(MultipartFile file, String authHeader) {
    Document document;
    List<Lot> importedList = new ArrayList<>();
    document = XlsxUtils.checkDocument(file, authHeader, LOT_COLUMNS, objectMapper, xlsApiBaseURL);

    final Map<String, Lot> lotCodeWithObject = new HashMap<>();

    if (!document.getContent().isEmpty()) {

      List<Map<String, Object>> sheetContent = document.getContent().getFirst().getSheetContent();
      sheetContent.forEach(
          stringObjectMap ->
              this.getOrCreateLotElement(
                  lotCodeWithObject, stringObjectMap, sheetContent.indexOf(stringObjectMap)));

      LocalDateTime currentDate = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
      String now = currentDate.format(formatter);

      for (Map.Entry<String, Lot> entry : lotCodeWithObject.entrySet()) {
        String key = entry.getKey();
        Lot lot = lotCodeWithObject.get(key);

        Lot savedLot = lotService.getByCode(lot.getCode());

        if (savedLot == null) {
          createNewLot(importedList, now, lot);
        } else {
          updateExistingLot(importedList, now, lot, savedLot);
        }
      }

      return importedList;
    } else {
      throw new InvalidParameterException("Error in mapping document");
    }
  }

  public void updateExistingLot(List<Lot> importedList, String now, Lot lot, Lot savedLot) {
    List<GarantieTechnique> warrantyListToSave = savedLot.getGarantieTechniques();
    List<GarantieTechnique> garantieTechniqueList = lot.getGarantieTechniques();

    for (GarantieTechnique garantieTechniqueFromFile : garantieTechniqueList) {
      List<GarantieTechnique> existingValidWarranties =
          warrantyListToSave.stream()
              .filter(
                  warranty ->
                      warranty.equals(garantieTechniqueFromFile)
                          && warranty.getDateSuppressionLogique() == null)
              .collect(Collectors.toList());
      if (CollectionUtils.isEmpty(existingValidWarranties)) {
        warrantyListToSave.add(addWarranty(now, garantieTechniqueFromFile));
      }
    }

    List<GarantieTechnique> warrantiesToRemove =
        warrantyListToSave.stream()
            .filter(warrantyToSave -> !garantieTechniqueList.contains(warrantyToSave))
            .toList();

    for (GarantieTechnique warrantyToRemove : warrantiesToRemove) {
      warrantyToRemove.setDateSuppressionLogique(now);
    }

    List<GarantieTechnique> validWarrantiesForLot =
        warrantyListToSave.stream()
            .filter(warrantyToSave -> warrantyToSave.getDateSuppressionLogique() == null)
            .toList();
    if (validWarrantiesForLot.size() < 2) {
      throw new UnexpectedFileException("Il faut au moins 2 garanties techniques actives par lot");
    }
    savedLot.setGarantieTechniques(warrantyListToSave);
    savedLot.setLibelle(lot.getLibelle());
    lotService.update(savedLot);
    importedList.add(savedLot);
  }

  public void createNewLot(List<Lot> importedList, String now, Lot lot) {
    List<GarantieTechnique> garantieTechniqueList = lot.getGarantieTechniques();
    if (garantieTechniqueList.size() < 2) {
      throw new UnexpectedFileException("Il faut au moins 2 garanties techniques par lot");
    }
    for (GarantieTechnique garantieTechnique : garantieTechniqueList) {
      garantieTechnique.setDateAjout(now);
    }

    lotService.createLot(lot);
    importedList.add(lot);
  }

  private static GarantieTechnique addWarranty(
      String dateAjout, GarantieTechnique alreadyInDBWarranty) {
    GarantieTechnique garantieTechnique = new GarantieTechnique();
    garantieTechnique.setCodeGarantie(alreadyInDBWarranty.getCodeGarantie());
    garantieTechnique.setCodeAssureur(alreadyInDBWarranty.getCodeAssureur());
    garantieTechnique.setDateAjout(dateAjout);
    garantieTechnique.setDateSuppressionLogique(null);
    return garantieTechnique;
  }

  public void getOrCreateLotElement(
      Map<String, Lot> lotCodeWithObject, Map<String, Object> stringObjectMap, int index) {
    // on saute la ligne d'entête du fichier excel qui n'est pas pris en compte dans
    // le contenu et on part de 1 au lieu de 0 pour indiquer la ligne correspondant
    // dans le fichier excel)
    int lineNumber = index + 2;
    String key = XlsxUtils.valueOfCustom(stringObjectMap.get(CODE_LOT));
    if (StringUtils.isBlank(key)) {
      throw new UnexpectedFileException(
          String.format("Ligne %d, le code lot est obligatoire", lineNumber));
    }
    String libelle = XlsxUtils.valueOfCustom(stringObjectMap.get(LIBELLE_LOT));
    if (StringUtils.isBlank(libelle)) {
      throw new UnexpectedFileException(
          String.format("Ligne %d, le libelle lot est obligatoire", lineNumber));
    }
    String codeGT = XlsxUtils.valueOfCustom(stringObjectMap.get(CODE_GT));
    if (StringUtils.isBlank(codeGT)) {
      throw new UnexpectedFileException(
          String.format("Ligne %d, le code GT est obligatoire", lineNumber));
    }
    String codeAssureur = XlsxUtils.valueOfCustom(stringObjectMap.get(CODE_ASSUREUR));
    if (StringUtils.isBlank(codeAssureur)) {
      throw new UnexpectedFileException(
          String.format("Ligne %d, le code assureur est obligatoire", lineNumber));
    }

    final String codeGTWithoutSpaces = codeGT.strip();
    final String codeAssureurWithoutSpaces = codeAssureur.strip();
    ContractElement contractElement =
        contractElementService.get(codeGTWithoutSpaces, codeAssureurWithoutSpaces, true);
    // check if technical warranty exists in db
    if (contractElement == null) {
      throw new UnexpectedFileException(
          String.format("Ligne %d, la garantie technique n'existe pas", lineNumber));
    }

    key = key.strip();
    libelle = libelle.strip();
    if (lotCodeWithObject.get(key) == null) {
      Lot lot = new Lot();
      lot.setCode(key);
      lot.setLibelle(libelle);
      lot.setId(UUID.randomUUID().toString());
      GarantieTechnique garantieTechnique = new GarantieTechnique();
      garantieTechnique.setCodeGarantie(codeGTWithoutSpaces);
      garantieTechnique.setCodeAssureur(codeAssureurWithoutSpaces);
      List<GarantieTechnique> garantieTechniqueList = new ArrayList<>();
      garantieTechniqueList.add(garantieTechnique);
      lot.setGarantieTechniques(garantieTechniqueList);
      lotCodeWithObject.put(key, lot);

    } else {
      Lot lot = lotCodeWithObject.get(key);
      lot.setLibelle(libelle);
      List<GarantieTechnique> alreadyAddedWarranties =
          lot.getGarantieTechniques().stream()
              .filter(
                  garantieTechnique ->
                      garantieTechnique.getCodeGarantie().equals(codeGTWithoutSpaces)
                          && garantieTechnique.getCodeAssureur().equals(codeAssureurWithoutSpaces))
              .toList();
      if (CollectionUtils.isEmpty(alreadyAddedWarranties)) {
        GarantieTechnique garantieTechnique = new GarantieTechnique();
        garantieTechnique.setCodeGarantie(codeGTWithoutSpaces);
        garantieTechnique.setCodeAssureur(codeAssureurWithoutSpaces);
        List<GarantieTechnique> garantieTechniqueList = lot.getGarantieTechniques();
        garantieTechniqueList.add(garantieTechnique);
      }
    }
  }
}
