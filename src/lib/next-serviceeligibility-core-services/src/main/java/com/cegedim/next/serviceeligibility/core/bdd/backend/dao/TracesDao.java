package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Traces;

/** Interface de la classe d'accès aux {@code Traces} de la base de donnees. */
public interface TracesDao extends IMongoGenericDao<Traces> {

  /**
   * Recherche dans la base de donnees une traces avec un identifiant identique à celui de la
   * déclaration recherchée.
   *
   * @param idDeclaration l'id de la déclaration.
   * @return Traces.
   */
  Traces findByIdDeclaration(String idDeclaration);
}
