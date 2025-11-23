package com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.generic;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;

/** Interface de la classe {@code GenericMapperImpl}. */
@SuppressWarnings("rawtypes")
public interface GenericMapper<Domain extends GenericDomain, Dto extends GenericDto> {

  /**
   * Mappe un objet Dto en objet Domain.
   *
   * @param dto objet a mapper
   * @return objet Domain mappé
   */
  Domain dtoToEntity(Dto dto);

  /**
   * Mappe une liste d'objet Dto en une liste d'objet Domain.
   *
   * @param dtoList list d'objet à mapper
   * @return liste d'objet Domain mappé
   */
  List<Domain> dtoListToEntityList(List<Dto> dtoList);

  /**
   * Mappe un objet Domain en objet Dto.
   *
   * @param domain objet a mapper
   * @return objet Dto mappé
   */
  Dto entityToDto(
      Domain domain,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);

  /**
   * Mappe une liste d'objet Domain en une liste d'objet Dto.
   *
   * @param domainList objet a mapper
   * @return liste d'objet Dto mappé
   */
  List<Dto> entityListToDtoList(
      List<Domain> domainList,
      TypeProfondeurRechercheService profondeurRecherche,
      boolean isFormatV2,
      boolean isFormatV3,
      String numAmcRecherche);
}
