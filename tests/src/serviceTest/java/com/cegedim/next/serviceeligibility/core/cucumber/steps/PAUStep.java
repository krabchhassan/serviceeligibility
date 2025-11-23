package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TransformUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import io.cucumber.core.exception.CucumberException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
public class PAUStep {

  public static final String INSURER_ID = "insurerId";
  public static final String START_DATE = "startDate";
  public static final String END_DATE = "endDate";
  public static final String SUBSCRIBER_ID = "subscriberId";
  public static final String BENEFICIARY_ID = "beneficiaryId";
  public static final String ISSUING_COMPANY_CODE = "issuingCompanyCode";
  public static final String BIRTH_DATE = "birthDate";
  public static final String BIRTH_RANK = "birthRank";
  public static final String NIR_CODE = "nirCode";
  public static final String IS_FORCED = "isForced";
  public static final String DOMAINS = "domains";
  public static final String CONTEXT = "context";

  private static final String PAU_BASE_URL =
      TestCommonStoreService.CORE_API + "/v5/beneficiaryContracts";

  private final TestCommonStoreService testCommonStoreService;

  private UniqueAccessPointResponse pauResponse;

  @When(
      "^I get contrat PAUV5 for (\\S*) '(\\S*)' (\\S*) '(\\S*)' '(\\S*)' (\\S*) (\\S*)(?: (\\S*))?(?: for domains (\\S*))?(?: for beneficiaryId (\\S*))?(?: for issuingCompanyCode (\\S*))?(?: and isForced (true))?")
  public void iGetContratPAUv5For(
      String nir,
      String dateNaissance,
      String rangNaissance,
      String debutSoins,
      String finSoins,
      String idDeclarant,
      String contexts,
      String numeroAdherent,
      String domains,
      String beneficiaryId,
      String issuingCompanyCode,
      Boolean isForced) {
    Map<String, String> params =
        this.buildPAUParamMap(
            nir,
            dateNaissance,
            rangNaissance,
            TransformUtils.DEFAULT.parse(debutSoins),
            TransformUtils.DEFAULT.parse(finSoins),
            idDeclarant,
            contexts,
            numeroAdherent,
            domains,
            beneficiaryId,
            issuingCompanyCode,
            isForced);
    getContratPAU(params);
  }

  @When(
      "^I get contrat PAUV5 without endDate for (\\S*) '(\\S*)' (\\S*) '(\\S*)' (\\S*) (\\S*)(?: (\\S*))?(?: for domains (\\S*))?(?: for beneficiaryId (\\S*))?(?: for issuingCompanyCode (\\S*))?(?: and isForced (true))?")
  public void iGetContratPAUv5ForWithoutEndDate(
      String nir,
      String dateNaissance,
      String rangNaissance,
      String debutSoins,
      String idDeclarant,
      String contexts,
      String numeroAdherent,
      String domains,
      String beneficiaryId,
      String issuingCompanyCode,
      Boolean isForced) {
    Map<String, String> params =
        this.buildPAUParamMap(
            nir,
            dateNaissance,
            rangNaissance,
            debutSoins,
            null,
            idDeclarant,
            contexts,
            numeroAdherent,
            domains,
            beneficiaryId,
            issuingCompanyCode,
            isForced);
    getContratPAU(params);
  }

  @When(
      "^I get contrat PAUV5 without Benef Info for '(\\S*)' '(\\S*)' (\\S*) (\\S*)(?: (\\S*))?(?: for domains (\\S*))?(?: for issuingCompanyCode (\\S*))?(?: and isForced (true))?")
  public void iGetContratPAUv5ForWithoutBenefInfo(
      String debutSoins,
      String finSoins,
      String idDeclarant,
      String contexts,
      String numeroAdherent,
      String domains,
      String issuingCompanyCode,
      Boolean isForced) {
    Map<String, String> params =
        this.buildPAUParamMap(
            null,
            null,
            null,
            debutSoins,
            finSoins,
            idDeclarant,
            contexts,
            numeroAdherent,
            domains,
            null,
            issuingCompanyCode,
            isForced);
    getContratPAU(params);
  }

