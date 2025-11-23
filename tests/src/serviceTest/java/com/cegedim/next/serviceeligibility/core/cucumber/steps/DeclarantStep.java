package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService.CORE_API;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class DeclarantStep {

  private final TestCommonStoreService testCommonStoreService;

  private final DeclarantDao declarantDao;

  private static final String URL = CORE_API + "/v1/declarants";

  @Value("${CLIENT_USERNAME}")
  private String apiKeyUsername;

  private static final String PATH = "declarants/%s.json";

  @When("I create a declarant from a file {string}")
  public void create(String fileName) {
    Declarant declarant = FileUtils.readRequestFile(String.format(PATH, fileName), Declarant.class);
    declarant.setIdClientBO(apiKeyUsername);
    createDeclarant(declarant);
  }

  private void createDeclarant(Declarant declarant) {
    declarantDao.create(declarant);
  }

  @Given("^I create a declarant with idClientBO \"(.*)\"$")
  public void createWithIdClientBO(String idClientBO) {
    Declarant declarant = FileUtils.readRequestFile("declarants/declarant.json", Declarant.class);
    declarant.setIdClientBO(idClientBO);
    createDeclarant(declarant);
  }
}
