package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.Mergeable;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Conventionnement */
@Data
public class Conventionnement implements GenericDomain<Conventionnement>, Mergeable {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private Integer priorite;

  /* DOCUMENTS EMBEDDED */
  private TypeConventionnement typeConventionnement;

  public Conventionnement() {
    /* empty constructor */ }

  public Conventionnement(Conventionnement source) {
    this.priorite = source.getPriorite();

    if (source.getTypeConventionnement() != null) {
      this.typeConventionnement = new TypeConventionnement(source.getTypeConventionnement());
    }
  }

  @Override
  public int compareTo(final Conventionnement conventionnement) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.priorite, conventionnement.priorite);
    compareToBuilder.append(this.typeConventionnement, conventionnement.typeConventionnement);
    return compareToBuilder.toComparison();
  }

  @Override
  public String mergeKey() {
    return "" + priorite + typeConventionnement;
  }

  @Override
  public String conflictKey() {
    return "" + priorite;
  }
}
