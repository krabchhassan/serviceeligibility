package com.cegedim.beyond.undue.retention;

import com.cegedim.beyond.undue.retention.services.Engine;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CommandLineEntryPoint implements ApplicationRunner {
  private final Engine engine;

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

  private void process() {
    int returnCode = engine.process();

    log.debug("Batch fini, code de retour : {}", returnCode);
    System.exit(returnCode);
  }
}
