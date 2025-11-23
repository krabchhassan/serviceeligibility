package com.cegedim.next.serviceeligibility.core.services;

import static com.cegedim.next.serviceeligibility.core.services.PwService.PW_ERROR_MESSAGE_IS_NOT_MANAGING_TP_OFFLINE_CONTEXTS;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.Produit;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflineAndOnlinePW;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DroitsTPOfflinePW;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.PwException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class PwServiceMockTest {
  @Autowired PwService pwService;

  @SpyBean PwCachedService pwCachedService;

  @Autowired RestConnector restConnector;

  @SpyBean private BeyondPropertiesService beyondPropertiesService;

  @BeforeEach
  void before() {
    ReflectionTestUtils.setField(pwCachedService, "restConnector", restConnector);
  }

  @Test
  void shoulGetDetailGarantieFromPW() {

    JSONObject productWorkshopResponseMock = new JSONObject();
    productWorkshopResponseMock.put(Constants.PW_OFFER_CODE, "Offre1");
    JSONArray array = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("offerVersionCode", 1);
    jsonObject.put(Constants.PW_VALIDITY_DATE, "2021-01-01");
    jsonObject.put("engineVersion", "EngineVersion1");
    array.put(jsonObject);
    productWorkshopResponseMock.put("offerVersions", array);
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponseMock);
    JSONArray productWorkshopResponse =
        pwCachedService.getDetailGarantieTpRightsFromPWV4(
            "OC1", "Produit1", "2020-01-01", "2020-01-01");

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>)
                    (json1, typeOfT, context) ->
                        LocalDateTime.parse(
                            json1.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .create();
    DroitsTPOfflinePW droitsTPOfflinePW =
        gson.fromJson(
            String.valueOf(productWorkshopResponse.getJSONObject(0)), DroitsTPOfflinePW.class);
    Assertions.assertEquals("Offre1", droitsTPOfflinePW.getOfferCode());
    Assertions.assertEquals("1", droitsTPOfflinePW.getOfferVersionCode());
    Assertions.assertEquals("2021-01-01", droitsTPOfflinePW.getValidityDate());
    Assertions.assertEquals("EngineVersion1", droitsTPOfflinePW.getEngineVersion());
  }

  @Test
  // FAUX
  void shoulGetDetailGarantieFromPW2() throws IOException {
    JSONObject productWorkshopResponseMock =
        UtilsForTesting.parseJSONFile(
            "src/test/resources/ResponsePWTpRigthsNaturePrestations.json");
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponseMock);
    JSONArray productWorkshopResponse =
        pwCachedService.getDetailGarantieTpRightsFromPWV4(
            "OC1", "Produit1", "2020-01-01", "2020-01-01");

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>)
                    (json1, typeOfT, context) ->
                        LocalDateTime.parse(
                            json1.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .create();
    DroitsTPOfflineAndOnlinePW droitsTPPW =
        gson.fromJson(
            String.valueOf(productWorkshopResponse.getJSONObject(0)),
            DroitsTPOfflineAndOnlinePW.class);
    Assertions.assertEquals("DR_7413", droitsTPPW.getOfferCode());
    Assertions.assertEquals("2023_janv_01.1", droitsTPPW.getOfferVersionCode());
    Assertions.assertEquals("2023-01-01", droitsTPPW.getValidityDate());
    Assertions.assertEquals("33.0", droitsTPPW.getEngineVersion());
    Assertions.assertEquals(3, droitsTPPW.getDomains().size());
    Assertions.assertEquals("OPTI", droitsTPPW.getDomains().get(0).getDomainCode());
    Assertions.assertEquals("OPTIQUE", droitsTPPW.getDomains().get(0).getTpOnline().getNature());
    Assertions.assertEquals("OPTIQUE", droitsTPPW.getDomains().get(0).getTpOffline().getNature());
    Assertions.assertEquals("DENT", droitsTPPW.getDomains().get(1).getDomainCode());
    Assertions.assertEquals("DENTAIRE", droitsTPPW.getDomains().get(1).getTpOnline().getNature());
    Assertions.assertEquals("DENTAIRE", droitsTPPW.getDomains().get(1).getTpOffline().getNature());
    Assertions.assertEquals("PHAR", droitsTPPW.getDomains().get(2).getDomainCode());
    Assertions.assertEquals("PHARMACIE", droitsTPPW.getDomains().get(2).getTpOnline().getNature());
    Assertions.assertEquals("PHARMACIE", droitsTPPW.getDomains().get(2).getTpOffline().getNature());
  }

  @Test
  void shoulGetDetailGarantieFromPWAndFile() throws IOException {

    JSONObject productWorkshopResponseMock =
        UtilsForTesting.parseJSONFile(
            "src/test/resources/pw_test_blue_tpOfflineRights_Cas1_V4.json");

    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponseMock);
    JSONArray productWorkshopResponse =
        pwCachedService.getDetailGarantieTpRightsFromPWV4(
            "BALOO", "PDT_BASE1", "2020-01-01", "2020-01-01");

    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>)
                    (json1, typeOfT, context) ->
                        LocalDateTime.parse(
                            json1.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .create();
    DroitsTPOfflinePW droitsTPOfflinePW =
        gson.fromJson(
            String.valueOf(productWorkshopResponse.getJSONObject(0)), DroitsTPOfflinePW.class);
    Assertions.assertEquals("CAS1", droitsTPOfflinePW.getOfferCode());
    Assertions.assertEquals("22", droitsTPOfflinePW.getOfferVersionCode());
    Assertions.assertEquals("2022-01-01", droitsTPOfflinePW.getValidityDate());
    Assertions.assertEquals("1.0", droitsTPOfflinePW.getEngineVersion());
  }

  @Test
  void shoulGetErrorFromPW() throws PwException {

    List<String> errorsToFill = new ArrayList<>();
    Produit produit = new Produit();
    produit.setCode("codeProduit");
    produit.setCodeAmc("Oc");
    HttpHeaders headers = new HttpHeaders();

    JSONObject productWorkshopResponseMock1 = null;
    try {
      productWorkshopResponseMock1 =
          UtilsForTesting.parseJSONFile("src/test/resources/offersStructureWithErrors.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponseMock1);

    PwException pwException =
        Assertions.assertThrows(
            PwException.class,
            () ->
                pwService.getDroitsProductsWorkshop(
                    errorsToFill, produit, LocalDateTime.now().toString()));
    Assertions.assertEquals(
        Anomaly.PRODUCT_NOT_FOUND, pwException.getTriggeredBeneficiaryAnomaly().getAnomaly());

    // Error 00408_2
    JSONObject productWorkshopResponseMock2 = new JSONObject();
    JSONObject error2 = new JSONObject();
    String errorMessage =
        "Version XXX of offer YYY" + PW_ERROR_MESSAGE_IS_NOT_MANAGING_TP_OFFLINE_CONTEXTS;
    error2.put("error_code", Constants.NEXT_ENGINE_CORE_00408_2);
    error2.put("message", errorMessage);
    productWorkshopResponseMock2.put("errors", List.of(error2));
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponseMock2);

    List<DroitsTPOfflinePW> droitsTPOfflinePW2 =
        pwService.getDroitsProductsWorkshop(errorsToFill, produit, LocalDateTime.now().toString());
    Assertions.assertTrue(CollectionUtils.isEmpty(droitsTPOfflinePW2));

    Assertions.assertEquals(errorMessage, errorsToFill.get(0));
  }

  @Test
  void shouldGetDroitsPW() throws PwException {
    List<String> errorsToFill = new ArrayList<>();
    Produit produit = new Produit();
    produit.setCode("codeProduit");
    produit.setCodeAmc("Oc");
    JSONObject productWorkshopResponse = null;
    try {
      productWorkshopResponse =
          UtilsForTesting.parseJSONFile("src/test/resources/offersStructure2.json");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Mockito.when(restConnector.fetchObject(Mockito.any(), Mockito.any()))
        .thenReturn(productWorkshopResponse);

    List<DroitsTPOfflinePW> droitsTPOfflinePW1 =
        pwService.getDroitsProductsWorkshop(errorsToFill, produit, LocalDateTime.now().toString());
    Assertions.assertTrue(CollectionUtils.isNotEmpty(droitsTPOfflinePW1));
    Assertions.assertEquals(2, droitsTPOfflinePW1.size());
    Assertions.assertEquals("DIFF_TP_2", droitsTPOfflinePW1.get(0).getOfferCode());
    Assertions.assertEquals("2023_janv_01.1", droitsTPOfflinePW1.get(0).getOfferVersionCode());
    Assertions.assertEquals("DIFF_TP_2", droitsTPOfflinePW1.get(1).getOfferCode());
    Assertions.assertEquals("2023_june_01.1", droitsTPOfflinePW1.get(1).getOfferVersionCode());
  }

  @Test
  void shouldProductNotFound() {
    List<String> errorsToFill = new ArrayList<>();
    Produit produit = new Produit();
    produit.setCode("codeProduit");
    produit.setCodeAmc("Oc");

    // Error 00408_1
    JSONObject productWorkshopResponseMock1 = new JSONObject();
    JSONObject error1 = new JSONObject();
    error1.put("error_code", Constants.NEXT_ENGINE_CORE_00408_1);
    error1.put("message", "message d'erreur sans importance");
    productWorkshopResponseMock1.put("errors", error1);
    JSONArray jsonArray = new JSONArray(List.of(error1));
    Mockito.doReturn(jsonArray)
        .when(pwCachedService)
        .getDetailGarantieTpRightsFromPWV4(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.nullable(String.class));

    PwException pwException =
        Assertions.assertThrows(
            PwException.class,
            () ->
                pwService.getDroitsProductsWorkshop(
                    errorsToFill, produit, LocalDateTime.now().toString()));
    Assertions.assertEquals(
        Anomaly.PRODUCT_NOT_FOUND, pwException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }
}
