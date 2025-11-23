package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

public enum TriggerStatus {
  Deleted,
  Processed,
  ProcessedWithErrors,
  StandBy,
  ToProcess,
  Processing,
  Abandonned,
  ProcessedWithWarnings,
  Abandoning
}
