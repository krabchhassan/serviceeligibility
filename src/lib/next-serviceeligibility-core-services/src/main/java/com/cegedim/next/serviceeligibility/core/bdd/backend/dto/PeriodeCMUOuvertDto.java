package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeCMUOuvert;
import lombok.Data;

@Data
public class PeriodeCMUOuvertDto implements GenericDto {
  private String code;
  private PeriodeDto periode;

  public PeriodeCMUOuvertDto(PeriodeCMUOuvert source) {
    if (source != null) {
      this.code = source.getCode();
      this.periode = new PeriodeDto(source.getPeriode());
    }
  }
}
