package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresPrestationDto;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import java.util.List;

/** Interface de la classe d'acces aux {@code ParametreBdd} de la base de donnees. */
public interface ParametreBddDao extends IMongoGenericDao<ParametreBdd> {

  /**
   * Trouve une liste de parametres selon un code fourni
   *
   * @param codeParametre
   * @return liste de parametres, null si codeParametre non defini
   */
  ParametreBdd findParametres(String codeParametre);

  /**
   * Trouve une liste de prestations associées à un domaine
   *
   * @param codeDomaine
   * @return liste de prestations
   */
  List<ParametresPrestationDto> findPrestationsByDomain(String codeDomaine);
}
