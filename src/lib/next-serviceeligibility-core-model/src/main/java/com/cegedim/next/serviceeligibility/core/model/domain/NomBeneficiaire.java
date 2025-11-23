package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Nom */
@Data
public class NomBeneficiaire implements GenericDomain<NomBeneficiaire> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String nomFamille;
  private String nomUsage;
  private String prenom;
  private String civilite;

  public NomBeneficiaire() {
    /* empty constructor */ }

  public NomBeneficiaire(NomBeneficiaire source) {
    this.nomFamille = source.getNomFamille();
    this.nomUsage = source.getNomUsage();
    this.prenom = source.getPrenom();
    this.civilite = source.getCivilite();
  }

  @Override
  public int compareTo(final NomBeneficiaire nom) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.nomFamille, nom.nomFamille);
    compareToBuilder.append(this.nomUsage, nom.nomUsage);
    compareToBuilder.append(this.prenom, nom.prenom);
    compareToBuilder.append(this.civilite, nom.civilite);
    return compareToBuilder.toComparison();
  }

  public void setNomFamille(String newNomFamille) {
    if (newNomFamille != null) {
      this.nomFamille = newNomFamille;
    }
  }

  public void setNomUsage(String newNomUsage) {
    if (newNomUsage != null) {
      this.nomUsage = newNomUsage;
    }
  }

  public void setPrenom(String newPrenom) {
    if (newPrenom != null) {
      this.prenom = newPrenom;
    }
  }

  public void setCivilite(String newCivilite) {
    if (newCivilite != null) {
      this.civilite = newCivilite;
    }
  }
}
