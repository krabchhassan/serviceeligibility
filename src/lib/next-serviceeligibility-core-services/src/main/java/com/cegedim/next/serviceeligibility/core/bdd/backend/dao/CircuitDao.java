package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import java.util.List;

/** Interface de la classe d'accès aux {@code Circuit} de la base de donnees. */
public interface CircuitDao extends IMongoGenericDao<Circuit> {

  /**
   * Recherche dans la base de donnees tous les circuits.
   *
   * @return liste des circuits trouves.
   */
  List<Circuit> findAllCircuits();
}
