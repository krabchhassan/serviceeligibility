package com.cegedim.next.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** This class defines the main of the SpringBoot application. */
@SpringBootApplication
@EnableScheduling
public class KConnectApi {

  /**
   * Main method of the SpringBoot startup.
   *
   * @param args eventual arguments [OPTIONAL].
   */
  public static void main(final String[] args) {
    new SpringApplication(KConnectApi.class).run();
  }
}
