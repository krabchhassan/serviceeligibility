package com.cegedim.next.serviceeligibility.purgehistoconsos.configuration;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.config.s3.S3Config;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDao;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceDaoImpl;
import com.cegedim.next.serviceeligibility.core.elast.contract.ElasticHistorisationContractService;
import com.cegedim.next.serviceeligibility.core.elast.contract.HistoriqueContratRepository;
import com.cegedim.next.serviceeligibility.core.elast.contract.IndexHistoContrat;
import com.cegedim.next.serviceeligibility.core.services.bdd.TraceService;
import com.cegedim.next.serviceeligibility.core.services.s3.S3Service;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = HistoriqueContratRepository.class)
@Import({S3Config.class, OmuHelperConfiguration.class})
@RequiredArgsConstructor
public class PurgeHistoConsosConfig {

  private final BeyondPropertiesService beyondPropertiesService;

  @Bean
  public CrexProducer crexProducer() {
    return new CrexProducer();
  }

  @Bean
  public S3Service s3Service(S3StorageService bddS3StorageService) {
    return new S3Service(bddS3StorageService, new ObjectMapper(), beyondPropertiesService);
  }

  @Bean
  public TraceService traceService(MongoTemplate mongoTemplate) {
    return new TraceService(traceDao(mongoTemplate));
  }

  @Bean
  public TraceDao traceDao(MongoTemplate mongoTemplate) {
    return new TraceDaoImpl(mongoTemplate);
  }

  @Bean
  public ElasticHistorisationContractService elasticHistorisationContractService(
      HistoriqueContratRepository historiqueContratRepository,
      ObjectMapper objectMapper,
      RestHighLevelClient opensearchClient) {
    return new ElasticHistorisationContractService(
        historiqueContratRepository, indexHistoContrat(), objectMapper, opensearchClient);
  }

  @Bean
  public IndexHistoContrat indexHistoContrat() {
    return new IndexHistoContrat(beyondPropertiesService);
  }
}
