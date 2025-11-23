package com.cegedim.next.serviceeligibility.extractbenefmultios;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExtractBenefMultiOS;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.extractbenefmultios.constants.Constants;
import com.cegedim.next.serviceeligibility.extractbenefmultios.services.Processor;
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

  private final Processor processor;

  private final CrexProducer crexProducer;

  private final String spanName;

  private final boolean extractFlux;

  private final Tracer tracer;

  public CommandLineEntryPoint(
      Processor processor,
      CrexProducer crexProducer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName,
      @Value("${EXTRACT_FLUX:false}") boolean extractFlux,
      Tracer tracer) {
    this.processor = processor;
    this.crexProducer = crexProducer;
    this.spanName = spanName;
    this.extractFlux = extractFlux;
    this.tracer = tracer;
  }

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.process();
    } finally {
      newSpan.end();
    }
  }

  private int process() {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;

    CompteRenduExtractBenefMultiOS compteRenduExtractBenefMultiOS =
        new CompteRenduExtractBenefMultiOS();

    if (extractFlux) {
      processReturnCode = processor.calcul(compteRenduExtractBenefMultiOS);
    }

    crexProducer.generateCrex(compteRenduExtractBenefMultiOS);

    log.info("Batch fini, code de retour : {}", processReturnCode);
    return processReturnCode;
  }
}
