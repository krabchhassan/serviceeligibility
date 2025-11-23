package com.cegedim.next.serviceeligibility.core.config;

import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.elast.ElasticAuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.elast.IndexBenef;
import com.cegedim.next.serviceeligibility.core.utils.ElasticRestConnector;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Profile("test")
@Configuration
public class TestConfiguration {
  @MockBean ElasticsearchOperations elastic;

  @MockBean ElasticRestConnector elasticRestConnector;

  @MockBean RestHighLevelClient client;

  @MockBean IndexBenef indexBenef;

  @MockBean ElasticAuthorizationScopeHandler elasticAuthorizationScopeHandler;

  @Bean
  public BenefElasticService benefElasticService() {
    return new BenefElasticService(indexBenef, elastic, client, elasticAuthorizationScopeHandler);
  }
}
