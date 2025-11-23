package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclarant;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Declarant}. */
@Component
public class MapperDeclarantImpl extends GenericMapperImpl<Declarant, DeclarantDto>
    implements MapperDeclarant {

  @Override
  public Declarant dtoToEntity(DeclarantDto declarantDto) {
    Declarant declarant = null;
    if (declarantDto != null) {
      declarant = new Declarant();
      declarant.setLibelle(declarantDto.getLibelle());
      declarant.setNom(declarantDto.getNom());
      declarant.setNumeroPrefectoral(declarantDto.getNumeroPrefectoral());
      declarant.setSiret(declarantDto.getSiret());
      declarant.setCodePartenaire(declarantDto.getCodePartenaire());
      declarant.setDateCreation(declarantDto.getDateCreation());
      declarant.setUserCreation(declarantDto.getUserCreation());
      declarant.setDateModification(declarantDto.getDateModification());
      declarant.setUserModification(declarantDto.getUserModification());
    }
    return declarant;
  }

  @Override
  public DeclarantDto entityToDto(
      Declarant declarant,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    DeclarantDto declarantDto = null;

    if (declarant != null) {
      declarantDto = new DeclarantDto();
      declarantDto.setNumeroRNM(declarant.get_id());
      declarantDto.setLibelle(declarant.getLibelle());
      declarantDto.setNom(declarant.getNom());
      if (StringUtils.isBlank(numAmcRecherche)) {
        numAmcRecherche = declarant.get_id();
      }
      declarantDto.setNumeroPrefectoral(numAmcRecherche);
      declarantDto.setSiret(declarant.getSiret());
      declarantDto.setCodePartenaire(declarant.getCodePartenaire());
      declarantDto.setDateCreation(declarant.getDateCreation());
      declarantDto.setUserCreation(declarant.getUserCreation());
      declarantDto.setDateModification(declarant.getDateModification());
      declarantDto.setUserModification(declarant.getUserModification());
    }
    return declarantDto;
  }
}
