package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;

/** Interface du mapper de l'entite {@code Declaration}. */
public interface MapperDeclaration extends GenericMapper<Declaration, DeclarationDto> {

  @Override
  Declaration dtoToEntity(DeclarationDto declarationDto);

  @Override
  DeclarationDto entityToDto(
      Declaration declaration,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
