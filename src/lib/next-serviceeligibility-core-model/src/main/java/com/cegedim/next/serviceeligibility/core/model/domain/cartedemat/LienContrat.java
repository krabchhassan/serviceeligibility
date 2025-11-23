package com.cegedim.next.serviceeligibility.core.model.domain.cartedemat;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document LienContrat */
@Data
@NoArgsConstructor
public class LienContrat implements GenericDomain<LienContrat> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String lienFamilial;
  private String rangAdministratif;
  private String modePaiementPrestations;

  public LienContrat(LienContrat source) {
    this.lienFamilial = source.getLienFamilial();
    this.rangAdministratif = source.getRangAdministratif();
    this.modePaiementPrestations = source.getModePaiementPrestations();
  }

  @Override
  public int compareTo(LienContrat lien) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.lienFamilial, lien.lienFamilial);
    compareToBuilder.append(this.rangAdministratif, lien.rangAdministratif);
    compareToBuilder.append(this.modePaiementPrestations, lien.modePaiementPrestations);
    return compareToBuilder.toComparison();
  }
}
