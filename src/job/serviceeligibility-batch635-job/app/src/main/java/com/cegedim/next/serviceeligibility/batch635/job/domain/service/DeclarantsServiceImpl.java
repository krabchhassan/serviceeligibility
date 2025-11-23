package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import com.cegedim.next.serviceeligibility.batch635.job.domain.model.Declarants;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.DeclarantsRepository;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeclarantsServiceImpl implements DeclarantsService {

  private final DeclarantsRepository declarantsRepository;

  @Value("${TARGET_ENV:local}")
  private String targetEnv;

  @Override
  @ContinueSpan(log = "getIdentityByDeclarants")
  public String getIdentityByDeclarants(Declarants declarant) {
    return targetEnv
        + declarant.getEmetteurDroits()
        + declarant.getCodeCircuit()
        + declarant.getCodePartenaire();
  }

  @Override
  @ContinueSpan(log = "declarantExistsById")
  public boolean declarantExistsById(String idDeclarant) {
    return declarantsRepository.existsById(idDeclarant);
  }

  @Override
  @ContinueSpan(log = "getDeclarantById")
  public Declarants getDeclarantById(String idDeclarant) {
    Optional<Declarants> optionalDeclarants = declarantsRepository.findById(idDeclarant);

    if (optionalDeclarants.isEmpty()) {
      throw new IllegalStateException(
          String.format("The declarant with id '%s' doesn't exist.", idDeclarant));
    }

    return optionalDeclarants.get();
  }
}
