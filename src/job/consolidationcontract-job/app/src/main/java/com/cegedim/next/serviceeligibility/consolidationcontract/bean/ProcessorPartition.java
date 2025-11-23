package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProcessorPartition {
  int partitionNumber;
  SkipRange range;
  PartitionStatus status;
}
