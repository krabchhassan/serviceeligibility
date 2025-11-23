package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;

/** Interface du mapper de l'entite {@code HistoriquePeriodeDroit}. */
public interface MapperPeriodeDroit extends GenericMapper<PeriodeDroit, PeriodeDroitDto> {

  @Override
  PeriodeDroit dtoToEntity(PeriodeDroitDto periodeDroitdto);

  @Override
  PeriodeDroitDto entityToDto(
      PeriodeDroit periodeDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
