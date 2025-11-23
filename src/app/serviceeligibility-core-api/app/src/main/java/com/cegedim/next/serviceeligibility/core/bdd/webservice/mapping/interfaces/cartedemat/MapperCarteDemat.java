package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;

/** Interface du mapper de l'entite {@code CarteDemat}. */
public interface MapperCarteDemat extends GenericMapper<CarteDemat, CarteDematDto> {

  @Override
  CarteDemat dtoToEntity(CarteDematDto carteDto);

  @Override
  CarteDematDto entityToDto(
      CarteDemat carte,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
