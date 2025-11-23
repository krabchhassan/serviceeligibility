package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.CacheDao;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericServiceImpl;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Cache;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Classe d'accès aux services lies aux {@code Cache}. */
@Service("cacheService")
public class CacheServiceImpl extends GenericServiceImpl<Cache> implements CacheService {

  /**
   * public constructeur.
   *
   * @param cacheDao bean dao injecte
   */
  @Autowired
  public CacheServiceImpl(@Qualifier("cacheDao") final IMongoGenericDao<Cache> cacheDao) {
    super(cacheDao);
  }

  /**
   * Cast le genericDao.
   *
   * @return La dao de {@code Cache}
   */
  @Override
  @ContinueSpan(log = "getCacheDao")
  public CacheDao getCacheDao() {
    return ((CacheDao) this.getGenericDao());
  }

  @Override
  @ContinueSpan(log = "findByName Cache")
  public Cache findByName(final String name) {
    return this.getCacheDao().findByName(name);
  }

  @Override
  @ContinueSpan(log = "findDtoById Cache")
  public Cache findDtoById(final String id) {
    return this.getCacheDao().findById(id, Cache.class);
  }

  @ContinueSpan(log = "createCache")
  public Cache createCache(final Cache cache) {
    cache.setVersion(1);
    this.getCacheDao().create(cache);
    return this.getCacheDao().findByName(cache.getCacheName());
  }

  @ContinueSpan(log = "updateCache")
  public void updateCache(final Cache cacheToUpdate, final Cache existingCache) {
    existingCache.setCacheName(cacheToUpdate.getCacheName());
    Integer oldVersion = existingCache.getVersion();
    oldVersion++;
    existingCache.setVersion(oldVersion);
    existingCache.setChangeDate(cacheToUpdate.getChangeDate());
    existingCache.setUserId(cacheToUpdate.getUserId());
    this.getCacheDao().update(existingCache);
  }

  @Override
  @ContinueSpan(log = "saveOrUpdate Cache")
  public Cache saveOrUpdate(final Cache cacheToSave) {
    final Cache existingCache = this.findByName(cacheToSave.getCacheName());
    if (existingCache == null) {
      this.createCache(cacheToSave);
    } else {
      this.updateCache(cacheToSave, existingCache);
    }

    return this.findByName(cacheToSave.getCacheName());
  }

  @Override
  @ContinueSpan(log = "deleteCache")
  public void deleteCache() {
    this.getCacheDao().dropCollection(Cache.class);
  }
}
