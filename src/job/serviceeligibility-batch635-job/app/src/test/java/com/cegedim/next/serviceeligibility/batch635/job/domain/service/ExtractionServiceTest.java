package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.createDirectoryIfNotExists;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFilesFromDirectory;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.EXTRACTION_START_FILE;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.OUTPUT_TMP_FOLDER;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import com.cegedim.next.serviceeligibility.batch635.job.domain.model.Declarants;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.CustomContratsRepository;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection.PeriodeDroitProjection;
import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class ExtractionServiceTest {

  @MockBean private CustomContratsRepository customContratsRepository;

  @MockBean private DeclarantsService declarantsService;

  @Autowired private ExtractionService extractionService;

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @BeforeEach
  public void beforeEach() throws IOException {
    createDirectoryIfNotExists(outputPefbFolder);
    createDirectoryIfNotExists(OUTPUT_TMP_FOLDER);
    deleteFilesFromDirectory(outputPefbFolder);
    deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
  }

  @AfterEach
  public void afterEach() throws IOException {
    deleteFilesFromDirectory(outputPefbFolder);
    deleteFilesFromDirectory(OUTPUT_TMP_FOLDER);
  }

  @Test
  void shouldExtractAmc_whenCallToServiceIsOk() throws IOException {
    Declarants declarants = getDeclarants();

    AmcReferenceDateLineEligible amcReferenceDateLineEligible =
        AmcReferenceDateLineEligible.builder().amc("0000012898").referenceDate("20210908").build();
    Path arlFilePath = Paths.get(outputPefbFolder, "fileName.csv");
    PrintWriter printWriter = new PrintWriter(new FileWriter(arlFilePath.toString()));

    when(customContratsRepository.extractPeriodesDroit(
            amcReferenceDateLineEligible.getAmc(),
            amcReferenceDateLineEligible.getReferenceDate(),
            0))
        .thenReturn(getExtractionResult());

    doReturn(declarants).when(declarantsService).getDeclarantById("0000012898");

    doReturn("DatabaseIdentity").when(declarantsService).getIdentityByDeclarants(declarants);

    Runnable extraction =
        extractionService.extract(
            amcReferenceDateLineEligible, "fileName.csv", printWriter, new ArrayList<>());

    extraction.run();
    printWriter.close();
    int filesNumber = getFolderFilesNumber();
    Assertions.assertEquals(2, filesNumber);
  }

  private Declarants getDeclarants() {
    Declarants declarants = new Declarants();
    declarants.setEmetteurDroits("EME");
    declarants.setCodePartenaire("Code");
    declarants.setCodeCircuit("Code");
    declarants.setNom("Nom");
    return declarants;
  }

  private List<PeriodeDroitProjection> getExtractionResult() {
    PeriodeDroitProjection periodeDroitProjection = new PeriodeDroitProjection();
    periodeDroitProjection.setPeriodeDebut("2020/12/12");
    periodeDroitProjection.setPeriodeFin("2022/12/12");
    return List.of(periodeDroitProjection);
  }

  private int getFolderFilesNumber() throws IOException {
    Stream<Path> stream = Files.walk(Paths.get(outputPefbFolder), 1);
    Set<String> fileNames =
        stream
            .filter(file -> !Files.isDirectory(file))
            .map(Path::getFileName)
            .map(Path::toString)
            .filter(f -> f.startsWith(EXTRACTION_START_FILE))
            .collect(Collectors.toSet());
    return fileNames.size();
  }
}
