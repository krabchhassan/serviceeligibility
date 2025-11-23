package com.cegedim.next.serviceeligibility.core.model.domain.benef;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RattachementRODeclaration implements GenericDomain<RattachementRODeclaration> {
  private String codeRegime;
  private String codeCaisse;
  private String codeCentre;

  public RattachementRODeclaration(RattachementRODeclaration source) {
    this.codeRegime = source.getCodeRegime();
    this.codeCaisse = source.getCodeCaisse();
    this.codeCentre = source.getCodeCentre();
  }

  @Override
  public int compareTo(RattachementRODeclaration o) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(codeRegime, o.codeRegime);
    compare.append(codeCaisse, o.codeCaisse);
    compare.append(codeCentre, o.codeCentre);
    return compare.toComparison();
  }
}
