package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;

/** Interface du mapper de l'entite {@code DomaineDroit}. */
public interface MapperDomaineDroit extends GenericMapper<DomaineDroit, DomaineDroitDto> {

  @Override
  DomaineDroit dtoToEntity(DomaineDroitDto domaineDroitDto);

  @Override
  DomaineDroitDto entityToDto(
      DomaineDroit domaineDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
