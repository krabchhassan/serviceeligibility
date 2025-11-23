package com.cegedim.next.serviceeligibility.core.services.trace;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.NUMERO_BATCH_620;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.arldata.ARLDataDto;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceConsolidationDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.job.utils.Constants;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationsConsolideesAlmerys;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraceConsolidationService {
  private final TraceConsolidationDao traceConsolidationDao;

  @ContinueSpan(log = "Generate traces for valid card consolidation")
  public List<TraceConsolidation> generateValidTraces(
      DeclarationConsolide declarationConsolide, String codeClient) {
    List<TraceConsolidation> traces = new ArrayList<>();

    // We create a trace for each asked service
    if (declarationConsolide != null
        && CollectionUtils.isNotEmpty(declarationConsolide.getCodeServices())) {
      for (String codeService : declarationConsolide.getCodeServices()) {
        TraceConsolidation trace = new TraceConsolidation();

        trace.setIdDeclaration(declarationConsolide.getIdDeclarations());
        trace.setIdDeclarationConsolidee(declarationConsolide.get_id());
        trace.setIdDeclarant(declarationConsolide.getIdDeclarant());
        trace.setDateExecution(declarationConsolide.getDateConsolidation());
        trace.setCodeService(codeService);
        trace.setBatch(NUMERO_BATCH_620);
        trace.setCodeClient(codeClient);
        trace.setCollectionConsolidee(Constants.COLLECTION_CONSOLIDATION_CARTES);

        // If the consolidation went well, codeRejet and nomFichierARL are null
        trace.setCodeRejet(null);
        trace.setNomFichierARL(null);

        traces.add(trace);
      }
    }

    return traces;
  }

  @ContinueSpan(log = "Generate traces for almerys declaration consolidation")
  public List<TraceConsolidation> generateValidTraces(
      DeclarationsConsolideesAlmerys declarationConsolide, String codeClient, String batchNumber) {
    List<TraceConsolidation> almerysTraces = new ArrayList<>();

    // We create a trace for each asked service
    if (declarationConsolide != null) {
      TraceConsolidation almerysTrace = new TraceConsolidation();
      almerysTrace.setIdDeclaration(declarationConsolide.getIdDeclarations().get(0));
      almerysTrace.setIdDeclarationConsolidee(
          Collections.singletonList(declarationConsolide.get_id()));
      almerysTrace.setIdDeclarant(declarationConsolide.getIdDeclarant());
      almerysTrace.setDateExecution(declarationConsolide.getDateConsolidation());
      almerysTrace.setCodeService(declarationConsolide.getCodeService());
      almerysTrace.setBatch(batchNumber);
      almerysTrace.setCodeClient(codeClient);
      almerysTrace.setCollectionConsolidee(Constants.COLLECTION_CONSOLIDATION_ALMERYS);

      // If the consolidation went well, codeRejet and nomFichierARL are null
      almerysTrace.setCodeRejet(null);
      almerysTrace.setNomFichierARL(null);

      almerysTraces.add(almerysTrace);
    }

    return almerysTraces;
  }

  @ContinueSpan(
      log = "Generate traces for declaration that couldn't be consolidated beacause rejected")
  public List<TraceConsolidation> generateTraceRejet(
      Declaration declaration, String codeClient, String batchNumber, List<String> codeRejets) {
    List<TraceConsolidation> almerysTraces = new ArrayList<>();
    for (String codeRejet : codeRejets) {
      if (declaration != null) {
        TraceConsolidation almerysTrace = new TraceConsolidation();
        almerysTrace.setIdDeclaration(declaration.get_id());
        almerysTrace.setIdDeclarationConsolidee(
            Collections.singletonList(UUID.randomUUID().toString()));
        almerysTrace.setIdDeclarant(declaration.getIdDeclarant());
        almerysTrace.setDateExecution(new Date());
        almerysTrace.setCodeService(com.cegedim.next.serviceeligibility.core.utils.Constants.ALMV3);
        almerysTrace.setBatch(batchNumber);
        almerysTrace.setCodeClient(codeClient);
        almerysTrace.setCollectionConsolidee(Constants.COLLECTION_CONSOLIDATION_ALMERYS);

        almerysTrace.setCodeRejet(codeRejet);
        almerysTrace.setNomFichierARL(null);

        almerysTraces.add(almerysTrace);
      }
    }
    return almerysTraces;
  }

  @ContinueSpan(log = "Generate traces for rejected card consolidation")
  public TraceConsolidation generateRejectedTrace(
      @NotNull ARLDataDto arlData, String nomFichierARL, String codeClient, String batchName) {
    // It makes no sense to generate a reject trace if the declaration is valid
    if (arlData.getValeurRejet().isEmpty()) {
      log.error(
          "Erreur lors de la création de la trace de rejet : la déclaration {} est valide",
          arlData.getNomFichierNumDeclaration());
      return null;
    }
    TraceConsolidation trace =
        traceConsolidationDao.getByServiceAndIdDeclaration(
            arlData.getNomFichierNumDeclaration(), arlData.getCodeService());
    // We create a trace for this ARL
    if (trace == null) {
      trace = new TraceConsolidation();
      // If the consolidation went bad, we don't have any declarationConsolidee
      trace.setCollectionConsolidee(null);
      trace.setIdDeclarationConsolidee(null);
      trace.setIdDeclaration(arlData.getNomFichierNumDeclaration());
      trace.setIdDeclarant(arlData.getAmcNumber());
      trace.setDateExecution(
          DateUtils.parseDate(arlData.getDateTraitement(), DateUtils.YYYY_MM_DD_HH_MM_SS));
      trace.setCodeService(arlData.getCodeService());
      trace.setBatch(batchName);
      trace.setCodeClient(codeClient);
      trace.setNomFichierARL(nomFichierARL);
    }
    trace.setCodeRejet(arlData.getRejet());
    return trace;
  }

  @ContinueSpan(log = "Save list of traces for card consolidation")
  public void saveTraceList(List<TraceConsolidation> traces) {
    if (CollectionUtils.isNotEmpty(traces)) {
      traceConsolidationDao.saveAll(traces);
    }
  }

  @ContinueSpan(log = "Save list of traces for card consolidation")
  public void insertTraceList(List<TraceConsolidation> traces) {
    if (CollectionUtils.isNotEmpty(traces)) {
      traceConsolidationDao.insertAll(traces);
    }
  }

  @ContinueSpan(log = "Get all traces for card consolidation")
  public List<TraceConsolidation> getAll() {
    return traceConsolidationDao.getAll();
  }

  @ContinueSpan(log = "Delete traces for card consolidation")
  public void deleteAll() {
    traceConsolidationDao.deleteAll();
  }

  @ContinueSpan(log = "create card consolidation")
  public void create(TraceConsolidation traceExtractionConso) {
    traceConsolidationDao.create(traceExtractionConso);
  }

  @ContinueSpan(log = "findById card consolidation")
  public TraceConsolidation findById(String id) {
    return traceConsolidationDao.findById(id, TraceConsolidation.class);
  }
}
