package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceConsolidationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceExtractionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.model.domain.TraceConsolidation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class MapperTraceConsolidation {

  @Autowired private ParametreBddService parametreBddService;

  @Autowired private MapperTraceExtraction mapperTraceExtraction;

  public TraceConsolidationDto entityToDto(TraceConsolidation traceConsolidation) {
    SimpleDateFormat sdfEffetDebut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    TraceConsolidationDto traceConsolidationDto = new TraceConsolidationDto();
    if (traceConsolidation != null) {
      Map<String, ErreursDto> erreurs = parametreBddService.findRejets();
      traceConsolidationDto.setIsTraceValide(true);
      traceConsolidationDto.setCodeService(traceConsolidation.getCodeService());
      traceConsolidationDto.setErreur(erreurs.get(traceConsolidation.getCodeRejet()));
      traceConsolidationDto.setDateExecution(
          sdfEffetDebut.format(traceConsolidation.getDateExecution()));
      traceConsolidationDto.setNomFichierARL(traceConsolidation.getNomFichierARL());
      traceConsolidationDto.setDateExecutionDate(traceConsolidation.getDateExecution());
      if (traceConsolidation.getListeExtractions() != null) {
        List<TraceExtractionDto> extractions =
            mapperTraceExtraction.entityListToDtoList(traceConsolidation.getListeExtractions());
        Comparator<TraceExtractionDto> comparator =
            Comparator.comparing(TraceExtractionDto::getDateExecutionDate);
        Stream<TraceExtractionDto> dtoStream = extractions.stream().sorted(comparator.reversed());
        traceConsolidationDto.setExtractions(dtoStream.toList());
      }
      traceConsolidationDto.setIsTraceValide(
          traceConsolidationDto.getErreur() == null
              && isExtractValide(traceConsolidationDto.getExtractions()));
    }
    return traceConsolidationDto;
  }

  private boolean isExtractValide(List<TraceExtractionDto> extracts) {
    return extracts == null || extracts.isEmpty() || extracts.get(0).getIsTraceValide();
  }

  public List<TraceConsolidationDto> entityListToDtoList(final List<TraceConsolidation> list) {
    List<TraceConsolidationDto> dtoList = new ArrayList<>();
    for (TraceConsolidation domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
