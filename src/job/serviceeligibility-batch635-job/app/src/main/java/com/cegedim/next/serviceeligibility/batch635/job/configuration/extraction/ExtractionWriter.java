package com.cegedim.next.serviceeligibility.batch635.job.configuration.extraction;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.*;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.ESCAPED_DOT;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.HIDDEN_ARL_FILE_NAME;

import com.cegedim.next.serviceeligibility.batch635.job.domain.service.ExtractionService;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Qualifier("extractionWriter")
@RequiredArgsConstructor
public class ExtractionWriter implements ItemWriter<AmcReferenceDateLineEligible> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExtractionWriter.class);

  private final ExtractionService extractionService;

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Value("${PARALLEL_AMC_SIZE}")
  Integer parallelAmcSize;

  private ExecutorService executorService;
  private String fileName;
  private String arlFileName;
  private Path arlFilePath;
  private PrintWriter printWriter = null;
  private List<String> extractionFileNames = Collections.synchronizedList(new ArrayList<>());

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    fileName = extractFileName(stepExecution);
    arlFileName = extractArlFileName(stepExecution);
    configureArlFileWriter();
  }

  @Override
  public void write(Chunk<? extends AmcReferenceDateLineEligible> chunk) throws Exception {
    try {
      setExecutorService(chunk.size());

      for (AmcReferenceDateLineEligible item : chunk) {
        executorService.execute(
            extractionService.extract(item, fileName, printWriter, extractionFileNames));
      }
    } catch (Exception ex) {
      LOGGER.error("An error occurred while extracting eligible lines for file => " + fileName, ex);
      throw ex;
    }
  }

  private void setExecutorService(int size) {
    if (executorService == null) {
      int numberOfThreads = computeNumberOfThreads(size, parallelAmcSize);
      executorService = Executors.newFixedThreadPool(numberOfThreads);
    }
  }

  @AfterStep
  public ExitStatus afterStep(StepExecution stepExecution) {
    ExitStatus exitStatus = stepExecution.getExitStatus();

    if (exitStatus.equals(ExitStatus.COMPLETED)) {
      try {
        if (executorService != null) {
          executorService.shutdown();
          executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        if (printWriter != null) {
          printWriter.println(LocalDate.now(ZoneOffset.UTC));
          printWriter.println(LocalDateTime.now(ZoneOffset.UTC).toLocalTime().toString());
          printWriter.close();
          createMetaAndRenameFiles(arlFileName, outputPefbFolder);
        }
      } catch (InterruptedException e) {
        LOGGER.warn("Interrupted!", e);
        // Restore interrupted state...
        Thread.currentThread().interrupt();
      } catch (Exception ex) {
        LOGGER.error("Error occurred during extraction step", ex);
        cleanWhenFailure(stepExecution);
        throw new IllegalStateException("Error occurred during extraction step", ex);
      }
    } else {
      cleanWhenFailure(stepExecution);
    }
    return exitStatus;
  }

  private void cleanWhenFailure(StepExecution stepExecution) {
    if (printWriter != null) {
      printWriter.close();
    }
    if (arlFilePath == null) {
      Path filePath = getFilePath(extractArlFileName(stepExecution));
      deleteFileIfExists(filePath);
    } else {
      deleteFileIfExists(arlFilePath);
    }
    // If we cannot complete the arl file we should clean all extraction files.
    cleanExtractionFiles();
  }

  private void cleanExtractionFiles() {
    for (String hiddenFileName : extractionFileNames) {
      String extractionFileName = hiddenFileName.replaceFirst(ESCAPED_DOT, "");
      Path filePath = Paths.get(outputPefbFolder, extractionFileName);
      String metaFileName = extractionFileName + ".meta";
      Path metaFilePath = Paths.get(outputPefbFolder, metaFileName);
      deleteFilesIfExists(filePath, metaFilePath);
    }
  }

  private String extractArlFileName(StepExecution stepExecution) {
    return (String) stepExecution.getJobExecution().getExecutionContext().get(HIDDEN_ARL_FILE_NAME);
  }

  private void configureArlFileWriter() {
    try {
      arlFilePath = getFilePath(arlFileName);
      printWriter = new PrintWriter(new FileWriter(arlFilePath.toString(), true), true);
    } catch (IOException ex) {
      LOGGER.error("Error occurred when opening file : {}", arlFileName);
      throw new IllegalStateException("Error occurred when opening file : " + arlFileName);
    }
  }

  private Path getFilePath(String fileName) {
    return Paths.get(outputPefbFolder, fileName);
  }
}
