package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe le document Affiliation */
@Data
public class Affiliation implements GenericDomain<Affiliation> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String nom;
  private String nomPatronymique;
  private String nomMarital;
  private String prenom;
  private String civilite;
  private String periodeDebut;
  private String periodeFin;
  private String qualite;
  private String regimeOD1 = "00";
  private String caisseOD1 = "000";
  private String centreOD1;
  private String regimeOD2;
  private String caisseOD2;
  private String centreOD2;
  private Boolean hasMedecinTraitant;
  private String regimeParticulier;
  private Boolean isBeneficiaireACS = false;
  private Boolean isTeleTransmission;
  private String typeAssure;

  public Affiliation() {
    /* empty constructor */ }

  public Affiliation(Affiliation source) {
    this.nom = source.getNom();
    this.nomPatronymique = source.getNomPatronymique();
    this.nomMarital = source.getNomMarital();
    this.prenom = source.getPrenom();
    this.civilite = source.getCivilite();
    this.periodeDebut = source.getPeriodeDebut();
    this.periodeFin = source.getPeriodeFin();
    this.qualite = source.getQualite();
    this.regimeOD1 = source.getRegimeOD1();
    this.caisseOD1 = source.getCaisseOD1();
    this.centreOD1 = source.getCentreOD1();
    this.regimeOD2 = source.getRegimeOD2();
    this.caisseOD2 = source.getCaisseOD2();
    this.centreOD2 = source.getCentreOD2();
    this.hasMedecinTraitant = source.getHasMedecinTraitant();
    this.regimeParticulier = source.getRegimeParticulier();
    this.isBeneficiaireACS = source.getIsBeneficiaireACS();
    this.isTeleTransmission = source.getIsTeleTransmission();
    this.typeAssure = source.getTypeAssure();
  }

  @Override
  public int compareTo(final Affiliation affiliation) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.caisseOD1, affiliation.caisseOD1);
    compareToBuilder.append(this.caisseOD2, affiliation.caisseOD2);
    compareToBuilder.append(this.centreOD1, affiliation.centreOD1);
    compareToBuilder.append(this.centreOD2, affiliation.centreOD2);
    compareToBuilder.append(this.civilite, affiliation.civilite);
    compareToBuilder.append(this.isBeneficiaireACS, affiliation.isBeneficiaireACS);
    compareToBuilder.append(this.hasMedecinTraitant, affiliation.hasMedecinTraitant);
    compareToBuilder.append(this.nom, affiliation.nom);
    compareToBuilder.append(this.nomMarital, affiliation.nomMarital);
    compareToBuilder.append(this.nomPatronymique, affiliation.nomPatronymique);
    compareToBuilder.append(this.periodeDebut, affiliation.periodeDebut);
    compareToBuilder.append(this.periodeFin, affiliation.periodeFin);
    compareToBuilder.append(this.prenom, affiliation.prenom);
    compareToBuilder.append(this.qualite, affiliation.qualite);
    compareToBuilder.append(this.regimeOD1, affiliation.regimeOD1);
    compareToBuilder.append(this.regimeOD2, affiliation.regimeOD2);
    compareToBuilder.append(this.regimeParticulier, affiliation.regimeParticulier);
    compareToBuilder.append(this.isTeleTransmission, affiliation.isTeleTransmission);
    compareToBuilder.append(this.typeAssure, affiliation.typeAssure);
    return compareToBuilder.toComparison();
  }
}
