package com.cegedim.next.serviceeligibility.core.services.trigger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.MotifEvenement;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.PeriodeSuspension;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.TriggerTestPeriode;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.GenerationDomaineResult;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerBeneficiaryToDeclarations;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.*;
import java.util.*;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerBuildDeclarationNewServiceTest {

  @Autowired private TriggerBuildDeclarationNewService triggerBuildDeclarationService;

  @MockBean private TriggerDomaineService triggerDomaineService;

  @Autowired private MongoTemplate mongoTemplate;

  @SpyBean private TriggerService triggerService;

  /**
   * Cas n°1: Date résiliation est avant les dates de radiation et de restitution. Attendu: date de
   * résilation -- Cas n°2: Date résiliation est aprés les dates de radiation et de restitution.
   * Attendu: date de radiation attendu -- Cas n°3: Date restitution est avant les dates de
   * resilation et de radiation. Attendu: date de restitution
   */
  @Test
  void updateDateFermeture() {
    // Test cas n°1
    String dateFermeture =
        triggerBuildDeclarationService.getDateFermeture("2023/03/05", "2023/04/05", "2023/04/10");
    Assertions.assertEquals("2023/03/05", dateFermeture);

    // Test cas n°2
    dateFermeture =
        triggerBuildDeclarationService.getDateFermeture("2023/03/15", "2023/03/05", "2023/03/10");
    Assertions.assertEquals("2023/03/05", dateFermeture);

    // Test cas n°3
    dateFermeture =
        triggerBuildDeclarationService.getDateFermeture("2023/03/05", "2023/04/05", "2023/02/15");
    Assertions.assertEquals("2023/02/15", dateFermeture);
  }

  @Test
  void createOpenDeclarationComplete()
      throws CarenceException,
          DomaineNotFoundException,
          BobbNotFoundException,
          TriggerWarningException,
          PwException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    List<DomaineDroit> domaineDroitList = createListDomaineDroit();

    mockTriggerDomaine(new GenerationDomaineResult(domaineDroitList, false));

    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        createTriggerBeneficiaryToDeclarations();
    TriggeredBeneficiary triggeredBeneficiary =
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary();
    ParametrageCarteTP parametrageCarteTP =
        triggerBeneficiaryToDeclarations.getParametrageCarteTP();
    Trigger trigger = triggerBeneficiaryToDeclarations.getTrigger();

    triggerBuildDeclarationService.createOpenDeclaration(triggerBeneficiaryToDeclarations);

    assertNotNull(triggerBeneficiaryToDeclarations.getCreatedDeclaration());

    Declaration createdDeclaration = triggerBeneficiaryToDeclarations.getCreatedDeclaration();
    String resultDigital =
        TriggerUtils.calculateDigital(
            triggeredBeneficiary, parametrageCarteTP.getParametrageDroitsCarteTP());

    assertNotNull(createdDeclaration.getBeneficiaire());
    assertNotNull(createdDeclaration.getContrat());

    assertEquals(domaineDroitList, createdDeclaration.getDomaineDroits());
    assertEquals(triggeredBeneficiary.getAmc(), createdDeclaration.getIdDeclarant());
    assertEquals(
        triggeredBeneficiary.getIsCartePapierAEditer(), createdDeclaration.getIsCarteTPaEditer());
    assertEquals(triggeredBeneficiary.getIdTrigger(), createdDeclaration.getIdTrigger());
    assertEquals(trigger.getNomFichierOrigine(), createdDeclaration.getNomFichierOrigine());
    assertEquals(resultDigital, createdDeclaration.getCarteTPaEditerOuDigitale());
  }

  @Test
  void createOpenDeclarationNoDomaine()
      throws BobbNotFoundException,
          TriggerWarningException,
          PwException,
          CarenceException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    mockTriggerDomaine(new GenerationDomaineResult(Collections.emptyList(), false));

    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        createTriggerBeneficiaryToDeclarations();

    // No DomaineDroit so no creation
    DomaineNotFoundException domaineNotFoundException =
        assertThrows(
            DomaineNotFoundException.class,
            () ->
                triggerBuildDeclarationService.createOpenDeclaration(
                    triggerBeneficiaryToDeclarations));
    assertEquals(
        Anomaly.NO_WARRANTY_FOR_THIS_CONTRACT,
        domaineNotFoundException.getTriggeredBeneficiaryAnomaly().getAnomaly());

    DomaineDroit badDomaineDroit =
        createDomaineDroit("PHAR", "PHAR_GAR", null, "2031/12/31", "2021/01/01", null, "Carence");

    mockTriggerDomaine(new GenerationDomaineResult(List.of(badDomaineDroit), false));

    // No debutDroit so no creation
    TriggerWarningException noTpRightsNoPeriod =
        assertThrows(
            TriggerWarningException.class,
            () ->
                triggerBuildDeclarationService.createOpenDeclaration(
                    triggerBeneficiaryToDeclarations));
    assertEquals(
        Anomaly.NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS,
        noTpRightsNoPeriod.getTriggeredBeneficiaryAnomaly().getAnomaly());

    badDomaineDroit.getPeriodeDroit().setPeriodeDebut("2021/12/01");
    badDomaineDroit.getPeriodeDroit().setPeriodeFin("2021/01/01");

    // debutDroit > finDroit so no creation
    TriggerWarningException badPeriods =
        assertThrows(
            TriggerWarningException.class,
            () ->
                triggerBuildDeclarationService.createOpenDeclaration(
                    triggerBeneficiaryToDeclarations));
    assertEquals(
        Anomaly.NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS,
        badPeriods.getTriggeredBeneficiaryAnomaly().getAnomaly());
  }

  // fillExistingDeclarations()
  @Test
  void fillExistingDeclarationsNoExistingDecl() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture(null);
    List<Declaration> declarations = new ArrayList<>();

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertTrue(
        CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
  }

  @Test
  void fillExistingDeclarationsOneDeclNoIdOrigineDateBefore() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2022/05/15");

    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarations.add(declarationOuverture);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertTrue(
        CollectionUtils.isNotEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
    Assertions.assertEquals(1, triggerBeneficiaryToDeclarations.getExistingDeclarations().size());
    Declaration declaration =
        new ArrayList<>(triggerBeneficiaryToDeclarations.getExistingDeclarations()).get(0);
    Assertions.assertEquals("DeclOuverture", declaration.get_id());
  }

  @Test
  void fillExistingDeclarationsNoDeclNoIdOrigineDateAfter() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2024/05/15");
    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarations.add(declarationOuverture);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertFalse(
        CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
  }

  @Test
  void fillExistingDeclarationsOneDeclWithIdOrigineDateBefore() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2023/05/15");
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(getDeclarationOuverture());
    List<Declaration> declarations = new ArrayList<>();

    Declaration declarationOuverture = getDeclarationOuverture();
    declarationOuverture.set_id("1");
    Declaration declarationFermeture = getDeclarationFermeture();
    declarations.add(declarationFermeture);
    declarationFermeture.setIdOrigine("1");
    declarations.add(declarationOuverture);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertFalse(
        CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
  }

  @Test
  void fillExistingDeclarationsOneDeclWithIdOrigineDateAfter() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2023/08/15");
    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarationOuverture.set_id("1");
    Declaration declarationFermeture = getDeclarationFermeture();
    declarations.add(declarationFermeture);
    declarationFermeture.setIdOrigine("1");
    Declaration declarationOuverture2 = getDeclarationOuverture();
    declarationOuverture2.set_id("2");
    declarations.add(declarationOuverture2);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertFalse(
        CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
    Assertions.assertEquals(1, triggerBeneficiaryToDeclarations.getExistingDeclarations().size());
    List<Declaration> existingDeclarations =
        new ArrayList<>(triggerBeneficiaryToDeclarations.getExistingDeclarations());
    Assertions.assertEquals("2", existingDeclarations.get(0).get_id());
  }

  @Test
  void fillExistingDeclarationsTwoDeclLinkedByIdOriginDateBefore() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2022/06/15");

    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarations.add(declarationOuverture);
    Declaration declarationFermeture = getDeclarationFermeture();
    declarations.add(declarationFermeture);
    Declaration declarationOuverture2 = getDeclarationOuverture();
    declarationOuverture2.set_id("DeclOuverture2");
    declarations.add(declarationOuverture2);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertTrue(
        CollectionUtils.isNotEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
    Assertions.assertEquals(2, triggerBeneficiaryToDeclarations.getExistingDeclarations().size());
    List<Declaration> existingDeclarations =
        new ArrayList<>(triggerBeneficiaryToDeclarations.getExistingDeclarations());
    Assertions.assertEquals("DeclOuverture2", existingDeclarations.get(0).get_id());
    Assertions.assertEquals("DeclFermeture", existingDeclarations.get(1).get_id());
  }

  @Test
  void fillExistingSixDeclarations() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2022/06/15");

    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarations.add(declarationOuverture);
    Declaration declarationFermeture = getDeclarationFermeture();
    declarationFermeture
        .getDomaineDroits()
        .forEach(domaineDroit -> domaineDroit.getPeriodeDroit().setPeriodeFin("2022/01/01"));
    declarations.add(declarationFermeture);
    Declaration declarationOuverture1 = getDeclarationOuverture();
    declarationOuverture1.set_id("2");
    declarations.add(declarationOuverture1);
    Declaration declarationFermeture1 = getDeclarationFermeture();
    declarationFermeture1.set_id("3");
    declarationFermeture1.setIdOrigine("2");
    declarationFermeture1
        .getDomaineDroits()
        .forEach(domaineDroit -> domaineDroit.getPeriodeDroit().setPeriodeFin("2022/01/01"));
    declarations.add(declarationFermeture1);
    Declaration declarationOuverture2 = getDeclarationOuverture();
    declarationOuverture2.set_id("DeclOuverture2");
    declarationOuverture2.setEffetDebut(DateUtils.parseDate("2021-01-01", DateUtils.YYYY_MM_DD));
    declarations.add(declarationOuverture2);
    Declaration declarationOuverture3 = getDeclarationOuverture();
    declarationOuverture3.set_id("DeclOuverture3");
    declarationOuverture3.setEffetDebut(DateUtils.parseDate("2022-01-01", DateUtils.YYYY_MM_DD));
    declarations.add(declarationOuverture3);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertTrue(
        CollectionUtils.isNotEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
    Assertions.assertEquals(2, triggerBeneficiaryToDeclarations.getExistingDeclarations().size());
    List<Declaration> existingDeclarations =
        triggerBeneficiaryToDeclarations.getExistingDeclarations();

    Assertions.assertEquals("DeclOuverture3", existingDeclarations.get(0).get_id());
    Assertions.assertEquals("DeclOuverture2", existingDeclarations.get(1).get_id());
  }

  @Test
  void fillExistingDeclarationsTwoDeclLinkedByIdOriginDateBetween() {
    // Test setup
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(new TriggeredBeneficiary());
    triggerBeneficiaryToDeclarations.setDateFermeture("2023/06/15");
    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarations.add(declarationOuverture);
    Declaration declarationFermeture = getDeclarationFermeture();
    declarations.add(declarationFermeture);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsGetFirst(
        triggerBeneficiaryToDeclarations, declarations);

    // Assertions
    Assertions.assertFalse(
        CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
    List<Declaration> existingDeclarations =
        triggerBeneficiaryToDeclarations.getExistingDeclarations();

    Assertions.assertEquals("DeclFermeture", existingDeclarations.get(0).get_id());
  }

  @Test
  void fillExistingDeclarationsTwoDeclLinkedByIdOriginDateAfter() {
    // Test setup
    DeclarationService declarationService = Mockito.mock(DeclarationService.class);
    DeclarationService declarationServiceAvant =
        (DeclarationService)
            ReflectionTestUtils.getField(triggerBuildDeclarationService, "declarationService");
    ReflectionTestUtils.setField(
        triggerBuildDeclarationService, "declarationService", declarationService);
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTrigger(new Trigger());
    TriggeredBeneficiary triggeredBenef = new TriggeredBeneficiary();
    triggeredBenef.setAmc("A");
    triggeredBenef.setNumeroContrat("B");
    triggeredBenef.setNumeroPersonne("C");
    triggeredBenef.setDateNaissance("D");
    triggeredBenef.setRangNaissance("E");
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(triggeredBenef);
    triggerBeneficiaryToDeclarations.setDateFermeture("2024/06/15");
    List<Declaration> declarations = new ArrayList<>();
    Declaration declarationOuverture = getDeclarationOuverture();
    declarations.add(declarationOuverture);
    Declaration declarationFermeture = getDeclarationFermeture();
    declarations.add(declarationFermeture);
    Mockito.when(
            declarationService.findDeclarationsOfBenef(
                triggeredBenef.getAmc(),
                triggeredBenef.getNumeroContrat(),
                triggeredBenef.getNumeroPersonne(),
                triggeredBenef.getDateNaissance(),
                triggeredBenef.getRangNaissance(),
                triggeredBenef.getRangAdministratif(),
                null))
        .thenReturn(declarations);

    // Test
    triggerBuildDeclarationService.fillExistingDeclarationsForDeleteContrat(
        triggerBeneficiaryToDeclarations, null);

    // Assertions
    Assertions.assertFalse(
        CollectionUtils.isEmpty(triggerBeneficiaryToDeclarations.getExistingDeclarations()));
    List<Declaration> existingDeclarations =
        triggerBeneficiaryToDeclarations.getExistingDeclarations();

    Assertions.assertEquals("DeclFermeture", existingDeclarations.get(0).get_id());
    ReflectionTestUtils.setField(
        triggerBuildDeclarationService, "declarationService", declarationServiceAvant);
  }

  /**
   * Jeu de donnée n°1: -- Cas n°1: Date Fermeture dans une période de droit (pas de fin periode de
   * droit). Attendu: Création d'une fermeture -- Cas n°2: Date Fermeture avant une période de
   * droit. Attendu: Pas d'intégrer alpha et ses suspension -- Jeu de donnée n°2: -- Cas n°3: Date
   * Fermeture dans une période de droit (fin periode de droit). Attendu: Création d'une fermeture
   * -- Cas n°4: Date Fermeture aprés une période de droit. Attendu: Pas de fermeture, date online =
   * Date Fermeture
   */
  @Test
  void checkWhatToDoNext() {
    // Jeu de donnée n°1
    TriggerBeneficiaryToDeclarations tbtd = new TriggerBeneficiaryToDeclarations();
    Trigger trigger = new Trigger();
    trigger.setOrigine(TriggerEmitter.Renewal);
    tbtd.setTrigger(trigger);
    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    triggeredBeneficiary.setOldContract(new ServicePrestationTriggerBenef());
    tbtd.setTriggeredBeneficiary(triggeredBeneficiary);

    Declaration declaration = new Declaration();
    declaration.setIdDeclarant("1234567890");
    declaration.setEffetDebut(new Date());
    declaration.setDateModification(new Date());
    BeneficiaireV2 beneficiaire = new BeneficiaireV2();
    beneficiaire.setNirBeneficiaire("1234567890123");
    beneficiaire.setDateNaissance("19800101");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setNumeroPersonne("12");
    declaration.setBeneficiaire(beneficiaire);
    Contrat contrat = new Contrat();
    contrat.setNumero("1");
    declaration.setContrat(contrat);
    declaration.setDomaineDroits(
        Stream.of(
                new String[] {"PHAR", "2023/01/01", "2023/03/31"},
                new String[] {"PHAR", "2023/04/01", null},
                new String[] {"OPT", "2022/01/01", "2022/12/02"})
            .map(
                dataset -> {
                  DomaineDroit domaineDroit = new DomaineDroit();
                  domaineDroit.setCode(dataset[0]);
                  domaineDroit.setPeriodeDroit(new PeriodeDroit());
                  domaineDroit.getPeriodeDroit().setPeriodeDebut(dataset[1]);
                  domaineDroit.getPeriodeDroit().setPeriodeFin(dataset[2]);
                  domaineDroit.setPeriodeOnline(new PeriodeDroit());
                  domaineDroit.setPrioriteDroit(new PrioriteDroit());
                  return domaineDroit;
                })
            .toList());
    tbtd.setCreatedDeclaration(declaration);
    ServicePrestationTriggerBenef servicePrestationTriggerBenef =
        new ServicePrestationTriggerBenef();
    tbtd.getTriggeredBeneficiary().setNewContract(servicePrestationTriggerBenef);
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Automatique);
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);
    parametrageRenouvellement.setDebutEcheance("01/01");
    tbtd.setParametrageCarteTP(parametrageCarteTP);

    // Test cas n°1 (fermeture sans fin de periode domaine de droit)
    tbtd.setDateFermeture("2023/04/02");
    triggerBuildDeclarationService.checkWhatToDoNextAndCreateCloseDeclaration(tbtd);
    Assertions.assertEquals(
        1, tbtd.getWhatToDo()); // Création d'une fermeture// Déclaration de fermeture présente

    triggeredBeneficiary.setOldContract(null);
    triggerBuildDeclarationService.checkWhatToDoNextAndCreateCloseDeclaration(tbtd);
    Assertions.assertEquals(
        1, tbtd.getWhatToDo()); // Création d'une fermeture// Déclaration de fermeture présente

    // Test cas n°2 (rien à intégré)
    tbtd.setDateFermeture("2021/12/31");
    triggeredBeneficiary.setOldContract(new ServicePrestationTriggerBenef());
    triggerBuildDeclarationService.checkWhatToDoNextAndCreateCloseDeclaration(tbtd);
    Assertions.assertEquals(3, tbtd.getWhatToDo());

    // Jeu de donnée n°2
    tbtd.getCreatedDeclaration()
        .setDomaineDroits(
            Stream.of(
                    new String[] {"PHAR", "2023/01/01", "2023/03/31"},
                    new String[] {"OPT", "2022/01/01", "2022/12/02"})
                .map(
                    dataset -> {
                      DomaineDroit domaineDroit = new DomaineDroit();
                      domaineDroit.setCode(dataset[0]);
                      domaineDroit.setPeriodeDroit(new PeriodeDroit());
                      domaineDroit.getPeriodeDroit().setPeriodeDebut(dataset[1]);
                      domaineDroit.getPeriodeDroit().setPeriodeFin(dataset[2]);
                      domaineDroit.setPeriodeOnline(new PeriodeDroit());
                      domaineDroit.setPrioriteDroit(new PrioriteDroit());
                      return domaineDroit;
                    })
                .toList());

    // Test cas n°3 (fermeture avec fin de periode domaine de droit)
    tbtd.setDateFermeture("2023/02/01");
    triggerBuildDeclarationService.checkWhatToDoNextAndCreateCloseDeclaration(tbtd);
    assertEquals(1, tbtd.getWhatToDo()); // Création d'une fermeture
    assertNotNull(tbtd.getClosedDeclarations()); // Déclaration de fermeture présente
    // Test cas 2
    Assertions.assertEquals(
        1, tbtd.getWhatToDo()); // Création d'une fermeture// Déclaration de fermeture présente

    // Test cas n°4 (pas de fermeture)
    tbtd.setDateFermeture("2023/04/01");
    tbtd.setClosedDeclarations(new LinkedList<>());
    triggerBuildDeclarationService.checkWhatToDoNextAndCreateCloseDeclaration(tbtd);
    assertNotNull(tbtd.getCreatedDeclaration());
    assertEquals(0, tbtd.getClosedDeclarations().size());
    Assertions.assertEquals(2, tbtd.getWhatToDo());
  }

  /** */
  @ParameterizedTest
  @CsvSource(value = {", 2021/06/15, 2021/12/31", "2021/08/01, 2021/06/15, 2021/06/15"})
  void buildClosedDeclaration(String restitutionDate, String dateFinOnline, String dateFinOffline) {
    // Jeu de donnée : déclaration a fermée
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(
        TriggerDataForTesting.getTriggeredBenef1("UUID"));
    triggerBeneficiaryToDeclarations.setDateFermeture("2021/06/15");
    triggerBeneficiaryToDeclarations.setDateRadiation("2021-06-15");
    triggerBeneficiaryToDeclarations.setDateResiliation("2021-06-16");
    Declaration declaration = new Declaration();
    declaration.setIdDeclarant("1234567890");
    Date dateTraitement = new Date();
    declaration.setEffetDebut(dateTraitement);
    declaration.setDateModification(dateTraitement);
    declaration.setDateCreation(dateTraitement);
    declaration.setDateRestitution(restitutionDate);
    BeneficiaireV2 beneficiaire = new BeneficiaireV2();
    beneficiaire.setNirBeneficiaire("1234567890123");
    beneficiaire.setDateNaissance("19800101");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setNumeroPersonne("12");
    declaration.setBeneficiaire(beneficiaire);

    DomaineDroit domaineDroit1 = new DomaineDroit();
    domaineDroit1.setCode("PHAR");
    domaineDroit1.setCodeGarantie("PHAR_GAR");
    domaineDroit1.setPeriodeOnline(new PeriodeDroit());
    domaineDroit1.setPrioriteDroit(new PrioriteDroit());
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2021/01/01");
    periodeDroit.setPeriodeFin("2021/12/31");
    domaineDroit1.setPeriodeDroit(periodeDroit);
    PeriodeDroit periodeDroitOnline = new PeriodeDroit();
    periodeDroitOnline.setPeriodeDebut("2021/01/01");
    domaineDroit1.setPeriodeOnline(periodeDroitOnline);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    domaineDroit1.setPrioriteDroit(prioriteDroit);

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode("PHAR");
    domaineDroit2.setCodeGarantie("PHAR_GAR2");
    domaineDroit2.setPeriodeOnline(new PeriodeDroit());
    domaineDroit2.setPrioriteDroit(new PrioriteDroit());
    PeriodeDroit periodeDroit1 = new PeriodeDroit();
    periodeDroit1.setPeriodeDebut("2021/01/01");
    periodeDroit1.setPeriodeFin("2021/03/31");
    domaineDroit2.setPeriodeDroit(periodeDroit1);
    domaineDroit2.setPrioriteDroit(new PrioriteDroit());
    declaration.setDomaineDroits(List.of(domaineDroit1, domaineDroit2));
    Contrat contrat = new Contrat();
    contrat.setNumero("C1");
    declaration.setContrat(contrat);
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(declaration);

    Trigger trigger = new Trigger();
    trigger.setAmc("AMC1");
    trigger.setNbBenef(1);
    trigger.setNbBenefKO(0);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setStatus(TriggerStatus.ToProcess);
    trigger.setDateEffet("2021-10-15");
    triggerBeneficiaryToDeclarations.setTrigger(trigger);

    // Jeu de donnée : paramétrage carte tp
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    parametrageCarteTP.setAmc("1234567890");
    parametrageCarteTP.setIdentifiantCollectivite("COLPARAM");
    parametrageCarteTP.setGroupePopulation("COLLEGE_PARAM");
    parametrageCarteTP.setCritereSecondaireDetaille("CSD_PARAM");

    // paramétrage renouvellement
    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDateExecutionBatch("2021-01-01");

    // date déclanehcement automatique
    parametrageRenouvellement.setSeuilSecurite(1000);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Manuel);
    parametrageCarteTP.setParametrageRenouvellement(parametrageRenouvellement);

    // paramétrage de droits de carte tp
    parametrageCarteTP.setParametrageDroitsCarteTP(
        TriggerDataForTesting.getParametrageDroitsCarteTP());

    // Cas n°1
    declaration =
        triggerBuildDeclarationService.buildClosedDeclaration(
            triggerBeneficiaryToDeclarations, declaration.getDateCreation());

    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setMotifEvenement(MotifEvenement.FE.name());
    triggerTestPeriode.setDateDebut("2021/01/01");
    triggerTestPeriode.setDateFin("2021/06/15");
    triggerTestPeriode.setDateDebutFermeture("2021/06/16");
    triggerTestPeriode.setDateFinFermeture("2021/12/31");
    triggerTestPeriode.setDateDebutOnline("2021/01/01");
    triggerTestPeriode.setDateFinOnline(dateFinOnline);
    triggerTestPeriode.setDateFinOffline(dateFinOffline);
    TriggerDataForTesting.checkPeriode(
        declaration.getDomaineDroits().get(0),
        triggerTestPeriode,
        declaration.getDateRestitution());
    Assertions.assertEquals(
        triggerBeneficiaryToDeclarations.getCreatedDeclaration().getDateCreation(),
        declaration.getDateCreation());
    Assertions.assertEquals("2021/06/15", declaration.getBeneficiaire().getDateRadiation());
    Assertions.assertEquals("2021/06/16", declaration.getContrat().getDateResiliation());
  }

  /** Case : New and Old contracts present and no diffs between them */
  @Test
  void checkIfWeCloseAll() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    assertFalse(triggerBuildDeclarationService.checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
  }

  /** Case : No old contract present */
  @Test
  void checkIfWeCloseAllNoOldContrat() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    triggerBeneficiaryToDeclarations.getTriggeredBeneficiary().setOldContract(null);

    assertFalse(triggerBuildDeclarationService.checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
  }

  /** Case : Different DateFermeture between Old and New contract */
  @Test
  void checkIfWeCloseAllDiffsDateFermeture() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    ServicePrestationTriggerBenef oldContrat =
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary().getOldContract();

    // Set newDateFermeture != oldDateFermeture
    oldContrat.setDateResiliation("2021/12/01");
    oldContrat.setDateRadiation("2021/12/20");
    triggerBeneficiaryToDeclarations.setDateFermeture("2022/07/31");

    assertTrue(triggerBuildDeclarationService.checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
  }

  /** Case : Different list of {@link DroitAssure} between Old and New contract */
  @Test
  void checkIfWeCloseAllDiffsListDroitsHTP() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    ServicePrestationTriggerBenef newContrat =
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary().getNewContract();
    ServicePrestationTriggerBenef oldContrat =
        triggerBeneficiaryToDeclarations.getTriggeredBeneficiary().getOldContract();

    // Check droitHTP
    DroitAssure droit1 = new DroitAssure();
    droit1.setCode("Droit_Test_1");
    droit1.setCode("ASSURE1");
    DroitAssure droit2 = new DroitAssure();
    droit1.setCode("Droit_Test_2");
    droit1.setCode("ASSURE2");

    newContrat.setDroitsGaranties(List.of(droit1, droit2));
    oldContrat.setDroitsGaranties(List.of(droit1));

    assertTrue(triggerBuildDeclarationService.checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
  }

  /** Case : Old and new Parametrage carte TP different ID */
  @Test
  void checkIfWeCloseAllDiffsParamCarteTP() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    // Check ParamCarteTP
    TriggeredBeneficiary oldTriggeredBeneficiary =
        TriggerDataForTesting.getTriggeredBenef1("UUID_old");
    oldTriggeredBeneficiary.setParametrageCarteTPId("Old_ParamCarteTP_Test");
    doReturn(oldTriggeredBeneficiary)
        .when(triggerService)
        .getLastTriggeredBeneficiariesByServicePrestation(anyString(), anyString());

    assertTrue(triggerBuildDeclarationService.checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
  }

  /**
   * Case : earliest and newly created declaration don t have same number of {@link DomaineDroit}
   */
  @Test
  void checkIfWeCloseAllDiffsNombreDomaineDroits() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    Declaration newEarliestDeclaration = new Declaration();

    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("new_Code_Test");
    domaineDroit.setCodeGarantie("new_Code_Garantie_Test");
    domaineDroit.setVersionOffre("new_Version_Offre_Test");
    domaineDroit.setCodeOffre("new_Code_Offre_Test");
    domaineDroit.setCodeProduit("new_Code_Produit_Test");

    newEarliestDeclaration.setDomaineDroits(new ArrayList<>(List.of(domaineDroit)));
    triggerBeneficiaryToDeclarations.setExistingDeclarations(
        new LinkedList<>(List.of(newEarliestDeclaration)));

    Declaration createdDeclaration = new Declaration();

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode("new_Code_Test_2");
    domaineDroit2.setCodeGarantie("new_Code_Garantie_Test_2");
    domaineDroit2.setVersionOffre("new_Version_Offre_Test_2");
    domaineDroit2.setCodeOffre("new_Code_Offre_Test_2");
    domaineDroit2.setCodeProduit("new_Code_Produit_Test_2");

    createdDeclaration.setDomaineDroits(new ArrayList<>(List.of(domaineDroit, domaineDroit2)));
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(createdDeclaration);

    assertTrue(triggerBuildDeclarationService.checkIfWeCloseAll(triggerBeneficiaryToDeclarations));
  }

  /**
   * Case 1 : earliest and newly created declaration don t have same code garantie Case 2 : earliest
   * and newly created declaration have same code garantie but not same offre/produit
   */
  @Test
  void checkIfWeCloseAllDiffsDroitsByGarantie() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    Declaration newEarliestDeclaration = new Declaration();

    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("Code_Test");
    domaineDroit.setCodeGarantie("Code_Garantie_Test");
    domaineDroit.setVersionOffre("Version_Offre_Test");
    domaineDroit.setCodeOffre("Code_Offre_Test");
    domaineDroit.setCodeProduit("Code_Produit_Test");

    newEarliestDeclaration.setDomaineDroits(new ArrayList<>(List.of(domaineDroit)));
    triggerBeneficiaryToDeclarations.setExistingDeclarations(
        new LinkedList<>(List.of(newEarliestDeclaration)));

    Declaration createdDeclaration = new Declaration();

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode("Code_Test_2");
    domaineDroit2.setCodeGarantie("Code_Garantie_Test_2");
    domaineDroit2.setVersionOffre("Version_Offre_Test_2");
    domaineDroit2.setCodeOffre("Code_Offre_Test_2");
    domaineDroit2.setCodeProduit("Code_Produit_Test_2");

    createdDeclaration.setDomaineDroits(new ArrayList<>(List.of(domaineDroit2)));
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(createdDeclaration);

    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 1

    domaineDroit2.setCodeGarantie(domaineDroit.getCodeGarantie());

    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 2
  }

  /**
   * Case 1 : earliest and newly created declaration don t have same period on Bobb Case 2 :
   * earliest and newly created declaration don t have same nature on Bobb Case 3 : earliest and
   * newly created declaration are the same not same bobb
   */
  @Test
  void checkIfWeCloseAllDiffsDroitsByGarantieBobb() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    Declaration newEarliestDeclaration = new Declaration();

    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("Code_Test");
    domaineDroit.setCodeGarantie("Code_Garantie_Test");
    domaineDroit.setVersionOffre("Version_Offre_Test");
    domaineDroit.setCodeOffre("Code_Offre_Test");
    domaineDroit.setCodeProduit("Code_Produit_Test");
    domaineDroit.setNaturePrestation("NATURE");
    Periode periodeDroitProductElement = new Periode();
    periodeDroitProductElement.setDebut("2020-01-01");
    periodeDroitProductElement.setFin("2023-01-01");
    domaineDroit.setPeriodeProductElement(periodeDroitProductElement);
    List<DomaineDroit> domaineDroitList = new ArrayList<>();
    domaineDroitList.add(domaineDroit);
    newEarliestDeclaration.setDomaineDroits(domaineDroitList);
    LinkedList<Declaration> existingDeclarations = new LinkedList<>();
    existingDeclarations.add(newEarliestDeclaration);
    triggerBeneficiaryToDeclarations.setExistingDeclarations(existingDeclarations);

    Declaration createdDeclaration = new Declaration();

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode("Code_Test");
    domaineDroit2.setCodeGarantie("Code_Garantie_Test");
    domaineDroit2.setVersionOffre("Version_Offre_Test");
    domaineDroit2.setCodeOffre("Code_Offre_Test");
    domaineDroit2.setCodeProduit("Code_Produit_Test");
    domaineDroit2.setNaturePrestation("NATURE");
    Periode periodeDroitProductElement2 = new Periode();
    periodeDroitProductElement2.setDebut("2020-01-01");
    periodeDroitProductElement2.setFin("2024-01-01");
    domaineDroit2.setPeriodeProductElement(periodeDroitProductElement2);

    List<DomaineDroit> domaineDroitList2 = new ArrayList<>();
    domaineDroitList2.add(domaineDroit2);
    createdDeclaration.setDomaineDroits(domaineDroitList2);
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(createdDeclaration);

    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 1

    domaineDroit2.setNaturePrestation("NATURE2");
    periodeDroitProductElement2.setFinToNull();
    periodeDroitProductElement.setFinToNull();

    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 2

    domaineDroit2.setNaturePrestation("NATURE");

    assertFalse(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 3
  }

  /**
   * Case 1 : earliest and newly created declaration have same periodCarence Case 2 : earliest and
   * newly created declaration don t have same periodCarence Case 3 : earliest and newly created
   * declaration have same periodCarence but not same code
   */
  @Test
  void checkIfWeCloseAllDiffsCarences() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        getTriggerBeneficiaryToDeclarationsComplete();

    Declaration newEarliestDeclaration = new Declaration();
    newEarliestDeclaration.setUserCreation(Constants.ORIGINE_DECLARATIONEVT);

    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("Code_Test");
    domaineDroit.setCodeGarantie("Code_Garantie_Test");
    domaineDroit.setVersionOffre("Version_Offre_Test");
    domaineDroit.setCodeOffre("Code_Offre_Test");
    domaineDroit.setCodeProduit("Code_Produit_Test");
    domaineDroit.setNaturePrestation("NATURE");
    Periode periodeDroitProductElement = new Periode();
    periodeDroitProductElement.setDebut("2020-01-01");
    periodeDroitProductElement.setFin("2024-01-01");
    domaineDroit.setPeriodeProductElement(periodeDroitProductElement);
    PeriodeCarence periodeCarence = new PeriodeCarence();
    periodeCarence.setDebut("2023-01-01");
    periodeCarence.setFin("2023-03-31");
    domaineDroit.setPeriodeCarence(periodeCarence);
    domaineDroit.setCodeCarence("C1");
    List<DomaineDroit> domaineDroitList = new ArrayList<>();
    domaineDroitList.add(domaineDroit);
    newEarliestDeclaration.setDomaineDroits(domaineDroitList);
    LinkedList<Declaration> earliestDeclarations = new LinkedList<>();
    earliestDeclarations.add(newEarliestDeclaration);
    triggerBeneficiaryToDeclarations.setExistingDeclarations(earliestDeclarations);

    Declaration createdDeclaration = new Declaration();

    DomaineDroit domaineDroit2 = new DomaineDroit();
    domaineDroit2.setCode("Code_Test");
    domaineDroit2.setCodeGarantie("Code_Garantie_Test");
    domaineDroit2.setVersionOffre("Version_Offre_Test");
    domaineDroit2.setCodeOffre("Code_Offre_Test");
    domaineDroit2.setCodeProduit("Code_Produit_Test");
    domaineDroit2.setNaturePrestation("NATURE");
    Periode periodeDroitProductElement2 = new Periode();
    periodeDroitProductElement2.setDebut("2020-01-01");
    periodeDroitProductElement2.setFin("2024-01-01");
    domaineDroit2.setPeriodeProductElement(periodeDroitProductElement2);
    PeriodeCarence periodeCarence2 = new PeriodeCarence();
    periodeCarence2.setDebut("2023-01-01");
    periodeCarence2.setFin("2023-03-31");
    domaineDroit2.setPeriodeCarence(periodeCarence2);
    domaineDroit2.setCodeCarence("C1");

    List<DomaineDroit> domaineDroitList2 = new ArrayList<>();
    domaineDroitList2.add(domaineDroit2);
    createdDeclaration.setDomaineDroits(domaineDroitList2);
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(createdDeclaration);

    // carences identiques
    assertFalse(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 1

    // periodes carences differentes
    periodeCarence2.setFin("2023-06-30");
    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 2

    // periode carences null
    domaineDroit2.setPeriodeCarence(null);
    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 3

    // periode carences null dans l'ancienne mais pas dans la nouvelle
    domaineDroit2.setPeriodeCarence(periodeCarence2);
    domaineDroit.setPeriodeCarence(null);
    assertTrue(
        triggerBuildDeclarationService.checkIfWeCloseAll(
            triggerBeneficiaryToDeclarations)); // Case 4
  }

  private TriggerBeneficiaryToDeclarations getTriggerBeneficiaryToDeclarationsComplete() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    Trigger trigger = new Trigger();
    trigger.setDateRestitution(null);
    triggerBeneficiaryToDeclarations.setTrigger(trigger);
    TriggeredBeneficiary triggeredBeneficiary = TriggerDataForTesting.getTriggeredBenef1("UUID");
    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(triggeredBeneficiary);

    ServicePrestationTriggerBenef newContrat = new ServicePrestationTriggerBenef();
    ServicePrestationTriggerBenef oldContrat = new ServicePrestationTriggerBenef();
    triggeredBeneficiary.setNewContract(newContrat);
    triggeredBeneficiary.setOldContract(oldContrat);

    // Check dates
    oldContrat.setDateResiliation("2021/12/01");
    oldContrat.setDateRadiation("2021/12/20");
    triggerBeneficiaryToDeclarations.setDateFermeture("2021/12/01");

    // Check droitHTP
    DroitAssure droit1 = new DroitAssure();
    droit1.setCode("Droit_Test_1");
    droit1.setCodeAssureur("ASSUREUR");

    newContrat.setDroitsGaranties(List.of(droit1));
    oldContrat.setDroitsGaranties(List.of(droit1));

    // Check ParamCarteTP
    triggeredBeneficiary.setParametrageCarteTPId("ParamCarteTP_Test");
    triggeredBeneficiary.setServicePrestationId("ServicePrestation_Test");
    doReturn(new ArrayList<>(List.of(triggeredBeneficiary)))
        .when(triggerService)
        .getTriggeredBeneficiariesByServicePrestation(anyString());

    // Check existing declaration
    Declaration declaration = new Declaration();
    declaration.setDateCreation(new Date());
    triggerBeneficiaryToDeclarations.setExistingDeclarations(
        new LinkedList<>(List.of(declaration)));

    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("Code_Test");
    domaineDroit.setCodeGarantie("Code_Garantie_Test");
    domaineDroit.setVersionOffre("Version_Offre_Test");
    domaineDroit.setCodeOffre("Code_Offre_Test");
    domaineDroit.setCodeProduit("Code_Produit_Test");
    declaration.setDomaineDroits(new ArrayList<>(List.of(domaineDroit)));
    triggerBeneficiaryToDeclarations.setCreatedDeclaration(declaration);

    return triggerBeneficiaryToDeclarations;
  }

  @Test
  void fillClosedDeclarationsWithDateFermetureAfterDateFinAndDateDebut() {
    TriggerBeneficiaryToDeclarations tbtd = new TriggerBeneficiaryToDeclarations();
    Trigger trigger = new Trigger();
    trigger.setId("trigger");
    tbtd.setTrigger(trigger);
    LinkedList<Declaration> existingDeclarations = new LinkedList<>();
    Declaration createdDeclaration = getCreatedDeclaration(tbtd);
    Date dateTraitement = createdDeclaration.getDateCreation();
    PeriodeDroit periodeDroit = createdDeclaration.getDomaineDroits().get(0).getPeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/02/01");
    periodeDroit.setPeriodeFin("2023/01/31");
    existingDeclarations.add(createdDeclaration);
    tbtd.setExistingDeclarations(existingDeclarations);
    tbtd.setDateFermeture("2023/03/01");
    triggerBuildDeclarationService.fillClosedDeclarations(tbtd, dateTraitement);
    Assertions.assertNotNull(tbtd.getClosedDeclarations());
    List<Declaration> closedDeclarations = new ArrayList<>(tbtd.getClosedDeclarations());
    List<Declaration> existingDeclarationsList = new ArrayList<>(existingDeclarations);
    Assertions.assertEquals("R", closedDeclarations.get(0).getCodeEtat());
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2023/02/01");
    triggerTestPeriode.setDateFin("2023/01/31");
    triggerTestPeriode.setDateDebutFermeture("2023/02/01");
    triggerTestPeriode.setDateFinFermeture(
        existingDeclarationsList
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getPeriodeDroit()
            .getPeriodeFin());
    triggerTestPeriode.setDateDebutOnline("2023/01/01");
    triggerTestPeriode.setDateFinOnline("2023/01/31");
    triggerTestPeriode.setDateFinOffline("2023/01/31");
    TriggerDataForTesting.checkPeriode(
        closedDeclarations.get(0).getDomaineDroits().get(0),
        triggerTestPeriode,
        closedDeclarations.get(0).getDateRestitution());

    Assertions.assertEquals(dateTraitement, closedDeclarations.get(0).getDateCreation());
  }

  @Test
  void fillClosedDeclarationsWithDateFermetureBeforeDateFinAndAfteDateDebut() {
    TriggerBeneficiaryToDeclarations tbtd = new TriggerBeneficiaryToDeclarations();
    Trigger trigger = new Trigger();
    trigger.setId("trigger");
    tbtd.setTrigger(trigger);
    LinkedList<Declaration> existingDeclarations = new LinkedList<>();
    tbtd.setExistingDeclarations(existingDeclarations);
    Declaration createdDeclaration = getCreatedDeclaration(tbtd);
    Date dateTraitement = createdDeclaration.getDateCreation();
    existingDeclarations.add(createdDeclaration);
    tbtd.setDateFermeture("2022/12/31");
    triggerBuildDeclarationService.fillClosedDeclarations(tbtd, dateTraitement);
    Assertions.assertNotNull(tbtd.getClosedDeclarations());
    List<Declaration> closedDeclarations = new ArrayList<>(tbtd.getClosedDeclarations());
    List<Declaration> existingDeclarationsList = new ArrayList<>(existingDeclarations);
    Assertions.assertEquals("R", closedDeclarations.get(0).getCodeEtat());
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2023/01/01");
    triggerTestPeriode.setDateFin("2022/12/31");
    triggerTestPeriode.setDateDebutFermeture("2023/01/01");
    triggerTestPeriode.setDateFinFermeture(
        existingDeclarationsList
            .get(0)
            .getDomaineDroits()
            .get(0)
            .getPeriodeDroit()
            .getPeriodeFin());
    triggerTestPeriode.setDateDebutOnline("2023/01/01");
    triggerTestPeriode.setDateFinOnline("2022/12/31");
    triggerTestPeriode.setDateFinOffline("2023/03/31");
    TriggerDataForTesting.checkPeriode(
        closedDeclarations.get(0).getDomaineDroits().get(0),
        triggerTestPeriode,
        closedDeclarations.get(0).getDateRestitution());

    Assertions.assertEquals(dateTraitement, closedDeclarations.get(0).getDateCreation());
  }

  @Test
  void fillClosedDeclarationsWithDateFermetureBeforeDateFinAndAfterDateDebut() {
    TriggerBeneficiaryToDeclarations tbtd = new TriggerBeneficiaryToDeclarations();
    Trigger trigger = new Trigger();
    trigger.setId("trigger");
    tbtd.setTrigger(trigger);
    LinkedList<Declaration> existingDeclarations = new LinkedList<>();
    existingDeclarations.add(getCreatedDeclaration(tbtd));
    Date dateTraitement = new Date();
    Declaration createdDeclaration = new Declaration();
    createdDeclaration.setDateModification(dateTraitement);
    tbtd.setCreatedDeclaration(createdDeclaration);
    createdDeclaration.setBeneficiaire(new BeneficiaireV2());
    createdDeclaration.setContrat(new Contrat());
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setPeriodeDroit(new PeriodeDroit());
    domaineDroit.getPeriodeDroit().setPeriodeDebut("2023/01/01");
    domaineDroit.getPeriodeDroit().setPeriodeFin("2023/12/31");
    domaineDroit.setPeriodeOnline(new PeriodeDroit());
    domaineDroit.getPeriodeOnline().setPeriodeDebut("2023/01/01");
    domaineDroit.setPrioriteDroit(new PrioriteDroit());
    createdDeclaration.setDomaineDroits(List.of(domaineDroit));

    tbtd.setExistingDeclarations(existingDeclarations);
    tbtd.setDateFermeture("2023/02/01");
    triggerBuildDeclarationService.fillClosedDeclarations(tbtd, dateTraitement);
    Assertions.assertNotNull(tbtd.getClosedDeclarations());
    Assertions.assertEquals(1, tbtd.getClosedDeclarations().size());
    List<Declaration> closedDeclarations = new ArrayList<>(tbtd.getClosedDeclarations());
    Assertions.assertEquals("R", closedDeclarations.get(0).getCodeEtat());
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2023/01/01");
    triggerTestPeriode.setDateFin("2022/12/31");
    triggerTestPeriode.setDateDebutFermeture("2023/01/01");
    triggerTestPeriode.setDateFinFermeture("2023/03/31");
    triggerTestPeriode.setDateDebutOnline("2023/01/01");
    triggerTestPeriode.setDateFinOnline("2022/12/31");
    triggerTestPeriode.setDateFinOffline("2023/03/31");
    TriggerDataForTesting.checkPeriode(
        closedDeclarations.get(0).getDomaineDroits().get(0),
        triggerTestPeriode,
        closedDeclarations.get(0).getDateRestitution());
  }

  @Test
  void fillClosedDeclarationsAndCloseContract() {
    TriggerBeneficiaryToDeclarations tbtd = new TriggerBeneficiaryToDeclarations();
    LinkedList<Declaration> existingDeclarations = new LinkedList<>();
    Declaration createdDeclaration = getCreatedDeclaration(tbtd);
    Date dateTraitement = createdDeclaration.getDateCreation();
    PeriodeDroit periodeDroit = createdDeclaration.getDomaineDroits().get(0).getPeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/01/01");
    periodeDroit.setPeriodeFin("2023/12/31");
    existingDeclarations.add(createdDeclaration);
    tbtd.setExistingDeclarations(existingDeclarations);
    tbtd.setDateFermeture("2023/01/01");
    Trigger trigger = new Trigger();
    trigger.setId("toto");
    tbtd.setTrigger(trigger);
    triggerBuildDeclarationService.fillClosedDeclarations(tbtd, dateTraitement);
    Assertions.assertNotNull(tbtd.getClosedDeclarations());
    List<Declaration> closedDeclarations = new ArrayList<>(tbtd.getClosedDeclarations());
    Assertions.assertEquals("R", closedDeclarations.get(0).getCodeEtat());
    TriggerTestPeriode triggerTestPeriode = new TriggerTestPeriode();
    triggerTestPeriode.setDateDebut("2023/01/01");
    triggerTestPeriode.setDateFin("2022/12/31");
    triggerTestPeriode.setDateDebutFermeture("2023/01/01");
    triggerTestPeriode.setDateFinFermeture("2023/12/31");
    triggerTestPeriode.setDateDebutOnline("2023/01/01");
    triggerTestPeriode.setDateFinOnline("2022/12/31");
    triggerTestPeriode.setDateFinOffline("2023/12/31");
    TriggerDataForTesting.checkPeriode(
        closedDeclarations.get(0).getDomaineDroits().get(0),
        triggerTestPeriode,
        closedDeclarations.get(0).getDateRestitution());

    Assertions.assertEquals(dateTraitement, closedDeclarations.get(0).getDateCreation());
  }

  private static Declaration getCreatedDeclaration(TriggerBeneficiaryToDeclarations tbtd) {
    return getCreatedDeclaration(tbtd, null);
  }

  private static Declaration getCreatedDeclaration(
      TriggerBeneficiaryToDeclarations tbtd, Date dateTraitement) {
    Declaration createdDeclaration = new Declaration();
    if (dateTraitement == null) {
      dateTraitement = new Date();
    }
    createdDeclaration.setDateModification(dateTraitement);
    createdDeclaration.setDateCreation(dateTraitement);
    createdDeclaration.setEffetDebut(dateTraitement);
    tbtd.setCreatedDeclaration(createdDeclaration);
    createdDeclaration.setBeneficiaire(new BeneficiaireV2());
    createdDeclaration.setContrat(new Contrat());
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setPeriodeDroit(new PeriodeDroit());
    domaineDroit.getPeriodeDroit().setPeriodeDebut("2023/01/01");
    domaineDroit.getPeriodeDroit().setPeriodeFin("2023/03/31");
    domaineDroit.setPeriodeOnline(new PeriodeDroit());
    domaineDroit.getPeriodeOnline().setPeriodeDebut("2023/01/01");
    domaineDroit.setPrioriteDroit(new PrioriteDroit());
    createdDeclaration.setDomaineDroits(List.of(domaineDroit));
    createdDeclaration.getContrat().setPeriodeSuspensions(new ArrayList<>());

    for (PeriodeSuspension periodeSuspension : ListUtils.emptyIfNull(tbtd.getSuspensionPeriods())) {
      PeriodeSuspensionDeclaration newPer = new PeriodeSuspensionDeclaration();
      newPer.setDebut(periodeSuspension.getPeriode().getDebut());
      newPer.setFin(periodeSuspension.getPeriode().getFin());
      newPer.setTypeSuspension(periodeSuspension.getTypeSuspension());
      newPer.setMotifSuspension(periodeSuspension.getMotifSuspension());
      newPer.setMotifLeveeSuspension(periodeSuspension.getMotifLeveeSuspension());
      createdDeclaration.getContrat().getPeriodeSuspensions().add(newPer);
    }

    return createdDeclaration;
  }

  @Test
  void saveDeclarations() {}

  private ParametrageCarteTP getParametrageCarteTP() {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setAmc("1234567890");
    param.setIdentifiantCollectivite("COLPARAM");
    param.setGroupePopulation("COLLEGE_PARAM");
    param.setCritereSecondaireDetaille("CSD_PARAM");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    parametrageRenouvellement.setDateExecutionBatch("2021-01-01");
    // dateDeclenchementAutomatique;
    parametrageRenouvellement.setSeuilSecurite(1000);
    parametrageRenouvellement.setModeDeclenchement(ModeDeclenchementCarteTP.Manuel);
    param.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP

    ParametrageDroitsCarteTP parametrageDroitsCarteTP =
        TriggerDataForTesting.getParametrageDroitsCarteTP();

    param.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    return param;
  }

  private static Trigger createTrigger(String dateEffet) {
    Trigger trigger = new Trigger();
    trigger.setAmc("AMC1");
    trigger.setNbBenef(1);
    trigger.setNbBenefKO(0);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setStatus(TriggerStatus.ToProcess);
    trigger.setDateEffet(dateEffet);
    return trigger;
  }

  private List<DomaineDroit> createListDomaineDroit() {
    DomaineDroit domaineDroit1 =
        createDomaineDroit(
            "PHAR", "PHAR_GAR", "2021/01/01", "2031/12/31", "2021/01/01", null, null);

    DomaineDroit domaineDroit2 =
        createDomaineDroit("PHAR", "PHAR_GAR2", "2021/01/01", "2021/03/31", null, null, null);

    return List.of(domaineDroit1, domaineDroit2);
  }

  private DomaineDroit createDomaineDroit(
      String code,
      String codeGarantie,
      String debutDroit,
      String finDroit,
      String debutOnline,
      String finOnline,
      String codeCarence) {
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode(code);
    domaineDroit.setCodeGarantie(codeGarantie);

    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut(debutDroit);
    periodeDroit.setPeriodeFin(finDroit);
    periodeDroit.setMotifEvenement(MotifEvenement.DE.name());
    periodeDroit.setMotifEvenement(MotifEvenement.DE.getLibelle());
    domaineDroit.setPeriodeDroit(periodeDroit);

    PeriodeDroit periodeOnline = new PeriodeDroit();
    periodeOnline.setPeriodeDebut(debutOnline);
    periodeOnline.setPeriodeFin(finOnline);
    periodeOnline.setMotifEvenement(MotifEvenement.DE.name());
    periodeOnline.setMotifEvenement(MotifEvenement.DE.getLibelle());
    domaineDroit.setPeriodeOnline(periodeOnline);

    domaineDroit.setPrioriteDroit(new PrioriteDroit());

    domaineDroit.setCodeCarence(codeCarence);

    return domaineDroit;
  }

  /** Mocking subservice triggerDomaineService to avoid RestConnector call */
  private void mockTriggerDomaine(@NonNull GenerationDomaineResult generationDomaineResult)
      throws BobbNotFoundException,
          TriggerWarningException,
          PwException,
          CarenceException,
          TriggerParametersException,
          BeneficiaryToIgnoreException {
    doReturn(generationDomaineResult)
        .when(triggerDomaineService)
        .generationDomaine(
            nullable(TriggeredBeneficiary.class),
            nullable(ParametrageCarteTP.class),
            nullable(Trigger.class),
            nullable(String.class),
            nullable(boolean.class),
            nullable(List.class));
  }

  private TriggerBeneficiaryToDeclarations createTriggerBeneficiaryToDeclarations() {
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    TriggeredBeneficiary triggeredBeneficiary = TriggerDataForTesting.getTriggeredBenef1("UUID");
    Trigger trigger = createTrigger("2021-10-15");
    ParametrageCarteTP parametrageCarteTP = getParametrageCarteTP();

    triggerBeneficiaryToDeclarations.setTriggeredBeneficiary(triggeredBeneficiary);
    triggerBeneficiaryToDeclarations.setTrigger(trigger);
    triggerBeneficiaryToDeclarations.setParametrageCarteTP(parametrageCarteTP);

    return triggerBeneficiaryToDeclarations;
  }

  private Declaration getDeclarationOuverture() {
    Declaration declaration = new Declaration();
    declaration.set_id("DeclOuverture");
    declaration.setIdOrigine(null);

    List<DomaineDroit> domaineDroits = new ArrayList<>();

    // HOSP => 2023/01/01 - 2023/12/31
    DomaineDroit domaineHosp = new DomaineDroit();
    domaineHosp.setCode("HOSP");
    domaineHosp.setCodeGarantie("GAR");
    PeriodeDroit periodeHosp = new PeriodeDroit();
    periodeHosp.setPeriodeDebut("2023/01/01");
    periodeHosp.setPeriodeFin("2023/12/31");
    domaineHosp.setPeriodeDroit(periodeHosp);
    PeriodeDroit periodeOnlineHosp = new PeriodeDroit();
    periodeOnlineHosp.setPeriodeDebut("2023/01/01");
    domaineHosp.setPeriodeOnline(periodeOnlineHosp);
    domaineDroits.add(domaineHosp);

    // DENT => 2023/03/01 - 2023/12/31
    DomaineDroit domaineDent = new DomaineDroit();
    domaineDent.setCode("DENT");
    domaineDent.setCodeGarantie("GAR");
    PeriodeDroit periodeDent = new PeriodeDroit();
    periodeDent.setPeriodeDebut("2023/03/01");
    periodeDent.setPeriodeFin("2023/12/31");
    domaineDent.setPeriodeDroit(periodeDent);
    PeriodeDroit periodeOnlineDent = new PeriodeDroit();
    periodeOnlineDent.setPeriodeDebut("2023/03/01");
    domaineHosp.setPeriodeOnline(periodeOnlineDent);
    domaineDroits.add(domaineDent);

    // OPTI => 2023/04/01 - 2023/10/31
    DomaineDroit domaineOpti = new DomaineDroit();
    domaineOpti.setCode("OPTI");
    domaineOpti.setCodeGarantie("GAR");
    PeriodeDroit periodeOpti = new PeriodeDroit();
    periodeOpti.setPeriodeDebut("2023/04/01");
    periodeOpti.setPeriodeFin("2023/10/31");
    domaineOpti.setPeriodeDroit(periodeOpti);
    PeriodeDroit periodeOnlineOpti = new PeriodeDroit();
    periodeOnlineOpti.setPeriodeDebut("2023/04/01");
    domaineHosp.setPeriodeOnline(periodeOnlineOpti);
    domaineDroits.add(domaineOpti);

    declaration.setDomaineDroits(domaineDroits);

    return declaration;
  }

  private Declaration getDeclarationFermeture() {
    Declaration declaration = new Declaration();
    declaration.set_id("DeclFermeture");
    declaration.setIdOrigine("DeclOuverture");

    List<DomaineDroit> domaineDroits = new ArrayList<>();

    // HOSP => 2023/01/01 - 2023/07/31
    DomaineDroit domaineHosp = new DomaineDroit();
    domaineHosp.setCode("HOSP");
    domaineHosp.setCodeGarantie("GAR");
    PeriodeDroit periodeHosp = new PeriodeDroit();
    periodeHosp.setPeriodeDebut("2023/01/01");
    periodeHosp.setPeriodeFin("2023/07/31");
    domaineHosp.setPeriodeDroit(periodeHosp);
    PeriodeDroit periodeOnlineHosp = new PeriodeDroit();
    periodeOnlineHosp.setPeriodeDebut("2023/01/01");
    domaineHosp.setPeriodeOnline(periodeOnlineHosp);
    domaineDroits.add(domaineHosp);

    // DENT => 2023/03/01 - 2023/07/31
    DomaineDroit domaineDent = new DomaineDroit();
    domaineDent.setCode("DENT");
    domaineDent.setCodeGarantie("GAR");
    PeriodeDroit periodeDent = new PeriodeDroit();
    periodeDent.setPeriodeDebut("2023/03/01");
    periodeDent.setPeriodeFin("2023/07/31");
    domaineDent.setPeriodeDroit(periodeDent);
    PeriodeDroit periodeOnlineDent = new PeriodeDroit();
    periodeOnlineDent.setPeriodeDebut("2023/03/01");
    domaineHosp.setPeriodeOnline(periodeOnlineDent);
    domaineDroits.add(domaineDent);

    // OPTI => 2023/04/01 - 2023/07/31
    DomaineDroit domaineOpti = new DomaineDroit();
    domaineOpti.setCode("OPTI");
    domaineOpti.setCodeGarantie("GAR");
    PeriodeDroit periodeOpti = new PeriodeDroit();
    periodeOpti.setPeriodeDebut("2023/04/01");
    periodeOpti.setPeriodeFin("2023/07/31");
    domaineOpti.setPeriodeDroit(periodeOpti);
    PeriodeDroit periodeOnlineOpti = new PeriodeDroit();
    periodeOnlineOpti.setPeriodeDebut("2023/04/01");
    domaineHosp.setPeriodeOnline(periodeOnlineOpti);
    domaineDroits.add(domaineOpti);

    declaration.setDomaineDroits(domaineDroits);

    return declaration;
  }

  @Test
  void testEqualsDroit() {

    DroitAssure droit1 = new DroitAssure();
    droit1.setCode("Droit_Test_1");
    droit1.setCodeAssureur("ASSURE1");
    droit1.setLibelle("toto");
    droit1.setDateAncienneteGarantie("2002/01/01");
    droit1.setOrdrePriorisation("1");
    DroitAssure droit2 = new DroitAssure();
    droit2.setCode("Droit_Test_2");
    droit2.setCodeAssureur("ASSURE1");
    droit2.setLibelle("toto");
    droit2.setDateAncienneteGarantie("2002/01/01");
    droit2.setOrdrePriorisation("1");

    List<DroitAssure> droitAssureList = List.of(droit1);
    List<DroitAssure> droitAssureList2 = List.of(droit2);
    Assertions.assertTrue(
        triggerBuildDeclarationService.equalsDroits(droitAssureList, droitAssureList2));

    droit1.setCode("Droit_Test_1");
    droit1.setCodeAssureur("ASSURE1");
    droit2.setCode("Droit_Test_1");
    droit2.setCodeAssureur("ASSURE1");
    droit2.setDateAncienneteGarantie("2001/01/01");

    Assertions.assertTrue(
        triggerBuildDeclarationService.equalsDroits(droitAssureList, droitAssureList2));

    droit2.setDateAncienneteGarantie("2002/01/01");
    droit1.setOrdrePriorisation("2");

    Assertions.assertTrue(
        triggerBuildDeclarationService.equalsDroits(droitAssureList, droitAssureList2));

    droit1.setOrdrePriorisation("1");
    droit1.setType("type");

    Assertions.assertTrue(
        triggerBuildDeclarationService.equalsDroits(droitAssureList, droitAssureList2));

    droit1.setTypeToNull();

    Assertions.assertFalse(
        triggerBuildDeclarationService.equalsDroits(droitAssureList, droitAssureList2));
  }

  @Test
  void testChangementDroits() {

    DroitAssure droit1 = new DroitAssure();
    droit1.setCode("Droit_Test_1");
    droit1.setCodeAssureur("ASSURE1");
    droit1.setLibelle("toto");
    droit1.setDateAncienneteGarantie("2002/01/01");
    droit1.setOrdrePriorisation("1");
    droit1.setPeriode(new Periode("2020-01-01", "2025-01-01"));
    DroitAssure droit2 = new DroitAssure();
    droit2.setCode("Droit_Test_2");
    droit2.setCodeAssureur("ASSURE1");
    droit2.setLibelle("toto");
    droit2.setDateAncienneteGarantie("2002/01/01");
    droit2.setOrdrePriorisation("1");
    droit2.setPeriode(new Periode("2020-01-01", "2025-01-01"));

    List<DroitAssure> oldDroits = List.of(droit1);
    List<DroitAssure> newDroits = List.of(droit2);
    TriggerBeneficiaryToDeclarations triggerBeneficiaryToDeclarations =
        new TriggerBeneficiaryToDeclarations();
    Assertions.assertTrue(
        triggerBuildDeclarationService.changementDroits(
            oldDroits, newDroits, triggerBeneficiaryToDeclarations));

    droit2.setCode("Droit_Test_1");

    CarenceDroit carenceDroit = new CarenceDroit();
    carenceDroit.setCode("CAR001");
    carenceDroit.setPeriode(new PeriodeCarence("2023-01-01", "2023-02-01"));
    droit2.setCarences(List.of(carenceDroit));
    triggerBeneficiaryToDeclarations = new TriggerBeneficiaryToDeclarations();
    Assertions.assertTrue(
        triggerBuildDeclarationService.changementDroits(
            oldDroits, newDroits, triggerBeneficiaryToDeclarations));

    droit2.setCarencesToNull();

    droit2.setPeriode(new Periode("2020-01-01", "2024-01-01"));
    triggerBeneficiaryToDeclarations = new TriggerBeneficiaryToDeclarations();
    Assertions.assertTrue(
        triggerBuildDeclarationService.changementDroits(
            oldDroits, newDroits, triggerBeneficiaryToDeclarations));
    Assertions.assertTrue(triggerBeneficiaryToDeclarations.isChangeRightPeriods());
    droit2.setPeriode(new Periode("2020-01-01", "2025-01-01"));
    triggerBeneficiaryToDeclarations = new TriggerBeneficiaryToDeclarations();
    Assertions.assertFalse(
        triggerBuildDeclarationService.changementDroits(
            oldDroits, newDroits, triggerBeneficiaryToDeclarations));
  }

  @Test
  void shouldGenerateClosedDeclarations() {
    Trigger trigger = new Trigger();
    trigger.setId("1");
    trigger.setAmc("AMC1");
    trigger.setNbBenef(1);
    trigger.setNbBenefKO(0);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setStatus(TriggerStatus.ToProcess);
    ManageBenefsContract manageBenefsContract = new ManageBenefsContract();
    List<TriggeredBeneficiary> benefs = new ArrayList<>();
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setAmc("AMC1");
    benef.setNumeroContrat("CONTRAT1");
    benef.setNumeroAdherent("123");
    benef.setIdTrigger(trigger.getId());
    benef.setRangAdministratif("1");

    benef.setParametreAction(Constants.DELETE_ENDPOINT);
    List<DroitAssure> triggerServicePrestationDroitList = new ArrayList<>();
    benef.setOldContract(new ServicePrestationTriggerBenef());
    benef.getOldContract().setDroitsGaranties(triggerServicePrestationDroitList);

    benefs.add(benef);
    manageBenefsContract.setBenefs(benefs);
    manageBenefsContract.setDeclarations(new LinkedList<>());

    List<Declaration> existingDeclarations = new ArrayList<>();
    existingDeclarations.add(getDecl());
    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(Declaration.class)))
        .thenReturn(existingDeclarations);

    triggerBuildDeclarationService.manageBenefs(trigger, manageBenefsContract, false, null);
    Assertions.assertEquals(1, manageBenefsContract.getDeclarations().size());
    Assertions.assertEquals(
        "2021/12/31",
        manageBenefsContract.getDeclarations().stream()
            .findFirst()
            .get()
            .getDomaineDroits()
            .get(0)
            .getPeriodeDroit()
            .getPeriodeFin());
  }

  Declaration getDecl() {
    Declaration declaration = new Declaration();
    declaration.setIdDeclarant("AMC1");
    declaration.setEffetDebut(new Date());
    declaration.setDateModification(new Date());
    BeneficiaireV2 beneficiaire = new BeneficiaireV2();
    beneficiaire.setNirBeneficiaire("1234567890123");
    beneficiaire.setDateNaissance("19800101");
    beneficiaire.setRangNaissance("1");
    beneficiaire.setNumeroPersonne("12");
    declaration.setBeneficiaire(beneficiaire);
    Contrat contrat = new Contrat();
    contrat.setNumero("CONTRAT1");
    contrat.setNumeroAdherent("123");
    contrat.setRangAdministratif("1");
    declaration.setContrat(contrat);
    declaration.setDomaineDroits(
        Stream.of(new String[][] {new String[] {"OPT", "2022/01/01", "2022/12/02"}})
            .map(
                dataset -> {
                  DomaineDroit domaineDroit = new DomaineDroit();
                  domaineDroit.setCode(dataset[0]);
                  domaineDroit.setPeriodeDroit(new PeriodeDroit());
                  domaineDroit.getPeriodeDroit().setPeriodeDebut(dataset[1]);
                  domaineDroit.getPeriodeDroit().setPeriodeFin(dataset[2]);
                  domaineDroit.setPeriodeOnline(new PeriodeDroit());
                  domaineDroit.setPrioriteDroit(new PrioriteDroit());
                  return domaineDroit;
                })
            .toList());

    return declaration;
  }
}
