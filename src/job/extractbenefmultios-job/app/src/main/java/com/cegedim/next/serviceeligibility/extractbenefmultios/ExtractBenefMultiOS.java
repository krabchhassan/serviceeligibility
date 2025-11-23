package com.cegedim.next.serviceeligibility.extractbenefmultios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtractBenefMultiOS {
  public static void main(final String[] args) {
    new SpringApplication(ExtractBenefMultiOS.class).run(args).close();
  }
}
