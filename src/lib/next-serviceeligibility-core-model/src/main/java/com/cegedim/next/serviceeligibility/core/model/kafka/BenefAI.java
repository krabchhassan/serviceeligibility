package com.cegedim.next.serviceeligibility.core.model.kafka;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BenefAI extends PersonAI {
  private List<String> services;
  private String key;
}
