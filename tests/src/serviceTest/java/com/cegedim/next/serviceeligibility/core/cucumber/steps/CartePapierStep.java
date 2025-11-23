package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCarteService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.RecursiveUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TransformUtils;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.cartepapier.DomaineConventionCartePapier;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CartePapier;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class CartePapierStep {

  private final TestCarteService testCarteService;

  public List<CartePapierEditique> cartePapierList;
  public CartePapierEditique carte;

  @When("^I wait for (\\d+) cartes papier(?:s)?$")
  public void iWaitForCards(Integer number) throws Exception {
    Callable<List<CartePapierEditique>> call = testCarteService::findAllCartesPapierEditiques;
    int indexCarte = Objects.requireNonNullElse(number, 0);
    Predicate<List<CartePapierEditique>> validation =
        response ->
            indexCarte == 0 && CollectionUtils.isEmpty(response) || indexCarte == response.size();
    cartePapierList = RecursiveUtils.defaultRecursive(call, validation);
  }

  @Then("^the carte papier at index (\\d+) has this adresse$")
  public void theCartePapierAtIndex(Integer index, DataTable table) throws Exception {
    CartePapierEditique cartePapierEditique =
        cartePapierList.get(Objects.requireNonNullElse(index, 0));
    Adresse actualAdresse = cartePapierEditique.getCartePapier().getAdresse();

    List<Map<String, String>> expected = TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Map<String, String> expectedAdresse = expected.getFirst();

    // dans un objet Adresse, il n'y a que 7lignes de 1 à 7.
    for (int numLigne = 1; numLigne < 8; numLigne++) {
      Method getLigne = actualAdresse.getClass().getMethod("getLigne" + numLigne);
      String ligneValue = (String) getLigne.invoke(actualAdresse);
      if (expectedAdresse.get("ligne" + numLigne) != null) {
        Assertions.assertEquals(expectedAdresse.get("ligne" + numLigne), ligneValue);
      }
    }

    if (expectedAdresse.get("codePostal") != null) {
      Assertions.assertEquals(expectedAdresse.get("codePostal"), actualAdresse.getCodePostal());
    }
  }

  @Then("the carte papier has code itelis {string}")
  public void thecardhascodeitelis(String code) {
    CartePapier cartePapier = cartePapierList.get(0).getCartePapier();
    if (code != null && !code.equals("null") && cartePapier != null) {
      Assertions.assertEquals(code, cartePapier.getContrat().getCodeItelis());
    }
  }

  @Then("^the carte papier at index (\\d+) has these values$")
  public void theCartePapierAtIndexhasthesevalues(Integer index, DataTable table) throws Exception {
    Map<String, String> expectedCartePapier = table.asMap();
    int indexCarte = Objects.requireNonNullElse(index, 0);
    CartePapierEditique cartePapierEditique = cartePapierList.get(indexCarte);
    CartePapier cartePapier = cartePapierEditique.getCartePapier();

    if (expectedCartePapier.get("numeroAMC") != null) {
      Assertions.assertEquals(expectedCartePapier.get("numeroAMC"), cartePapier.getNumeroAMC());
    }
    if (expectedCartePapier.get("nomAMC") != null) {
      Assertions.assertEquals(expectedCartePapier.get("nomAMC"), cartePapier.getNomAMC());
    }
    if (expectedCartePapier.get("libelleAMC") != null) {
      Assertions.assertEquals(expectedCartePapier.get("libelleAMC"), cartePapier.getLibelleAMC());
    }
    if (expectedCartePapier.get("periodeDebut") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(expectedCartePapier.get("periodeDebut")),
          cartePapier.getPeriodeDebut());
    }
    if (expectedCartePapier.get("periodeFin") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(expectedCartePapier.get("periodeFin")),
          cartePapier.getPeriodeFin());
    }
    if (expectedCartePapier.get("codeConvention") != null) {
      Assertions.assertEquals(
          expectedCartePapier.get("codeConvention"), cartePapier.getCodeConvention());
    }
    if (expectedCartePapier.get("libelleConvention") != null) {
      Assertions.assertEquals(
          expectedCartePapier.get("libelleConvention"), cartePapier.getLibelleConvention());
    }
    if (expectedCartePapier.get("domaineConventionLibelle") != null) {
      Assertions.assertEquals(
          expectedCartePapier.get("domaineConventionLibelle"),
          cartePapier.getDomainesConventions().get(0).getLibelle());
    }
    if (expectedCartePapier.get("domaineConventionLibelleConv") != null) {
      Assertions.assertEquals(
          expectedCartePapier.get("domaineConventionLibelleConv"),
          cartePapier.getDomainesConventions().get(0).getConventions().get(0).getLibelle());
    }
    if (expectedCartePapier.get("dateTraitement") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(expectedCartePapier.get("dateTraitement")),
          cartePapier.getDateTraitement());
    }

    Assertions.assertEquals("EMISSION", cartePapierEditique.getEditingObject().getStateObject());
    Assertions.assertEquals(
        "CARTES_PAPIER", cartePapierEditique.getEditingObject().getDocumentType().getCode());
    Assertions.assertEquals("TO_BE_ISSUED", cartePapierEditique.getInternal().getStatus());
    Assertions.assertNotEquals(
        0, cartePapierEditique.getInternal().getTraceExtractionConso().size());

    if (expectedCartePapier.get("contexte") != null) {
      Assertions.assertEquals(expectedCartePapier.get("contexte"), cartePapier.getContexte());
    }
    if (expectedCartePapier.get("fondCarte") != null
        && !expectedCartePapier.get("fondCarte").equals("null")) {
      Assertions.assertEquals(expectedCartePapier.get("fondCarte"), cartePapier.getFondCarte());
    }
  }

  @Then(
      "^there are (\\d+) domainesConventions on the carte papier (?:at index (\\d+))? has this values$")
  public void testRangConvention(int nbConventions, Integer index, DataTable table) {
    List<Map<String, String>> expected = TransformUtils.DEFAULT.parseMaps(table.asMaps());

    CartePapierEditique cartePapierEditique =
        cartePapierList.get(Objects.requireNonNullElse(index, 0));
    List<DomaineConventionCartePapier> conventions =
        cartePapierEditique.getCartePapier().getDomainesConventions();

    Assertions.assertEquals(nbConventions, conventions.size());

    for (int i = 0; i < nbConventions; i++) {
      Assertions.assertEquals(
          Integer.valueOf(expected.get(i).get("rang")), conventions.get(i).getRang());
      Assertions.assertEquals(expected.get(i).get("code"), conventions.get(i).getCode());
    }
  }

  @Then("^the carte papier for the benef(?: with indice (\\d+))? has (\\d+) domainesCouverture$")
  public void thecardforbenefhasdomainecouverture(
      Integer indice, int nbDomainesCouvertures, DataTable table) {
    List<Map<String, String>> expectedCouvertures =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    int indexForBenef = Objects.requireNonNullElse(indice, 0);

    List<DomaineDroit> couvertures =
        cartePapierList
            .get(0)
            .getCartePapier()
            .getBeneficiaires()
            .get(indexForBenef)
            .getDomainesCouverture();

    Assertions.assertEquals(nbDomainesCouvertures, couvertures.size());

    for (int i = 0; i < nbDomainesCouvertures; i += 1) {
      if (expectedCouvertures.get(i).get("code") != null) {
        Assertions.assertEquals(
            expectedCouvertures.get(i).get("code"), couvertures.get(i).getCode());
      }
      if (expectedCouvertures.get(i).get("tauxRemboursement") != null) {
        Assertions.assertEquals(
            expectedCouvertures.get(i).get("tauxRemboursement"),
            couvertures.get(i).getTauxRemboursement());
      }
      if (expectedCouvertures.get(i).get("noOrdreDroit") != null) {
        Assertions.assertEquals(
            Integer.valueOf(expectedCouvertures.get(i).get("noOrdreDroit")),
            couvertures.get(i).getNoOrdreDroit());
      }
    }
  }
}
