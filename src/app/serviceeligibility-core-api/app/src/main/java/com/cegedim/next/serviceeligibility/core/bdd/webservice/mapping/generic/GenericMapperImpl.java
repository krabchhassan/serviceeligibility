package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/** Classe parenete de toutes les classe {@code Mapper}. */
@SuppressWarnings("rawtypes")
@Component
public abstract class GenericMapperImpl<Domain extends GenericDomain, Dto extends GenericDto>
    implements GenericMapper<Domain, Dto> {

  /** constructeur protected. */
  protected GenericMapperImpl() {}

  @Override
  public List<Domain> dtoListToEntityList(final List<Dto> dtoList) {
    List<Domain> domainList = new ArrayList<>();
    for (Dto dto : dtoList) {
      domainList.add(dtoToEntity(dto));
    }
    return domainList;
  }

  @Override
  public List<Dto> entityListToDtoList(
      final List<Domain> domainList,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche) {
    List<Dto> dtoList = new ArrayList<>();
    if (profondeurRecherche == null) {
      profondeurRecherche = TypeProfondeurRechercheService.AVEC_FORMULES;
    }
    if (domainList != null) {
      for (Domain domain : domainList) {
        dtoList.add(
            entityToDto(domain, profondeurRecherche, isFormatV2, isFormatV3, numAmcRecherche));
      }
    }
    return dtoList;
  }
}
