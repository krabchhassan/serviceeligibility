package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class AmcExclues implements GenericDomain<AmcExclues> {

  /* PROPRIETES */

  private String idDeclarant;
  private String idDerniereDeclaration;
  private boolean reprise = false;

  @Override
  public int compareTo(AmcExclues amcExclues) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, amcExclues.idDeclarant);
    compareToBuilder.append(this.idDerniereDeclaration, amcExclues.idDerniereDeclaration);
    compareToBuilder.append(this.reprise, amcExclues.reprise);
    return compareToBuilder.toComparison();
  }
}
