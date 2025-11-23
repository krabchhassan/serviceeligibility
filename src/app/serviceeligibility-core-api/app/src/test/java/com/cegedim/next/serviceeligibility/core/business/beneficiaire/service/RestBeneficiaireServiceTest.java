package com.cegedim.next.serviceeligibility.core.business.beneficiaire.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.dao.BeneficiaireConsultationHistoryDao;
import com.cegedim.next.serviceeligibility.core.model.entity.BeneficiaireConsultationHistory;
import com.cegedim.next.serviceeligibility.core.services.bdd.RestBeneficiaireService;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class RestBeneficiaireServiceTest {

  @Autowired BeneficiaireConsultationHistoryDao beneficiaireConsultationHistoryDao;

  @Autowired RestBeneficiaireService restBeneficiaireService;

  @Test
  void findIdHistoryTest() {
    List<BeneficiaireConsultationHistory> benefHistory = new ArrayList<>();
    BeneficiaireConsultationHistory beneficiaireConsultationHistory1 =
        new BeneficiaireConsultationHistory();
    beneficiaireConsultationHistory1.setUser("testUser");
    beneficiaireConsultationHistory1.setIdElasticBeneficiaire("01234567");
    beneficiaireConsultationHistory1.setExternalOrigin(true);
    beneficiaireConsultationHistory1.setDateConsultation(
        DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    benefHistory.add(beneficiaireConsultationHistory1);
    BeneficiaireConsultationHistory beneficiaireConsultationHistory2 =
        new BeneficiaireConsultationHistory();
    beneficiaireConsultationHistory2.setUser("testUser");
    beneficiaireConsultationHistory2.setIdElasticBeneficiaire("01234568");
    beneficiaireConsultationHistory2.setExternalOrigin(false);
    beneficiaireConsultationHistory2.setDateConsultation(
        DateUtils.parseDate("2022/01/01", DateUtils.FORMATTERSLASHED));
    benefHistory.add(beneficiaireConsultationHistory2);
    BeneficiaireConsultationHistory beneficiaireConsultationHistory3 =
        new BeneficiaireConsultationHistory();
    beneficiaireConsultationHistory3.setUser("testUser");
    beneficiaireConsultationHistory3.setIdElasticBeneficiaire("01234569");
    beneficiaireConsultationHistory3.setExternalOrigin(true);
    beneficiaireConsultationHistory3.setDateConsultation(
        DateUtils.parseDate("2023/01/01", DateUtils.FORMATTERSLASHED));
    benefHistory.add(beneficiaireConsultationHistory3);

    String user = "testUser";
    Mockito.when(
            beneficiaireConsultationHistoryDao.findBeneficiaireConsultationHistoriesByCriteria(
                user, true))
        .thenReturn(benefHistory);

    List<Pair<Boolean, String>> idList = restBeneficiaireService.findIdHistory(user);
    Assertions.assertEquals(3, idList.size());
    Assertions.assertEquals("01234567", idList.get(0).getRight());
    Assertions.assertTrue(idList.get(0).getLeft());
    Assertions.assertEquals("01234568", idList.get(1).getRight());
    Assertions.assertFalse(idList.get(1).getLeft());
    Assertions.assertEquals("01234569", idList.get(2).getRight());
    Assertions.assertTrue(idList.get(2).getLeft());
  }

  @Test
  void addHistoryTest() {
    String user = "testUser";
    List<BeneficiaireConsultationHistory> benefHistory = new ArrayList<>();
    BeneficiaireConsultationHistory beneficiaireConsultationHistory1 =
        new BeneficiaireConsultationHistory();
    beneficiaireConsultationHistory1.setUser("testUser");
    beneficiaireConsultationHistory1.setIdElasticBeneficiaire("01234567");
    beneficiaireConsultationHistory1.setExternalOrigin(true);
    beneficiaireConsultationHistory1.setDateConsultation(
        DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    benefHistory.add(beneficiaireConsultationHistory1);
    BeneficiaireConsultationHistory beneficiaireConsultationHistory2 =
        new BeneficiaireConsultationHistory();
    beneficiaireConsultationHistory2.setUser("testUser");
    beneficiaireConsultationHistory2.setIdElasticBeneficiaire("01234568");
    beneficiaireConsultationHistory2.setExternalOrigin(false);
    beneficiaireConsultationHistory2.setDateConsultation(
        DateUtils.parseDate("2022/01/01", DateUtils.FORMATTERSLASHED));
    benefHistory.add(beneficiaireConsultationHistory2);
    BeneficiaireConsultationHistory beneficiaireConsultationHistory3 =
        new BeneficiaireConsultationHistory();
    beneficiaireConsultationHistory3.setUser("testUser");
    beneficiaireConsultationHistory3.setIdElasticBeneficiaire("01234569");
    beneficiaireConsultationHistory3.setExternalOrigin(true);
    beneficiaireConsultationHistory3.setDateConsultation(
        DateUtils.parseDate("2023/01/01", DateUtils.FORMATTERSLASHED));
    benefHistory.add(beneficiaireConsultationHistory3);

    Mockito.when(
            beneficiaireConsultationHistoryDao.findBeneficiaireConsultationHistoriesByCriteria(
                user, true))
        .thenReturn(benefHistory);

    BeneficiaireConsultationHistory newBeneficiaireConsultationHistory =
        new BeneficiaireConsultationHistory();
    newBeneficiaireConsultationHistory.setUser("testUser");
    newBeneficiaireConsultationHistory.setIdElasticBeneficiaire("01234569");
    newBeneficiaireConsultationHistory.setDateConsultation(
        DateUtils.parseDate("2023/01/01", DateUtils.FORMATTERSLASHED));

    restBeneficiaireService.addHistory("01234569", "external", user);
    List<Pair<Boolean, String>> idList = restBeneficiaireService.findIdHistory(user);
    Assertions.assertEquals(3, idList.size());
    Assertions.assertEquals("01234567", idList.get(0).getRight());
    Assertions.assertTrue(idList.get(0).getLeft());
    Assertions.assertEquals("01234568", idList.get(1).getRight());
    Assertions.assertFalse(idList.get(1).getLeft());
    Assertions.assertEquals("01234569", idList.get(2).getRight());
    Assertions.assertTrue(idList.get(2).getLeft());
  }
}
