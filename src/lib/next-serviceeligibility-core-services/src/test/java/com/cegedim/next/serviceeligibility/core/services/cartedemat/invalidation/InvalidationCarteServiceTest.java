package com.cegedim.next.serviceeligibility.core.services.cartedemat.invalidation;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.config.UtilsForTesting;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class InvalidationCarteServiceTest {

  @Autowired private InvalidationCarteService invalidationCarteService;

  @Autowired ObjectMapper objectMapper;

  private static final String DECLARATION_PATH = "src/test/resources/620-declarations/";
  private static final String CARTES_DEMAT_PATH = "src/test/resources/620-CarteDemat/";

  /** Cas 4 bis => carte invalide */
  @Test
  void cas4bis() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas4-1.json", CarteDemat.class);
    boolean invalid =
        invalidationCarteService.shouldInvalidCarte(carte, "2024/01/01", "2023/12/31");
    Assertions.assertTrue(invalid);
  }

  /** Cas 5 bis => carte invalide */
  @Test
  void cas5bis() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas5-1.json", CarteDemat.class);
    boolean invalid =
        invalidationCarteService.shouldInvalidCarte(carte, "2024/01/01", "2024/12/31");
    Assertions.assertTrue(invalid);
  }

  @Test
  void checkSameGuaranteesWithNoChanges() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas5-1.json", CarteDemat.class);
    Declaration declaration =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-2.json", Declaration.class);

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte),
            List.of(declaration),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_INSURER,
            new ArrayList<>());
    Assertions.assertFalse(invalids.isEmpty());
  }

  @Test
  void checkSameGuaranteesWithNewGuarantee() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas5-1.json", CarteDemat.class);
    Declaration declaration =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-2.json", Declaration.class);
    Declaration declarationWithNewGuarantee = new Declaration(declaration);
    declarationWithNewGuarantee.getDomaineDroits().get(0).setCodeGarantie("KC_PlatineBase2");

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte),
            List.of(declaration, declarationWithNewGuarantee),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_INSURER,
            new ArrayList<>());
    Assertions.assertFalse(invalids.isEmpty());
  }

  @Test
  void checkOldFermetureWithNewCardForOTP() throws IOException {
    CarteDemat carte2025 =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas9-1.json", CarteDemat.class);
    CarteDemat carte2026 =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas9-2.json", CarteDemat.class);
    Declaration declarationR2025 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas9-1.json", Declaration.class);

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte2025, carte2026),
            List.of(declarationR2025),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_OTP,
            new ArrayList<>());
    Assertions.assertEquals(1, invalids.size());
    Assertions.assertEquals(carte2025, invalids.get(0));
  }

  @Test
  void checkDoubleFermetureWithNewCardForOTP() throws IOException {
    CarteDemat carte2025 =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas9-1.json", CarteDemat.class);
    CarteDemat carte2026 =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas9-2.json", CarteDemat.class);
    Declaration declarationR2025 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas9-1.json", Declaration.class);
    Declaration declarationR2026 =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas9-2.json", Declaration.class);

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte2025, carte2026),
            List.of(declarationR2025, declarationR2026),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_OTP,
            new ArrayList<>());
    Assertions.assertEquals(2, invalids.size());
  }

  @Test
  void checkSameGuaranteesWithChanges() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas5-1.json", CarteDemat.class);
    Declaration declaration =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-2.json", Declaration.class);

    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2024/01/01");
    periodeDroit.setPeriodeFin("2024/12/31");
    DomaineDroit domaineDroit1 = new DomaineDroit(),
        domaineDroit2 = new DomaineDroit(),
        domaineDroit3 = new DomaineDroit();
    domaineDroit1.setCode("HOSP");
    domaineDroit1.setCodeGarantie("GT1");
    domaineDroit1.setPeriodeDroit(periodeDroit);
    domaineDroit2.setCode("OPTI");
    domaineDroit2.setCodeGarantie("GT2");
    domaineDroit2.setPeriodeDroit(periodeDroit);
    domaineDroit3.setCode("DENT");
    domaineDroit3.setCodeGarantie("GT3");
    domaineDroit3.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit1, domaineDroit2, domaineDroit3));
    carte.getBeneficiaires().get(0).setDomainesCouverture(List.of(domaineDroit3, domaineDroit1));

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte),
            List.of(declaration),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_OTP,
            new ArrayList<>());
    Assertions.assertFalse(invalids.isEmpty());
  }

  @Test
  void checkSameGuaranteesWithNoChanges2() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas5-1.json", CarteDemat.class);
    Declaration declaration =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-2.json", Declaration.class);

    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2024/01/01");
    periodeDroit.setPeriodeFin("2024/12/31");
    DomaineDroit domaineDroit1 = new DomaineDroit(),
        domaineDroit2 = new DomaineDroit(),
        domaineDroit3 = new DomaineDroit();
    domaineDroit1.setCode("HOSP");
    domaineDroit1.setCodeGarantie("GT1");
    domaineDroit1.setPeriodeDroit(periodeDroit);
    domaineDroit2.setCode("OPTI");
    domaineDroit2.setCodeGarantie("GT2");
    domaineDroit2.setPeriodeDroit(periodeDroit);
    domaineDroit3.setCode("DENT");
    domaineDroit3.setCodeGarantie("GT1");
    domaineDroit3.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit1, domaineDroit2, domaineDroit3));
    carte.getBeneficiaires().get(0).setDomainesCouverture(List.of(domaineDroit3, domaineDroit2));

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte),
            List.of(declaration),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_OTP,
            new ArrayList<>());
    Assertions.assertFalse(invalids.isEmpty());
  }

  @Test
  void checkSameGuaranteesWithDifferentTimePeriods() throws IOException {
    CarteDemat carte =
        UtilsForTesting.createTFromJson(
            CARTES_DEMAT_PATH + "carteDematCas5-1.json", CarteDemat.class);
    Declaration declaration =
        UtilsForTesting.createTFromJson(
            DECLARATION_PATH + "declarationCas5-2.json", Declaration.class);

    DomaineDroit domaineDroit1 = new DomaineDroit(),
        domaineDroit2 = new DomaineDroit(),
        domaineDroit3 = new DomaineDroit();
    PeriodeDroit periodeDroit1 = new PeriodeDroit(),
        periodeDroit2 = new PeriodeDroit(),
        periodeDroit3 = new PeriodeDroit();
    periodeDroit1.setPeriodeDebut("2024/01/01");
    periodeDroit1.setPeriodeFin("2024/06/03");
    periodeDroit2.setPeriodeDebut("2024/06/04");
    periodeDroit2.setPeriodeFin("2024/10/01");
    periodeDroit3.setPeriodeDebut("2024/10/02");
    periodeDroit3.setPeriodeFin("2024/12/31");

    domaineDroit1.setCode("OPTI");
    domaineDroit1.setCodeGarantie("GT1");
    domaineDroit1.setPeriodeDroit(periodeDroit1);
    domaineDroit2.setCode("OPTI");
    domaineDroit2.setCodeGarantie("GT2");
    domaineDroit2.setPeriodeDroit(periodeDroit2);
    domaineDroit3.setCode("OPTI");
    domaineDroit3.setCodeGarantie("GT3");
    domaineDroit3.setPeriodeDroit(periodeDroit3);
    declaration.setDomaineDroits(List.of(domaineDroit1, domaineDroit2, domaineDroit3));

    DomaineDroit domaineDroitCarte1 = new DomaineDroit(domaineDroit3);
    domaineDroitCarte1.setPeriodeDroit(new PeriodeDroit(periodeDroit1));
    DomaineDroit domaineDroitCarte2 = new DomaineDroit(domaineDroit2);
    domaineDroitCarte2.setPeriodeDroit(new PeriodeDroit(periodeDroit3));
    DomaineDroit domaineDroitCarte3 = new DomaineDroit(domaineDroit1);
    domaineDroitCarte3.setPeriodeDroit(new PeriodeDroit(periodeDroit2));
    carte
        .getBeneficiaires()
        .get(0)
        .setDomainesCouverture(List.of(domaineDroitCarte1, domaineDroitCarte3, domaineDroitCarte2));

    List<CarteDemat> invalids =
        invalidationCarteService.invalidationCartes(
            List.of(carte),
            List.of(declaration),
            Declaration::getDomaineDroits,
            Constants.CLIENT_TYPE_INSURER,
            new ArrayList<>());
    Assertions.assertFalse(invalids.isEmpty());
  }
}
