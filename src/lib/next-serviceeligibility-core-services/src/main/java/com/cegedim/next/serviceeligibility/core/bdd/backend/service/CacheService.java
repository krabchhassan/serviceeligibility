package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CacheDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.model.entity.Cache;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Cache}. */
public interface CacheService extends GenericService<Cache> {

  /**
   * @return La DAO des caches.
   */
  CacheDao getCacheDao();

  /**
   * Recherche dans la base de donnees un cache a partir de son nom.
   *
   * @param name le nom du cache.
   * @return le cache.
   */
  Cache findByName(String name);

  /**
   * Recherche dans la base de donnees un cache a partir de son identifiant technique.
   *
   * @param id l'identifiant du cache.
   * @return le cache dto.
   */
  Cache findDtoById(String id);

  /**
   * Recherche dans la base de donnees tous les caches
   *
   * @return liste des caches trouves. Si aucun cache n'est trouve, cette méthode retournera une
   *     liste vide.
   */
  @Override
  List<Cache> findAll();

  /**
   * Creation ou modification du cache
   *
   * @param cache cache a creer ou mettre a jour.
   * @return cache cree dans la base
   */
  Cache saveOrUpdate(Cache cache);

  /** Suppression du cache pour les tests */
  void deleteCache();
}
