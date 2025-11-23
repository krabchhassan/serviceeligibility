package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceExtractionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceRetourDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.model.domain.TraceExtraction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperTraceExtraction {

  @Autowired private ParametreBddService parametreBddService;

  @Autowired private MapperTraceRetour mapperTraceRetour;

  public TraceExtractionDto entityToDto(TraceExtraction traceExtraction) {
    SimpleDateFormat sdfEffetDebut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    TraceExtractionDto traceExtractionDto = new TraceExtractionDto();
    if (traceExtraction != null) {
      Map<String, ErreursDto> erreurs = parametreBddService.findRejets();
      traceExtractionDto.setIsTraceValide(true);
      traceExtractionDto.setCodeService(traceExtraction.getCodeService());
      traceExtractionDto.setClient(traceExtraction.getCodeClient());
      traceExtractionDto.setErreur(erreurs.get(traceExtraction.getCodeRejet()));
      traceExtractionDto.setDateExecution(sdfEffetDebut.format(traceExtraction.getDateExecution()));
      traceExtractionDto.setNomFichierARL(traceExtraction.getNomFichierARL());
      traceExtractionDto.setNomFichier(traceExtraction.getNomFichier());
      traceExtractionDto.setNumeroFichier(traceExtraction.getNumeroFichier());
      traceExtractionDto.setDateExecutionDate(traceExtraction.getDateExecution());
      if (traceExtraction.getListeRetours() != null) {
        List<TraceRetourDto> retours =
            mapperTraceRetour.entityListToDtoList(traceExtraction.getListeRetours());
        Comparator<TraceRetourDto> comparator =
            Comparator.comparing(TraceRetourDto::getDateExecutionDate);
        Stream<TraceRetourDto> dtoStream = retours.stream().sorted(comparator.reversed());
        traceExtractionDto.setRetours(dtoStream.toList());
      }
      traceExtractionDto.setIsTraceValide(traceExtractionDto.getErreur() == null);
    }
    return traceExtractionDto;
  }

  public List<TraceExtractionDto> entityListToDtoList(final List<TraceExtraction> list) {
    List<TraceExtractionDto> dtoList = new ArrayList<>();
    for (TraceExtraction domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