  @When("^I get contrat PAUV5 for '(\\S*)' (\\S*) '(\\S*)' '(\\S*)' (\\S*) (\\S*) (\\S*)?")
  public void iGetContratPAUv5For(
      String dateNaissance,
      String rangNaissance,
      String debutSoins,
      String finSoins,
      String idDeclarant,
      String contexts,
      String numeroAdherent) {
    Map<String, String> params =
        this.buildPAUParamMap(
            null,
            dateNaissance,
            rangNaissance,
            debutSoins,
            finSoins,
            idDeclarant,
            contexts,
            numeroAdherent,
            null,
            null,
            null,
            null);
    getContratPAU(params);
  }

  @When("^I get contrat PAUV5 for '(\\S*)' (\\S*) '(\\S*)' '(\\S*)' (\\S*) (\\S*)?")
  public void iGetContratPAUv5For(
      String dateNaissance,
      String rangNaissance,
      String debutSoins,
      String finSoins,
      String idDeclarant,
      String contexts) {
    Map<String, String> params =
        this.buildPAUParamMap(
            null,
            dateNaissance,
            rangNaissance,
            debutSoins,
            finSoins,
            idDeclarant,
            contexts,
            null,
            null,
            null,
            null,
            null);
    getContratPAU(params);
  }

  @When("^I get contrat PAUV5 for (\\S*) '(\\S*)' '(\\S*)' (\\S*) (\\S*)?")
  public void iGetContratPAUv5ForWithoutBirth(
      String nir, String debutSoins, String finSoins, String idDeclarant, String contexts) {
    Map<String, String> params =
        this.buildPAUParamMap(
            nir,
            null,
            null,
            debutSoins,
            finSoins,
            idDeclarant,
            contexts,
            null,
            null,
            null,
            null,
            null);
    getContratPAU(params);
  }

  @When("^I get contrat PAUV5 for '(\\S*)' '(\\S*)' (\\S*) (\\S*) and numAdherent (\\S*)?")
  public void iGetContratPAUv5For(
      String debutSoins,
      String finSoins,
      String idDeclarant,
      String contexts,
      String numeroAdherent) {
    Map<String, String> params =
        this.buildPAUParamMap(
            null,
            null,
            null,
            debutSoins,
            finSoins,
            idDeclarant,
            contexts,
            numeroAdherent,
            null,
            null,
            null,
            null);
    getContratPAU(params);
  }

  @Then("we found {int} contracts")
  public void wefoundcontracts(int nombre) {
    Assertions.assertEquals(nombre, this.pauResponse.getContracts().size());
  }

  @Then("^the contract(?: with indice (\\d+))? is (not )?forced$")
  public void theContractIsNotForced(Integer indice, Boolean forced) {
    GenericRightDto genericRightDto =
        this.pauResponse.getContracts().get(Objects.requireNonNullElse(indice, 0));
    if (forced == null) {
      Assertions.assertTrue(genericRightDto.getIsForced());
    } else {
      Assertions.assertFalse(genericRightDto.getIsForced());
    }
  }

  @Then("With this request I have this list of numeroPersonnes")
  public void checkPersonne(DataTable table) {
    // TODO #todotests
  }

  @Then("With this request I have this adherent {string}")
  public void checkAdherent(String adherent) {
    // TODO #todotests
  }

  @Then("the pau is identical to {string} content")
  public void thePauIsIdenticalTo(String fileName) {
    UniqueAccessPointResponse expected =
        FileUtils.readResultFile(fileName + ".json", UniqueAccessPointResponse.class);
    assertThat(expected).usingRecursiveComparison().isEqualTo(this.pauResponse);
  }

