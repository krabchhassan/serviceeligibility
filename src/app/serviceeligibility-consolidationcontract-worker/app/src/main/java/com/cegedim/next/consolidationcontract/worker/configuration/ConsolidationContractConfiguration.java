package com.cegedim.next.consolidationcontract.worker.configuration;

import com.cegedim.beyond.messaging.api.events.producer.BusinessEventProducer;
import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.*;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = {HistoriqueContratRepository.class})
@RequiredArgsConstructor
public class ConsolidationContractConfiguration {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public DomaineTPService domaineTPService() {
    return new DomaineTPService(periodeDroitTPService());
  }

  @Bean
  public PeriodeDroitTPService periodeDroitTPService() {
    return new PeriodeDroitTPService(
        periodeDroitTPStep1(), periodeDroitTPStep2(), periodeDroitTPStep3(), periodeDroitTPStep4());
  }

  @Bean
  public PeriodeDroitTPStep1 periodeDroitTPStep1() {
    return new PeriodeDroitTPStep1();
  }

  @Bean
  public PeriodeDroitTPStep2 periodeDroitTPStep2() {
    return new PeriodeDroitTPStep2();
  }

  @Bean
  public PeriodeDroitTPStep3 periodeDroitTPStep3() {
    return new PeriodeDroitTPStep3();
  }

  @Bean
  public PeriodeDroitTPStep4 periodeDroitTPStep4() {
    return new PeriodeDroitTPStep4();
  }

  @Bean
  public ContractTPService contractTPService(MongoTemplate mongoTemplate) {
    return new ContractTPService(contractDao(mongoTemplate), domaineTPService());
  }

  @Bean
  public ContractDao contractDao(MongoTemplate mongoTemplate) {
    return new ContractDaoImpl(mongoTemplate);
  }

  @Bean
  public DeclarationDao declarationDao(MongoTemplate mongoTemplate) {
    return new DeclarationDaoImpl(mongoTemplate, beyondPropertiesService);
  }

  @Bean
  ContractService contractService(MongoTemplate mongoTemplate) {
    return new ContractService(contractDao(mongoTemplate));
  }

  @Bean
  DeclarationService declarationService(MongoTemplate mongoTemplate) {
    return new DeclarationService(declarationDao(mongoTemplate));
  }

  @Bean
  ElasticHistorisationContractService elasticHistorisationContractService(
      HistoriqueContratRepository historiqueContratRepository,
      ObjectMapper objectMapper,
      RestHighLevelClient opensearchClient) {
    return new ElasticHistorisationContractService(
        historiqueContratRepository, indexHistoContrat(), objectMapper, opensearchClient);
  }

  @Bean
  IndexHistoContrat indexHistoContrat() {
    return new IndexHistoContrat(beyondPropertiesService);
  }

  @Bean
  EventService eventService(BusinessEventProducer businessEventProducer) {
    return new EventService(businessEventProducer, beyondPropertiesService);
  }
}
