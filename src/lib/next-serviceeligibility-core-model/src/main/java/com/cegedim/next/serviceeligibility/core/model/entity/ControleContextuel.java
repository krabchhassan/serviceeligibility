package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.Map;
import lombok.Data;

@Data
public class ControleContextuel {
  private Map<String, Integer> beneficiaire;
  private Map<String, Integer> contrat;
  private Map<String, Integer> domaineDroits;
}
