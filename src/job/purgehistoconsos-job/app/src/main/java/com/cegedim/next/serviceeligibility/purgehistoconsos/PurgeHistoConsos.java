package com.cegedim.next.serviceeligibility.purgehistoconsos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PurgeHistoConsos {
  public static void main(final String[] args) {
    new SpringApplication(PurgeHistoConsos.class).run(args).close();
  }
}
