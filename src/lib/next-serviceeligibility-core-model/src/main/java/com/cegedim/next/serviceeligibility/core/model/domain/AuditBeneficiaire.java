package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Audit */
@Data
public class AuditBeneficiaire implements GenericDomain<AuditBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String dateEmission;

  @Override
  public int compareTo(final AuditBeneficiaire audit) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateEmission, audit.dateEmission);
    return compareToBuilder.toComparison();
  }

  public void setDateEmission(String newDateEmission) {
    if (newDateEmission != null) {
      this.dateEmission = newDateEmission;
    }
  }
}
