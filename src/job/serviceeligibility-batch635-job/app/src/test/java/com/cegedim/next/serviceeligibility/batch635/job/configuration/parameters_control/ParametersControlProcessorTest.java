package com.cegedim.next.serviceeligibility.batch635.job.configuration.parameters_control;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.PARAMETERS_CONTROL_STEP_NAME;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.JobUtils.getStepExecution;
import static org.mockito.Mockito.when;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.domain.service.DeclarantsService;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDate;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineErrors;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class ParametersControlProcessorTest {
  @MockBean DeclarantsService declarantsService;

  ParametersControlProcessor parametersControlProcessor;

  @BeforeEach
  public void beforeEach() {
    parametersControlProcessor = new ParametersControlProcessor(declarantsService);
    ReflectionTestUtils.setField(
        parametersControlProcessor, "declarantsService", declarantsService);
  }

  @Test
  void shouldReturnEligibleLine_whenNoErrorsAreFound() throws Exception {
    String amc = "0000401471";
    String referenceDate = "20210121";

    when(declarantsService.declarantExistsById(amc)).thenReturn(true);

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineEligible);

    AmcReferenceDateLineEligible amcReferenceDateLineEligible = (AmcReferenceDateLineEligible) item;

    Assertions.assertEquals(amc, amcReferenceDateLineEligible.getAmc());
    Assertions.assertEquals(referenceDate, amcReferenceDateLineEligible.getReferenceDate());
  }

  @Test
  void shouldReturnAmcErrors_whenAmcDoesntExistInDb() throws Exception {
    String amc = "wrong-1234";
    String referenceDate = "20210121";

    when(declarantsService.declarantExistsById(amc)).thenReturn(false);

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineErrors);

    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;

    Assertions.assertEquals(1, amcReferenceDateLineErrors.getErrors().size());
    Assertions.assertTrue(
        amcReferenceDateLineErrors.getErrors().stream()
            .findAny()
            .get()
            .startsWith("Le numéro de l’AMC '" + amc + "' n’existe pas"));
  }

  @Test
  void shouldReturnAmcErrors_whenAmcIsEmpty() throws Exception {
    String amc = "";
    String referenceDate = "20210121";

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineErrors);

    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;

    Assertions.assertEquals(1, amcReferenceDateLineErrors.getErrors().size());
    Assertions.assertTrue(
        amcReferenceDateLineErrors.getErrors().stream()
            .findAny()
            .get()
            .startsWith("Le numéro de l’AMC n’est pas renseigné sur la ligne"));
  }

  @Test
  void shouldReturnAmcErrors_whenAmcIsNotIn10Characters() throws Exception {
    String amc = "12345";
    String referenceDate = "20210121";

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineErrors);

    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;

    Assertions.assertEquals(1, amcReferenceDateLineErrors.getErrors().size());
    Assertions.assertTrue(
        amcReferenceDateLineErrors.getErrors().stream()
            .findAny()
            .get()
            .startsWith("La taille du numéro de l’AMC "));
  }

  @Test
  void shouldSetReferenceDateErrors_whenReferenceDateIsEmpty() throws Exception {
    String amc = "0000401471";
    String referenceDate = "";

    when(declarantsService.declarantExistsById(amc)).thenReturn(true);

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineErrors);

    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;

    Assertions.assertEquals(1, amcReferenceDateLineErrors.getErrors().size());
    Assertions.assertTrue(
        amcReferenceDateLineErrors.getErrors().stream()
            .findAny()
            .get()
            .startsWith("La date de référence n’est pas renseignée sur la ligne"));
  }

  @Test
  void shouldSetReferenceDateErrors_whenReferenceDateLengthIsLessThan8Char() throws Exception {
    String amc = "0000401471";
    String referenceDate = "202101";

    when(declarantsService.declarantExistsById(amc)).thenReturn(true);

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineErrors);

    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;

    Assertions.assertEquals(1, amcReferenceDateLineErrors.getErrors().size());
    Assertions.assertTrue(
        amcReferenceDateLineErrors.getErrors().stream()
            .findAny()
            .get()
            .startsWith("La date de référence est erronée sur la ligne"));
  }

  @Test
  void shouldSetReferenceDateErrors_whenReferenceDateIsInvalid() throws Exception {
    String amc = "0000401471";
    String referenceDate = "2021E12Q";

    when(declarantsService.declarantExistsById(amc)).thenReturn(true);

    AmcReferenceDate amcReferenceDate = setup(amc, referenceDate);

    AmcReferenceDateLineHelper item = parametersControlProcessor.process(amcReferenceDate);

    Assertions.assertTrue(item instanceof AmcReferenceDateLineErrors);

    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;

    Assertions.assertEquals(1, amcReferenceDateLineErrors.getErrors().size());
    Assertions.assertTrue(
        amcReferenceDateLineErrors.getErrors().stream()
            .findAny()
            .get()
            .startsWith("La date de référence est erronée sur la ligne"));
  }

  private AmcReferenceDate setup(String amc, String referenceDate) throws Exception {
    AmcReferenceDate amcReferenceDate =
        AmcReferenceDate.builder().amc(amc).referenceDate(referenceDate).build();
    StepExecution stepExecution = getStepExecution(PARAMETERS_CONTROL_STEP_NAME);
    parametersControlProcessor.beforeStep(stepExecution);
    return amcReferenceDate;
  }
}
