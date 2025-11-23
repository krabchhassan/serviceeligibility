package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestDeclarationService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import io.cucumber.core.exception.CucumberException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class DeclarationStep {

  private final TestDeclarationService testDeclarationService;

  private List<Declaration> allDeclarations;

  @Then("^I wait for (\\d+) declaration(?:s)?$")
  public void iWaitForDeclarations(int number) throws Exception {
    Callable<List<Declaration>> call = testDeclarationService::findAll;
    Predicate<List<Declaration>> validation =
        response ->
            number == 0 && response == null || response != null && number == response.size();
    allDeclarations = RecursiveUtils.defaultRecursive(call, validation);
  }

  @Then("^The declaration number (\\d+) has codeEtat \"(.*)\"$")
  public void theDeclarationNumberHasCodeEtat(int number, String codeEtat) {
    Assertions.assertTrue(allDeclarations.size() > number);
    Declaration declaration = allDeclarations.get(number);
    Assertions.assertEquals(codeEtat, declaration.getCodeEtat());
  }

  @Then("^The declaration with indice (\\d+) has dateRestitution \"(.*)\"$")
  public void theDeclarationNumberHasDateRestitution(int number, String dateRestitution) {
    Assertions.assertTrue(allDeclarations.size() > number);
    Declaration declaration = allDeclarations.get(number);
    Assertions.assertEquals(
        TransformUtils.DEFAULT.parse(dateRestitution), declaration.getDateRestitution());
  }

  @When(
      "^there is (\\d+) rightsDomains and the different rightsDomains has this values(?: with indice (\\d+))?$")
  public void validateDeclarations(
      Integer expectedNumberOfDomains, Integer index, DataTable table) {
    List<Map<String, String>> expectedDeclarationElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));
    Assertions.assertEquals(declaration.getDomaineDroits().size(), expectedNumberOfDomains);
    LocalDate currentDate = LocalDate.now();
    String currentYear = String.valueOf(currentDate.getYear());
    expectedDeclarationElements =
        TransformUtils.parser("%%CURRENT_YEAR%%", currentYear)
            .parseMaps(expectedDeclarationElements);

    for (int i = 0; i < expectedNumberOfDomains; i++) {
      boolean found = false;
      String startDate =
          declaration
              .getDomaineDroits()
              .get(i)
              .getPeriodeDroit()
              .getPeriodeDebut()
              .replace("/", "-");
      for (int j = 0; j < expectedNumberOfDomains; j++) {
        String endDate = expectedDeclarationElements.get(j).get("fin");
        if ("tomorrow".equals(endDate)) {
          endDate = DateUtils.stringDateFromDelta(1);
        }
        String expectedStartDate = expectedDeclarationElements.get(j).get("debut");
        if ("today".equals(expectedStartDate)) {
          expectedStartDate = DateUtils.formatDateWithoutTimestamp(null);
        }
        if (declaration
                .getDomaineDroits()
                .get(i)
                .getCode()
                .equals(expectedDeclarationElements.get(j).get("domaine"))
            && declaration
                .getDomaineDroits()
                .get(i)
                .getCodeGarantie()
                .equals(expectedDeclarationElements.get(j).get("garantie"))
            && startDate.equals(expectedStartDate)) {
          Assertions.assertEquals(
              declaration
                  .getDomaineDroits()
                  .get(i)
                  .getPeriodeDroit()
                  .getPeriodeFin()
                  .replace("/", "-"),
              endDate);
          found = true;
          if (expectedDeclarationElements.get(j).get("produit") != null) {
            Assertions.assertEquals(
                declaration.getDomaineDroits().get(i).getCodeProduit(),
                expectedDeclarationElements.get(j).get("produit"));
          }
          String periodeFermetureDebut =
              expectedDeclarationElements.get(j).get("periodeFermetureDebut");
          if (periodeFermetureDebut != null) {
            if (!"null".equals(periodeFermetureDebut)) {
              Assertions.assertEquals(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeDroit()
                      .getPeriodeFermetureDebut()
                      .replace("/", "-"),
                  periodeFermetureDebut);
            } else {
              Assertions.assertNull(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeDroit()
                      .getPeriodeFermetureDebut());
            }
          }
          String periodeFermetureFin =
              expectedDeclarationElements.get(j).get("periodeFermetureFin");
          if (periodeFermetureFin != null) {
            if (!"null".equals(periodeFermetureFin)) {
              Assertions.assertEquals(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeDroit()
                      .getPeriodeFermetureFin()
                      .replace("/", "-"),
                  periodeFermetureFin);
            } else {
              Assertions.assertNull(
                  declaration.getDomaineDroits().get(i).getPeriodeDroit().getPeriodeFermetureFin());
            }
          }

          String finOnline = expectedDeclarationElements.get(j).get("finOnline");
          if (finOnline != null) {
            String onlineEnd = finOnline;
            if ("tomorrow".equals(onlineEnd)) {
              onlineEnd = DateUtils.stringDateFromDelta(1);
            }
            if (!"null".equals(onlineEnd)) {
              Assertions.assertEquals(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeOnline()
                      .getPeriodeFin()
                      .replace("/", "-"),
                  onlineEnd);
            } else if (declaration.getDomaineDroits().get(i).getPeriodeOnline() != null) {
              Assertions.assertNull(
                  declaration.getDomaineDroits().get(i).getPeriodeOnline().getPeriodeFin());
            }
          }
          if (expectedDeclarationElements.get(j).get("isEditable") != null) {
            boolean expectedEditable =
                "true".equals(expectedDeclarationElements.get(j).get("isEditable"));
            Assertions.assertEquals(
                declaration.getDomaineDroits().get(i).getIsEditable(), expectedEditable);
          }
          if (expectedDeclarationElements.get(j).get("taux") != null) {
            Assertions.assertEquals(
                declaration.getDomaineDroits().get(i).getTauxRemboursement(),
                expectedDeclarationElements.get(j).get("taux"));
          }
          if (expectedDeclarationElements.get(j).get("unite") != null) {
            Assertions.assertEquals(
                declaration.getDomaineDroits().get(i).getUniteTauxRemboursement(),
                expectedDeclarationElements.get(j).get("unite"));
          }
          if (expectedDeclarationElements.get(j).get("priorite") != null) {
            Assertions.assertEquals(
                declaration.getDomaineDroits().get(i).getPrioriteDroit().getCode(),
                expectedDeclarationElements.get(j).get("priorite"));
          }
          if (expectedDeclarationElements.get(j).get("codeProduit") != null) {
            Assertions.assertEquals(
                declaration.getDomaineDroits().get(i).getCodeProduit(),
                expectedDeclarationElements.get(j).get("codeProduit"));
          }
        }
      }
      if (!found) {
        Assertions.fail(
            String.format(
                "Problème sur le domaine %s et la garantie %s à la date de début suivante %s",
                declaration.getDomaineDroits().get(i).getCode(),
                declaration.getDomaineDroits().get(i).getCodeGarantie(),
                startDate));
      }
    }
  }

  @Given("^the different rightsDomains has this values(?: with indice (\\d+))?$")
  public void theDifferentRightsDomainsHasThisValues(Integer index, DataTable data) {
    Map<String, String> expectedDeclarationElements = TransformUtils.DEFAULT.parseMap(data.asMap());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));

    Assertions.assertEquals(
        declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeDebut().replace("/", "-"),
        expectedDeclarationElements.get("periodeDebut"));
    Assertions.assertEquals(
        declaration.getDomaineDroits().get(0).getPeriodeDroit().getPeriodeFin().replace("/", "-"),
        expectedDeclarationElements.get("periodeFin"));
    Assertions.assertEquals(
        declaration.getDomaineDroits().get(0).getPeriodeDroit().getMotifEvenement(),
        expectedDeclarationElements.get("motifEvenement"));
    if (expectedDeclarationElements.get("periodeFermetureDebut") != null) {
      Assertions.assertEquals(
          declaration
              .getDomaineDroits()
              .get(0)
              .getPeriodeDroit()
              .getPeriodeFermetureDebut()
              .replace("/", "-"),
          expectedDeclarationElements.get("periodeFermetureDebut"));
    }
    if (expectedDeclarationElements.get("periodeFermetureFin") != null) {
      Assertions.assertEquals(
          declaration
              .getDomaineDroits()
              .get(0)
              .getPeriodeDroit()
              .getPeriodeFermetureFin()
              .replace("/", "-"),
          expectedDeclarationElements.get("periodeFermetureFin"));
    }
  }

  @Given("^I get all declarations?")
  public void getAllDeclarations() {
    throw new CucumberException(
        "/!\\/!\\/!\\ Utiliser i wait for X declarations a la place /!\\/!\\/!\\");
  }

  @Then(
      "^On the birthday \"(.*)\" there is (\\d+) rightsDomains and the different rightsDomains has this values(?: with indice (\\d+))?$")
  public void validateDeclarationsBirthday(
      String birthday, int numberOfDomains, Integer index, DataTable table) {
    List<Map<String, String>> expectedDeclarationElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));

    Assertions.assertEquals(declaration.getDomaineDroits().size(), numberOfDomains);

    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();
    LocalDate start = LocalDate.parse(currentYear + "-" + birthday);
    LocalDate startBirthday = LocalDate.parse((currentYear - 1) + "-" + birthday);
    LocalDate endBirthday = start.minusDays(1);

    if (currentDate.isAfter(start)) {
      startBirthday = start;
      endBirthday = startBirthday.minusDays(1);
      endBirthday = endBirthday.plusYears(1);
    }

    expectedDeclarationElements =
        TransformUtils.parser(
                "endBirthdayOnNextYear",
                endBirthday.plusYears(1).format(DateTimeFormatter.ISO_DATE))
            .then("endBirthday", endBirthday.format(DateTimeFormatter.ISO_DATE))
            .then(
                "startBirthdayOnNextYear",
                startBirthday.plusYears(1).format(DateTimeFormatter.ISO_DATE))
            .then("startBirthday", startBirthday.format(DateTimeFormatter.ISO_DATE))
            .parseMaps(expectedDeclarationElements);

    for (int i = 0; i < numberOfDomains; i++) {
      boolean found = false;
      String dateDebut =
          declaration
              .getDomaineDroits()
              .get(i)
              .getPeriodeDroit()
              .getPeriodeDebut()
              .replace("/", "-");

      for (int j = 0; j < numberOfDomains; j++) {
        String dateFin = expectedDeclarationElements.get(j).get("fin");
        String expectedDateDebut = expectedDeclarationElements.get(j).get("debut");

        if (declaration
                .getDomaineDroits()
                .get(i)
                .getCode()
                .equals(expectedDeclarationElements.get(j).get("domaine"))
            && declaration
                .getDomaineDroits()
                .get(i)
                .getCodeGarantie()
                .equals(expectedDeclarationElements.get(j).get("garantie"))
            && dateDebut.equals(expectedDateDebut)) {

          Assertions.assertEquals(
              declaration
                  .getDomaineDroits()
                  .get(i)
                  .getPeriodeDroit()
                  .getPeriodeFin()
                  .replace("/", "-"),
              dateFin);
          found = true;

          if (expectedDeclarationElements.get(j).get("periodeFermetureDebut") != null) {
            if (!"null".equals(expectedDeclarationElements.get(j).get("periodeFermetureDebut"))) {
              Assertions.assertEquals(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeDroit()
                      .getPeriodeFermetureDebut()
                      .replace("/", "-"),
                  expectedDeclarationElements.get(j).get("periodeFermetureDebut"));
            } else {
              Assertions.assertNull(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeDroit()
                      .getPeriodeFermetureDebut());
            }
          }

          if (expectedDeclarationElements.get(j).get("periodeFermetureFin") != null) {
            if (!"null".equals(expectedDeclarationElements.get(j).get("periodeFermetureFin"))) {
              Assertions.assertEquals(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeDroit()
                      .getPeriodeFermetureFin()
                      .replace("/", "-"),
                  expectedDeclarationElements.get(j).get("periodeFermetureFin"));
            } else {
              Assertions.assertNull(
                  declaration.getDomaineDroits().get(i).getPeriodeDroit().getPeriodeFermetureFin());
            }
          }

          if (expectedDeclarationElements.get(j).get("finOnline") != null) {
            String finOnline = expectedDeclarationElements.get(j).get("finOnline");
            if ("tomorrow".equals(finOnline)) {
              finOnline = DateUtils.stringDateFromDelta(1);
            }
            if (!"null".equals(finOnline)) {
              Assertions.assertEquals(
                  declaration
                      .getDomaineDroits()
                      .get(i)
                      .getPeriodeOnline()
                      .getPeriodeFin()
                      .replace("/", "-"),
                  finOnline);
            } else if (declaration.getDomaineDroits().get(i).getPeriodeOnline() != null) {
              Assertions.assertNull(
                  declaration.getDomaineDroits().get(i).getPeriodeOnline().getPeriodeFin());
            }
          }

          if (expectedDeclarationElements.get(j).get("isEditable") != null) {
            boolean wanted = "true".equals(expectedDeclarationElements.get(j).get("isEditable"));
            Assertions.assertEquals(declaration.getDomaineDroits().get(i).getIsEditable(), wanted);
          }
        }
      }

      if (!found) {
        Assertions.fail(
            "Problème sur le domaine "
                + declaration.getDomaineDroits().get(i).getCode()
                + " et la garantie "
                + declaration.getDomaineDroits().get(i).getCodeGarantie()
                + " à la date de début suivante "
                + dateDebut);
      }
    }
  }

  @When("I create a declaration from a file {string}")
  public void createDeclarationFormFile(String filename) {
    Declaration declaration =
        FileUtils.readRequestFile("declarations/" + filename + ".json", Declaration.class);
    declaration = testDeclarationService.createDeclaration(declaration);
    Assertions.assertNotNull(declaration);
  }

  @When("I create a declaration from a file {string} and change effet debut")
  public void createDeclarationFormFileAndChangeEffetDebut(String filename) {
    Declaration declaration =
        FileUtils.readRequestFile("declarations/" + filename + ".json", Declaration.class);
    declaration.setEffetDebut(new Date());
    declaration = testDeclarationService.createDeclaration(declaration);
    Assertions.assertNotNull(declaration);
  }

  @Then("^The declaration(?: with indice (\\d+))? has carteTPaEditerOuDigitale \"(.*)\"$")
  public void theDeclarationNumberHasCarteTPaEditerOuDigitale(
      Integer number, String carteTPaEditerOuDigitale) {
    Assertions.assertFalse(allDeclarations.isEmpty());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(number, 0));
    Assertions.assertEquals(carteTPaEditerOuDigitale, declaration.getCarteTPaEditerOuDigitale());
  }

  @Then("^The declaration with indice (\\d+) has code itelis \"(.*)\"$")
  public void theDeclarationNumberHasCodeItelis(Integer number, String codeItelis) {
    Assertions.assertFalse(allDeclarations.isEmpty());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(number, 0));
    Assertions.assertEquals(codeItelis, declaration.getContrat().getCodeItelis());
  }

  @Then(
      "^the declaration has the etat suspension \"(.*)\" with this values(?: with indice (\\d+))?$")
  public void theDeclarationNumberHasEtatSuspensionAndValues(
      String etatSuspension, Integer number, DataTable table) {
    List<Map<String, String>> expectedDeclarationElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Assertions.assertFalse(allDeclarations.isEmpty());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(number, 0));

    Assertions.assertEquals(etatSuspension, declaration.getEtatSuspension());
    Assertions.assertEquals(
        expectedDeclarationElements.size(),
        declaration.getContrat().getPeriodeSuspensions().size());

    for (int i = 0; i < expectedDeclarationElements.size(); i++) {
      PeriodeSuspensionDeclaration currentPeriode =
          declaration.getContrat().getPeriodeSuspensions().get(i);
      Assertions.assertEquals(
          expectedDeclarationElements.get(i).get("debut"), currentPeriode.getDebut());
      if ("null".equals(expectedDeclarationElements.get(i).get("fin"))) {
        Assertions.assertNull(currentPeriode.getFin());
      } else {
        Assertions.assertEquals(
            expectedDeclarationElements.get(i).get("fin"), currentPeriode.getFin());
      }
      Assertions.assertEquals(
          expectedDeclarationElements.get(i).get("typeSuspension"),
          currentPeriode.getTypeSuspension());
      Assertions.assertEquals(
          expectedDeclarationElements.get(i).get("motifSuspension"),
          currentPeriode.getMotifSuspension());
      if (!"null".equals(expectedDeclarationElements.get(i).get("motifLeveeSuspension"))) {
        Assertions.assertEquals(
            expectedDeclarationElements.get(i).get("motifLeveeSuspension"),
            currentPeriode.getMotifLeveeSuspension());
      }
    }
  }

  @And("I empty the declaration database")
  public void emptyTheDeclarationDatabase() {
    testDeclarationService.removeAll();
  }

  @Then("The declaration has this adresse")
  public void theDeclarationhasthisadress(DataTable table) {
    List<Map<String, String>> expectedDeclarationElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Declaration declaration = this.allDeclarations.get(0);
    for (Map<String, String> expectedDeclarationElement : expectedDeclarationElements) {
      Adresse adresse = declaration.getBeneficiaire().getAdresses().get(0);
      if (expectedDeclarationElement.get("ligne1") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne1"), adresse.getLigne1());
      }
      if (expectedDeclarationElement.get("ligne2") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne2"), adresse.getLigne2());
      }
      if (expectedDeclarationElement.get("ligne3") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne3"), adresse.getLigne3());
      }
      if (expectedDeclarationElement.get("ligne4") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne4"), adresse.getLigne4());
      }
      if (expectedDeclarationElement.get("ligne5") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne5"), adresse.getLigne5());
      }
      if (expectedDeclarationElement.get("ligne6") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne6"), adresse.getLigne6());
      }
      if (expectedDeclarationElement.get("ligne7") != null) {
        Assertions.assertEquals(expectedDeclarationElement.get("ligne7"), adresse.getLigne7());
      }
      Assertions.assertEquals(
          expectedDeclarationElement.get("codePostal"), adresse.getCodePostal());
    }
  }

  @Then("^The right number (\\d+) has theses conventions$")
  public void testConvention(int index, DataTable table) {
    List<Map<String, String>> expectedDeclarationElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Declaration declarationDto = allDeclarations.get(0);
    DomaineDroit right = declarationDto.getDomaineDroits().get(index);
    int indice = 0;
    for (Map<String, String> expectedDeclarationElement : expectedDeclarationElements) {
      right.getConventionnements().sort(Comparator.comparing(Conventionnement::getPriorite));
      for (int i = 0; i < right.getConventionnements().size(); i++) {
        if (indice == i) {
          Conventionnement conventionnementDto = right.getConventionnements().get(i);
          Assertions.assertEquals(
              conventionnementDto.getPriorite(),
              Integer.valueOf(expectedDeclarationElement.get("priorite")));
          Assertions.assertEquals(
              conventionnementDto.getTypeConventionnement().getCode(),
              expectedDeclarationElement.get("code"));
        }
      }
      indice++;
    }
  }

  @Then(
      "^The right number (\\d+) has codeRenvoi (additional )?\"(.*)\" and label codeRenvoi \"(.*)\"$")
  public void testCodeRenvoi(
      Integer index, Boolean additional, String codeRenvoiExpected, String labelExpected) {
    Declaration declaration = this.allDeclarations.get(0);
    DomaineDroit right = declaration.getDomaineDroits().get(index);
    if (additional != null) {
      if ("null".equals(codeRenvoiExpected)) {
        Assertions.assertNull(right.getCodeRenvoiAdditionnel());
      } else {
        Assertions.assertEquals(right.getCodeRenvoiAdditionnel(), codeRenvoiExpected);
      }
      if ("null".equals(labelExpected)) {
        Assertions.assertNull(right.getLibelleCodeRenvoiAdditionnel());
      } else {
        Assertions.assertEquals(right.getLibelleCodeRenvoiAdditionnel(), labelExpected);
      }
    } else {
      if ("null".equals(codeRenvoiExpected)) {
        Assertions.assertNull(right.getCodeRenvoi());
      } else {
        Assertions.assertEquals(right.getCodeRenvoi(), codeRenvoiExpected);
      }
      if ("null".equals(labelExpected)) {
        Assertions.assertNull(right.getLibelleCodeRenvoi());
      } else {
        Assertions.assertEquals(right.getLibelleCodeRenvoi(), labelExpected);
      }
    }
  }

  @Then("^the beneficiary has this values(?: with indice (\\d+))?$")
  public void testBeneficiary(Integer index, DataTable table) {
    List<Map<String, String>> expectedDeclarationElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));
    boolean found = false;
    if (declaration
        .getBeneficiaire()
        .getDateNaissance()
        .equals(expectedDeclarationElements.get(0).get("dateNaissance"))) {
      found = true;
      Assertions.assertEquals(
          expectedDeclarationElements.get(0).get("rangNaissance"),
          declaration.getBeneficiaire().getRangNaissance());
      Assertions.assertEquals(
          expectedDeclarationElements.get(0).get("numeroPersonne"),
          declaration.getBeneficiaire().getNumeroPersonne());
    }
    if (!found) {
      Assertions.fail(
          "Problème sur le bénéficiaire "
              + declaration.getBeneficiaire().getNumeroPersonne()
              + " né le"
              + declaration.getBeneficiaire().getDateAdhesionMutuelle());
    }
  }

  @Then("^The declaration(?: with the indice (\\d+))? has this values$")
  public void theDeclarationHas(Integer index, DataTable table) {
    Map<String, String> expected = table.asMap();
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));
    if (expected.get("nom") != null) {
      Assertions.assertEquals(
          declaration.getBeneficiaire().getAffiliation().getNom(), expected.get("nom"));
    }
    if (expected.get("nomPatronymique") != null) {
      Assertions.assertEquals(
          declaration.getBeneficiaire().getAffiliation().getNomPatronymique(),
          expected.get("nomPatronymique"));
    }
    if (expected.get("nomMarital") != null) {
      Assertions.assertEquals(
          declaration.getBeneficiaire().getAffiliation().getNomMarital(),
          expected.get("nomMarital"));
    }
    if (expected.get("codeEtat") != null) {
      Assertions.assertEquals(declaration.getCodeEtat(), expected.get("codeEtat"));
    }
    if (expected.get("numeroPersonne") != null) {
      Assertions.assertEquals(
          declaration.getBeneficiaire().getNumeroPersonne(), expected.get("numeroPersonne"));
    }
    if (expected.get("nomPorteur") != null) {
      Assertions.assertEquals(declaration.getContrat().getNomPorteur(), expected.get("nomPorteur"));
    }
  }

  @Then("^the declaration(?: with indice (\\d+))? has this contract collective information$")
  public void theDeclarationHasCollectiveInfo(Integer index, DataTable table) {
    Map<String, String> expected = table.asMap();
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));
    Assertions.assertEquals(
        declaration.getContrat().getIdentifiantCollectivite(),
        expected.get("identifiantCollectivite"));
    Assertions.assertEquals(
        declaration.getContrat().getRaisonSociale(), expected.get("raisonSociale"));
    Assertions.assertEquals(declaration.getContrat().getSiret(), expected.get("siret"));
    Assertions.assertEquals(
        declaration.getContrat().getGroupePopulation(), expected.get("groupePopulation"));
  }

  @Then("^The declaration(?: with indice (\\d+))? has domains with noOrdreDroit$")
  public void thedeclarationtestnordredroit(Integer index, DataTable table) {
    Map<String, String> expected = table.asMap();
    Declaration declaration = allDeclarations.get(Objects.requireNonNullElse(index, 0));

    for (int i = 0; i < expected.size(); i++) {
      for (DomaineDroit allDomaineDroit : declaration.getDomaineDroits()) {
        if (expected.get("code").equals(allDomaineDroit.getCode())) {
          Assertions.assertEquals(
              Integer.valueOf(expected.get("order")), allDomaineDroit.getNoOrdreDroit());
        }
      }
    }
  }
}
