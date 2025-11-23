package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.TriggerCountDao;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TriggerCountService {

  private final TriggerCountDao triggerCountDao;

  @ContinueSpan(log = "saveTriggerCount")
  public void saveTriggerCount(String triggerId, int nbTriggerUnitaire) {
    triggerCountDao.saveTriggerCount(triggerId, nbTriggerUnitaire);
  }

  @ContinueSpan(log = "incCountTriggerUnitaire")
  public void incCountTriggerUnitaire(String triggerId) {
    triggerCountDao.incCountTriggerUnitaire(triggerId);
  }

  @ContinueSpan(log = "updateLockTriggerUnitaire")
  public void updateLockTriggerUnitaire(String triggerId) {
    triggerCountDao.updateLockTriggerUnitaire(triggerId);
  }

  @ContinueSpan(log = "incCountTriggerUnitaire")
  public long findAndLock(String triggerId) {
    return triggerCountDao.findAndLock(triggerId);
  }

  @ContinueSpan(log = "getTriggerCount")
  public TriggerCount getTriggerCount(String triggerId) {
    return triggerCountDao.getTriggerCount(triggerId);
  }
}
