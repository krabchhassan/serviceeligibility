package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationTrace;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceConsolidationService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestTraceService {

  private final TraceConsolidationService traceConsolidationService;

  private final TraceExtractionConsoService traceExtractionConsoService;

  private final MongoTemplate mongoTemplate;

  public List<TraceConsolidation> getAllTracesConso() {
    return traceConsolidationService.getAll();
  }

  public void deleteTracesConso() {
    traceConsolidationService.deleteAll();
  }

  public TraceConsolidation createTraceConsolidation(TraceConsolidation traceConsolidation) {
    traceConsolidationService.create(traceConsolidation);
    return traceConsolidationService.findById(traceConsolidation.get_id());
  }

  public List<TraceExtractionConso> getAllTracesExtractionConso() {
    return traceExtractionConsoService.getAll();
  }

  public void deleteTracesExtractionConso() {
    traceExtractionConsoService.deleteAll();
  }

  public TraceExtractionConso createTraceExtractionConso(
      TraceExtractionConso traceExtractionConso) {
    traceExtractionConsoService.create(traceExtractionConso);
    return traceExtractionConsoService.findById(traceExtractionConso.get_id());
  }

  public List<ServicePrestationTrace> getAllServicePrestationTraces() {
    return mongoTemplate.findAll(ServicePrestationTrace.class);
  }
}
