package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractTPServiceTest {
  @Autowired MongoTemplate mongoTemplate;

  @Autowired ContractService contractService;

  @Test
  void deleteBenefFromContractNotFound() {
    Mockito.when(
            mongoTemplate.aggregate(
                Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.any()))
        .thenReturn(new AggregationResults<>(List.of(), new Document()));

    Assertions.assertEquals(-1, contractService.deleteBenefFromContract("any", "any", "any", "10"));
  }

  @Test
  void deleteBenefFromContractNoBenefsLeft() {
    Mockito.when(
            mongoTemplate.findOne(Mockito.any(Query.class), Mockito.any(), Mockito.anyString()))
        .thenReturn(getContractWithBenefs(1));

    Assertions.assertEquals(0, contractService.deleteBenefFromContract("any", "any", "any", "0"));
  }

  @Test
  void deleteBenefFromContractBenefsLeft() {
    Mockito.when(
            mongoTemplate.findOne(Mockito.any(Query.class), Mockito.any(), Mockito.anyString()))
        .thenReturn(getContractWithBenefs(3));

    Assertions.assertEquals(1, contractService.deleteBenefFromContract("any", "any", "any", "2"));
  }

  private ContractTP getContract() {
    ContractTP contractTP = new ContractTP();

    contractTP.set_id("UUID");

    return contractTP;
  }

  private ContractTP getContractWithBenefs(int nbBenefs) {
    ContractTP contractTP = getContract();

    List<BeneficiaireContractTP> benefs = new ArrayList<>();
    for (int i = 0; i < nbBenefs; i++) {
      benefs.add(getBenef(Integer.toString(i)));
    }
    contractTP.setBeneficiaires(benefs);

    return contractTP;
  }

  private BeneficiaireContractTP getBenef(String numeroPersonne) {
    BeneficiaireContractTP benef = new BeneficiaireContractTP();

    benef.setNumeroPersonne(numeroPersonne);

    return benef;
  }
}
