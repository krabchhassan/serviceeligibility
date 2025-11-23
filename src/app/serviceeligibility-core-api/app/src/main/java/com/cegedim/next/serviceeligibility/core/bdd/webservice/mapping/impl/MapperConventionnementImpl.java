package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperConventionnement;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Conventionnement}. */
@Component
public class MapperConventionnementImpl
    extends GenericMapperImpl<Conventionnement, ConventionnementDto>
    implements MapperConventionnement {

  @Autowired MapperTypeConventionnementImpl mapperTypeConventionnement;

  @Override
  public Conventionnement dtoToEntity(ConventionnementDto conventionnementDto) {
    Conventionnement conventionnement = null;
    if (conventionnementDto != null) {
      conventionnement = new Conventionnement();
      conventionnement.setPriorite(conventionnementDto.getPriorite());
      conventionnement.setTypeConventionnement(
          mapperTypeConventionnement.dtoToEntity(conventionnementDto.getTypeConventionnement()));
    }
    return conventionnement;
  }

  @Override
  public ConventionnementDto entityToDto(
      Conventionnement conventionnement,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    ConventionnementDto conventionnementDto = null;
    if (conventionnement != null) {
      conventionnementDto = new ConventionnementDto();
      conventionnementDto.setPriorite(conventionnement.getPriorite());
      conventionnementDto.setTypeConventionnement(
          mapperTypeConventionnement.entityToDto(
              conventionnement.getTypeConventionnement(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
    }
    return conventionnementDto;
  }
}
