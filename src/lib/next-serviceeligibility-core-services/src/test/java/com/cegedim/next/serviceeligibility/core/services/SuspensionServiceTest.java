package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class SuspensionServiceTest {
  public static final String MIN_PERIODE_DEBUT = "2025/01/01";
  public static final String MAX_PERIODE_FIN = "2025/12/31";
  @Autowired SuspensionService suspensionService;

  private PeriodeSuspensionDeclaration createSuspension(String debut, String fin) {
    PeriodeSuspensionDeclaration suspension = new PeriodeSuspensionDeclaration();
    suspension.setDebut(debut);
    suspension.setFin(fin);
    return suspension;
  }

  @Test
  void test_getLastSuspensionActive_1() {
    List<PeriodeSuspensionDeclaration> periodeSuspensionList = new ArrayList<>();
    PeriodeSuspensionDeclaration suspension1 = createSuspension("2025/02/01", "2025/03/31");
    PeriodeSuspensionDeclaration suspension2 = createSuspension("2025/05/01", "2025/04/30");
    PeriodeSuspensionDeclaration suspension3 = createSuspension("2025/09/01", "2025/08/31");
    periodeSuspensionList.add(suspension1);
    periodeSuspensionList.add(suspension2);
    periodeSuspensionList.add(suspension3);

    PeriodeSuspensionDeclaration lastSuspensionActive =
        suspensionService.getLastSuspensionActive(
            periodeSuspensionList, MIN_PERIODE_DEBUT, MAX_PERIODE_FIN);
    Assertions.assertNotNull(lastSuspensionActive);
    Assertions.assertEquals(lastSuspensionActive.getDebut(), suspension1.getDebut());
    Assertions.assertEquals(lastSuspensionActive.getFin(), suspension1.getFin());
  }

  @Test
  void test_getLastSuspensionActive_2() {
    List<PeriodeSuspensionDeclaration> periodeSuspensionList = new ArrayList<>();
    PeriodeSuspensionDeclaration suspension1 = createSuspension("2025/02/01", "2025/03/31");
    PeriodeSuspensionDeclaration suspension2 = createSuspension("2025/05/01", "2025/04/30");
    PeriodeSuspensionDeclaration suspension3 = createSuspension("2025/09/01", null);
    periodeSuspensionList.add(suspension1);
    periodeSuspensionList.add(suspension2);
    periodeSuspensionList.add(suspension3);

    PeriodeSuspensionDeclaration lastSuspensionActive =
        suspensionService.getLastSuspensionActive(
            periodeSuspensionList, MIN_PERIODE_DEBUT, MAX_PERIODE_FIN);
    Assertions.assertNotNull(lastSuspensionActive);
    Assertions.assertEquals(lastSuspensionActive.getDebut(), suspension3.getDebut());
    Assertions.assertEquals(lastSuspensionActive.getFin(), suspension3.getFin());
  }

  @Test
  void test_getLastSuspensionActive_3() {
    List<PeriodeSuspensionDeclaration> periodeSuspensionList = new ArrayList<>();
    PeriodeSuspensionDeclaration suspension1 = createSuspension("2024/02/01", "2024/03/31");
    PeriodeSuspensionDeclaration suspension2 = createSuspension("2024/05/01", "2024/06/30");
    PeriodeSuspensionDeclaration suspension3 = createSuspension("2024/09/01", "2024/10/31");
    periodeSuspensionList.add(suspension1);
    periodeSuspensionList.add(suspension2);
    periodeSuspensionList.add(suspension3);

    PeriodeSuspensionDeclaration lastSuspensionActive =
        suspensionService.getLastSuspensionActive(
            periodeSuspensionList, MIN_PERIODE_DEBUT, MAX_PERIODE_FIN);
    Assertions.assertNull(lastSuspensionActive);
  }

  @Test
  void test_getLastSuspensionActive_4() {
    List<PeriodeSuspensionDeclaration> periodeSuspensionList = new ArrayList<>();
    PeriodeSuspensionDeclaration suspension1 = createSuspension("2024/02/01", "2024/03/31");
    PeriodeSuspensionDeclaration suspension2 = createSuspension("2024/05/01", "2024/06/30");
    PeriodeSuspensionDeclaration suspension3 = createSuspension("2024/09/01", null);
    periodeSuspensionList.add(suspension1);
    periodeSuspensionList.add(suspension2);
    periodeSuspensionList.add(suspension3);

    PeriodeSuspensionDeclaration lastSuspensionActive =
        suspensionService.getLastSuspensionActive(
            periodeSuspensionList, MIN_PERIODE_DEBUT, MAX_PERIODE_FIN);
    Assertions.assertNotNull(lastSuspensionActive);
    Assertions.assertEquals(lastSuspensionActive.getDebut(), suspension3.getDebut());
    Assertions.assertEquals(lastSuspensionActive.getFin(), suspension3.getFin());
  }

  @Test
  void test_getLastSuspensionActive_5() {
    List<PeriodeSuspensionDeclaration> periodeSuspensionList = new ArrayList<>();
    PeriodeSuspensionDeclaration suspension1 = createSuspension("2024/02/01", "2024/03/31");
    PeriodeSuspensionDeclaration suspension2 = createSuspension("2024/05/01", "2024/06/30");
    PeriodeSuspensionDeclaration suspension3 = createSuspension("2024/12/01", "2025/01/31");
    periodeSuspensionList.add(suspension1);
    periodeSuspensionList.add(suspension2);
    periodeSuspensionList.add(suspension3);

    PeriodeSuspensionDeclaration lastSuspensionActive =
        suspensionService.getLastSuspensionActive(
            periodeSuspensionList, MIN_PERIODE_DEBUT, MAX_PERIODE_FIN);
    Assertions.assertNotNull(lastSuspensionActive);
    Assertions.assertEquals(lastSuspensionActive.getDebut(), suspension3.getDebut());
    Assertions.assertEquals(lastSuspensionActive.getFin(), suspension3.getFin());
  }

  @Test
  void test_getLastSuspensionActive_6() {
    List<PeriodeSuspensionDeclaration> periodeSuspensionList = new ArrayList<>();
    PeriodeSuspensionDeclaration suspension1 = createSuspension("2024/02/01", "2024/01/31");
    PeriodeSuspensionDeclaration suspension2 = createSuspension("2024/05/01", "2024/04/30");
    PeriodeSuspensionDeclaration suspension3 = createSuspension("2024/09/01", "2024/08/31");
    periodeSuspensionList.add(suspension1);
    periodeSuspensionList.add(suspension2);
    periodeSuspensionList.add(suspension3);

    PeriodeSuspensionDeclaration lastSuspensionActive =
        suspensionService.getLastSuspensionActive(
            periodeSuspensionList, MIN_PERIODE_DEBUT, MAX_PERIODE_FIN);
    Assertions.assertNull(lastSuspensionActive);
  }
}
