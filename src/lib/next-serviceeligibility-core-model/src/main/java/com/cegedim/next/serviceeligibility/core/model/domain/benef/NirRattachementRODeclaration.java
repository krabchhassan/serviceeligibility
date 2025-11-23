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
public class NirRattachementRODeclaration implements GenericDomain<NirRattachementRODeclaration> {
  private NirDeclaration nir;
  private RattachementRODeclaration rattachementRO;
  private PeriodeComparable periode;

  public NirRattachementRODeclaration(NirRattachementRODeclaration source) {
    if (source.getNir() != null) {
      this.nir = new NirDeclaration(source.getNir());
    }
    if (source.getRattachementRO() != null) {
      this.rattachementRO = new RattachementRODeclaration(source.getRattachementRO());
    }
    if (source.getPeriode() != null) {
      this.periode = new PeriodeComparable(source.getPeriode());
    }
  }

  @Override
  public int compareTo(NirRattachementRODeclaration o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(nir, o.nir);
    compare.append(rattachementRO, o.rattachementRO);
    compare.append(periode, o.periode);
    return compare.toComparison();
  }
}
