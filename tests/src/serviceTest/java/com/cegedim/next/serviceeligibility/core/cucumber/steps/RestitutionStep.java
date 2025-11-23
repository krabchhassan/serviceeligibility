package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestRestitutionCarteService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.RecursiveUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TransformUtils;
import com.cegedim.next.serviceeligibility.core.model.entity.RestitutionCarte;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
@Slf4j
public class RestitutionStep {

  private final TestRestitutionCarteService testRestitutionCarteService;

  private List<RestitutionCarte> allRestitutionCartes;

  @Then(
      "^I wait for (\\d+) restitutionCarte(?:s)? with idDeclarant \"(.*)\", numeroPersonne \"(.*)\", numeroContrat \"(.*)\" and numeroAdherent \"(.*)\"$")
  public void iWaitForDeclarations(
      int number,
      String idDeclarant,
      String numeroPersonne,
      String numeroContrat,
      String numeroAdherent)
      throws Exception {
    Callable<List<RestitutionCarte>> call =
        () ->
            testRestitutionCarteService.getRestitutionCarte(
                idDeclarant, numeroPersonne, numeroContrat, numeroAdherent);
    Predicate<List<RestitutionCarte>> validation =
        response ->
            number == 0 && response == null || response != null && number == response.size();
    allRestitutionCartes = RecursiveUtils.defaultRecursive(call, validation);
  }

  @Then("^The restitutionCarte has these values(?: with indice (\\d+))?$")
  public void hasRestitutionCarte(Integer index, DataTable table) {
    Map<String, String> expected = table.asMap();
    RestitutionCarte restitutionCarte =
        allRestitutionCartes.get(Objects.requireNonNullElse(index, 0));

    Assertions.assertEquals(expected.get("idDeclarant"), restitutionCarte.getIdDeclarant());
    Assertions.assertEquals(expected.get("numeroPersonne"), restitutionCarte.getNumeroPersonne());
    Assertions.assertEquals(expected.get("numeroAdherent"), restitutionCarte.getNumeroAdherent());
    Assertions.assertEquals(expected.get("numeroContrat"), restitutionCarte.getNumeroContrat());
    Assertions.assertEquals(expected.get("dateNaissance"), restitutionCarte.getDateNaissance());
    Assertions.assertEquals(expected.get("rangNaissance"), restitutionCarte.getRangNaissance());
    Assertions.assertEquals(expected.get("nirOd1"), restitutionCarte.getNirOd1());
    Assertions.assertEquals(expected.get("cleNirOd1"), restitutionCarte.getCleNirOd1());
    Assertions.assertEquals(
        TransformUtils.DEFAULT.parse(expected.get("dateRestitutionCarte")),
        restitutionCarte.getDateRestitutionCarte());
    Assertions.assertEquals(expected.get("userCreation"), restitutionCarte.getUserCreation());
  }
}
