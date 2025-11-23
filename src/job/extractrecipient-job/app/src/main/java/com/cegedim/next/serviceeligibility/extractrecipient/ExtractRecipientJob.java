package com.cegedim.next.serviceeligibility.extractrecipient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtractRecipientJob {
  public static void main(final String[] args) {
    new SpringApplication(ExtractRecipientJob.class).run().close();
  }
}
