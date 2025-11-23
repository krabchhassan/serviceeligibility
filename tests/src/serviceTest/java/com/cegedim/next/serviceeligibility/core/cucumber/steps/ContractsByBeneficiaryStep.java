package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractByBeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim.ContractsByBeneficiaryDto;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.dto.ContractRightsByBeneficiaryRequestDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.GenericRightDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
public class ContractsByBeneficiaryStep {
  private final TestCommonStoreService testCommonStoreService;

  private static final String CONTRACTSBYBENEF =
      TestCommonStoreService.CORE_API + "/v1/contractsByBeneficiary";

  private static final String CONTRACTRIGHTSSBYBENEF =
      TestCommonStoreService.CORE_API + "/v1/contractRightsByBeneficiary";

  private UniqueAccessPointResponse uniqueAccessPointResponse;

  private ContractsByBeneficiaryDto contractsByBeneficiaryDto;

  @Given("I get contracts for beneficiaryID {string} and context {string}")
  public void getContracts(String beneficiaryID, String context) {
    Map<String, String> params = new HashMap<>();
    params.put("beneficiaryId", beneficiaryID);
    params.put("context", context);
    try {

      testCommonStoreService.setHttpClientErrorException(Optional.empty());
      ResponseEntity<ContractsByBeneficiaryDto> response =
          testCommonStoreService.getToGivenURL(
              CONTRACTSBYBENEF, params, TypeUtils.simpleParamType(ContractsByBeneficiaryDto.class));
      Assertions.assertNotNull(response);
      Assertions.assertNotNull(response.getBody());
      contractsByBeneficiaryDto = response.getBody();
    } catch (Throwable e) {
      contractsByBeneficiaryDto = null;
      if (e instanceof HttpClientErrorException) {
        testCommonStoreService.setHttpClientErrorException(
            Optional.of((HttpClientErrorException) e));
      }
    }
  }

  @Then("^the result in contractright has (\\d+) contracts$")
  public void theResultcontractrightHasContracts(int count) {
    Assertions.assertEquals(count, uniqueAccessPointResponse.getContracts().size());
  }

  @Then("^the result has (\\d+) contracts$")
  public void theResultHasContracts(int count) {
    Assertions.assertEquals(count, contractsByBeneficiaryDto.contracts().size());
  }

  @Given(
      "I get contracts for beneficiaryID {string}, context {string}, nir {string} and contract list")
  public void getContracts(String beneficiaryID, String context, String nir, DataTable table) {
    ContractRightsByBeneficiaryRequestDto requestDto = new ContractRightsByBeneficiaryRequestDto();
    requestDto.setBeneficiaryId(beneficiaryID);
    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
    List<ContractRightsByBeneficiaryRequestDto.ContractSubscriber> contractList = new ArrayList<>();
    for (Map<String, String> columns : rows) {
      contractList.add(
          new ContractRightsByBeneficiaryRequestDto.ContractSubscriber(
              columns.get("subscriberId"), columns.get("contractNumber")));
    }
    requestDto.setContractList(contractList);
    requestDto.setContext(context);
    requestDto.setNir(nir);

    ResponseEntity<UniqueAccessPointResponse> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            CONTRACTRIGHTSSBYBENEF,
            requestDto,
            TypeUtils.simpleParamType(UniqueAccessPointResponse.class));
    assert response != null;
    uniqueAccessPointResponse = response.getBody();
  }

  @Then("^the result(?: with indice (\\d+))? has values$")
  public void theResultWithIndiceHasValues(Integer indice, DataTable table) {
    ContractByBeneficiaryDto received =
        indice != null
            ? contractsByBeneficiaryDto.contracts().get(indice)
            : contractsByBeneficiaryDto.contracts().get(0);
    Map<String, String> expected = table.asMap();
    Assertions.assertEquals(expected.get("insurerId"), received.insurerId());
    Assertions.assertEquals(expected.get("contractNumber"), received.contractNumber());
    Assertions.assertEquals(expected.get("subscriberId"), received.subscriberId());
    if (expected.get("period.start") != null) {
      Assertions.assertEquals(expected.get("period.start"), received.period().getStart());
    }
    if (expected.get("period.end") != null) {
      if ("null".equals(expected.get("period.end"))) {
        Assertions.assertNull(received.period().getEnd());
      } else {
        Assertions.assertEquals(expected.get("period.end"), received.period().getEnd());
      }
    }
    if (expected.get("nir.code") != null) {
      Assertions.assertEquals(expected.get("nir.code"), received.nir().getCode());
    }
    if (expected.get("nir.key") != null) {
      Assertions.assertEquals(expected.get("nir.key"), received.nir().getKey());
    }
    if (expected.get("subscriber.lastname") != null) {
      if ("null".equals(expected.get("subscriber.lastname"))) {
        Assertions.assertNull(received.subscriber().getLastname());
      } else {
        Assertions.assertEquals(
            expected.get("subscriber.lastname"), received.subscriber().getLastname());
      }
    }
    if (expected.get("subscriber.firstname") != null) {
      if ("null".equals(expected.get("subscriber.firstname"))) {
        Assertions.assertNull(received.subscriber().getFirstname());
      } else {
        Assertions.assertEquals(
            expected.get("subscriber.firstname"), received.subscriber().getFirstname());
      }
    }
  }

  @Then("^the result in contractright(?: with indice (\\d+))? has values$")
  public void theResultcontractrightWithIndiceHasValues(Integer indice, DataTable table) {
    GenericRightDto received =
        indice != null
            ? uniqueAccessPointResponse.getContracts().get(indice)
            : uniqueAccessPointResponse.getContracts().getFirst();
    Map<String, String> expected = table.asMap();
    Assertions.assertEquals(expected.get("insurerId"), received.getInsurerId());
    Assertions.assertEquals(expected.get("number"), received.getNumber());
    Assertions.assertEquals(expected.get("subscriberId"), received.getSubscriberId());
  }
}
