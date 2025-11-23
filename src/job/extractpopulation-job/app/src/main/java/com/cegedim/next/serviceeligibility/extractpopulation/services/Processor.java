package com.cegedim.next.serviceeligibility.extractpopulation.services;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExtractionPopulation;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.extractpopulation.constants.ConstantsExtractPop;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class Processor {

  private final ServicePrestationService servicePrestationService;
  private final JsonFileService jsonFileService;
  private final CsvFileService csvFileService;

  @NewSpan
  public int extract(
      String format,
      String csvDelemiter,
      String outputDirectory,
      CompteRenduExtractionPopulation compteRenduExtractionPopulation,
      int maxContractsPerFile) {
    int processReturnCode;
    String stringDateExec =
        DateUtils.formatDate(new Date(), DateUtils.IDENTIFIANT_BATCH_FORMAT_DATE);

    if (ConstantsExtractPop.JSON.equalsIgnoreCase(format)) {
      processReturnCode =
          extractJsonFiles(
              outputDirectory,
              compteRenduExtractionPopulation,
              stringDateExec,
              maxContractsPerFile);
    } else {
      processReturnCode =
          extractCsvFiles(
              csvDelemiter, outputDirectory, compteRenduExtractionPopulation, stringDateExec);
    }

    return processReturnCode;
  }

  private int extractJsonFiles(
      String outputDirectory,
      CompteRenduExtractionPopulation compteRenduExtractionPopulation,
      String stringDateExec,
      int maxContractsPerFile) {
    return jsonFileService.fillJsonFiles(
        maxContractsPerFile, stringDateExec, outputDirectory, compteRenduExtractionPopulation);
  }

  private int extractCsvFiles(
      String csvDelemiter,
      String outputDirectory,
      CompteRenduExtractionPopulation compteRenduExtractionPopulation,
      String stringDateExec) {
    String fileContratName =
        ConstantsExtractPop.EXTRACTION_CONTRATS
            + stringDateExec
            + ConstantsExtractPop.CSV_EXTENSION;
    String fileAssureName =
        ConstantsExtractPop.EXTRACTION_CONTRATS_ASSURES
            + stringDateExec
            + ConstantsExtractPop.CSV_EXTENSION;
    String fileGarantieName =
        ConstantsExtractPop.EXTRACTION_CONTRATS_ASSURES_GARANTIES
            + stringDateExec
            + ConstantsExtractPop.CSV_EXTENSION;

    try (FileWriter contratsWriter =
            new FileWriter(outputDirectory + fileContratName, StandardCharsets.UTF_8, true);
        FileWriter assuresWriter =
            new FileWriter(outputDirectory + fileAssureName, StandardCharsets.UTF_8, true);
        FileWriter garantiesWriter =
            new FileWriter(outputDirectory + fileGarantieName, StandardCharsets.UTF_8, true)) {

      Stream<ContratAIV6> stream = servicePrestationService.getAllContrats();
      csvFileService.fillCsvFiles(
          csvDelemiter, stream, contratsWriter, assuresWriter, garantiesWriter);

      MetaService.generateMetaData(fileContratName, outputDirectory, ConstantsExtractPop.ISSUER);
      MetaService.generateMetaData(fileAssureName, outputDirectory, ConstantsExtractPop.ISSUER);
      MetaService.generateMetaData(fileGarantieName, outputDirectory, ConstantsExtractPop.ISSUER);

      compteRenduExtractionPopulation.addNomFichiers(fileContratName);
      compteRenduExtractionPopulation.addNomFichiers(fileAssureName);
      compteRenduExtractionPopulation.addNomFichiers(fileGarantieName);

    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return -1;
    }
    return 0;
  }
}
