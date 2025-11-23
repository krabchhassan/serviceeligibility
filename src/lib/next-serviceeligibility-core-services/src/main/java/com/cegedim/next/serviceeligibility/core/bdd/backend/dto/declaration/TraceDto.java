package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

/** Classe DTO de l'entite {@code Traces}. */
public class TraceDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 7102826496273049620L;

  private String motifDeclaration;

  private String typeMouvement;

  private String dateTraitement;

  private String codeEtat;

  private String nomFichier;

  private String codePartenaire;

  private String operateurPrincipal;

  private String backOfficeEmetteur;

  private String codeCircuit;

  private Boolean allServiceValide;

  private List<TraceServiceDto> services;

  private String versionTDB;

  public String getDateTraitement() {
    return dateTraitement;
  }

  public void setDateTraitement(String dateTraitement) {
    this.dateTraitement = dateTraitement;
  }

  public String getCodeEtat() {
    return codeEtat;
  }

  public void setCodeEtat(String codeEtat) {
    this.codeEtat = codeEtat;
  }

  public String getNomFichier() {
    return nomFichier;
  }

  public void setNomFichier(String nomFichier) {
    this.nomFichier = nomFichier;
  }

  public String getCodePartenaire() {
    return codePartenaire;
  }

  public void setCodePartenaire(String codePartenaire) {
    this.codePartenaire = codePartenaire;
  }

  public String getOperateurPrincipal() {
    return operateurPrincipal;
  }

  public void setOperateurPrincipal(String operateurPrincipal) {
    this.operateurPrincipal = operateurPrincipal;
  }

  public String getBackOfficeEmetteur() {
    return backOfficeEmetteur;
  }

  public void setBackOfficeEmetteur(String backOfficeEmetteur) {
    this.backOfficeEmetteur = backOfficeEmetteur;
  }

  public String getCodeCircuit() {
    return codeCircuit;
  }

  public void setCodeCircuit(String codeCircuit) {
    this.codeCircuit = codeCircuit;
  }

  public String getMotifDeclaration() {
    return motifDeclaration;
  }

  public void setMotifDeclaration(String motifDeclaration) {
    this.motifDeclaration = motifDeclaration;
  }

  public String getTypeMouvement() {
    return typeMouvement;
  }

  public void setTypeMouvement(String typeMouvement) {
    this.typeMouvement = typeMouvement;
  }

  public List<TraceServiceDto> getTracesServices() {
    return services;
  }

  public void setTracesServices(List<TraceServiceDto> services) {
    this.services = services;
  }

  public Boolean getAllServiceValide() {
    return allServiceValide;
  }

  public void setAllServiceValide(Boolean allServiceValide) {
    this.allServiceValide = allServiceValide;
  }

  public String getVersionTDB() {
    return versionTDB;
  }

  public void setVersionTDB(String versionTDB) {
    this.versionTDB = versionTDB;
  }
}
