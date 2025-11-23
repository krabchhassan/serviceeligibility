package com.cegedim.next.serviceeligibility.batch635.job.configuration.parameters_control;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getStepExecution;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineErrors;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource(properties = {"TARGET_ENV = test", "classpath:application-test.properties"})
class ParametersControlWriterTest {

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Value("${spring.data.mongodb.uri}")
  private String databaseUrl;

  ParametersControlWriter parametersControlWriter;

  @Value("${TARGET_ENV}")
  private String targetEnv;

  @BeforeEach
  public void beforeEach() throws Exception {
    Helper.deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
    Helper.deleteFilesFromDirectory(outputPefbFolder);
    parametersControlWriter = new ParametersControlWriter();
    ReflectionTestUtils.setField(parametersControlWriter, "outputPefbFolder", outputPefbFolder);
    ReflectionTestUtils.setField(parametersControlWriter, "targetEnv", targetEnv);
    ReflectionTestUtils.setField(parametersControlWriter, "databaseUrl", databaseUrl);

    runBeforeStep();
  }

  @AfterEach
  public void afterEach() throws Exception {
    Helper.deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
    Helper.deleteFilesFromDirectory(outputPefbFolder);
  }

  private void runBeforeStep() throws Exception {
    StepExecution stepExecution = getStepExecution(PARAMETERS_CONTROL_STEP_NAME);
    parametersControlWriter.beforeStep(stepExecution);
  }

  private void runAfterStep() throws Exception {
    StepExecution stepExecution = getStepExecution(PARAMETERS_CONTROL_STEP_NAME);
    parametersControlWriter.afterStep(stepExecution);
  }

  @Test
  void shouldWriteToArlFile_whenWriterReceivesErrorItems() throws Exception {
    List<String> errors =
        List.of(
            "La date de référence est erronée sur la ligne '1'",
            "Le numéro de l’AMC n’est pas renseigné sur la ligne '2'");

    Chunk<AmcReferenceDateLineErrors> items = getErrorItems(errors);

    parametersControlWriter.write(items);

    runAfterStep();

    List<String> arlContent = getFileContent(outputPefbFolder);

    List<String> eligibleLinesTmpContent = getFileContent(OUTPUT_TMP_FOLDER);
    Assertions.assertTrue(arlContent.size() > 0);
    Assertions.assertTrue(arlContent.contains(targetEnv));
    Assertions.assertTrue(arlContent.contains(LocalDate.now(ZoneOffset.UTC).toString()));
    Assertions.assertEquals(0, eligibleLinesTmpContent.size());
  }

  @Test
  void shouldWriteToEligibleLineTmpFile_whenWriterReceivesEligibleItems() throws Exception {
    Chunk<AmcReferenceDateLineEligible> items = getEligibleItems();

    parametersControlWriter.write(items);

    runAfterStep();

    List<String> arlContent = getFileContent(outputPefbFolder);

    List<String> eligibleLinesTmpContent = getFileContent(OUTPUT_TMP_FOLDER);

    Assertions.assertTrue(arlContent.contains(LocalDate.now(ZoneOffset.UTC).toString()));
    Assertions.assertEquals(2, eligibleLinesTmpContent.size());
  }

  private Chunk<AmcReferenceDateLineErrors> getErrorItems(List<String> errors) {
    return Chunk.of(AmcReferenceDateLineErrors.builder().errors(errors).build());
  }

  private Chunk<AmcReferenceDateLineEligible> getEligibleItems() {
    return Chunk.of(
        AmcReferenceDateLineEligible.builder().amc("0000401471").referenceDate("20210909").build(),
        AmcReferenceDateLineEligible.builder().amc("0000403453").referenceDate("20110910").build());
  }

  private List<String> getFileContent(String dir) throws IOException {
    List<String> content = new ArrayList<>();
    Optional<Path> filePath =
        Files.walk(Path.of(dir), 1)
            .filter(path -> path.toString().endsWith(DOT + CSV_EXTENSION))
            .findFirst();
    if (filePath.isEmpty()) {
      throw new IllegalStateException("Arl Step didn't generate required file.");
    }
    String arlFile = filePath.get().toString();
    try (BufferedReader csvReader = new BufferedReader(new FileReader(arlFile))) {
      String row;
      while ((row = csvReader.readLine()) != null) {
        content.add(row);
      }
    }
    return content;
  }
}
