package com.cegedim.next.serviceeligibility.almerysacl.batch.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micrometer.tracing.Tracer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProcessorTest {

  @Test
  void should_process_ok() throws IOException {
    SftpService s = Mockito.mock(SftpService.class);
    Tracer tracer = Mockito.mock(Tracer.class);
    Processor p = new Processor(tracer, s);
    List<String> sample = new ArrayList<>();
    sample.add("test");
    Mockito.when(s.listFiles()).thenReturn(sample);
    Mockito.when(s.processFolder(Mockito.any())).thenReturn(false);
    int status = p.readFolders();
    assertEquals(0, status);
  }
}
