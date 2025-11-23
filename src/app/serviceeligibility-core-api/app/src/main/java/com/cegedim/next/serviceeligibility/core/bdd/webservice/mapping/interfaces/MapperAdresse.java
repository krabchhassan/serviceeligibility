package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;

/** Interface du mapper de l'entite {@code Adresse}. */
public interface MapperAdresse extends GenericMapper<Adresse, AdresseDto> {

  @Override
  Adresse dtoToEntity(AdresseDto adresseDto);

  @Override
  AdresseDto entityToDto(
      Adresse adresse,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
