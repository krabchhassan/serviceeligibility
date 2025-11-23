package com.cegedim.next.serviceeligibility.batch635.job.configuration.parameters_control;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFileIfExists;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.extractFileName;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineErrors;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineHelper;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper;
import com.cegedim.next.serviceeligibility.batch635.job.utils.Constants;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.WriterNotOpenException;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@Qualifier("parametersControlWriter")
@StepScope
public class ParametersControlWriter implements ItemWriter<AmcReferenceDateLineHelper> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ParametersControlWriter.class);

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @Value("${spring.data.mongodb.uri}")
  private String databaseUrl;

  private FlatFileItemWriter<String> arlFileWriter;
  private FlatFileItemWriter<AmcReferenceDateLineEligible> eligibleLinesTmpFileWriter;

  private Path arlFilePath;
  private String arlFileName;
  private Path eligibleLinesTmpFile;

  @Value("${TARGET_ENV:local}")
  private String targetEnv;

  private int numbersOfOkItems;
  private List<String> errors;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) {
    errors = new ArrayList<>();
    ExecutionContext executionContext = stepExecution.getExecutionContext();
    String fileName = extractFileName(stepExecution);

    LOGGER.info("Generating Arl file for the file => '{}'.", fileName);

    configureArlWriter();
    configureEligibleLinesTmpFileWriter(fileName);

    arlFileWriter.open(executionContext);
    eligibleLinesTmpFileWriter.open(executionContext);

    handleEmptyInputFile(stepExecution);
  }

  private void configureArlWriter() {
    String dateFormat =
        Constants.UNDERSCORE
            + LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    handleArlFileName(dateFormat);

    arlFilePath = Paths.get(outputPefbFolder, arlFileName);

    arlFileWriter = new FlatFileItemWriter<>();
    arlFileWriter.setAppendAllowed(true);
    arlFileWriter.setResource(new FileSystemResource(arlFilePath));
    arlFileWriter.setEncoding(COMMON_FILE_ENCODING);
    arlFileWriter.setLineAggregator(new PassThroughLineAggregator<>());
  }

  @SneakyThrows
  private void handleArlFileName(String dateStamp) {
    arlFileName = Helper.generateFileName(DOT + ARL_FILE_NAME_PREFIX + dateStamp);
  }

  private void configureEligibleLinesTmpFileWriter(String fileName) {
    String eligibleLinesTmpFileNamePrefix = ELIGIBLE_LINES_TMP_FILE_NAME_PREFIX + fileName;
    String eligibleLinesTmpFileName = Helper.generateFileName(eligibleLinesTmpFileNamePrefix);

    eligibleLinesTmpFile = Paths.get(OUTPUT_TMP_FOLDER, eligibleLinesTmpFileName);

    eligibleLinesTmpFileWriter = new FlatFileItemWriter<>();
    eligibleLinesTmpFileWriter.setAppendAllowed(true);
    eligibleLinesTmpFileWriter.setResource(new FileSystemResource(eligibleLinesTmpFile));
    eligibleLinesTmpFileWriter.setEncoding(COMMON_FILE_ENCODING);

    BeanWrapperFieldExtractor<AmcReferenceDateLineEligible> fieldExtractor =
        new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(new String[] {"amc", "referenceDate"});

    DelimitedLineAggregator<AmcReferenceDateLineEligible> lineAggregator =
        new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(DELIMITER);
    lineAggregator.setFieldExtractor(fieldExtractor);

    eligibleLinesTmpFileWriter.setLineAggregator(lineAggregator);
  }

  private void handleEmptyInputFile(StepExecution stepExecution) {
    try {
      Object emptyFilePlaceholder =
          stepExecution.getJobExecution().getExecutionContext().get(EMPTY_INPUT_FILE_KEY);
      if (emptyFilePlaceholder != null) {
        String error = "Le fichier de demande d’extraction est vide !";
        errors.add(error);
      }
    } catch (Exception ex) {
      throw new IllegalStateException("Error while writing errors to Arl file.", ex);
    }
  }

  @AfterStep
  public ExitStatus afterStep(StepExecution stepExecution) {
    ExitStatus exitStatus = stepExecution.getExitStatus();

    if (exitStatus.equals(ExitStatus.COMPLETED)) {
      try {
        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(HIDDEN_ARL_FILE_NAME, arlFileName);
        writeArlFileHeaders(stepExecution.getReadCount());
        closeWriters();
        finishFilesTreatment(stepExecution);
      } catch (Exception ex) {
        LOGGER.error("Error occurred during parameters control step", ex);
        cleanWhenFailure();
        throw new IllegalStateException("Error occurred during parameters control step", ex);
      }
    } else {
      cleanWhenFailure();
    }
    return exitStatus;
  }

  private void cleanWhenFailure() {
    closeWriters();
    deleteFileIfExists(arlFilePath);
    deleteFileIfExists(eligibleLinesTmpFile);
  }

  private void writeArlFileHeaders(long totalItems) {
    try {
      Chunk<String> chunck = new Chunk<>();
      chunck.add(LocalDate.now(ZoneOffset.UTC).toString());
      chunck.add(LocalDateTime.now(ZoneOffset.UTC).toLocalTime().toString());
      chunck.add(targetEnv);
      chunck.add("Nombre de demande d’extraction à traiter : " + totalItems);
      chunck.add("Contrôle des paramètres : ");
      for (String error : errors) {
        chunck.add(error);
      }
      chunck.add("Nombre d'AMC OK à traiter : " + numbersOfOkItems);
      arlFileWriter.write(chunck);
    } catch (WriterNotOpenException e) {
      LOGGER.error("Writer not open for file : {}", arlFileName);
      throw new IllegalStateException("Writer not open for file : " + arlFileName);
    } catch (Exception ex) {
      LOGGER.error("Error occured while writing content to ARL FILE : {}", arlFileName);
      throw new IllegalStateException(
          "Error occured while writing content to ARL FILE : " + arlFileName);
    }
  }

  private void closeWriters() {
    if (arlFileWriter != null) {
      arlFileWriter.close();
    }
    if (eligibleLinesTmpFileWriter != null) {
      eligibleLinesTmpFileWriter.close();
    }
  }

  private void finishFilesTreatment(StepExecution stepExecution) {
    if (eligibleLinesTmpFileWriter != null) {
      putEligibleLineTmpFileNameInJobExecution(stepExecution);
    }
  }

  private void putEligibleLineTmpFileNameInJobExecution(StepExecution stepExecution) {
    JobExecution jobExecution = stepExecution.getJobExecution();
    jobExecution
        .getExecutionContext()
        .put(ELIGIBLE_LINES_TMP_FILE_KEY, eligibleLinesTmpFile.toString());
  }

  @Override
  public void write(Chunk<? extends AmcReferenceDateLineHelper> chunk) throws Exception {
    for (AmcReferenceDateLineHelper item : chunk) {
      if (item instanceof AmcReferenceDateLineErrors) {
        writeErrorsToArlFile(item);
      } else if (item instanceof AmcReferenceDateLineEligible) {
        writeLinesToEligibleLinesTmpFile(item);
        numbersOfOkItems++;
      }
    }
  }

  private void writeErrorsToArlFile(AmcReferenceDateLineHelper item) {
    AmcReferenceDateLineErrors amcReferenceDateLineErrors = (AmcReferenceDateLineErrors) item;
    errors.addAll(amcReferenceDateLineErrors.getErrors());
  }

  private void writeLinesToEligibleLinesTmpFile(AmcReferenceDateLineHelper item) {
    try {
      AmcReferenceDateLineEligible amcReferenceDateLineEligible =
          (AmcReferenceDateLineEligible) item;
      eligibleLinesTmpFileWriter.write(Chunk.of(amcReferenceDateLineEligible));
    } catch (Exception ex) {
      throw new IllegalStateException("Error while writing eligible lines to tmp file.", ex);
    }
  }
}
