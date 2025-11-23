package com.cegedim.next.serviceeligibility.consolidationcontract.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SkipRange {
  private long fromDeclaration;
  private long toDeclaration;
  private ContractKey fromContractKey;
  private ContractKey toContractKey;
}
