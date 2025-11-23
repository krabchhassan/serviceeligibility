package com.cegedim.next.serviceeligibility.core.model.domain.benef;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeletransmissionDeclaration implements GenericDomain<TeletransmissionDeclaration> {
  private PeriodeComparable periode;
  private Boolean isTeletransmission;

  public TeletransmissionDeclaration(TeletransmissionDeclaration source) {
    this.periode = new PeriodeComparable(source.getPeriode());
    this.isTeletransmission = source.getIsTeletransmission();
  }

  @Override
  public int compareTo(TeletransmissionDeclaration o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(periode, o.periode);
    compare.append(isTeletransmission, o.isTeletransmission);
    return compare.toComparison();
  }
}
