package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.directoryExists;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.fileExists;

import com.cegedim.next.serviceeligibility.batch635.job.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

  private final Logger logger = LoggerFactory.getLogger(FileService.class);

  @Value("${SERVICE_ELIGIBILITY_PEFB_INPUT}")
  private String inputPefbFolder;

  @Value("${CONTROL_META}")
  private Boolean controlMeta;

  @ContinueSpan(log = "getPefbFolderFileNames")
  public Set<String> getPefbFolderFileNames() {
    throwIfPefbInputFolderDoesntExist();
    Set<String> result;

    try (Stream<Path> stream = Files.walk(Paths.get(inputPefbFolder), 1)) {
      result =
          stream
              .filter(file -> !Files.isDirectory(file))
              .map(Path::getFileName)
              .map(Path::toString)
              .filter(f -> f.startsWith(Constants.EXTRACTION_START_FILE_INPUT))
              .filter(f -> f.endsWith(Constants.DOT + Constants.CSV_EXTENSION))
              .filter(metadataControlPredicate())
              .collect(Collectors.toSet());

    } catch (IOException e) {
      logger.error("Could not list files in folder {} error : {}", inputPefbFolder, e.getMessage());
      throw new IllegalStateException(
          "Error while listing files from folder => " + inputPefbFolder);
    }
    if (result.isEmpty()) {
      logger.info("No eligible files to treat");
    }

    return result;
  }

  private void throwIfPefbInputFolderDoesntExist() {
    if (!directoryExists(this.inputPefbFolder)) {
      logger.error("PEFB Input folder doesn't exist => '{}'", this.inputPefbFolder);
      throw new IllegalStateException("PEFB Folder is mandatory");
    }
  }

  private Predicate<String> metadataControlPredicate() {
    return fileName ->
        controlMeta == null || !controlMeta || fileExists(inputPefbFolder, fileName + ".meta");
  }
}
