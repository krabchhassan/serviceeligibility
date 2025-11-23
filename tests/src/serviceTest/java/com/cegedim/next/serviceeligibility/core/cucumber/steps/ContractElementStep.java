package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTResult;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.LotService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TransformUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class ContractElementStep {

  @Value("${CLIENT_USERNAME}")
  private String apiKeyUsername;

  private final ContractElementService service;
  private final LotService lotService;
  private final TestCommonStoreService testCommonStoreService;

  private List<GTResult> results = new ArrayList<>();

  @Given("I create a contract element from a file {string}")
  public void createContractElement(String fileName) {
    LocalDate currentDate = LocalDate.now();
    String currentYear = String.valueOf(currentDate.getYear());
    TransformUtils.Parser parser = TransformUtils.parser("%%CURRENT_YEAR%%", currentYear);
    ContractElement element =
        FileUtils.readRequestFile(
            String.format("contractElement/%s.json", fileName), ContractElement.class, parser);

    element.setOrigine("API");
    element.setUser(apiKeyUsername);
    delete(element.getCodeInsurer(), element.getCodeContractElement());
    for (ProductElement productElement : element.getProductElements()) {
      if (productElement.getCodeBenefitNature() == null) {
        productElement.setCodeBenefitNature("");
      }
    }
    service.create(element);
  }

  @Given("I create a lot from a file {string}")
  public void createLot(String fileName) {
    Lot element = FileUtils.readRequestFile(String.format("lot/%s.json", fileName), Lot.class);
    lotService.createLot(element);
  }

  @When(
      "I end mapping of product element with {string}, {string}, {string}, {string} to the contract element with {string}, {string}, {string} on tomorrow")
  public void endMappingTomorrow(
      String codeOffer,
      String codeProduct,
      String codeBenefitNature,
      String codeAmc,
      String codeAMC,
      String codeContractElement,
      String codeInsurer) {
    LocalDateTime to = LocalDateTime.now().plusDays(1);
    for (ContractElement contractElement : get(codeAMC)) {
      if (Objects.equals(contractElement.getCodeContractElement(), codeContractElement)
          && Objects.equals(contractElement.getCodeInsurer(), codeInsurer)) {
        ProductElement productElement = new ProductElement();
        productElement.setCodeProduct(codeProduct);
        productElement.setCodeBenefitNature(codeBenefitNature);
        productElement.setCodeAmc(codeAmc);
        productElement.setCodeOffer(codeOffer);
        productElement.setTo(to);
        endMapping(contractElement.getId(), productElement);
      }
    }
  }

  @When(
      "I update product element with {string}, {string}, {string}, {string} to the contract element with {string}, {string}, {string}")
  public void update(
      String codeOffer,
      String codeProduct,
      String codeBenefitNature,
      String codeAmc,
      String codeAMC,
      String codeContractElement,
      String codeInsurer) {
    for (ContractElement contractElement : get(codeAMC)) {
      if (Objects.equals(contractElement.getCodeContractElement(), codeContractElement)
          && Objects.equals(contractElement.getCodeInsurer(), codeInsurer)) {
        ProductElement productElement = new ProductElement();
        productElement.setCodeProduct(codeProduct);
        productElement.setCodeBenefitNature(codeBenefitNature);
        productElement.setCodeAmc(codeAmc);
        productElement.setCodeOffer(codeOffer);
        productElement.setFrom(DateUtils.parseLocalDateTime("2010-05-01", DateUtils.YYYY_MM_DD));
        update(contractElement.getId(), productElement);
      }
    }
  }

  @When("I request to gtlist with this codeInsurer {string} and codeContractElement {string}")
  public void gtList(String codeInsurers, String codeContractElements) {
    String[] codeInsurerList = codeInsurers.split(",");
    String[] codeContractElementList = codeContractElements.split(",");
    List<GTElement> elements = new ArrayList<>();
    for (int i = 0; i < codeInsurerList.length; i++) {
      GTElement gtElement = new GTElement();
      gtElement.setCodeInsurer(codeInsurerList[i]);
      gtElement.setCodeContractElement(codeContractElementList[i]);
      elements.add(gtElement);
    }

    String query = "/v2/contractelement/gtlist";
    ResponseEntity<List<GTResult>> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            "serviceeligibility/core/api" + query,
            elements,
            TypeUtils.listParamType(GTResult.class));
    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    results = response.getBody();
  }

  private void update(String id, ProductElement productElement) {
    service.update(id, productElement);
  }

  private void endMapping(String id, ProductElement productElement) {
    service.endMapping(id, productElement);
  }

  private Collection<ContractElement> get(String codeAMC) {
    return service.get(codeAMC);
  }

  private void delete(String codeInsurer, String codeContractElement) {
    service.delete(codeInsurer, codeContractElement);
  }

  @Then("^The response element (\\d+) has the following details and (\\d+) products$")
  public void testTheResponse(int indice, int nombre, DataTable table) {
    Map<String, String> expected = table.asMap();

    GTResult gtResult = results.get(indice);
    Assertions.assertEquals(expected.get("codeInsurer"), gtResult.getCodeInsurer());
    Assertions.assertEquals(expected.get("codeContractElement"), gtResult.getCodeContractElement());
    Assertions.assertEquals(Boolean.valueOf(expected.get("gtExist")), gtResult.isGtExist());
    Assertions.assertEquals(expected.get("label"), gtResult.getLabel());
    Assertions.assertEquals(Boolean.valueOf(expected.get("ignored")), gtResult.isIgnored());
    // TODO Assertions.assertEquals(, gtResult.getProductElements());
    Assertions.assertEquals(nombre, gtResult.getProductElements().size());
  }
}
