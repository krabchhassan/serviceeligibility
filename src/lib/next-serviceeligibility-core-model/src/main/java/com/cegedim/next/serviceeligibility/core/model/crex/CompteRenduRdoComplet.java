package com.cegedim.next.serviceeligibility.core.model.crex;

import lombok.Data;

@Data
public class CompteRenduRdoComplet {
  private String nomFichier;

  private String nomFichierARL;

  private String idDeclarant;

  private String dateTraitement;

  private String nbContratsLus;

  private String nbContratsIntegres;

  private String nbContratsRejetes;

  private String nbDeclarationsOuverture;

  private String nbDeclarationFermeture;

  public String getNomFichier() {
    return nomFichier;
  }

  public void setNomFichier(String nomFichier) {
    this.nomFichier = nomFichier;
  }

  public CompteRenduRdoComplet(
      String nomFichier,
      String nomFichierARL,
      String idDeclarant,
      String dateTraitement,
      String nbContratsLus,
      String nbContratsIntegres,
      String nbContratsRejetes,
      String nbDeclarationsOuverture,
      String nbDeclarationFermeture) {
    this.nomFichier = nomFichier;
    this.nomFichierARL = nomFichierARL;
    this.idDeclarant = idDeclarant;
    this.dateTraitement = dateTraitement;
    this.nbContratsLus = nbContratsLus;
    this.nbContratsIntegres = nbContratsIntegres;
    this.nbContratsRejetes = nbContratsRejetes;
    this.nbDeclarationsOuverture = nbDeclarationsOuverture;
    this.nbDeclarationFermeture = nbDeclarationFermeture;
  }
}
