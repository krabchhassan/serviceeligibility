package com.cegedim.next.serviceeligibility.batch635.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
    exclude = {
      MongoReactiveAutoConfiguration.class,
      MongoReactiveDataAutoConfiguration.class,
      MongoDataAutoConfiguration.class,
      DataSourceAutoConfiguration.class,
      SecurityAutoConfiguration.class
    })
@EnableBatchProcessing
public class Batch635Job {
  public static void main(final String[] args) {
    new SpringApplication(Batch635Job.class).run().close();
  }
}
