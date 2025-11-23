package com.cegedim.next.serviceeligibility.core.services;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class MemoryCacheService {

  private final CacheManager cacheManager;

  public MemoryCacheService(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  private final Logger logger = LoggerFactory.getLogger(MemoryCacheService.class);

  public boolean evictAllCache() {
    boolean retour = false;
    if (CollectionUtils.isEmpty(cacheManager.getCacheNames())) {
      return true;
    }
    for (String cacheName : cacheManager.getCacheNames()) {
      retour = this.clearCache(cacheName);
    }
    return retour;
  }

  public boolean clearCache(String cacheName) {
    logger.debug("Vidage du cache '{}'", cacheName);
    Cache cache = cacheManager.getCache(cacheName);

    if (cache != null) {
      // la méthode invalidate renvoie false si y a des clés sinon true (on s'en fout pas mal)
      cache.invalidate();
    }
    return true;
  }

  public void evictReferentialCache() {
    clearCache("referentialCache");
  }
}
