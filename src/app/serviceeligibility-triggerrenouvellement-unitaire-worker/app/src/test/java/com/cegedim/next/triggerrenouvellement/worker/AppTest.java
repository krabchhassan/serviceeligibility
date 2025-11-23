package com.cegedim.next.triggerrenouvellement.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/** This class defines the main of the SpringBoot application test. */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
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
