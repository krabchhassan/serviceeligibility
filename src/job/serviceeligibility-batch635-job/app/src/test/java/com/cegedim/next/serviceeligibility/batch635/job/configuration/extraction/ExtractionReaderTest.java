package com.cegedim.next.serviceeligibility.batch635.job.configuration.extraction;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFilesFromDirectory;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getStepExecution;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class ExtractionReaderTest {

  private ExtractionReader extractionReader;
  private StepExecution stepExecution = getStepExecution(EXTRACTION_STEP_NAME);
  private Path tmpFilePath =
      Paths.get(
          OUTPUT_TMP_FOLDER,
          ELIGIBLE_LINES_TMP_FILE_NAME_PREFIX + "TMP_FILE" + DOT + CSV_EXTENSION);
  private String amc = "0000401471";
  private String referenceDate = "20210121";

  @BeforeEach
  public void beforeEach() throws IOException {
    stepExecution
        .getJobExecution()
        .getExecutionContext()
        .put(ELIGIBLE_LINES_TMP_FILE_KEY, tmpFilePath.toString());
    extractionReader = new ExtractionReader();
  }

  private void setupTmpFile(boolean emptyFile) throws FileNotFoundException {
    File file = new File(tmpFilePath.toString());

    PrintWriter printWriter = new PrintWriter(file);

    if (!emptyFile) {
      printWriter.write(amc);
      printWriter.write(";");
      printWriter.write(referenceDate);
    }

    printWriter.flush();
    printWriter.close();
  }

  @AfterEach
  public void afterEach() throws IOException {
    extractionReader.afterStep(stepExecution);
    deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
  }

  // TODO @Test
  void shouldReadEligibleLinesFromLine_whenCallToReaderIsOk() throws Exception {
    setupTmpFile(false);
    extractionReader.beforeStep(stepExecution);
    AmcReferenceDateLineEligible amcReferenceDateLineEligible = extractionReader.read();
    Assertions.assertEquals(amc, amcReferenceDateLineEligible.getAmc());
    Assertions.assertEquals(referenceDate, amcReferenceDateLineEligible.getReferenceDate());
  }

  // TODO @Test
  void shouldReturnNull_whenCallToReaderWithEmptyFile() throws Exception {
    setupTmpFile(true);
    extractionReader.beforeStep(stepExecution);
    Assertions.assertNull(extractionReader.read());
  }
}
