package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerCount;

public interface TriggerCountDao {

  void saveTriggerCount(String triggerId, int nbTriggerUnitaire);

  void incCountTriggerUnitaire(String triggerId);

  void updateLockTriggerUnitaire(String triggerId);

  long findAndLock(String triggerId);

  TriggerCount getTriggerCount(String triggerId);
}
