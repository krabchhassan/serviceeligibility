package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TriggerUtilsRDOTest {
  // déclenchement Manuel - AnniversaireContrat - Renewal
  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase1Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-01-01", "2021-01-01");

    Assertions.assertEquals("2021-01-01", dates.get(0));
    Assertions.assertEquals("2021-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase1Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-01-01", "2021-01-01");

    Assertions.assertEquals("2021-01-01", dates.get(0));
    Assertions.assertEquals("2021-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase1Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-01-01", "2021-01-01");

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals("2022-01-01", dates.get(0));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase2Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-02-15", "2021-01-01");

    Assertions.assertEquals("2021-02-15", dates.get(0));
    Assertions.assertEquals("2022-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase2Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-02-15", "2021-01-01");

    Assertions.assertEquals("2021-02-15", dates.get(0));
    Assertions.assertEquals("2022-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase2Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-02-15", "2021-01-01");

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals("2022-02-15", dates.get(0));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase3Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-07-15", "2021-01-01");

    Assertions.assertEquals("2021-07-15", dates.get(0));
    Assertions.assertEquals("2022-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase3Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-07-15", "2021-01-01");

    Assertions.assertEquals("2021-07-15", dates.get(0));
    Assertions.assertEquals("2022-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase3Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-07-15", "2021-01-01");

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals("2022-07-15", dates.get(0));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase4Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-12-31", "2021-01-01");

    Assertions.assertEquals("2021-12-31", dates.get(0));
    Assertions.assertEquals("2022-12-30", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase4Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-12-31", "2021-01-01");

    Assertions.assertEquals("2021-12-31", dates.get(0));
    Assertions.assertEquals("2022-12-30", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2021UseCase4Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-12-31", "2021-01-01");

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals("2022-12-31", dates.get(0));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase1Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-01-01", "2022-01-01");

    Assertions.assertEquals("2022-01-01", dates.get(0));
    Assertions.assertEquals("2022-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase1Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-01-01", "2022-01-01");

    Assertions.assertEquals("2022-01-01", dates.get(0));
    Assertions.assertEquals("2022-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase1Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-01-01", "2022-01-01");

    Assertions.assertEquals("2022-01-01", dates.get(0));
    Assertions.assertEquals("2022-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase2Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-02-15", "2022-01-01");

    Assertions.assertEquals("2022-02-15", dates.get(0));
    Assertions.assertEquals("2023-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase2Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-02-15", "2022-01-01");

    Assertions.assertEquals("2022-02-15", dates.get(0));
    Assertions.assertEquals("2023-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase2Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-02-15", "2022-01-01");

    Assertions.assertEquals("2022-02-15", dates.get(0));
    Assertions.assertEquals("2023-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase3Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-07-15", "2022-01-01");

    Assertions.assertEquals("2022-07-15", dates.get(0));
    Assertions.assertEquals("2023-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase3Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-07-15", "2022-01-01");

    Assertions.assertEquals("2022-07-15", dates.get(0));
    Assertions.assertEquals("2023-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase3Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-07-15", "2022-01-01");

    Assertions.assertEquals("2022-07-15", dates.get(0));
    Assertions.assertEquals("2023-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase4Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-12-31", "2022-01-01");

    Assertions.assertEquals("2022-12-31", dates.get(0));
    Assertions.assertEquals("2023-12-30", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase4Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-12-31", "2022-01-01");

    Assertions.assertEquals("2022-12-31", dates.get(0));
    Assertions.assertEquals("2023-12-30", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2022UseCase4Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-12-31", "2022-01-01");

    Assertions.assertEquals("2022-12-31", dates.get(0));
    Assertions.assertEquals("2023-12-30", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase1Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-01-01", "2023-01-01");

    Assertions.assertEquals("2023-01-01", dates.get(0));
    Assertions.assertEquals("2023-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase1Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-01-01", "2023-01-01");

    Assertions.assertEquals("2023-01-01", dates.get(0));
    Assertions.assertEquals("2023-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase1Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-01-01", "2023-01-01");

    Assertions.assertEquals("2023-01-01", dates.get(0));
    Assertions.assertEquals("2023-12-31", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase2Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-02-15", "2023-01-01");

    Assertions.assertEquals("2023-02-15", dates.get(0));
    Assertions.assertEquals("2024-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase2Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-02-15", "2023-01-01");

    Assertions.assertEquals("2023-02-15", dates.get(0));
    Assertions.assertEquals("2024-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase2Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-02-15", "2023-01-01");

    Assertions.assertEquals("2023-02-15", dates.get(0));
    Assertions.assertEquals("2024-02-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase3Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-07-15", "2023-01-01");

    Assertions.assertEquals("2023-07-15", dates.get(0));
    Assertions.assertEquals("2024-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase3Rdo2() {
    List<String> dates = getDatesWithRenewal("2021-07-15", "2023-01-01");

    Assertions.assertEquals("2023-07-15", dates.get(0));
    Assertions.assertEquals("2024-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase3Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-07-15", "2023-01-01");

    Assertions.assertEquals("2023-07-15", dates.get(0));
    Assertions.assertEquals("2024-07-14", dates.get(1));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase4Rdo1() {
    List<String> dates = getDatesWithRenewal("2001-12-31", "2023-01-01");

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals("2001-12-31", dates.get(0));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase4Rdo2() {
    String dateSous = "2021-12-31";
    String debutDroitsTP = "2023-01-01";
    List<String> dates = getDatesWithRenewal(dateSous, debutDroitsTP);

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals(dateSous, dates.get(0));
  }

  @Test
  void calculateDatesManuelAnnivRenewal2023UseCase4Rdo3() {
    List<String> dates = getDatesWithRenewal("2022-12-31", "2023-01-01");

    Assertions.assertEquals(1, dates.size());
    Assertions.assertEquals("2022-12-31", dates.get(0));
  }

  private static List<String> getDatesWithRenewal(String dateSouscription, String debutDroitsTP) {
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Manuel;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    TriggerEmitter triggerType = TriggerEmitter.Renewal;
    // date setup
    String dateResil = null;
    String affDebut = dateSouscription;
    String affFin = null;
    String execBatch = "2023-06-20";
    int delai = 30;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParamAnniv(mode, type, execBatch, debutDroitsTP, delai);
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb =
        TriggerDataForTesting.getTriggerBenef(dateSouscription, dateResil, affDebut, affFin);
    ParametrageTrigger parametrageTrigger =
        TriggerUtilsTest.getParametrageTriggerRdoForTest(triggerType, pc, tb, dateSouscription);
    Trigger trigger = new Trigger();
    trigger.setRdo(true);
    return TriggerUtils.calculDates(parametrageTrigger, tspd, false);
  }
}
