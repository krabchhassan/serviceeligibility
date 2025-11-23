package com.cegedim.next.serviceeligibility.initrdoclaim.services;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdoClaim;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.RDOServicePrestationService;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import com.cegedim.next.serviceeligibility.initrdoclaim.constants.Constants;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RdoClaimProcessor {

  private final RDOServicePrestationService rdoServicePrestationService;

  private final ServicePrestationService servicePrestationService;

  @NewSpan
  public int fillCollection(String idDeclarant, CompteRenduRdoClaim compteRenduRdoClaim) {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;
    AtomicInteger nbContratsLus = new AtomicInteger();
    AtomicInteger nbBenefIntegres = new AtomicInteger();
    Stream<ContratAIV6> contrat =
        servicePrestationService.getAllContratForParametrageStream(idDeclarant);
    contrat.forEach(
        contratAIV6 -> {
          nbContratsLus.getAndIncrement();
          nbBenefIntegres.addAndGet(rdoServicePrestationService.upsertRdo(contratAIV6));
        });

    compteRenduRdoClaim.setNombreContratsLus(nbContratsLus.get());
    compteRenduRdoClaim.setNbBeneficiairesIntegres(nbBenefIntegres.get());
    return processReturnCode;
  }
}
