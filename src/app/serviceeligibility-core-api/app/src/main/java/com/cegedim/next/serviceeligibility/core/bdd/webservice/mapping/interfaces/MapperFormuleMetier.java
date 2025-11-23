package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleMetierDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.FormuleMetier;

/** Interface du mapper de l'entite {@code FormuleMetier}. */
public interface MapperFormuleMetier extends GenericMapper<FormuleMetier, FormuleMetierDto> {

  @Override
  FormuleMetier dtoToEntity(FormuleMetierDto formuleMetierDto);

  @Override
  FormuleMetierDto entityToDto(
      FormuleMetier formuleMetier,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
