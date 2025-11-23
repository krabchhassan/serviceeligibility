package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;

/** Interface du mapper de l'entite {@code Declarant}. */
public interface MapperDeclarant extends GenericMapper<Declarant, DeclarantDto> {

  @Override
  Declarant dtoToEntity(DeclarantDto declarantDto);

  @Override
  DeclarantDto entityToDto(
      Declarant declarant,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
