package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BeneficiaireCouvertureDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapper;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;

/** Interface du mapper de l'entite {@code BeneficiaireCouverture}. */
public interface MapperBeneficiaireCouverture
    extends GenericMapper<DomaineDroit, BeneficiaireCouvertureDto> {

  @Override
  DomaineDroit dtoToEntity(BeneficiaireCouvertureDto beneficiaireCouvertureDto);

  @Override
  BeneficiaireCouvertureDto entityToDto(
      DomaineDroit domaineDroit,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