  @Then(
      "^the contract (?:number (\\d+) )?(?:and the right (\\d+) )?(?:and the product (\\d+) )?data has values")
  public void theContractNumberHasValues(
      Integer indice, Integer rightNumber, Integer productNumber, DataTable table) {
    Map<String, String> expected = table.asMap();
    GenericRightDto received =
        indice != null
            ? this.pauResponse.getContracts().get(indice)
            : this.pauResponse.getContracts().get(0);
    if (expected.containsKey(INSURER_ID)) {
      Assertions.assertEquals(expected.get(INSURER_ID), received.getInsurerId());
    }
    if (expected.containsKey("suspensionPeriods")) {
      Assertions.assertNull(received.getSuspensionPeriods());
    }
    if (expected.containsKey("number")) {
      Assertions.assertEquals(expected.get("number"), received.getNumber());
    }
    if (expected.containsKey("subscriberId")) {
      Assertions.assertEquals(expected.get("subscriberId"), received.getSubscriberId());
    }
    Right rightReceived =
        rightNumber != null
            ? received.getInsured().getRights().get(rightNumber)
            : received.getInsured().getRights().get(0);
    checkRightValues(expected, rightReceived);

    if (CollectionUtils.isNotEmpty(rightReceived.getProducts())) {
      Product productReceived =
          productNumber != null
              ? rightReceived.getProducts().get(productNumber)
              : rightReceived.getProducts().get(0);
      if (expected.containsKey("productCode")) {
        Assertions.assertEquals(expected.get("productCode"), productReceived.getProductCode());
      }
      if (expected.containsKey("offerCode")) {
        Assertions.assertEquals(expected.get("offerCode"), productReceived.getOfferCode());
      }
    }
  }

  @Then("the contract number {int} data has this personNumber")
  public void theContractHasthisPerson(int indice, DataTable table) {
    Map<String, String> expected = table.asMap();
    GenericRightDto contract = this.pauResponse.getContracts().get(indice);
    Assertions.assertEquals(
        expected.get("personNumber"), contract.getInsured().getIdentity().getPersonNumber());
  }

  @Then("the contract number {int} hasn't companyName in collectiveContract")
  public void the_contract_number_hasn_t_company_name_in_collective_contract(int number) {
    CollectiveContractV5 collectiveContractV5 =
        this.pauResponse.getContracts().get(number).getCollectiveContract();
    Assertions.assertNull(collectiveContractV5.getCompanyName());
  }

  private static void checkRightValues(Map<String, String> expected, Right rightReceived) {
    if (expected.containsKey("nombreProduit")) {
      Assertions.assertEquals(
          Integer.parseInt(expected.get("nombreProduit")), rightReceived.getProducts().size());
    }
    if (expected.containsKey("codeGT")) {
      Assertions.assertEquals(expected.get("codeGT"), rightReceived.getCode());
    }
    if (expected.containsKey("insurerCode")) {
      Assertions.assertEquals(expected.get("insurerCode"), rightReceived.getInsurerCode());
    }
    if (expected.containsKey("originCode")) {
      if ("null".equals(expected.get("originCode"))) {
        Assertions.assertNull(rightReceived.getOriginCode());
      } else {
        Assertions.assertEquals(expected.get("originCode"), rightReceived.getOriginCode());
      }
    }
    if (expected.containsKey("originInsurerCode")) {
      if ("null".equals(expected.get("originInsurerCode"))) {
        Assertions.assertNull(rightReceived.getOriginInsurerCode());
      } else {
        Assertions.assertEquals(
            expected.get("originInsurerCode"), rightReceived.getOriginInsurerCode());
      }
    }
    if (expected.containsKey("waitingCode")) {
      if ("null".equals(expected.get("waitingCode"))) {
        Assertions.assertNull(rightReceived.getWaitingCode());
      } else {
        Assertions.assertEquals(expected.get("waitingCode"), rightReceived.getWaitingCode());
      }
    }
  }

