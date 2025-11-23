package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.createDirectoryIfNotExists;
import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.deleteFilesFromDirectory;

import com.cegedim.next.serviceeligibility.batch635.job.configuration.TestConfiguration;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
class FileServiceTest {

  @Autowired FileService fileService;

  @Value("${SERVICE_ELIGIBILITY_PEFB_OUTPUT}")
  private String outputPefbFolder;

  @BeforeEach
  public void beforeEach() throws IOException {
    createDirectoryIfNotExists(outputPefbFolder);
    deleteFilesFromDirectory(outputPefbFolder);
  }

  @AfterEach
  public void afterEach() throws IOException {
    deleteFilesFromDirectory(outputPefbFolder);
  }

  @Test
  void should_get_folder_files_name() {
    Set<String> filesName = fileService.getPefbFolderFileNames();
    Assertions.assertEquals(1, filesName.size());
    Assertions.assertEquals("Extraction_droit_635.csv", filesName.stream().findFirst().get());
  }
}
