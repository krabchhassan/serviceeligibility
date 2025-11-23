package com.cegedim.next.serviceeligibility.batch635.job.configuration.listener;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.createDirectoryIfNotExists;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFilesFromDirectory;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.OUTPUT_TMP_FOLDER;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getFolderFilesNumber;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getJobExecution;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class ServiceEligibilityListenerTest {

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Autowired ServiceEligibilityListener serviceEligibilityListener;

  @BeforeEach
  public void beforeEach() throws IOException {
    createDirectoryIfNotExists(OUTPUT_TMP_FOLDER);
    deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
  }

  @AfterEach
  public void afterEach() throws IOException {
    deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
  }

  @Test
  void shouldInitOutputFolders_whenBeforeIsExecuted() {
    JobExecution jobExecution = getJobExecution();
    serviceEligibilityListener.beforeJob(jobExecution);

    File output = new File(outputPefbFolder);
    File outputTmp = new File(OUTPUT_TMP_FOLDER);

    Assertions.assertTrue(output.exists());
    Assertions.assertTrue(outputTmp.exists());
  }

  @Test
  void shouldClearOutputTmpFolder_whenAfterIsExecuted() throws IOException {
    JobExecution jobExecution = getJobExecution();
    int before = getFolderFilesNumber(OUTPUT_TMP_FOLDER);

    Assertions.assertEquals(0, before);

    File f = new File(Paths.get(OUTPUT_TMP_FOLDER, "newFile.csv").toString());
    f.createNewFile();

    int after = getFolderFilesNumber(OUTPUT_TMP_FOLDER);
    serviceEligibilityListener.afterJob(jobExecution);
    Assertions.assertEquals(1, after);
  }
}
