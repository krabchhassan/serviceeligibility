package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class TypeAdresse implements GenericDomain<TypeAdresse> {

  private static final long serialVersionUID = 1L;

  private String libelle;
  private String type;

  public TypeAdresse() {
    /* empty constructor */ }

  public TypeAdresse(TypeAdresse source) {
    this.libelle = source.getLibelle();
    this.type = source.getType();
  }

  @Override
  public int compareTo(final TypeAdresse typeAdresse) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.libelle, typeAdresse.libelle);
    compareToBuilder.append(this.type, typeAdresse.type);
    return compareToBuilder.toComparison();
  }

  public void setLibelle(String newLibelle) {
    if (newLibelle != null) {
      this.libelle = newLibelle;
    }
  }

  public void setType(String newType) {
    if (newType != null) {
      this.type = newType;
    }
  }
}
