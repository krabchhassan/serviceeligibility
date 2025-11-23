package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PeriodeSuspension {
  private Periode periode;
  private String typeSuspension;
  private String motifSuspension;
  private String motifLeveeSuspension;

  public PeriodeSuspension(PeriodeSuspension source) {
    if (source.getPeriode() != null) {
      this.periode = new Periode(source.getPeriode());
    }
    this.setTypeSuspension(source.getTypeSuspension());
    this.setMotifSuspension(source.getMotifSuspension());
    this.setMotifLeveeSuspension(source.getMotifLeveeSuspension());
  }
}
