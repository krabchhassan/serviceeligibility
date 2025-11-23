package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperPrioriteDroit;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code PrioriteDroit}. */
@Component
public class MapperPrioriteDroitImpl extends GenericMapperImpl<PrioriteDroit, PrioriteDroitDto>
    implements MapperPrioriteDroit {

  @Override
  public PrioriteDroit dtoToEntity(PrioriteDroitDto prioriteDroitDto) {
    PrioriteDroit prioriteDroit = null;
    if (prioriteDroitDto != null) {
      prioriteDroit = new PrioriteDroit();
      prioriteDroit.setCode(prioriteDroitDto.getCode());
      prioriteDroit.setLibelle(prioriteDroitDto.getLibelle());
      prioriteDroit.setTypeDroit(prioriteDroitDto.getTypeDroit());
      prioriteDroit.setPrioriteBO(prioriteDroitDto.getPrioriteBO());
      prioriteDroit.setNirPrio1(prioriteDroitDto.getNirPrio1());
      prioriteDroit.setNirPrio2(prioriteDroitDto.getNirPrio2());
      prioriteDroit.setPrioDroitNir1(prioriteDroitDto.getPrioDroitNir1());
      prioriteDroit.setPrioDroitNir2(prioriteDroitDto.getPrioDroitNir2());
      prioriteDroit.setPrioContratNir1(prioriteDroitDto.getPrioContratNir1());
      prioriteDroit.setPrioContratNir2(prioriteDroitDto.getPrioContratNir2());
    }
    return prioriteDroit;
  }

  @Override
  public PrioriteDroitDto entityToDto(
      PrioriteDroit prioriteDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    PrioriteDroitDto prioriteDroitDto = null;
    if (prioriteDroit != null) {
      prioriteDroitDto = new PrioriteDroitDto();
      prioriteDroitDto.setCode(prioriteDroit.getCode());
      prioriteDroitDto.setLibelle(prioriteDroit.getLibelle());
      prioriteDroitDto.setTypeDroit(prioriteDroit.getTypeDroit());
      prioriteDroitDto.setPrioriteBO(prioriteDroit.getPrioriteBO());
      prioriteDroitDto.setNirPrio1(prioriteDroit.getNirPrio1());
      prioriteDroitDto.setNirPrio2(prioriteDroit.getNirPrio2());
      prioriteDroitDto.setPrioDroitNir1(prioriteDroit.getPrioDroitNir1());
      prioriteDroitDto.setPrioDroitNir2(prioriteDroit.getPrioDroitNir2());
      prioriteDroitDto.setPrioContratNir1(prioriteDroit.getPrioContratNir1());
      prioriteDroitDto.setPrioContratNir2(prioriteDroit.getPrioContratNir2());
    }
    return prioriteDroitDto;
  }
}
