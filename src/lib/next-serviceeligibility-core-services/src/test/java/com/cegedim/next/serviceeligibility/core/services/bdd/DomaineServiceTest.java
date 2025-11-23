package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineDroitBuffer;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = TestConfiguration.class)
class DomaineServiceTest {
  @Autowired private DomaineService domaineService;

  private DomaineDroit createDomaineDroit(
      String code,
      String codeGarantie,
      String tauxRemboursement,
      String uniteTauxRemboursement,
      String finDroit,
      String prioriteDroit,
      ModeAssemblage modeAssemblage) {
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode(code);
    domaineDroit.setCodeGarantie(codeGarantie);
    domaineDroit.setModeAssemblage(modeAssemblage);
    domaineDroit.setTauxRemboursement(tauxRemboursement);
    domaineDroit.setUniteTauxRemboursement(uniteTauxRemboursement);

    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("12/01/2024");
    periodeDroit.setPeriodeFin(finDroit);
    domaineDroit.setPeriodeDroit(periodeDroit);

    PrioriteDroit prioDroit = new PrioriteDroit();
    prioDroit.setCode(prioriteDroit);
    prioDroit.setLibelle(prioriteDroit);
    prioDroit.setPrioriteBO(prioriteDroit);
    prioDroit.setTypeDroit(prioriteDroit);
    domaineDroit.setPrioriteDroit(prioDroit);

    return domaineDroit;
  }

