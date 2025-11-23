package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdo;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Processor implements ApplicationContextAware {

  private final Logger logger = LoggerFactory.getLogger(Processor.class);

  @Value("${CI_COVERAGE_ENABLED:false}")
  private Boolean keepRunning;

  private ApplicationContext context;

  private final FileService sftp;
  private final DateTimeFormatter df;

  private final CrexProducer crexProducer;

  public Processor(FileService sftp, CrexProducer crexProducer) {
    this.sftp = sftp;
    df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    this.crexProducer = crexProducer;
  }

  @Override
  @ContinueSpan(log = "setApplicationContext")
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    this.context = ctx;
  }

  public int readFolders(String version) {
    int status;
    CompteRenduRdo compteRendu = new CompteRenduRdo();
    String now = LocalDateTime.now().format(df);
    boolean readAllFiles = sftp.init();
    if (!readAllFiles) {
      logger.error("error reading folder");
      status = 1;
    } else {
      boolean problemWhileProcessing = sftp.processFolder(now, compteRendu, version);
      if (!problemWhileProcessing) {
        logger.info("Job Success");
        status = 0;
      } else {
        logger.error("Error while processing folder");
        status = 1;
      }
    }
    if (context != null && !keepRunning) {
      ((ConfigurableApplicationContext) context).close();
    }

    crexProducer.generateCrex(compteRendu);

    return status;
  }
}
