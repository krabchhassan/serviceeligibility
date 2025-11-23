package com.cegedim.next.serviceeligibility.initrdoclaim;

import static com.cegedim.next.serviceeligibility.initrdoclaim.constants.Constants.INVALID_ARGUMENT;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdoClaim;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.initrdoclaim.services.RdoClaimProcessor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandLineEntryPoint implements ApplicationRunner {

  private final RdoClaimProcessor rdoClaimProcessor;

  private final CrexProducer crexProducer;

  private final String spanName;

  private final Tracer tracer;

  private final OmuHelper omuHelper;

  private String numAmc;

  public CommandLineEntryPoint(
      RdoClaimProcessor rdoClaimProcessor,
      CrexProducer crexProducer,
      Tracer tracer,
      OmuHelper omuHelper,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.rdoClaimProcessor = rdoClaimProcessor;
    this.crexProducer = crexProducer;
    this.tracer = tracer;
    this.omuHelper = omuHelper;
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

  private int process(ApplicationArguments args) {
    CompteRenduRdoClaim compteRenduRdoClaim = new CompteRenduRdoClaim();
    int processReturnCode = readParameters(args);
    if (processReturnCode != INVALID_ARGUMENT) {
      processReturnCode = rdoClaimProcessor.fillCollection(numAmc, compteRenduRdoClaim);
    }
    crexProducer.generateCrex(compteRenduRdoClaim);
    log.info("Batch fini, code de retour : {}", processReturnCode);
    return processReturnCode;
  }

  public int readParameters(ApplicationArguments args) {
    OmuCommand arguments = new OmuCommand();

    try {
      log.info("Lecture des paramètres d'entrée");
      omuHelper.parseArgs(arguments, args.getSourceArgs());
    } catch (SecuredAnalyzerException e) {
      log.error(String.format("Erreur lors du parsing des arguments : %s", e.getMessage()), e);
      return INVALID_ARGUMENT;
    }

    numAmc = arguments.getNumAmc();
    log.debug("numAmc = {}", numAmc);
    return 0;
  }
}
