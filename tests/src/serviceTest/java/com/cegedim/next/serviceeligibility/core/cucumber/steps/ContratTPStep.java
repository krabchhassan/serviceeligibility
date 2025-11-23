package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService.CORE_API;
import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestContratTPService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeSuspensionContract;
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@Slf4j
public class ContratTPStep {

  private final TestContratTPService testContratTPService;

  private List<ContractTP> contratTPs;
  private HttpStatusCode statusCode;

  private static final String PATH = "contratsTP/%s.json";

  private final TestCommonStoreService testCommonStoreService;

  private static final String URL = CORE_API + "/v1/consolidation";

  @When("I drop the collection for Contract")
  public void iDropTheCollectionForContract() {
    testContratTPService.dropCollection();
  }

  @When("I create a contrat from file {string}")
  public void iCreateAContratFromFile(String fileName) {
    ContractTP contractTP =
        FileUtils.readRequestFile(String.format(PATH, fileName), ContractTP.class);
    testContratTPService.create(contractTP);
  }

  @Then("I write a contrat into file {string}")
  public void iWriteAContratIntoFile(String fileName) {
    FileUtils.writeFile(String.format(PATH, fileName), contratTPs.getFirst());
  }

  @Then("^I wait for (\\d+) contract(?:s)?$")
  public void iWaitForContracts(int number) throws Exception {
    Callable<List<ContractTP>> call = testContratTPService::findAll;
    Predicate<List<ContractTP>> validation =
        response ->
            number == 0 && response == null || response != null && number == response.size();
    contratTPs = RecursiveUtils.defaultRecursive(call, validation);
  }

  @When("^the expected contract TP(?: with indice (\\d+))? is identical to \"(.*)\" content$")
  public void theExpectedContractTPIsIdenticalTo(Integer indice, String content) {
    checkContrat(indice, content);
  }

  private void checkContrat(Integer indice, String content, TransformUtils.Parser... parsers) {
    ContractTP expected =
        FileUtils.readResultFile(String.format(PATH, content), ContractTP.class, parsers);
    expected
        .getBeneficiaires()
        .forEach(
            beneficiaire -> {
              beneficiaire
                  .getDomaineDroits()
                  .forEach(
                      domaineDroitContractTP ->
                          domaineDroitContractTP
                              .getGaranties()
                              .forEach(
                                  garantie ->
                                      garantie
                                          .getProduits()
                                          .forEach(
                                              produit ->
                                                  produit
                                                      .getReferencesCouverture()
                                                      .forEach(
                                                          referenceCouverture ->
                                                              referenceCouverture
                                                                  .getNaturesPrestation()
                                                                  .forEach(
                                                                      naturePrestation -> {
                                                                        if (naturePrestation
                                                                                .getNaturePrestation()
                                                                            == null) {
                                                                          naturePrestation
                                                                              .setNaturePrestation(
                                                                                  "");
                                                                        }
                                                                      })))));
            });
    ContractTP received = indice != null ? contratTPs.get(indice) : contratTPs.get(0);
    removeDynamicValues(received);
    assertThat(expected).usingRecursiveComparison().isEqualTo(received);
  }

  @When(
      "^on the birthday \"(.*)\" the expected contract TP(?: with indice (\\d+))? is identical to \"(.*)\" content$")
  public void onBirthdayTheExpectedContractTPIsIdenticalTo(
      String birthday, Integer indice, String content) {
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    LocalDate start = LocalDate.parse(currentYear + "/" + birthday, DateUtils.YYYY_MM_DD);
    LocalDate startBirthday = start.minusYears(1);
    LocalDate endBirthday = start.minusDays(1);
    if (currentDate.isAfter(start)) {
      endBirthday = start.plusYears(1).minusDays(1);
      startBirthday = start;
    }
    TransformUtils.Parser birthdayParser =
        TransformUtils.parser(
                "endBirthdayOnNextYear", endBirthday.plusYears(1).format(DateUtils.YYYY_MM_DD))
            .then("endBirthday", endBirthday.format(DateUtils.YYYY_MM_DD))
            .then(
                "startBirthdayOnNextYear", startBirthday.plusYears(1).format(DateUtils.YYYY_MM_DD))
            .then("startBirthday", startBirthday.format(DateUtils.YYYY_MM_DD));
    checkContrat(indice, content, birthdayParser);
  }

  private void removeDynamicValues(ContractTP contractTP) {
    contractTP.set_id(null);
    for (BeneficiaireContractTP beneficiaire : contractTP.getBeneficiaires()) {
      beneficiaire.setDateCreation(null);
      beneficiaire.setDateModification(null);
    }
    contractTP.setDateModification(null);
    contractTP.setDateConsolidation(null);
    contractTP.setDateCreation(null);
  }

