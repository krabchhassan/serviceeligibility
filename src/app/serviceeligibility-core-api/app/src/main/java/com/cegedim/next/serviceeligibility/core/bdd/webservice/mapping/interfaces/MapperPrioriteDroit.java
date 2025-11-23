package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.PrioriteDroit;

/** Interface du mapper de l'entite {@code PeriodeDroit}. */
public interface MapperPrioriteDroit extends GenericMapper<PrioriteDroit, PrioriteDroitDto> {

  @Override
  PrioriteDroit dtoToEntity(PrioriteDroitDto prioriteDroitDto);

  @Override
  PrioriteDroitDto entityToDto(
      PrioriteDroit prioriteDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
