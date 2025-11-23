package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

public class DroitsDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 1952421855284023500L;

  private Integer nbDomaines;
  private Integer nbGaranties;
  private Boolean isDroitOuvert;
  private String dateRestitutionCarte;
  private String periodeDroitDebut;
  private String periodeDroitFin;
  private String periodeDroitOfflineFin;
  private List<DomaineDroitDto> domaines;

  public Integer getNbDomaines() {
    return nbDomaines;
  }

  public void setNbDomaines(Integer nbDomaines) {
    this.nbDomaines = nbDomaines;
  }

  public String getDateRestitutionCarte() {
    return dateRestitutionCarte;
  }

  public void setDateRestitutionCarte(String dateRestitutionCarte) {
    this.dateRestitutionCarte = dateRestitutionCarte;
  }

  public Integer getNbGaranties() {
    return nbGaranties;
  }

  public void setNbGaranties(Integer nbGaranties) {
    this.nbGaranties = nbGaranties;
  }

  public Boolean getIsDroitOuvert() {
    return isDroitOuvert;
  }

  public void setIsDroitOuvert(Boolean isDroitOuvert) {
    this.isDroitOuvert = isDroitOuvert;
  }

  public String getPeriodeDroitDebut() {
    return periodeDroitDebut;
  }

  public void setPeriodeDroitDebut(String periodeDroitDebut) {
    this.periodeDroitDebut = periodeDroitDebut;
  }

  public String getPeriodeDroitFin() {
    return periodeDroitFin;
  }

  public void setPeriodeDroitFin(String periodeDroitFin) {
    this.periodeDroitFin = periodeDroitFin;
  }

  public String getPeriodeDroitOfflineFin() {
    return periodeDroitOfflineFin;
  }

  public void setPeriodeDroitOfflineFin(String periodeDroitOfflineFin) {
    this.periodeDroitOfflineFin = periodeDroitOfflineFin;
  }

  public List<DomaineDroitDto> getDomaines() {
    return domaines;
  }

  public void setDomaines(List<DomaineDroitDto> domaines) {
    this.domaines = domaines;
  }
}
