package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NomAssure {
  @NotBlank(message = "Le nom de famille est obligatoire")
  private String nomFamille;

  private String nomUsage;

  @NotBlank(message = "Le prénom est obligatoire")
  private String prenom;

  @NotBlank(message = "La civilité est obligatoire")
  private String civilite;

  public NomAssure(
      @NotBlank(message = "Le nom de famille est obligatoire") String nomFamille,
      String nomUsage,
      @NotBlank(message = "Le prénom est obligatoire") String prenom,
      @NotBlank(message = "La civilité est obligatoire") String civilite) {
    this.nomFamille = nomFamille;
    this.nomUsage = nomUsage;
    this.prenom = prenom;
    this.civilite = civilite;
  }

  public NomAssure() {}

  public NomAssure(NomAssure source) {
    this.nomFamille = source.getNomFamille();
    this.nomUsage = source.getNomUsage();
    this.civilite = source.getCivilite();
    this.prenom = source.getPrenom();
  }

  public void setNomFamille(String newNomFamille) {
    if (newNomFamille != null) {
      this.nomFamille = newNomFamille;
    }
  }

  public void setNomUsage(String newNomUsage) {
    if (newNomUsage != null) {
      this.nomUsage = newNomUsage;
    }
  }

  public void setPrenom(String newPrenom) {
    if (newPrenom != null) {
      this.prenom = newPrenom;
    }
  }

  public void setCivilite(String newCivilite) {
    if (newCivilite != null) {
      this.civilite = newCivilite;
    }
  }
}
