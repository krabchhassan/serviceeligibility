package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.FormuleDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Formule;

/** Interface du mapper de l'entite {@code Formule}. */
public interface MapperFormule extends GenericMapper<Formule, FormuleDto> {

  @Override
  public Formule dtoToEntity(FormuleDto formuleDto);

  @Override
  public FormuleDto entityToDto(
      Formule formule,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
