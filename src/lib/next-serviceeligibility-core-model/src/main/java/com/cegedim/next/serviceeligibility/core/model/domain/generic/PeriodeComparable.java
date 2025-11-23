package com.cegedim.next.serviceeligibility.core.model.domain.generic;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
public class PeriodeComparable implements GenericDomain<PeriodeComparable> {
  private String debut;
  private String fin;

  public PeriodeComparable(String debut, String fin) {
    if (debut != null) {
      this.debut = debut.replace("-", "/");
    }
    if (fin != null) {
      this.fin = fin.replace("-", "/");
    }
  }

  public PeriodeComparable(PeriodeComparable source) {
    this.debut = source.getDebut();
    this.fin = source.getFin();
  }

  @Override
  public int compareTo(PeriodeComparable o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(debut, o.debut);
    compare.append(fin, o.fin);
    return compare.toComparison();
  }
}
