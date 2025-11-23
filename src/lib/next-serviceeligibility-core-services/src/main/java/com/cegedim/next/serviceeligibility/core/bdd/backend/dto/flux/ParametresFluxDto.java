package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

/**
 * Contient les données d'une requete Flux.
 *
 * @author cgd
 */
public class ParametresFluxDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String amc;

  private String nomAMC;

  private String codePartenaire;

  private String codeCircuit;

  private String dateDebut;

  private String dateFin;

  private String emetteur;

  private String numeroFichier;

  private String processus;

  private String typeFichier;

  private boolean isFichierEmis;

  private String nomFichier;

  private boolean isNewSearch;

  private String position;

  private String numberByPage;

  public String getAmc() {
    return amc;
  }

  public void setAmc(String amc) {
    this.amc = amc;
  }

  public String getNomAMC() {
    return nomAMC;
  }

  public void setNomAMC(String nomAMC) {
    this.nomAMC = nomAMC;
  }

  public String getCodePartenaire() {
    return codePartenaire;
  }

  public void setCodePartenaire(String codePartenaire) {
    this.codePartenaire = codePartenaire;
  }

  public String getCodeCircuit() {
    return codeCircuit;
  }

  public void setCodeCircuit(String codeCircuit) {
    this.codeCircuit = codeCircuit;
  }

  public String getDateDebut() {
    return dateDebut;
  }

  public void setDateDebut(String dateDebut) {
    this.dateDebut = dateDebut;
  }

  public String getDateFin() {
    return dateFin;
  }

  public void setDateFin(String dateFin) {
    this.dateFin = dateFin;
  }

  public String getEmetteur() {
    return emetteur;
  }

  public void setEmetteur(String emetteur) {
    this.emetteur = emetteur;
  }

  public String getNumeroFichier() {
    return numeroFichier;
  }

  public void setNumeroFichier(String numeroFichier) {
    this.numeroFichier = numeroFichier;
  }

  public String getProcessus() {
    return processus;
  }

  public void setProcessus(String processus) {
    this.processus = processus;
  }

  public String getTypeFichier() {
    return typeFichier;
  }

  public void setTypeFichier(String typeFichier) {
    this.typeFichier = typeFichier;
  }

  public boolean isFichierEmis() {
    return isFichierEmis;
  }

  public void setFichierEmis(boolean isFichierEmis) {
    this.isFichierEmis = isFichierEmis;
  }

  public String getNomFichier() {
    return nomFichier;
  }

  public void setNomFichier(String nomFichier) {
    this.nomFichier = nomFichier;
  }

  public boolean isNewSearch() {
    return isNewSearch;
  }

  public void setNewSearch(boolean isNewSearch) {
    this.isNewSearch = isNewSearch;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getNumberByPage() {
    return numberByPage;
  }

  public void setNumberByPage(String numberByPage) {
    this.numberByPage = numberByPage;
  }
}
