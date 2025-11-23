package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestRetentionService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.RecursiveUtils;
import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class RetentionStep {

  private final TestRetentionService testRetentionService;

  private List<Retention> allRetentions;
  private List<Retention> oldRetentions;

  @When("I wait for {int} retention")
  public void IWaitForRetention(int retention) throws Exception {
    oldRetentions = this.allRetentions;
    Callable<List<Retention>> call = testRetentionService::getAllRetentions;
    Predicate<List<Retention>> validation =
        response ->
            retention == 0 && response == null || response != null && retention == response.size();
    allRetentions = RecursiveUtils.defaultRecursive(call, validation);
  }

  @Then("The retention with the indice {int} has these values")
  public void TheRetentionWithTheIndicesHasTheValues(int retention, DataTable table) {
    Map<String, String> expected = table.asMap();
    Retention retention1 = allRetentions.get(retention);
    if (!"null".equals(expected.get("currentEndDate"))) {
      Assertions.assertEquals(expected.get("currentEndDate"), retention1.getCurrentEndDate());
    } else {
      Assertions.assertNull(retention1.getCurrentEndDate());
    }
    if (!"null".equals(expected.get("originalEndDate"))) {
      Assertions.assertEquals(expected.get("originalEndDate"), retention1.getOriginalEndDate());
    } else {
      Assertions.assertNull(retention1.getOriginalEndDate());
    }
    Assertions.assertEquals(expected.get("status"), retention1.getStatus().toString());
  }

  @When("I update retention with the indice {int} to status {string}")
  public void IUpdateRetentionWithTheIndicesToStatus(int retention, String status) {
    testRetentionService.updateStatusRetention(allRetentions.get(retention), status);
  }

  @Then("The reception date for the retention {int} does not change")
  public void TheReceptionDateForTheRetentionDoesNotChange(int retention) {
    Assertions.assertEquals(
        allRetentions.get(retention).getReceptionDate(),
        oldRetentions.get(retention).getReceptionDate());
  }
}
