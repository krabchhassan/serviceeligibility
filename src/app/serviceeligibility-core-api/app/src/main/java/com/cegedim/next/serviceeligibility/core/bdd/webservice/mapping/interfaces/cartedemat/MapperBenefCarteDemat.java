package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BenefCarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;

/** Interface du mapper de l'entite {@code BenefCarteDemat}. */
public interface MapperBenefCarteDemat extends GenericMapper<BenefCarteDemat, BenefCarteDematDto> {

  @Override
  BenefCarteDemat dtoToEntity(BenefCarteDematDto benefCarteDematDto);

  @Override
  BenefCarteDematDto entityToDto(
      BenefCarteDemat benefCarteDemat,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
