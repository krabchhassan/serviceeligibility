package com.cegedim.next.serviceeligibility.exporttriggerinfos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExportTriggerInfos {
  public static void main(final String[] args) {
    new SpringApplication(ExportTriggerInfos.class).run(args).close();
  }
}
