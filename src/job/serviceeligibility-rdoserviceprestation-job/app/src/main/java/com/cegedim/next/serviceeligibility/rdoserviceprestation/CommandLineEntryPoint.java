package com.cegedim.next.serviceeligibility.rdoserviceprestation;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommandLineEntryPoint implements ApplicationRunner {
  private OmuHelper omuHelper;

  private Processor processor;

  private String version;

  private Tracer tracer;

  @Value("${JOB_SPAN_NAME:default_span}")
  private String spanName;

  private final Logger logger = LoggerFactory.getLogger(CommandLineEntryPoint.class);

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.process(args);
    } finally {
      newSpan.end();
    }
  }

  private void process(ApplicationArguments args) {
    int returnCode = readParameters(args);
    if (0 == returnCode) {
      returnCode = processor.readFolders(version);
    }

    logger.debug("Batch fini, code de retour : {}", returnCode);
    System.exit(returnCode);
  }

  // Returns 0 if the arguments are correctly read, otherwise returns 1
  public int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      logger.debug("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      logger.error("Erreur lors du parsing des arguments : {}", e.getMessage(), e);
      return 1;
    }

    String contractVersion = arguments.getContractVersion();
    // By default, contractVersion is CONTRACT_VERSION_6
    if (StringUtils.isBlank(contractVersion)) {
      contractVersion = Constants.CONTRACT_VERSION_V6;
    }

    logger.info("contractVersion = {}", contractVersion);

    if (Constants.CONTRACT_VERSION_V5.equals(contractVersion)
        || Constants.CONTRACT_VERSION_V6.equals(contractVersion)) {
      version = contractVersion;
      return 0;
    } else {
      logger.error(
          "Erreur lors de la lecture des arguments : le valeurs acceptées pour contract_version sont {} ou {}",
          Constants.CONTRACT_VERSION_V5,
          Constants.CONTRACT_VERSION_V6);
      return 1;
    }
  }
}
