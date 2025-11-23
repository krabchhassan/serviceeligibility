package com.cegedim.next.serviceeligibility.core.elast.contract;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.COMMON_INDEX_BASENAME;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("indexHistoContrat")
@RequiredArgsConstructor
public class IndexHistoContrat {

  private final BeyondPropertiesService beyondPropertiesService;

  // méthode appelé dans ContratElastic, voir SimpleElasticsearchPersistentEntity
  // et BeyondElasticsearchPersistentEntity
  public String getKeyIndex() {
    return "bdd-histo-contrat_index";
  }

  public String getIndexName() {
    return "itcare_"
        + beyondPropertiesService.getPropertyOrThrowError(COMMON_INDEX_BASENAME)
        + "_bdd-histo-contrat_index";
  }

  public String getIndexAlias() {
    return "itcare_"
        + beyondPropertiesService.getPropertyOrThrowError(COMMON_INDEX_BASENAME)
        + "_bdd-histo-contrat";
  }
}