  @Then("the consolidated contract has values")
  public void testConsolidatedContrat(DataTable table) {
    Map<String, String> expected = table.asMap();
    ContractTP contractTP = contratTPs.getFirst();

    BeneficiaireContractTP beneficiaire = contractTP.getBeneficiaires().getFirst();

    Assertions.assertEquals(expected.get("numeroContrat"), contractTP.getNumeroContrat());
    Assertions.assertEquals(expected.get("idDeclarant"), contractTP.getIdDeclarant());
    if ("true".equals(expected.get("isContratResponsable"))) {
      Assertions.assertEquals(true, contractTP.getIsContratResponsable());
    }
    if ("false".equals(expected.get("isContratResponsable"))) {
      Assertions.assertEquals(false, contractTP.getIsContratResponsable());
    }
    if (expected.get("codeItelis") != null) {
      Assertions.assertEquals(expected.get("codeItelis"), contractTP.getCodeItelis());
    }
    if (expected.get("dateRadiation") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(expected.get("dateRadiation")),
          beneficiaire.getDateRadiation());
    }
    if (expected.get("dateResiliation") != null) {
      Assertions.assertEquals(
          TransformUtils.DEFAULT.parse(expected.get("dateResiliation")),
          contractTP.getDateResiliation());
    }
    if (expected.get("nirBeneficiaire") != null) {
      Assertions.assertEquals(expected.get("nirBeneficiaire"), beneficiaire.getNirBeneficiaire());
    }
    if (expected.get("cleNirBeneficiaire") != null) {
      Assertions.assertEquals(
          expected.get("cleNirBeneficiaire"), beneficiaire.getCleNirBeneficiaire());
    }
    if (expected.get("rangAdministratif") != null) {
      Assertions.assertEquals(
          expected.get("rangAdministratif"), beneficiaire.getRangAdministratif());
    }
    if (expected.get("dernierMouvementRecu") != null) {
      Assertions.assertEquals(
          expected.get("dernierMouvementRecu"), beneficiaire.getDernierMouvementRecu());
    }

    if (expected.get("identifiantCollectivite") != null) {
      Assertions.assertEquals(
          expected.get("identifiantCollectivite"), contractTP.getIdentifiantCollectivite());
    }
    if (expected.get("raisonSociale") != null) {
      Assertions.assertEquals(expected.get("raisonSociale"), contractTP.getRaisonSociale());
    }
    if (expected.get("siret") != null) {
      Assertions.assertEquals(expected.get("siret"), contractTP.getSiret());
    }
    if (expected.get("groupePopulation") != null) {
      Assertions.assertEquals(expected.get("groupePopulation"), contractTP.getGroupePopulation());
    }
  }

  @Then(
      "^I process all declaration for idDeclarant \"(.*)\", numContrat \"(.*)\", numAdhérent \"(.*)\"$")
  public void consolidate(String idDeclarant, String numeroContrat, String numeroAdherent) {
    ResponseEntity<String> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            URL + "/" + idDeclarant + "/" + numeroContrat + "/" + numeroAdherent,
            null,
            TypeUtils.simpleParamType(String.class));
    Assertions.assertNotNull(response);
    statusCode = response.getStatusCode();
  }

  @Then("The error is {string}")
  public void testError(String error) {
    Assertions.assertEquals(error, String.valueOf(statusCode.value()));
  }

  @Then("^the consolidated contract(?: with indice (\\d+))? has (\\d+) periodeSuspension$")
  public void testNumberSuspension(Integer indiceContrat, int nbPeriodeSuspension) {
    ContractTP contractTP = contratTPs.get(Objects.requireNonNullElse(indiceContrat, 0));

    Assertions.assertEquals(
        nbPeriodeSuspension, contractTP.getSuspension().getPeriodesSuspension().size());
  }

  @Then(
      "^the periodeSuspension(?: with indice (\\d+))? of the consolidated contract(?: with indice (\\d+))? has these values$")
  public void testPeriodeSuspension(
      Integer indicePeriode, Integer periodeContract, DataTable table) {
    Map<String, String> expected = table.asMap();

    ContractTP contractTP = contratTPs.get(Objects.requireNonNullElse(periodeContract, 0));
    PeriodeSuspensionContract periodeSuspensionContract =
        contractTP
            .getSuspension()
            .getPeriodesSuspension()
            .get(Objects.requireNonNullElse(indicePeriode, 0));
    Assertions.assertEquals(
        expected.get("dateDebutSuspension"), periodeSuspensionContract.getDateDebutSuspension());
    if (!"null".equals(expected.get("dateFinSuspension"))) {
      Assertions.assertEquals(
          expected.get("dateFinSuspension"), periodeSuspensionContract.getDateFinSuspension());
    } else {
      Assertions.assertNull(periodeSuspensionContract.getDateFinSuspension());
    }
  }
}
