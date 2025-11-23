package com.cegedim.next.serviceeligibility.rdoserviceprestationpurge;

import static com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.constants.Constants.*;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandLineEntryPoint implements ApplicationRunner {
  private final OmuHelper omuHelper;

  private final Processor processor;

  private String input;

  private final Tracer tracer;

  private final String spanName;

  public CommandLineEntryPoint(
      OmuHelper omuHelper,
      Processor processor,
      Tracer tracer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.omuHelper = omuHelper;
    this.processor = processor;
    this.tracer = tracer;
    this.spanName = spanName;
  }

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
    int result = readParameters(args);
    int processReturnCode = processor.readFolders(result, input);

    log.debug("Batch fini, code de retour : {}", processReturnCode);
    System.exit(processReturnCode);
  }

  public int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      log.debug("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error(String.format("Erreur lors du parsing des arguments : %s", e.getMessage()), e);
      return INVALID_ARGUMENT;
    }

    String nomFichier = arguments.getNomFichier();
    String numAmc = arguments.getNumAmc();
    log.debug("nomFichier = {} | numAmc = {}", nomFichier, numAmc);

    if (StringUtils.isNotEmpty(nomFichier) ^ StringUtils.isNotEmpty(numAmc)) {
      if (StringUtils.isNotEmpty(nomFichier)) {
        input = nomFichier;
        return SEARCH_BY_FILE_NAME;
      }
      input = numAmc;
      return SEARCH_BY_AMC_NUMBER;
    }

    return INVALID_ARGUMENT;
  }
}
