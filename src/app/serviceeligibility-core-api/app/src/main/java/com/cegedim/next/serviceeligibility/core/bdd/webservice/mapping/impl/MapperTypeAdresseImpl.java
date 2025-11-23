package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeAdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperTypeAdresse;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeAdresse;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code TypeAdresse}. */
@Component
public class MapperTypeAdresseImpl extends GenericMapperImpl<TypeAdresse, TypeAdresseDto>
    implements MapperTypeAdresse {

  @Override
  public TypeAdresse dtoToEntity(TypeAdresseDto typeAdresseDto) {
    TypeAdresse typeAdresse = null;
    if (typeAdresseDto != null) {
      typeAdresse = new TypeAdresse();
      typeAdresse.setLibelle(typeAdresseDto.getLibelle());
      typeAdresse.setType(typeAdresseDto.getType());
    }
    return typeAdresse;
  }

  @Override
  public TypeAdresseDto entityToDto(
      TypeAdresse typeAdresse,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    TypeAdresseDto typeAdresseDto = null;
    if (typeAdresse != null) {
      typeAdresseDto = new TypeAdresseDto();
      typeAdresseDto.setLibelle(typeAdresse.getLibelle());
      typeAdresseDto.setType(typeAdresse.getType());
    }
    return typeAdresseDto;
  }
}
