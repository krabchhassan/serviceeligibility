package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;

/** Interface du mapper de l'entite {@code Prestation}. */
public interface MapperPrestation extends GenericMapper<Prestation, PrestationDto> {

  @Override
  Prestation dtoToEntity(PrestationDto prestationDto);

  @Override
  PrestationDto entityToDto(
      Prestation prestation,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
