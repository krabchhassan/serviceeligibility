package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeAdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeAdresse;

/** Interface du mapper de l'entite {@code TypeAdresse}. */
public interface MapperTypeAdresse extends GenericMapper<TypeAdresse, TypeAdresseDto> {

  @Override
  TypeAdresse dtoToEntity(TypeAdresseDto typeAdresseDto);

  @Override
  TypeAdresseDto entityToDto(
      TypeAdresse typeAdresse,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
