package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.PrioritesDto;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import org.springframework.stereotype.Component;

@Component
public class MapperPriorites {

  public PrioritesDto entityToDto(PrioriteDroit prioriteDroit) {
    PrioritesDto prioritesDto = new PrioritesDto();
    if (prioriteDroit != null) {
      prioritesDto.setPrioriteBackOffice(prioriteDroit.getPrioriteBO());
      prioritesDto.setNumeroRO1(prioriteDroit.getNirPrio1());
      prioritesDto.setNumeroRO2(prioriteDroit.getNirPrio2());
      prioritesDto.setPrioContratRO1(prioriteDroit.getPrioContratNir1());
      prioritesDto.setPrioContratRO2(prioriteDroit.getPrioContratNir2());
      prioritesDto.setPrioDroitRO1(prioriteDroit.getPrioDroitNir1());
      prioritesDto.setPrioDroitRO2(prioriteDroit.getPrioDroitNir2());
    }
    return prioritesDto;
  }
}
