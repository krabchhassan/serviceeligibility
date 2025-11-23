package com.cegedim.next.serviceeligibility.batch620extractconso;

import static com.cegedim.next.serviceeligibility.batch620extractconso.constants.Constants.INVALID_ARGUMENT;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.batch620extractconso.services.Processor;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduBatch620ExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.job.DataForJob620;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandLineEntryPoint implements ApplicationRunner {
  private final OmuHelper omuHelper;

  private final String spanName;

  private final Tracer tracer;

  private final Processor processor;

  private final CrexProducer crexProducer;

  private final String clientType;

  public CommandLineEntryPoint(
      OmuHelper omuHelper,
      Tracer tracer,
      Processor processor,
      CrexProducer crexProducer,
      @Value("${CLIENT_TYPE:INSURER}") String clientType,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.omuHelper = omuHelper;
    this.tracer = tracer;
    this.processor = processor;
    this.crexProducer = crexProducer;
    this.clientType = clientType;
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
    CompteRenduBatch620ExtractionConso compteRendu = new CompteRenduBatch620ExtractionConso();
    int processReturnCode = readParameters(args);
    if (processReturnCode != INVALID_ARGUMENT) {
      DataForJob620 dataForJob620 = new DataForJob620();
      dataForJob620.setJddSize(0);
      dataForJob620.setToday(new Date());
      dataForJob620.setClientType(clientType);
      processReturnCode = processor.process(dataForJob620, compteRendu);
    }
    crexProducer.generateCrex(compteRendu);
    log.info("Batch fini, code de retour : {}", processReturnCode);
    System.exit(processReturnCode);
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
    return 0;
  }
}
