package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import lombok.Getter;

@Getter
public enum PartitionStatus {
  NOT_PROCESSED("not_processed"),
  PROCESSED("processed"),
  PROCESSING("processing");

  private final String value;

  PartitionStatus(String value) {
    this.value = value;
  }
}
