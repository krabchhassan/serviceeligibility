package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TracesDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.DeclarantBackendDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.PilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceConsolidationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceExtractionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TracePriorisationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceServiceDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Traces;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperTrace {

  @Autowired private MapperTraceConsolidation mapperTraceConsolidation;

  @Autowired private MapperTraceExtraction mapperTraceExtraction;

  @Autowired private MapperTracePriorisation mapperTracePriorisation;

  @Autowired private DeclarantBackendService declarantBackendService;

  @Autowired private TracesDao tracesDao;

  public TraceDto entityToDto(Declaration declaration) {
    Traces traces = tracesDao.findByIdDeclaration(declaration.get_id());
    SimpleDateFormat sdfEffetDebut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    TraceDto traceDto = new TraceDto();

    boolean isAllServiceOk = true;
    DeclarantBackendDto declarant =
        declarantBackendService.findDtoById(declaration.getIdDeclarant());
    // Donnee trace
    traceDto.setNomFichier(declaration.getNomFichierOrigine());
    traceDto.setDateTraitement(sdfEffetDebut.format(declaration.getEffetDebut()));
    traceDto.setCodeEtat(declaration.getCodeEtat());
    traceDto.setVersionTDB(declaration.getVersionDeclaration());

    // Donnee AMC
    traceDto.setOperateurPrincipal(declarant.getOperateurPrincipal());
    traceDto.setCodeCircuit(declarant.getCodeCircuit());
    traceDto.setCodePartenaire(declarant.getCodePartenaire());

    // Découpage des traces par service
    traceDto.setTracesServices(getTracesByService(traces, declarant));
    for (TraceServiceDto serv : traceDto.getTracesServices()) {
      isAllServiceOk = isAllServiceOk && serv.getLastExecutionOk();
    }
    traceDto.setAllServiceValide(isAllServiceOk);
    List<DomaineDroit> domaines = declaration.getDomaineDroits();
    if (domaines != null && !domaines.isEmpty()) {
      DomaineDroit domaine = domaines.get(0);
      traceDto.setBackOfficeEmetteur(domaine.getOrigineDroits());
      traceDto.setMotifDeclaration(
          domaine.getPeriodeDroit().getMotifEvenement()
              + " - "
              + domaine.getPeriodeDroit().getLibelleEvenement());
      traceDto.setTypeMouvement(domaine.getPeriodeDroit().getModeObtention());
    }
    return traceDto;
  }

  private List<TraceServiceDto> getTracesByService(Traces trace, DeclarantBackendDto declarant) {
    List<TraceServiceDto> listServiceTrace = new ArrayList<>();
    List<TraceConsolidationDto> tracesConsos = new ArrayList<>();
    List<TraceExtractionDto> tracesExtractions = new ArrayList<>();
    List<TracePriorisationDto> tracesPrio = new ArrayList<>();

    // Convert traces to sublist
    if (trace != null) {
      if (trace.getListeConsolidations() != null) {
        tracesConsos = mapperTraceConsolidation.entityListToDtoList(trace.getListeConsolidations());
      }
      if (trace.getListeExtractions() != null) {
        tracesExtractions = mapperTraceExtraction.entityListToDtoList(trace.getListeExtractions());
      }
      if (trace.getListePriorisation() != null) {
        tracesPrio = mapperTracePriorisation.entityListToDtoList(trace.getListePriorisation());
      }
    }

    // Feed sublist by service
    for (PilotageDto pil : declarant.getPilotages()) {
      getPilotage(listServiceTrace, tracesConsos, tracesExtractions, tracesPrio, pil);
    }
    return listServiceTrace;
  }

  private void getPilotage(
      List<TraceServiceDto> listServiceTrace,
      List<TraceConsolidationDto> tracesConsos,
      List<TraceExtractionDto> tracesExtractions,
      List<TracePriorisationDto> tracesPrio,
      PilotageDto pil) {
    if (Boolean.TRUE.equals(pil.getServiceOuvert())) {
      TraceServiceDto traceServiceDto = new TraceServiceDto();
      boolean lastExecOk = true;
      traceServiceDto.setCodeService(pil.getNom());
      traceServiceDto.setTypeService(pil.getTypeService());
      Comparator<TraceConsolidationDto> comparatorConso =
          Comparator.comparing(TraceConsolidationDto::getDateExecutionDate);
      Stream<TraceConsolidationDto> dtoStreamConso =
          tracesConsos.stream()
              .filter(dto -> pil.getNom().equals(dto.getCodeService()))
              .sorted(comparatorConso.reversed());
      traceServiceDto.setConsolidations(dtoStreamConso.toList());

      Comparator<TraceExtractionDto> comparatorExtract =
          Comparator.comparing(TraceExtractionDto::getDateExecutionDate);
      Stream<TraceExtractionDto> dtoStreamExtract =
          tracesExtractions.stream()
              .filter(dto -> pil.getNom().equals(dto.getCodeService()))
              .sorted(comparatorExtract.reversed());
      traceServiceDto.setExtractions(dtoStreamExtract.toList());

      Comparator<TracePriorisationDto> comparatorPrio =
          Comparator.comparing(TracePriorisationDto::getDateExecutionDate);
      Stream<TracePriorisationDto> dtoStreamPrio =
          tracesPrio.stream()
              .filter(dto -> pil.getNom().equals(dto.getCodeService()))
              .sorted(comparatorPrio.reversed());
      traceServiceDto.setTraitements(dtoStreamPrio.toList());

      if (traceServiceDto.getConsolidations().isEmpty()) {
        traceServiceDto.setConsolidations(null);
      } else {
        lastExecOk = traceServiceDto.getConsolidations().get(0).getIsTraceValide();
      }
      if (traceServiceDto.getExtractions().isEmpty()) {
        traceServiceDto.setExtractions(null);
      } else {
        lastExecOk = lastExecOk && traceServiceDto.getExtractions().get(0).getIsTraceValide();
      }
      if (traceServiceDto.getTraitements().isEmpty()) {
        traceServiceDto.setTraitements(null);
      } else {
        lastExecOk = lastExecOk && traceServiceDto.getTraitements().get(0).getIsTraceValide();
      }
      traceServiceDto.setLastExecutionOk(lastExecOk);
      listServiceTrace.add(traceServiceDto);
    }
  }
}
