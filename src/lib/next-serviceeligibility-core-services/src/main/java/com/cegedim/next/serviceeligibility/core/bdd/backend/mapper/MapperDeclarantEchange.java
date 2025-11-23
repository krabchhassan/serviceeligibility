package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.DeclarantEchangeDto;
import com.cegedim.next.serviceeligibility.core.model.domain.DeclarantsEchange;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code DeclarantsEchange}. */
@Component
public class MapperDeclarantEchange {

  public DeclarantEchangeDto entityToDto(DeclarantsEchange domain) {

    DeclarantEchangeDto declarantsEchangeDto = null;

    if (domain != null) {
      declarantsEchangeDto = new DeclarantEchangeDto();
      declarantsEchangeDto.setNumerosAMCEchanges(domain.getNumerosAMCEchanges());
    }

    return declarantsEchangeDto;
  }
}
