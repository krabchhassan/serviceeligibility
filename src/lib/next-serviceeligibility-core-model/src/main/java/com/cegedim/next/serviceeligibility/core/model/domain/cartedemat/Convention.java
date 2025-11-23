package com.cegedim.next.serviceeligibility.core.model.domain.cartedemat;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Convention */
@Data
public class Convention implements GenericDomain<Convention> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private Integer priorite;
  private String code;

  @Override
  public int compareTo(final Convention convention) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.priorite, convention.priorite);
    compareToBuilder.append(this.code, convention.code);
    return compareToBuilder.toComparison();
  }
}
