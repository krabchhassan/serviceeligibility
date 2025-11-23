package com.cegedim.next.serviceeligibility.core.bdd.service.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.CodeRenvoiTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.ConventionTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.FondCarteTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.RegroupementDomainesTPDto;
import com.cegedim.next.serviceeligibility.core.features.utils.DeclarantUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DeclarantUtilsTest {
  static ConventionTPDto conventionA = new ConventionTPDto();
  static ConventionTPDto conventionB = new ConventionTPDto();
  static ConventionTPDto conventionC = new ConventionTPDto();

  @BeforeAll
  static void setUp() {
    conventionA.setReseauSoin("KA");
    conventionA.setConventionCible("VM");
    conventionA.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    conventionA.setDateFin(LocalDateTime.of(2020, 1, 1, 0, 0));

    conventionB.setReseauSoin("CB");
    conventionB.setDomaineTP("COSP");
    conventionB.setConventionCible("AL");
    conventionB.setDateDebut(LocalDateTime.of(2020, 1, 1, 0, 0));
    conventionB.setDateFin(LocalDateTime.of(2021, 1, 1, 0, 0));

    conventionC.setReseauSoin("IT");
    conventionC.setDomaineTP("HOSP");
    conventionC.setConventionCible("CB");
    conventionC.setDateDebut(LocalDateTime.of(2020, 1, 1, 0, 0));
    conventionC.setDateFin(LocalDateTime.of(2019, 12, 31, 0, 0));
  }

  @Test
  void test_isConventionEqual() {
    setUp();
    ConventionTPDto conventionTest = new ConventionTPDto();
    conventionTest.setReseauSoin("CB");
    conventionTest.setDomaineTP("COSP");
    conventionTest.setConventionCible("AL");
    conventionTest.setDateDebut(LocalDateTime.of(2020, 1, 1, 0, 0));
    conventionTest.setDateFin(LocalDateTime.of(2021, 1, 1, 0, 0));

    Assertions.assertTrue(DeclarantUtils.isConventionTPEqual(conventionTest, conventionB));

    conventionTest.setDomaineTP(null);
    Assertions.assertFalse(DeclarantUtils.isConventionTPEqual(conventionTest, conventionB));

    conventionTest.setDomaineTP("DENT");
    Assertions.assertFalse(DeclarantUtils.isConventionTPEqual(conventionTest, conventionB));
  }

  @Test
  void createCodeRenvoiNotUniqueTest() {
    LocalDateTime localDateTime = LocalDateTime.now();
    List<CodeRenvoiTPDto> codesRenvoiTPDto = new ArrayList<>();

    CodeRenvoiTPDto codeRenvoi1 = new CodeRenvoiTPDto();
    codeRenvoi1.setDomaineTP("LPPS");
    codeRenvoi1.setReseauSoin("IT");
    codeRenvoi1.setDateDebut(localDateTime);
    codesRenvoiTPDto.add(codeRenvoi1);
    codesRenvoiTPDto.add(codeRenvoi1);

    Assertions.assertFalse(DeclarantUtils.checkUnicityCodesRenvoiOK(codesRenvoiTPDto));
  }

  @Test
  void createCodeRenvoiNotUniqueButDifferentPeriodTest() {
    LocalDateTime localDateTime = LocalDateTime.now();
    LocalDateTime localDateTime2 = LocalDateTime.MAX;
    List<CodeRenvoiTPDto> codesRenvoiTPDto = new ArrayList<>();

    CodeRenvoiTPDto codeRenvoi1 = new CodeRenvoiTPDto();
    codeRenvoi1.setDomaineTP("LPPS");
    codeRenvoi1.setReseauSoin("IT");
    codeRenvoi1.setDateDebut(localDateTime);
    codeRenvoi1.setDateFin(localDateTime);
    codesRenvoiTPDto.add(codeRenvoi1);

    CodeRenvoiTPDto codeRenvoi2 = new CodeRenvoiTPDto();
    codeRenvoi2.setDomaineTP("LPPS");
    codeRenvoi2.setReseauSoin("IT");
    codeRenvoi2.setDateDebut(localDateTime2);
    codesRenvoiTPDto.add(codeRenvoi2);

    Assertions.assertTrue(DeclarantUtils.checkUnicityCodesRenvoiOK(codesRenvoiTPDto));
  }

  @Test
  void createConventionsTPUniqueTest() {
    setUp();
    List<ConventionTPDto> conventions = new ArrayList<>();
    conventions.add(conventionA);
    conventions.add(conventionB);
    conventions.add(conventionC);

    Assertions.assertTrue(DeclarantUtils.checkUnicityConventionOK(conventions));

    ConventionTPDto conventionTest = new ConventionTPDto();
    conventionTest.setReseauSoin("IT");
    conventionTest.setDomaineTP("COSP");
    conventionTest.setConventionCible("CB");
    conventionTest.setDateDebut(LocalDateTime.of(2020, 1, 1, 0, 0));
    conventionTest.setDateFin(LocalDateTime.of(2019, 12, 31, 0, 0));
    Assertions.assertTrue(DeclarantUtils.checkUnicityConventionOK(conventions));
  }

  @Test
  void createConventionsTPNotUniqueTest() {
    setUp();
    List<ConventionTPDto> conventions = new ArrayList<>();
    conventions.add(conventionA);
    conventions.add(conventionB);
    conventions.add(conventionC);

    ConventionTPDto conventionTest = new ConventionTPDto();
    conventionTest.setReseauSoin("KA");
    conventionTest.setConventionCible("IT");
    conventionTest.setDateDebut(LocalDateTime.of(2020, 1, 1, 0, 0));
    conventions.add(conventionTest);
    Assertions.assertFalse(DeclarantUtils.checkUnicityConventionOK(conventions));

    conventions.add(conventionB);
    Assertions.assertFalse(DeclarantUtils.checkUnicityConventionOK(conventions));
  }

  @Test
  void createRegroupementsDomainesTPNotUniqueTest() {
    List<RegroupementDomainesTPDto> regroupementsDomainesTP = new ArrayList<>();

    RegroupementDomainesTPDto regroupementDomainesA = new RegroupementDomainesTPDto();
    regroupementDomainesA.setDomaineRegroupementTP("LARA");
    regroupementDomainesA.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    regroupementsDomainesTP.add(regroupementDomainesA);
    regroupementsDomainesTP.add(regroupementDomainesA);

    Assertions.assertFalse(
        DeclarantUtils.checkUnicityRegroupementsDomainesTPOK(regroupementsDomainesTP));
  }

  @Test
  void createRegroupementsDomainesTPNotUniqueTestButDifferentPeriodTest() {
    List<RegroupementDomainesTPDto> regroupementsDomainesTP = new ArrayList<>();

    RegroupementDomainesTPDto regroupementDomainesA = new RegroupementDomainesTPDto();
    regroupementDomainesA.setDomaineRegroupementTP("LARA");
    regroupementDomainesA.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    regroupementDomainesA.setDateFin(LocalDateTime.of(2020, 1, 1, 0, 0));

    RegroupementDomainesTPDto regroupementDomainesB = new RegroupementDomainesTPDto();
    regroupementDomainesB.setDomaineRegroupementTP("LARA");
    regroupementDomainesB.setDateDebut(LocalDateTime.of(2020, 1, 2, 0, 0));

    regroupementsDomainesTP.add(regroupementDomainesA);
    regroupementsDomainesTP.add(regroupementDomainesB);

    Assertions.assertTrue(
        DeclarantUtils.checkUnicityRegroupementsDomainesTPOK(regroupementsDomainesTP));
  }

  @Test
  void createFondCarteTPNotUniqueButDifferentPeriodTest() {
    List<FondCarteTPDto> fondCarteList = new ArrayList<>();
    FondCarteTPDto fondCarteA = new FondCarteTPDto();
    fondCarteA.setReseauSoin("IT");
    fondCarteA.setFondCarte("test1.jpeg");
    fondCarteA.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    fondCarteA.setDateFin(LocalDateTime.of(2020, 1, 1, 0, 0));
    fondCarteList.add(fondCarteA);

    FondCarteTPDto fondCarteB = new FondCarteTPDto();
    fondCarteB.setReseauSoin("KA");
    fondCarteB.setFondCarte("test2.jpeg");
    fondCarteB.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    fondCarteB.setDateFin(LocalDateTime.of(2020, 1, 1, 0, 0));
    fondCarteList.add(fondCarteB);

    FondCarteTPDto fondCarteC = new FondCarteTPDto();
    fondCarteC.setReseauSoin("KA");
    fondCarteC.setFondCarte("test3.jpeg");
    fondCarteC.setDateDebut(LocalDateTime.of(2021, 12, 31, 0, 0));
    fondCarteC.setDateFin(LocalDateTime.of(2022, 1, 1, 0, 0));
    fondCarteList.add(fondCarteC);

    Assertions.assertTrue(DeclarantUtils.checkUnicityFondCarteTPOK(fondCarteList));
  }

  @Test
  void createFondCarteNotUniqueTest() {
    List<FondCarteTPDto> fondCarteList = new ArrayList<>();
    FondCarteTPDto fondCarteA = new FondCarteTPDto();
    fondCarteA.setReseauSoin("IT");
    fondCarteA.setFondCarte("test1.jpeg");
    fondCarteA.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    fondCarteA.setDateFin(LocalDateTime.of(2020, 1, 1, 0, 0));
    fondCarteList.add(fondCarteA);

    FondCarteTPDto fondCarteB = new FondCarteTPDto();
    fondCarteB.setReseauSoin("KA");
    fondCarteB.setFondCarte("test2.jpeg");
    fondCarteB.setDateDebut(LocalDateTime.of(2019, 12, 31, 0, 0));
    fondCarteB.setDateFin(LocalDateTime.of(2020, 1, 1, 0, 0));
    fondCarteList.add(fondCarteB);

    FondCarteTPDto fondCarteC = new FondCarteTPDto();
    fondCarteC.setReseauSoin("KA");
    fondCarteC.setFondCarte("test3.jpeg");
    fondCarteC.setDateDebut(LocalDateTime.of(2020, 1, 1, 0, 0));
    fondCarteC.setDateFin(LocalDateTime.of(2022, 1, 1, 0, 0));
    fondCarteList.add(fondCarteC);

    Assertions.assertFalse(DeclarantUtils.checkUnicityFondCarteTPOK(fondCarteList));
  }
}
