package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestPrestijService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
public class PrestijStep {

  private final TestCommonStoreService testCommonStoreService;
  private final TestPrestijService testPrestijService;
  private static final String URL = TestCommonStoreService.PRESTIJ_WORKER + "/v2/prestijs";
  private static final String REQUEST_PATH = "servicePrestIJ/";
  private String traceId = null;
  private ResponseEntity<CreationResponse> response = null;
  private PrestIJ resultatPrestIJ = null;
  private String errorMessage = null;

  @Given("^I empty the prestij database$")
  public void emptyPrestijDatabase() {
    testPrestijService.emptyCollection();
  }

  @When("^I (try to )?send a prestIJ in version \"(.*)\" from file \"(.*)\"$")
  public void create(Boolean tryTo, String version, String fileName) {
    PrestIJ reponsePrestij =
        FileUtils.readRequestFile(REQUEST_PATH + fileName + FileUtils.JSON, PrestIJ.class);
    try {
      response = createPrestij(reponsePrestij);
      traceId = response.getBody().getTraceid();
    } catch (Throwable e) {
      if (e instanceof HttpClientErrorException) {
        testCommonStoreService.setHttpClientErrorException(
            Optional.of((HttpClientErrorException) e));
      }
    }
  }

  private ResponseEntity<CreationResponse> createPrestij(PrestIJ prestij) {
    ResponseEntity<CreationResponse> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            URL, prestij, TypeUtils.simpleParamType(CreationResponse.class));
    resultatPrestIJ = testPrestijService.savePrestij(prestij);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    return response;
  }

  @Then("^the trace is created and contains the status \"(.*)\"$")
  public void traceCreatedAndContainsStatus(TraceStatus status) {
    Trace trace = testPrestijService.getPrestijTrace(traceId);
    Assertions.assertEquals(status, trace.getStatus());
  }

  @Then("^the trace is created and contains the prestIJ id$")
  public void traceCreatedAndContainsPrestijId() {
    Trace trace = testPrestijService.getPrestijTrace(traceId);
    Assertions.assertNotNull(trace);
  }

  @Then("^I get the detail of the prestIJ$")
  public void prestijDetail() {
    Assertions.assertNotNull(resultatPrestIJ);
  }

  @Then("the expected prestIJ is identical to {string} content")
  public void prestijResponseExpected(String fileName) {
    PrestIJ expected =
        FileUtils.readResultFile(REQUEST_PATH + fileName + FileUtils.JSON, PrestIJ.class);
    assertThat(resultatPrestIJ)
        .usingRecursiveComparison()
        .ignoringFields(Constants.ID)
        .isEqualTo(expected);
  }

  @Then("the trace has the error message {string}")
  public void traceErrorMessage(String expectedErrorMessage) {
    Assertions.assertEquals(expectedErrorMessage, this.errorMessage);
  }

  // TODO @Then("^the error provider is \"(.*)\"$")
  public void errorProvider(String provider) {
    Assertions.assertEquals(provider, this.errorMessage);
  }
}
