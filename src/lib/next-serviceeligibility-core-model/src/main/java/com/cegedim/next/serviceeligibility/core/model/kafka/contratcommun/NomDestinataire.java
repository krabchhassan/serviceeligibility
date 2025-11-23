package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class NomDestinataire implements GenericDomain<NomDestinataire> {
  private String nomFamille;
  private String nomUsage;
  private String prenom;
  private String civilite;
  private String raisonSociale;

  public NomDestinataire(
      String nomFamille, String nomUsage, String prenom, String civilite, String raisonSociale) {
    this.nomFamille = nomFamille;
    this.nomUsage = nomUsage;
    this.prenom = prenom;
    this.civilite = civilite;
    this.raisonSociale = raisonSociale;
  }

  public NomDestinataire() {}

  /**
   * Methode permettant de retourner un message d'avertissement si le nom du destinataire n'est pas
   * valide
   *
   * @return
   */
  public String validateNomDestinataire() {
    if (StringUtils.isBlank(this.nomFamille)
        && StringUtils.isBlank(this.prenom)
        && StringUtils.isBlank(this.civilite)
        && StringUtils.isBlank(this.raisonSociale)) {
      return "Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées. Si le destinataire de prestation est une personne morale, la raison sociale doit être renseignée.";
    }
    if (StringUtils.isBlank(this.raisonSociale)
        && (StringUtils.isBlank(this.nomFamille)
            || StringUtils.isBlank(this.prenom)
            || StringUtils.isBlank(this.civilite))) {
      return "Si le destinataire de prestation est une personne physique les données nomFamille, prenom et civilite doivent être renseignées.";
    }
    if (StringUtils.isNotBlank(this.raisonSociale)
        && (StringUtils.isNotBlank(this.nomFamille)
            || StringUtils.isNotBlank(this.nomUsage)
            || StringUtils.isNotBlank(this.prenom)
            || StringUtils.isNotBlank(this.civilite))) {
      return "La raison sociale ne peut pas être renseignée si l'un des champs suivant l'est aussi : nomFamille, nomUsage, prenom, civilite";
    }
    return null;
  }

  public void setNomFamille(String newNomFamille) {
    if (newNomFamille != null) {
      this.nomFamille = newNomFamille;
    }
  }

  public void setNomFamilleToNull() {
    this.nomFamille = null;
  }

  public void setNomUsage(String newNomUsage) {
    if (newNomUsage != null) {
      this.nomUsage = newNomUsage;
    }
  }

  public void setNomUsageToNull() {
    this.nomUsage = null;
  }

  public void setPrenom(String newPrenom) {
    if (newPrenom != null) {
      this.prenom = newPrenom;
    }
  }

  public void setPrenomToNull() {
    this.prenom = null;
  }

  public void setCivilite(String newCivilite) {
    if (newCivilite != null) {
      this.civilite = newCivilite;
    }
  }

  public void setCiviliteToNull() {
    this.civilite = null;
  }

  public void setRaisonSociale(String newRaisonSociale) {
    if (newRaisonSociale != null) {
      this.raisonSociale = newRaisonSociale;
    }
  }

  public void setRaisonSocialeToNull() {
    this.raisonSociale = null;
  }

  @Override
  public int compareTo(NomDestinataire nomDestinataire) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.nomFamille, nomDestinataire.nomFamille);
    compareToBuilder.append(this.nomUsage, nomDestinataire.nomUsage);
    compareToBuilder.append(this.prenom, nomDestinataire.prenom);
    compareToBuilder.append(this.civilite, nomDestinataire.civilite);
    compareToBuilder.append(this.raisonSociale, nomDestinataire.raisonSociale);
    return compareToBuilder.toComparison();
  }
}
