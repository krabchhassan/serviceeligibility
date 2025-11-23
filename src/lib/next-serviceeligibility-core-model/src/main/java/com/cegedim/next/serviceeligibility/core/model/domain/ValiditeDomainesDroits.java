package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class ValiditeDomainesDroits implements GenericDomain<ValiditeDomainesDroits> {
  private String codeDomaine;
  private int duree;
  private UniteDomainesDroitsEnum unite;

  // Optionnal if unite != Mois
  private boolean positionnerFinDeMois;

  public ValiditeDomainesDroits() {
    /* empty constructor */ }

  public ValiditeDomainesDroits(ValiditeDomainesDroits source) {
    this.codeDomaine = source.getCodeDomaine();
    this.duree = source.getDuree();
    this.unite = source.getUnite();
    this.positionnerFinDeMois = source.isPositionnerFinDeMois();
  }

  @Override
  public int compareTo(ValiditeDomainesDroits validiteDomainesDroits) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codeDomaine, validiteDomainesDroits.codeDomaine);
    compareToBuilder.append(this.duree, validiteDomainesDroits.duree);
    compareToBuilder.append(this.unite, validiteDomainesDroits.unite);
    compareToBuilder.append(this.positionnerFinDeMois, validiteDomainesDroits.positionnerFinDeMois);
    return compareToBuilder.toComparison();
  }
}
