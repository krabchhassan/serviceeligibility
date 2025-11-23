package com.cegedim.next.serviceeligibility.core.services.scopeManagement;

import com.cegedim.beyond.spring.starter.managementscope.SecuredManagementScopeServiceS3DataLoader;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "beyond.management-scope.enabled", havingValue = "true")
public class ManagementScopesCacheManager {

  private final CacheManager cacheManager;

  /** Clears the management scopes cache periodically. */
  @Scheduled(fixedDelay = 120000)
  public void evictAll() {
    String cacheName = SecuredManagementScopeServiceS3DataLoader.CACHE_MANAGEMENT_SCOPE_ENTITIES;

    Optional.ofNullable(cacheManager.getCache(cacheName))
        .ifPresentOrElse(
            this::evictCache, () -> log.warn("Management scopes cache '{}' not found.", cacheName));
  }

  /** Evicts all entries from the given cache. */
  private void evictCache(Cache cache) {
    log.debug("Evicting management scopes cache '{}'", cache.getName());
    cache.invalidate();
    log.debug("Management scopes cache '{}' successfully evicted.", cache.getName());
  }
}
