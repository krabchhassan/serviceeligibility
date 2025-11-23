package com.cegedim.next.serviceeligibility.core.webservices.clc;

import java.util.List;
import lombok.Data;

@Data
public class DomaineDroitContratCLC {
  private String code;
  private String codeExterne;
  private List<GarantieCLC> garanties;
}
