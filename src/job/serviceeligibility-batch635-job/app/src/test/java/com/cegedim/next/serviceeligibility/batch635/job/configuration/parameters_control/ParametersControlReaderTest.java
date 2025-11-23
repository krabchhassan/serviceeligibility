package com.cegedim.next.serviceeligibility.batch635.job.configuration.parameters_control;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.PARAMETERS_CONTROL_STEP_NAME;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getStepExecution;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.FileService;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class ParametersControlReaderTest {

  @Value("${SERVICE_ELIGIBILITY_PEFB_INPUT}")
  private String inputPefbFolder;

  @Autowired FileService fileService;

  ParametersControlReader parametersControlReader;

  @BeforeEach
  public void beforeEach() {
    parametersControlReader = new ParametersControlReader();
    ReflectionTestUtils.setField(parametersControlReader, "inputPefbFolder", inputPefbFolder);
  }

  @Test
  void shouldInitOutputFoldersAndExecutionVars_whenBeforeIsExecuted() throws Exception {
    StepExecution stepExecution = getStepExecution(PARAMETERS_CONTROL_STEP_NAME);
    parametersControlReader.beforeStep(stepExecution);

    AmcReferenceDate amcReferenceDate = parametersControlReader.read();

    Assertions.assertEquals("0000401471", amcReferenceDate.getAmc());
    Assertions.assertEquals("20210121", amcReferenceDate.getReferenceDate());

    parametersControlReader.afterStep(stepExecution);
  }
}
