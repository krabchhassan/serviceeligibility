package com.cegedim.next.serviceeligibility.exporttriggerinfos;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduExportTriggerInfos;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.exporttriggerinfos.services.Processor;
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
  private final Tracer tracer;

  private final String spanName;

  public CommandLineEntryPoint(
      Processor processor,
      CrexProducer crexProducer,
      Tracer tracer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.processor = processor;
    this.crexProducer = crexProducer;
    this.tracer = tracer;
    this.spanName = spanName;
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
    int processReturnCode;

    CompteRenduExportTriggerInfos compteRenduExportTriggerInfos =
        new CompteRenduExportTriggerInfos();

    processReturnCode = processor.export(compteRenduExportTriggerInfos);

    crexProducer.generateCrex(compteRenduExportTriggerInfos);

    log.info("Batch fini, code de retour : {}", processReturnCode);
    return processReturnCode;
  }
}
