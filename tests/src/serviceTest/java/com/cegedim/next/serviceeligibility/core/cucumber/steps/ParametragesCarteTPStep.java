package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TryToUtils;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParametragesCarteTPStep {
  private final ParametrageCarteTPService parametrageCarteTPService;
  private final TestCommonStoreService testCommonStoreService;

  private static final String PATH = "parametrageCarteTP/%s.json";
  private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  @Given("I remove all TP card parameters from database")
  public void deleteAll() {
    parametrageCarteTPService.deleteAll();
  }

  @When("^I (try to )?create manual TP card parameters from file \"(.*)\"$")
  public void createManual(Boolean tryTo, String fileName) {
    String today = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
    create(tryTo, fileName, today, null, null);
  }

  @When("^I (try to )?create TP card parameters from file \"(.*)\"$")
  public void createNormal(Boolean tryTo, String fileName) {
    create(tryTo, fileName, null, null, null);
  }

  @When("^I (try to )?create manual TP parameters from file \"(.*)\" with date \"(.*)\"$")
  public void createManual(Boolean tryTo, String fileName, String dateExecutionBatch) {
    create(tryTo, fileName, dateExecutionBatch, null, null);
  }

  @When("^I (try to )?create an automatic TP card parameters from file \"(.*)\"$")
  public void createAutomatic(Boolean tryTo, String fileName) {
    String todayEcheance = DateTimeFormatter.ofPattern("dd/MM").format(LocalDate.now());
    create(tryTo, fileName, null, todayEcheance, null);
  }

  private void create(
      Boolean tryTo,
      ParametrageCarteTP param,
      String dateExecutionBatch,
      String debutEcheance,
      Integer delaiDeclenchementCarteTP) {
    TryToUtils.tryTo(
        () -> {
          if (dateExecutionBatch != null) {
            param.getParametrageRenouvellement().setDateExecutionBatch(dateExecutionBatch);
          }
          if (debutEcheance != null) {
            param.getParametrageRenouvellement().setDebutEcheance(debutEcheance);
          }
          if (delaiDeclenchementCarteTP != null) {
            param
                .getParametrageRenouvellement()
                .setDelaiDeclenchementCarteTP(delaiDeclenchementCarteTP);
          }
          parametrageCarteTPService.create(param);
        },
        tryTo);
  }

  private void create(
      Boolean tryTo,
      String fileName,
      String dateExecutionBatch,
      String debutEcheance,
      Integer delaiDeclenchementCarteTP) {
    ParametrageCarteTP param =
        FileUtils.readRequestFile(String.format(PATH, fileName), ParametrageCarteTP.class);
    create(tryTo, param, dateExecutionBatch, debutEcheance, delaiDeclenchementCarteTP);
  }

  @Given(
      "^I (try to )?create a birthday TP card parameters on \"(.*)\" on next year from file \"(.*)\"$")
  public void createBirthday(Boolean tryTo, String birthday, String fileName) {
    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();
    String nextRenewal = currentYear + "/" + birthday;

    LocalDate nextRenewalDate = LocalDate.parse(nextRenewal, DateUtils.YYYY_MM_DD);
    long diffDays = ChronoUnit.DAYS.between(today, nextRenewalDate);

    if (diffDays < 0) {
      int nextYear = currentYear + 1;
      nextRenewal = nextYear + "/" + birthday;
      nextRenewalDate = LocalDate.parse(nextRenewal, DateUtils.YYYY_MM_DD);
      diffDays = ChronoUnit.DAYS.between(today, nextRenewalDate);
    }

    int intDays = (int) diffDays;
    if (DateUtils.isLeapYear(currentYear) && today.getMonthValue() < 2) {
      intDays--;
    }

    create(tryTo, fileName, null, null, intDays);
  }

  @Given("^I (try to )?create an automatic TP card parameters on next year from file \"(.*)\"$")
  public void creatAutomaticNextYear(Boolean tryTo, String fileName) {
    ParametrageCarteTP param =
        FileUtils.readRequestFile(String.format(PATH, fileName), ParametrageCarteTP.class);
    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();
    int lastYear = currentYear - 1;
    int nextYear = currentYear + 1;

    String nextRenouvellement =
        param.getParametrageRenouvellement().getDebutEcheance() + "/" + nextYear;

    LocalDate nextRenouvellementDate = LocalDate.parse(nextRenouvellement, DD_MM_YYYY);
    long diffDays = ChronoUnit.DAYS.between(today, nextRenouvellementDate);

    int intDays = (int) diffDays;
    if (DateUtils.isLeapYear(currentYear) && today.getMonthValue() < 3) {
      intDays--;
    } else if (DateUtils.isLeapYear(lastYear) && diffDays >= 307) {
      // from 01/01/2025 to 28/02/2025, because last year was a leap !
      intDays++;
    }

    create(tryTo, param, null, null, intDays);
  }
}
