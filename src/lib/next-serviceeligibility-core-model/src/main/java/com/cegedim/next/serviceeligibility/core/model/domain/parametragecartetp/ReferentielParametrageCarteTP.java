package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "referentielParametragesCarteTP")
@Getter
@Setter
public class ReferentielParametrageCarteTP {
  /** Unicite sur le champ amc */
  @Id private String amc;

  private List<String> identifiantsCollectivite;
  private List<String> groupesPopulation;
  private List<String> portefeuille;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((amc == null) ? 0 : amc.hashCode());
    result =
        prime * result
            + ((identifiantsCollectivite == null) ? 0 : identifiantsCollectivite.hashCode());
    result = prime * result + ((groupesPopulation == null) ? 0 : groupesPopulation.hashCode());
    result = prime * result + ((portefeuille == null) ? 0 : portefeuille.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ReferentielParametrageCarteTP other = (ReferentielParametrageCarteTP) obj;
    if (amc == null) {
      if (other.amc != null) return false;
    } else if (!amc.equals(other.amc)) return false;
    if (identifiantsCollectivite == null) {
      if (other.identifiantsCollectivite != null) return false;
    } else if (!identifiantsCollectivite.equals(other.identifiantsCollectivite)) return false;
    if (groupesPopulation == null) {
      if (other.groupesPopulation != null) return false;
    } else if (!groupesPopulation.equals(other.groupesPopulation)) return false;
    if (portefeuille == null) {
      if (other.portefeuille != null) return false;
    } else if (!portefeuille.equals(other.portefeuille)) return false;
    return true;
  }
}
