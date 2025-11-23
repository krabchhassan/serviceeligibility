package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class CarenceServiceTest {

  @Autowired private CarenceService carenceService;

  @Autowired private RestConnector restConnector;

  @Test
  void shouldReturn4WaitingParameters() throws IOException, CarenceException {

    JSONObject productWorkshopResponse =
        UtilsForTesting.parseJSONFile("src/test/resources/carences.json");

    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(productWorkshopResponse.getJSONArray("carences"));

    List<ParametrageCarence> parametrageCarenceList =
        carenceService.getParametragesCarence("BALOO", "CASE1", "PDT1");

    Assertions.assertEquals(4, parametrageCarenceList.size());
    ParametrageCarence parametrageCarence = parametrageCarenceList.getFirst();
    Assertions.assertEquals("CAR001", parametrageCarence.getCodeCarence());
    Assertions.assertEquals("PDT1", parametrageCarence.getProduit());
    Assertions.assertEquals("CASE1", parametrageCarence.getOffre());
    Assertions.assertEquals("2000-01-01", parametrageCarence.getDateDebutParametrage());
    Assertions.assertEquals("DENTAIRE", parametrageCarence.getNaturePrestation());
    Assertions.assertNull(parametrageCarence.getDateFinParametrage());

    Assertions.assertEquals("HOSPITALISATION", parametrageCarenceList.get(1).getNaturePrestation());
    Assertions.assertEquals("OPTIQUE", parametrageCarenceList.get(2).getNaturePrestation());
    Assertions.assertEquals(
        "APPAREILLAGEDENTAIRE", parametrageCarenceList.get(3).getNaturePrestation());
  }

  @Test
  void shouldReturn0WaitingParameters() throws CarenceException {

    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(new JSONArray());

    List<ParametrageCarence> parametrageCarenceList =
        carenceService.getParametragesCarence("BALOO", "CASE1", "PDT1");

    Assertions.assertEquals(0, parametrageCarenceList.size());
  }

  @Test
  void shouldReturn8WaitingParameters() throws IOException, CarenceException {
    JSONObject productWorkshopResponse =
        UtilsForTesting.parseJSONFile("src/test/resources/multiplescarences.json");
    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(productWorkshopResponse.getJSONArray("carences"));

    List<ParametrageCarence> parametrageCarenceList =
        carenceService.getParametragesCarence("BALOO", "CASE1", "PDT1");

    Assertions.assertEquals(8, parametrageCarenceList.size());
    Assertions.assertEquals("CAR001", parametrageCarenceList.get(0).getCodeCarence());
    Assertions.assertEquals("CAR002", parametrageCarenceList.get(7).getCodeCarence());
    Assertions.assertEquals("PDT1", parametrageCarenceList.get(0).getProduit());
    Assertions.assertEquals("2021-12-31", parametrageCarenceList.get(2).getDateFinParametrage());

    Assertions.assertEquals("2021-01-01", parametrageCarenceList.get(0).getDateDebutParametrage());

    Assertions.assertNull(parametrageCarenceList.get(3).getDateFinParametrage());

    Assertions.assertEquals("SOINSCOURANTS", parametrageCarenceList.get(0).getNaturePrestation());
    Assertions.assertEquals(
        "PRVENTIONETMDECINESALTERNATIVES", parametrageCarenceList.get(1).getNaturePrestation());
    Assertions.assertEquals("OPTIQUE", parametrageCarenceList.get(2).getNaturePrestation());
    Assertions.assertEquals("SOINSCOURANTS", parametrageCarenceList.get(3).getNaturePrestation());
    Assertions.assertEquals(
        "PRVENTIONETMDECINESALTERNATIVES", parametrageCarenceList.get(4).getNaturePrestation());
    Assertions.assertEquals("OPTIQUE", parametrageCarenceList.get(5).getNaturePrestation());
    Assertions.assertEquals(
        "APPAREILLAGEETPROTHESES", parametrageCarenceList.get(6).getNaturePrestation());
    Assertions.assertEquals("AIDESAUDITIVES", parametrageCarenceList.get(7).getNaturePrestation());
  }

  @Test
  void shouldThrowCarenceException() {
    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenThrow(new GenericNotFoundException("pas bien"));

    CarenceException carenceException =
        Assertions.assertThrows(
            CarenceException.class,
            () -> carenceService.getParametragesCarence("BALOO", "CASE1", "PDT1"));
    Assertions.assertEquals(
        Anomaly.SERVICE_SETTINGS_UI_NOT_RESPONDING,
        carenceException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void shouldReturnCarenceExceptionBis() throws IOException {

    JSONObject productWorkshopResponse =
        UtilsForTesting.parseJSONFile("src/test/resources/carenceserror.json");

    Mockito.when(restConnector.fetchArray(Mockito.anyString(), Mockito.any()))
        .thenReturn(productWorkshopResponse.getJSONArray("carences"));

    CarenceException carenceException =
        Assertions.assertThrows(
            CarenceException.class,
            () -> carenceService.getParametragesCarence("BALOO", "CASE1", "PDT1"));
    Assertions.assertEquals(
        Anomaly.SERVICE_SETTINGS_UI_NOT_RESPONDING,
        carenceException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }
}
