package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

public class DomaineDroitDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 3904256197673059177L;

  private String code;
  private String formulaMask;
  private String garantie;
  private String garantieLibelle;
  private String priorite;
  private String periodeDebut;
  private String periodeFin;

  private String periodeOfflineFin;
  private String periodeOnlineDebut;
  private String periodeOnlineFin;
  private String periodeFermetureDebut;
  private String periodeFermetureFin;
  private String tauxRemboursement;
  private String uniteTaux;
  private CouvertureDto couverture;
  private List<ConventionDto> conventions;
  private PrioritesDto priorites;
  private Integer nbPrestations;
  private List<PrestationDto> prestations;

  private String naturePrestation;
  private String naturePrestationOnline;

  @JsonIgnore private Date periodeDebutDate;
  @JsonIgnore private Date periodeFinDate;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFormulaMask() {
    return formulaMask;
  }

  public void setFormulaMask(String formulaMask) {
    this.formulaMask = formulaMask;
  }

  public String getGarantie() {
    return garantie;
  }

  public String getPeriodeOnlineDebut() {
    return periodeOnlineDebut;
  }

  public void setPeriodeOnlineDebut(String periodeOnlineDebut) {
    this.periodeOnlineDebut = periodeOnlineDebut;
  }

  public String getPeriodeOnlineFin() {
    return periodeOnlineFin;
  }

  public void setPeriodeOnlineFin(String periodeOnlineFin) {
    this.periodeOnlineFin = periodeOnlineFin;
  }

  public void setGarantie(String garantie) {
    this.garantie = garantie;
  }

  public String getGarantieLibelle() {
    return garantieLibelle;
  }

  public void setGarantieLibelle(String garantieLibelle) {
    this.garantieLibelle = garantieLibelle;
  }

  public String getPriorite() {
    return priorite;
  }

  public void setPriorite(String priorite) {
    this.priorite = priorite;
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

  public String getPeriodeFermetureDebut() {
    return periodeFermetureDebut;
  }

  public void setPeriodeFermetureDebut(String periodeFermetureDebut) {
    this.periodeFermetureDebut = periodeFermetureDebut;
  }

  public String getPeriodeFermetureFin() {
    return periodeFermetureFin;
  }

  public void setPeriodeFermetureFin(String periodeFermetureFin) {
    this.periodeFermetureFin = periodeFermetureFin;
  }

  public String getTauxRemboursement() {
    return tauxRemboursement;
  }

  public void setTauxRemboursement(String tauxRemboursement) {
    this.tauxRemboursement = tauxRemboursement;
  }

  public String getUniteTaux() {
    return uniteTaux;
  }

  public void setUniteTaux(String uniteTaux) {
    this.uniteTaux = uniteTaux;
  }

  public CouvertureDto getCouverture() {
    return couverture;
  }

  public void setCouverture(CouvertureDto couverture) {
    this.couverture = couverture;
  }

  public List<ConventionDto> getConventions() {
    return conventions;
  }

  public void setConventions(List<ConventionDto> conventions) {
    this.conventions = conventions;
  }

  public PrioritesDto getPriorites() {
    return priorites;
  }

  public void setPriorites(PrioritesDto priorites) {
    this.priorites = priorites;
  }

  public Date getPeriodeDebutDate() {
    return periodeDebutDate;
  }

  public void setPeriodeDebutDate(Date periodeDebutDate) {
    this.periodeDebutDate = periodeDebutDate;
  }

  public Date getPeriodeFinDate() {
    return periodeFinDate;
  }

  public void setPeriodeFinDate(Date periodeFinDate) {
    this.periodeFinDate = periodeFinDate;
  }

  public Integer getNbPrestations() {
    return nbPrestations;
  }

  public void setNbPrestations(Integer nbPrestations) {
    this.nbPrestations = nbPrestations;
  }

  public List<PrestationDto> getPrestations() {
    return prestations;
  }

  public void setPrestations(List<PrestationDto> prestations) {
    this.prestations = prestations;
  }

  public String getNaturePrestation() {
    return naturePrestation;
  }

  public void setNaturePrestation(String naturePrestation) {
    this.naturePrestation = naturePrestation;
  }

  public String getNaturePrestationOnline() {
    return naturePrestationOnline;
  }

  public void setNaturePrestationOnline(String naturePrestationOnline) {
    this.naturePrestationOnline = naturePrestationOnline;
  }

  public String getPeriodeOfflineFin() {
    return periodeOfflineFin;
  }

  public void setPeriodeOfflineFin(String periodeOfflineFin) {
    this.periodeOfflineFin = periodeOfflineFin;
  }
}
