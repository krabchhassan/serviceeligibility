package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.DomaineConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;

/** Interface du mapper de l'entite {@code DomaineConvention}. */
public interface MapperDomaineConvention
    extends GenericMapper<DomaineConvention, DomaineConventionDto> {

  @Override
  DomaineConvention dtoToEntity(DomaineConventionDto domaineDto);

  @Override
  DomaineConventionDto entityToDto(
      DomaineConvention domaine,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
