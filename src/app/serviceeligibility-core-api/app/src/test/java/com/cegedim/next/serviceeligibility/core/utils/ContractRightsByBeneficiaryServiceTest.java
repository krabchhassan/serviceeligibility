package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.dto.ContractRightsByBeneficiaryRequestDto;
import com.cegedim.next.serviceeligibility.core.features.utils.ContractRightsByBeneficiaryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class ContractRightsByBeneficiaryServiceTest {
  @Autowired ContractRightsByBeneficiaryService contractRightsByBeneficiaryService;

  @Test
  void testHTPResponse() {
    ContractRightsByBeneficiaryRequestDto crbRequest = new ContractRightsByBeneficiaryRequestDto();
    crbRequest.setBeneficiaryId("1-1");
    crbRequest.setContext(ContextConstants.HTP);
    List<ContractRightsByBeneficiaryRequestDto.ContractSubscriber> contractList = new ArrayList<>();
    ContractRightsByBeneficiaryRequestDto.ContractSubscriber contractSubscriber =
        new ContractRightsByBeneficiaryRequestDto.ContractSubscriber("S", "N");
    contractList.add(contractSubscriber);
    crbRequest.setContractList(contractList);

    var result =
        contractRightsByBeneficiaryService.getContractRightsByBeneficiaryResponse(crbRequest);
    Assertions.assertNotNull(result);
  }

  @Test
  void testTPOnlineResponse() {
    ContractRightsByBeneficiaryRequestDto crbRequest = new ContractRightsByBeneficiaryRequestDto();
    crbRequest.setBeneficiaryId("1-1");
    crbRequest.setContext(ContextConstants.TP_ONLINE);
    List<ContractRightsByBeneficiaryRequestDto.ContractSubscriber> contractList = new ArrayList<>();
    ContractRightsByBeneficiaryRequestDto.ContractSubscriber contractSubscriber =
        new ContractRightsByBeneficiaryRequestDto.ContractSubscriber("S", "N");
    contractList.add(contractSubscriber);
    crbRequest.setContractList(contractList);

    var result =
        contractRightsByBeneficiaryService.getContractRightsByBeneficiaryResponse(crbRequest);
    Assertions.assertNotNull(result);
  }

  @Test
  void testTPOfflineResponse() {
    ContractRightsByBeneficiaryRequestDto crbRequest = new ContractRightsByBeneficiaryRequestDto();
    crbRequest.setBeneficiaryId("1-1");
    crbRequest.setContext(ContextConstants.TP_OFFLINE);
    List<ContractRightsByBeneficiaryRequestDto.ContractSubscriber> contractList = new ArrayList<>();
    ContractRightsByBeneficiaryRequestDto.ContractSubscriber contractSubscriber =
        new ContractRightsByBeneficiaryRequestDto.ContractSubscriber("S", "N");
    contractList.add(contractSubscriber);
    crbRequest.setContractList(contractList);

    var result =
        contractRightsByBeneficiaryService.getContractRightsByBeneficiaryResponse(crbRequest);
    Assertions.assertNotNull(result);
  }
}
