package com.cegedim.next.serviceeligibility.almerysacl.batch;

import com.cegedim.next.serviceeligibility.almerysacl.batch.services.Processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AlmerysAclJob {

  public static void main(final String[] args) {
    ConfigurableApplicationContext context = new SpringApplication(AlmerysAclJob.class).run(args);
    System.exit(context.getBean(Processor.class).readFolders());
  }
}
