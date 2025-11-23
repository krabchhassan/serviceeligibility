package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import lombok.Data;

@Data
public class PeriodeDto implements GenericDto {
  private String debut;
  private String fin;

  public PeriodeDto(PeriodeComparable source) {
    if (source != null) {
      this.debut = source.getDebut();
      this.fin = source.getFin();
    }
  }
}
