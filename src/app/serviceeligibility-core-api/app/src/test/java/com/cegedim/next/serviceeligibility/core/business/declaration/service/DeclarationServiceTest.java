package com.cegedim.next.serviceeligibility.core.business.declaration.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireInconnu;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireNonEligible;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheBeneficiaireService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheDeclarantService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheSegmentService;
import com.cegedim.next.serviceeligibility.core.business.contrat.dao.ContratDao;
import com.cegedim.next.serviceeligibility.core.business.declaration.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class DeclarationServiceTest {
  private static final String NUM_AMC_01 = "123456789";
  private static final String NUM_AMC_02 = "987654321";
  private static final String NUM_DECLARATION = "1111111111";
  private static final String NUM_CONTRAT = "2222222222";

  private static final int INFO_BENEF_INVALID = -1;
  private static final int INFO_BENEF_NO_RIGHTS = 0;
  private static final int INFO_BENEF_VALID = 1;

  private static final String NIR_BENEF_VALIDE = "2810399351178";
  private static final String CLE_NIR_BENEF_VALIDE = "58";
  private static final String DATE_NAISSANCE_BENEF_VALIDE = "19810301";
  private static final String RANG_NAISSANCE_BENEF_VALIDE = "2";
  private static final String NUM_PREFECTORAL_BENEF_VALIDE = NUM_AMC_01;
  private static final String NUM_ADHERENT_BENEF_VALIDE = "123456789";

  private static final String NIR_BENEF_NO_RIGHTS = "2810399351177";
  private static final String CLE_NIR_BENEF_NO_RIGHTS = "59";
  private static final String DATE_NAISSANCE_BENEF_NO_RIGHTS = "19810301";
  private static final String RANG_NAISSANCE_BENEF_NO_RIGHTS = "2";
  private static final String NUM_PREFECTORAL_BENEF_NO_RIGHTS = NUM_AMC_01;
  private static final String NUM_ADHERENT_BENEF_NO_RIGHTS = "123456789";

  private static final String NIR_BENEF_INVALIDE = "2810399351179";
  private static final String CLE_NIR_BENEF_INVALIDE = "57";
  private static final String DATE_NAISSANCE_BENEF_INVALIDE = "19000101";
  private static final String RANG_NAISSANCE_BENEF_INVALIDE = "2";
  private static final String NUM_PREFECTORAL_BENEF_INVALIDE = NUM_AMC_01;
  private static final String NUM_ADHERENT_BENEF_INVALIDE = "123456789";

  private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired private DeclarationService declarationService;

  @Autowired private DeclarantDao declarantDao;

  @Autowired private DeclarationDao declarationDao;

  @Autowired private ContratDao contratDao;

  void setupMocks(
      TypeRechercheDeclarantService typeRecherche,
      boolean isRechercheCarteFamille,
      boolean isSearchByIdPrefectoral,
      boolean isSearchByAdherent)
      throws Exception {
    mockDeclarant(typeRecherche);
    mockService();
    mockDeclaration(isRechercheCarteFamille, isSearchByIdPrefectoral, isSearchByAdherent);
    mockContract(isRechercheCarteFamille, isSearchByIdPrefectoral, isSearchByAdherent);
  }

  private void mockDeclarant(TypeRechercheDeclarantService typeRecherche) throws Exception {
    Declarant declarant = new Declarant();

    switch (typeRecherche) {
      case NUMERO_PREFECTORAL:
        declarant.set_id(NUM_AMC_01);
        break;

      case NUMERO_ECHANGE:
        declarant.set_id(NUM_AMC_02);
        break;

      default:
        throw new Exception("Invalid infoBenefType argument passed to getInfoBenef()");
    }

    Mockito.when(declarantDao.findById(NUM_AMC_01)).thenReturn(declarant);
  }

  private void mockService() {
    this.declarationService = Mockito.spy(declarationService);
    Mockito.doReturn(declarationDao).when(declarationService).getDeclarationDao();
  }

  private void mockDeclaration(
      boolean isRechercheCarteFamille,
      boolean isSearchByIdPrefectoral,
      boolean isSearchByAdherent) {
    // Existing benef
    Declaration declaration = getDeclaration();

    Mockito.doReturn(List.of(declaration))
        .when(declarationDao)
        .findDeclarationsByBeneficiaire(
            DATE_NAISSANCE_BENEF_VALIDE,
            RANG_NAISSANCE_BENEF_VALIDE,
            NIR_BENEF_VALIDE,
            CLE_NIR_BENEF_VALIDE,
            NUM_PREFECTORAL_BENEF_VALIDE,
            isRechercheCarteFamille,
            isSearchByIdPrefectoral,
            isSearchByAdherent,
            NUM_ADHERENT_BENEF_VALIDE);

    // Existing benef without rights
    Declaration emptyDeclaration = new Declaration();
    emptyDeclaration.setIdDeclarant(NUM_AMC_01);

    Mockito.doReturn(List.of(emptyDeclaration))
        .when(declarationDao)
        .findDeclarationsByBeneficiaire(
            DATE_NAISSANCE_BENEF_NO_RIGHTS,
            RANG_NAISSANCE_BENEF_NO_RIGHTS,
            NIR_BENEF_NO_RIGHTS,
            CLE_NIR_BENEF_NO_RIGHTS,
            NUM_PREFECTORAL_BENEF_NO_RIGHTS,
            isRechercheCarteFamille,
            isSearchByIdPrefectoral,
            isSearchByAdherent,
            NUM_ADHERENT_BENEF_NO_RIGHTS);

    // Unknown benef
    Mockito.doReturn(new ArrayList<>())
        .when(declarationDao)
        .findDeclarationsByBeneficiaire(
            DATE_NAISSANCE_BENEF_INVALIDE,
            RANG_NAISSANCE_BENEF_INVALIDE,
            NIR_BENEF_INVALIDE,
            CLE_NIR_BENEF_INVALIDE,
            NUM_PREFECTORAL_BENEF_INVALIDE,
            isRechercheCarteFamille,
            isSearchByIdPrefectoral,
            isSearchByAdherent,
            NUM_ADHERENT_BENEF_INVALIDE);

    if (isRechercheCarteFamille) {
      Mockito.doReturn(List.of(declaration))
          .when(declarationDao)
          .findDeclarationsByNumeroContrat(NUM_PREFECTORAL_BENEF_VALIDE, NUM_DECLARATION, true);

      Mockito.doReturn(List.of(declaration))
          .when(declarationDao)
          .findDeclarationsByNumeroContrat(NUM_PREFECTORAL_BENEF_VALIDE, NUM_DECLARATION, false);
    }
  }

  private void mockContract(
      boolean isRechercheCarteFamille,
      boolean isSearchByIdPrefectoral,
      boolean isSearchByAdherent) {
    // Existing benef
    ContractTP contractTP = getContract();

    Mockito.doReturn(List.of(contractTP))
        .when(contratDao)
        .findContractsTPByBeneficiary(
            DATE_NAISSANCE_BENEF_VALIDE,
            RANG_NAISSANCE_BENEF_VALIDE,
            NIR_BENEF_VALIDE,
            CLE_NIR_BENEF_VALIDE,
            NUM_PREFECTORAL_BENEF_VALIDE,
            isRechercheCarteFamille,
            isSearchByIdPrefectoral,
            isSearchByAdherent,
            NUM_ADHERENT_BENEF_VALIDE);

    // Existing benef without rights
    ContractTP emptyContractTP = new ContractTP();
    emptyContractTP.setIdDeclarant(NUM_AMC_01);

    Mockito.doReturn(List.of(emptyContractTP))
        .when(contratDao)
        .findContractsTPByBeneficiary(
            DATE_NAISSANCE_BENEF_NO_RIGHTS,
            RANG_NAISSANCE_BENEF_NO_RIGHTS,
            NIR_BENEF_NO_RIGHTS,
            CLE_NIR_BENEF_NO_RIGHTS,
            NUM_PREFECTORAL_BENEF_NO_RIGHTS,
            isRechercheCarteFamille,
            isSearchByIdPrefectoral,
            isSearchByAdherent,
            NUM_ADHERENT_BENEF_NO_RIGHTS);

    // Unknown benef
    Mockito.doReturn(new ArrayList<>())
        .when(contratDao)
        .findContractsTPByBeneficiary(
            DATE_NAISSANCE_BENEF_INVALIDE,
            RANG_NAISSANCE_BENEF_INVALIDE,
            NIR_BENEF_INVALIDE,
            CLE_NIR_BENEF_INVALIDE,
            NUM_PREFECTORAL_BENEF_INVALIDE,
            isRechercheCarteFamille,
            isSearchByIdPrefectoral,
            isSearchByAdherent,
            NUM_ADHERENT_BENEF_INVALIDE);
  }

  /**
   * @return a declaration with the following rights : OPTI (2023/01/01 -> 2023/12/31), DENT
   *     (2023/07/01 -> 2023/12/31), MEDG (2023/01/01 -> 2023/10/31)
   */
  private Declaration getDeclaration() {
    Declaration declaration = new Declaration();

    declaration.setCodeEtat("V");
    declaration.setEffetDebut(new Date());
    declaration.setIdDeclarant(NUM_AMC_01);

    Contrat contrat = new Contrat();
    contrat.setNumero(NUM_DECLARATION);
    declaration.setContrat(contrat);

    DomaineDroit domaineOpti = buildDomaine("OPTI", "2023/01/01", "2023/12/31");
    DomaineDroit domaineDent = buildDomaine("DENT", "2023/07/01", "2023/12/31");
    DomaineDroit domaineMedg = buildDomaine("MEDG", "2023/01/01", "2023/10/31");

    declaration.setDomaineDroits(List.of(domaineOpti, domaineDent, domaineMedg));

    BeneficiaireV2 benef = new BeneficiaireV2();
    benef.setDateNaissance(DATE_NAISSANCE_BENEF_VALIDE);
    benef.setRangNaissance(RANG_NAISSANCE_BENEF_VALIDE);
    benef.setNirBeneficiaire(NIR_BENEF_VALIDE);
    benef.setCleNirBeneficiaire(CLE_NIR_BENEF_VALIDE);
    benef.setNirOd1(NIR_BENEF_VALIDE);
    benef.setCleNirOd1(CLE_NIR_BENEF_VALIDE);
    declaration.setBeneficiaire(benef);

    return declaration;
  }

  /**
   * @return a contract with the following rights : OPTI (2023/01/01 -> 2023/12/31), DENT
   *     (2023/07/01 -> 2023/12/31), MEDG (2023/01/01 -> 2023/10/31)
   */
  private ContractTP getContract() {
    ContractTP contractTP = new ContractTP();

    contractTP.setIdDeclarant(NUM_PREFECTORAL_BENEF_VALIDE);
    contractTP.setNumeroAdherent(NUM_ADHERENT_BENEF_VALIDE);
    contractTP.setNumeroContrat(NUM_CONTRAT);

    BeneficiaireContractTP benef = new BeneficiaireContractTP();
    benef.setDateModification(LocalDateTime.now());
    benef.setNirBeneficiaire(NIR_BENEF_VALIDE);
    benef.setCleNirBeneficiaire(CLE_NIR_BENEF_VALIDE);
    benef.setDateNaissance(DATE_NAISSANCE_BENEF_VALIDE);
    benef.setRangNaissance(RANG_NAISSANCE_BENEF_VALIDE);

    DomaineDroitContractTP domaineOpti =
        buildDomaineContract("OPTI", List.of(new Periode("2023/01/01", "2023/12/31")));
    DomaineDroitContractTP domaineDent =
        buildDomaineContract("DENT", List.of(new Periode("2023/07/01", "2023/12/31")));
    DomaineDroitContractTP domaineMedg =
        buildDomaineContract("MEDG", List.of(new Periode("2023/01/01", "2023/10/31")));

    benef.setDomaineDroits(List.of(domaineOpti, domaineDent, domaineMedg));

    contractTP.setBeneficiaires(List.of(benef));

    return contractTP;
  }

  private DomaineDroit buildDomaine(String code, String dateDebut, String dateFin) {
    DomaineDroit domaine = new DomaineDroit();

    domaine.setCode(code);
    domaine.setCodeProduit("produit01");
    domaine.setCodeGarantie("base01");

    PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut(dateDebut);
    periode.setPeriodeFin(dateFin);
    domaine.setPeriodeDroit(periode);

    PrioriteDroit priorite = new PrioriteDroit();
    priorite.setCode("01");
    domaine.setPrioriteDroit(priorite);

    return domaine;
  }

  private DomaineDroitContractTP buildDomaineContract(String code, List<Periode> periodes) {
    DomaineDroitContractTP domaine = new DomaineDroitContractTP();

    domaine.setCode(code);

    List<PeriodeDroitContractTP> periodesDroits = new ArrayList<>();

    for (Periode periode : periodes) {
      PeriodeDroitContractTP periodeDroit = new PeriodeDroitContractTP();

      periodeDroit.setPeriodeDebut(periode.getDebut());
      periodeDroit.setPeriodeFin(periode.getFin());

      periodesDroits.add(periodeDroit);
    }
    PrioriteDroitContrat priorite = new PrioriteDroitContrat();
    priorite.setCode("01");

    Periode periode = new Periode();
    periode.setDebut("2021/01/01");
    periode.setFin("2021/12/31");

    RemboursementContrat remboursementContrat = new RemboursementContrat();
    remboursementContrat.setPeriodes(List.of(periode));
    remboursementContrat.setTauxRemboursement("01");

    Garantie garantie = new Garantie();
    garantie.setCodeGarantie("GAR");
    Produit produit = new Produit();
    produit.setCodeProduit("PROD");
    ReferenceCouverture referenceCouverture = new ReferenceCouverture();
    referenceCouverture.setReferenceCouverture("toto");
    NaturePrestation naturePrestation = new NaturePrestation();
    naturePrestation.setPeriodesDroit(periodesDroits);
    naturePrestation.setPrioritesDroit(List.of(priorite));
    naturePrestation.setRemboursements(List.of(remboursementContrat));
    referenceCouverture.setNaturesPrestation(List.of(naturePrestation));
    produit.setReferencesCouverture(List.of(referenceCouverture));
    garantie.setProduits(List.of(produit));
    domaine.setGaranties(List.of(garantie));

    return domaine;
  }

  /**
   * Get a DemandeInfoBeneficiaire object to call getDroitsBeneficiaire
   *
   * @param from start date (format yyyy-MM-dd)
   * @param to end date (format yyyy-MM-dd)
   * @param infoBenefType type of infoBenef to return
   */
  private DemandeInfoBeneficiaire getInfoBenef(
      String from, String to, int infoBenefType, TypeRechercheBeneficiaireService typeRecherche)
      throws Exception {
    DemandeInfoBeneficiaire infoBenef = new DemandeInfoBeneficiaire();
    Date dateReference = format.parse(from);
    Date dateFin = to == null ? null : format.parse(to);

    infoBenef.setTypeRechercheSegment(TypeRechercheSegmentService.LISTE_SEGMENT);
    infoBenef.setSegmentRecherche("3");
    infoBenef.setListeSegmentRecherche(List.of("OPTI", "DENT"));
    infoBenef.setDateReference(dateReference);
    infoBenef.setDateFin(dateFin);
    infoBenef.setProfondeurRecherche(TypeProfondeurRechercheService.AVEC_FORMULES);
    infoBenef.setTypeRechercheBeneficiaire(typeRecherche);

    switch (infoBenefType) {
      case INFO_BENEF_INVALID:
        // Get non-existant beneficiary infos
        infoBenef.setNirBeneficiaire(NIR_BENEF_INVALIDE);
        infoBenef.setCleNirBneficiare(CLE_NIR_BENEF_INVALIDE);
        infoBenef.setDateNaissance(DATE_NAISSANCE_BENEF_INVALIDE);
        infoBenef.setRangNaissance(RANG_NAISSANCE_BENEF_INVALIDE);
        infoBenef.setNumeroPrefectoral(NUM_PREFECTORAL_BENEF_INVALIDE);
        infoBenef.setNumeroAdherent(NUM_ADHERENT_BENEF_INVALIDE);
        break;

      case INFO_BENEF_NO_RIGHTS:
        // Get existing beneficiary without rights infos
        infoBenef.setNirBeneficiaire(NIR_BENEF_NO_RIGHTS);
        infoBenef.setCleNirBneficiare(CLE_NIR_BENEF_NO_RIGHTS);
        infoBenef.setDateNaissance(DATE_NAISSANCE_BENEF_NO_RIGHTS);
        infoBenef.setRangNaissance(RANG_NAISSANCE_BENEF_NO_RIGHTS);
        infoBenef.setNumeroPrefectoral(NUM_PREFECTORAL_BENEF_NO_RIGHTS);
        infoBenef.setNumeroAdherent(NUM_ADHERENT_BENEF_NO_RIGHTS);
        break;

      case INFO_BENEF_VALID:
        // Get existing beneficiary infos
        infoBenef.setNirBeneficiaire(NIR_BENEF_VALIDE);
        infoBenef.setCleNirBneficiare(CLE_NIR_BENEF_VALIDE);
        infoBenef.setDateNaissance(DATE_NAISSANCE_BENEF_VALIDE);
        infoBenef.setRangNaissance(RANG_NAISSANCE_BENEF_VALIDE);
        infoBenef.setNumeroPrefectoral(NUM_PREFECTORAL_BENEF_VALIDE);
        infoBenef.setNumeroAdherent(NUM_ADHERENT_BENEF_VALIDE);
        break;

      default:
        throw new Exception("Invalid infoBenefType argument passed to getInfoBenef()");
    }

    return infoBenef;
  }

  @Test
  void getDeclarationDaoTest() {
    DeclarationDao dao = declarationService.getDeclarationDao();
    Assertions.assertNotNull(dao);
  }

  @Test
  void shouldThrowBeneficiaireInconnu() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-06-01",
            "2023-12-31",
            INFO_BENEF_INVALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    Assertions.assertThrows(
        ExceptionServiceBeneficiaireInconnu.class,
        () -> declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false));
  }

  @Test
  void shouldThrowDroitNonOuvert() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2024-06-01",
            "2024-12-31",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    Assertions.assertThrows(
        ExceptionServiceDroitNonOuvert.class,
        () -> declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false));
  }

  @Test
  void shouldThrowBeneficiaireNonEligible() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-06-01",
            "2023-12-31",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    infoBenef.setListeSegmentRecherche(List.of("PHAR1"));
    Assertions.assertThrows(
        ExceptionServiceBeneficiaireNonEligible.class,
        () -> declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false));
  }

  @Test
  void getDroitsBeneficiaireV2NoEndTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false);

    // We should only get rights for OPTI domain
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV3NoEndTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, true, false, false);

    // We should only get rights for OPTI domain
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV4NoEndTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, false, true, false);

    // We should only get rights for OPTI domain
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV2NoEndTwoRightsTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-08-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV3NoEndTwoRightsTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-08-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, true, false, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV4NoEndTwoRightsTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-08-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, false, true, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineDent.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV2Test() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01",
            "2023-10-01",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV3Test() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01",
            "2023-10-01",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, true, false, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV4Test() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, false, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01",
            "2023-10-01",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, false, true, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-01"), domaineDent.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-01"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV2NoEndCarteFamilleTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, true, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.CARTE_FAMILLE);
    List<DeclarationDto> declarations =
        declarationService.getDeclarationsCarteTiersPayant(infoBenef, true, false);

    // We should get rights for MEDG and OPTI since search by CarteFamille doesn't
    // filter out MEDG
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    Assertions.assertEquals(2, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("MEDG", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-31"), domaineDent.getPeriodeDroit().getPeriodeFin());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(1);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV3NoEndCarteFamilleTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_PREFECTORAL, true, true, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.CARTE_FAMILLE);
    List<DeclarationDto> declarations =
        declarationService.getDeclarationsCarteTiersPayant(infoBenef, false, true);

    // We should get rights for MEDG and OPTI since search by CarteFamille doesn't
    // filter out MEDG
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    Assertions.assertEquals(2, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("MEDG", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-31"), domaineDent.getPeriodeDroit().getPeriodeFin());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(1);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV2NumEchangeTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_ECHANGE, false, false, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01",
            "2023-10-01",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, true, false, false, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV3NumEchangeTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_ECHANGE, false, false, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01",
            "2023-10-01",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, true, false, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV4NumEchangeTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_ECHANGE, false, false, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01",
            "2023-10-01",
            INFO_BENEF_VALID,
            TypeRechercheBeneficiaireService.BENEFICIAIRE);
    List<DeclarationDto> declarations =
        declarationService.getDroitsBeneficiaire(infoBenef, false, false, true, false);

    // We should get rights for both OPTI and DENT domains
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(2, declarations.size());

    Assertions.assertEquals(1, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("DENT", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-07-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-01"), domaineDent.getPeriodeDroit().getPeriodeFin());

    Assertions.assertEquals(1, declarations.get(1).getDomaineDroits().size());

    DomaineDroitDto domaineOpti = declarations.get(1).getDomaineDroits().get(0);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-01"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV2NoEndCarteFamilleNumEchangeTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_ECHANGE, true, false, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.CARTE_FAMILLE);
    List<DeclarationDto> declarations =
        declarationService.getDeclarationsCarteTiersPayant(infoBenef, true, false);

    // We should get rights for MEDG and OPTI since search by CarteFamille doesn't
    // filter out MEDG
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    Assertions.assertEquals(2, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("MEDG", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-31"), domaineDent.getPeriodeDroit().getPeriodeFin());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(1);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void getDroitsBeneficiaireV3NoEndCarteFamilleNumEchangeTest() throws Exception {
    setupMocks(TypeRechercheDeclarantService.NUMERO_ECHANGE, true, false, false);

    DemandeInfoBeneficiaire infoBenef =
        getInfoBenef(
            "2023-01-01", null, INFO_BENEF_VALID, TypeRechercheBeneficiaireService.CARTE_FAMILLE);
    List<DeclarationDto> declarations =
        declarationService.getDeclarationsCarteTiersPayant(infoBenef, false, true);

    // We should get rights for MEDG and OPTI since search by CarteFamille doesn't
    // filter out MEDG
    Assertions.assertNotNull(declarations);
    Assertions.assertEquals(1, declarations.size());

    Assertions.assertEquals(2, declarations.get(0).getDomaineDroits().size());

    DomaineDroitDto domaineDent = declarations.get(0).getDomaineDroits().get(0);
    Assertions.assertEquals("MEDG", domaineDent.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineDent.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-10-31"), domaineDent.getPeriodeDroit().getPeriodeFin());

    DomaineDroitDto domaineOpti = declarations.get(0).getDomaineDroits().get(1);
    Assertions.assertEquals("OPTI", domaineOpti.getCode());
    Assertions.assertEquals(
        format.parse("2023-01-01"), domaineOpti.getPeriodeDroit().getPeriodeDebut());
    Assertions.assertEquals(
        format.parse("2023-12-31"), domaineOpti.getPeriodeDroit().getPeriodeFin());
  }

  @Test
  void filterDeclarationsWithNoDeclarations() {
    Set<String> segmentRecherche = null;
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, Collections.emptyList(), filteredDeclarations));
    Assertions.assertEquals(0, filteredDeclarations.size());
  }

  private static DemandeInfoBeneficiaire initInfoBeneficiaire() {
    DemandeInfoBeneficiaire infoBeneficiaire = new DemandeInfoBeneficiaire();
    LocalDateTime currentDate = LocalDateTime.of(2024, 5, 5, 10, 10);
    infoBeneficiaire.setDateReference(Date.from(currentDate.toInstant(ZoneOffset.UTC)));
    return infoBeneficiaire;
  }

  @Test
  void filterDeclarationsWithNoLastDeclarationOuverture() {
    Set<String> segmentRecherche = null;
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_VALIDE);
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(1, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationOuvertureWithPHNOAndPHAR() {
    Set<String> segmentRecherche = Set.of("PHAR");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_VALIDE);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertTrue(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(0, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationOuvertureWithPHNO() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_VALIDE);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/01/01");
    periodeDroit.setPeriodeFin("2023/12/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(1, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationFermetureWithPHNO() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_RESILIATION);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/01/01");
    periodeDroit.setPeriodeFin("2023/05/31");
    periodeDroit.setPeriodeDebut("2023/06/01");
    periodeDroit.setPeriodeDebut("2023/12/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(0, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationFermetureWithPHNO2() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_RESILIATION);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2024/01/01");
    periodeDroit.setPeriodeFin("2024/05/31");
    periodeDroit.setPeriodeFermetureDebut("2024/06/01");
    periodeDroit.setPeriodeFermetureFin("2024/12/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(1, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationFermetureWithPHNO2WithFin() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    LocalDateTime currentDate = LocalDateTime.of(2024, 6, 5, 10, 10);
    infoBeneficiaire.setDateFin(Date.from(currentDate.toInstant(ZoneOffset.UTC)));
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_RESILIATION);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2024/01/01");
    periodeDroit.setPeriodeFin("2024/05/31");
    periodeDroit.setPeriodeFermetureDebut("2024/06/01");
    periodeDroit.setPeriodeFermetureFin("2024/12/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(1, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationFermetureWithPHNO3() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = createClosedDeclaration();
    declarations.add(declaration);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(0, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationFermetureAndOuvertureWithPHNO3() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = createClosedDeclaration();
    Declaration declaration1 = createOpenDeclaration();
    declarations.add(declaration);
    declarations.add(declaration1);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(0, filteredDeclarations.size());
  }

  @Test
  void filterDeclarationsWithDeclarationFermetureAndOuvertureWithPHNO3WithFin() {
    Set<String> segmentRecherche = Set.of("PHNO");
    DemandeInfoBeneficiaire infoBeneficiaire = initInfoBeneficiaire();
    LocalDateTime currentDate = LocalDateTime.of(2024, 6, 5, 10, 10);
    infoBeneficiaire.setDateFin(Date.from(currentDate.toInstant(ZoneOffset.UTC)));
    List<Declaration> declarations = new ArrayList<>();
    Declaration declaration = createClosedDeclaration();
    Declaration declaration1 = createOpenDeclaration();
    declarations.add(declaration);
    declarations.add(declaration1);
    List<Declaration> filteredDeclarations = new ArrayList<>();
    Assertions.assertFalse(
        DeclarationServiceImpl.filterDeclarations(
            infoBeneficiaire, segmentRecherche, declarations, filteredDeclarations));
    Assertions.assertEquals(0, filteredDeclarations.size());
  }

  private static Declaration createClosedDeclaration() {
    Declaration declaration = new Declaration();
    declaration.set_id("1");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 4, 10, 10);
    declaration.setCodeEtat(Constants.CODE_ETAT_RESILIATION);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/01/01");
    periodeDroit.setPeriodeFin("2024/01/31");
    periodeDroit.setPeriodeFermetureDebut("2024/02/01");
    periodeDroit.setPeriodeFermetureFin("2024/03/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    return declaration;
  }

  private static Declaration createOpenDeclaration() {
    Declaration declaration = new Declaration();
    declaration.set_id("2");
    LocalDateTime effetDebut = LocalDateTime.of(2024, 5, 3, 10, 20);
    declaration.setCodeEtat(Constants.CODE_ETAT_VALIDE);
    DomaineDroit domaineDroit = new DomaineDroit();
    domaineDroit.setCode("PHNO");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2023/01/01");
    periodeDroit.setPeriodeFin("2024/01/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    declaration.setDomaineDroits(List.of(domaineDroit));
    declaration.setEffetDebut(Date.from(effetDebut.toInstant(ZoneOffset.UTC)));
    return declaration;
  }
}
