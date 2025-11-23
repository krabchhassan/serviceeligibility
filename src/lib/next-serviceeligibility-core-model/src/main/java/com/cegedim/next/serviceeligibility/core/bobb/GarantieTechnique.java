package com.cegedim.next.serviceeligibility.core.bobb;

import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Classe POJO de vue partielle d'un {@link ContractElement} (on ne garde que le code assureur et le
 * code garantie)
 */
@Data
public class GarantieTechnique implements Comparable<GarantieTechnique>, Serializable {
  private String codeAssureur;
  private String codeGarantie;
  private String dateAjout;
  private String dateSuppressionLogique;
  private String priorite;

  public GarantieTechnique() {}

  public GarantieTechnique(
      String codeAssureur,
      String codeGarantie,
      String dateAjout,
      String dateSuppressionLogique,
      String priorite) {
    this.codeAssureur = codeAssureur;
    this.codeGarantie = codeGarantie;
    this.dateAjout = dateAjout;
    this.dateSuppressionLogique = dateSuppressionLogique;
    this.priorite = priorite;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GarantieTechnique that = (GarantieTechnique) o;
    return codeGarantie.equals(that.codeGarantie) && codeAssureur.equals(that.codeAssureur);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codeGarantie == null) ? 0 : codeGarantie.hashCode());
    result = prime * result + ((codeAssureur == null) ? 0 : codeAssureur.hashCode());
    return result;
  }

  @Override
  public int compareTo(GarantieTechnique gt) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.priorite, gt.priorite);
    compareToBuilder.append(this.codeGarantie, gt.codeGarantie);
    compareToBuilder.append(this.codeAssureur, gt.codeAssureur);
    return compareToBuilder.toComparison();
  }
}
