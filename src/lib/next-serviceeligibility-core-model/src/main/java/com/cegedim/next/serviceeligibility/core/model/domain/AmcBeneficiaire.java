package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Amc */
@Data
public class AmcBeneficiaire implements GenericDomain<AmcBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String idDeclarant;

  @Override
  public int compareTo(final AmcBeneficiaire amc) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, amc.idDeclarant);
    return compareToBuilder.toComparison();
  }

  public void setIdDeclarant(String newIdDeclarant) {
    if (newIdDeclarant != null) {
      this.idDeclarant = newIdDeclarant;
    }
  }
}
