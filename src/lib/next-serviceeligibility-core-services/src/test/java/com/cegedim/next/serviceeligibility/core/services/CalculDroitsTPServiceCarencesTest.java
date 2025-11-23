package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = TestConfiguration.class)
class CalculDroitsTPServiceCarencesTest {
  @Autowired private CarenceService carenceService;

  private CarenceService mockedCarenceService;

  @Autowired private CalculDroitsTPGenerationService calculDroitsTPGenerationService;

  @BeforeAll
  void beforeAll() {
    ReflectionTestUtils.setField(
        calculDroitsTPGenerationService, "carenceService", mockedCarenceService);
  }

  @AfterAll
  void afterAll() {
    ReflectionTestUtils.setField(calculDroitsTPGenerationService, "carenceService", carenceService);
  }

  @Test
  void shouldNotReturnParametreCarence() throws CarenceException {
    createMockForParametreCarence();
    Mockito.when(
            mockedCarenceService.getParametragesCarence(
                Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(new ArrayList<>());
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR01");
    carence.setPeriode(new PeriodeCarence());
    CarenceException carenceException =
        Assertions.assertThrows(
            CarenceException.class,
            () ->
                calculDroitsTPGenerationService.getParametrageCarenceList(
                    "ASS",
                    "OFF",
                    "PROD",
                    carence.getCode(),
                    carence.getPeriode().getDebut(),
                    carence.getPeriode().getFin(),
                    false));
    Assertions.assertEquals(
        Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
        carenceException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void shouldNotReturnParametreCarence2() throws CarenceException {
    createMockForParametreCarence();
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setCodeCarence("CAR02");
    parametrageCarenceList.add(parametrageCarence);
    Mockito.when(
            mockedCarenceService.getParametragesCarence(
                Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(parametrageCarenceList);
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR01");
    carence.setPeriode(new PeriodeCarence());
    CarenceException carenceException =
        Assertions.assertThrows(
            CarenceException.class,
            () ->
                calculDroitsTPGenerationService.getParametrageCarenceList(
                    "ASS",
                    "OFF",
                    "PROD",
                    carence.getCode(),
                    carence.getPeriode().getDebut(),
                    carence.getPeriode().getFin(),
                    false));
    Assertions.assertEquals(
        Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
        carenceException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void shouldNotReturnParametreCarence3() throws CarenceException {
    createMockForParametreCarence();
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setCodeCarence("CAR01");
    parametrageCarence.setDateDebutParametrage("2022-01-01");
    parametrageCarenceList.add(parametrageCarence);
    Mockito.when(
            mockedCarenceService.getParametragesCarence(
                Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(parametrageCarenceList);
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR01");
    PeriodeCarence periode = new PeriodeCarence();
    periode.setDebut("2021-12-01");
    periode.setFin("2022-02-28");
    carence.setPeriode(periode);

    CarenceException carenceException =
        Assertions.assertThrows(
            CarenceException.class,
            () ->
                calculDroitsTPGenerationService.getParametrageCarenceList(
                    "ASS",
                    "OFF",
                    "PROD",
                    carence.getCode(),
                    carence.getPeriode().getDebut(),
                    carence.getPeriode().getFin(),
                    false));
    Assertions.assertEquals(
        Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
        carenceException.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  @Test
  void shouldReturnParametreCarence() throws CarenceException {
    createMockForParametreCarence();
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence = new ParametrageCarence();
    parametrageCarence.setCodeCarence("CAR01");
    parametrageCarence.setDateDebutParametrage("2020-01-01");
    parametrageCarenceList.add(parametrageCarence);
    Mockito.when(
            mockedCarenceService.getParametragesCarence(
                Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(parametrageCarenceList);
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CAR01");
    PeriodeCarence periode = new PeriodeCarence();
    periode.setDebut("2023-01-01");
    periode.setFin("2023-03-31");
    carence.setPeriode(periode);

    List<ParametrageCarence> retour =
        calculDroitsTPGenerationService.getParametrageCarenceList(
            "ASS",
            "OFF",
            "PROD",
            carence.getCode(),
            carence.getPeriode().getDebut(),
            carence.getPeriode().getFin(),
            false);
    Assertions.assertNotNull(retour);
  }

  private void createMockForParametreCarence() {
    // Utile pour pas mettre la grouille dans les autres tests qui ne bouchonnent
    // pas pareil
    mockedCarenceService = Mockito.mock(CarenceService.class);
    ReflectionTestUtils.setField(
        calculDroitsTPGenerationService, "carenceService", mockedCarenceService);
  }
}
