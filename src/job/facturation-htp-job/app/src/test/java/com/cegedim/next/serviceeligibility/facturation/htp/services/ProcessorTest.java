package com.cegedim.next.serviceeligibility.facturation.htp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduFacturationHTP;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.pojo.BillingResult;
import com.cegedim.next.serviceeligibility.facturation.htp.constants.Constants;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileSystemUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProcessorTest {

  Processor processor;

  @Mock private DeclarantService declarantService;

  @Mock private ContractService contractService;

  private File tempDirForCSV;

  private static final String DELEMITER = ";";

  /** Creation d un dossier temporaire pour l export des csv */
  @BeforeEach
  void beaforeEach() throws IOException {
    tempDirForCSV = Files.createTempDirectory("tmpDirCSV").toFile();
    processor = new Processor(contractService, declarantService);
  }

  /** Suppression des csv crees et du dossier temporaire */
  @AfterEach
  void afterEach() {
    FileSystemUtils.deleteRecursively(tempDirForCSV);
  }

  /** 2 declarants et aucun contrat -> 2 fichiers crees contenant seuelement le header de 1 ligne */
  @Test
  void twoDeclarantsNoContract() throws IOException {
    final List<String> idDeclarantsList = List.of("declarant_1", "declarant_2");
    doReturn(idDeclarantsList).when(declarantService).getAllDeclarantIDs();
    final CompteRenduFacturationHTP compteRenduFacturationHTP = new CompteRenduFacturationHTP();
    doReturn(Collections.emptyList())
        .when(contractService)
        .getContractsForBillingJob(Mockito.any());

    int processCode =
        processor.calcul(
            YearMonth.now().atEndOfMonth(),
            DELEMITER,
            tempDirForCSV.getAbsolutePath() + "/",
            compteRenduFacturationHTP);
    assertEquals(Constants.PROCESSED_WITHOUT_ERRORS, processCode);
    assertEquals(2, compteRenduFacturationHTP.getNbFichierCree());

    File[] csvFiles = Objects.requireNonNull(tempDirForCSV.listFiles());
    assertEquals(4, csvFiles.length);
    assertNumberOfLines(1, csvFiles);
  }

  /**
   * 1 declarant et un contrat -> un fichier crees, ce fichier doit etre rempli du header et d une
   * ligne par gestionnaire soit un total de 3 lignes
   */
  @Test
  void oneDeclarantOneContract() throws IOException {
    final String idDeclarant = "declarant_1";
    final List<String> idDeclarantList = List.of(idDeclarant);
    doReturn(idDeclarantList).when(declarantService).getAllDeclarantIDs();
    final LocalDate lastDayOfCurrentDate = YearMonth.now().atEndOfMonth();
    CompteRenduFacturationHTP compteRenduFacturationHTP = new CompteRenduFacturationHTP();
    compteRenduFacturationHTP.setDateCalcul(lastDayOfCurrentDate);

    BillingResult billingResult1 = createBillingResult(idDeclarant, "gestionnaire_1", 10);
    BillingResult billingResult2 = createBillingResult(idDeclarant, "gestionnaire_2", 5);
    BillingResult billingResult3 = createBillingResult("declarant_inconnu", "gestionnaire_1", 0);
    doReturn(List.of(billingResult1, billingResult2, billingResult3))
        .when(contractService)
        .getContractsForBillingJob(lastDayOfCurrentDate);

    int processCode =
        processor.calcul(
            lastDayOfCurrentDate,
            DELEMITER,
            tempDirForCSV.getAbsolutePath() + "/",
            compteRenduFacturationHTP);

    assertEquals(Constants.PROCESSED_WITHOUT_ERRORS, processCode);
    assertEquals(1, compteRenduFacturationHTP.getNbFichierCree());

    File[] csvFiles = Objects.requireNonNull(tempDirForCSV.listFiles());
    assertEquals(2, csvFiles.length);
    assertNumberOfLines(3, csvFiles);
  }

  /**
   * 2 declarants, pas de contrat. Un declarant provoque une erreur au moment de l ecriture du csv
   * -> un fichier cree, un fichier ko. Le fichier cree doit etre rempli seulement du header (1
   * ligne)
   */
  // TODO @Test
  void twoDeclarantNoContractOneError() throws IOException {
    final String dec2 = "dec_2";
    final List<String> idDeclarantList = List.of("dec_1", dec2);
    doReturn(idDeclarantList).when(declarantService).getAllDeclarantIDs();
    final LocalDate lastDayOfCurrentDate = YearMonth.now().atEndOfMonth();
    CompteRenduFacturationHTP compteRenduFacturationHTP = new CompteRenduFacturationHTP();
    compteRenduFacturationHTP.setDateCalcul(lastDayOfCurrentDate);

    doReturn(Collections.emptyList())
        .when(contractService)
        .getContractsForBillingJob(lastDayOfCurrentDate);

    int processCode =
        processor.calcul(
            lastDayOfCurrentDate,
            DELEMITER,
            tempDirForCSV.getAbsolutePath() + "/",
            compteRenduFacturationHTP);

    assertEquals(Constants.PROCESSED_WITH_ERRORS, processCode);
    assertEquals(1, compteRenduFacturationHTP.getNbFichierCree());
    assertEquals(1, compteRenduFacturationHTP.getNbFichierKO());

    File[] csvFiles = Objects.requireNonNull(tempDirForCSV.listFiles());
    assertEquals(2, csvFiles.length);
    assertNumberOfLines(1, csvFiles);
  }

  private BillingResult createBillingResult(String idDeclarant, String gestionnaire, int count) {
    BillingResult billingResult = new BillingResult();
    billingResult.setIdDeclarant(idDeclarant);
    billingResult.setGestionnaire(gestionnaire);
    billingResult.setCount(count);

    return billingResult;
  }

  /** Verifie que chaque fichier de la liste a exactement le nombre de lignes voulues */
  void assertNumberOfLines(int nbLines, File[] csvFiles) throws IOException {
    for (File csvFile : csvFiles) {
      if (csvFile.getName().endsWith(".csv")) {
        try (var lines = Files.lines(csvFile.toPath())) {
          assertEquals(nbLines, lines.count());
        }
      }
    }
  }
}
