package com.cegedim.next.serviceeligibility.batch635.job.configuration.extraction;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.fileIsEmpty;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.DELIMITER;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.ELIGIBLE_LINES_TMP_FILE_KEY;

import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import java.io.IOException;
import java.nio.file.Path;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Qualifier("extractionReader")
public class ExtractionReader implements ItemReader<AmcReferenceDateLineEligible> {

  private FlatFileItemReader<AmcReferenceDateLineEligible> delegateReader;

  @BeforeStep
  public void beforeStep(StepExecution stepExecution) throws IOException {
    Path eligibleLinesTmpFile = extractEligibleLinesTmpFile(stepExecution);

    if (fileIsEmpty(eligibleLinesTmpFile)) {
      return;
    }

    delegateReader = new FlatFileItemReader<>();
    delegateReader.setResource(new FileSystemResource(eligibleLinesTmpFile));
    delegateReader.setLineMapper(getLineMapper());
    delegateReader.open(stepExecution.getExecutionContext());
  }

  private LineMapper<AmcReferenceDateLineEligible> getLineMapper() {
    DefaultLineMapper<AmcReferenceDateLineEligible> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    BeanWrapperFieldSetMapper<AmcReferenceDateLineEligible> beanWrapperFieldSetMapper =
        new BeanWrapperFieldSetMapper<>();

    delimitedLineTokenizer.setNames("amc", "referenceDate");
    delimitedLineTokenizer.setDelimiter(DELIMITER);
    delimitedLineTokenizer.setStrict(false);

    beanWrapperFieldSetMapper.setTargetType(AmcReferenceDateLineEligible.class);

    lineMapper.setLineTokenizer(delimitedLineTokenizer);
    lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

    return lineMapper;
  }

  private Path extractEligibleLinesTmpFile(StepExecution stepExecution) {
    String eligibleLinesTmpFile =
        (String)
            stepExecution.getJobExecution().getExecutionContext().get(ELIGIBLE_LINES_TMP_FILE_KEY);
    return Path.of(eligibleLinesTmpFile);
  }

  @AfterStep
  public void afterStep(StepExecution stepExecution) {
    if (delegateReader != null) {
      delegateReader.close();
    }
  }

  @Override
  public AmcReferenceDateLineEligible read() throws Exception {
    if (delegateReader != null) {
      return this.delegateReader.read();
    }
    return null;
  }
}
