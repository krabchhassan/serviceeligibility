package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleMetierDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperFormuleMetier;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code FormuleMetier}. */
@Component
public class MapperFormuleMetierImpl extends GenericMapperImpl<FormuleMetier, FormuleMetierDto>
    implements MapperFormuleMetier {

  @Override
  public FormuleMetier dtoToEntity(FormuleMetierDto formuleMetierDto) {
    FormuleMetier formuleMetier = new FormuleMetier();
    if (formuleMetierDto != null) {
      formuleMetier = new FormuleMetier();
      formuleMetier.setCode(formuleMetierDto.getCode());
      formuleMetier.setLibelle(formuleMetierDto.getLibelle());
    }
    return formuleMetier;
  }

  @Override
  public FormuleMetierDto entityToDto(
      FormuleMetier formuleMetier,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    FormuleMetierDto formuleMetierDto = new FormuleMetierDto();
    if (formuleMetier != null) {
      formuleMetierDto = new FormuleMetierDto();
      formuleMetierDto.setCode(formuleMetier.getCode());
      formuleMetierDto.setLibelle(formuleMetier.getLibelle());
    }
    return formuleMetierDto;
  }
}
