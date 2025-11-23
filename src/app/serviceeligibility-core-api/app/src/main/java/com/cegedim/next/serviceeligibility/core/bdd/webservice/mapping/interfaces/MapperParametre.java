package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametreDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Parametre;

/** Interface du mapper de l'entite {@code Parametre}. */
public interface MapperParametre extends GenericMapper<Parametre, ParametreDto> {

  @Override
  Parametre dtoToEntity(ParametreDto parametreDto);

  @Override
  ParametreDto entityToDto(
      Parametre parametre,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
