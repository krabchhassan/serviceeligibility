package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Classe DTO de l'info pilotage. */
@JsonInclude(Include.NON_NULL)
public class InfoPilotageDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String critereRegroupement;
  private String critereRegroupementDetaille;
  private String dateOuverture;
  private String dateSynchronisation;
  private Date dateTimeOuverture;
  private Date dateTimeSynchronisation;
  private String couloirClient;
  private String typeConventionnement;
  private Integer numDebutFichier;
  private String numEmetteur;
  private String numClient;
  private String nomClient;
  private String versionNorme;
  private String typeFichier;
  private String codePerimetre;
  private String nomPerimetre;
  private String typeGestionnaireBO;
  private String libelleGestionnaireBO;
  private String codeClient;
  private Boolean numExterneContratIndividuel;
  private Boolean numExterneContratCollectif;
  private String codeComptable;
  private String periodeReferenceDebut;
  private String periodeReferenceFin;
  private Date dateTimePeriodeReferenceDebut;
  private Date dateTimePeriodeReferenceFin;
  private Boolean filtreDomaine;
  private Boolean generateFichier;
  private Integer dureeValidite;
  private String periodeValidite;

  /* DOCUMENTS EMBEDDED */

  private List<ValiditeDomainesDroitsDto> validitesDomainesDroits;

  /* GETTERS SETTERS */

  public Integer getNumDebutFichier() {
    return numDebutFichier;
  }

  public void setNumDebutFichier(Integer numDebutFichier) {
    this.numDebutFichier = numDebutFichier;
  }

  public String getNumEmetteur() {
    return numEmetteur;
  }

  public void setNumEmetteur(String numEmetteur) {
    this.numEmetteur = numEmetteur;
  }

  public String getNumClient() {
    return numClient;
  }

  public void setNumClient(String numClient) {
    this.numClient = numClient;
  }

  public String getNomClient() {
    return nomClient;
  }

  public void setNomClient(String nomClient) {
    this.nomClient = nomClient;
  }

  public String getVersionNorme() {
    return versionNorme;
  }

  public void setVersionNorme(String versionNorme) {
    this.versionNorme = versionNorme;
  }

  public String getTypeFichier() {
    return typeFichier;
  }

  public void setTypeFichier(String typeFichier) {
    this.typeFichier = typeFichier;
  }

  public String getCodePerimetre() {
    return codePerimetre;
  }

  public void setCodePerimetre(String codePerimetre) {
    this.codePerimetre = codePerimetre;
  }

  public String getNomPerimetre() {
    return nomPerimetre;
  }

  public void setNomPerimetre(String nomPerimetre) {
    this.nomPerimetre = nomPerimetre;
  }

  public String getTypeGestionnaireBO() {
    return typeGestionnaireBO;
  }

  public void setTypeGestionnaireBO(String typeGestionnaireBO) {
    this.typeGestionnaireBO = typeGestionnaireBO;
  }

  public String getLibelleGestionnaireBO() {
    return libelleGestionnaireBO;
  }

  public void setLibelleGestionnaireBO(String libelleGestionnaireBO) {
    this.libelleGestionnaireBO = libelleGestionnaireBO;
  }

  public String getCodeClient() {
    return codeClient;
  }

  public void setCodeClient(String codeClient) {
    this.codeClient = codeClient;
  }

  public Boolean getNumExterneContratIndividuel() {
    return numExterneContratIndividuel;
  }

  public void setNumExterneContratIndividuel(Boolean numExterneContratIndividuel) {
    this.numExterneContratIndividuel = numExterneContratIndividuel;
  }

  public Boolean getNumExterneContratCollectif() {
    return numExterneContratCollectif;
  }

  public void setNumExterneContratCollectif(Boolean numExterneContratCollectif) {
    this.numExterneContratCollectif = numExterneContratCollectif;
  }

  public String getCritereRegroupement() {
    return critereRegroupement;
  }

  public void setCritereRegroupement(String critereRegroupement) {
    this.critereRegroupement = critereRegroupement;
  }

  public String getCritereRegroupementDetaille() {
    return critereRegroupementDetaille;
  }

  public void setCritereRegroupementDetaille(String critereRegroupementDetaille) {
    this.critereRegroupementDetaille = critereRegroupementDetaille;
  }

  public String getDateOuverture() {
    return dateOuverture;
  }

  public void setDateOuverture(String dateOuverture) {
    this.dateOuverture = dateOuverture;
  }

  public String getDateSynchronisation() {
    return dateSynchronisation;
  }

  public void setDateSynchronisation(String dateSynchronisation) {
    this.dateSynchronisation = dateSynchronisation;
  }

  @JsonIgnore
  public Date getDateTimeOuverture() {
    return dateTimeOuverture;
  }

  @JsonIgnore
  public void setDateTimeOuverture(Date dateTimeOuverture) {
    this.dateTimeOuverture = dateTimeOuverture;
  }

  @JsonIgnore
  public Date getDateTimeSynchronisation() {
    return dateTimeSynchronisation;
  }

  @JsonIgnore
  public void setDateTimeSynchronisation(Date dateTimeSynchronisation) {
    this.dateTimeSynchronisation = dateTimeSynchronisation;
  }

  public String getCouloirClient() {
    return couloirClient;
  }

  public void setCouloirClient(String couloirClient) {
    this.couloirClient = couloirClient;
  }

  public String getTypeConventionnement() {
    return typeConventionnement;
  }

  public void setTypeConventionnement(String typeConventionnement) {
    this.typeConventionnement = typeConventionnement;
  }

  public String getCodeComptable() {
    return codeComptable;
  }

  public void setCodeComptable(String codeComptable) {
    this.codeComptable = codeComptable;
  }

  public String getPeriodeReferenceDebut() {
    return periodeReferenceDebut;
  }

  public void setPeriodeReferenceDebut(String periodeReferenceDebut) {
    this.periodeReferenceDebut = periodeReferenceDebut;
  }

  public String getPeriodeReferenceFin() {
    return periodeReferenceFin;
  }

  public void setPeriodeReferenceFin(String periodeReferenceFin) {
    this.periodeReferenceFin = periodeReferenceFin;
  }

  @JsonIgnore
  public Date getDateTimePeriodeReferenceDebut() {
    return dateTimePeriodeReferenceDebut;
  }

  @JsonIgnore
  public void setDateTimePeriodeReferenceDebut(Date dateTimePeriodeReferenceDebut) {
    this.dateTimePeriodeReferenceDebut = dateTimePeriodeReferenceDebut;
  }

  @JsonIgnore
  public Date getDateTimePeriodeReferenceFin() {
    return dateTimePeriodeReferenceFin;
  }

  @JsonIgnore
  public void setDateTimePeriodeReferenceFin(Date dateTimePeriodeReferenceFin) {
    this.dateTimePeriodeReferenceFin = dateTimePeriodeReferenceFin;
  }

  public Boolean getFiltreDomaine() {
    return filtreDomaine;
  }

  public void setFiltreDomaine(Boolean filtreDomaine) {
    this.filtreDomaine = filtreDomaine;
  }

  public Boolean getGenerateFichier() {
    return generateFichier;
  }

  public void setGenerateFichier(Boolean generateFichier) {
    this.generateFichier = generateFichier;
  }

  public Integer getDureeValidite() {
    return dureeValidite;
  }

  public void setDureeValidite(Integer dureeValidite) {
    this.dureeValidite = dureeValidite;
  }

  public String getPeriodeValidite() {
    return periodeValidite;
  }

  public void setPeriodeValidite(String periodeValidite) {
    this.periodeValidite = periodeValidite;
  }

  public List<ValiditeDomainesDroitsDto> getValiditesDomainesDroits() {
    if (this.validitesDomainesDroits == null) {
      this.validitesDomainesDroits = new ArrayList<>();
    }
    return this.validitesDomainesDroits;
  }

  public void setValiditesDomainesDroits(List<ValiditeDomainesDroitsDto> validitesDomainesDroits) {
    this.validitesDomainesDroits = validitesDomainesDroits;
  }
}
