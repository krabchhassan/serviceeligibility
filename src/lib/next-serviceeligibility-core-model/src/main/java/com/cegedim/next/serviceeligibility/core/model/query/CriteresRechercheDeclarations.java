package com.cegedim.next.serviceeligibility.core.model.query;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class CriteresRechercheDeclarations implements GenericDomain<CriteresRechercheDeclarations> {

  /** */
  private static final long serialVersionUID = 3187925929221576428L;

  private String numeroAMC;

  private String numAMCEchange;

  private String nomAMC;

  private String numeroContrat;

  private String dateNaissance;

  private String rangNaissance;

  private String numeroAdherent;

  private String numeroPersonne;

  private String numeroRO;

  private List<String> idDeclarants;

  private String typeConventionnement;

  private List<String> domainesTiersPayant;

  private String dateDebut;

  private String dateFin;

  @Override
  public int compareTo(CriteresRechercheDeclarations parametreFlux) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.numeroAMC, parametreFlux.numeroAMC);
    compareToBuilder.append(this.numAMCEchange, parametreFlux.numAMCEchange);
    compareToBuilder.append(this.nomAMC, parametreFlux.nomAMC);
    compareToBuilder.append(this.numeroContrat, parametreFlux.numeroContrat);
    compareToBuilder.append(this.dateNaissance, parametreFlux.dateNaissance);
    compareToBuilder.append(this.rangNaissance, parametreFlux.rangNaissance);
    compareToBuilder.append(this.numeroAdherent, parametreFlux.numeroAdherent);
    compareToBuilder.append(this.numeroPersonne, parametreFlux.numeroPersonne);
    compareToBuilder.append(this.numeroRO, parametreFlux.numeroRO);
    compareToBuilder.append(this.typeConventionnement, parametreFlux.typeConventionnement);
    compareToBuilder.append(this.dateDebut, parametreFlux.dateDebut);
    compareToBuilder.append(this.dateFin, parametreFlux.dateFin);
    return compareToBuilder.toComparison();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((numeroAMC == null) ? 0 : numeroAMC.hashCode());
    result = prime * result + ((numAMCEchange == null) ? 0 : numAMCEchange.hashCode());
    result = prime * result + ((nomAMC == null) ? 0 : nomAMC.hashCode());
    result = prime * result + ((numeroContrat == null) ? 0 : numeroContrat.hashCode());
    result = prime * result + ((dateNaissance == null) ? 0 : dateNaissance.hashCode());
    result = prime * result + ((rangNaissance == null) ? 0 : rangNaissance.hashCode());
    result = prime * result + ((numeroAdherent == null) ? 0 : numeroAdherent.hashCode());
    result = prime * result + ((numeroPersonne == null) ? 0 : numeroPersonne.hashCode());
    result = prime * result + ((numeroRO == null) ? 0 : numeroRO.hashCode());
    result =
        prime * result + ((typeConventionnement == null) ? 0 : typeConventionnement.hashCode());
    result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
    result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) {
      return false;
    }
    CriteresRechercheDeclarations other = (CriteresRechercheDeclarations) obj;
    if (numeroAMC == null) {
      if (other.numeroAMC != null) {
        return false;
      }
    } else if (!numeroAMC.equals(other.numeroAMC)) {
      return false;
    }
    if (numAMCEchange == null) {
      if (other.numAMCEchange != null) {
        return false;
      }
    } else if (!numAMCEchange.equals(other.numAMCEchange)) {
      return false;
    }
    if (nomAMC == null) {
      if (other.nomAMC != null) {
        return false;
      }
    } else if (!nomAMC.equals(other.nomAMC)) {
      return false;
    }
    if (numeroContrat == null) {
      if (other.numeroContrat != null) {
        return false;
      }
    } else if (!numeroContrat.equals(other.numeroContrat)) {
      return false;
    }
    if (dateNaissance == null) {
      if (other.dateNaissance != null) {
        return false;
      }
    } else if (!dateNaissance.equals(other.dateNaissance)) {
      return false;
    }
    if (rangNaissance == null) {
      if (other.rangNaissance != null) {
        return false;
      }
    } else if (!rangNaissance.equals(other.rangNaissance)) {
      return false;
    }
    if (numeroAdherent == null) {
      if (other.numeroAdherent != null) {
        return false;
      }
    } else if (!numeroAdherent.equals(other.numeroAdherent)) {
      return false;
    }
    if (numeroPersonne == null) {
      if (other.numeroPersonne != null) {
        return false;
      }
    } else if (!numeroPersonne.equals(other.numeroPersonne)) {
      return false;
    }
    if (numeroRO == null) {
      if (other.numeroRO != null) {
        return false;
      }
    } else if (!numeroRO.equals(other.numeroRO)) {
      return false;
    }
    if (typeConventionnement == null) {
      if (other.typeConventionnement != null) {
        return false;
      }
    } else if (!typeConventionnement.equals(other.typeConventionnement)) {
      return false;
    }
    if (dateDebut == null) {
      if (other.dateDebut != null) {
        return false;
      }
    } else if (!dateDebut.equals(other.dateDebut)) {
      return false;
    }
    if (dateFin == null) {
      if (other.dateFin != null) {
        return false;
      }
    } else if (!dateFin.equals(other.dateFin)) {
      return false;
    }
    return true;
  }
}
