package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TraceRetourDto;
import com.cegedim.next.serviceeligibility.core.model.domain.TraceRetour;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MapperTraceRetour {

  public TraceRetourDto entityToDto(TraceRetour traceRetour) {
    SimpleDateFormat sdfEffetDebut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    TraceRetourDto traceRetourDto = null;
    if (traceRetour != null) {
      traceRetourDto = new TraceRetourDto();
      StringBuilder rejet = new StringBuilder();
      if (StringUtils.isNotBlank(traceRetour.getCodeRejet())) {
        rejet.append(traceRetour.getCodeRejet());
      }
      if (StringUtils.isNotBlank(traceRetour.getLibelleRejet())) {
        if (StringUtils.isNotBlank(rejet.toString())) {
          rejet.append(" - ").append(traceRetour.getLibelleRejet());
        } else {
          rejet.append(traceRetour.getLibelleRejet());
        }
      }
      if (StringUtils.isNotBlank(traceRetour.getValeurRejet())) {
        if (StringUtils.isNotBlank(rejet.toString())) {
          rejet.append(" : ").append(traceRetour.getValeurRejet());
        } else {
          rejet.append(traceRetour.getValeurRejet());
        }
      }
      traceRetourDto.setRejet(rejet.toString());
      traceRetourDto.setNomFichier(traceRetour.getNomFichier());
      traceRetourDto.setDateExecution(sdfEffetDebut.format(traceRetour.getDateExecution()));
      traceRetourDto.setDateExecutionDate(traceRetour.getDateExecution());
    }
    return traceRetourDto;
  }

  public List<TraceRetourDto> entityListToDtoList(final List<TraceRetour> list) {
    List<TraceRetourDto> dtoList = new ArrayList<>();
    for (TraceRetour domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
