package com.cegedim.next.consumer.worker;

import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** This class defines the main class of next undue kernel API. */
@SpringBootApplication
@EnableTransactionManagement
@EnableElasticsearchRepositories(basePackageClasses = {HistoriqueContratRepository.class})
public class ConsumerWorker {

  /**
   * Allows to launch the service provider core API.
   *
   * @param args array of arguments.
   */
  public static void main(final String[] args) {
    new SpringApplication(ConsumerWorker.class).run();
  }
}
