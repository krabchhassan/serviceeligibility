package com.cegedim.next.serviceeligibility.batch620.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Batch620Job {
  public static void main(final String[] args) {
    new SpringApplication(Batch620Job.class).run(args).close();
  }
}
