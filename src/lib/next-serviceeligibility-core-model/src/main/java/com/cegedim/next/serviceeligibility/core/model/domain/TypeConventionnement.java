package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TypeConventionnement implements GenericDomain<TypeConventionnement> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;

  private String libelle;

  public TypeConventionnement() {
    /* empty constructor */ }

  public TypeConventionnement(TypeConventionnement source) {
    this.code = source.getCode();
    this.libelle = source.getLibelle();
  }

  @Override
  public int compareTo(final TypeConventionnement typeConventionnement) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, typeConventionnement.code);
    compareToBuilder.append(this.libelle, typeConventionnement.libelle);
    return compareToBuilder.toComparison();
  }
}
