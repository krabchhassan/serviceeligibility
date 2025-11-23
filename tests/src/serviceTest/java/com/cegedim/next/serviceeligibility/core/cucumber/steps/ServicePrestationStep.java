package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService.CONSUMER_API;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CONTRACT_VERSION_V5;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CONTRAT_VERSION_5;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CONTRAT_VERSION_6;
import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestConsumerContractService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestServicePrestationService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.*;
import com.cegedim.next.serviceeligibility.core.dto.ServicePrestationsRdoDto;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CreationResponse;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
public class ServicePrestationStep {
  private final ObjectMapper objectMapper;
  private final TestConsumerContractService testConsumerContractService;
  private final TestServicePrestationService testServicePrestationService;
  private final TestCommonStoreService testCommonStoreService;

  public List<ContratAIV6> contratAIV6List;

  public int statusCode;
  private String validationStatus;
  private String firstErrorMessage;
  private String errorMessage;
  private List<String> errorMessages;

  private ServicePrestationsRdoDto servicePrestationsRdoDto;

  private static final String PATH = "serviceprestation/%s.json";

  @When("^I (try to )?send a test contract (v6 )?from file \"(.*)\"$")
  public void testContrat(Boolean tryTo, Boolean v6, String fileName) throws Exception {
    String version = CONTRAT_VERSION_5;
    Class<? extends ContratAICommun> clazz = ContratAIV5.class;
    if (v6 != null) {
      version = CONTRAT_VERSION_6;
      clazz = ContratAIV6.class;
    }
    testContrat(tryTo, fileName, version, clazz);
    savePrestationServices();
  }

