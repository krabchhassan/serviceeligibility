package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.TranscoParametrageDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco.TranscoParamDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.TranscoParametrage;
import java.util.List;

public interface TranscoParametrageService extends GenericService<TranscoParametrage> {

  /**
   * @return La DAO des transcodage
   */
  TranscoParametrageDao getTranscoParametrageDao();

  /**
   * Trouve transco Parametrage pour Code Objet Transco
   *
   * @param codeObjetTransco Code Objet Transco.
   * @return transco Parametrage.
   */
  TranscoParamDto findTranscoParametrage(String codeObjetTransco);

  /**
   * Retourne tous les parametrages de transco
   *
   * @return liste de transco Parametrage.
   */
  List<TranscoParamDto> findAllTranscoParametrage();

  /**
   * Create or update a transcoding paramameter
   *
   * @param transcoParamDto transcoding parameter Dto
   * @return Parameter transconding Dto
   */
  TranscoParamDto saveOrUpdate(TranscoParamDto transcoParamDto);

  /**
   * Delete a transcondig parameter by code
   *
   * @param code Code du paramétrage transcodage
   */
  void delete(String code);
}
