package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import lombok.Data;

@Data
public class CodePeriodeDto implements GenericDto {
  private String code;
  private PeriodeDto periode;

  public CodePeriodeDto(CodePeriodeDeclaration source) {
    if (source != null) {
      this.code = source.getCode();
      this.periode = new PeriodeDto(source.getPeriode());
    }
  }
}
