package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperFormule;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Formule}. */
@Component
public class MapperFormuleImpl extends GenericMapperImpl<Formule, FormuleDto>
    implements MapperFormule {

  @Autowired MapperParametreImpl mapperParametre;

  @Override
  public Formule dtoToEntity(FormuleDto formuleDto) {
    Formule formule = null;
    if (formuleDto != null) {
      formule = new Formule();
      formule.setLibelle(formuleDto.getLibelle());
      formule.setNumero(formuleDto.getNumero());
      formule.setParametres(mapperParametre.dtoListToEntityList(formuleDto.getParametres()));
    }
    return formule;
  }

  @Override
  public FormuleDto entityToDto(
      Formule formule,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    FormuleDto formuleDto = null;
    if (formule != null) {
      formuleDto = new FormuleDto();
      formuleDto.setLibelle(formule.getLibelle());
      formuleDto.setNumero(formule.getNumero());
      formuleDto.setParametres(
          mapperParametre.entityListToDtoList(
              formule.getParametres(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
    }
    return formuleDto;
  }
}
