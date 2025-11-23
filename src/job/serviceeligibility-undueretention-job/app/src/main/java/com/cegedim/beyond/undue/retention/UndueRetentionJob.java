package com.cegedim.beyond.undue.retention;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UndueRetentionJob {
  public static void main(final String[] args) {
    new SpringApplication(UndueRetentionJob.class).run(args);
  }
}
