package com.cegedim.next.serviceeligibility.core.utils;

import static com.cegedim.next.serviceeligibility.core.utils.TriggerUtils.getPeriodeFromParametrage;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TriggerUtilsNoRDOTest {
  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 1 Janvier
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJan1() {
    String debutRenouvellement = "2022-01-01";
    String declManuel = "2022-02-01";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-01-01", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-01-01", dates.getDebut());
    Assertions.assertEquals("2022-12-31", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-01-01", debutRenouvellement, isRdo);
    Assertions.assertEquals("2022-01-01", dates.getDebut());
    Assertions.assertEquals("2022-12-31", dates.getFin());

    dates = getDatesRenouvellement(pc, "2023-01-01", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 2 Janvier
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJan2() {
    String debutRenouvellement = "2022-01-01";
    String declManuel = "2022-02-01";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-02-15", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-01-01", dates.getDebut());
    Assertions.assertEquals("2023-02-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-02-15", debutRenouvellement, isRdo);
    Assertions.assertEquals("2022-02-15", dates.getDebut());
    Assertions.assertEquals("2023-02-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2023-02-15", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 3 Janvier
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJan3() {
    String debutRenouvellement = "2022-01-01";
    String declManuel = "2022-02-01";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-07-15", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-01-01", dates.getDebut());
    Assertions.assertEquals("2022-07-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-07-15", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);

    dates = getDatesRenouvellement(pc, "2023-07-15", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 4 Janvier
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJan4() {
    String debutRenouvellement = "2022-01-01";
    String declManuel = "2022-02-01";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-12-31", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-01-01", dates.getDebut());
    Assertions.assertEquals("2022-12-30", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-12-31", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);

    dates = getDatesRenouvellement(pc, "2023-12-31", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 1 Juillet
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJuly1() {
    String debutRenouvellement = "2022-07-01";
    String declManuel = "2022-06-20";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-01-01", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-07-01", dates.getDebut());
    Assertions.assertEquals("2022-12-31", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-01-01", debutRenouvellement, isRdo);
    Assertions.assertEquals("2022-07-01", dates.getDebut());
    Assertions.assertEquals("2022-12-31", dates.getFin());

    dates = getDatesRenouvellement(pc, "2023-01-01", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 1 Juillet
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJuly2() {
    String debutRenouvellement = "2022-07-01";
    String declManuel = "2022-06-20";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-02-15", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-07-01", dates.getDebut());
    Assertions.assertEquals("2023-02-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-02-15", debutRenouvellement, isRdo);
    Assertions.assertEquals("2022-07-01", dates.getDebut());
    Assertions.assertEquals("2023-02-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2023-02-15", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 1 Juillet
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJuly3() {
    String debutRenouvellement = "2022-07-01";
    String declManuel = "2022-06-20";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-07-15", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-07-01", dates.getDebut());
    Assertions.assertEquals("2023-07-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-07-15", debutRenouvellement, isRdo);
    Assertions.assertEquals("2022-07-15", dates.getDebut());
    Assertions.assertEquals("2023-07-14", dates.getFin());

    dates = getDatesRenouvellement(pc, "2023-07-15", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  // déclenchement Manuel - Anniversaire - Renewal - New version product workshop
  // - Use Case 1 Juillet
  @Test
  void calculateDatesManuelAnnivRenewalWhenNewProductJuly4() {
    String debutRenouvellement = "2022-07-01";
    String declManuel = "2022-06-20";
    int delai = 30;
    boolean isRdo = false;
    ParametrageCarteTP pc =
        setupParametrageCarteManuelAnniversaire(declManuel, debutRenouvellement, delai);
    Periode dates = getDatesRenouvellement(pc, "2001-12-31", debutRenouvellement, isRdo);

    Assertions.assertEquals("2022-07-01", dates.getDebut());
    Assertions.assertEquals("2022-12-30", dates.getFin());

    dates = getDatesRenouvellement(pc, "2022-12-31", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);

    dates = getDatesRenouvellement(pc, "2023-12-31", debutRenouvellement, isRdo);
    Assertions.assertNull(dates);
  }

  private static Periode getDatesRenouvellement(
      ParametrageCarteTP pc, String dateSouscription, String debutRenouvellement, boolean isRdo) {
    DroitAssure tspd = new DroitAssure();
    tspd.setPeriode(new Periode());
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggerBenef(dateSouscription, "", "", "");
    Trigger trigger = new Trigger();
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setRdo(isRdo);
    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(tb);
    parametrageTrigger.setParametrageCarteTP(pc);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(debutRenouvellement);
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());
    parametrageTrigger.setDateSouscription(dateSouscription);
    return getPeriodeFromParametrage(parametrageTrigger);
  }

  private static ParametrageCarteTP setupParametrageCarteManuelAnniversaire(
      String declManuel, String debutRenouvellement, int delai) {
    // type of config
    ModeDeclenchementCarteTP mode = ModeDeclenchementCarteTP.Manuel;
    DateRenouvellementCarteTP type = DateRenouvellementCarteTP.AnniversaireContrat;

    String debutEcheance = null;

    ParametrageCarteTP pc =
        TriggerDataForTesting.getParam(mode, type, declManuel, debutEcheance, delai);
    pc.getParametrageRenouvellement().setDateExecutionBatch(declManuel);
    pc.getParametrageRenouvellement().setDateDebutDroitTP(debutRenouvellement);
    return pc;
  }
}
