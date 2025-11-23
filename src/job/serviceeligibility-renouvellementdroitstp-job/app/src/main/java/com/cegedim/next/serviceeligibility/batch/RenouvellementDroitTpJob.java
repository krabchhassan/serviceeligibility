package com.cegedim.next.serviceeligibility.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RenouvellementDroitTpJob {

  public static void main(final String[] args) {
    new SpringApplication(RenouvellementDroitTpJob.class).run(args);
  }
}
