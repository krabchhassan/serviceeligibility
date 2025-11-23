package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.TranscoParametrage;

public interface TranscoParametrageDao extends IMongoGenericDao<TranscoParametrage> {

  /**
   * Trouve transco Parametrage pour Code Objet Transco
   *
   * @param codeObjetTransco Code Objet Transco.
   * @return transco Parametrage.
   */
  TranscoParametrage findTranscoParametrage(String codeObjetTransco);
}
