package com.cegedim.next.serviceeligibility.batch;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.BATCH_MODE_NO_RDO;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.BATCH_MODE_RDO;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.batch.services.Engine;
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

  private final Engine engine;

  private final Tracer tracer;

  private final String spanName;

  private boolean isRdo;

  public CommandLineEntryPoint(
      OmuHelper omuHelper,
      Engine engine,
      Tracer tracer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.omuHelper = omuHelper;
    this.engine = engine;
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
    int engineReturnCode = 0;
    try {
      engineReturnCode = engine.processStart(result, isRdo);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      Thread.currentThread().interrupt();
    }
    if (engineReturnCode == -1) {
      log.error("Erreur lors du traitement du batch : {}", engineReturnCode);
    } else {
      log.debug("Batch fini, code de retour : {}", engineReturnCode);
    }
    System.exit(engineReturnCode);
  }

  // Returns 0 if the arguments are correctly read, otherwise returns 1
  public int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      log.debug("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error(String.format("Erreur lors du parsing des arguments : %s", e.getMessage()), e);
      return 1;
    }

    String batchMode = arguments.getBatchMode();
    // By default, batchMode is NO_RDO
    if (StringUtils.isBlank(batchMode)) {
      batchMode = BATCH_MODE_NO_RDO;
    }

    log.debug("batchMode = {}", batchMode);

    if (BATCH_MODE_RDO.equals(batchMode)) {
      isRdo = true;
      return 0;
    } else if (BATCH_MODE_NO_RDO.equals(batchMode)) {
      isRdo = false;
      return 0;
    } else {
      log.error(
          "Erreur lors de la lecture des arguments : le valeurs acceptées pour batch_mode sont {} ou {}",
          BATCH_MODE_RDO,
          BATCH_MODE_NO_RDO);
      return 1;
    }
  }
}
