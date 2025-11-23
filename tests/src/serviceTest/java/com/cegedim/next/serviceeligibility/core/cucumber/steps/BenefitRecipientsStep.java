package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients.BenefitRecipientsDto;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestBeneficiaryService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TryToUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class BenefitRecipientsStep {

  private final TestCommonStoreService testCommonStoreService;
  private static final String URL = TestCommonStoreService.CORE_API + "/v1/benefitrecipients";

  private List<BenefitRecipientsDto> resultBenefitRecipents = null;

  private final TestBeneficiaryService testBeneficiaryService;

  @When(
      "^I (try to )?search for the beneficit recipients for \"(.*)\", \"(.*)\", \"(.*)\", \"(.*)\"$")
  public void searchBenefitRecipients(
      Boolean tryTo,
      String idPerson,
      String subscriberId,
      String contractNumber,
      String insurerId) {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("idPerson", idPerson);
    params.put("contractNumber", contractNumber);
    params.put("insurerId", insurerId);
    if (StringUtils.isNotBlank(subscriberId)) {
      params.put("subscriberId", subscriberId);
    }

    testCommonStoreService.setHttpClientErrorException(
        TryToUtils.tryTo(
            () -> {
              ResponseEntity<List<BenefitRecipientsDto>> benefRecipientsList =
                  testCommonStoreService.getToGivenURL(
                      URL, params, TypeUtils.listParamType(BenefitRecipientsDto.class));

              Assertions.assertNotNull(benefRecipientsList);
              Assertions.assertNotNull(benefRecipientsList.getBody());
              resultBenefitRecipents = benefRecipientsList.getBody();
            },
            tryTo));
  }

  @Then("^we found (\\d+) benefitRecipients$")
  public void numberBenefitRecipientsFound(int benefitRecipientsNb) {
    Assertions.assertEquals(benefitRecipientsNb, resultBenefitRecipents.size());
  }

  @Then("^the result with benefitRecipient id \"(.*)\" has values$")
  public void theResultWithBenefitRecipientseHasValues(int index, DataTable table) {
    BenefitRecipientsDto benefitRecipients = resultBenefitRecipents.get(index);
    Map<String, String> expected = table.asMap();
    Assertions.assertEquals(expected.get("idBeyond"), benefitRecipients.getIdBeyond());
    Assertions.assertEquals(
        expected.get("validityStartDate"), benefitRecipients.getValidityStartDate());
    Assertions.assertEquals(
        expected.get("validityEndDate"), benefitRecipients.getValidityEndDate());
  }

  @When("I drop the collection for Beneficiary")
  public void iDropTheCollectionForBeneficiary() {
    testBeneficiaryService.dropCollections();
  }
}
