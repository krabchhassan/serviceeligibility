package com.cegedim.next.serviceeligibility.core.elast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AdvancedElasticControllerTest {

  @Mock private BenefElasticService benefElasticService;

  @InjectMocks private AdvancedElasticController advancedElasticController;

  @Test
  void testAdvancedSearch() throws IOException {
    BenefAdvancedSearchRequest request = new BenefAdvancedSearchRequest();

    BenefElasticPageResult mockResult = new BenefElasticPageResult();

    when(benefElasticService.search(request)).thenReturn(mockResult);

    ResponseEntity<BenefElasticPageResult> response = advancedElasticController.search(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockResult, response.getBody());
  }
}
