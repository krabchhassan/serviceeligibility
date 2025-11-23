package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import lombok.Data;

@Data
public class TeletransmissionDto implements GenericDto {
  private PeriodeDto periode;
  private Boolean isTeletransmission;

  public TeletransmissionDto(TeletransmissionDeclaration source) {
    if (source != null) {
      this.periode = new PeriodeDto(source.getPeriode());
      this.isTeletransmission = source.getIsTeletransmission();
    }
  }
}
