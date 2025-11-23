package com.cegedim.next.serviceeligibility.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** This class defines the main of the SpringBoot application test. */
@SpringBootApplication
public class AppTest {

  /**
   * Main method of the SpringBoot startup.
   *
   * @param args eventual arguments [OPTIONAL].
   */
  public static void main(final String[] args) {
    new SpringApplication(AppTest.class).run();
  }
}
