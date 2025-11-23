package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PeriodesDroitsCarte {
  @NotEmpty private String debut;
  @NotEmpty private String fin;

  public PeriodesDroitsCarte(PeriodesDroitsCarte source) {
    this.debut = source.getDebut();
    this.fin = source.getFin();
  }
}
