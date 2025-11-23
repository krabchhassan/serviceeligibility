package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.BeneficiaireV2;

/** Interface du mapper de l'entite {@code Beneficiaire}. */
public interface MapperBeneficiaire extends GenericMapper<BeneficiaireV2, BeneficiaireDto> {

  @Override
  BeneficiaireV2 dtoToEntity(BeneficiaireDto beneficiaireDto);

  @Override
  BeneficiaireDto entityToDto(
      BeneficiaireV2 beneficiaire,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
