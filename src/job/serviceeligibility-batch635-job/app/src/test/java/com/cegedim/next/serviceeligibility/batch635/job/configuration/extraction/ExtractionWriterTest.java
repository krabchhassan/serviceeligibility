package com.cegedim.next.serviceeligibility.batch635.job.configuration.extraction;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFilesFromDirectory;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.EXTRACTION_STEP_NAME;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.HIDDEN_ARL_FILE_NAME;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getStepExecution;
import static org.mockito.Mockito.*;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.ExtractionService;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class ExtractionWriterTest {

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Value("${PARALLEL_AMC_SIZE}")
  Integer parallelAmcSize;

  @MockBean ExtractionService extractionService;

  ExtractionWriter extractionWriter;
  StepExecution stepExecution;

  @BeforeEach
  public void beforeEach() throws IOException {
    stepExecution = getStepExecution(EXTRACTION_STEP_NAME);
    Path fileNamePath = Paths.get(outputPefbFolder, "fileTest");
    new File(fileNamePath.toString()).createNewFile();
    stepExecution.getJobExecution().getExecutionContext().put(HIDDEN_ARL_FILE_NAME, "fileTest");
    extractionWriter = new ExtractionWriter(extractionService);
    ReflectionTestUtils.setField(extractionWriter, "extractionService", extractionService);
    ReflectionTestUtils.setField(extractionWriter, "outputPefbFolder", outputPefbFolder);
    ReflectionTestUtils.setField(extractionWriter, "parallelAmcSize", parallelAmcSize);
    extractionWriter.beforeStep(stepExecution);
  }

  @AfterEach
  public void afterEach() throws IOException, InterruptedException {
    StepExecution stepExecution = getStepExecution(EXTRACTION_STEP_NAME);
    extractionWriter.afterStep(stepExecution);
    deleteFilesFromDirectory(outputPefbFolder);
  }

  // TODO @Test
  void shouldExtractAmc_whenCallToWriterIsOk() throws Exception {
    AmcReferenceDateLineEligible amcReferenceDateLineEligible =
        AmcReferenceDateLineEligible.builder().amc("AMC").referenceDate("REFERENCE_DATE").build();

    when(extractionService.extract(any(), any(), any(), anyList())).thenReturn(() -> {});

    extractionWriter.write(Chunk.of(amcReferenceDateLineEligible));

    verify(extractionService, times(1))
        .extract(eq(amcReferenceDateLineEligible), any(), any(), anyList());
  }
}
