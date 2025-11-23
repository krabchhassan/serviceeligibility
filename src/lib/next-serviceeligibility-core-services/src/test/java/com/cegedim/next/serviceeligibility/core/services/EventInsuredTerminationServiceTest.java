package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.event.EventInsuredTerminationService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class EventInsuredTerminationServiceTest {

  @Autowired private EventInsuredTerminationService eventInsuredTerminationService;

  @Autowired private EventService eventService;

  private static final String SERVICE_PRESTATION_PATH = "src/test/resources/contracts-6073/";

  @Test
  void sendEventInsuredPeriode() throws IOException {
    ContratAIV6 contratWithOpenInsuredPeriod =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure1.json", ContratAIV6.class);
    ContratAIV6 contratWithOpenClosedInsuredPeriod =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure2.json", ContratAIV6.class);
    int nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contratWithOpenClosedInsuredPeriod, contratWithOpenInsuredPeriod);
    Assertions.assertEquals(3, nbSentEvent);
  }

  @Test
  void sendEventInsuredPeriode2() throws IOException {
    ContratAIV6 contratWithOpenInsuredPeriod =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure2.json", ContratAIV6.class);
    ContratAIV6 contratWithOpenClosedInsuredPeriod =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure3.json", ContratAIV6.class);
    int nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contratWithOpenClosedInsuredPeriod, contratWithOpenInsuredPeriod);
    Assertions.assertEquals(1, nbSentEvent);
  }

  @Test
  void sendEventRadiation() throws IOException {
    ContratAIV6 contrat1sansRadiation =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure1.json", ContratAIV6.class);
    // Radiation au 2024-02-28
    ContratAIV6 contrat2avecRadiation =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_radiation1.json", ContratAIV6.class);
    int nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contrat2avecRadiation, contrat1sansRadiation);
    Assertions.assertEquals(1, nbSentEvent);

    // Radiation au 2024-01-28
    ContratAIV6 contrat3avecRadiation =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_radiation2.json", ContratAIV6.class);
    nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contrat3avecRadiation, contrat2avecRadiation);
    Assertions.assertEquals(1, nbSentEvent);
  }

  @Test
  void sendEventResilisation() throws IOException {
    ContratAIV6 contrat1 =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure1.json", ContratAIV6.class);
    // Nouveau contrat avec Resiliation
    ContratAIV6 contrat2AvecResil =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_resiliation1.json", ContratAIV6.class);
    int nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contrat2AvecResil, contrat1);
    Assertions.assertEquals(3, nbSentEvent);

    // Envoi du même contrat avec la même date de resiliation
    nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contrat2AvecResil, contrat2AvecResil);
    Assertions.assertEquals(0, nbSentEvent);
  }

  @Test
  void sendEventRadiationResiliationFermeturePeriode() throws IOException {
    ContratAIV6 contrat1 =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure1.json", ContratAIV6.class);
    // Nouveau contrat avec Resiliation + dateRadiation pour 2 assures +
    // periodesFermeture pour 1 assure
    ContratAIV6 contrat2AvecResilRadFermeture =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_resiliation_radiation_fermeturePeriode.json",
            ContratAIV6.class);
    int nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contrat2AvecResilRadFermeture, contrat1);
    Assertions.assertEquals(3, nbSentEvent);
  }

  @Test
  void sendEventResiliationFermeturePeriodeSansFin() throws IOException {
    ContratAIV6 contrat1 =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_periodeAssure1.json", ContratAIV6.class);
    // Nouveau contrat avec Resiliation + 1 assuré ayant 2 periodes assures
    // dont une sans date de fin
    ContratAIV6 contrat2AvecResilFermetureSansFin =
        UtilsForTesting.createTFromJson(
            SERVICE_PRESTATION_PATH + "contrat_resiliation_fermeturePeriodeSansFin.json",
            ContratAIV6.class);
    int nbSentEvent =
        eventInsuredTerminationService.manageEventsInsuredTermination(
            eventService, contrat2AvecResilFermetureSansFin, contrat1);

    // 3 events (1 pour chaque assuré) : 2 avec dateResil + 1 avec dateFin periode
    Assertions.assertEquals(3, nbSentEvent);
  }
}