  private <T extends ContratAICommun> void testContrat(
      Boolean tryTo, String fileName, String version, Class<T> clazz)
      throws JsonProcessingException {
    T contrat =
        FileUtils.readRequestFile(String.format(PATH, fileName), clazz, TransformUtils.DEFAULT);
    String contratString = objectMapper.writeValueAsString(contrat);
    ResponseEntity<CreationResponse> response =
        testConsumerContractService.createContractTest(
            contratString, version, contrat.getIdDeclarant());
    statusCode = response.getStatusCode().value();
    if (response.getBody() != null) {
      validationStatus = response.getBody().getStatus();
      if (!CollectionUtils.isEmpty(response.getBody().getErrorMessages())) {
        firstErrorMessage = response.getBody().getErrorMessages().getFirst();
        errorMessage = response.getBody().getProvider();
        errorMessages = response.getBody().getErrorMessages();
      }
    }
    if (tryTo == null) {
      Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
  }

  public void savePrestationServices() throws Exception {
    // on enregistre le contrat nouvellement ajouté, utilisé par V5 et V6 ?
    if (errorMessage == null) {
      Callable<List<ContratAIV6>> call = testServicePrestationService::getAllServicePrestation;
      Predicate<List<ContratAIV6>> test = response -> !CollectionUtils.isEmpty(response);
      contratAIV6List = RecursiveUtils.defaultRecursive(call, test);
    }
  }

  @When("I create a service prestation from a file {string}")
  public void createServicePrestation(String fileName) {
    ContratAIV6 servicePres =
        FileUtils.readRequestFile(String.format(PATH, fileName), ContratAIV6.class);
    testServicePrestationService.create(servicePres);
  }

  @Then("^the trace is created and contains the contract number \"(.*)\"")
  public void checkTrace(String numeroContrat) {
    Trace trace = testServicePrestationService.getContractTrace(numeroContrat);
    Assertions.assertEquals(numeroContrat, trace.getNumeroContrat());
  }

  @Given("^I (try to )?send a contract from file \"(.*)\" to version \"(.*)\"$")
  public void sendContrat(Boolean tryTo, String fileName, String version) throws Exception {
    sendContratToParse(tryTo, fileName, version);
    savePrestationServices();
  }

  @Given(
      "^I (try to )?send a contract from file \"(.*)\" to version \"(.*)\" and change placeholder(?:s)?$")
  public void sendContrat(Boolean tryTo, String fileName, String version, DataTable data) {
    Map<String, String> keyValues = TransformUtils.DEFAULT.parseMap(data.asMap());
    TransformUtils.Parser parser = TransformUtils.parser(keyValues);
    sendContratToParse(tryTo, fileName, version, parser);
  }

  private void sendContratToParse(
      Boolean tryTo, String fileName, String version, TransformUtils.Parser... parsers) {
    Optional<HttpClientErrorException> res =
        TryToUtils.tryTo(
            () -> {
              Class<? extends ContratAICommun> clazz =
                  CONTRACT_VERSION_V5.equals(version) ? ContratAIV5.class : ContratAIV6.class;
              ContratAICommun contrat =
                  FileUtils.readRequestFile(String.format(PATH, fileName), clazz, parsers);
              String idDeclarant = contrat.getIdDeclarant();
              String url =
                  CONSUMER_API
                      + "/"
                      + version.toLowerCase()
                      + "/declarants/"
                      + idDeclarant
                      + "/contracts";
              ResponseEntity<CreationResponse> response =
                  testCommonStoreService.postToGivenURLWithGivenBody(
                      url, contrat, TypeUtils.simpleParamType(CreationResponse.class));
              Assertions.assertNotNull(response);
              Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
              statusCode = response.getStatusCode().value();
              Assertions.assertNotNull(response.getBody());
              validationStatus = response.getBody().getStatus();
              if (!CollectionUtils.isEmpty(response.getBody().getErrorMessages())) {
                firstErrorMessage = response.getBody().getErrorMessages().getFirst();
                errorMessages = response.getBody().getErrorMessages();
              }
            },
            tryTo);

    testCommonStoreService.setHttpClientErrorException(res);
  }

  @Then("the response has an HTTP code {string}")
  public void checkStatusCode(String expected) {
    Assertions.assertEquals(Integer.valueOf(expected), statusCode);
  }

  @Then("the status is {string}")
  public void checkStatus(String expected) {
    Assertions.assertEquals(expected, validationStatus);
  }

  @Then("the first error message is {string}")
  public void checkErrorMessage(String expected) {
    Assertions.assertEquals(expected, firstErrorMessage);
  }

  @Then("^the error provider is \"(.*)\"$")
  public void errorProvider(String provider) {
    Assertions.assertEquals(provider, this.errorMessage);
  }

  @Then("the error messages has this values")
  public void checkErrorMessage(DataTable expected) {
    // TODO #todotests errorMessages
  }

  @Given("I create a service prestation from a file {string} with the dateSouscription {string}")
  public void createServicePrestationFromFile(String fileName, String dateSouscription) {
    ContratAIV6 contratAIV6 =
        FileUtils.readRequestFile(String.format(PATH, fileName), ContratAIV6.class);
    contratAIV6.setDateSouscription(dateSouscription);
    testServicePrestationService.create(contratAIV6);
  }

  @Given("I drop the collection for Service Prestation")
  public void dropServicePrestation() {
    testServicePrestationService.clearCollection();
  }

  @When("I delete a contract {string} for amc {string}")
  public void deleteParameter(String numeroContrat, String amc) {
    String url = CONSUMER_API + "/v3/declarants/" + amc + "/contracts/" + numeroContrat;
    testCommonStoreService.delete(url, CreationResponse.class);
  }

  @When("^the expected contract(?: with indice (\\d+))? is identical to \"(.*)\" content$")
  public void theExpectedContractIsIdenticalTo(Integer indice, String content) {
    ContratAIV6 expected =
        FileUtils.readResultFile(
            String.format(PATH, content), ContratAIV6.class, TransformUtils.DEFAULT);
    ContratAIV6 received =
        indice != null ? contratAIV6List.get(indice) : contratAIV6List.getFirst();
    removeDynamicValues(received);
    assertThat(expected).usingRecursiveComparison().isEqualTo(received);
  }

  private void removeDynamicValues(ContratAIV6 contratAIV6) {
    contratAIV6.setId(null);
    contratAIV6.setTraceId(null);
    for (Assure beneficiaire : contratAIV6.getAssures()) {
      beneficiaire.setDateCreation(null);
      beneficiaire.setDateModification(null);
    }
  }

  @Then(
      "I search service prestation for amc {string}, adherent {string}, nir {string}, birth date {string} and birth rank {string}")
  public void Isearchserviceprestation(
      String amc, String adherent, String nir, String birthDate, String birthRank) {
    Map<String, String> uriVar = new HashMap<>();
    uriVar.put("insuredId", amc);
    uriVar.put("subscriberId", adherent);
    uriVar.put("nir", nir);
    uriVar.put("birthDate", birthDate);
    uriVar.put("birthRank", birthRank);
    ResponseEntity<ServicePrestationsRdoDto> response =
        testCommonStoreService.getToGivenURL(
            TestCommonStoreService.CORE_API + "/v1/servicePrestationsRdo",
            uriVar,
            TypeUtils.simpleParamType(ServicePrestationsRdoDto.class));
    Assertions.assertNotNull(response);
    servicePrestationsRdoDto = response.getBody();
    statusCode = response.getStatusCodeValue();
  }

  @Then("the servicePrestationsRdo number {int} has values")
  public void testServicePrestationRdo(int number, DataTable table) {
    // TODO #todotests
  }

  // ici "the trace" = "ServicePrestationTrace"
  @Then("the trace is created and contains the contract id for contract number {string}")
  public void theTracecontains(String contractNumber) throws Exception {
    // 1. on récupére le contrat crée + son id et l'id de sa trace
    ContratAIV6 contrat = contratAIV6List.getFirst();
    // un seul contrat, le numéro de contrat est unique
    Assertions.assertEquals(contractNumber, contrat.getNumero());
    String traceId = contrat.getTraceId();
    String idContrat = contrat.getId();

    // 2. on récupére la trace associée + son id et l'id du contrat
    Callable<Trace> call = () -> testServicePrestationService.getContractTrace(traceId);
    Predicate<Trace> validation = trace -> trace.getContratAiId() != null;
    Trace trace = RecursiveUtils.defaultRecursive(call, validation);

    // une seule trace possible
    String aiIdContrat = trace.getContratAiId();
    String idTrace = trace.getId();

    // on compare l'id du contrat dans la collection prestationService et l'id du
    // contrat dans la collection prestationServiceTrace
    Assertions.assertEquals(idContrat, aiIdContrat);
    // on compare l'id de la trace dans la collection prestationServiceTrace et l'id de la trace
    // dans la collection prestationService
    Assertions.assertEquals(idTrace, traceId);
  }
}
