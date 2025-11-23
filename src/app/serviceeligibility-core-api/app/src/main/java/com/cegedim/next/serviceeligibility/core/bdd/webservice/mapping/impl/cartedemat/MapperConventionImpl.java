package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.ConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat.MapperConvention;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code Convention}. */
@Component
public class MapperConventionImpl extends GenericMapperImpl<Convention, ConventionDto>
    implements MapperConvention {

  @Override
  public Convention dtoToEntity(ConventionDto conventionDto) {
    Convention convention = null;
    if (conventionDto != null) {
      convention = new Convention();
      convention.setCode(conventionDto.getTypeConventionnement());
      convention.setPriorite(conventionDto.getPrioriteConventionnement());
    }
    return convention;
  }

  @Override
  public ConventionDto entityToDto(
      Convention convention,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    ConventionDto conventionDto = null;
    if (convention != null) {
      conventionDto = new ConventionDto();
      conventionDto.setTypeConventionnement(convention.getCode());
      conventionDto.setPrioriteConventionnement(convention.getPriorite());
    }
    return conventionDto;
  }
}
