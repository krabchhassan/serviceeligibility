package com.cegedim.next.triggerrenouvellement.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class defines the main class of next undue kernel API. */
@SpringBootApplication
@EnableTransactionManagement
public class TriggerRenouvellementUnitaireApi {
  /**
   * Allows to launch the service provider core API.
   *
   * @param args array of arguments.
   * @throws Exception the exception
   */
  public static void main(final String[] args) {
    new SpringApplication(TriggerRenouvellementUnitaireApi.class).run();
  }
}
