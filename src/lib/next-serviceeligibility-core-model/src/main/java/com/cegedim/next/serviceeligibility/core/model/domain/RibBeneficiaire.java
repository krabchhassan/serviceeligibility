package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Rib */
@Data
public class RibBeneficiaire implements GenericDomain<RibBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String bic;
  private String iban;

  public RibBeneficiaire() {
    /* empty constructor */ }

  public RibBeneficiaire(RibBeneficiaire source) {
    this.bic = source.getBic();
    this.iban = source.getIban();
  }

  /* METHODS */

  @Override
  public int compareTo(final RibBeneficiaire rib) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.bic, rib.bic);
    compareToBuilder.append(this.iban, rib.iban);
    return compareToBuilder.toComparison();
  }

  public void setBic(String newBic) {
    if (newBic != null) {
      this.bic = newBic;
    }
  }

  public void setIban(String newIban) {
    if (newIban != null) {
      this.iban = newIban;
    }
  }
}
