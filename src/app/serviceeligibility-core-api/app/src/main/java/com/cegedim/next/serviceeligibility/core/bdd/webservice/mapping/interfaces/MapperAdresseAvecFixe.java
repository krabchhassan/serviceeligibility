package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.AdresseAvecFixe;

/** Interface du mapper de l'entite {@code Adresse}. */
public interface MapperAdresseAvecFixe extends GenericMapper<AdresseAvecFixe, AdresseDto> {

  @Override
  AdresseAvecFixe dtoToEntity(AdresseDto adresseDto);

  @Override
  AdresseDto entityToDto(
      AdresseAvecFixe adresse,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
