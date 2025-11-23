package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestTriggerService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.RecursiveUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TransformUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TryToUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class TriggerStep {

  private static final Logger log = LoggerFactory.getLogger(TriggerStep.class);
  private final TriggerCreationService triggerCreationService;
  private final TestTriggerService testTriggerService;
  private final TestCommonStoreService testCommonStoreService;

  private List<Trigger> generatedTrigger;
  private Trigger trigger;
  private List<TriggeredBeneficiary> triggeredBeneficiarys;
  private TriggeredBeneficiary triggeredBeneficiary;

  private TriggerResponse triggerResponse;

  private static final String PATH = "triggers/%s.json";

  @When("^I renew the rights today(?: with mode \"(.*)\")?$")
  public void renewToday(String mode) throws InterruptedException {
    renewOnDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), mode);
  }

  @When("^I renew the rights on \"(.*)\" with mode \"(.*)\"$")
  public void renewOn(String date, String mode) throws InterruptedException {
    renewOnDate(TransformUtils.DEFAULT.parse(date), mode);
  }

  private void renewOnDate(String date, String mode) throws InterruptedException {
    boolean isRdo = Constants.BATCH_MODE_RDO.equals(mode);
    TriggerGenerationRequest req = new TriggerGenerationRequest();
    req.setEmitter(TriggerEmitter.Renewal);
    req.setDate(date);
    req.setRdo(isRdo);

    generatedTrigger = triggerCreationService.generateTriggers(req);
    TimeUnit.SECONDS.sleep(1);
  }

  @Given("I delete trigger and sas")
  public void deleteTriggerAndSas() {
    testTriggerService.deleteTriggerAndSas();
  }

  @Given("I drop the collection SasContract")
  public void deleteSas() {
    testTriggerService.deleteSas();
  }

  @Given("^I get triggers with contract number \"(.*)\" and amc \"(.*)\"$")
  public void getTriggersWithContractNumberAndAmc(String contractNumber, String amc) {
    TriggerResponse response =
        testTriggerService.getTriggers(amc, contractNumber, TriggerEmitter.Event);
    Assertions.assertNotNull(response);
    generatedTrigger = response.getTriggers();
    Assertions.assertNotNull(generatedTrigger);
    trigger = generatedTrigger.get(0);
  }

  @Given(
      "^I (try to )?get the first renewal trigger with contract number \"(.*)\" and amc \"(.*)\"$")
  public void getFirstRenewalTriggerWithContractNumberAndAmc(
      Boolean tryTo, String contractNumber, String amc) {
    TryToUtils.tryTo(
        () -> {
          TriggerResponse response =
              testTriggerService.getTriggers(amc, contractNumber, TriggerEmitter.Renewal);
          generatedTrigger = response.getTriggers();
          trigger = generatedTrigger.get(0);
        },
        tryTo);
  }

  @Given(
      "^I wait for the (last|first) (renewal )?trigger with contract number \"(.*)\" and amc \"(.*)\" to be \"(.*)\"")
  public void waitForRenewalTrigger(
      String last, Boolean renewal, String contractNumber, String amc, TriggerStatus wantedStatus)
      throws Exception {
    TriggerEmitter emitter = renewal != null ? TriggerEmitter.Renewal : TriggerEmitter.Event;
    Callable<TriggerResponse> call =
        () -> testTriggerService.getTriggers(amc, contractNumber, emitter);
    Predicate<TriggerResponse> test =
        response -> wantedStatus.equals(response.getTriggers().getFirst().getStatus());
    generatedTrigger = RecursiveUtils.defaultRecursive(call, test).getTriggers();
    trigger = generatedTrigger.getFirst();
  }

  @Given(
      "^I get one more trigger with contract number \"(.*)\" and amc \"(.*)\" and indice \"(.*)\" for benef$")
  public void getOneMoreTrigger(String contractNumber, String amc, int indice) {
    TriggerResponse response =
        testTriggerService.getTriggers(
            amc, contractNumber, TriggerEmitter.Event, TriggerEmitter.Request);
    generatedTrigger = response.getTriggers();
    trigger = generatedTrigger.get(indice);
  }

  @Given("^I get (\\d) trigger with contract number \"(.*)\" and amc \"(.*)\"")
  public void get(int number, String contractNumber, String amc) {
    checkTriggerNumber(number, contractNumber, amc, TriggerEmitter.Event);
  }

  @Given("^I get (\\d) renewal trigger with contract number \"(.*)\" and amc \"(.*)\"")
  public void getRenewal(int number, String contractNumber, String amc) {
    checkTriggerNumber(number, contractNumber, amc, TriggerEmitter.Renewal);
  }

  private void checkTriggerNumber(
      int number, String contractNumber, String amc, TriggerEmitter emitter) {
    TriggerResponse response = testTriggerService.getTriggers(amc, contractNumber, emitter);
    generatedTrigger = response.getTriggers();
    if (number == 0) {
      Assertions.assertTrue(CollectionUtils.isEmpty(generatedTrigger));
    } else {
      Assertions.assertFalse(CollectionUtils.isEmpty(generatedTrigger));
      Assertions.assertEquals(number, generatedTrigger.size());
    }
  }

  @Given("^I create a trigger by UI from \"(.*)\"$")
  public void createTriggerUI(String filePath) {
    TriggerGenerationRequest trigger =
        FileUtils.readRequestFile(
            "declencheursCarteTP/V1/" + filePath + FileUtils.JSON, TriggerGenerationRequest.class);
    ResponseEntity<List<Trigger>> response =
        testCommonStoreService.postToGivenURLWithGivenBody(
            TestCommonStoreService.CORE_API + "/v1/declencheursCarteTP",
            trigger,
            TypeUtils.listParamType(Trigger.class));
    Assertions.assertNotNull(response);
    // generatedTrigger = response.getBody();
  }

  @Then("^I get the triggerBenef on the trigger( with the index \"(.*)\")?$")
  public void getTriggerBenefOnTrigger(Integer index) {
    index = index != null ? index : 0;
    Assertions.assertNotNull(trigger);
    List<TriggeredBeneficiary> benefs =
        testTriggerService.getTriggerBenefOnTrigger(trigger.getId());
    Assertions.assertNotNull(benefs);
    Assertions.assertTrue(benefs.size() > index);
    triggeredBeneficiarys = benefs;
    triggeredBeneficiary = triggeredBeneficiarys.get(index);
  }

  @Then("^I get \"(.*)\" suspension periods on the trigger$")
  public void getSuspensionPeriodsOnTrigger(int nbSuspensionPeriods) {
    // TODO #todotests A revoir les periodes de suspension ne sont plus a la racine du trigger
    // benef");
  }

  @Then("^the triggerBenef has this values$")
  public void triggerBenefHasThisValues(DataTable table) {
    Map<String, String> body = TransformUtils.DEFAULT.parseMap(table.asMap());
    if (body.containsKey("derniereAnomalie")) {
      Assertions.assertEquals(
          body.get("derniereAnomalie"),
          triggeredBeneficiary.getDerniereAnomalie().getDescription());
    }
    Assertions.assertEquals(body.get("statut"), triggeredBeneficiary.getStatut().name());
    if (triggeredBeneficiary.getNir() != null) {
      Assertions.assertEquals(body.get("nir"), triggeredBeneficiary.getNir());
    } else {
      Assertions.assertEquals(body.get("nir"), "null");
    }
    Assertions.assertEquals(body.get("numeroPersonne"), triggeredBeneficiary.getNumeroPersonne());
    if (body.containsKey("parametrageCarteTPId")) {
      Assertions.assertEquals(
          body.get("parametrageCarteTPId"), triggeredBeneficiary.getParametrageCarteTPId());
    }
  }

  @Then("^the trigger of indice \"(.*)\" has this values$")
  public void triggerHasThisValues(int index, DataTable table) {
    Map<String, String> body = table.asMap();
    trigger = generatedTrigger.get(index);

    TriggerStatus triggerStatus = EnumUtils.getEnum(TriggerStatus.class, body.get("status"));
    Assertions.assertEquals(triggerStatus, trigger.getStatus());
    Assertions.assertEquals(body.get("amc"), trigger.getAmc());

    if (body.get("nbBenef") != null) {
      Assertions.assertEquals(Integer.valueOf(body.get("nbBenef")), trigger.getNbBenef());
    }

    if (body.get("nbBenefKO") != null) {
      Assertions.assertEquals(Integer.valueOf(body.get("nbBenefKO")), trigger.getNbBenefKO());
    }

    if (body.get("nbBenefWarning") != null) {
      Assertions.assertEquals(
          Integer.valueOf(body.get("nbBenefWarning")), trigger.getNbBenefWarning());
    }
  }

  @Given("^I remove all triggers$")
  public void removeAllTriggers() {
    testTriggerService.deleteTriggerAndSas();
  }

  @Given("^I insert a new trigger from \"(.*)\"$")
  public void createTriggerFromFile(String filePath) {
    Trigger trigger =
        FileUtils.readRequestFile("triggers/" + filePath + FileUtils.JSON, Trigger.class);
    testTriggerService.createTrigger(trigger);
  }

  @Given("^I insert a new trigger beneficiary from \"(.*)\"$")
  public void createTriggerBeneficiaryFromFile(String filePath) {
    TriggeredBeneficiary triggeredBeneficiary1 =
        FileUtils.readRequestFile(
            "triggerBeneficiaries/" + filePath + FileUtils.JSON, TriggeredBeneficiary.class);
    testTriggerService.createTriggerBeneficiary(triggeredBeneficiary1);
  }

  @When("^I search a trigger with values (.*), (.*), (.*), (.*), (.*), (.*) and (.*)$")
  public void searchTrigger(
      String amcs,
      String status,
      String emitters,
      String dateDebut,
      String dateFin,
      String numeroContrat,
      String nir) {
    Map<String, Object> uriVar = new HashMap<>();
    if (StringUtils.isNotBlank(emitters)) {
      uriVar.put("emitters", EnumUtils.getEnum(TriggerEmitter.class, emitters));
    }
    if (StringUtils.isNotBlank(status)) {
      uriVar.put("status", EnumUtils.getEnum(TriggerStatus.class, status));
    }
    if (StringUtils.isNotBlank(dateDebut)) {
      uriVar.put("dateDebut", dateDebut);
    }
    if (StringUtils.isNotBlank(dateFin)) {
      uriVar.put("dateFin", dateFin);
    }
    if (StringUtils.isNotBlank(numeroContrat)) {
      uriVar.put("numeroContrat", numeroContrat);
    }
    if (StringUtils.isNotBlank(nir)) {
      uriVar.put("nir", nir);
    }
    if (StringUtils.isNotBlank(amcs)) {
      uriVar.put("amcs", amcs);
    }
    uriVar.put("page", 1);
    uriVar.put("perPage", 10);
    uriVar.put("isContratIndividuel", true);

    ResponseEntity<TriggerResponse> response =
        testCommonStoreService.getToGivenURL(
            TestCommonStoreService.CORE_API + "/v1/declencheursCarteTP",
            uriVar,
            TypeUtils.simpleParamType(TriggerResponse.class));
    Assertions.assertNotNull(response);
    triggerResponse = response.getBody();
  }

  @Then("^The trigger's search response is identical to \"(.*)\"$")
  public void theExpectedTriggerIsIdenticalTo(String content) {
    checkTrigger(content);
  }

  private void checkTrigger(String content, TransformUtils.Parser... parsers) {
    TriggerResponse expected =
        FileUtils.readResultFile(String.format(PATH, content), TriggerResponse.class, parsers);
    removeDynamicValues(triggerResponse);
    removeDynamicValues(expected);
    assertThat(expected).usingRecursiveComparison().isEqualTo(triggerResponse);
  }

  private void removeDynamicValues(TriggerResponse triggerResponse) {
    for (Trigger trigger : triggerResponse.getTriggers()) {
      trigger.setDateModification(null);
      trigger.setDateCreation(null);
    }
  }

  @Given("I recycle the trigger")
  public void iRecycleTheTrigger() {
    ResponseEntity<Void> response =
        testCommonStoreService.putToGivenURLWithGivenBody(
            TestCommonStoreService.CORE_API
                + "/v1/declencheursCarteTP/"
                + trigger.getId()
                + "/statut/ToProcess",
            null,
            TypeUtils.simpleParamType(Void.class));
    Assertions.assertNotNull(response);
  }

  @Given("I abandon the trigger")
  public void iAbandonTheTrigger() {
    ResponseEntity<Void> response =
        testCommonStoreService.putToGivenURLWithGivenBody(
            TestCommonStoreService.CORE_API
                + "/v1/declencheursCarteTP/"
                + trigger.getId()
                + "/statut/Abandoning",
            null,
            TypeUtils.simpleParamType(Void.class));
    Assertions.assertNotNull(response);
  }

  @Then("^The SasContrat for the current Trigger contains (\\d+) triggerBenefs$")
  public void theSasContratForTheCurrentTriggerContains(int nbToCheck) {
    int nbTriggerBenef = testTriggerService.getNbTriggerBenefOnSasByIdTrigger(trigger.getId());
    Assertions.assertEquals(nbToCheck, nbTriggerBenef);
  }

  @Then("^No sasContrat found for the contract \"(.*)\"$")
  public void noSasContractFound(String contractNumber) {
    List<SasContrat> sasContratList =
        testTriggerService.getSasContratByContractNumber(contractNumber);
    Assertions.assertEquals(Collections.emptyList(), sasContratList);
  }

  @Then("^I wait for the SasContrat of the current Trigger (not )?to be recycling$")
  public void iWaitForTheCurrentTriggerNotToBeRecycling(Boolean notRecycling) throws Exception {
    Callable<SasContrat> call = () -> testTriggerService.getSasContrat(trigger.getId());
    Predicate<SasContrat> test =
        (response -> {
          if (response == null) {
            return false;
          }
          boolean wantedRecycling;
          if (notRecycling == null) {
            wantedRecycling = true;
          } else {
            wantedRecycling = !notRecycling;
          }

          return response.isRecycling() == wantedRecycling;
        });
    RecursiveUtils.defaultRecursive(call, test);
  }
}
