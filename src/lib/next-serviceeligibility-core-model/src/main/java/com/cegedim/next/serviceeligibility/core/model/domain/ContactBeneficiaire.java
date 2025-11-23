package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Contact */
@Data
public class ContactBeneficiaire implements GenericDomain<ContactBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String fixe;
  private String mobile;
  private String email;

  public ContactBeneficiaire() {
    /* empty constructor */ }

  public ContactBeneficiaire(ContactBeneficiaire source) {
    this.fixe = source.getFixe();
    this.mobile = source.getMobile();
    this.email = source.getEmail();
  }

  @Override
  public int compareTo(final ContactBeneficiaire rib) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.fixe, rib.fixe);
    compareToBuilder.append(this.mobile, rib.mobile);
    compareToBuilder.append(this.email, rib.email);
    return compareToBuilder.toComparison();
  }

  public void setFixe(String newFixe) {
    if (newFixe != null) {
      this.fixe = newFixe;
    }
  }

  public void setMobile(String newMobile) {
    if (newMobile != null) {
      this.mobile = newMobile;
    }
  }

  public void setEmail(String newEmail) {
    if (newEmail != null) {
      this.email = newEmail;
    }
  }
}
