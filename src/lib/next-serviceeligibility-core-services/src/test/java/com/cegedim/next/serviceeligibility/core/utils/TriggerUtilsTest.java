package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageRenouvellement;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodesDroitsCarte;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TriggerUtilsTest {
  static ParametrageTrigger getParametrageTriggerForTest(
      TriggerEmitter triggerType,
      ParametrageCarteTP pc,
      TriggeredBeneficiary tb,
      String dateEffet,
      String dateSouscription) {
    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(tb);
    parametrageTrigger.setOrigine(triggerType);
    parametrageTrigger.setDateEffet(dateEffet);
    parametrageTrigger.setParametrageCarteTP(pc);
    parametrageTrigger.setDateSouscription(dateSouscription);
    parametrageTrigger.setRdo(false);
    return parametrageTrigger;
  }

  static ParametrageTrigger getParametrageTriggerRdoForTest(
      TriggerEmitter triggerType,
      ParametrageCarteTP pc,
      TriggeredBeneficiary tb,
      String dateSouscription) {
    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(tb);
    parametrageTrigger.setOrigine(triggerType);
    parametrageTrigger.setDateEffet("2021-01-01");
    parametrageTrigger.setParametrageCarteTP(pc);
    parametrageTrigger.setDateSouscription(dateSouscription);
    parametrageTrigger.setRdo(true);
    return parametrageTrigger;
  }

  @Test
  void should_calculate_digital() {
    TriggeredBeneficiary tb = new TriggeredBeneficiary();
    ParametrageDroitsCarteTP pdc = new ParametrageDroitsCarteTP();

    tb.setIsCartePapier(null);
    tb.setIsCarteDematerialisee(null);
    tb.setIsCartePapierAEditer(null);
    pdc.setIsCarteDematerialisee(true);
    pdc.setIsCarteEditablePapier(true);

    String result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("3", result);

    pdc.setIsCarteEditablePapier(true);
    pdc.setIsCarteDematerialisee(false);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("1", result);

    pdc.setIsCarteEditablePapier(false);
    pdc.setIsCarteDematerialisee(true);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("2", result);

    pdc.setIsCarteEditablePapier(false);
    pdc.setIsCarteDematerialisee(false);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("0", result);

    // without parametrage
    tb.setIsCartePapierAEditer(false);
    tb.setIsCarteDematerialisee(false);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("0", result);

    tb.setIsCartePapierAEditer(true);
    tb.setIsCarteDematerialisee(false);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("1", result);

    tb.setIsCartePapierAEditer(false);
    tb.setIsCarteDematerialisee(true);
    tb.setIsCartePapier(false);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("2", result);

    tb.setIsCartePapierAEditer(true);
    tb.setIsCarteDematerialisee(true);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("3", result);

    tb.setIsCartePapierAEditer(false);
    tb.setIsCarteDematerialisee(true);
    tb.setIsCartePapier(null);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertEquals("4", result);

    tb.setIsCartePapierAEditer(null);
    tb.setIsCarteDematerialisee(null);
    tb.setIsCartePapier(null);
    pdc.setIsCarteDematerialisee(null);
    pdc.setIsCarteEditablePapier(null);
    result = TriggerUtils.calculateDigital(tb, pdc);
    Assertions.assertNull(result);
  }

  @Test
  void calculateDates1() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2021-04-01";
    String affDebut = "2021-05-01";

    String affFin = "2021-09-17";
    String dateResil = "2021-10-10";
    String debutEcheance = null;

    String declManuel = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-03-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-05-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // déclenchement automatique - anniversaire - Renewal + containte
  @Test
  void calculateDates1b() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2021-04-01";
    String affDebut = "2021-05-01";

    String affFin = "2021-09-17";
    String dateResil = "2021-10-10";
    String debutEcheance = null;

    String declManuel = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-02-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-05-01", dates.get(0));
    Assertions.assertEquals("2022-03-03", dates.get(1));
  }

  // déclenchement Automatique - Anniversaire - Request
  @Test
  void calculateDates2() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Request;
    // date setup
    String dateSous = "2021-04-01";
    String affDebut = "2021-01-01";

    String dateResil = "2024-01-01";
    String affFin = "2024-01-01";
    String declManuel = null;
    String debutEcheance = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-04-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // déclenchement Automatique - Anniversaire - Request + contraintes
  @Test
  void calculateDates2b() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Request;
    // date setup
    String dateSous = "2021-04-01";
    String affDebut = "2021-05-01";

    String dateResil = "2024-01-01";
    String affFin = "2021-12-31";
    String declManuel = null;

    String debutEcheance = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-05-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // Déclenchement Automatique - Anniversaire - Event
  @Test
  void calculateDates3() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Event;
    // date setup
    String dateSous = "2021-04-01";
    String dateResil = "2022-12-31";
    String affDebut = "2021-01-01";
    String affFin = "2022-12-31";
    String declManuel = null;
    String debutEcheance = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-04-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // Déclenchement Automatique - Anniversaire - Event + changement année
  @Test
  void calculateDates3b() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Event;
    // date setup
    String dateSous = "2021-04-01";
    String dateResil = "2022-12-31";
    String affDebut = "2021-01-01";
    String affFin = "2022-12-31";
    String declManuel = null;
    String debutEcheance = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2022-05-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2022-04-01", dates.get(0));
    Assertions.assertEquals("2023-03-31", dates.get(1));
  }

  // déclenchement automatique - échéance - Renewal
  @Test
  void calculateDates4() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2018-03-15";
    String affDebut = "2017-02-01";

    String affFin = "2023-01-17";
    String dateResil = "2025-10-10";

    String declManuel = null;
    String debutEcheance = "01/04"; // 1er avril
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);

    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-04-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // déclenchement automatique - échéance - Renewal + before échéance
  @Test
  void calculateDates4b() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2018-03-15";
    String affDebut = "2017-02-01";

    String affFin = "2023-01-17";
    String dateResil = "2025-10-10";

    String declManuel = null;
    String debutEcheance = "01/04"; // 1er avril
    int delai = 15;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-03-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2020-04-01", dates.get(0));
    Assertions.assertEquals("2021-03-31", dates.get(1));
  }

  // déclenchement automatique - échéance - Renewal + before échéance -
  // trimestriel
  @Test
  void calculateDates4c() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2018-03-15";
    String affDebut = "2017-02-01";

    String affFin = "2023-01-17";
    String dateResil = "2025-10-10";

    String declManuel = null;
    String debutEcheance = "01/07"; // 1er juillet
    int delai = 0;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    pc.getParametrageRenouvellement()
        .setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Trimestriel);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2022-01-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2022-01-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // déclenchement automatique - échéance - Renewal + after échéance with delai
  @Test
  void calculateDates4d() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2018-03-15";
    String affDebut = "2017-02-01";

    String affFin = "2023-01-17";
    String dateResil = "2025-10-10";

    String declManuel = null;
    String debutEcheance = "01/04"; // 1er avril
    int delai = 30;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-03-10", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-04-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // déclenchement automatique - échéance - Event
  @Test
  void calculateDates5() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Event;
    // date setup
    String dateSous = "2018-03-15";
    String affDebut = "2017-02-01";

    String affFin = "2023-01-17";
    String dateResil = "2025-10-10";

    String declManuel = null;
    String debutEcheance = "01/04"; // 1er avril
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-04-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // déclenchement automatique - échéance - Request
  @Test
  void calculateDates6() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Automatique;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Request;
    // date setup
    String dateSous = "2018-03-15";
    String affDebut = "2017-02-01";

    String affFin = "2023-01-17";
    String dateResil = "2025-10-10";

    String declManuel = null;
    String debutEcheance = "01/04"; // 1er avril
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-04-01", dates.get(0));
    Assertions.assertEquals("2022-03-31", dates.get(1));
  }

  // Déclenchement manuel - Anniversaire - Renewal
  @Test
  void calculateDates7() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Manuel;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2021-05-01";
    String dateResil = "";
    String affDebut = "";
    String affFin = "";
    String declManuel = "2021-04-01";
    String debutEcheance = null;
    int delai = 30;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    pc.getParametrageRenouvellement().setDateExecutionBatch("2021-04-01");
    pc.getParametrageRenouvellement().setDateDebutDroitTP("2021-01-01");
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-04-01", dateSous);
    parametrageTrigger.setRdo(false);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-05-01", dates.get(0));
    Assertions.assertEquals("2022-04-30", dates.get(1));
  }

  // Déclenchement manuel - Anniversaire - Request
  @Test
  void calculateDates8() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Manuel;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Request;
    // date setup
    String dateSous = "";
    String dateResil = "";
    String affDebut = "";
    String affFin = "";
    String declManuel = "2021-09-01";
    String debutEcheance = null;
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-08-01", dateSous);
    parametrageTrigger.setRdo(false);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-08-01", dates.get(0));
    Assertions.assertEquals("2022-07-31", dates.get(1));
  }

  // Déclenchement manuel - Echeance - Renewal
  @Test
  void calculateDates9() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Manuel;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateSous = "2021-05-01";
    String dateResil = "";
    String affDebut = "";
    String affFin = "";
    String declManuel = "2021-04-01";
    String debutEcheance = null;
    int delai = 30;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2021-04-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2021-05-01", dates.get(0));
    Assertions.assertEquals("2022-04-30", dates.get(1));
  }

  // déclenchement Manuel - Echeance - Request
  @Test
  void calculateDates10() {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Manuel;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.DebutEcheance;

    TriggerEmitter triggerType = TriggerEmitter.Request;
    // date setup
    String dateSous = "2021-01-01";
    String dateResil = "2024-01-01";
    String affDebut = "2021-01-01";
    String affFin = "2024-01-01";
    String declManuel = "2022-03-01";
    String debutEcheance = "01/04"; // 1er avril
    int delai = 31;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSous, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        getParametrageTriggerForTest(triggerType, pc, tb, "2022-03-01", dateSous);
    List<String> dates = TriggerUtils.calculDates(parametrageTrigger, tspd, false);

    Assertions.assertEquals("2022-03-01", dates.get(0));
    Assertions.assertEquals("2023-02-28", dates.get(1));
  }

  @Test
  void shouldReturnPeriodInCheckPrestationsNatureInCarenceSetting() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence =
        new ParametrageCarence("A", "A", "DENT", "CAR1", "2000-01-01", "2000-04-01");
    parametrageCarenceList.add(parametrageCarence);
    String codeCarence =
        TriggerUtils.getCodeCarenceForPrestationsNatureInCarenceSetting(
            parametrageCarenceList, "DENT");
    Assertions.assertEquals("CAR1", codeCarence);
  }

  @Test
  void shouldNotReturnPeriodInCheckPrestationsNatureInCarenceSetting() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence =
        new ParametrageCarence("A", "A", "DENT", "CAR1", "2000-01-01", "2000-04-01");
    parametrageCarenceList.add(parametrageCarence);
    String codeCarence =
        TriggerUtils.getCodeCarenceForPrestationsNatureInCarenceSetting(
            parametrageCarenceList, "HOSP");
    Assertions.assertNull(codeCarence);
  }

  @Test
  void shouldReturnPeriodInCheckPrestationsNatureInMultipleCarenceSetting() {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    ParametrageCarence parametrageCarence =
        new ParametrageCarence("A", "A", "DENT", "CAR2", "2000-01-01", "2000-04-01");
    ParametrageCarence parametrageCarence2 =
        new ParametrageCarence("A", "A", "HOSP", "CAR3", "2020-01-01", "2020-04-01");
    ParametrageCarence parametrageCarence3 =
        new ParametrageCarence("A", "A", "OPTI", "CAR4", "2022-01-01", "2022-03-01");
    parametrageCarenceList.add(parametrageCarence);
    parametrageCarenceList.add(parametrageCarence2);
    parametrageCarenceList.add(parametrageCarence3);
    String codeCarence =
        TriggerUtils.getCodeCarenceForPrestationsNatureInCarenceSetting(
            parametrageCarenceList, "HOSP");
    Assertions.assertEquals("CAR3", codeCarence);
  }

  @Test
  void testExceptions() {
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    List<TriggeredBeneficiaryAnomaly> anomalies = new ArrayList<>();
    ServicePrestationTriggerBenef contrat = new ServicePrestationTriggerBenef();
    PeriodesDroitsCarte periodesDroitsCarte = new PeriodesDroitsCarte();
    contrat.setPeriodesDroitsCarte(periodesDroitsCarte);
    triggeredBeneficiary.setNewContract(contrat);

    TriggerUtils.controlePilotageBO(triggeredBeneficiary, anomalies);
    Assertions.assertEquals(
        Anomaly.IS_DEMATERIALISED_CARD_NOT_FOUND, anomalies.get(0).getAnomaly());
    Assertions.assertEquals(Anomaly.IS_PAPER_CARD_NOT_FOUND, anomalies.get(1).getAnomaly());
    Assertions.assertEquals(
        Anomaly.NO_START_DATE_ON_CARD_RIGHT_PERIODS, anomalies.get(2).getAnomaly());
    Assertions.assertEquals(
        Anomaly.NO_END_DATE_ON_CARD_RIGHT_PERIODS, anomalies.get(3).getAnomaly());
  }

  @Test
  void garantieShouldBeInTheLot() {
    Lot lot = new Lot();
    GarantieTechnique gt = new GarantieTechnique();
    gt.setCodeGarantie("AAAA");
    gt.setCodeAssureur("BBBB");
    List<GarantieTechnique> garantieTechniqueList = new ArrayList<>();
    garantieTechniqueList.add(gt);
    lot.setGarantieTechniques(garantieTechniqueList);
    Assertions.assertTrue(TriggerUtils.isGarantieInsideLot(lot, gt));
  }

  @Test
  void garantieShouldNotBeInTheLot2() {
    Lot lot = new Lot();
    GarantieTechnique gt = new GarantieTechnique();
    gt.setCodeGarantie("AAAA");
    gt.setCodeAssureur("BBBB");
    gt.setDateSuppressionLogique("2020-01-01");
    List<GarantieTechnique> garantieTechniqueList = new ArrayList<>();
    garantieTechniqueList.add(gt);
    lot.setGarantieTechniques(garantieTechniqueList);
    Assertions.assertFalse(TriggerUtils.isGarantieInsideLot(lot, gt));
  }

  @Test
  void garantieShouldNotBeInTheLot() {
    Lot lot = new Lot();
    GarantieTechnique gt = new GarantieTechnique();
    gt.setCodeGarantie("AAAA");
    gt.setCodeAssureur("BBBB");
    List<GarantieTechnique> garantieTechniqueList = new ArrayList<>();
    garantieTechniqueList.add(gt);
    lot.setGarantieTechniques(garantieTechniqueList);
    GarantieTechnique gt2 = new GarantieTechnique();
    gt.setCodeGarantie("BBBB");
    gt.setCodeAssureur("AAAA");
    Assertions.assertFalse(TriggerUtils.isGarantieInsideLot(lot, gt2));
  }

  @Test
  void shouldReturnAssure() {
    ContratAIV6 contrat = new ContratAIV6();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    droitAssure.setPeriode(periode);
    Assure assure = new Assure();
    assure.setDroits(List.of(droitAssure));
    assure.setIsSouscripteur(Boolean.TRUE);
    Assure assure2 = new Assure();
    assure.setDroits(List.of(droitAssure));
    contrat.setAssures(List.of(assure, assure2));
    Assertions.assertEquals(
        assure,
        TriggerUtils.getAssureForRenouvellement(contrat, new Periode("2024-01-01", "2024-12-31")));
  }

  @Test
  void shouldNotReturnAssure() {
    ContratAIV6 contrat = new ContratAIV6();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    periode.setFin("2022-01-01");
    droitAssure.setPeriode(periode);
    Assure assure = new Assure();
    assure.setDroits(List.of(droitAssure));
    assure.setIsSouscripteur(Boolean.TRUE);
    Assure assure2 = new Assure();
    assure.setDroits(List.of(droitAssure));
    contrat.setAssures(List.of(assure, assure2));
    Assertions.assertNull(
        TriggerUtils.getAssureForRenouvellement(contrat, new Periode("2024-01-01", "2024-12-31")));
  }

  @Test
  void shouldReturnAssure2() {
    ContratAIV6 contrat = new ContratAIV6();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    periode.setFin("2022-01-01");
    droitAssure.setPeriode(periode);
    Assure assure = new Assure();
    assure.setIsSouscripteur(Boolean.TRUE);
    assure.setDroits(List.of(droitAssure));

    Assure assure2 = new Assure();
    assure2.setRangAdministratif("2");
    DroitAssure droitAssure2 = new DroitAssure();
    Periode periode2 = new Periode();
    periode2.setDebut("2020-01-01");
    droitAssure2.setPeriode(periode2);
    assure2.setDroits(List.of(droitAssure2));
    contrat.setAssures(List.of(assure, assure2));

    Assertions.assertEquals(
        assure2,
        TriggerUtils.getAssureForRenouvellement(contrat, new Periode("2024-01-01", "2024-12-31")));
  }

  @Test
  void shouldNotReturnAssure2() {
    ContratAIV6 contrat = new ContratAIV6();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    periode.setFin("2022-01-01");
    droitAssure.setPeriode(periode);

    DroitAssure droitAssureBis = new DroitAssure();
    Periode periodeBis = new Periode();
    periodeBis.setDebut("2023-01-01");
    periodeBis.setFin("2023-12-31");
    droitAssureBis.setPeriode(periodeBis);
    Assure assure = new Assure();
    assure.setIsSouscripteur(Boolean.TRUE);
    assure.setDroits(List.of(droitAssure, droitAssureBis));

    Assure assure2 = new Assure();
    assure2.setRangAdministratif("2");
    DroitAssure droitAssure2 = new DroitAssure();
    Periode periode2 = new Periode();
    periode2.setDebut("2020-01-01");
    periode2.setFin("2023-12-31");
    droitAssure2.setPeriode(periode2);
    assure2.setDroits(List.of(droitAssure2));
    contrat.setAssures(List.of(assure, assure2));

    Assertions.assertNull(
        TriggerUtils.getAssureForRenouvellement(contrat, new Periode("2024-01-01", "2024-12-31")));
  }

  @Test
  void shouldReturnAssure3() {
    ContratAIV6 contrat = new ContratAIV6();
    DroitAssure droitAssure = new DroitAssure();
    Periode periode = new Periode();
    periode.setDebut("2020-01-01");
    periode.setFin("2022-01-01");
    droitAssure.setPeriode(periode);
    Assure assure = new Assure();
    assure.setRangAdministratif("1");
    assure.setDroits(List.of(droitAssure));

    Assure assure2 = new Assure();
    assure2.setRangAdministratif("2");
    DroitAssure droitAssure2 = new DroitAssure();
    Periode periode2 = new Periode();
    periode2.setDebut("2020-01-01");
    droitAssure2.setPeriode(periode2);
    assure2.setDroits(List.of(droitAssure2));

    Assure assure3 = new Assure();
    assure3.setRangAdministratif("3");
    DroitAssure droitAssure3 = new DroitAssure();
    Periode periode3 = new Periode();
    periode3.setDebut("2020-01-01");
    droitAssure3.setPeriode(periode3);
    assure3.setDroits(List.of(droitAssure3));

    contrat.setAssures(List.of(assure, assure2, assure3));

    Assertions.assertEquals(
        assure3,
        TriggerUtils.getAssureForRenouvellement(contrat, new Periode("2024-01-01", "2024-12-31")));
  }

  @Test
  void testCalculPeriodeReprise() {
    LocalDate today = LocalDate.now();
    LocalDate startRenewal = LocalDate.of(today.getYear(), 2, 28);

    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(
        DateRenouvellementCarteTP.AnniversaireContrat);
    parametrageRenouvellement.setDelaiRenouvellement(
        (int) ChronoUnit.DAYS.between(today, startRenewal));
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    Periode periode = TriggerUtils.calculPeriodeReprise(parametrageCarteTP, "2022-02-28", null);
    LocalDate startWanted =
        parametrageRenouvellement.getDelaiRenouvellement() >= 0
            ? startRenewal
            : startRenewal.plusYears(1);
    LocalDate endWanted = startWanted.plusYears(1).minusDays(1);
    Assertions.assertEquals(DateUtils.formatDate(startWanted), periode.getDebut());
    Assertions.assertEquals(DateUtils.formatDate(endWanted), periode.getFin());
  }

  @Test
  void testCalculPeriodeReprise2() {
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    LocalDate today = LocalDate.now();
    LocalDate startRenewal = LocalDate.of(today.getYear(), 6, 5);
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(
        DateRenouvellementCarteTP.AnniversaireContrat);
    parametrageRenouvellement.setDelaiRenouvellement(
        (int) ChronoUnit.DAYS.between(today, startRenewal));
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    Periode periode = TriggerUtils.calculPeriodeReprise(parametrageCarteTP, "2022-06-05", null);
    LocalDate startWanted =
        parametrageRenouvellement.getDelaiRenouvellement() >= 0
            ? startRenewal
            : startRenewal.plusYears(1);
    LocalDate endWanted = startWanted.plusYears(1).minusDays(1);
    Assertions.assertEquals(DateUtils.formatDate(startWanted), periode.getDebut());
    Assertions.assertEquals(DateUtils.formatDate(endWanted), periode.getFin());
  }

  @Test
  void testCalculPeriodeRepriseAuto() {
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    LocalDate today = LocalDate.now();
    LocalDate startRenewal = LocalDate.of(today.getYear() + 1, 1, 1);
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDelaiRenouvellement(
        (int) ChronoUnit.DAYS.between(today, startRenewal));
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    Periode periode = TriggerUtils.calculPeriodeReprise(parametrageCarteTP, "2022-06-05", null);
    Assertions.assertEquals((today.getYear() + 1) + "-01-01", periode.getDebut());
    Assertions.assertEquals((today.getYear() + 1) + "-12-31", periode.getFin());
  }

  @Test
  void testCalculPeriodeRepriseAutoWithDateBeforeTheEnd() {
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    LocalDate today = LocalDate.now();
    LocalDate startRenewal = LocalDate.of(today.getYear() + 1, 1, 1);
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDelaiRenouvellement(
        (int) ChronoUnit.DAYS.between(today, startRenewal));
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    Periode periode =
        TriggerUtils.calculPeriodeReprise(
            parametrageCarteTP, "2022-06-05", today.getYear() + 1 + "-10-30");
    Assertions.assertEquals((today.getYear() + 1) + "-01-01", periode.getDebut());
    Assertions.assertEquals((today.getYear() + 1) + "-12-31", periode.getFin());
  }

  @Test
  void testCalculPeriodeRepriseAutoWithDateBeforeTheStart() {
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    LocalDate today = LocalDate.now();
    LocalDate startRenewal = LocalDate.of(today.getYear() + 1, 1, 1);
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDelaiRenouvellement(
        (int) ChronoUnit.DAYS.between(today, startRenewal));
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    Periode periode =
        TriggerUtils.calculPeriodeReprise(
            parametrageCarteTP, "2022-06-05", today.getYear() + "-10-30");

    Assertions.assertEquals((today.getYear() + 1) + "-01-01", periode.getDebut());
    Assertions.assertEquals((today.getYear() + 1) + "-10-30", periode.getFin());
  }
}
