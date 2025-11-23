package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametreDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperParametre;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Parametre;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Parametre}. */
@Component
public class MapperParametreImpl extends GenericMapperImpl<Parametre, ParametreDto>
    implements MapperParametre {

  @Override
  public Parametre dtoToEntity(ParametreDto parametreDto) {
    Parametre parametre = null;
    if (parametreDto != null) {
      parametre = new Parametre();
      parametre.setLibelle(parametreDto.getLibelle());
      parametre.setNumero(parametreDto.getNumero());
      parametre.setValeur(parametreDto.getValeur());
    }
    return parametre;
  }

  @Override
  public ParametreDto entityToDto(
      Parametre parametre,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    ParametreDto parametreDto = null;
    if (parametre != null) {
      parametreDto = new ParametreDto();
      parametreDto.setLibelle(parametre.getLibelle());
      parametreDto.setNumero(parametre.getNumero());
      parametreDto.setValeur(parametre.getValeur());
    }
    return parametreDto;
  }
}
