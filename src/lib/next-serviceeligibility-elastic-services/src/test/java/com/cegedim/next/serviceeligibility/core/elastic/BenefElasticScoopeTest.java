package com.cegedim.next.serviceeligibility.core.elastic;

import static org.mockito.Mockito.*;

import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.elast.ElasticAuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.elast.IndexBenef;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

class BenefElasticScoopeTest {

  private ElasticsearchOperations elasticMock;
  private ElasticAuthorizationScopeHandler authHandlerMock;
  private IndexBenef elasticUtilsMock;
  private BenefElasticService service;

  @BeforeEach
  public void setUp() {

    elasticMock = Mockito.mock(ElasticsearchOperations.class);
    authHandlerMock = Mockito.mock(ElasticAuthorizationScopeHandler.class);
    elasticUtilsMock = Mockito.mock(IndexBenef.class);
    RestHighLevelClient clientMock = Mockito.mock(RestHighLevelClient.class);

    service = new BenefElasticService(elasticUtilsMock, elasticMock, clientMock, authHandlerMock);
  }

  @Test
  void test_getBenefById_withAuthorizedBeneficiary() {
    // Arrange
    String validId = "12345";
    BenefAIV5 expectedBeneficiary = new BenefAIV5();
    expectedBeneficiary.setKey(validId);

    when(elasticUtilsMock.getIndexAlias()).thenReturn("beneficiary_index");
    when(elasticMock.get(validId, BenefAIV5.class, IndexCoordinates.of("beneficiary_index")))
        .thenReturn(expectedBeneficiary);

    when(authHandlerMock.isAuthorized(expectedBeneficiary)).thenReturn(true);

    BenefAIV5 actualBeneficiary = service.getBenefById(validId);

    Assertions.assertNotNull(actualBeneficiary, "The beneficiary should not be null.");
    Assertions.assertEquals(
        expectedBeneficiary.getKey(),
        actualBeneficiary.getKey(),
        "The retrieved beneficiary ID should match the expected one.");

    verify(authHandlerMock, times(1)).isAuthorized(expectedBeneficiary);
  }

  @Test
  void test_getBenefById_withUnauthorizedBeneficiary() {

    String validId = "12345";
    BenefAIV5 expectedBeneficiary = new BenefAIV5();
    expectedBeneficiary.setKey(validId);

    when(elasticUtilsMock.getIndexAlias()).thenReturn("beneficiary_index");
    when(elasticMock.get(validId, BenefAIV5.class, IndexCoordinates.of("beneficiary_index")))
        .thenReturn(expectedBeneficiary);

    when(authHandlerMock.isAuthorized(expectedBeneficiary)).thenReturn(false);

    Assertions.assertNull(service.getBenefById(validId));
    verify(authHandlerMock, times(1)).isAuthorized(expectedBeneficiary);
  }
}
