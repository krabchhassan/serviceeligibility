package com.cegedim.next.serviceeligibility.core.kafka.services;

import com.cegedim.next.serviceeligibility.core.kafka.dao.TriggerKafkaDao;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TriggerKafkaService {
  @Autowired TriggerKafkaDao triggerDao;

  @ContinueSpan(log = "updateStatus trigger")
  public void updateStatus(String id, TriggerStatus statut) {
    triggerDao.updateStatus(id, statut);
  }

  // utilisé par triggerRenouvellement
  @ContinueSpan(log = "sendMessageUnitaire")
  public int sendMessageUnitaire(String idTrigger, Long randomRecyclingId) {
    return triggerDao.getTriggersBatchUnitaireAndSend(idTrigger, randomRecyclingId, null);
  }
}
