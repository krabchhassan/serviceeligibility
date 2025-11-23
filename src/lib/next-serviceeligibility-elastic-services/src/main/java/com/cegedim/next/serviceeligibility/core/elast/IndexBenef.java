package com.cegedim.next.serviceeligibility.core.elast;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.COMMON_INDEX_BASENAME;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexBenef {

  private final BeyondPropertiesService beyondPropertiesService;

  private String indexName;

  @PostConstruct
  public void initIndexName() {
    indexName =
        "bdd-benef-" + beyondPropertiesService.getPropertyOrThrowError(COMMON_INDEX_BASENAME);
  }

  public String getIndexAlias() {
    return indexName;
  }
}
