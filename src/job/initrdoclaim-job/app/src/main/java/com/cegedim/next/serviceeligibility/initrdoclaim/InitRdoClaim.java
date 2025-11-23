package com.cegedim.next.serviceeligibility.initrdoclaim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InitRdoClaim {
  public static void main(final String[] args) {
    new SpringApplication(InitRdoClaim.class).run(args).close();
  }
}
