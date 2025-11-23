package com.cegedim.next.serviceeligibility.purgeserviceprestationrdo.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PurgeRdoServicePrestation {
  public static void main(final String[] args) {
    new SpringApplication(PurgeRdoServicePrestation.class).run(args).close();
  }
}
