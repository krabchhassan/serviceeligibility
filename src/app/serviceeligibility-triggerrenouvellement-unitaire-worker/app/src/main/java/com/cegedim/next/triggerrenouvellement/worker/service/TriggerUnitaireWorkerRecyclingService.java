package com.cegedim.next.triggerrenouvellement.worker.service;

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
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TriggerUnitaireWorkerRecyclingService extends TriggerProcessingService {

  public TriggerUnitaireWorkerRecyclingService(
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
  @ContinueSpan(log = "processTrigger from TriggerUnitaireWorkerRecyclingService")
  public ResponseTriggerProcessing processTrigger(
      RequestTriggerProcessing requestTriggerProcessing) {
    if (StringUtils.isNotBlank(requestTriggerProcessing.getIdTrigger())) {
      log.debug("recyclage du trigger {} ", requestTriggerProcessing.getIdTrigger());
    }
    ResponseTriggerProcessing responseTriggerProcessing = new ResponseTriggerProcessing();
    List<String> recycledIdTrigger = new ArrayList<>();
    if (StringUtils.isNotBlank(requestTriggerProcessing.getIdTrigger())) {
      List<SasContrat> sasContrats =
          sasContratService.getByIdTrigger(requestTriggerProcessing.getIdTrigger());
      if (CollectionUtils.isNotEmpty(sasContrats)) {
        SasContrat sasContrat =
            sasContratService.getByServicePrestationId(
                requestTriggerProcessing.getServicePrestationId());
        if (sasContrat != null) {
          recycledIdTrigger =
              manageRecycleBySasContrat(
                  sasContrat,
                  requestTriggerProcessing.getRandomRecyclingId(),
                  requestTriggerProcessing.getUpdateTrigger(),
                  requestTriggerProcessing.getSession());
        }
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
