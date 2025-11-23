package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FluxStep {

  @When("I get flux with parameters")
  public void iGetFluxWithParameters(DataTable expected) {
    // TODO #todotest
  }

  @Then("{int} flux is returned")
  public void fluxIsReturned(int code) {
    // TODO #todotest
  }

  @Then(
      "^I received a flux with the type file \"(.*)\", the declarant id \"(.*)\" and the processus \"(.*)\"(?: with index (\\d+))?")
  public void IReceivedaflux(String file, String amc, String processus, Integer indice) {
    // TODO #todotest
  }
}
