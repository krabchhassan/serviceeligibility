package com.cegedim.next.serviceeligibility.purgeserviceprestationrdo.job;

import com.cegedim.next.serviceeligibility.purgeserviceprestationrdo.job.services.Processor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineEntryPoint implements ApplicationRunner {
  private final Processor processor;

  private final Tracer tracer;

  @Value("${JOB_SPAN_NAME:default_span}")
  private String spanName;

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
    int processReturnCode = processor.processPurge();

    log.info("Batch fini, code de retour : {}", processReturnCode);
    return processReturnCode;
  }
}
