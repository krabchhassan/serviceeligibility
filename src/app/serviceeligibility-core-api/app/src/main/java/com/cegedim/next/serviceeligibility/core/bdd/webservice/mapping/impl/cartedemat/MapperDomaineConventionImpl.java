package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.DomaineConventionDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic.GenericMapperImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.cartedemat.MapperDomaineConvention;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Classe de mapping de l'entite {@code DomaineConvention}. */
@Component
public class MapperDomaineConventionImpl
    extends GenericMapperImpl<DomaineConvention, DomaineConventionDto>
    implements MapperDomaineConvention {

  @Autowired MapperConventionImpl mapperConvention;

  @Override
  public DomaineConvention dtoToEntity(DomaineConventionDto domaineConventionDto) {
    DomaineConvention domaineConvention = null;
    if (domaineConventionDto != null) {
      domaineConvention = new DomaineConvention();
      domaineConvention.setCode(domaineConventionDto.getCodeDomaine());
      domaineConvention.setRang(domaineConventionDto.getRang());
      domaineConvention.setConventions(
          mapperConvention.dtoListToEntityList(domaineConventionDto.getConventionDtos()));
    }
    return domaineConvention;
  }

  @Override
  public DomaineConventionDto entityToDto(
      DomaineConvention domaineConvention,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    DomaineConventionDto domaineConventionDto = null;
    if (domaineConvention != null) {
      domaineConventionDto = new DomaineConventionDto();
      domaineConventionDto.setCodeDomaine(domaineConvention.getCode());
      domaineConventionDto.setRang(domaineConvention.getRang());
      domaineConventionDto.setConventionDtos(
          mapperConvention.entityListToDtoList(
              domaineConvention.getConventions(),
              profondeurRecherche,
              isFormatV2,
              isFormatV3,
              numAmcRecherche));
    }
    return domaineConventionDto;
  }
}
