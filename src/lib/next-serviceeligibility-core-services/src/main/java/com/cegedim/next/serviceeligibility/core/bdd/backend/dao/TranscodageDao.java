package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import java.util.List;

public interface TranscodageDao extends IMongoGenericDao<Transcodage> {

  /**
   * Trouve le trascodage pour une cle
   *
   * @param codeService code service.
   * @param codeObjetTransco code Objet Transco.
   * @return list transco.
   */
  List<Transcodage> findByCodeObjetTransco(String codeService, String codeObjetTransco);

  /**
   * Trouve le code trascodage pour une cle
   *
   * @param codeService code service.
   * @param codeObjetTransco code Objet Transco.
   * @return code transco.
   */
  String findCodeTranscoByCodeObjetTransco(String codeService, String codeObjetTransco);
}
