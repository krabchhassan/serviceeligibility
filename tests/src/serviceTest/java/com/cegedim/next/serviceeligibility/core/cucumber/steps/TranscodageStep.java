package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscoParamDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscodageListDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.TranscoParametrageServiceImpl;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
public class TranscodageStep {
  private final TranscoParametrageServiceImpl transcoParametrageService;

  private List<TranscoParamDto> transcoListPrevious = null;
  private List<TranscoParamDto> transcoListCurrent = null;
  private TranscoParamDto previousInserted = null;
  private TranscoParamDto searchedTransco = null;
  private String previousRemovedCode = null;

  private TranscodageListDto transcodageList = null;
  String serviceCode = null;
  String transcoCode = null;

  private final TestCommonStoreService testCommonStoreService;

  @Given("^I search for all transcoding parameters$")
  public void getAllTranscoding() {
    transcoListPrevious = transcoListCurrent;
    transcoListCurrent = transcoParametrageService.findAllTranscoParametrage();
  }

  @Then("^The list of transcoding parameters contains one more item$")
  public void compareNumberTranscoding() {
    Assertions.assertTrue(
        transcoListCurrent != null
            && transcoListPrevious != null
            && transcoListCurrent.size() == transcoListPrevious.size() + 1);
  }

  @When(
      "^I insert a new transcoding parameter with code \"(.*)\" and name \"(.*)\" and one column with name \"(.*)\"$")
  public void insertNewTranscoding(String code, String name, String columnName) {
    TranscoParamDto transcoParamDto = new TranscoParamDto();
    transcoParamDto.setCodeObjetTransco(code);
    transcoParamDto.setNomObjetTransco(name);
    transcoParamDto.setColNames(List.of(columnName));
    previousInserted = transcoParamDto;
    transcoParametrageService.saveOrUpdate(transcoParamDto);
  }

  @Then("^The transcoding parameter list contains the previously inserted transcoding parameter$")
  public void checkPreviousInsertedTranscoding() {
    Assertions.assertTrue(
        transcoListCurrent != null
            && transcoListCurrent.stream()
                .anyMatch(
                    transco ->
                        transco.getCodeObjetTransco().equals(previousInserted.getCodeObjetTransco())
                            && transco
                                .getNomObjetTransco()
                                .equals(previousInserted.getNomObjetTransco())));
  }

  @When("^I delete the transcoding parameter with code \"(.*)\"$")
  public void deleteTranscoding(String code) {
    previousRemovedCode = code;
    transcoParametrageService.delete(previousRemovedCode);
  }

  @Then("^The transcoding parameter is not present on the list$")
  public void checkPreviousremovedIsMissing() {
    Assertions.assertTrue(
        previousRemovedCode != null
            && transcoParametrageService
                    .findTranscoParametrage(previousRemovedCode)
                    .getNomObjetTransco()
                == null);
  }

  @When("^I search for transcoding parameter with code \"(.*)\"$")
  public void searchTranscoding(String code) {
    searchedTransco = transcoParametrageService.findTranscoParametrage(code);
  }

  @Then("^The returned transcoding parameter is correct$")
  public void checkTranscoding() {
    Assertions.assertNotNull(searchedTransco);
  }

  @When("^I update a parameter with code \"(.*)\" and change the name to \"(.*)\"$")
  public void updateTranscoding(String code, String name) {
    var transco = transcoParametrageService.findTranscoParametrage(code);
    transco.setNomObjetTransco(name);
    previousInserted = transco;
    transcoParametrageService.saveOrUpdate(transco);
  }

  @Then("^The modified parameter's name is \"(.*)\"$")
  public void checkModifiedTranscoding(String name) {
    var transco =
        transcoParametrageService.findTranscoParametrage(previousInserted.getCodeObjetTransco());
    Assertions.assertEquals(name, transco.getNomObjetTransco());
  }

  @When("^I search transco per services with service code (.*) and transco code (.*)$")
  public void searchTranscodingPerServices(String serviceCode, String transcoCode) {
    this.serviceCode = serviceCode;
    this.transcoCode = transcoCode;
    ResponseEntity<TranscodageListDto> response =
        testCommonStoreService.getToGivenURL(
            TestCommonStoreService.CORE_API
                + "/v1/transcodage/services/"
                + serviceCode
                + "/"
                + transcoCode,
            null,
            TypeUtils.simpleParamType(TranscodageListDto.class));
    Assertions.assertNotNull(response);
    transcodageList = response.getBody();
  }

  @Then("The transco per service is correct")
  public void checkTranscodingPerService() {
    Assertions.assertEquals(transcodageList.getCodeService(), this.serviceCode);
    Assertions.assertEquals(transcodageList.getCodeObjetTransco(), this.transcoCode);
    Assertions.assertNotNull(transcodageList.getTranscoList());
  }

  @Then("^The transco per service contains (.*) keys$")
  public void checkTranscodingPerServiceContains(int keys) {
    Assertions.assertEquals(transcodageList.getTranscoList().size(), keys);
  }

  @Then("^I update the transco par service keeping (.*) records$")
  public void updateTranscodingPerServiceKeeping(int records) {
    TranscodageListDto transcoToUpdate = transcodageList;
    transcoToUpdate.setTranscoList(transcodageList.getTranscoList().subList(0, records));
    String endpoint =
        "/v1/transcodage/services/"
            + transcodageList.getCodeService()
            + "/"
            + transcodageList.getCodeObjetTransco();

    ResponseEntity<Void> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            TestCommonStoreService.CORE_API + endpoint,
            transcoToUpdate,
            TypeUtils.simpleParamType(Void.class));
    Assertions.assertNotNull(response);
  }
}