  @Test
  void consolidationInModeInclusionWithOneReimbursementRateToZero() {
    // LABO - 100 PO => LABO - 130 PO
    // PHAR - 100 XX => PHAR - 100 XX
    // LABO - 30 PO
    // MEDE - 0 F0
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie2", "30", "PO", "31/12/2024", "03", ModeAssemblage.INCLUSION);
    DomaineDroit dd4 =
        createDomaineDroit(
            "MEDE", "Garantie1", "0", "FO", "31/12/2024", "04", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(3, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("130", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domainesDroitsConsolides.get(1).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(1).getTauxRemboursement());
    Assertions.assertEquals("MEDE", domainesDroitsConsolides.get(2).getCode());
    Assertions.assertEquals("0", domainesDroitsConsolides.get(2).getTauxRemboursement());
  }

  public List<DomaineDroit> createDomaines(List<DomaineDroit> domainesDroits) {
    List<DomaineDroit> resDomaineDroits = new ArrayList<>();
    Map<String, Map<String, DomaineDroitBuffer>> domainsBuffer =
        domaineService.groupeByDateFinAndCode(domainesDroits);

    for (Map<String, DomaineDroitBuffer> bufferByCode : domainsBuffer.values()) {
      resDomaineDroits.addAll(domaineService.updateDomainesConsolides(bufferByCode.values()));
    }

    return resDomaineDroits;
  }

  public String valideDomaines(List<DomaineDroit> domainesDroits) {
    StringBuilder error = new StringBuilder();
    Map<String, Map<String, DomaineDroitBuffer>> domainsBuffer =
        domaineService.groupeByDateFinAndCode(domainesDroits);

    for (Map<String, DomaineDroitBuffer> bufferByCode : domainsBuffer.values()) {
      error.append(domaineService.validateDomainesConsolides(bufferByCode.values()));
    }

    return error.toString();
  }

  @Test
  void consolidationInModeInclusionWithDifferentUnits() {
    // LABO - 000100 TA => LABO - 110 TA
    // PHAR - 10% XX => PHAR - 10% XX
    // LABO - 000010 TA => MEDE - 120 TA
    // MEDE - 100.00 TA
    // MEDE - 20.00 TA
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "000100", "TA", "31/12/2024", "05", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "10%", "XX", "31/12/2024", "04", ModeAssemblage.INCLUSION);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie1", "000010", "TA", "31/12/2024", "03", ModeAssemblage.INCLUSION);
    DomaineDroit dd4 =
        createDomaineDroit(
            "MEDE", "Garantie1", "100.00", "TA", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    DomaineDroit dd5 =
        createDomaineDroit(
            "MEDE", "Garantie1", "20.00", "TA", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4, dd5);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(3, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("110", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domainesDroitsConsolides.get(1).getCode());
    Assertions.assertEquals("10%", domainesDroitsConsolides.get(1).getTauxRemboursement());
    Assertions.assertEquals("MEDE", domainesDroitsConsolides.get(2).getCode());
    Assertions.assertEquals("120", domainesDroitsConsolides.get(2).getTauxRemboursement());
  }

  @Test
  void consolidationInModeInclusionWithDifferentGuarantees() {
    // LABO - 100 PO - Garantie1 - prio1 => LABO - 130 PO - Garantie1
    // PHAR - 100 XX - Garantie1 - prio2 => PHAR - 100 XX - Garantie1
    // LABO - 30 PO - Garantie2 - prio3
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie2", "30", "PO", "31/12/2024", "03", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(2, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("130", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domainesDroitsConsolides.get(1).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(1).getTauxRemboursement());
  }

  @Test
  void consolidationInModeInclusionFailException() {
    // LABO - 100 PO
    // PHAR - 100 XX => "Incompatibilité des unités.."
    // LABO - 30 FO
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "FO", "31/12/2024", "03", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3);
    String error = valideDomaines(domainesDroits);
    Assertions.assertTrue(error.contains("C14;UNITES INCOMPATIBLES"));

    // LABO - 100 PO - Priorité 1
    // PHAR - 100 XX - Priorité 2 => "Absence de priorisation.."
    // LABO - 30 PO - Priorité 1
    DomaineDroit dd4 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd5 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    DomaineDroit dd6 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits2 = Arrays.asList(dd4, dd5, dd6);
    error = valideDomaines(domainesDroits2);
    Assertions.assertTrue(error.contains("C02;GARANTIES NON PRIORISEES"));

    // LABO - 100 XX
    // LABO - 30 XX => "Incompatibilité des valeurs.."
    DomaineDroit dd7 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "XX", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd8 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "XX", "31/12/2024", "03", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits3 = Arrays.asList(dd7, dd8);
    error = valideDomaines(domainesDroits3);
    Assertions.assertTrue(error.contains("C14;UNITES INCOMPATIBLES"));
  }

  @Test
  void consolidationInModeInclusionWithDifferentEndRighDate() {
    // LABO - 100 PO - periodFin 31/12/2024
    // PHAR - 50 PO - periodFin 31/12/2024
    // MEDE - 30 PO - periodFin 31/12/2024
    // OPTI - 100 PO - periodFin 10/12/2024
    // LABO - 10 PO - periodFin 31/12/2024
    // PHOR - 30 PO - periodFin 10/12/2024
    // OPTI - 70 PO - periodFin 31/12/2024
    // MEDE - 50 PO - periodFin 10/12/2024

    // 1ere carte demat générée avec :
    // LABO - 110 PO - periodFin 31/12/2024
    // PHAR - 50 PO - periodFin 31/12/2024
    // MEDE - 30 PO - periodFin 31/12/2024
    // OPTI - 70 PO - periodFin 31/12/2024

    // 2eme carte demat générée avec :
    // OPTI - 110 PO - periodFin 10/12/2024
    // PHOR - 30 PO - periodFin 10/12/2024
    // MEDE - 50 PO - periodFin 10/12/2024
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "08", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "50", "PO", "31/12/2024", "07", ModeAssemblage.INCLUSION);
    DomaineDroit dd3 =
        createDomaineDroit(
            "MEDE", "Garantie1", "30", "PO", "31/12/2024", "06", ModeAssemblage.INCLUSION);
    DomaineDroit dd4 =
        createDomaineDroit(
            "OPTI", "Garantie1", "100", "PO", "10/12/2024", "05", ModeAssemblage.INCLUSION);
    DomaineDroit dd5 =
        createDomaineDroit(
            "LABO", "Garantie1", "10", "PO", "31/12/2024", "04", ModeAssemblage.INCLUSION);
    DomaineDroit dd6 =
        createDomaineDroit(
            "PHOR", "Garantie1", "30", "PO", "10/12/2024", "03", ModeAssemblage.INCLUSION);
    DomaineDroit dd7 =
        createDomaineDroit(
            "OPTI", "Garantie1", "70", "PO", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    DomaineDroit dd8 =
        createDomaineDroit(
            "MEDE", "Garantie1", "50", "PO", "10/12/2024", "01", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4, dd5, dd6, dd7, dd8);

    List<DomaineDroit> domaineDroits = createDomaines(domainesDroits);

    Assertions.assertEquals(7, domaineDroits.size());
    Assertions.assertEquals("OPTI", domaineDroits.get(0).getCode());
    Assertions.assertEquals("100", domaineDroits.get(0).getTauxRemboursement());
    Assertions.assertEquals("10/12/2024", domaineDroits.get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("MEDE", domaineDroits.get(1).getCode());
    Assertions.assertEquals("50", domaineDroits.get(1).getTauxRemboursement());
    Assertions.assertEquals("10/12/2024", domaineDroits.get(1).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("PHOR", domaineDroits.get(2).getCode());
    Assertions.assertEquals("30", domaineDroits.get(2).getTauxRemboursement());
    Assertions.assertEquals("10/12/2024", domaineDroits.get(2).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("LABO", domaineDroits.get(3).getCode());
    Assertions.assertEquals("110", domaineDroits.get(3).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(3).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("OPTI", domaineDroits.get(4).getCode());
    Assertions.assertEquals("70", domaineDroits.get(4).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(4).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("PHAR", domaineDroits.get(5).getCode());
    Assertions.assertEquals("50", domaineDroits.get(5).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(5).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("MEDE", domaineDroits.get(6).getCode());
    Assertions.assertEquals("30", domaineDroits.get(6).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(6).getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void consolidationInModeDeltaWithOneReimbursementRateToZero() {
    // LABO - 100 PO => LABO - 100 PO
    // PHAR - 100 XX => PHAR - 100 XX
    // LABO - 30 PO
    // MEDE - 0 F0
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.DELTA);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "PO", "31/12/2024", "03", ModeAssemblage.DELTA);
    DomaineDroit dd4 =
        createDomaineDroit(
            "MEDE", "Garantie1", "0", "FO", "31/12/2024", "04", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(3, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domainesDroitsConsolides.get(1).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(1).getTauxRemboursement());
    Assertions.assertEquals("MEDE", domainesDroitsConsolides.get(2).getCode());
    Assertions.assertEquals("0", domainesDroitsConsolides.get(2).getTauxRemboursement());
  }

  @Test
  void consolidationInModeDeltaWithDifferentUnits() {
    // LABO - 000100 TA => LABO - 100 PO
    // PHAR - 10% XX => PHAR - 10% XX
    // LABO - 000010 TA => MEDE - 100 TA
    // MEDE - 100.00 TA
    // MEDE - 20.00 TA
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "000100", "TA", "31/12/2024", "01", ModeAssemblage.DELTA);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "10%", "XX", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie1", "000010", "TA", "31/12/2024", "03", ModeAssemblage.DELTA);
    DomaineDroit dd4 =
        createDomaineDroit(
            "MEDE", "Garantie1", "100.00", "TA", "31/12/2024", "04", ModeAssemblage.DELTA);
    DomaineDroit dd5 =
        createDomaineDroit(
            "MEDE", "Garantie1", "20.00", "TA", "31/12/2024", "05", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4, dd5);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(3, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domainesDroitsConsolides.get(1).getCode());
    Assertions.assertEquals("10%", domainesDroitsConsolides.get(1).getTauxRemboursement());
    Assertions.assertEquals("MEDE", domainesDroitsConsolides.get(2).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(2).getTauxRemboursement());
  }

  @Test
  void consolidationInModeDeltaWithDifferentGuarantees() {
    // LABO - 100 PO - Garantie1 => LABO - 100 PO - Garantie1
    // PHAR - 100 XX - Garantie1 => PHAR - 100 XX - Garantie1
    // LABO - 30 PO - Garantie2
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.DELTA);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie2", "30", "PO", "31/12/2024", "03", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(2, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domainesDroitsConsolides.get(1).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(1).getTauxRemboursement());
  }

  @Test
  void consolidationInModeDeltaFailException() {
    // LABO - 100 PO
    // PHAR - 100 XX => "Incompatibilité des unités.."
    // LABO - 30 FO
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.DELTA);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "FO", "31/12/2024", "03", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3);
    String error = valideDomaines(domainesDroits);
    Assertions.assertTrue(error.contains("C14;UNITES INCOMPATIBLES"));

    // LABO - Garantie1 - 100 PO - Priorité 1
    // PHAR - Garantie1 - 100 XX - Priorité 2 => "Absence de priorisation.."
    // LABO - Garantie1 - 30 PO - Priorité 1
    DomaineDroit dd4 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.DELTA);
    DomaineDroit dd5 =
        createDomaineDroit(
            "PHAR", "Garantie1", "100", "XX", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd6 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "PO", "31/12/2024", "01", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits2 = Arrays.asList(dd4, dd5, dd6);
    error = valideDomaines(domainesDroits2);
    Assertions.assertTrue(error.contains("C02;GARANTIES NON PRIORISEES"));

    // LABO - 100 XX
    // LABO - 30 XX => "Incompatibilité des valeurs.."
    DomaineDroit dd7 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "XX", "31/12/2024", "01", ModeAssemblage.DELTA);
    DomaineDroit dd8 =
        createDomaineDroit(
            "LABO", "Garantie1", "30", "XX", "31/12/2024", "03", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits3 = Arrays.asList(dd7, dd8);
    error = valideDomaines(domainesDroits3);
    Assertions.assertTrue(error.contains("C14;UNITES INCOMPATIBLES"));
  }

  @Test
  void consolidationInModeDeltaWithDifferentEndRighDate() {
    // LABO - 100 PO - periodFin 31/12/2024
    // PHAR - 50 PO - periodFin 31/12/2024
    // MEDE - 30 PO - periodFin 31/12/2024
    // OPTI - 100 PO - periodFin 10/12/2024
    // LABO - 10 PO - periodFin 31/12/2024
    // PHOR - 30 PO - periodFin 10/12/2024
    // OPTI - 70 PO - periodFin 31/12/2024
    // MEDE - 50 PO - periodFin 10/12/2024

    // 1ere carte demat avec :
    // LABO - 100 PO - periodFin 31/12/2024
    // PHAR - 50 PO - periodFin 31/12/2024
    // MEDE - 30 PO - periodFin 31/12/2024
    // OPTI - 70 PO - periodFin 31/12/2024

    // 2eme carte demat avec :
    // OPTI - 110 PO - periodFin 10/12/2024
    // PHOR - 30 PO - periodFin 10/12/2024
    // MEDE - 50 PO - periodFin 10/12/2024
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "04", ModeAssemblage.DELTA);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "Garantie1", "50", "PO", "31/12/2024", "07", ModeAssemblage.DELTA);
    DomaineDroit dd3 =
        createDomaineDroit(
            "MEDE", "Garantie1", "30", "PO", "31/12/2024", "06", ModeAssemblage.DELTA);
    DomaineDroit dd4 =
        createDomaineDroit(
            "OPTI", "Garantie1", "100", "PO", "10/12/2024", "05", ModeAssemblage.DELTA);
    DomaineDroit dd5 =
        createDomaineDroit(
            "LABO", "Garantie1", "10", "PO", "31/12/2024", "08", ModeAssemblage.DELTA);
    DomaineDroit dd6 =
        createDomaineDroit(
            "PHOR", "Garantie1", "30", "PO", "10/12/2024", "03", ModeAssemblage.DELTA);
    DomaineDroit dd7 =
        createDomaineDroit(
            "OPTI", "Garantie1", "70", "PO", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd8 =
        createDomaineDroit(
            "MEDE", "Garantie1", "50", "PO", "10/12/2024", "01", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4, dd5, dd6, dd7, dd8);

    List<DomaineDroit> domaineDroits = createDomaines(domainesDroits);

    Assertions.assertEquals(7, domaineDroits.size());
    Assertions.assertEquals("OPTI", domaineDroits.get(0).getCode());
    Assertions.assertEquals("100", domaineDroits.get(0).getTauxRemboursement());
    Assertions.assertEquals("10/12/2024", domaineDroits.get(0).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("MEDE", domaineDroits.get(1).getCode());
    Assertions.assertEquals("50", domaineDroits.get(1).getTauxRemboursement());
    Assertions.assertEquals("10/12/2024", domaineDroits.get(1).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("PHOR", domaineDroits.get(2).getCode());
    Assertions.assertEquals("30", domaineDroits.get(2).getTauxRemboursement());
    Assertions.assertEquals("10/12/2024", domaineDroits.get(2).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("LABO", domaineDroits.get(3).getCode());
    Assertions.assertEquals("100", domaineDroits.get(3).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(3).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("OPTI", domaineDroits.get(4).getCode());
    Assertions.assertEquals("70", domaineDroits.get(4).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(4).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("PHAR", domaineDroits.get(5).getCode());
    Assertions.assertEquals("50", domaineDroits.get(5).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(5).getPeriodeDroit().getPeriodeFin());
    Assertions.assertEquals("MEDE", domaineDroits.get(6).getCode());
    Assertions.assertEquals("30", domaineDroits.get(6).getTauxRemboursement());
    Assertions.assertEquals("31/12/2024", domaineDroits.get(6).getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void consolidationInModeDeltaOrInclusion() {
    // HOSP - 50 PO - Garantie1 - prio - inclusion => HOSP - 130 PO - Garantie1
    // HOSP - 80 PO - Garantie2 - noprio - delta
    DomaineDroit dd1 =
        createDomaineDroit(
            "HOSP", "Garantie1", "50", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "HOSP", "Garantie2", "80", "PO", "31/12/2024", "02", ModeAssemblage.DELTA);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(Arrays.asList(dd1, dd2));
    Assertions.assertEquals(1, domainesDroitsConsolides.size());
    Assertions.assertEquals("HOSP", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("130", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("Garantie2", domainesDroitsConsolides.get(0).getCodeGarantie());

    // HOSP - 50 PO - Garantie1 - prio - inclusion => HOSP - 80 PO - Garantie2
    // HOSP - 80 PO - Garantie2 - noprio - delta
    dd1 =
        createDomaineDroit(
            "HOSP", "Garantie1", "50", "PO", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    dd2 =
        createDomaineDroit(
            "HOSP", "Garantie2", "80", "PO", "31/12/2024", "01", ModeAssemblage.DELTA);
    domainesDroitsConsolides = createDomaines(Arrays.asList(dd1, dd2));
    Assertions.assertEquals(1, domainesDroitsConsolides.size());
    Assertions.assertEquals("HOSP", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("80", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("Garantie1", domainesDroitsConsolides.get(0).getCodeGarantie());
  }

  @Test
  void consolidationInModeDeltaOrInclusionGlobalTest() {
    // LABO - AXA_PREM - 100 PO - periodFin 31/12/2024 - INCLU - prio2
    // PHAR - AXA_PREM - 000050 TA - periodFin 31/12/2024 - DELTA - prio1
    // PHAR - AXA_CGDM - 000080 TA - periodFin 31/12/2024 - INCLU - prio3
    // OPTI - AXA_CGDM - 100.00 FO - periodFin 10/12/2024 - INCLU - prio2
    // OPTI - AXA_PREM - 80.00 FO - periodFin 10/12/2024 - INCLU - prio1
    // LABO - AXA_CGDM - 10 PO - periodFin 31/12/2024 - DELTA - prio1
    // OPTI - AXA_PREM - 70 PO - periodFin 31/12/2024 - INCLU - prio4
    // PHOR - AXA_CGDM - PEC XX - periodFin 05/12/2024 - DELTA - prio1
    // HOSP - AXA_CGDM - 0 PO - periodFin 05/12/2024 - DELTA - prio2
    // OPTI - AXA_PREM - 70 PO - periodFin 31/12/2024
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "AXA_PREM", "100", "PO", "31/12/2024", "04", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "PHAR", "AXA_PREM", "000050", "TA", "31/12/2024", "02", ModeAssemblage.DELTA);
    DomaineDroit dd3 =
        createDomaineDroit(
            "PHAR", "AXA_CGDM", "000080", "TA", "31/12/2024", "04", ModeAssemblage.INCLUSION);
    DomaineDroit dd4 =
        createDomaineDroit(
            "OPTI", "AXA_CGDM", "100.00", "FO", "10/12/2024", "03", ModeAssemblage.INCLUSION);
    DomaineDroit dd5 =
        createDomaineDroit(
            "OPTI", "AXA_PREM", "80.00", "FO", "10/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd6 =
        createDomaineDroit(
            "LABO", "AXA_CGDM", "10", "PO", "31/12/2024", "03", ModeAssemblage.DELTA);
    DomaineDroit dd7 =
        createDomaineDroit(
            "OPTI", "AXA_PREM", "70", "PO", "31/12/2024", "04", ModeAssemblage.INCLUSION);
    DomaineDroit dd8 =
        createDomaineDroit(
            "PHOR", "AXA_CGDM", "PEC", "XX", "05/12/2024", "04", ModeAssemblage.DELTA);
    DomaineDroit dd9 =
        createDomaineDroit("HOSP", "AXA_CGDM", "0", "PO", "05/12/2024", "03", ModeAssemblage.DELTA);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2, dd3, dd4, dd5, dd6, dd7, dd8, dd9);

    List<DomaineDroit> domaineDroits = createDomaines(domainesDroits);

    Assertions.assertEquals(6, domaineDroits.size());
    Assertions.assertEquals("OPTI", domaineDroits.get(0).getCode());
    Assertions.assertEquals("AXA_CGDM", domaineDroits.get(0).getCodeGarantie());
    Assertions.assertEquals("180", domaineDroits.get(0).getTauxRemboursement());
    Assertions.assertEquals("HOSP", domaineDroits.get(1).getCode());
    Assertions.assertEquals("AXA_CGDM", domaineDroits.get(1).getCodeGarantie());
    Assertions.assertEquals("0", domaineDroits.get(1).getTauxRemboursement());
    Assertions.assertEquals("PHOR", domaineDroits.get(2).getCode());
    Assertions.assertEquals("AXA_CGDM", domaineDroits.get(2).getCodeGarantie());
    Assertions.assertEquals("PEC", domaineDroits.get(2).getTauxRemboursement());
    Assertions.assertEquals("LABO", domaineDroits.get(3).getCode());
    Assertions.assertEquals("AXA_PREM", domaineDroits.get(3).getCodeGarantie());
    Assertions.assertEquals("10", domaineDroits.get(3).getTauxRemboursement());
    Assertions.assertEquals("OPTI", domaineDroits.get(4).getCode());
    Assertions.assertEquals("AXA_PREM", domaineDroits.get(4).getCodeGarantie());
    Assertions.assertEquals("70", domaineDroits.get(4).getTauxRemboursement());
    Assertions.assertEquals("PHAR", domaineDroits.get(5).getCode());
    Assertions.assertEquals("AXA_CGDM", domaineDroits.get(5).getCodeGarantie());
    Assertions.assertEquals("50", domaineDroits.get(5).getTauxRemboursement());
  }

  @Test
  void consolidationByDefaultIfAssemblyModeNull() {
    // LABO - 100 PO - modeAssemblage=null => LABO - 130 PO
    // LABO - 30 PO - modeAssemblage=null
    DomaineDroit dd1 =
        createDomaineDroit("LABO", "Garantie1", "100", "PO", "31/12/2024", "01", null);
    DomaineDroit dd2 =
        createDomaineDroit("LABO", "Garantie2", "30", "PO", "31/12/2024", "03", null);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2);

    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(1, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("130", domainesDroitsConsolides.get(0).getTauxRemboursement());
  }

  @Test
  void consolidationWithNCGuarantee() {
    // LABO - 100 PO => OK (no error)
    // LABO - NC XX
    DomaineDroit dd1 =
        createDomaineDroit(
            "LABO", "Garantie1", "100", "PO", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    DomaineDroit dd2 =
        createDomaineDroit(
            "LABO", "Garantie1", "NC", "XX", "31/12/2024", "02", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits = Arrays.asList(dd1, dd2);
    String error = valideDomaines(domainesDroits);
    Assertions.assertTrue(error.isEmpty());

    // LABO - 100 PO => LABO - 100 PO
    // LABO - NC XX
    List<DomaineDroit> domainesDroitsConsolides = createDomaines(domainesDroits);
    Assertions.assertEquals(1, domainesDroitsConsolides.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides.get(0).getCode());
    Assertions.assertEquals("100", domainesDroitsConsolides.get(0).getTauxRemboursement());
    Assertions.assertEquals("PO", domainesDroitsConsolides.get(0).getUniteTauxRemboursement());

    // LABO - NC XX => LABO - NC XX
    // LABO - NC XX
    DomaineDroit dd3 =
        createDomaineDroit(
            "LABO", "Garantie1", "NC", "XX", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits2 = Arrays.asList(dd2, dd3);

    List<DomaineDroit> domainesDroitsConsolides2 = createDomaines(domainesDroits2);
    Assertions.assertEquals(1, domainesDroitsConsolides2.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides2.get(0).getCode());
    Assertions.assertEquals("NC", domainesDroitsConsolides2.get(0).getTauxRemboursement());
    Assertions.assertEquals("XX", domainesDroitsConsolides2.get(0).getUniteTauxRemboursement());

    // LABO - NC XX => LABO - PEC XX
    // LABO - PEC XX
    DomaineDroit dd4 =
        createDomaineDroit(
            "LABO", "Garantie1", "PEC", "XX", "31/12/2024", "01", ModeAssemblage.INCLUSION);
    List<DomaineDroit> domainesDroits3 = Arrays.asList(dd2, dd4);

    List<DomaineDroit> domainesDroitsConsolides3 = createDomaines(domainesDroits3);
    Assertions.assertEquals(1, domainesDroitsConsolides3.size());
    Assertions.assertEquals("LABO", domainesDroitsConsolides3.get(0).getCode());
    Assertions.assertEquals("PEC", domainesDroitsConsolides3.get(0).getTauxRemboursement());
    Assertions.assertEquals("XX", domainesDroitsConsolides3.get(0).getUniteTauxRemboursement());
  }
}