  @Then("^the (\\d+) suspensions in contract data has values")
  public void theSuspensionInContractDataHasValues(int nombre, List<Map<String, String>> table) {
    Assertions.assertEquals(
        nombre, pauResponse.getContracts().get(0).getSuspensionPeriods().size());
    List<SuspensionPeriod> suspensionPeriodsReceived =
        pauResponse.getContracts().get(0).getSuspensionPeriods();
    for (int i = 0; i < table.size(); i++) {
      Assertions.assertEquals(
          table.get(i).get("suspensionStart"),
          suspensionPeriodsReceived.get(i).getPeriod().getStart());
      if ("null".equals(table.get(i).get("suspensionEnd"))) {
        Assertions.assertNull(suspensionPeriodsReceived.get(i).getPeriod().getEnd());
      } else {
        Assertions.assertEquals(
            table.get(i).get("suspensionEnd"),
            suspensionPeriodsReceived.get(i).getPeriod().getEnd());
      }
      Assertions.assertEquals(
          table.get(i).get("suspensionType"), suspensionPeriodsReceived.get(i).getSuspensionType());
      // Seulement en HTP :
      if ("null".equals((table.get(i).get("suspensionReason")))) {
        Assertions.assertNull(suspensionPeriodsReceived.get(i).getSuspensionReason());
      } else {
        Assertions.assertEquals(
            table.get(i).get("suspensionReason"),
            suspensionPeriodsReceived.get(i).getSuspensionReason());
      }
      if ("null".equals((table.get(i).get("suspensionRemovalReason")))) {
        Assertions.assertNull(suspensionPeriodsReceived.get(i).getSuspensionRemovalReason());
      } else {
        Assertions.assertEquals(
            table.get(i).get("suspensionRemovalReason"),
            suspensionPeriodsReceived.get(i).getSuspensionRemovalReason());
      }
    }
  }

  @Then(
      "^the contract(?: with indice (\\d+))? and the right (\\d+) data has this period on the product (\\d+)")
  public void theContractAndTheRightDataHasThisPeriodOnTheProduct(
      Integer indice, int rightNumber, int productNumber, Map<String, String> table) {
    GenericRightDto received =
        indice != null
            ? this.pauResponse.getContracts().get(indice)
            : this.pauResponse.getContracts().get(0);
    Product productReceived =
        received.getInsured().getRights().get(rightNumber).getProducts().get(productNumber);
    if (table.containsKey(INSURER_ID)) {
      Assertions.assertEquals(table.get(INSURER_ID), received.getInsurerId());
    }
    if (table.containsKey("periodDebut")) {
      Assertions.assertEquals(table.get("periodDebut"), productReceived.getPeriod().getStart());
    }
    if (table.containsKey("periodFin")) {
      if ("null".equals((table.get("periodFin")))) {
        Assertions.assertNull(productReceived.getPeriod().getEnd());
      } else {
        Assertions.assertEquals(table.get("periodFin"), productReceived.getPeriod().getEnd());
      }
    }
    if (table.containsKey("nombreBenefit")) {
      Assertions.assertEquals(
          Integer.parseInt(table.get("nombreBenefit")), productReceived.getBenefitsType().size());
    }
  }

  @Then("the pau response has {int} contract(s) with these values")
  public void thePauResponseHasContractsWithTheseValues(int nbContract, DataTable table) {
    Assertions.assertEquals(nbContract, this.pauResponse.getContracts().size());
    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
    for (int i = 0; i < rows.size(); i++) {
      if (rows.get(i).containsKey("nir")) {
        Assertions.assertEquals(
            rows.get(i).get("nir"),
            this.pauResponse.getContracts().get(i).getInsured().getIdentity().getNir().getCode());
      }
      if (rows.get(i).containsKey("number")) {
        Assertions.assertEquals(
            rows.get(i).get("number"), this.pauResponse.getContracts().get(i).getNumber());
      }
    }
  }

