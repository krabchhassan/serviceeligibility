package com.cegedim.next.serviceeligibility.batch635.job.utils;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;

public class JobUtils {
  public static JobExecution getJobExecution() {
    JobInstance jobInstance = new JobInstance(Long.valueOf(1), JOB_NAME);
    return new JobExecution(jobInstance, makeJobParameters("Extraction_droit_635.csv"));
  }

  public static StepExecution getStepExecution(String stepName) {
    JobExecution jobExecution = getJobExecution();
    StepExecution stepExecution = new StepExecution(stepName, jobExecution);
    stepExecution.setExitStatus(ExitStatus.COMPLETED);
    return stepExecution;
  }

  public static ChunkContext getChunkContext(String stepName, JobExecution jobExecution) {
    StepExecution stepExecution = new StepExecution(stepName, jobExecution);
    StepContext stepContext = new StepContext(stepExecution);
    return new ChunkContext(stepContext);
  }

  public static JobParameters makeJobParameters(String fileName) {
    return new JobParametersBuilder()
        .addLong(TIME_KEY, System.currentTimeMillis())
        .addString(FILE_NAME_KEY, fileName)
        .toJobParameters();
  }

  public static int getFolderFilesNumber(String folder) {
    Set<Path> result = null;
    try (Stream<Path> stream = Files.walk(Paths.get(folder), 1)) {
      result =
          stream
              .filter(file -> !Files.isDirectory(file))
              .map(Path::getFileName)
              .collect(Collectors.toSet());
    } catch (IOException e) {
      e.printStackTrace();
      return 0;
    }
    return result.size();
  }
}
