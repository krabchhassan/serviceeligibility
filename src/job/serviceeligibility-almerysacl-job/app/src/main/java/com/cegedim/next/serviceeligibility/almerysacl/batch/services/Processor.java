package com.cegedim.next.serviceeligibility.almerysacl.batch.services;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Processor implements ApplicationRunner {

  @Value("${JOB_SPAN_NAME:default_span}")
  private String spanName;

  private final Tracer tracer;

  private final SftpService sftp;

  public int readFolders() {
    int status = -1;
    try {
      List<String> files = sftp.listFiles();
      if (files == null) {
        status = 1;
      } else {
        boolean problemWhileProcessing = sftp.processFolder(files);
        if (!problemWhileProcessing) {
          log.info("Job Success");
          status = 0;
        } else {
          log.error("Error while processing input folder");
          status = 1;
        }
      }
    } catch (IOException e) {
      status = 1;
    }

    return status;
  }

  @Override
  public void run(ApplicationArguments args) {
    log.info("Set up tracer for span {}", spanName);
    Span newSpan = tracer.nextSpan().name(spanName).start();
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(newSpan.start())) {
      this.readFolders();
    } finally {
      newSpan.end();
    }
  }
}
