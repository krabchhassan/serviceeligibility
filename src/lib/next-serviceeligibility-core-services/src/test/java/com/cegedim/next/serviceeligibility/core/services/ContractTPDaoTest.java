package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractTPDaoTest {
  @Autowired MongoTemplate mongoTemplate;

  @Autowired private ContractDao contractDao;

  @Test
  void getContractTest() {
    ContractTP expectedContractTP = getContract();
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(ContractTP.class), Mockito.anyString()))
        .thenReturn(expectedContractTP);
    ContractTP contractTPResult = contractDao.getContract("amc", "numeroContrat", "numeroAdhrant");
    Assertions.assertEquals(contractTPResult, expectedContractTP);
  }

  @Test
  void saveContractTest() {
    Mockito.when(mongoTemplate.save(Mockito.any(ContractTP.class)))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
    contractDao.saveContract(getContract());
  }

  private ContractTP getContract() {
    ContractTP contractTP = new ContractTP();

    contractTP.set_id("UUID");

    return contractTP;
  }
}
