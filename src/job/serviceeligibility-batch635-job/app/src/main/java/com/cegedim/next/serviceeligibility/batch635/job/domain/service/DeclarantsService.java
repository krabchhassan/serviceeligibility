package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import com.cegedim.next.serviceeligibility.batch635.job.domain.model.Declarants;

public interface DeclarantsService {
  String getIdentityByDeclarants(Declarants declarant);

  boolean declarantExistsById(String idDeclarant);

  Declarants getDeclarantById(String idDeclarant);
}
