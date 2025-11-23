package com.cegedim.next.consumer.api.service;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.LOAD_DECLARANTS_ONLY_REDEPLOY;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("declarantServiceWorker")
public class TestDeclarantService {
  private final BeyondPropertiesService beyondPropertiesService;
  private final MongoTemplate template;
  List<Declarant> declarants = null;

  @PostConstruct
  public void loadDeclarants() {
    if (beyondPropertiesService
        .getBooleanProperty(LOAD_DECLARANTS_ONLY_REDEPLOY)
        .orElse(Boolean.FALSE)) {
      declarants = template.findAll(Declarant.class);
    }
  }
}
