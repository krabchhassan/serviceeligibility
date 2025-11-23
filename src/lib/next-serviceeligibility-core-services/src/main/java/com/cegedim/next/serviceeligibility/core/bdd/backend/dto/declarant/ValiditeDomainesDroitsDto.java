package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonInclude;

/** Classe DTO du pilotage. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValiditeDomainesDroitsDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String codeDomaine;
  private int duree;
  private String unite;

  // Optionnal if unite != Mois
  private boolean positionnerFinDeMois;

  /* GETTERS SETTERS */
  public String getCodeDomaine() {
    return codeDomaine;
  }

  public void setCodeDomaine(String codeDomaine) {
    this.codeDomaine = codeDomaine;
  }

  public int getDuree() {
    return duree;
  }

  public void setDuree(int duree) {
    this.duree = duree;
  }

  public String getUnite() {
    return unite;
  }

  public void setUnite(String unite) {
    this.unite = unite;
  }

  public boolean isPositionnerFinDeMois() {
    return positionnerFinDeMois;
  }

  public void setPositionnerFinDeMois(boolean positionnerFinDeMois) {
    this.positionnerFinDeMois = positionnerFinDeMois;
  }
}
