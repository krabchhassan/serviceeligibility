package com.cegedim.next.serviceeligibility.purgehistoconsos.services;

import com.cegedim.next.serviceeligibility.core.elast.contract.ContratElastic;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduPurgeHistoConsos;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.purgehistoconsos.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Processor {

  private static final String EXPORT_FILE_NAME = "Export_Histo_Contrat_";
  private static final String EXPORT_FILE_EXTENTION = ".json";

  private final S3Service s3Service;

  private final ElasticHistorisationContractService elasticHistorisationContractService;

  @NewSpan
  public int calcul(CompteRenduPurgeHistoConsos compteRenduPurgeHistoConsos, int days) {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;
    LocalDateTime minusOneYear = LocalDateTime.now().minusDays(days);
    String requestDay =
        minusOneYear.format(DateTimeFormatter.ofPattern(DateFormat.date.getPattern()));
    String timestamp =
        minusOneYear.format(DateTimeFormatter.ofPattern(DateUtils.IDENTIFIANT_BATCH_FORMAT_DATE));

    log.info("Purging from {}", requestDay);

    try {
      List<ContratElastic> toPurge = elasticHistorisationContractService.findToPurge(requestDay);
      if (!toPurge.isEmpty()) {
        String exportFileName = EXPORT_FILE_NAME + timestamp;
        File file = createFileJson(toPurge, exportFileName);
        String pathToSavedFile =
            s3Service.writeToS3bucketHistoConsos(file, exportFileName + EXPORT_FILE_EXTENTION);
        elasticHistorisationContractService.deleteHistoContrats(toPurge);
        compteRenduPurgeHistoConsos.setPathToS3(pathToSavedFile);
        compteRenduPurgeHistoConsos.setContratPurge(toPurge.size());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      processReturnCode = Constants.PROCESSED_WITH_ERRORS;
    }

    return processReturnCode;
  }

  private File createFileJson(List<ContratElastic> extractions, String exportFileName)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String json = objectMapper.writeValueAsString(extractions);

    File tmpFile = File.createTempFile(exportFileName, EXPORT_FILE_EXTENTION);
    try (FileWriter writer = new FileWriter(tmpFile)) {
      writer.write(json);
    }

    return tmpFile;
  }
}
