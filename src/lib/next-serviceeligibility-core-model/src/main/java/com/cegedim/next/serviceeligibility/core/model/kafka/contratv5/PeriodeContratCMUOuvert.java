package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PeriodeContratCMUOuvert {
  @NotBlank private String code;
  @NotBlank private Periode periode;

  public PeriodeContratCMUOuvert(PeriodeContratCMUOuvert source) {
    this.code = source.getCode();
    if (source.getPeriode() != null) {
      this.periode = new Periode(source.getPeriode());
    }
  }
}
