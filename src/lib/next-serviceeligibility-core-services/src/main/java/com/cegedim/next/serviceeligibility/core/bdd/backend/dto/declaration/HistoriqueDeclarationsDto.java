package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

public class HistoriqueDeclarationsDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 826850494693716514L;

  private String numeroContrat;
  private List<HistoriqueInfoDeclarationDto> infosHistorique;

  public String getNumeroContrat() {
    return numeroContrat;
  }

  public void setNumeroContrat(String numeroContrat) {
    this.numeroContrat = numeroContrat;
  }

  public List<HistoriqueInfoDeclarationDto> getInfosHistorique() {
    return infosHistorique;
  }

  public void setInfosHistorique(List<HistoriqueInfoDeclarationDto> infosHistorique) {
    this.infosHistorique = infosHistorique;
  }
}
