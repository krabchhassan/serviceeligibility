package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService.CORE_API;
import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.*;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestBatch620Service;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCarteService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.*;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class CardStep {

  private final TestCommonStoreService testCommonStoreService;
  private final TestBatch620Service testBatch620Service;
  private final TestCarteService testCarteService;

  private static final String URL = CORE_API + "/v1/cards";

  private static final String PATH = "carteDemat/%s.json";

  public List<CarteDemat> carteDematList;
  public CarteDemat carte;

  @When("I create a card from a file {string}")
  public void create(String fileName) {
    CarteDemat carteDemat =
        FileUtils.readRequestFile(String.format(PATH, fileName), CarteDemat.class);
    createCard(carteDemat);
  }

  private void createCard(CarteDemat carteDemat) {
    ResponseEntity<CarteDemat> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            URL, carteDemat, TypeUtils.simpleParamType(CarteDemat.class));
    Assertions.assertNotNull(response);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Then("^I wait for (\\d+) card(?:s)?$")
  public void iWaitForCards(Integer number) throws Exception {
    Callable<List<CarteDemat>> call = testCarteService::findAll;
    Predicate<List<CarteDemat>> validation =
        response -> number == 0 && CollectionUtils.isEmpty(response) || number == response.size();
    carteDematList = RecursiveUtils.defaultRecursive(call, validation);
  }

  @Then("the card at index {int} has this values")
  public void theCardAtIndexHasThisValues(Integer index, DataTable table) {
    Map<String, String> expected = table.asMap();
    CarteDemat carteDemat = carteDematList.get(index);

    checkValue(expected, carteDemat);
    if (expected.get("periodeDebut") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(TransformUtils.DEFAULT.parse(expected.get("periodeDebut"))),
          carteDemat.getPeriodeDebut());
    }
    if (expected.get("periodeFin") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(TransformUtils.DEFAULT.parse(expected.get("periodeFin"))),
          carteDemat.getPeriodeFin());
    }
  }

  private static void checkValue(Map<String, String> expected, CarteDemat carteDemat) {
    if (expected.get("AMC_contrat") != null) {
      Assertions.assertEquals(expected.get("AMC_contrat"), carteDemat.getAMC_contrat());
    }
    if (expected.get("isLastCarteDemat") != null) {
      if ("true".equals(expected.get("isLastCarteDemat"))) {
        Assertions.assertTrue(carteDemat.getIsLastCarteDemat());
      } else {
        Assertions.assertFalse(carteDemat.getIsLastCarteDemat());
      }
    }
    if (expected.get("codeServices") != null) {
      Assertions.assertEquals(expected.get("codeServices"), carteDemat.getCodeServices().get(0));
    }
    if (expected.get("codeServicesSize") != null) {
      Assertions.assertEquals(
          Integer.valueOf(expected.get("codeServicesSize")), carteDemat.getCodeServices().size());
    }
  }

  @Then("^On the birthday \"(.*)\" the card at index (\\d+) has this values?")
  public void onBirthdayThetCardAtIndex(String birthday, Integer index, DataTable table) {
    Map<String, String> expected = table.asMap();
    CarteDemat carteDemat = carteDematList.get(index);

    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    LocalDate start = LocalDate.parse(currentYear + "/" + birthday, DateUtils.YYYY_MM_DD);
    LocalDate startBirthday = start.minusYears(1);
    LocalDate endBirthday = start.minusDays(1);
    if (currentDate.isAfter(start)) {
      endBirthday = start.plusYears(1).minusDays(1);
      startBirthday = start;
    }
    checkValue(expected, carteDemat);

    Assertions.assertEquals(
        formatDate(startBirthday, SLASHED_FORMATTER), carteDemat.getPeriodeDebut());
    Assertions.assertEquals(formatDate(endBirthday, SLASHED_FORMATTER), carteDemat.getPeriodeFin());
  }

  @When("I process declarations for carteDemat the {string}")
  public void iProcessDeclarationsForCarteDematThe(String date) {
    try {
      testBatch620Service.processDeclarations620(TransformUtils.DEFAULT.parse(date), null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @When("I process declarations for carteDemat the {string} with codeCouloir {string}")
  public void iProcessDeclarationsForCarteDematThe(String date, String codeCouloir) {
    try {
      testBatch620Service.processDeclarations620(date, codeCouloir);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Then("^there are (\\d+) domainesConventions on the card (?:at index (\\d+))? has this values$")
  public void testRangConvention(int nbConventions, Integer index, DataTable table) {
    List<Map<String, String>> expected = TransformUtils.DEFAULT.parseMaps(table.asMaps());
    CarteDemat carteDemat = carteDematList.get(Objects.requireNonNullElse(index, 0));
    List<DomaineConvention> conventions = carteDemat.getDomainesConventions();
    Assertions.assertEquals(conventions.size(), nbConventions);

    for (int i = 0; i < nbConventions; i++) {
      Assertions.assertEquals(
          Integer.valueOf(expected.get(i).get("rang")), conventions.get(i).getRang());
      Assertions.assertEquals(expected.get(i).get("code"), conventions.get(i).getCode());
    }
  }

  @Then(
      "^there are (\\d+) conventions on the domainesConventions (?:at index (\\d+))? on the card (?:at index (\\d+))? has this values$")
  public void testConvention(
      int nbConventions, Integer indexConvention, Integer index, DataTable table) {
    List<Map<String, String>> expected = TransformUtils.DEFAULT.parseMaps(table.asMaps());
    CarteDemat carteDemat = carteDematList.get(Objects.requireNonNullElse(index, 0));
    List<Convention> conventions =
        carteDemat
            .getDomainesConventions()
            .get(Objects.requireNonNullElse(indexConvention, 0))
            .getConventions();
    Assertions.assertEquals(conventions.size(), nbConventions);

    for (int i = 0; i < nbConventions; i++) {
      Assertions.assertEquals(
          Integer.valueOf(expected.get(i).get("priorite")), conventions.get(i).getPriorite());
      Assertions.assertEquals(expected.get(i).get("code"), conventions.get(i).getCode());
    }
  }

  @Then("the card has code itelis {string}")
  public void thecardhascodeitelis(String code) {
    CarteDemat carteDemat = carteDematList.getFirst();
    code = "null".equals(code) ? null : code;
    Assertions.assertEquals(code, carteDemat.getContrat().getCodeItelis());
  }

  @Then("^the card for the benef(?: with indice (\\d+))? has (\\d+) domainesCouverture$")
  public void thecardforbenefhasdomainecouverture(
      Integer indiceBenef, int nbDomainesCouv, DataTable table) {
    List<Map<String, String>> expected = TransformUtils.DEFAULT.parseMaps(table.asMaps());
    int indexBenef = Objects.requireNonNullElse(indiceBenef, 0);
    CarteDemat carteDemat = carteDematList.get(0);
    List<DomaineDroit> domainesCouvertures =
        carteDemat.getBeneficiaires().get(indexBenef).getDomainesCouverture();
    Assertions.assertEquals(nbDomainesCouv, domainesCouvertures.size());
    for (int i = 0; i < nbDomainesCouv; i++) {
      if (expected.get(i).get("code") != null) {
        Assertions.assertEquals(expected.get(i).get("code"), domainesCouvertures.get(i).getCode());
      }
      if (expected.get(i).get("tauxRemboursement") != null) {
        Assertions.assertEquals(
            expected.get(i).get("tauxRemboursement"),
            domainesCouvertures.get(i).getTauxRemboursement());
      }
      if (expected.get(i).get("codeGarantie") != null) {
        Assertions.assertEquals(
            expected.get(i).get("codeGarantie"), domainesCouvertures.get(i).getCodeGarantie());
      }
      if (expected.get(i).get("libelleGarantie") != null) {
        Assertions.assertEquals(
            expected.get(i).get("libelleGarantie"),
            domainesCouvertures.get(i).getLibelleGarantie());
      }
      if (expected.get(i).get("noOrdreDroit") != null) {
        Assertions.assertEquals(
            Integer.valueOf(expected.get(i).get("noOrdreDroit")),
            domainesCouvertures.get(i).getNoOrdreDroit());
      }
    }
  }

  @Then("^the card for the benef(?: with indice (\\d+))? has (\\d+) domainesRegroup$")
  public void thecardforbenefhasdomaineRegroup(
      Integer indiceBenef, int nbDomaineRegroup, DataTable table) {
    List<Map<String, String>> expected = TransformUtils.DEFAULT.parseMaps(table.asMaps());
    int indexBenef = Objects.requireNonNullElse(indiceBenef, 0);
    CarteDemat carteDemat = carteDematList.getFirst();
    List<DomaineCarte> domainesRegroup =
        carteDemat.getBeneficiaires().get(indexBenef).getDomainesRegroup();
    Assertions.assertEquals(nbDomaineRegroup, domainesRegroup.size());
    for (int i = 0; i < nbDomaineRegroup; i++) {
      if (expected.get(i).get("code") != null) {
        Assertions.assertEquals(expected.get(i).get("code"), domainesRegroup.get(i).getCode());
      }
      if (expected.get(i).get("taux") != null) {
        Assertions.assertEquals(expected.get(i).get("taux"), domainesRegroup.get(i).getTaux());
      }
      if (expected.get(i).get("unite") != null) {
        Assertions.assertEquals(expected.get(i).get("unite"), domainesRegroup.get(i).getUnite());
      }
      if (expected.get(i).get("priorite") != null) {
        Assertions.assertEquals(
            expected.get(i).get("priorite"), domainesRegroup.get(i).getPrioriteDroits().getCode());
      }
      if (expected.get(i).get("rang") != null) {
        Assertions.assertEquals(
            Integer.valueOf(expected.get(i).get("rang")), domainesRegroup.get(i).getRang());
      }
    }
  }

  @Then(
      "^the card(?: at index (\\d+))? for the benef(?: with indice (\\d+))? and domaineRegroup with indice (\\d+)$")
  public void thecardforbenefhasdomaineRegroupAtIndice(
      Integer indexCard, Integer indexBenef, Integer indexDomain, DataTable table) {
    Map<String, String> expectedDataDomaineRegroup = TransformUtils.DEFAULT.parseMap(table.asMap());
    int indexForCard = Objects.requireNonNullElse(indexCard, 0);
    int indexForBenef = Objects.requireNonNullElse(indexBenef, 0);
    int indexForDomain = Objects.requireNonNullElse(indexDomain, 0);
    DomaineCarte domaineRegroup =
        carteDematList
            .get(indexForCard)
            .getBeneficiaires()
            .get(indexForBenef)
            .getDomainesRegroup()
            .get(indexForDomain);

    if (expectedDataDomaineRegroup.containsKey("code")) {
      Assertions.assertEquals(expectedDataDomaineRegroup.get("code"), domaineRegroup.getCode());
    }
    if (expectedDataDomaineRegroup.containsKey("taux")) {
      Assertions.assertEquals(expectedDataDomaineRegroup.get("taux"), domaineRegroup.getTaux());
    }
    if (expectedDataDomaineRegroup.containsKey("unite")) {
      Assertions.assertEquals(expectedDataDomaineRegroup.get("unite"), domaineRegroup.getUnite());
    }
    if (expectedDataDomaineRegroup.containsKey("codeProduit")) {
      Assertions.assertEquals(
          expectedDataDomaineRegroup.get("codeProduit"), domaineRegroup.getCodeProduit());
    }
    if (expectedDataDomaineRegroup.containsKey("categorieDomaine")) {
      Assertions.assertEquals(
          expectedDataDomaineRegroup.get("categorieDomaine"), domaineRegroup.getCategorieDomaine());
    }
    if (expectedDataDomaineRegroup.containsKey("referenceCouverture")) {
      Assertions.assertEquals(
          expectedDataDomaineRegroup.get("referenceCouverture"),
          domaineRegroup.getReferenceCouverture());
    }
    if (expectedDataDomaineRegroup.containsKey("codeGarantie")) {
      Assertions.assertEquals(
          expectedDataDomaineRegroup.get("codeGarantie"), domaineRegroup.getCodeGarantie());
    }
    if (expectedDataDomaineRegroup.containsKey("libelleGarantie")) {
      Assertions.assertEquals(
          expectedDataDomaineRegroup.get("libelleGarantie"), domaineRegroup.getLibelleGarantie());
    }
  }

  @Then("^I compare the card and the trace(?: at index (\\d+))$")
  public void icomparethetraceatindex(Integer index) {
    //    index = Objects.requireNonNullElse(index,0);
    //    CarteDemat carteDemat = carteDematList.get(index);
    // TODO TraceStep avant celui_ci
  }

  @Then("^I invalid cards with amc \"(.*)\" and contractNumber \"(.*)\"$")
  public void iInvalidCardsWithAmcAndContractNumber(String amc, String contractNumber) {
    testBatch620Service.invalidationCarte620(amc, contractNumber);
  }

  @Then("I get card with id {string}")
  public void iGetcardWithId(String id) {
    carte = testCarteService.findOneById(id);
  }

  @Then("The value of the field isLastCarteDemat is {string}")
  public void testLastCarteDemat(String isLastCarteDemat) {
    boolean isLastCarteDematExpected = Boolean.parseBoolean(isLastCarteDemat);
    Assertions.assertEquals(isLastCarteDematExpected, carte.getIsLastCarteDemat());
  }
}
