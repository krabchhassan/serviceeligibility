package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.bdd.ServicePrestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RenouvellementService {

  @Autowired ServicePrestationService contractService;

  @Autowired ParametrageCarteTPService parametrageCarteTPService;

  private final Logger logger = LoggerFactory.getLogger(RenouvellementService.class);

  void addParametrageID(final ContratAIV6 contrat) {
    for (final Assure assure : contrat.getAssures()) {
      final TriggeredBeneficiary trgBenef = new TriggeredBeneficiary();
      final ServicePrestationTriggerBenef contractBenef = new ServicePrestationTriggerBenef();
      contractBenef.setDroitsGaranties(assure.getDroits());

      trgBenef.setNewContract(contractBenef);
      long t0 = System.currentTimeMillis();
      ParametrageCarteTP parametrageCarteTP =
          parametrageCarteTPService.extractBestParametrage(
              contrat, TriggerEmitter.Renewal, trgBenef);
      if (parametrageCarteTP != null) {
        assure.setParametrageCarteId(parametrageCarteTP.getId());
      }
      long t1 = System.currentTimeMillis();
      if (assure.getIdentite() != null) {
        logger.debug(
            "Temps de calcul du paramétrage {} pour l'assuré {}",
            (t1 - t0),
            assure.getIdentite().getNumeroPersonne());
      }
    }
  }
}
