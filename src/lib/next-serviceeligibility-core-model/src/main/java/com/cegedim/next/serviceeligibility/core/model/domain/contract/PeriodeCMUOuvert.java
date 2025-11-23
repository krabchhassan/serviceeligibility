package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
public class PeriodeCMUOuvert implements GenericDomain<PeriodeCMUOuvert> {
  private String code;
  private PeriodeComparable periode;

  public PeriodeCMUOuvert(PeriodeCMUOuvert source) {
    this.code = source.getCode();
    if (source.getPeriode() != null) {
      this.periode = new PeriodeComparable(source.getPeriode());
    }
  }

  @Override
  public int compareTo(PeriodeCMUOuvert o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(code, o.code);
    compare.append(periode, o.periode);
    return compare.toComparison();
  }
}
