package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestHistoriqueContratService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.RecursiveUtils;
import com.cegedim.next.serviceeligibility.core.elast.contract.ContratElastic;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoriqueContratStep {

  private final TestHistoriqueContratService testHistoriqueContratService;

  public List<ContratElastic> contratElastics;

  @Given("^I delete the contract histo for this contract (\\S*)$")
  public void IdeleteTheContractHistoForThisContract(String numeroContrat) {
    testHistoriqueContratService.deleteDataIndexHistoContratForNumberContrat(numeroContrat);
  }

  @Then("^I wait for (\\d+) contract histo for this contract (\\S*)?$")
  public void IwaitforTheContractHistoForThisContract(int index, String numeroContrat)
      throws Exception {
    Callable<List<ContratElastic>> call =
        () -> testHistoriqueContratService.getIndexHistoContratByNumberContrat(numeroContrat);
    Predicate<List<ContratElastic>> validation =
        response -> index == 0 && response == null || response != null && index == response.size();
    contratElastics = RecursiveUtils.defaultRecursive(call, validation);
  }
}
