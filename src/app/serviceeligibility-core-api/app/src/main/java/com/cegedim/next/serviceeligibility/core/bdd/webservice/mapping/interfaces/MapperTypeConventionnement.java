package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.TypeConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.TypeConventionnement;

/** Interface du mapper de l'entite {@code TypeConventionnement}. */
public interface MapperTypeConventionnement
    extends GenericMapper<TypeConventionnement, TypeConventionnementDto> {

  @Override
  TypeConventionnement dtoToEntity(TypeConventionnementDto typeConventionnementDto);

  @Override
  TypeConventionnementDto entityToDto(
      TypeConventionnement typeConventionnement,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
