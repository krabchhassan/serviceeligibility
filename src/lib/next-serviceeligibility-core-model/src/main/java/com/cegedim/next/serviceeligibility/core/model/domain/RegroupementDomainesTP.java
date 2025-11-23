package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class RegroupementDomainesTP implements GenericDomain<RegroupementDomainesTP> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String domaineRegroupementTP;
  private List<String> codesDomainesTP;
  private boolean niveauRemboursementIdentique;
  private LocalDateTime dateDebut;
  private LocalDateTime dateFin;

  @Override
  public int compareTo(RegroupementDomainesTP o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.domaineRegroupementTP, o.domaineRegroupementTP);
    compareToBuilder.append(this.codesDomainesTP, o.codesDomainesTP);
    compareToBuilder.append(this.niveauRemboursementIdentique, o.niveauRemboursementIdentique);
    compareToBuilder.append(this.dateDebut, o.dateDebut);
    compareToBuilder.append(this.dateFin, o.dateFin);
    return compareToBuilder.toComparison();
  }

  @Override
  public RegroupementDomainesTP clone() throws CloneNotSupportedException {
    return (RegroupementDomainesTP) super.clone();
  }
}
