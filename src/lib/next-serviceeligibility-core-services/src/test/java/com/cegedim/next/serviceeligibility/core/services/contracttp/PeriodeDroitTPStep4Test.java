package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class PeriodeDroitTPStep4Test {

  @Autowired private PeriodeDroitTPStep4 periodeDroitTPStep4;

  @Test
  void quiSeSuitTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2021/01/01", "2021/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPeriodes().size());
    Periode periode = res.getPeriodes().get(0);
    Assertions.assertEquals("2020/01/01", periode.getDebut());
    Assertions.assertEquals("2021/12/31", periode.getFin());
  }

  @Test
  void avecUnTrouTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2021/02/01", "2021/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(2, res.getPeriodes().size());

    Periode periode1 = res.getPeriodes().get(0);
    Assertions.assertEquals("2020/01/01", periode1.getDebut());
    Assertions.assertEquals("2020/12/31", periode1.getFin());

    Periode periode2 = res.getPeriodes().get(1);
    Assertions.assertEquals("2021/02/01", periode2.getDebut());
    Assertions.assertEquals("2021/12/31", periode2.getFin());
  }

  @Test
  void quiFiniAvantTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2019/01/01", "2019/12/25"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(2, res.getPeriodes().size());

    Periode periode2 = res.getPeriodes().get(0);
    Assertions.assertEquals("2020/01/01", periode2.getDebut());
    Assertions.assertEquals("2020/12/31", periode2.getFin());

    Periode periode1 = res.getPeriodes().get(1);
    Assertions.assertEquals("2019/01/01", periode1.getDebut());
    Assertions.assertEquals("2019/12/25", periode1.getFin());
  }

  @Test
  void quiAgranditAvantEtApresTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2019/01/01", "2023/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPeriodes().size());

    Periode periode = res.getPeriodes().get(0);
    Assertions.assertEquals("2019/01/01", periode.getDebut());
    Assertions.assertEquals("2023/12/31", periode.getFin());
  }

  @Test
  void quiRapetitTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2020/02/01", "2020/10/15"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPeriodes().size());

    Periode periode = res.getPeriodes().get(0);
    Assertions.assertEquals("2020/01/01", periode.getDebut());
    Assertions.assertEquals("2020/12/31", periode.getFin());
  }

  @Test
  void quiEcraseTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("1");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2020/01/01", "2020/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPeriodes().size());

    Periode periode = res.getPeriodes().get(0);
    Assertions.assertEquals("2020/01/01", periode.getDebut());
    Assertions.assertEquals("2020/12/31", periode.getFin());
    Assertions.assertEquals("1", res.getTypeConventionnement().getCode());
  }

  @Test
  void quiCoupeTDB() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("1");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2020/04/01", "2020/07/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(2, toUpdate.size());

    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertNull(res.getTypeConventionnement().getCode());
    Assertions.assertEquals(2, res.getPeriodes().size());
    Assertions.assertEquals("2020/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/03/31", res.getPeriodes().get(0).getFin());
    Assertions.assertEquals("2020/08/01", res.getPeriodes().get(1).getDebut());
    Assertions.assertEquals("2020/12/31", res.getPeriodes().get(1).getFin());

    res = toUpdate.get(1);
    Assertions.assertEquals("1", res.getTypeConventionnement().getCode());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2020/04/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/07/31", res.getPeriodes().get(0).getFin());
  }

  @Test
  void quiCoupeTDB2() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("1");

    ConventionnementContrat conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("2");
    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2020/04/01", "2020/07/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(3, toUpdate.size());

    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertNull(res.getTypeConventionnement().getCode());
    Assertions.assertEquals(2, res.getPeriodes().size());
    Assertions.assertEquals("2020/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/03/31", res.getPeriodes().get(0).getFin());
    Assertions.assertEquals("2020/08/01", res.getPeriodes().get(1).getDebut());
    Assertions.assertEquals("2020/12/31", res.getPeriodes().get(1).getFin());

    res = toUpdate.get(1);
    Assertions.assertEquals("1", res.getTypeConventionnement().getCode());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2020/04/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/07/31", res.getPeriodes().get(0).getFin());

    res = toUpdate.get(2);
    Assertions.assertEquals("2", res.getTypeConventionnement().getCode());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2020/04/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/07/31", res.getPeriodes().get(0).getFin());
  }

  @Test
  void agrandiOuverture() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2021/01/01", "2021/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());

    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2020/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2021/12/31", res.getPeriodes().get(0).getFin());
  }

  @Test
  void faitRienOuverture() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/12/31");
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2020/01/01", "2020/06/01"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());

    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2020/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/12/31", res.getPeriodes().get(0).getFin());
  }

  @Test
  void mergeOuverture() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();

    // Contrat
    ConventionnementContrat conventionnementContrat =
        createConventionnementContrat("2020/01/01", "2020/05/01");
    conventionnementContrat.getPeriodes().add(new Periode("2020/06/01", "2020/12/31"));
    toUpdate.add(conventionnementContrat);

    // Declaration
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2020/01/01", "2020/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());

    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2020/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2020/12/31", res.getPeriodes().get(0).getFin());
  }

  @Test
  void casComplexeReynald() {
    List<ConventionnementContrat> toUpdate = new ArrayList<>();
    /*
     * IS- 2023/08/01 2023/12/31
     *
     * Consolidation : IS - 0 01/08 => 31/12
     */
    ConventionnementContrat conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("IS");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration),
        new Periode("2023/08/01", "2023/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(1, toUpdate.size());
    ConventionnementContrat res = toUpdate.get(0);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("2023/08/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());

    /*
     * KA-/IS- 2023/07/01 2023/08/31
     *
     *
     * Consolidation : IS - 0 01/09 => 31/12 KA - 0 01/07 => 31/08 IS - 1 01/07 =>
     * 31/08
     */
    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("IS");
    conventionnementDeclaration.setPriorite(1);

    ConventionnementContrat conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("KA");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/07/01", "2023/08/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(3, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/09/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/07/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/08/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(2);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/07/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/08/31", res.getPeriodes().get(0).getFin());

    /*
     * KA-/IS- 2023/07/01 2023/12/31
     *
     * Consolidation : KA - 0 01/07 => 31/12 IS - 1 01/07 => 31/12
     *
     */
    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("IS");
    conventionnementDeclaration.setPriorite(1);

    conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("KA");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/07/01", "2023/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(2, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/07/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/07/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());

    /*
     * CB-/IS- 2023/05/01 2023/06/30
     *
     * Consolidation : CB - 0 01/05 => 30/06 KA - 0 01/07 => 31/12 IS - 1 01/05 =>
     * 31/12
     */
    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("IS");
    conventionnementDeclaration.setPriorite(1);
    conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("CB");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/05/01", "2023/06/30"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(3, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/05/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/07/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(2);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("CB", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/05/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/06/30", res.getPeriodes().get(0).getFin());

    /*
     * CB-/IS- 2023/05/01 2023/12/31
     *
     * Consolidation : CB - 0 01/05 => 31/12 IS - 1 01/05 => 31/12
     */
    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("IS");
    conventionnementDeclaration.setPriorite(1);
    conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("CB");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/05/01", "2023/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(2, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/05/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("CB", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/05/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());

    /*
     * VM-/KA- 2023/03/01 2023/04/30
     *
     * Consolidation : VM - 0 01/03 => 30/04 KA - 1 01/03 => 30/04
     *
     * CB - 0 01/05 => 31/12 IS - 1 01/05 => 31/12
     */
    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("KA");
    conventionnementDeclaration.setPriorite(1);
    conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("VM");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/03/01", "2023/04/30"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(4, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/05/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("CB", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/05/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(2);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/03/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/04/30", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(3);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("VM", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/03/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/04/30", res.getPeriodes().get(0).getFin());

    /*
     * VM-/KA- 2023/03/01 2023/12/31
     *
     * Consolidation : VM - 0 01/03 => 31/12 KA - 1 01/03 => 31/12
     */

    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("KA");
    conventionnementDeclaration.setPriorite(1);
    conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("VM");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/03/01", "2023/12/31"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(2, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/03/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("VM", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/03/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());

    /*
     * KA/IS- 2023/01/01 2023/02/28
     *
     * Consolidation : KA - 0 01/01 => 28/02 IS - 1 01/01 => 28/02
     *
     * VM - 0 01/03 => 31/12 KA - 1 01/03 => 31/12
     */
    conventionnementDeclaration = createConventionnementContrat();
    conventionnementDeclaration.getTypeConventionnement().setCode("IS");
    conventionnementDeclaration.setPriorite(1);
    conventionnementDeclaration2 = createConventionnementContrat();
    conventionnementDeclaration2.getTypeConventionnement().setCode("KA");

    periodeDroitTPStep4.consolidatePeriods(
        toUpdate,
        List.of(conventionnementDeclaration, conventionnementDeclaration2),
        new Periode("2023/01/01", "2023/02/28"),
        ConventionnementContrat::getPeriodes,
        true);

    Assertions.assertEquals(4, toUpdate.size());
    res = toUpdate.get(0);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/03/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(1);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("VM", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/03/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/12/31", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(2);
    Assertions.assertEquals(1, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("IS", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/02/28", res.getPeriodes().get(0).getFin());
    res = toUpdate.get(3);
    Assertions.assertEquals(0, res.getPriorite());
    Assertions.assertEquals(1, res.getPeriodes().size());
    Assertions.assertEquals("KA", res.getTypeConventionnement().getCode());
    Assertions.assertEquals("2023/01/01", res.getPeriodes().get(0).getDebut());
    Assertions.assertEquals("2023/02/28", res.getPeriodes().get(0).getFin());
  }

  private ConventionnementContrat createConventionnementContrat(String debut, String fin) {
    ConventionnementContrat conventionnementContrat = createConventionnementContrat();
    Periode periode = new Periode(debut, fin);
    conventionnementContrat.setPeriodes(new ArrayList<>(List.of(periode)));
    return conventionnementContrat;
  }

  private ConventionnementContrat createConventionnementContrat() {
    ConventionnementContrat conventionnementContrat = new ConventionnementContrat();
    conventionnementContrat.setPriorite(0);
    conventionnementContrat.setTypeConventionnement(new TypeConventionnement());
    return conventionnementContrat;
  }
}
