package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import java.util.List;
import lombok.Data;

@Data
public class ContractProcessResponseV5 {
  private List<BenefAIV5> benefs;
  private String key;
  private ContratAICommun contract;
}
