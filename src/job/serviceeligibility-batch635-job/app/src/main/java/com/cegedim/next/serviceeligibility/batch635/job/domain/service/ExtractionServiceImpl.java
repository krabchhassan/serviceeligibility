package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.ConcurrentLists.alreadyUsedExtractionFilesContains;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.ConcurrentLists.alreadyUsedTmpExtractionFilesContains;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.*;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.DELIMITER;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.EXTRACTION_INDICATOR_R;

import com.cegedim.next.serviceeligibility.batch635.job.domain.model.Declarants;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.CustomContratsRepository;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection.PeriodeDroitProjection;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.ExtractionItem;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.ExtractionStatusValueHolder;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.FileAndMeta;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExtractionServiceImpl implements ExtractionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExtractionServiceImpl.class);

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Value("${DATABASE_PAGINATION_LIMIT:20000}")
  private Integer databasePaginationLimit;

  private final CustomContratsRepository customContratsRepository;
  private final DeclarantsService declarantsService;

  private DelimitedLineAggregator<ExtractionItem> lineAggregator;

  public ExtractionServiceImpl(
      CustomContratsRepository customContratsRepository, DeclarantsService declarantsService) {
    lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(DELIMITER);
    this.customContratsRepository = customContratsRepository;
    this.declarantsService = declarantsService;
    BeanWrapperFieldExtractor<ExtractionItem> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(
        new String[] {
          "extractionTime",
          "referenceDate",
          "databaseType",
          "databaseId",
          "amc",
          "beneficiaryNir",
          "beneficiaryNirKey",
          "beneficiaryBirthDate",
          "birthDateRank",
          "periodeDroitStartDate",
          "periodeDroitEndDate",
          "adherentNumber",
          "contratNumber",
          "personneNumber",
          "beneficiaryLastName",
          "beneficiaryFirstName"
        });
    lineAggregator.setFieldExtractor(fieldExtractor);
  }

  @Override
  @ContinueSpan(log = "extract")
  public Runnable extract(
      AmcReferenceDateLineEligible amcReferenceDateLineEligible,
      String fileName,
      PrintWriter printWriter,
      List<String> extractionFileNames) {
    return () -> {
      String amc = amcReferenceDateLineEligible.getAmc();
      String referenceDate = amcReferenceDateLineEligible.getReferenceDate();
      Path hiddenExtractionFilePath = null;
      Path tmpExtractionFilePath = null;
      FileAndMeta fileAndMeta = new FileAndMeta();
      ExtractionStatusValueHolder extractionStatusValueHolder =
          new ExtractionStatusValueHolder(EXTRACTION_INDICATOR_R);
      try {
        LOGGER.info(
            "STARTED extraction for Amc => '{}' and Reference date => '{}' for input file => '{}' ASYNCHRONOUSLY!",
            amc,
            referenceDate,
            fileName);

        Declarants declarants = declarantsService.getDeclarantById(amc);

        String tmpExtractionFileName = handleTmpExtractionFileName(amc);
        tmpExtractionFilePath = Paths.get(outputPefbFolder, tmpExtractionFileName);

        extractAndWriteToTmpFile(
            tmpExtractionFilePath, amc, referenceDate, declarants, extractionStatusValueHolder);

        String hiddenExtractionFileName =
            handleExtractionFileName(amcReferenceDateLineEligible, amc);
        hiddenExtractionFilePath = Paths.get(outputPefbFolder, hiddenExtractionFileName);

        storeFileNamesForCleanUp(extractionFileNames, hiddenExtractionFileName);

        writeTmpFileAndExtractionStatusToPefbFile(
            tmpExtractionFilePath,
            extractionStatusValueHolder.getExtractionStatus(),
            hiddenExtractionFilePath);

        fileAndMeta = createMetaAndRenameFiles(hiddenExtractionFileName, outputPefbFolder);

        printWriter.println(
            "Traitement de l'AMC " + amc + " pour la Date de référence " + referenceDate + " : OK");

        LOGGER.info(
            "FINISHED extraction for Amc => '{}' and Reference date => '{}' for input file => '{}' ASYNCHRONOUSLY!",
            amc,
            referenceDate,
            fileName);
      } catch (Exception ex) {
        LOGGER.error(
            "Error occcurred while handling extraction for Amc => '{}' and Reference date => '{}' "
                + "for file => '{}'",
            amc,
            referenceDate,
            fileName,
            ex);
        printWriter.println(
            "Traitement de l'AMC " + amc + " pour la Date de référence " + referenceDate + " : KO");

        deleteFilesIfExists(
            hiddenExtractionFilePath, fileAndMeta.getFilePath(), fileAndMeta.getMetaFilePath());

        throw new IllegalStateException("Error during extraction", ex);
      } finally {
        deleteFileIfExists(tmpExtractionFilePath);
      }
    };
  }

  private void writeTmpFileAndExtractionStatusToPefbFile(
      Path tmpExtractionFilePath, String extractionStatus, Path hiddenExtractionFilePath)
      throws IOException {
    LOGGER.info(
        "Writing lines from the tmp to the extraction PEFB file => '{}'!",
        hiddenExtractionFilePath);
    copyFileContentContentAndAppendToLines(
        tmpExtractionFilePath, extractionStatus, hiddenExtractionFilePath);
  }

  private void storeFileNamesForCleanUp(
      List<String> extractionFileNames, String extractionFileName) {
    extractionFileNames.add(extractionFileName);
  }

  // When launching multiple jobs asynchronously, two threads may arrive at this
  // step at the same time (Exact millisecond!) and therefore creating the same
  // file.
  // So we should loop until we get a new file name that is not already used.
  @SneakyThrows
  private String handleExtractionFileName(
      AmcReferenceDateLineEligible amcReferenceDateLineEligible, String declarantName) {
    String extractionFileName = generateExtractionFileName(declarantName);

    while (alreadyUsedExtractionFilesContains(extractionFileName)) {
      extractionFileName = generateExtractionFileName(declarantName);
    }
    return extractionFileName;
  }

  private String handleTmpExtractionFileName(String declarantNo) {
    String tmpExtractionFileName = generateTmpExtractionFileName(declarantNo);

    while (alreadyUsedTmpExtractionFilesContains(tmpExtractionFileName)) {
      tmpExtractionFileName = generateTmpExtractionFileName(declarantNo);
    }
    return tmpExtractionFileName;
  }

  private void extractAndWriteToTmpFile(
      Path tmpExtractionFilePath,
      String amc,
      String referenceDate,
      Declarants declarants,
      ExtractionStatusValueHolder extractionStatusValueHolder)
      throws FileNotFoundException {
    PrintWriter extractionWriter = null;
    List<ExtractionItem> items = new ArrayList<>();

    try {
      String identity = declarantsService.getIdentityByDeclarants(declarants);

      extractionWriter = getCustomPrintWriter(tmpExtractionFilePath);

      int pageIndex = 0;
      List<PeriodeDroitProjection> periodeDroits =
          customContratsRepository.extractPeriodesDroit(amc, referenceDate, pageIndex);

      extractionWriter.println(
          "DtHHmmss-extract;Dtref;Type_base;Idbase;NumAMC;NIRBenef;CléNIRBenef;Dtnaiss;Rgnaiss;DtDebDroits;DtFinDroits;NumAdh;NumContrat;IdPersonne;NomBenef;PrenomBenef;EtatDroitsDtref");
      while (!periodeDroits.isEmpty()) {
        int periodeDroitsSize = periodeDroits.size();

        items =
            ExtractionItem.from(
                periodeDroits, amc, referenceDate, identity, extractionStatusValueHolder);

        LOGGER.info(
            "Writing '{}' lines to the extraction TMP file => '{}'!",
            periodeDroitsSize,
            tmpExtractionFilePath);

        writeItemsToTmpExtractionFile(items, extractionWriter);

        if (periodeDroitsSize < databasePaginationLimit) {
          break;
        }
        pageIndex++;
        periodeDroits =
            customContratsRepository.extractPeriodesDroit(amc, referenceDate, pageIndex);
      }
    } finally {
      if (extractionWriter != null) {
        extractionWriter.flush();
        extractionWriter.close();
      }
    }
  }

  private void writeItemsToTmpExtractionFile(
      List<ExtractionItem> items, PrintWriter extractionWriter) {
    for (ExtractionItem item : items) {
      extractionWriter.println(lineAggregator.aggregate(item));
    }
    extractionWriter.flush();
  }
}
