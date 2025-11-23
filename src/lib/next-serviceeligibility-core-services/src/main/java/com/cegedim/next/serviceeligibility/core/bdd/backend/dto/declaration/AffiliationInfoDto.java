package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

/** Classe DTO de l'entite {@code HistoriqueAffiliatio}. */
public class AffiliationInfoDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  private String nirBeneficiaire;

  private String cleNirBeneficiaire;

  private String nirOd1;

  private String cleNirOd1;

  private String nirOd2;

  private String cleNirOd2;

  private String periodeDebut;

  private String periodeFin;

  private String regimeOD1;

  private String caisseOD1;

  private String centreOD1;

  private String regimeOD2;

  private String caisseOD2;

  private String centreOD2;

  private String qualite;

  private String regimeParticulier;

  public String getNirBeneficiaire() {
    return nirBeneficiaire;
  }

  public void setNirBeneficiaire(String nirBeneficiaire) {
    this.nirBeneficiaire = nirBeneficiaire;
  }

  public String getCleNirBeneficiaire() {
    return cleNirBeneficiaire;
  }

  public void setCleNirBeneficiaire(String cleNirBeneficiaire) {
    this.cleNirBeneficiaire = cleNirBeneficiaire;
  }

  public String getNirOd1() {
    return nirOd1;
  }

  public void setNirOd1(String nirOd1) {
    this.nirOd1 = nirOd1;
  }

  public String getCleNirOd1() {
    return cleNirOd1;
  }

  public void setCleNirOd1(String cleNirOd1) {
    this.cleNirOd1 = cleNirOd1;
  }

  public String getNirOd2() {
    return nirOd2;
  }

  public void setNirOd2(String nirOd2) {
    this.nirOd2 = nirOd2;
  }

  public String getCleNirOd2() {
    return cleNirOd2;
  }

  public void setCleNirOd2(String cleNirOd2) {
    this.cleNirOd2 = cleNirOd2;
  }

  public String getPeriodeDebut() {
    return periodeDebut;
  }

  public void setPeriodeDebut(String periodeDebut) {
    this.periodeDebut = periodeDebut;
  }

  public String getPeriodeFin() {
    return periodeFin;
  }

  public void setPeriodeFin(String periodeFin) {
    this.periodeFin = periodeFin;
  }

  public String getRegimeOD1() {
    return regimeOD1;
  }

  public void setRegimeOD1(String regimeOD1) {
    this.regimeOD1 = regimeOD1;
  }

  public String getCaisseOD1() {
    return caisseOD1;
  }

  public void setCaisseOD1(String caisseOD1) {
    this.caisseOD1 = caisseOD1;
  }

  public String getCentreOD1() {
    return centreOD1;
  }

  public void setCentreOD1(String centreOD1) {
    this.centreOD1 = centreOD1;
  }

  public String getRegimeOD2() {
    return regimeOD2;
  }

  public void setRegimeOD2(String regimeOD2) {
    this.regimeOD2 = regimeOD2;
  }

  public String getCaisseOD2() {
    return caisseOD2;
  }

  public void setCaisseOD2(String caisseOD2) {
    this.caisseOD2 = caisseOD2;
  }

  public String getCentreOD2() {
    return centreOD2;
  }

  public void setCentreOD2(String centreOD2) {
    this.centreOD2 = centreOD2;
  }

  public String getQualite() {
    return qualite;
  }

  public void setQualite(String qualite) {
    this.qualite = qualite;
  }

  public String getRegimeParticulier() {
    return regimeParticulier;
  }

  public void setRegimeParticulier(String regimeParticulier) {
    this.regimeParticulier = regimeParticulier;
  }
}
