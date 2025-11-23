package com.cegedim.next.serviceeligibility.core.model.domain.benef;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodePeriodeDeclaration implements GenericDomain<CodePeriodeDeclaration> {
  private String code;
  private PeriodeComparable periode;

  public CodePeriodeDeclaration(CodePeriodeDeclaration source) {
    this.code = source.getCode();
    this.periode = new PeriodeComparable(source.getPeriode());
  }

  @Override
  public int compareTo(CodePeriodeDeclaration o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(code, o.code);
    compare.append(periode, o.periode);
    return compare.toComparison();
  }
}
