package com.cegedim.next.serviceeligibility.core.features.volumetry.process;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.EXCEL_API;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.DeclarantLightDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.volumetrie.VolumetrieDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.VolumetrieService;
import com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.ColumnHeaderConstants;
import com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.enums.ColumnFormat;
import com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.model.*;
import com.cegedim.next.serviceeligibility.core.utility.rest.RestConnectorUtils;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/** Processus de dialogue avec la base de droits pour la gestion de la volumétrie */
@Component
public class VolumetrieProcessImpl implements VolumetrieProcess {

  private final String xlsApiBaseURL;
  private final DeclarantBackendService declarantService;
  private final VolumetrieService volumetrieService;

  public VolumetrieProcessImpl(
      BeyondPropertiesService beyondPropertiesService,
      DeclarantBackendService declarantService,
      VolumetrieService volumetrieService) {
    xlsApiBaseURL =
        beyondPropertiesService.getProperty(EXCEL_API).orElse("http://next-common-excel-api:8080");
    this.declarantService = declarantService;
    this.volumetrieService = volumetrieService;
  }

  private static final List<String> columnsLabel =
      List.of(
          ColumnHeaderConstants.PARTENAIRE,
          ColumnHeaderConstants.AMC,
          ColumnHeaderConstants.NB_DECLARATION,
          ColumnHeaderConstants.NB_PERSONS,
          ColumnHeaderConstants.NB_PERSONS_OPENED_RIGHTS,
          ColumnHeaderConstants.NB_PERSONS_CLOSED_RIGHTS);

  @Override
  public List<VolumetrieDto> getLastVolumetries() {
    List<VolumetrieDto> volumetriesDtos = volumetrieService.findLastVolumetries();

    if (!volumetriesDtos.isEmpty()) {
      // Enrichir les volumétrie avec les nom AMC et code partenaire AMC
      List<DeclarantLightDto> declarantsLight = declarantService.findAllLightDto();

      // Creation map temporaire
      Map<String, DeclarantLightDto> declarantsLightMap = new HashMap<>();
      for (DeclarantLightDto declarantLight : declarantsLight) {
        declarantsLightMap.put(declarantLight.getNumero(), declarantLight);
      }

      for (VolumetrieDto volumetrieDto : volumetriesDtos) {
        String numeroAmc = volumetrieDto.getAmc();

        // Recupere declarantLightDto par numero AMC
        DeclarantLightDto declarantLightDto = declarantsLightMap.get(numeroAmc);
        if (declarantLightDto != null) {
          String codePartenaire = declarantLightDto.getCodePartenaire();
          String nomAmc =
              declarantLightDto.getNom() == null ? "" : declarantLightDto.getNom().trim();

          // Libelle AMC = "numero AMC - nom AMC"
          String libelleAmc = numeroAmc + " - " + nomAmc;

          volumetrieDto.setCodePartenaire(codePartenaire);
          volumetrieDto.setAmc(libelleAmc);
        }
      }
      // Trier par Partenaire, AMC
      Collections.sort(volumetriesDtos);
    }

    return volumetriesDtos;
  }

  /**
   * calculate the volumetry data matching the criteria parameters given, and build the appropriate
   * XLS document
   *
   * @param amc criteria parameter AMC
   * @param codePartenaire criteria parameter codePartenaire
   * @return XLS document containing all volumetrie data matching the criteria
   */
  public byte[] getFilteredVolumetrieAsXLS(String amc, String codePartenaire, String authHeader) {
    List<VolumetrieDto> volumetries = getLastVolumetries();
    List<VolumetrieDto> filteredVolumetries =
        volumetries.stream()
            .filter(
                dataRow -> {
                  if (amc != null) {
                    return dataRow.getAmc().contains(amc);
                  }
                  if (codePartenaire != null) {
                    return dataRow.getCodePartenaire().equals(codePartenaire);
                  }
                  return true;
                })
            .toList();

    Document document = new Document();
    document.setContent(mapVolumetryDataToContent(filteredVolumetries));
    document.setMetadata(getMetaData(amc, codePartenaire));

    // XLS API call
    final ResponseEntity<byte[]> result =
        RestConnectorUtils.postForRawContent(
            xlsApiBaseURL + "/documents/excel", new HashMap<>(), document, authHeader);
    return result.getBody();
  }

