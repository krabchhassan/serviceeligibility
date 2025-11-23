package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService.CORE_API;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestParametreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TryToUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class ParametresStep {

  private final TestCommonStoreService testCommonStoreService;

  private final TestParametreService testParametreService;

  private static final String URL = CORE_API + "/v2/parameters";

  private ParametresDto result = null;
  private List<? extends ParametresDto> parameters;

  @When("^I (try to )?create a parameter for type \"(.*)\" in version \"V2\" with parameters$")
  public void createParameter(Boolean tryTo, String type, DataTable parameters) {
    String param = generateParamJson(parameters);
    TryToUtils.tryTo(
        () -> {
          try {
            testParametreService.insertV2(type, param);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        },
        tryTo);
  }

  @When("^I (try to )?delete the parameter for type \"(.*)\" with code \"(.*)\" in version \"V2\"$")
  public void deleteParameter(Boolean tryTo, String type, String code) {
    String url = URL + "/" + type + "/" + code;
    TryToUtils.tryTo(() -> testCommonStoreService.delete(url, Object.class), tryTo);
  }

  @When("^I (try to )?update a parameter for type \"(.*)\" in version \"V2\" with parameters$")
  public void updateParameter(Boolean tryTo, String type, DataTable parameters) {
    String param = generateParamJson(parameters);
    TryToUtils.tryTo(
        () ->
            testCommonStoreService.putToGivenURLWithGivenBody(
                URL + "/" + type, param, TypeUtils.simpleParamType(ParametresDto.class)),
        tryTo);
  }

  @When("^I (try to )?get the parameter with code \"(.*)\", for type \"(.*)\" in version \"V2\"$")
  public void getParameter(Boolean tryTo, String code, String type) {
    String url = URL + "/" + type + "/" + code;
    TryToUtils.tryTo(() -> result = testParametreService.get(code, type), tryTo);
  }

  @Then("^the parameter has values$")
  public void verifyParameters(DataTable table) {
    // TODO #todotests
  }

  @When("^I get the prestation's list for the domain \"(.*)\"$")
  public void getPrestation(String domain) {
    Map<String, String> urlVar = Map.of("domain", domain);
    String url = URL + "/prestations";
    ResponseEntity<List<ParametresDto>> params =
        testCommonStoreService.getToGivenURL(
            url, urlVar, TypeUtils.listParamType(ParametresDto.class));
    parameters = params.getBody();
  }

  @When("^the list contains \"(.*)\" entries$")
  public void nbParams(String nb) {
    int entrie = Integer.parseInt(nb);
    Assertions.assertEquals(entrie, parameters.size());
  }

  @When("^the parameter doesn't exists$")
  public void parameterDoesNotExist() {
    Assertions.assertTrue(CollectionUtils.isEmpty(parameters));
  }

  @When("^I delete all the parameters$")
  public void deleteAllParameters() {
    testCommonStoreService.delete(URL, ParametresDto.class);
  }

  private String generateParamJson(DataTable parameters) {
    StringBuilder parametreIn = new StringBuilder();
    Map<String, String> body = parameters.asMap();
    for (Map.Entry<String, String> entry : body.entrySet()) {
      String key = entry.getKey();
      switch (key) {
        case "code":
          parametreIn
              .append("{\"")
              .append(key)
              .append("\": \"")
              .append(entry.getValue())
              .append("\"");
          break;
        case "libelle":
          parametreIn
              .append(", \"")
              .append(key)
              .append("\": \"")
              .append(entry.getValue())
              .append("\"}");
          break;
        case "codeDomaine1":
          parametreIn
              .append(", \"domaines\" : [ {\"codeDomaine\": \"")
              .append(entry.getValue())
              .append("\"}");
          break;
        case "codeDomaine2":
          parametreIn.append(", {\"codeDomaine\": \"").append(entry.getValue()).append("\"}");
          break;
        case "NoMoreDomaine":
          parametreIn.append("]");
          break;
        default:
          parametreIn
              .append(", \"")
              .append(key)
              .append("\" : \"")
              .append(entry.getValue())
              .append("\"");
          break;
      }
    }
    return parametreIn.toString();
  }
}