  @Then("^the contract number (\\d+) has these collectiveContract values")
  public void theContractNumberHasTheseCollectiveContractValues(
      int indice, Map<String, String> expected) {
    CollectiveContract received =
        this.pauResponse.getContracts().get(indice).getCollectiveContract();
    if (expected.containsKey("number")) {
      Assertions.assertEquals(expected.get("number"), received.getNumber());
    }
    if (expected.containsKey("externalNumber")) {
      Assertions.assertEquals(expected.get("externalNumber"), received.getExternalNumber());
    }
  }

  private record Benefit(String code, String debut, String fin) {}

  @Then(
      "^there is (\\d+) benefitType for the right (\\d+) and the different benefitType has this values")
  public void thereIsBenefitTypeForTheRightAndTheDifferentBenefitTypeHasThisValues(
      int nbBenefitsType, int rightNumber, DataTable table) {
    GenericRightDto contract = this.pauResponse.getContracts().get(0);
    Product product = contract.getInsured().getRights().get(rightNumber).getProducts().get(0);
    Assertions.assertEquals(nbBenefitsType, product.getBenefitsType().size());
    List<BenefitType> benefitTypeList = product.getBenefitsType().stream().toList();
    List<Benefit> benefitList = new ArrayList<>();
    for (int i = 0; i < nbBenefitsType; i += 1) {
      String code = benefitTypeList.get(i).getBenefitType();
      String debut = benefitTypeList.get(i).getPeriod().getStart();
      String fin = benefitTypeList.get(i).getPeriod().getEnd();
      benefitList.add(new Benefit(code, debut, fin));
    }
    benefitList.sort(
        (a, b) -> {
          String premier = a.code + a.debut;
          String deuxieme = b.code + b.debut;
          return premier.compareTo(deuxieme);
        });

    List<Map<String, String>> expectedRightElements =
        TransformUtils.DEFAULT.parseMaps(table.asMaps());
    int i = 0;
    for (Benefit element : benefitList) {
      Assertions.assertEquals(expectedRightElements.get(i).get("code"), element.code);
      Assertions.assertEquals(expectedRightElements.get(i).get("debut"), element.debut);
      Assertions.assertEquals(expectedRightElements.get(i).get("fin"), element.fin);
      i += 1;
    }
  }

  private void throwBadUsage() {
    throw new CucumberException("Prefer the use of theSuspensionInContractDataHasValues()");
  }

  private void getContratPAU(Map<String, String> params) {
    try {
      testCommonStoreService.setHttpClientErrorException(Optional.empty());
      ResponseEntity<UniqueAccessPointResponse> response =
          testCommonStoreService.getToGivenURL(
              PAU_BASE_URL, params, TypeUtils.simpleParamType(UniqueAccessPointResponse.class));
      Assertions.assertNotNull(response);
      Assertions.assertNotNull(response.getBody());
      pauResponse = response.getBody();
    } catch (Throwable e) {
      pauResponse = null;
      if (e instanceof HttpClientErrorException) {
        testCommonStoreService.setHttpClientErrorException(
            Optional.of((HttpClientErrorException) e));
      }
    }
  }

  private Map<String, String> buildPAUParamMap(
      String nir,
      String dateNaissance,
      String rangNaissance,
      String debutSoins,
      String finSoins,
      String idDeclarant,
      String contexts,
      String numeroAdherent,
      String domains,
      String beneficiaryId,
      String issuingCompanyCode,
      Boolean isForced) {
    Map<String, String> params = new HashMap<>();
    checkParameterAndAddToParamMap(params, INSURER_ID, idDeclarant);
    checkParameterAndAddToParamMap(params, START_DATE, debutSoins);
    checkParameterAndAddToParamMap(params, CONTEXT, contexts);
    checkParameterAndAddToParamMap(params, END_DATE, finSoins);
    checkParameterAndAddToParamMap(params, SUBSCRIBER_ID, numeroAdherent);
    checkParameterAndAddToParamMap(params, DOMAINS, domains);
    checkParameterAndAddToParamMap(params, BENEFICIARY_ID, beneficiaryId);
    checkParameterAndAddToParamMap(params, ISSUING_COMPANY_CODE, issuingCompanyCode);
    checkParameterAndAddToParamMap(params, BIRTH_DATE, dateNaissance);
    checkParameterAndAddToParamMap(params, BIRTH_RANK, rangNaissance);
    checkParameterAndAddToParamMap(params, NIR_CODE, nir);
    if (isForced != null) {
      params.put(IS_FORCED, String.valueOf(isForced));
    }
    return params;
  }