  /**
   * Build Meta data (headers) of the document using to create the XLS file
   *
   * @param amc optional criteria used
   * @param codePartenaire optional criteria used
   * @return Meta Data object used to build an XLS file (with common-excel-api)
   */
  private Metadata getMetaData(String amc, String codePartenaire) {
    ExcelSheet excelSheet = new ExcelSheet();
    Metadata metadata = new Metadata();
    AtomicInteger counter = new AtomicInteger(0);
    String criteriaToDisplay = getVolumetrieCriteriaTitle(amc, codePartenaire);

    metadata.setName("List_volumetry_" + criteriaToDisplay + "_" + new Date().getTime() + ".xlsx");
    excelSheet.setName("volumetry data");
    excelSheet.setNumber(0);
    List<Column> columns =
        columnsLabel.stream()
            .map(
                currentColumnLabel -> {
                  Column column = new Column();
                  column.setName(currentColumnLabel);
                  column.setColumnFormat(
                      currentColumnLabel.startsWith("Nombre")
                          ? ColumnFormat.NUMERIC
                          : ColumnFormat.STRING);
                  column.setPosition(counter.getAndIncrement());
                  return column;
                })
            .toList();
    excelSheet.setColumns(columns);
    metadata.addSheet(excelSheet);
    return metadata;
  }

  /**
   * Build the content part of the document used to build the XLS file
   *
   * @param volumetries data used to build content
   * @return rows for the XLS file
   */
  private List<Content> mapVolumetryDataToContent(List<VolumetrieDto> volumetries) {
    List<Content> result = new ArrayList<>();
    Content content = new Content();
    AtomicInteger counter = new AtomicInteger(1);
    List<Map<String, Object>> sheetContent =
        volumetries.stream()
            .map(
                volumetry -> {
                  Map<String, Object> sheetContentItem =
                      columnsLabel.stream()
                          .collect(
                              Collectors.toMap(
                                  columnLabel -> columnLabel,
                                  columnLabel -> getDataByColumnLabel(columnLabel, volumetry)));
                  sheetContentItem.put("lineNumber", counter.getAndIncrement());
                  return sheetContentItem;
                })
            .toList();

    content.setSheetContent(sheetContent);
    content.setSheetName("volumetry data");
    result.add(content);
    return result;
  }

  private Object getDataByColumnLabel(String columnLabel, VolumetrieDto volumetry) {
    return switch (columnLabel) {
      case ColumnHeaderConstants.PARTENAIRE -> safeStringGetter(volumetry.getCodePartenaire());
      case ColumnHeaderConstants.AMC -> safeStringGetter(volumetry.getAmc());
      case ColumnHeaderConstants.NB_DECLARATION -> safeLongGetter(volumetry.getDeclarations());
      case ColumnHeaderConstants.NB_PERSONS -> safeLongGetter(volumetry.getPersonnes());
      case ColumnHeaderConstants.NB_PERSONS_OPENED_RIGHTS ->
          safeLongGetter(volumetry.getPersonnesDroitsOuverts());
      case ColumnHeaderConstants.NB_PERSONS_CLOSED_RIGHTS ->
          safeLongGetter(volumetry.getPersonnesDroitsFermes());
      default -> "";
    };
  }

  private String safeStringGetter(String value) {
    return value == null ? "" : value;
  }

  private Long safeLongGetter(Long value) {
    return value == null ? 0 : value;
  }

  public String getVolumetrieCriteriaTitle(String amc, String codePartenaire) {
    StringJoiner joiner = new StringJoiner("_");

    if (amc != null) {
      joiner.add(amc);
    }

    if (codePartenaire != null) {
      joiner.add(codePartenaire);
    }

    return joiner.toString();
  }
}
