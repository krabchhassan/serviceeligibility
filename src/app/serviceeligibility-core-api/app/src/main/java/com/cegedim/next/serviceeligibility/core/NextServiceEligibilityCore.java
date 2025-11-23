package com.cegedim.next.serviceeligibility.core;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NextServiceEligibilityCore {

  public static void main(final String[] args) {
    new SpringApplication(NextServiceEligibilityCore.class).run();
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
}
