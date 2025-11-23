package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;

/** Interface du mapper de l'entite {@code HistoriqueHaffiliation}. */
public interface MapperAffiliation extends GenericMapper<Affiliation, AffiliationDto> {

  @Override
  Affiliation dtoToEntity(AffiliationDto affiliationDto);

  @Override
  AffiliationDto entityToDto(
      Affiliation affiliation,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
