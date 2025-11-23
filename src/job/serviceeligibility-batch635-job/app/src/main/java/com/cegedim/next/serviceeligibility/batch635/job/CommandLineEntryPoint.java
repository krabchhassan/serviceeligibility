package com.cegedim.next.serviceeligibility.batch635.job;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CommandLineEntryPoint implements ApplicationRunner {

  private final JobExecutor jobExecutor;

  private final Tracer tracer;

  private final String spanName;

  public CommandLineEntryPoint(
      JobExecutor jobExecutor,
      Tracer tracer,
      @Value("${JOB_SPAN_NAME:default_span}") String spanName) {
    this.jobExecutor = jobExecutor;
    this.tracer = tracer;
    this.spanName = spanName;
  }

  @Override
  public void run(ApplicationArguments args) {
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      jobExecutor.execute();
    } finally {
      newSpan.end();
    }
  }
}
