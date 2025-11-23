package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.ConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;

/** Interface du mapper de l'entite {@code Convention}. */
public interface MapperConvention extends GenericMapper<Convention, ConventionDto> {

  @Override
  Convention dtoToEntity(ConventionDto conventionDto);

  @Override
  ConventionDto entityToDto(
      Convention convention,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
