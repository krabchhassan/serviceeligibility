package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class RechercheCriteresDeclarationsDto implements GenericDto {

  /** */
  private static final long serialVersionUID = -3485647055193885241L;

  private String numeroOuNomAMC;

  private String numeroAMCEchange;

  private String numeroAdherent;

  private String numeroContrat;

  private String numeroPersonne;

  private String numeroRO;

  private String dateNaissance;

  private String rangNaissance;

  public String getNumeroOuNomAMC() {
    return numeroOuNomAMC;
  }

  public void setNumeroOuNomAMC(String numeroAMC) {
    this.numeroOuNomAMC = numeroAMC;
  }

  public String getNumeroAMCEchange() {
    return numeroAMCEchange;
  }

  public void setNumeroAMCEchange(String numeroAMCEchange) {
    this.numeroAMCEchange = numeroAMCEchange;
  }

  public String getNumeroAdherent() {
    return numeroAdherent;
  }

  public void setNumeroAdherent(String numeroAdherent) {
    this.numeroAdherent = numeroAdherent;
  }

  public String getNumeroContrat() {
    return numeroContrat;
  }

  public void setNumeroContrat(String numeroContrat) {
    this.numeroContrat = numeroContrat;
  }

  public String getNumeroRO() {
    return numeroRO;
  }

  public void setNumeroRO(String numeroRO) {
    this.numeroRO = numeroRO;
  }

  public String getNumeroPersonne() {
    return numeroPersonne;
  }

  public void setNumeroPersonne(String numeroPersonne) {
    this.numeroPersonne = numeroPersonne;
  }

  public String getDateNaissance() {
    return dateNaissance;
  }

  public void setDateNaissance(String dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public String getRangNaissance() {
    return rangNaissance;
  }

  public void setRangNaissance(String rangNaissance) {
    this.rangNaissance = rangNaissance;
  }

  @Override
  public String toString() {
    return "Criteres Recherche Droits [numeroOuNomAMC="
        + numeroOuNomAMC
        + ", numeroAMCEchange="
        + numeroAMCEchange
        + ", numeroAdherent="
        + numeroAdherent
        + ", numeroContrat="
        + numeroContrat
        + ", numeroPersonne="
        + numeroPersonne
        + ", numeroRO="
        + numeroRO
        + ", dateNaissance="
        + dateNaissance
        + ", rangNaissance="
        + rangNaissance
        + "]";
  }
}
