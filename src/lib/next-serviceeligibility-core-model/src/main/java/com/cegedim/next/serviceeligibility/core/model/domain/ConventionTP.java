package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class ConventionTP implements GenericDomain<ConventionTP> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String reseauSoin;
  private String domaineTP;
  private String conventionCible;
  private boolean concatenation;
  private LocalDateTime dateDebut;
  private LocalDateTime dateFin;
  private boolean isConventionConcatenable;

  @Override
  public int compareTo(ConventionTP o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.reseauSoin, o.reseauSoin);
    compareToBuilder.append(this.domaineTP, o.domaineTP);
    compareToBuilder.append(this.conventionCible, o.conventionCible);
    compareToBuilder.append(this.concatenation, o.concatenation);
    compareToBuilder.append(this.dateDebut, o.dateDebut);
    compareToBuilder.append(this.dateFin, o.dateFin);
    return compareToBuilder.toComparison();
  }

  @Override
  public ConventionTP clone() throws CloneNotSupportedException {
    return (ConventionTP) super.clone();
  }
}
