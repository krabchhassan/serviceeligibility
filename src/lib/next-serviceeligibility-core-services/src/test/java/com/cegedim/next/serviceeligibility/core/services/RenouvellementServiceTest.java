package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class RenouvellementServiceTest {

  @Autowired private RenouvellementService renouvellementService;

  @SpyBean private ParametrageCarteTPService parametrageCarteTPService;

  @Test
  void testAddParameterId() {

    TriggerDataForTesting.setParametrageCarteTPServiceMock(parametrageCarteTPService);
    ContratAIV6 contrat = TriggerDataForTesting.getContratAIV6();
    renouvellementService.addParametrageID(contrat);
    for (Assure assure : contrat.getAssures()) {
      Assertions.assertEquals("11111111", assure.getParametrageCarteId());
    }
  }
}
