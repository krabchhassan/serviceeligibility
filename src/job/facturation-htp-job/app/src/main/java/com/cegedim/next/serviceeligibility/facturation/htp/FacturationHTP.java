package com.cegedim.next.serviceeligibility.facturation.htp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FacturationHTP {
  public static void main(final String[] args) {
    new SpringApplication(FacturationHTP.class).run(args).close();
  }
}
