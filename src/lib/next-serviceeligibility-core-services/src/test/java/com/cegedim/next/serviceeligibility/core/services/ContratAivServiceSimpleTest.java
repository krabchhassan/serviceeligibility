package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContratAivServiceSimpleTest {

  @Autowired ContratAivService service;

  ContratAIV6 prepareContractWithSuspension(List<PeriodeSuspension> periodes) {
    ContratAIV6 c = new ContratAIV6();
    c.setPeriodesSuspension(periodes);
    return c;
  }

  PeriodeSuspension preparePeriodeSuspension(String debut, String fin) {
    PeriodeSuspension ps = new PeriodeSuspension();
    Periode p = new Periode(debut, fin);
    ps.setPeriode(p);
    return ps;
  }

  @Test
  void test_suppression_event1() {
    List<PeriodeSuspension> oldPeriodes = new ArrayList<>();
    List<PeriodeSuspension> newPeriodes = new ArrayList<>();
    oldPeriodes.add(preparePeriodeSuspension("2000-01-01", null));

    ContratAIV6 newC = prepareContractWithSuspension(oldPeriodes);
    ContratAIV6 oldC = null;
    String result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.SUSPENSION, result);

    newC.setPeriodesSuspension(null);
    oldC = prepareContractWithSuspension(null);

    newPeriodes.add(preparePeriodeSuspension("2000-01-01", "2000-01-02"));
    oldC.setPeriodesSuspension(newPeriodes);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);

    oldC.setPeriodesSuspension(null);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.ERREUR_SUSPENSION, result);

    newPeriodes.clear();
    newPeriodes.add(preparePeriodeSuspension("2001-01-01", "2001-01-02"));
    newPeriodes.add(preparePeriodeSuspension("2000-01-01", "2000-01-02"));

    oldPeriodes.clear();
    oldPeriodes.add(preparePeriodeSuspension("2000-01-01", "2000-01-02"));

    oldC.setPeriodesSuspension(oldPeriodes);
    newC.setPeriodesSuspension(newPeriodes);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);

    newPeriodes.clear();
    newPeriodes.add(preparePeriodeSuspension("2001-01-01", "2001-01-02"));
    oldPeriodes.clear();

    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);
    newPeriodes.clear();
    newPeriodes.add(preparePeriodeSuspension("2001-01-01", "2001-01-02"));
    oldPeriodes.clear();
    oldPeriodes.add(preparePeriodeSuspension("2001-01-01", null));
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);

    newPeriodes.clear();
    newPeriodes.add(preparePeriodeSuspension("2001-01-01", null));
    oldPeriodes.clear();
    oldPeriodes.add(preparePeriodeSuspension("2001-02-01", null));
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.SUSPENSION, result);

    newPeriodes.clear();
    oldPeriodes.clear();
    oldPeriodes.add(preparePeriodeSuspension("2000-01-01", "2000-01-02"));

    oldC.setPeriodesSuspension(oldPeriodes);
    newC.setPeriodesSuspension(newPeriodes);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);
  }

  @Test
  void testSuspensionCalculation() {
    List<PeriodeSuspension> oldPeriodes = new ArrayList<>();
    List<PeriodeSuspension> newPeriodes = new ArrayList<>();
    oldPeriodes.add(preparePeriodeSuspension("2024-01-01", null));

    ContratAIV6 newC = prepareContractWithSuspension(oldPeriodes);
    ContratAIV6 oldC = null;
    String result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.SUSPENSION, result);

    oldC = prepareContractWithSuspension(oldPeriodes);

    newPeriodes.add(preparePeriodeSuspension("2024-01-01", "2024-01-02"));
    newC.setPeriodesSuspension(newPeriodes);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);

    oldC.setPeriodesSuspension(new ArrayList<>(newPeriodes));
    newPeriodes.add(preparePeriodeSuspension("2024-02-01", null));
    newC.setPeriodesSuspension(newPeriodes);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.SUSPENSION, result);

    oldC.setPeriodesSuspension(new ArrayList<>(newPeriodes));
    List<PeriodeSuspension> periodes = new ArrayList<>();
    periodes.add(preparePeriodeSuspension("2024-01-01", "2024-01-02"));
    periodes.add(preparePeriodeSuspension("2024-02-01", "2024-02-02"));
    newC.setPeriodesSuspension(periodes);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);

    oldC.setPeriodesSuspension(newPeriodes);
    newC.setPeriodesSuspension(null);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.LEVEE_SUSPENSION, result);

    oldC.setPeriodesSuspension(null);
    newC.setPeriodesSuspension(null);
    result = service.suspensionCalculation(oldC, newC);
    Assertions.assertEquals(Constants.ERREUR_SUSPENSION, result);
  }
}
