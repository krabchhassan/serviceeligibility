package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydataname.BeneficiaryDataNameDto;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestBeneficiaryService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TransformUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TryToUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.kafkabenef.serviceprestation.ProducerBenef;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class BeneficiaryStep {

  private final TestBeneficiaryService testBeneficiaryService;
  private final TestCommonStoreService testCommonStoreService;
  private final ProducerBenef producerBenef;

  private static final String REQUEST_PATH = "beneficiaires/";

  private static final String REQUEST_PATH_BENEF_WORKER = "beneficiaires/beneficiary_worker/";

  private BeneficiaryDataNameDto beneficiaryDataNameDto;

  private List<BenefAIV5> benefAIV5List;

  private static final String PATH = "beneficiaires/%s.json";

  @Given("I create a beneficiaire from file {string}")
  public void createBenefFromFile(String fileName) {
    BenefAIV5 benef =
        FileUtils.readRequestFile(REQUEST_PATH + fileName + FileUtils.JSON, BenefAIV5.class);
    Assertions.assertNotNull(testBeneficiaryService.createBeneficiary(benef));
  }

  @Given("^I (try to )?search for the beneficiary data name for \"(.*)\", \"(.*)\"")
  public void searchBenef(Boolean tryTo, String personNumber, String contractNumber) {
    Map<String, String> uriVar =
        Map.of("personNumber", personNumber, "contractNumber", contractNumber);
    String url = TestCommonStoreService.CORE_API + "/v1/beneficiaryDataName";
    testCommonStoreService.setHttpClientErrorException(
        TryToUtils.tryTo(
            () -> {
              ResponseEntity<BeneficiaryDataNameDto> benefDto =
                  testCommonStoreService.getToGivenURL(
                      url, uriVar, TypeUtils.simpleParamType(BeneficiaryDataNameDto.class));
              Assertions.assertNotNull(benefDto);
              Assertions.assertNotNull(benefDto.getBody());
              beneficiaryDataNameDto = benefDto.getBody();
            },
            tryTo));
  }

  @Then("^the result with beneficiaryDataName has values$")
  public void theResultWithBeneficiaryDataNameHasValues(DataTable table) {
    Map<String, String> expected = table.asMap();
    Assertions.assertEquals(expected.get("lastName"), beneficiaryDataNameDto.getLastName());
    Assertions.assertEquals(expected.get("commonName"), beneficiaryDataNameDto.getCommonName());
    Assertions.assertEquals(expected.get("firstName"), beneficiaryDataNameDto.getFirstName());
    Assertions.assertEquals(expected.get("civility"), beneficiaryDataNameDto.getCivility());
  }

  @When("I get all benef from the database")
  public void getAllBenef() {
    benefAIV5List = testBeneficiaryService.getBeneficiaires();
  }

  @When("^I send a message from (?:source (.*) )?file (.*) to the kafka topic (.*)$")
  public void sendMessageFromFileToTopic(String source, String fileName, String topic)
      throws InterruptedException {
    // TODO #todotests
    BenefAIV5 benef =
        FileUtils.readRequestFile(
            REQUEST_PATH_BENEF_WORKER + fileName + FileUtils.JSON, BenefAIV5.class);
    producerBenef.send(benef, source);
  }

  @Then("The benef is identical to {string} content")
  public void theExpectedBenefIsIdenticalTo(String content) {
    checkBenef(content, 0);
  }

  private void checkBenef(String content, int indice, TransformUtils.Parser... parsers) {
    BenefAIV5 expected =
        FileUtils.readResultFile(String.format(PATH, content), BenefAIV5.class, parsers);

    BenefAIV5 received = benefAIV5List.get(indice);
    removeDynamicValues(received);
    assertThat(expected).usingRecursiveComparison().isEqualTo(received);
  }

  private void removeDynamicValues(BenefAIV5 benefAIV5) {
    benefAIV5.setIdToNull();
    benefAIV5.setTraceIdToNull();
    benefAIV5.setIdClientBOToNull();
    benefAIV5.getAudit().setDateEmissionToNull();
  }

  @Then("The benef {int} is identical to {string} content")
  public void theExpectedBenefIsIdenticalToOnIndice(int indice, String content) {
    checkBenef(content, indice);
  }
}
