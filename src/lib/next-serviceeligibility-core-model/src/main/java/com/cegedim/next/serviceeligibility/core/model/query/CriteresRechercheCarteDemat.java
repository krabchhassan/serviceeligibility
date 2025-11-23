package com.cegedim.next.serviceeligibility.core.model.query;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class CriteresRechercheCarteDemat implements GenericDomain<CriteresRechercheCarteDemat> {

  private static final long serialVersionUID = -5727332228938740665L;

  private String numeroAMC;
  private String numeroContrat;
  private String dateReference;

  public CriteresRechercheCarteDemat(String numeroAMC, String numeroContrat, String dateReference) {
    super();
    this.numeroAMC = numeroAMC;
    this.numeroContrat = numeroContrat;
    this.dateReference = dateReference;
  }

  public CriteresRechercheCarteDemat() {}

  @Override
  public int compareTo(CriteresRechercheCarteDemat parametreFlux) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.numeroAMC, parametreFlux.numeroAMC);
    compareToBuilder.append(this.numeroContrat, parametreFlux.numeroContrat);
    compareToBuilder.append(this.dateReference, parametreFlux.dateReference);
    return compareToBuilder.toComparison();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((numeroAMC == null) ? 0 : numeroAMC.hashCode());
    result = prime * result + ((numeroContrat == null) ? 0 : numeroContrat.hashCode());
    result = prime * result + ((dateReference == null) ? 0 : dateReference.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) {
      return false;
    }
    CriteresRechercheCarteDemat other = (CriteresRechercheCarteDemat) obj;
    if (numeroAMC == null) {
      if (other.numeroAMC != null) {
        return false;
      }
    } else if (!numeroAMC.equals(other.numeroAMC)) {
      return false;
    }
    if (numeroContrat == null) {
      if (other.numeroContrat != null) {
        return false;
      }
    } else if (!numeroContrat.equals(other.numeroContrat)) {
      return false;
    }
    if (dateReference == null) {
      if (other.dateReference != null) {
        return false;
      }
    } else if (!dateReference.equals(other.dateReference)) {
      return false;
    }

    return true;
  }
}
