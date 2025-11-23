package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;

/** Interface du mapper de l'entite {@code Contrat}. */
public interface MapperContrat extends GenericMapper<Contrat, ContratDto> {

  @Override
  Contrat dtoToEntity(ContratDto contratDto);

  @Override
  ContratDto entityToDto(
      Contrat contrat,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
