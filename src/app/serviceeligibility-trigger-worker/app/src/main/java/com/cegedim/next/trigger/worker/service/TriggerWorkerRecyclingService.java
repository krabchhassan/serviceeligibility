package com.cegedim.next.trigger.worker.service;

import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.kafka.trigger.Producer;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.services.pojo.RequestTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.ResponseTriggerProcessing;
import com.cegedim.next.serviceeligibility.core.services.pojo.TriggerContract;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerBuildDeclarationNewService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerCreationService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerProcessingService;
import com.cegedim.next.serviceeligibility.core.services.trigger.TriggerRecyclageService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TriggerWorkerRecyclingService extends TriggerProcessingService {

  public TriggerWorkerRecyclingService(
      TriggerBuildDeclarationNewService triggerBuildDeclarationService,
      TriggerCreationService triggerCreationService,
      TriggerService triggerService,
      SasContratService sasContratService,
      ServicePrestationDao servicePrestationDao,
      Producer producer,
      TriggerRecyclageService triggerRecyclageService) {
    super(
        triggerBuildDeclarationService,
        triggerCreationService,
        triggerService,
        sasContratService,
        servicePrestationDao,
        producer,
        triggerRecyclageService);
  }

  @Override
  @ContinueSpan(log = "processTrigger from TriggerWorkerRecyclingService")
  public ResponseTriggerProcessing processTrigger(
      RequestTriggerProcessing requestTriggerProcessing) {
    ResponseTriggerProcessing responseTriggerProcessing = new ResponseTriggerProcessing();
    List<String> recycledIdTrigger = new ArrayList<>();
    if (StringUtils.isNotBlank(requestTriggerProcessing.getIdTrigger())) {
      List<SasContrat> sasContrats =
          sasContratService.getByIdTrigger(requestTriggerProcessing.getIdTrigger());
      if (CollectionUtils.isNotEmpty(sasContrats)) {
        // Tous les sas contrats sont sur le même contrat, il va y avoir plusieurs
        // triggers dedans le sas.
        SasContrat sasContrat = sasContrats.get(0);
        recycledIdTrigger =
            manageRecycleBySasContrat(
                sasContrat,
                0L,
                Constants.KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE,
                requestTriggerProcessing.getSession());
      }
    } else {
      log.error(
          "Le trigger {} n'existe pas ou ne référence aucun bénéficiaire",
          requestTriggerProcessing.getIdTrigger());
    }
    if (CollectionUtils.isNotEmpty(recycledIdTrigger)) {
      for (String idTrigger : recycledIdTrigger) {
        TriggerContract triggerContract = new TriggerContract(requestTriggerProcessing.getBenefs());
        triggerContract.setIdTrigger(idTrigger);
        responseTriggerProcessing.getTriggerContracts().add(triggerContract);
      }
    }
    return responseTriggerProcessing;
  }
}
