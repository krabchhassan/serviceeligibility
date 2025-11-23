package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
public class HttpStep {

  private final TestCommonStoreService testCommonStoreService;

  @Given("^an error \"(.*)\" is returned(?: with message \"(.*)\")?$")
  public void anErrorIsReturned(int error, String message) {
    checkErrorStatusMessage(error, message);
  }

  @Then("^With this request I have a contract resiliated exception")
  public void withThisRequestIhaveAcontractResiliatedException() {
    checkErrorStatusMessage(404, "Contrat résilié");
  }

  @Then("^With this request I have a contract not found exception")
  public void withThisRequestIhaveAcontractNotFoundException() {
    checkErrorStatusMessage(404, "Contrat non trouvé");
  }

  @Then("With this request I have a beneficiary not found without subscriber exception")
  public void subscriberException() {
    checkErrorStatusMessage(
        400, "Veuillez faire la recherche de droits en renseignant le n° d’adhérent");
  }

  @Then("With this request I have a beneficiary not found exception")
  public void notFoundException() {
    checkErrorStatusMessage(404, "Bénéficiaire non trouvé");
  }

  @Then("With this request I have a contract without any rights open exception")
  public void anyRightsException() {
    checkErrorStatusMessage(400, "Droits non ouverts");
  }

  @Then("With this request I have a contract without any rights open for this domain exception")
  public void noRightsException() {
    checkErrorStatusMessage(400, "Type de dépense non ouvert");
  }

  @Then("I have a beneficiary without subscriber not found exception")
  public void beneficiaryNotFoundException() {
    checkErrorStatusMessage(
        400, "Veuillez faire la recherche de droits en renseignant le n° d’adhérent");
  }

  @Then("I have an unknown beneficiary not found exception")
  public void unknownbeneficiary() {
    checkErrorStatusMessage(400, "Béneficiaire inconnu");
  }

  @Then("I have an unknown beneficiary with id not found exception")
  public void unknownbeneficiaryId() {
    checkErrorStatusMessage(404, "Veuillez renseigner les critères de recherche du bénéficiaire");
  }

  public void checkErrorStatusMessage(int error, String message) {
    Optional<HttpClientErrorException> httpClientErrorException =
        testCommonStoreService.getHttpClientErrorException();
    Assertions.assertTrue(httpClientErrorException.isPresent());
    Assertions.assertEquals(
        HttpStatusCode.valueOf(error), httpClientErrorException.get().getStatusCode());
    if (message != null) {
      Assertions.assertNotNull(httpClientErrorException.get().getMessage());
      String messageUnescaped =
          StringEscapeUtils.unescapeJava(httpClientErrorException.get().getMessage());
      Assertions.assertTrue(messageUnescaped.contains(message));
    }
  }
}
