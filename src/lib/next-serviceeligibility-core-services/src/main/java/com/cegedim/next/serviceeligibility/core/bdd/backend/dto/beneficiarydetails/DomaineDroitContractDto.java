package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import java.util.List;
import lombok.Data;

@Data
public class DomaineDroitContractDto {
  private String code;
  private List<PeriodeDroitContractDto> periodesDroit;
}
