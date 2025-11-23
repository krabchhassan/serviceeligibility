package com.cegedim.next.common.kafkaconnect.readiness.model;

public enum ConnectorState {
  UNASSIGNED,
  RUNNING,
  PAUSED,
  FAILED,
  DESTROYED,
}
