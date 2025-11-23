package com.cegedim.next.serviceeligibility.batch635.job;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFilesFromDirectory;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.batch635.job.domain.service.FileService;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JobExecutor {
  private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

  private final JobLauncher jobLauncher;
  private final FileService fileService;

  private final Job job;

  public JobExecutor(
      ApplicationContext applicationContext, JobLauncher jobLauncher, FileService fileService) {
    job = applicationContext.getBean(JOB_BEAN_NAME, Job.class);
    this.jobLauncher = jobLauncher;
    this.fileService = fileService;
  }

  @NewSpan
  public int execute() {
    int endCode = 0;
    try {
      Set<String> fileNames = fileService.getPefbFolderFileNames();
      fileNames.forEach(this::launchJob);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      endCode = -1;
    } finally {
      clearOutputTmpFiles();
    }
    return endCode;
  }

  private void launchJob(String fileName) {
    try {
      LOGGER.info("Job => '{}' IS treating file => '{}'!", JOB_NAME, fileName);
      jobLauncher.run(job, makeJobParameters(fileName));
      LOGGER.info("Job => '{}' FINISHED treating file => '{}'!", JOB_NAME, fileName);
    } catch (Exception ex) {
      LOGGER.error(
          "An error has occurred while running the job for file => '" + fileName + "'", ex);
      throw new IllegalStateException(ex);
    }
  }

  private JobParameters makeJobParameters(String fileName) {
    return new JobParametersBuilder()
        .addLong(TIME_KEY, System.currentTimeMillis())
        .addString(FILE_NAME_KEY, fileName)
        .toJobParameters();
  }

  private void clearOutputTmpFiles() {
    try {
      deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
    } catch (IOException e) {
      LOGGER.error("Error while trying to delete output tmp files!");
    }
  }
}
