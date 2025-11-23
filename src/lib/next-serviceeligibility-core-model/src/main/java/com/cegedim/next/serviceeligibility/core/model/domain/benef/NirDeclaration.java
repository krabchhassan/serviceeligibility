package com.cegedim.next.serviceeligibility.core.model.domain.benef;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NirDeclaration implements GenericDomain<NirDeclaration> {
  private String code;
  private String cle;

  public NirDeclaration(NirDeclaration source) {
    this.code = source.code;
    this.cle = source.cle;
  }

  @Override
  public int compareTo(NirDeclaration o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(code, o.code);
    compare.append(cle, o.cle);
    return compare.toComparison();
  }
}
