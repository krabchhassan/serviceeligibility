package com.cegedim.next.serviceeligibility.extractpopulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtractPopulation {
  public static void main(final String[] args) {
    new SpringApplication(ExtractPopulation.class).run(args).close();
  }
}
