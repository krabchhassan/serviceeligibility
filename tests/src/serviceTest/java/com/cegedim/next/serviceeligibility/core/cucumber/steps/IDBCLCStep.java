package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.consultation_droit.GetInfoBddResponseDto;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TypeUtils;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.webservices.clc.CLCCompleteResponse;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeBeneficiaire;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeContrat;
import com.cegedim.next.serviceeligibility.core.webservices.idb_clc.TypeHistoriqueAffiliation;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.DeclarantAmc;
import com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs.PeriodeWithDateRenouv;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@RequiredArgsConstructor
public class IDBCLCStep {

  private final TestCommonStoreService testCommonStoreService;

  private static final String IDB_URL =
      TestCommonStoreService.CORE_API + "/v1/interrogationDroitsBenefs";
  private static final String CLC_URL = TestCommonStoreService.CORE_API + "/v1/calcul";
  private static final String CONSULT_V4 =
      TestCommonStoreService.CORE_API + "/v4/consultationDroits";

  private Object response = null;

  //  private static final class CustomHttpComponentsClientHttpRequestFactory
  //      extends HttpComponentsClientHttpRequestFactory {
  //
  //    @NotNull
  //    @Override
  //    protected ClassicHttpRequest createHttpUriRequest(
  //        @NotNull HttpMethod httpMethod, @NotNull URI uri) {
  //
  //      if (HttpMethod.GET.equals(httpMethod)) {
  //        return new HttpEntityEnclosingGetRequestBase(uri);
  //      }
  //      return super.createHttpUriRequest(httpMethod, uri);
  //    }
  //  }
  //
  //  private static final class HttpEntityEnclosingGetRequestBase
  //      extends HttpEntityEnclosingRequestBase {
  //
  //    public HttpEntityEnclosingGetRequestBase(final URI uri) {
  //      super.setURI(uri);
  //    }
  //
  //    @Override
  //    public String getMethod() {
  //      return HttpMethod.GET.name();
  //    }
  //  }

  @Data
  private static final class IDBResponseForTest {
    private DeclarantAmc declarantAmc;
    private TypeBeneficiaire beneficiaire;
    private List<PeriodeWithDateRenouv> periodesDroits;
    private TypeContrat contrat;
  }

  @Data
  private static final class IDBCompleteResponseForTest {
    private TypeCodeReponse codeReponse;
    private List<IDBResponseForTest> droits;
  }

  @Then("^I post rest request from file \"(.*)\" to the \"(.*)\" endpoint")
  public void postRestRequestFromFileToIDBOrCLCEndpoint(String fileName, String endpointName) {
    testCommonStoreService
        .getRestTemplate()
        .setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    String url;
    switch (endpointName) {
      case "IDB" -> {
        url = IDB_URL;
        ResponseEntity<IDBCompleteResponseForTest> responseIDB =
            testCommonStoreService.getToGivenURLWithGivenBody(
                url,
                FileUtils.readRequestFile(fileName + ".json", GetInfoBddRequestDto.class),
                TypeUtils.simpleParamType(IDBCompleteResponseForTest.class));
        if (responseIDB != null) {
          response = responseIDB.getBody();
        }
      }
      case "CLC" -> {
        url = CLC_URL;
        ResponseEntity<CLCCompleteResponse> responseCLC =
            testCommonStoreService.getToGivenURLWithGivenBody(
                url,
                FileUtils.readRequestFile(fileName + ".json", GetInfoBddRequestDto.class),
                TypeUtils.simpleParamType(CLCCompleteResponse.class));
        if (responseCLC != null) {
          response = responseCLC.getBody();
        }
      }
      case "V4" -> {
        url = CONSULT_V4;
        ResponseEntity<GetInfoBddResponseDto> responseV4 =
            testCommonStoreService.getToGivenURLWithGivenBody(
                url,
                FileUtils.readRequestFile(fileName + ".json", GetInfoBddRequestDto.class),
                TypeUtils.simpleParamType(GetInfoBddResponseDto.class));
        if (responseV4 != null) {
          response = responseV4.getBody();
        }
      }
      default -> Assertions.assertEquals("Endpoint inconu", endpointName);
    }
  }

  @And("the expected response is identical to {string} content")
  public void theExpectedResponseIsIdenticalTo(String fileName) {
    Object expected = FileUtils.readResultFile(fileName + ".json", response.getClass());
    assertThat(expected).usingRecursiveComparison().isEqualTo(response);
  }

  @And("the IDB dateRenouvellement is set according to dureeValidite {int}")
  public void theIDBDateRenouvellementIsSetAccordingToDureeValidite(int dureeValidite) {
    LocalDate today = com.cegedim.next.serviceeligibility.core.cucumber.utils.DateUtils.TODAY_DATE;
    LocalDate dateRenouvellementExpected = today.plusDays(dureeValidite);
    LocalDate dateRenouvellementReceived = getReceivedDateRenouvellementIDB();
    Assertions.assertEquals(dateRenouvellementExpected, dateRenouvellementReceived);
  }

