package com.cegedim.next.prestij.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class defines the main class of next undue kernel API. */
@SpringBootApplication
@EnableTransactionManagement
public class PrestijApi {

  /**
   * Allows to launch the service provider core API.
   *
   * @param args array of arguments.
   * @throws Exception the exception
   */
  public static void main(final String[] args) {
    new SpringApplication(PrestijApi.class).run();
  }
}
