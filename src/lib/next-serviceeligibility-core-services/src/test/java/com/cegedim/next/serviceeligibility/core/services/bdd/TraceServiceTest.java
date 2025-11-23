package com.cegedim.next.serviceeligibility.core.services.bdd;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.TRACES;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.enumeration.TraceSource;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.TraceStatus;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

/**
 * Important : All dependant tests must be prefixed by stage{number} to keep execution order This is
 * more an integration than a junit one
 */
@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TraceServiceTest {

  @Autowired MongoTemplate mongoTemplate;

  @Autowired private TraceService traceService;

  @BeforeEach
  public void before() {
    mongoTemplate.dropCollection(TRACES);

    Mockito.when(mongoTemplate.save(Mockito.any(Trace.class), Mockito.anyString()))
        .thenReturn(getTrace(TraceStatus.SuccesfullyProcessed));
  }

  @Test
  void stage1_should_create_trace() {
    String service = "test";
    String declarationId = "1";
    String id = traceService.createTraceForDeclaration(service, declarationId, TRACES);
    Assertions.assertNotNull(id);
  }

  @Test
  void stage1_should_create_fulltrace() {
    String service = "test";
    String declarationId = "1";
    String id =
        traceService.createTrace(
            service,
            declarationId,
            TraceSource.File,
            TraceStatus.SuccesfullyProcessed,
            "FichierTest",
            TRACES);
    Assertions.assertNotNull(id);
  }

  @Test
  void stage2_should_update_trace() {
    String service = "test";
    String declarationId = "1";
    String id = traceService.createTraceForDeclaration(service, declarationId, TRACES);

    traceService.updateStatus(id, TraceStatus.SuccesfullyProcessed, TRACES);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(Trace.class), Mockito.anyString()))
        .thenReturn(getTrace(TraceStatus.SuccesfullyProcessed));
    Trace trace = traceService.getTrace(id, TRACES);

    // Assertions
    Assertions.assertNotNull(trace);
    Assertions.assertEquals(id, trace.getId());
    Assertions.assertEquals(TraceStatus.SuccesfullyProcessed.name(), trace.getStatus().name());
  }

  @Test
  void stage3_should_complete_trace() {
    String service = "test";
    String declarationId = "1";
    String benefId = "3";
    String id = traceService.createTraceForDeclaration(service, declarationId, TRACES);

    traceService.completeTrace(id, TraceStatus.SuccesfullyProcessed, benefId, TRACES);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(Trace.class), Mockito.anyString()))
        .thenReturn(getTrace(TraceStatus.SuccesfullyProcessed));
    Trace trace = traceService.getTrace(id, TRACES);

    // Assertions
    Assertions.assertNotNull(trace);
    Assertions.assertEquals(id, trace.getId());
    Assertions.assertEquals(TraceStatus.SuccesfullyProcessed.name(), trace.getStatus().name());
  }

  @Test
  void stage4_should_update_status_error_trace() {
    final String service = "test";
    final String declarationId = "1";
    final String errorMessage = "There is an error";
    final String id = traceService.createTraceForDeclaration(service, declarationId, TRACES);

    traceService.updateStatusError(id, TraceStatus.ErrorDeserializing, errorMessage, TRACES);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(Trace.class), Mockito.anyString()))
        .thenReturn(getTrace(TraceStatus.ErrorDeserializing));
    Trace trace = traceService.getTrace(id, TRACES);
    trace.setErrorMessage(errorMessage);

    // Assertions
    Assertions.assertNotNull(trace);
    Assertions.assertEquals(id, trace.getId());
    Assertions.assertEquals(TraceStatus.ErrorDeserializing.name(), trace.getStatus().name());
    Assertions.assertEquals(errorMessage, trace.getErrorMessage());
  }

  @Test
  void stage5_should_update_status_error_trace() {
    final String service = "test";
    final String declarationId = "1";
    final String id = traceService.createTraceForDeclaration(service, declarationId, TRACES);
    List<String> errorMessages = new ArrayList<>();
    errorMessages.add("First error");
    errorMessages.add("Second error");
    Long lineNumber = 12L;
    traceService.updateStatusError(
        id, TraceStatus.ErrorDeserializing, errorMessages, lineNumber, TRACES);
    Mockito.when(
            mongoTemplate.findOne(
                Mockito.any(Query.class), Mockito.eq(Trace.class), Mockito.anyString()))
        .thenReturn(getTrace(TraceStatus.ErrorDeserializing));
    Trace trace = traceService.getTrace(id, TRACES);
    trace.setErrorMessages(errorMessages);
    trace.setLineNumber(lineNumber);

    // Assertions
    Assertions.assertNotNull(trace);
    Assertions.assertEquals(id, trace.getId());
    Assertions.assertEquals(TraceStatus.ErrorDeserializing.name(), trace.getStatus().name());
    Assertions.assertEquals(errorMessages.get(0), trace.getErrorMessages().get(0));
    Assertions.assertEquals(errorMessages.get(1), trace.getErrorMessages().get(1));
    Assertions.assertEquals(lineNumber, trace.getLineNumber());
  }

  private Trace getTrace(TraceStatus status) {
    Trace trace = new Trace();

    trace.setId("UUID");
    trace.setOriginId("origin");
    trace.setMessage("service");
    trace.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    trace.setStatus(status);

    return trace;
  }
}
