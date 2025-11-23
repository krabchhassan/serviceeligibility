package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ConventionnementDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;

/** Interface du mapper de l'entite {@code Conventionnement}. */
public interface MapperConventionnement
    extends GenericMapper<Conventionnement, ConventionnementDto> {

  @Override
  Conventionnement dtoToEntity(ConventionnementDto conventionnementDto);

  @Override
  public ConventionnementDto entityToDto(
      Conventionnement conventionnement,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
