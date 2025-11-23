package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.TracePriorisationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.model.domain.TracePriorisation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperTracePriorisation {

  @Autowired private ParametreBddService parametreBddService;

  public TracePriorisationDto entityToDto(TracePriorisation tracePriorisation) {
    SimpleDateFormat sdfEffetDebut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    TracePriorisationDto tracePriorisationDto = new TracePriorisationDto();
    if (tracePriorisation != null) {
      Map<String, ErreursDto> erreurs = parametreBddService.findRejets();
      tracePriorisationDto.setIsTraceValide(true);
      tracePriorisationDto.setCodeService(tracePriorisation.getCodeService());
      tracePriorisationDto.setErreur(erreurs.get(tracePriorisation.getCodeRejet()));
      tracePriorisationDto.setNomFichierARL(tracePriorisation.getNomFichierARL());
      tracePriorisationDto.setNbPrioOrigineDifferent(tracePriorisation.getNbPrioOrigineDifferent());
      tracePriorisationDto.setDateExecution(
          sdfEffetDebut.format(tracePriorisation.getDateExecution()));
      tracePriorisationDto.setDateExecutionDate(tracePriorisation.getDateExecution());
      tracePriorisationDto.setIsTraceValide(tracePriorisationDto.getErreur() == null);
    }
    return tracePriorisationDto;
  }

  public List<TracePriorisationDto> entityListToDtoList(final List<TracePriorisation> list) {
    List<TracePriorisationDto> dtoList = new ArrayList<>();
    for (TracePriorisation domain : list) {
      dtoList.add(entityToDto(domain));
    }
    return dtoList;
  }
}
