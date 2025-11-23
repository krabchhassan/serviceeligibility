package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.entity.FileFlowMetadata;
import com.cegedim.next.serviceeligibility.core.model.enumeration.StatutFileFlowMetaData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class FileFlowMetadataServiceTest {

  @Autowired FileFlowMetadataService service;

  @Autowired MongoTemplate mongoTemplate;

  @Test
  void shouldCreate() {
    service.createFileFlowMetadata("JUNIT", "MASOURCE", "123");
  }

  @Test
  void shouldUpdate() {
    FileFlowMetadata fileFlowMetadata = new FileFlowMetadata();
    fileFlowMetadata.setNomFichier("JUNITUPD");
    fileFlowMetadata.setNomFichierARL("JUNITUPD_ARL");
    fileFlowMetadata.setNbLignesIntegrees(100L);
    fileFlowMetadata.setNbLignesLues(100L);
    fileFlowMetadata.setNbLignesRejetees(0L);
    fileFlowMetadata.setSource("TEST");
    fileFlowMetadata.setStatut(StatutFileFlowMetaData.Pending);
    service.updateFileFlowMetadata(fileFlowMetadata);
    fileFlowMetadata.setStatut(StatutFileFlowMetaData.Completed);
    service.updateFileFlowMetadata(fileFlowMetadata);
  }
}
