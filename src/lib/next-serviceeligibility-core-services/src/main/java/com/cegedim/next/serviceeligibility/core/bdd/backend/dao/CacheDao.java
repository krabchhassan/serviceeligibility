package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Cache;

/** Interface de la classe d'accès aux {@code Cache} de la base de donnees. */
public interface CacheDao extends IMongoGenericDao<Cache> {

  /**
   * Recherche dans la base de donnees le cache spécifique avec son nom.
   *
   * @param cacheName le nom du cache.
   * @return Cache.
   */
  Cache findByName(String cacheName);
}
