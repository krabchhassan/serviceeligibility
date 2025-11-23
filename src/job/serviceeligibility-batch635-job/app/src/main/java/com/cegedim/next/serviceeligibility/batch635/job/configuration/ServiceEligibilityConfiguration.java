package com.cegedim.next.serviceeligibility.batch635.job.configuration;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;

import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.configuration.listener.ServiceEligibilityListener;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDate;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineHelper;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import({OmuHelperConfiguration.class})
public class ServiceEligibilityConfiguration {

  public ServiceEligibilityConfiguration(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ServiceEligibilityListener serviceEligibilityListener) {
    this.jobRepository = jobRepository;
    this.transactionManager = transactionManager;
    this.serviceEligibilityListener = serviceEligibilityListener;
  }

  private final JobRepository jobRepository;

  private final PlatformTransactionManager transactionManager;

  private final ServiceEligibilityListener serviceEligibilityListener;

  @Bean
  public Step parametersControlStep(
      @Qualifier("parametersControlReader") ItemReader<AmcReferenceDate> parametersControlReader,
      @Qualifier("parametersControlProcessor")
          ItemProcessor<AmcReferenceDate, AmcReferenceDateLineHelper> parametersControlProcessor,
      @Qualifier("parametersControlWriter")
          ItemWriter<AmcReferenceDateLineHelper> parametersControlWriter) {
    return new StepBuilder(PARAMETERS_CONTROL_STEP_NAME, jobRepository)
        .<AmcReferenceDate, AmcReferenceDateLineHelper>chunk(1000, transactionManager)
        .reader(parametersControlReader)
        .processor(parametersControlProcessor)
        .writer(parametersControlWriter)
        .build();
  }

  @Bean
  public Step extractionStep(
      @Qualifier("extractionReader")
          ItemReader<AmcReferenceDateLineEligible> extractionTreatmentReader,
      @Qualifier("extractionWriter")
          ItemWriter<AmcReferenceDateLineEligible> extractionTreatmentWriter) {
    return new StepBuilder(EXTRACTION_STEP_NAME, jobRepository)
        .<AmcReferenceDateLineEligible, AmcReferenceDateLineEligible>chunk(1000, transactionManager)
        .reader(extractionTreatmentReader)
        .writer(extractionTreatmentWriter)
        .build();
  }

  @Bean
  public Job batch635() {
    return new JobBuilder(JOB_NAME, jobRepository)
        .incrementer(new RunIdIncrementer())
        .listener(serviceEligibilityListener)
        .start(parametersControlStep(null, null, null))
        .next(extractionStep(null, null))
        .build();
  }

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }
}
