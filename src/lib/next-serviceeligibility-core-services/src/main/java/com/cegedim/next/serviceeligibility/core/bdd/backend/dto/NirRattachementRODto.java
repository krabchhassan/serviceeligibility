package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirRattachementRODeclaration;
import lombok.Data;

@Data
public class NirRattachementRODto implements GenericDto {
  private NirDto nir;
  private RattachementRODto rattachementRO;
  private PeriodeDto periode;

  public NirRattachementRODto() {}

  public NirRattachementRODto(NirRattachementRODeclaration source) {
    if (source != null) {
      this.nir = new NirDto(source.getNir());
      this.rattachementRO = new RattachementRODto(source.getRattachementRO());
      this.periode = new PeriodeDto(source.getPeriode());
    }
  }
}
