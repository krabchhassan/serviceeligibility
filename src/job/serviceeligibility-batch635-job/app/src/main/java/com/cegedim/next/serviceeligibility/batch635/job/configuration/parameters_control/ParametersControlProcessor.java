package com.cegedim.next.serviceeligibility.batch635.job.configuration.parameters_control;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.COMMON_DATE_FORMATTER;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.extractFileName;

import com.cegedim.next.serviceeligibility.batch635.job.domain.service.DeclarantsService;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDate;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineErrors;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineHelper;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("parametersControlProcessor")
@StepScope
@RequiredArgsConstructor
public class ParametersControlProcessor
    implements ItemProcessor<AmcReferenceDate, AmcReferenceDateLineHelper> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParametersControlProcessor.class);

  private int lineNumber = 0;
  private String fileName;

  private final DeclarantsService declarantsService;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    fileName = extractFileName(stepExecution);
  }

  @Override
  public AmcReferenceDateLineHelper process(AmcReferenceDate item) {
    lineNumber++;
    String amc = item.getAmc();
    String referenceDate = item.getReferenceDate();
    return handleAmcAndReferenceDate(amc, referenceDate);
  }

  private AmcReferenceDateLineHelper handleAmcAndReferenceDate(String amc, String referenceDate) {
    LOGGER.info("Controlling line number '{}' for file => {}", lineNumber, fileName);

    List<String> errors = new ArrayList<>();

    String eligibleAmc = getEligibleAmc(amc, errors);
    String eligibleReferenceDate = getEligibleReferenceDate(referenceDate, errors);

    if (eligibleAmc != null && eligibleReferenceDate != null) {
      return AmcReferenceDateLineEligible.builder()
          .amc(eligibleAmc)
          .referenceDate(eligibleReferenceDate)
          .build();
    }
    return new AmcReferenceDateLineErrors(errors);
  }

  private String getEligibleAmc(String amc, List<String> errors) {
    boolean hasErrors = false;
    if (amc.isBlank()) {
      errors.add(
          String.format("Le numéro de l’AMC n’est pas renseigné sur la ligne '%d'", lineNumber));
      hasErrors = true;
    } else if (amc.length() != 10) {
      errors.add(
          String.format(
              "La taille du numéro de l’AMC '%s' doit être sur 10 caractères (Ligne '%d')",
              amc, lineNumber));
      hasErrors = true;
    } else {
      boolean contratExistsByAmc = declarantsService.declarantExistsById(amc);
      if (!contratExistsByAmc) {
        errors.add(
            String.format("Le numéro de l’AMC '%s' n’existe pas (Ligne '%d')", amc, lineNumber));
        hasErrors = true;
      }
    }
    return !hasErrors ? amc : null;
  }

  private String getEligibleReferenceDate(String referenceDate, List<String> errors) {
    boolean hasErrors = false;
    if (referenceDate.isBlank()) {
      errors.add(
          String.format("La date de référence n’est pas renseignée sur la ligne '%d'", lineNumber));
      hasErrors = true;
    } else if (referenceDate.length() != 8) {
      errors.add(String.format("La date de référence est erronée sur la ligne '%d'", lineNumber));
      hasErrors = true;
    } else {
      try {
        COMMON_DATE_FORMATTER.parse(referenceDate);
      } catch (DateTimeParseException e) {
        errors.add(String.format("La date de référence est erronée sur la ligne '%d'", lineNumber));
        hasErrors = true;
      }
    }
    return !hasErrors ? referenceDate : null;
  }
}
