package com.cegedim.next.serviceeligibility.core;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.PARAMETER_CACHE_NAME;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.CacheService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.VisioDroitsService;
import com.cegedim.next.serviceeligibility.core.model.entity.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CacheScheduler {
  private final BeyondPropertiesService beyondPropertiesService;
  private final CacheService cacheService;
  private final VisioDroitsService visioDroitsService;

  private Integer localVersion = 0;

  @Scheduled(fixedDelay = 60000)
  public void checkCache() {

    Cache onlineCache =
        cacheService.findByName(
            beyondPropertiesService.getPropertyOrThrowError(PARAMETER_CACHE_NAME));
    if (onlineCache != null) {
      Integer onlineVersion = onlineCache.getVersion();
      if (onlineVersion != null) {
        if (localVersion < onlineVersion) {
          visioDroitsService.emptyCache();
          localVersion = onlineVersion;
        }
      } else {
        onlineCache.setVersion(0);
      }
    }
  }
}
