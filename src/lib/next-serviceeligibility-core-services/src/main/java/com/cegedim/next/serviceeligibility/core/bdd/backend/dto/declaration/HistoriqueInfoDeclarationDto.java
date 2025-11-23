package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

public class HistoriqueInfoDeclarationDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 7712364360519195482L;

  private boolean externalOrigin = false;
  private String effetDebut;
  private Boolean isDroitOuvert;
  private String idHistorique;
  private Boolean isCurrent;

  @JsonIgnore private Date effetDebutDate;

  public String getEffetDebut() {
    return effetDebut;
  }

  public void setEffetDebut(String effetDebut) {
    this.effetDebut = effetDebut;
  }

  public Date getEffetDebutDate() {
    return effetDebutDate;
  }

  public void setEffetDebutDate(Date effetDebutDate) {
    this.effetDebutDate = effetDebutDate;
  }

  public Boolean getIsDroitOuvert() {
    return isDroitOuvert;
  }

  public void setIsDroitOuvert(Boolean isDroitOuvert) {
    this.isDroitOuvert = isDroitOuvert;
  }

  public String getIdHistorique() {
    return idHistorique;
  }

  public void setIdHistorique(String idHistorique) {
    this.idHistorique = idHistorique;
  }

  public Boolean getIsCurrent() {
    return isCurrent;
  }

  public void setIsCurrent(Boolean isCurrent) {
    this.isCurrent = isCurrent;
  }

  public boolean isExternalOrigin() {
    return externalOrigin;
  }

  public void setExternalOrigin(boolean externalOrigin) {
    this.externalOrigin = externalOrigin;
  }
}
