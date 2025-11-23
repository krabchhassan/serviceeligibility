package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class FondCarteTP implements GenericDomain<FondCarteTP> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String reseauSoin;
  private String fondCarte;
  private LocalDateTime dateDebut;
  private LocalDateTime dateFin;

  @Override
  public int compareTo(FondCarteTP o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.reseauSoin, o.reseauSoin);
    compareToBuilder.append(this.fondCarte, o.fondCarte);
    compareToBuilder.append(this.dateDebut, o.dateDebut);
    compareToBuilder.append(this.dateFin, o.dateFin);
    return compareToBuilder.toComparison();
  }

  @Override
  public FondCarteTP clone() throws CloneNotSupportedException {
    return (FondCarteTP) super.clone();
  }
}
