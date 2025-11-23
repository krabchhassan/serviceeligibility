package com.cegedim.next.serviceeligibility.rdoserviceprestation.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdo;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.rdoserviceprestation.config.TestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfiguration.class})
class ProcessorTest {

  @Autowired private CrexProducer crexProducer;

  @Test
  void should_process_ok() {
    FileService s = Mockito.mock(FileService.class);
    Processor p = new Processor(s, crexProducer);
    Mockito.when(s.init()).thenReturn(true);
    Mockito.when(s.processFolderV5(Mockito.any(String.class), Mockito.any(CompteRenduRdo.class)))
        .thenReturn(false);
    int status = p.readFolders("V5");
    Assertions.assertEquals(0, status);
  }

  @Test
  void should_process_ko_for_folders() {
    FileService s = Mockito.mock(FileService.class);
    Processor p = new Processor(s, crexProducer);
    Mockito.when(s.init()).thenReturn(false);
    int status = p.readFolders("V5");
    Assertions.assertEquals(1, status);
  }
}
