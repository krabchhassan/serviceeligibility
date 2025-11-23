package com.cegedim.beyond.blb.recalcul.kafka.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
  JOB_RECALCUL_BLB_START_EVENT("recalcul-blb-start-event"), //
  JOB_RECALCUL_BLB_END_EVENT("recalcul-blb-end-event");

  private final String eventType;
}
