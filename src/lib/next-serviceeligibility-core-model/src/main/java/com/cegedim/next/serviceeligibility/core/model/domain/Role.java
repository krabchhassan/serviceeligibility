package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class Role implements GenericDomain<Role> {

  private static final long serialVersionUID = 1L;

  private String code;
  private String libelle;
  private int valeur;

  @Override
  public int compareTo(Role role) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, role.code);
    compareToBuilder.append(this.libelle, role.libelle);
    compareToBuilder.append(this.valeur, role.valeur);
    return compareToBuilder.toComparison();
  }
}
