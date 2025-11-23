package com.cegedim.next.serviceeligibility.mergealmerys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MergeAlmerysJob {
  public static void main(final String[] args) {
    new SpringApplication(MergeAlmerysJob.class).run().stop();
  }
}
