package com.cegedim.next.consumer.worker.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cegedim.next.consumer.worker.TestTools;
import com.cegedim.next.consumer.worker.exception.AssureNotFoundException;
import com.cegedim.next.consumer.worker.model.ContratBLB;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.RattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ExtractContratConsumerTest {

  @Mock private ExtractContratConsumer extractContratConsumer = new ExtractContratConsumer();

  @Test
  void calculerLaPlusPetitePeriodeAvecUnePeriodeDAssure()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(
            contrat, beneficiaireId, new ArrayList<ContratBLB>());

    assertEquals("2020-01-01", periode.getLeft());
    assertEquals("2020-06-01", periode.getRight());
  }

  @Test
  void calculerLaPlusPetitePeriodeAvecPlusieursPeriodesDAssure()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final List<Periode> periodesAssure =
        List.of(
            this.buildPeriode("2020-06-01", "2020-12-31"),
            this.buildPeriode("2020-09-01", "2021-12-31"));
    contrat.getAssures().get(0).setPeriodes(periodesAssure);
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(
            contrat, beneficiaireId, new ArrayList<ContratBLB>());

    assertEquals("2020-06-01", periode.getLeft());
    assertEquals("2020-12-01", periode.getRight());
  }

  @Test
  void calculerLaPlusPetitePeriodeAvecPlusieursPeriodesDeDroit()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    final Assure assure = contrat.getAssures().get(0);
    assure.setDroits(
        List.of(
            this.buildDroitAssure("2020-02-01", "2020-02-15"),
            this.buildDroitAssure("2020-02-16", "2020-06-05")));

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(
            contrat, beneficiaireId, new ArrayList<ContratBLB>());

    assertEquals("2020-02-01", periode.getLeft());
    assertEquals("2020-06-01", periode.getRight());
  }

  @Test
  void calculerLaPlusPetitePeriodeSansDateDeFin()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    contrat.getAssures().get(0).setPeriodes(List.of(new Periode("2019-06-03", null)));
    contrat.getAssures().get(0).setDroits(List.of(this.buildDroitAssure("2020-01-01", null)));
    contrat.getAssures().get(0).setIdentite(this.buildIdentiteContratWithoutEndDate());

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(
            contrat, beneficiaireId, new ArrayList<ContratBLB>());

    assertEquals("2020-01-01", periode.getLeft());
    assertNull(periode.getRight());
  }

  @Test
  void calculerLaPlusPetitePeriodeAvecPlusieursDatesDeDroit()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    contrat.getAssures().get(0).setPeriodes(List.of(new Periode("2019-06-03", null)));
    contrat
        .getAssures()
        .get(0)
        .setDroits(
            List.of(
                this.buildDroitAssure("2021-01-01", "2021-12-26"),
                this.buildDroitAssure("2021-12-27", "2022-12-31"),
                this.buildDroitAssure("2023-01-01", "2023-12-31")));
    contrat.getAssures().get(0).setIdentite(this.buildIdentiteContratWithoutEndDate());

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(
            contrat, beneficiaireId, new ArrayList<ContratBLB>());

    assertEquals("2021-01-01", periode.getLeft());
    assertEquals("2023-12-31", periode.getRight());
  }

  @Test
  void calculerLaPlusPetitePeriodeAvecPlusieursDatesDeDroitQuiNeSeSuiventPas()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    contrat.getAssures().get(0).setPeriodes(List.of(new Periode("2019-06-03", null)));
    contrat
        .getAssures()
        .get(0)
        .setDroits(
            List.of(
                this.buildDroitAssure("2021-01-01", "2021-12-26"),
                this.buildDroitAssure("2021-12-31", "2022-12-31"),
                this.buildDroitAssure("2023-01-01", "2023-12-31")));
    contrat.getAssures().get(0).setIdentite(this.buildIdentiteContratWithoutEndDate());

    List<ContratBLB> contratsList = new ArrayList<>();

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(contrat, beneficiaireId, contratsList);

    assertEquals("2021-01-01", periode.getLeft());
    assertEquals("2021-12-26", periode.getRight());
    assertEquals("2021-12-31", contratsList.get(0).getDebut());
    assertEquals("2023-12-31", contratsList.get(0).getFin());
  }

  @Test
  void
      calculerLaPlusPetitePeriodeAvecPlusieursDatesDeDroitQuiNeSeSuiventPasEtDateDeSoucriptionSuperieure()
          throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    contrat.setDateSouscription("2021-01-02");
    contrat.getAssures().get(0).setPeriodes(List.of(new Periode("2019-06-03", null)));
    contrat
        .getAssures()
        .get(0)
        .setDroits(
            List.of(
                this.buildDroitAssure("2001-01-01", "2021-12-26"),
                this.buildDroitAssure("2021-12-31", "2022-12-31"),
                this.buildDroitAssure("2023-01-01", "2023-12-31")));
    contrat.getAssures().get(0).setIdentite(this.buildIdentiteContratWithoutEndDate());

    List<ContratBLB> contratsList = new ArrayList<>();

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(contrat, beneficiaireId, contratsList);

    assertEquals("2021-01-02", periode.getLeft());
    assertEquals("2021-12-26", periode.getRight());
    assertEquals("2021-12-31", contratsList.get(0).getDebut());
    assertEquals("2023-12-31", contratsList.get(0).getFin());
  }

  @Test
  void
      calculerLaPlusPetitePeriodeAvecPlusieursDatesDeDroitQuiNeSeSuiventPasEtDateDeResilInferieure()
          throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    contrat.setDateResiliation("2023-06-01");
    contrat.getAssures().get(0).setPeriodes(List.of(new Periode("2019-06-03", null)));
    contrat
        .getAssures()
        .get(0)
        .setDroits(
            List.of(
                this.buildDroitAssure("2001-01-01", "2021-12-26"),
                this.buildDroitAssure("2021-12-31", "2022-12-31"),
                this.buildDroitAssure("2023-01-01", "2023-12-31")));
    contrat.getAssures().get(0).setIdentite(this.buildIdentiteContratWithoutEndDate());

    List<ContratBLB> contratsList = new ArrayList<>();

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(contrat, beneficiaireId, contratsList);

    assertEquals("2020-01-01", periode.getLeft());
    assertEquals("2021-12-26", periode.getRight());
    assertEquals("2021-12-31", contratsList.get(0).getDebut());
    assertEquals("2023-06-01", contratsList.get(0).getFin());
  }

  @Test
  void calculerLaPlusPetitePeriodeAvecPlusieursAffiliationsRO()
      throws AssureNotFoundException, CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    final BeneficiaireId beneficiaireId = this.buildBeneficiaireId();
    contrat.getAssures().get(0).setIdentite(this.buildIdentiteContratWithAffiliationsRO());
    contrat.getAssures().get(0).setDroits(List.of(this.buildDroitAssure("2020-01-01", null)));
    contrat.getAssures().get(0).setPeriodes(List.of(new Periode("2019-06-03", "2023-12-31")));

    final ImmutablePair<String, String> periode =
        this.extractContratConsumer.calculatePeriod(
            contrat, beneficiaireId, new ArrayList<ContratBLB>());

    assertEquals("2020-01-01", periode.getLeft());
    assertEquals("2023-12-31", periode.getRight());
  }

  @Test
  void shouldAddOneContract() throws CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2020-01-01", "2020-12-31"));
    assertEquals(1, contratsBLB.size());
  }

  @Test
  void shouldAddSimpleContractToList() throws CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat);
    contratBLB.setDebut("2020-01-01");
    contratBLB.setFin("2020-12-31");
    contratsBLB.add(contratBLB);
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2021-01-01", "2021-12-31"));
    assertEquals(2, contratsBLB.size());
    assertEquals("2020-01-01", contratsBLB.get(0).getDebut());
    assertEquals("2020-12-31", contratsBLB.get(0).getFin());
    assertEquals("2021-01-01", contratsBLB.get(1).getDebut());
    assertEquals("2021-12-31", contratsBLB.get(1).getFin());
  }

  @Test
  void shouldAddCutBeforeContractToList() throws CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat);
    contratBLB.setDebut("2020-01-01");
    contratBLB.setFin("2020-12-31");
    contratsBLB.add(contratBLB);
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2019-01-01", "2020-03-31"));
    assertEquals(2, contratsBLB.size());
    assertEquals("2020-01-01", contratsBLB.get(0).getDebut());
    assertEquals("2020-12-31", contratsBLB.get(0).getFin());
    assertEquals("2019-01-01", contratsBLB.get(1).getDebut());
    assertEquals("2019-12-31", contratsBLB.get(1).getFin());
  }

  @Test
  void shouldAddCutAfterContractToList() throws CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat);
    contratBLB.setDebut("2020-01-01");
    contratBLB.setFin("2020-12-31");
    contratsBLB.add(contratBLB);
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2020-06-01", "2022-03-31"));
    assertEquals(2, contratsBLB.size());
    assertEquals("2020-01-01", contratsBLB.get(0).getDebut());
    assertEquals("2020-12-31", contratsBLB.get(0).getFin());
    assertEquals("2021-01-01", contratsBLB.get(1).getDebut());
    assertEquals("2022-03-31", contratsBLB.get(1).getFin());
  }

  @Test
  void shouldAddDuplicateContractToList() throws CloneNotSupportedException {
    final ContratAIV6 contrat = this.buildContractAIV5();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat);
    contratBLB.setDebut("2020-01-01");
    contratBLB.setFin("2020-12-31");
    contratsBLB.add(contratBLB);

    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2019-06-01", "2021-03-31"));
    assertEquals(3, contratsBLB.size());
    assertEquals("2020-01-01", contratsBLB.get(0).getDebut());
    assertEquals("2020-12-31", contratsBLB.get(0).getFin());

    assertEquals("2019-06-01", contratsBLB.get(1).getDebut());
    assertEquals("2019-12-31", contratsBLB.get(1).getFin());

    assertEquals("2021-01-01", contratsBLB.get(2).getDebut());
    assertEquals("2021-03-31", contratsBLB.get(2).getFin());
  }

  @Test
  void shouldAddComplexeContractToList() throws CloneNotSupportedException {
    final ContratAIV6 contrat1 = new ContratAIV6();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB1 = new ContratBLB(contrat1);
    contratBLB1.setDebut("2020-03-01");
    contratBLB1.setFin("2020-04-30");

    final ContratAIV6 contrat2 = new ContratAIV6();
    ContratBLB contratBLB2 = new ContratBLB(contrat2);
    contratBLB2.setDebut("2020-06-01");
    contratBLB2.setFin("2020-11-30");

    contratsBLB.add(contratBLB1);
    contratsBLB.add(contratBLB2);

    final ContratAIV6 contrat = this.buildContractAIV5();
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2020-01-01", "2020-12-31"));
    assertEquals(5, contratsBLB.size());
    assertEquals("2020-03-01", contratsBLB.get(0).getDebut());
    assertEquals("2020-04-30", contratsBLB.get(0).getFin());

    assertEquals("2020-06-01", contratsBLB.get(1).getDebut());
    assertEquals("2020-11-30", contratsBLB.get(1).getFin());

    assertEquals("2020-05-01", contratsBLB.get(3).getDebut());
    assertEquals("2020-05-31", contratsBLB.get(3).getFin());

    assertEquals("2020-01-01", contratsBLB.get(2).getDebut());
    assertEquals("2020-02-29", contratsBLB.get(2).getFin());

    assertEquals("2020-12-01", contratsBLB.get(4).getDebut());
    assertEquals("2020-12-31", contratsBLB.get(4).getFin());
  }

  @Test
  void shouldAddContractWithNoEndToList() throws CloneNotSupportedException {
    final ContratAIV6 contrat1 = new ContratAIV6();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB1 = new ContratBLB(contrat1);
    contratBLB1.setDebut("2020-04-01");

    final ContratAIV6 contrat2 = new ContratAIV6();
    ContratBLB contratBLB2 = new ContratBLB(contrat2);
    contratBLB2.setDebut("2020-02-01");
    contratBLB2.setFin("2020-03-31");

    contratsBLB.add(contratBLB1);
    contratsBLB.add(contratBLB2);

    final ContratAIV6 contrat = this.buildContractAIV5();
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2020-01-01", null));
    assertEquals(3, contratsBLB.size());
    assertEquals("2020-04-01", contratsBLB.get(0).getDebut());
    assertNull(contratsBLB.get(0).getFin());

    assertEquals("2020-02-01", contratsBLB.get(1).getDebut());
    assertEquals("2020-03-31", contratsBLB.get(1).getFin());

    assertEquals("2020-01-01", contratsBLB.get(2).getDebut());
    assertEquals("2020-01-31", contratsBLB.get(2).getFin());
  }

  @Test
  void shouldAddContractWithAllNoEnd() throws CloneNotSupportedException {
    final ContratAIV6 contrat1 = new ContratAIV6();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat1);
    contratBLB.setDebut("2021-01-01");

    contratsBLB.add(contratBLB);

    final ContratAIV6 contrat = this.buildContractAIV5();
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2016-01-01", null));
    assertEquals(2, contratsBLB.size());
    assertEquals("2021-01-01", contratsBLB.get(0).getDebut());
    assertNull(contratsBLB.get(0).getFin());

    assertEquals("2016-01-01", contratsBLB.get(1).getDebut());
    assertEquals("2020-12-31", contratsBLB.get(1).getFin());
  }

  @Test
  void shouldAddContractWithSameStartDate() throws CloneNotSupportedException {
    final ContratAIV6 contrat1 = new ContratAIV6();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat1);
    contratBLB.setDebut("2022-02-14");
    contratBLB.setFin("2023-03-12");

    contratsBLB.add(contratBLB);

    final ContratAIV6 contrat = this.buildContractAIV5();
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2022-02-14", null));
    assertEquals(2, contratsBLB.size());
    assertEquals("2022-02-14", contratsBLB.get(0).getDebut());
    assertEquals("2023-03-12", contratsBLB.get(0).getFin());

    assertEquals("2023-03-13", contratsBLB.get(1).getDebut());
    assertNull(contratsBLB.get(1).getFin());
  }

  @Test
  void shouldAddContractWithStartDateEqualEndPriorityContract() throws CloneNotSupportedException {
    final ContratAIV6 contrat1 = new ContratAIV6();
    List<ContratBLB> contratsBLB = new ArrayList<>();
    ContratBLB contratBLB = new ContratBLB(contrat1);
    contratBLB.setDebut("2021-01-15");

    contratsBLB.add(contratBLB);

    final ContratAIV6 contrat = this.buildContractAIV5();
    this.extractContratConsumer.buildBLBContracts(
        contrat, contratsBLB, new ImmutablePair<>("2016-01-01", "2021-01-15"));
    assertEquals(2, contratsBLB.size());
    assertEquals("2021-01-15", contratsBLB.get(0).getDebut());
    assertNull(contratsBLB.get(0).getFin());

    assertEquals("2016-01-01", contratsBLB.get(1).getDebut());
    assertEquals("2021-01-14", contratsBLB.get(1).getFin());
  }

  @Test
  void extractContractWhenAffiliationEqualIdentite()
      throws IOException, CloneNotSupportedException {
    // INIT
    final var contrat = TestTools.readAs("/Contrat1.json", ContratAIV6.class);
    final var id =
        BeneficiaireId.builder()
            .nir("1610561111111")
            .dateNaissance("19610531")
            .rangNaissance("1")
            .build();
    final List<ContratBLB> contratsBLB = new ArrayList<>();

    // RUN
    var dates = this.extractContratConsumer.calculatePeriod(contrat, id, new ArrayList<>());
    this.extractContratConsumer.buildBLBContracts(contrat, contratsBLB, dates);

    // VERIFY
    assertNotNull(dates);
    assertNotNull(dates.getLeft());
    assertNotNull(dates.getRight());

    assertTrue(CollectionUtils.isNotEmpty(contratsBLB));
    assertNotNull(contratsBLB.get(0).getDebut());
    assertNotNull(contratsBLB.get(0).getFin());
  }

  @Test
  void extractContractWhenMergePeriods() throws IOException, CloneNotSupportedException {
    // INIT
    final var contrat = TestTools.readAs("/Contrat2.json", ContratAIV6.class);
    final var id =
        BeneficiaireId.builder()
            .nir("2670999999999")
            .dateNaissance("19670909")
            .rangNaissance("1")
            .build();
    final List<ContratBLB> contratsBLB = new ArrayList<>();

    // RUN
    var dates = this.extractContratConsumer.calculatePeriod(contrat, id, new ArrayList<>());
    this.extractContratConsumer.buildBLBContracts(contrat, contratsBLB, dates);

    // VERIFY
    assertNotNull(dates);
    assertNotNull(dates.getLeft());
    assertNotNull(dates.getRight());

    assertEquals(1, contratsBLB.size());
    final var blb = contratsBLB.get(0);
    assertEquals("2022-01-01", blb.getDebut());
    assertEquals("2024-06-30", blb.getFin());
  }

  // --------------------
  // UTILS
  // --------------------
  private BeneficiaireId buildBeneficiaireId() {
    final BeneficiaireId beneficiaireId = new BeneficiaireId();
    beneficiaireId.setNir("2550898932073");
    beneficiaireId.setDateNaissance("19550813");
    beneficiaireId.setRangNaissance("1");

    return beneficiaireId;
  }

  private ContratAIV6 buildContractAIV5() {
    final ContratAIV6 contrat = new ContratAIV6();

    contrat.setDateSouscription("2020-01-01");
    contrat.setAssures(List.of(this.buildAssure()));

    return contrat;
  }

  private Assure buildAssure() {
    final Assure assure = new Assure();

    // Data
    final DataAssure dataAssure = new DataAssure();
    final NomAssure nomAssure = new NomAssure("nomFamille", "nomUsage", "prenom", "civilite");
    dataAssure.setNom(nomAssure);
    assure.setData(dataAssure);

    // Droits
    assure.setDroits(List.of(this.buildDroitAssure("2020-01-01", "2020-12-01")));

    // Identite
    assure.setIdentite(this.buildIdentiteContrat());

    // Periodes
    assure.setPeriodes(List.of(this.buildPeriode("2020-01-01", "2020-06-01")));

    return assure;
  }

  private DroitAssure buildDroitAssure(final String dateDebut, final String dateFin) {
    final Periode periode = new Periode(dateDebut, dateFin);
    final DroitAssure droitAssureV3 = new DroitAssure();
    droitAssureV3.setPeriode(periode);
    droitAssureV3.setCode("droit " + dateDebut);
    return droitAssureV3;
  }

  private Periode buildPeriode(final String dateDebut, final String dateFin) {
    return new Periode(dateDebut, dateFin);
  }

  private Nir buildNir() {
    final Nir nir = new Nir();
    nir.setCode("2550898932073");
    nir.setCle("22");

    return nir;
  }

  private IdentiteContrat buildIdentiteContrat() {
    final IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNir(this.buildNir());
    identiteContrat.setDateNaissance("19550813");
    identiteContrat.setRangNaissance("1");
    identiteContrat.setNumeroPersonne("numPersonne");

    final NirRattachementRO nirRattachementRO = new NirRattachementRO();
    nirRattachementRO.setNir(this.buildNir());

    final Periode period = new Periode();
    period.setDebut("2020-01-01");
    period.setFin("2021-08-01");
    nirRattachementRO.setPeriode(period);

    final RattachementRO rattachementRO = new RattachementRO();
    rattachementRO.setCodeCaisse("1");
    rattachementRO.setCodeCentre("1");
    rattachementRO.setCodeCentre("1");
    nirRattachementRO.setRattachementRO(rattachementRO);

    identiteContrat.setAffiliationsRO(List.of(nirRattachementRO));

    return identiteContrat;
  }

  private IdentiteContrat buildIdentiteContratWithoutEndDate() {
    final IdentiteContrat identiteContrat = new IdentiteContrat();
    identiteContrat.setNir(this.buildNir());
    identiteContrat.setDateNaissance("19550813");
    identiteContrat.setRangNaissance("1");
    identiteContrat.setNumeroPersonne("numPersonne");

    final NirRattachementRO nirRattachementRO = new NirRattachementRO();
    nirRattachementRO.setNir(this.buildNir());

    final Periode period = new Periode();
    period.setDebut("2021-01-01");
    nirRattachementRO.setPeriode(period);

    final RattachementRO rattachementRO = new RattachementRO();
    rattachementRO.setCodeCaisse("1");
    rattachementRO.setCodeCentre("1");
    rattachementRO.setCodeCentre("1");
    nirRattachementRO.setRattachementRO(rattachementRO);

    identiteContrat.setAffiliationsRO(List.of(nirRattachementRO));

    return identiteContrat;
  }

  private IdentiteContrat buildIdentiteContratWithAffiliationsRO() {
    final IdentiteContrat identiteContrat = this.buildIdentiteContrat();
    NirRattachementRO affiliation1 = new NirRattachementRO();
    affiliation1.setNir(this.buildNir());
    affiliation1.setPeriode(new Periode("2021-01-01", "2021-12-26"));

    NirRattachementRO affiliation2 = new NirRattachementRO();
    affiliation2.setNir(this.buildNir());
    affiliation2.setPeriode(new Periode("2021-12-27", "2022-12-31"));

    NirRattachementRO affiliation3 = new NirRattachementRO();
    affiliation3.setNir(this.buildNir());
    affiliation3.setPeriode(new Periode("2023-01-01", "2023-12-31"));

    identiteContrat.setAffiliationsRO(List.of(affiliation1, affiliation2, affiliation3));

    return identiteContrat;
  }
}
