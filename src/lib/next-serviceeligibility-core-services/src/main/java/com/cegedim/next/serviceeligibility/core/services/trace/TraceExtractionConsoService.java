package com.cegedim.next.serviceeligibility.core.services.trace;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.arldata.ARLDataDto;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.job.utils.Constants;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraceExtractionConsoService {
  private final TraceExtractionConsoDao traceExtractionConsoDao;

  @ContinueSpan(log = "Generate traces for extraction card conso")
  public List<TraceExtractionConso> generateExtractionTrace(
      CarteDemat carteDemat, String codeRejet, List<DeclarationConsolide> declarationConsolides) {
    List<TraceExtractionConso> traces = new ArrayList<>();

    // We create a trace only for TP cards
    boolean createTrace =
        (carteDemat != null)
            && CollectionUtils.isNotEmpty(carteDemat.getCodeServices())
            && carteDemat.getCodeServices().contains(CARTE_TP);

    if (createTrace) {
      for (String idDeclarationsConsolides : carteDemat.getIdDeclarationsConsolides()) {
        TraceExtractionConso trace = new TraceExtractionConso();
        if (CollectionUtils.isNotEmpty(declarationConsolides)) {
          declarationConsolides.stream()
              .filter(
                  declarationConsolide ->
                      idDeclarationsConsolides.equals(declarationConsolide.get_id()))
              .findFirst()
              .ifPresent(conso -> trace.setIdDeclaration(conso.getIdDeclarations()));
        }
        trace.setIdDeclarationConsolidee(idDeclarationsConsolides);
        trace.setIdDeclarant(carteDemat.getIdDeclarant());
        trace.setDateExecution(carteDemat.getDateCreation());
        trace.setCodeService(CARTE_TP);
        trace.setBatch(NUMERO_BATCH_620);
        trace.setCodeClient(carteDemat.getCodeClient());
        trace.setCollectionConsolidee(Constants.COLLECTION_CONSOLIDATION_CARTES);
        trace.setNumAMCEchange(carteDemat.getContrat().getNumAMCEchange());

        // In Talend, numeroFichier is a bit mystic, and probably useless since nobody
        // knows what purpose it serves
        // It's read from declarant first, then incremented at each card wrote, which
        // also increments in histo exec
        trace.setNumeroFichier(null);

        // In Talend, idLigne is read from a field named "numBenef", which is a String
        // formatted from 8 digits
        // I didn't find where this context variable was read from
        // On C3 database, no tracesExtractionConso element have a non-null idLigne
        trace.setIdLigne(null);

        // If the consolidation went well, codeRejet and nomFichierARL are null
        trace.setCodeRejet(codeRejet);
        trace.setNomFichierARL(null);

        traces.add(trace);
      }
    }

    return traces;
  }

  @ContinueSpan(log = "Generate traces for extraction almerys conso")
  public List<TraceExtractionConso> generateExtractionTraceAlmerys(
      TmpObject2 tmpObject2, Pilotage pilotage) {
    List<TraceExtractionConso> traces = new ArrayList<>();
    TraceExtractionConso trace = new TraceExtractionConso();
    trace.setIdDeclarationConsolidee(tmpObject2.get_id());
    trace.setIdDeclarant(tmpObject2.getIdDeclarant());
    trace.setDateExecution(new Date());
    trace.setCodeService(pilotage.getCodeService());
    trace.setBatch(NUMERO_BATCH_608);
    trace.setCodeClient(pilotage.getCaracteristique().getNumClient());
    trace.setCollectionConsolidee(Constants.COLLECTION_CONSOLIDATION_ALMERYS);

    trace.setNumeroPersonne(tmpObject2.getBeneficiaire().getNumeroPersonne());

    trace.setNumeroFichier(1);

    // In Talend, idLigne is read from a field named "numBenef", which is a String
    // formatted from 8 digits
    // I didn't find where this context variable was read from
    // On C3 database, no tracesExtractionConso element have a non-null idLigne
    trace.setIdLigne(null);

    // If the consolidation went well, codeRejet and nomFichierARL are null
    trace.setCodeRejet(null);
    trace.setNomFichierARL(null);
    trace.setNomFichier(null);

    traces.add(trace);

    return traces;
  }

  @ContinueSpan(log = "Generate traces for rejected card consolidation")
  public List<TraceExtractionConso> completeRejectedTrace(
      @NotNull ARLDataDto arlData, String nomFichierARL) {
    // It makes no sense to generate a reject trace if the declaration is valid
    if (arlData.getValeurRejet().isEmpty()) {
      log.error(
          "Erreur lors de la création de la trace de rejet : la déclaration {} est valide",
          arlData.getNomFichierNumDeclaration());
      return Collections.emptyList();
    }

    List<TraceExtractionConso> traces =
        traceExtractionConsoDao.getByServiceAndIdDeclaration(
            arlData.getNomFichierNumDeclaration(), arlData.getCodeService());
    // We add the file to the existing trace
    if (CollectionUtils.isNotEmpty(traces)) {
      traces.forEach(trace -> trace.setNomFichierARL(nomFichierARL));
      return traces;
    }
    return Collections.emptyList();
  }

  @ContinueSpan(log = "Save only new traces for extraction card conso")
  public void saveTraceListWithBulk(List<TraceExtractionConso> traces, ClientSession session) {
    if (CollectionUtils.isNotEmpty(traces)) {
      traceExtractionConsoDao.saveAll(traces, session);
    }
  }

  @ContinueSpan(log = "Save list of traces for extraction card conso")
  public void saveTraceList(List<TraceExtractionConso> traces, ClientSession session) {
    if (CollectionUtils.isNotEmpty(traces)) {
      for (TraceExtractionConso traceExtractionConso : traces) {
        traceExtractionConsoDao.save(traceExtractionConso, session);
      }
    }
  }

  @ContinueSpan(log = "Save trace for extraction card conso")
  public TraceExtractionConso save(TraceExtractionConso trace) {
    // Save the trace and store it to get its id
    TraceExtractionConso savedTrace = traceExtractionConsoDao.save(trace, null);
    log.debug("Trace crée avec l'id : {}", savedTrace.get_id());

    return savedTrace;
  }

  @ContinueSpan(log = "Get all traces for extraction card conso")
  public List<TraceExtractionConso> getAll() {
    return traceExtractionConsoDao.getAll();
  }

  @ContinueSpan(log = "Delete traces for extraction card conso")
  public void deleteAll() {
    traceExtractionConsoDao.deleteAll();
  }

  @ContinueSpan(log = "create TraceExtractionConso")
  public void create(TraceExtractionConso traceExtractionConso) {
    traceExtractionConsoDao.create(traceExtractionConso);
  }

  @ContinueSpan(log = "findById TraceExtractionConso")
  public TraceExtractionConso findById(String id) {
    return traceExtractionConsoDao.findById(id, TraceExtractionConso.class);
  }

  @ContinueSpan(log = "replaceFileName TraceExtractionConso")
  public long replaceFileName(List<String> oldNames, String newName) {
    return traceExtractionConsoDao.replaceFileName(oldNames, newName);
  }
}
