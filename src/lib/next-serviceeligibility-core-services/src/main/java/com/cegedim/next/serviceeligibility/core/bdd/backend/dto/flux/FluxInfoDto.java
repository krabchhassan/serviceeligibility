package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.flux;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class FluxInfoDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String batch;

  private String dateExecution;

  private String processus;

  private boolean isFichierEmis;

  private String idDeclarant;

  private String nomAMC;

  private String codePartenaire;

  private String codeCircuit;

  private String emetteurDroits;

  private String operateurPrincipal;

  private String typeFichier;

  private String numAMCEchange;

  private InfoFichierDto infoFichier;

  public String getBatch() {
    return batch;
  }

  public void setBatch(String batch) {
    this.batch = batch;
  }

  public String getDateExecution() {
    return dateExecution;
  }

  public void setDateExecution(String dateExecution) {
    this.dateExecution = dateExecution;
  }

  public String getProcessus() {
    return processus;
  }

  public void setProcessus(String processus) {
    this.processus = processus;
  }

  public boolean isFichierEmis() {
    return isFichierEmis;
  }

  public void setFichierEmis(boolean isFichierEmis) {
    this.isFichierEmis = isFichierEmis;
  }

  public String getIdDeclarant() {
    return idDeclarant;
  }

  public void setIdDeclarant(String idDeclarant) {
    this.idDeclarant = idDeclarant;
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

  public String getEmetteurDroits() {
    return emetteurDroits;
  }

  public void setEmetteurDroits(String emetteurDroits) {
    this.emetteurDroits = emetteurDroits;
  }

  public String getOperateurPrincipal() {
    return operateurPrincipal;
  }

  public void setOperateurPrincipal(String operateurPrincipal) {
    this.operateurPrincipal = operateurPrincipal;
  }

  public String getTypeFichier() {
    return typeFichier;
  }

  public void setTypeFichier(String typeFichier) {
    this.typeFichier = typeFichier;
  }

  public String getNumAMCEchange() {
    return numAMCEchange;
  }

  public void setNumAMCEchange(String numAMCEchange) {
    this.numAMCEchange = numAMCEchange;
  }

  public InfoFichierDto getInfoFichier() {
    return infoFichier;
  }

  public void setInfoFichier(InfoFichierDto infoFichier) {
    this.infoFichier = infoFichier;
  }
}
