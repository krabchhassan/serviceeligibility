package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

public class AssureDto {

  private String numeroAdherent;

  private String numeroAdherentComplet;

  private String numeroPersonne;

  private String refExternePersonne;

  private String nomPatronymique;

  private String nomMarital;

  public String getNumeroAdherent() {
    return numeroAdherent;
  }

  public void setNumeroAdherent(String numeroAdherent) {
    this.numeroAdherent = numeroAdherent;
  }

  public String getNumeroAdherentComplet() {
    return numeroAdherentComplet;
  }

  public void setNumeroAdherentComplet(String numeroAdherentComplet) {
    this.numeroAdherentComplet = numeroAdherentComplet;
  }

  public String getNumeroPersonne() {
    return numeroPersonne;
  }

  public void setNumeroPersonne(String numeroPersonne) {
    this.numeroPersonne = numeroPersonne;
  }

  public String getRefExternePersonne() {
    return refExternePersonne;
  }

  public void setRefExternePersonne(String refExternePersonne) {
    this.refExternePersonne = refExternePersonne;
  }

  public String getNomPatronymique() {
    return nomPatronymique;
  }

  public void setNomPatronymique(String nomPatronymique) {
    this.nomPatronymique = nomPatronymique;
  }

  public String getNomMarital() {
    return nomMarital;
  }

  public void setNomMarital(String nomMarital) {
    this.nomMarital = nomMarital;
  }
}
