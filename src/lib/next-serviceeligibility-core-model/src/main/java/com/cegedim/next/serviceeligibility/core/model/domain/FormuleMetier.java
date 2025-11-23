package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
@EqualsAndHashCode
public class FormuleMetier implements GenericDomain<FormuleMetier> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;

  public FormuleMetier() {
    /* empty constructor */ }

  public FormuleMetier(FormuleMetier source) {
    this.code = source.getCode();
    this.libelle = source.getLibelle();
  }

  @Override
  public int compareTo(final FormuleMetier formuleMetier) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, formuleMetier.code);
    compareToBuilder.append(this.libelle, formuleMetier.libelle);
    return compareToBuilder.toComparison();
  }
}
