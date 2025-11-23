package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.ParametreBddDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.CodesRenvoiDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ErreursDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresPrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametreBdd;
import java.util.List;
import java.util.Map;

public interface ParametreBddService extends GenericService<ParametreBdd> {

  /**
   * @return La DAO des ParametreBdd.
   */
  ParametreBddDao getParametreBddDao();

  /**
   * Trouve la liste de tous les code rejets
   *
   * @return une map d'erreur comprenant le codeErreur, et l'objet complet
   */
  Map<String, ErreursDto> findRejets();

  Map<String, CodesRenvoiDto> findCodesRenvoi();

  /**
   * Find all parameters with the given type
   *
   * @param type Parameter's type
   * @return All parameters matching the given type
   */
  List<ParametresDto> findByType(String type);

  /**
   * Find all prestations for a given domaine
   *
   * @param type Paramter's type
   * @param domaine Domain's code
   * @return All prestations associated to the domain
   */
  List<ParametresPrestationDto> findPrestationsByDomaine(String type, String domaine);

  /**
   * Find a parameter value by his type and code
   *
   * @param type Parameter type
   * @param code Parameter code
   * @return The parameter which match the type and code value
   */
  ParametresDto findOneByType(String type, String code);

  /**
   * Save a parameter
   *
   * @param type Parameter type
   * @param parametresDto Parameter's Dto
   * @param update Flag to know if this is an update or a new object to save
   * @return The Parameter's Dto of the saved parameter
   */
  List<ParametresDto> saveOrUpdate(
      String type, ParametresDto parametresDto, boolean update, String version);

  /**
   * Save a parameter
   *
   * @param type Parameter type
   * @param parametresDto Parameter's Dto
   */
  void insert(String type, ParametresDto parametresDto);

  /**
   * Delete a parameter value by his type and code
   *
   * @param type Parameter type
   * @param code Parameter code value
   * @return The Parameter's Dto of the given type
   */
  List<ParametresDto> remove(String type, String code, String version);

  String findLibelleCodeRenvoi(String codeRenvoi);

  void dropCollectionParameter();
}
