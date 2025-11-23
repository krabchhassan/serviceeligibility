package com.cegedim.next.serviceeligibility.batch635.job.configuration.parameters_control;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.extractFileName;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.fileIsEmpty;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.DELIMITER;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.EMPTY_INPUT_FILE_KEY;

import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDate;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@Qualifier("parametersControlReader")
@StepScope
public class ParametersControlReader implements ItemReader<AmcReferenceDate> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParametersControlReader.class);

  @Value("${SERVICE_ELIGIBILITY_PEFB_INPUT}")
  private String inputPefbFolder;

  private FlatFileItemReader<AmcReferenceDate> delegateReader;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) throws IOException {
    JobExecution jobExecution = stepExecution.getJobExecution();
    String fileName = extractFileName(stepExecution);

    Path filePath = Paths.get(inputPefbFolder, fileName);

    if (fileIsEmpty(filePath)) {
      LOGGER.info("The file => '{}' is empty.", fileName);
      jobExecution.getExecutionContext().put(EMPTY_INPUT_FILE_KEY, true);
      return;
    }

    delegateReader = new FlatFileItemReader<>();
    delegateReader.setResource(new FileSystemResource(filePath));
    delegateReader.setLineMapper(getLineMapper());
    delegateReader.open(stepExecution.getExecutionContext());
  }

  private LineMapper<AmcReferenceDate> getLineMapper() {
    DefaultLineMapper lineMapper = new DefaultLineMapper();
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    BeanWrapperFieldSetMapper beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper();

    delimitedLineTokenizer.setNames("amc", "referenceDate");
    delimitedLineTokenizer.setDelimiter(DELIMITER);
    delimitedLineTokenizer.setStrict(false);
    beanWrapperFieldSetMapper.setTargetType(AmcReferenceDate.class);

    lineMapper.setLineTokenizer(delimitedLineTokenizer);
    lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

    return lineMapper;
  }

  @AfterStep
  public void afterStep(StepExecution stepExecution) {
    if (delegateReader != null) {
      delegateReader.close();
    }
  }

  @Override
  public AmcReferenceDate read() throws Exception {
    if (delegateReader != null) {
      return this.delegateReader.read();
    }
    return null;
  }
}