  private void checkParameterAndAddToParamMap(
      Map<String, String> params, String param, String value) {
    if (StringUtils.isNotBlank(value)) {
      params.put(param, value);
    }
  }

  @Then("^the suspension in contract data has values")
  public void theSuspensionInContractDataHasValues(Map<String, String> expected) {
    throwBadUsage();
    Assertions.assertEquals(1, pauResponse.getContracts().get(0).getSuspensionPeriods().size());
    SuspensionPeriod suspensionPeriodReceived =
        pauResponse.getContracts().get(0).getSuspensionPeriods().get(0);
    Assertions.assertEquals(
        expected.get("suspensionStart"), suspensionPeriodReceived.getPeriod().getStart());
    Assertions.assertEquals(
        expected.get("suspensionEnd"), suspensionPeriodReceived.getPeriod().getEnd());
    Assertions.assertEquals(
        expected.get("suspensionType"), suspensionPeriodReceived.getSuspensionType());
  }

  @Then("^the suspension in contract data has only start value")
  public void theSuspensionInContractDataHasOnlyStartValue(Map<String, String> expected) {
    throwBadUsage();
  }

  @Then("In the PAU, there is {int} contract")
  public void thereisXcontract(int nbre) {
    Assertions.assertEquals(nbre, pauResponse.getContracts().size());
  }

  @Then("^the benfitType (\\d+) of product (\\d+) of right (\\d+) data has values$")
  public void theBenfitTypeOfProductOfRightDataHasValues(
      int benefitNumber, int productNumber, int rightNumber, DataTable table) {
    Map<String, String> expected = table.asMap();
    List<BenefitType> benefitTypes =
        new ArrayList<>(
            pauResponse
                .getContracts()
                .get(0)
                .getInsured()
                .getRights()
                .get(rightNumber)
                .getProducts()
                .get(productNumber)
                .getBenefitsType());
    BenefitType benefitType = benefitTypes.get(benefitNumber);
    Assertions.assertEquals(expected.get("benefitType"), benefitType.getBenefitType());
    Assertions.assertEquals(expected.get("start"), benefitType.getPeriod().getStart());
    if ("null".equals(expected.get("end"))) {
      Assertions.assertNull(benefitType.getPeriod().getEnd());
    } else {
      Assertions.assertEquals(expected.get("end"), benefitType.getPeriod().getEnd());
    }
  }

  @Then(
      "^the product (\\d+) of the right (\\d+) has a benefitType (\\S*) with a convention (\\S*) in the domain (\\d+)$")
  public void testProduct(
      int productNumber, int rightNumber, String benefitType, String convention, int domainNumber) {
    Product product =
        pauResponse
            .getContracts()
            .get(0)
            .getInsured()
            .getRights()
            .get(rightNumber)
            .getProducts()
            .get(productNumber);
    AtomicReference<String> reponseConvention = new AtomicReference<>();
    product
        .getBenefitsType()
        .forEach(
            benefitType1 -> {
              if (benefitType.equals(benefitType1.getBenefitType())) {
                reponseConvention.set(benefitType1.getDomains().get(domainNumber).getConvention());
              }
            });
    Assertions.assertEquals(convention, reponseConvention.get());
  }
}