  @And("the IDB dateRenouvellement is set according to periodeValidite {string}")
  public void theIDBDateRenouvellementIsSetAccordingToPeriodeValidite(String periodeValidite) {
    LocalDate today = com.cegedim.next.serviceeligibility.core.cucumber.utils.DateUtils.TODAY_DATE;
    LocalDate dateRenouvellementExpected = null;
    switch (periodeValidite) {
      case "debut":
        dateRenouvellementExpected = LocalDate.of(today.getYear(), today.getMonthValue() + 1, 1);
        break;
      case "milieu":
        int month = today.getMonthValue();
        if (today.getDayOfMonth() > 15) {
          month = month + 1;
        }
        dateRenouvellementExpected = LocalDate.of(today.getYear(), month, 15);
        break;
      case "fin":
        int day;
        if (today.getMonthValue() == 2) {
          if (today.isLeapYear()) {
            day = 29;
          } else {
            day = 28;
          }
        } else if (List.of(4, 6, 9, 11).contains(today.getMonthValue())) {
          day = 30;
        } else {
          day = 31;
        }
        dateRenouvellementExpected = LocalDate.of(today.getYear(), today.getMonthValue(), day);
        break;
    }
    LocalDate dateRenouvellementReceived = getReceivedDateRenouvellementIDB();
    Assertions.assertEquals(dateRenouvellementExpected, dateRenouvellementReceived);
  }

  @And("^The (?:IDB|CLC) response(?: with indice (\\d+))? has contrat")
  public void theResponseHasContrat(Integer indice, DataTable table) {
    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
    int index = indice != null ? indice : 0;
    TypeContrat contrat;
    if (response instanceof IDBCompleteResponseForTest) {
      contrat = ((IDBCompleteResponseForTest) response).droits.get(index).getContrat();
    } else {
      contrat = ((CLCCompleteResponse) response).getDroits().get(index).getContrat();
    }
    for (Map<String, String> columns : rows) {
      Assertions.assertEquals(
          Boolean.parseBoolean(columns.get("isContratResponsable")),
          contrat.getIsContratResponsable());
      if ("null".equals(columns.get("isContratCMU"))) {
        Assertions.assertNull(contrat.getIsContratCMU());
      } else {
        Assertions.assertEquals(
            Boolean.parseBoolean(columns.get("isContratCMU")), contrat.getIsContratCMU());
      }
      if ("null".equals(columns.get("situationParticuliere"))) {
        Assertions.assertNull(contrat.getSituationParticuliere());
      } else {
        Assertions.assertEquals(
            columns.get("situationParticuliere"), contrat.getSituationParticuliere());
      }
    }
  }

  @And("^The (?:IDB|CLC) response(?: with indice (\\d+))? has beneficiaire.historiqueAffiliations")
  public void theResponseHasHistoriqueAffiliations(Integer indice, DataTable table) {
    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
    int index = indice != null ? indice : 0;
    List<TypeHistoriqueAffiliation> typeHistoriqueAffiliations;
    if (response instanceof IDBCompleteResponseForTest) {
      typeHistoriqueAffiliations =
          ((IDBCompleteResponseForTest) response)
              .droits
              .get(index)
              .getBeneficiaire()
              .getHistoriqueAffiliations();
    } else {
      typeHistoriqueAffiliations =
          ((CLCCompleteResponse) response)
              .getDroits()
              .get(index)
              .getBeneficiaire()
              .getHistoriqueAffiliations();
    }
    for (Map<String, String> columns : rows) {
      for (TypeHistoriqueAffiliation typeHistoriqueAffiliation : typeHistoriqueAffiliations) {
        Assertions.assertTrue(
            Boolean.parseBoolean(columns.get("medecinTraitant")),
            typeHistoriqueAffiliation.getMedecinTraitant());
        if ("null".equals(columns.get("regimeParticulier"))) {
          Assertions.assertNull(typeHistoriqueAffiliation.getRegimeParticulier());
        } else {
          Assertions.assertEquals(
              columns.get("regimeParticulier"), typeHistoriqueAffiliation.getRegimeParticulier());
        }
      }
    }
  }

  private LocalDate getReceivedDateRenouvellementIDB() {
    IDBCompleteResponseForTest responseIDB = (IDBCompleteResponseForTest) this.response;
    PeriodeWithDateRenouv periodeWithDateRenouv =
        responseIDB.getDroits().get(0).getPeriodesDroits().get(0);
    return DateUtils.parse(periodeWithDateRenouv.getDateRenouvellement(), DateUtils.FORMATTER);
  }
}
