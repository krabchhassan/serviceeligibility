package com.cegedim.next.consolidationcontract.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class defines the main class of next undue kernel API. */
@SpringBootApplication
@EnableTransactionManagement
public class ConsolidationContractWorker {

  /**
   * Allows to launch the service provider core API.
   *
   * @param args array of arguments.
   */
  public static void main(final String[] args) {
    new SpringApplication(ConsolidationContractWorker.class).run();
  }
}
