package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.contract;

import java.util.List;
import lombok.Data;

@Data
public class DomaineDroitContratDto {
  private String code;
  private String codeExterne;
  private List<GarantieDto> garanties;
}
