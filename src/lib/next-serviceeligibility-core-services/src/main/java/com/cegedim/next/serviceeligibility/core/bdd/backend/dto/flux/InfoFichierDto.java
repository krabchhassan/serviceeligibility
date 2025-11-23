package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class InfoFichierDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String statut;

  private String codeRejet;

  private String messageRejet;

  private String nomFichier;

  private String mouvementRecus;

  private String mouvementRejetes;

  private String mouvementOk;

  private String mouvementEmis;

  private String mouvementNonEmis;

  private String nomFichierARL;

  private String versionFichier;

  private String numeroFichier;

  private String critereSecondaire;

  private String critereSecondaireDetaille;

  public String getStatut() {
    return statut;
  }

  public void setStatut(String statut) {
    this.statut = statut;
  }

  public String getCodeRejet() {
    return codeRejet;
  }

  public void setCodeRejet(String codeRejet) {
    this.codeRejet = codeRejet;
  }

  public String getMessageRejet() {
    return messageRejet;
  }

  public void setMessageRejet(String messageRejet) {
    this.messageRejet = messageRejet;
  }

  public String getNomFichier() {
    return nomFichier;
  }

  public void setNomFichier(String nomFichier) {
    this.nomFichier = nomFichier;
  }

  public String getMouvementRecus() {
    return mouvementRecus;
  }

  public void setMouvementRecus(String mouvementRecus) {
    this.mouvementRecus = mouvementRecus;
  }

  public String getMouvementRejetes() {
    return mouvementRejetes;
  }

  public void setMouvementRejetes(String mouvementRejetes) {
    this.mouvementRejetes = mouvementRejetes;
  }

  public String getMouvementOk() {
    return mouvementOk;
  }

  public void setMouvementOk(String mouvementOk) {
    this.mouvementOk = mouvementOk;
  }

  public String getMouvementEmis() {
    return mouvementEmis;
  }

  public void setMouvementEmis(String mouvementEmis) {
    this.mouvementEmis = mouvementEmis;
  }

  public String getMouvementNonEmis() {
    return mouvementNonEmis;
  }

  public void setMouvementNonEmis(String mouvementNonEmis) {
    this.mouvementNonEmis = mouvementNonEmis;
  }

  public String getNomFichierARL() {
    return nomFichierARL;
  }

  public void setNomFichierARL(String nomFichierARL) {
    this.nomFichierARL = nomFichierARL;
  }

  public String getVersionFichier() {
    return versionFichier;
  }

  public void setVersionFichier(String versionFichier) {
    this.versionFichier = versionFichier;
  }

  public String getNumeroFichier() {
    return numeroFichier;
  }

  public void setNumeroFichier(String numeroFichier) {
    this.numeroFichier = numeroFichier;
  }

  public String getCritereSecondaire() {
    return critereSecondaire;
  }

  public void setCritereSecondaire(String critereSecondaire) {
    this.critereSecondaire = critereSecondaire;
  }

  public String getCritereSecondaireDetaille() {
    return critereSecondaireDetaille;
  }

  public void setCritereSecondaireDetaille(String critereSecondaireDetaille) {
    this.critereSecondaireDetaille = critereSecondaireDetaille;
  }
}
