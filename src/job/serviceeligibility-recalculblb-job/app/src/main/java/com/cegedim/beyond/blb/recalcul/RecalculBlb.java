package com.cegedim.beyond.blb.recalcul;

import com.cegedim.beyond.blb.recalcul.service.BlbRecalculService;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class RecalculBlb {
  public static void main(final String[] args) {
    final ConfigurableApplicationContext context = new SpringApplication(RecalculBlb.class).run();
    final String jobSpanName = context.getEnvironment().getProperty("JOB_SPAN_NAME");
    final Tracer tracing = context.getBean(Tracer.class);
    final Span newSpan = tracing.nextSpan().name(jobSpanName);
    try (final Tracer.SpanInScope ws = tracing.withSpan(newSpan.start())) {
      job(context);
    } finally {
      newSpan.end();
    }
  }

  private static Runnable job(final ConfigurableApplicationContext context) {
    return () -> {
      try {
        System.exit(context.getBean(BlbRecalculService.class).processStart());
      } catch (final InterruptedException | KafkaSendingException e) {
        Thread.currentThread().interrupt();
      }
    };
  }
}
