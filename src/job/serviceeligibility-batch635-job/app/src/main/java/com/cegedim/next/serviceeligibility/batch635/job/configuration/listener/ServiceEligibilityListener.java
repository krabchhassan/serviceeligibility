package com.cegedim.next.serviceeligibility.batch635.job.configuration.listener;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.*;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.OUTPUT_TMP_FOLDER;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceEligibilityListener implements JobExecutionListener {

  private final Logger logger = LoggerFactory.getLogger(ServiceEligibilityListener.class);

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Value("${SERVICE_ELIGIBILITY_PEFB_INPUT}")
  private String inputPefbFolder;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    initOutputFolders();
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    BatchStatus batchStatus = jobExecution.getStatus();

    String fileName = extractFileName(jobExecution);

    if (batchStatus.equals(BatchStatus.COMPLETED)) {
      Path inputFilePath = Path.of(inputPefbFolder, fileName);
      Path inputMetaFilePath = Path.of(inputPefbFolder, fileName + ".meta");
      deleteFileIfExists(inputFilePath);
      deleteFileIfExists(inputMetaFilePath);
      logger.info("Job was SUCCESSFUL while treating File => '{}'!", fileName);
    } else {
      logger.error("Job FAILED while treating File => '{}'!", fileName);
    }
  }

  private void initOutputFolders() {
    createDirectoryIfNotExists(outputPefbFolder);
    createDirectoryIfNotExists(OUTPUT_TMP_FOLDER);
  }
}
