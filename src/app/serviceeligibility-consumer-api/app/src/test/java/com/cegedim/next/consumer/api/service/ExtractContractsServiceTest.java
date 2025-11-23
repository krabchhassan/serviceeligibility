package com.cegedim.next.consumer.api.service;

import com.cegedim.next.consumer.api.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.services.ExtractContractsService;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ExtractContractsServiceTest {
  @Autowired ExtractContractsService extractContractsService;

  @Test
  void shouldGetBeneficiairesTest() {
    Assure assure = new Assure();
    assure.setIdentite(DeclarantServiceTest.getIdentiteContrat());
    Set<BeneficiaireId> beneficiaireIdSet = extractContractsService.getBeneficiaires(assure);
    Assertions.assertNotNull(beneficiaireIdSet);
    Assertions.assertNotEquals(0, beneficiaireIdSet.size());
    Assertions.assertFalse(beneficiaireIdSet.isEmpty());
    for (BeneficiaireId beneficiaireId : beneficiaireIdSet) {
      Assertions.assertEquals("2160631412621", beneficiaireId.getNir());
      Assertions.assertEquals("20161101", beneficiaireId.getDateNaissance());
      Assertions.assertEquals("1", beneficiaireId.getRangNaissance());
    }
  }
}
