package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperTypeConventionnement;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code TypeConventionnement}. */
@Component
public class MapperTypeConventionnementImpl
    extends GenericMapperImpl<TypeConventionnement, TypeConventionnementDto>
    implements MapperTypeConventionnement {

  @Override
  public TypeConventionnement dtoToEntity(TypeConventionnementDto typeConventionnementDto) {
    TypeConventionnement typeConventionnement = null;
    if (typeConventionnementDto != null) {
      typeConventionnement = new TypeConventionnement();
      typeConventionnement.setCode(typeConventionnementDto.getCode());
      typeConventionnement.setLibelle(typeConventionnementDto.getLibelle());
    }
    return typeConventionnement;
  }

  @Override
  public TypeConventionnementDto entityToDto(
      TypeConventionnement typeConventionnement,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    TypeConventionnementDto typeConventionnementDto = null;
    if (typeConventionnement != null) {
      typeConventionnementDto = new TypeConventionnementDto();
      typeConventionnementDto.setCode(typeConventionnement.getCode());
      typeConventionnementDto.setLibelle(typeConventionnement.getLibelle());
    }
    return typeConventionnementDto;
  }
}
