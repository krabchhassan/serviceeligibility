package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

/** Classe DTO de l'entite Transco Param. */
public class TranscoParamDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codeObjetTransco;

  private String nomObjetTransco;

  private List<String> colNames;

  /* GETTERS SETTERS */

  public String getCodeObjetTransco() {
    return codeObjetTransco;
  }

  public void setCodeObjetTransco(String codeObjetTransco) {
    this.codeObjetTransco = codeObjetTransco;
  }

  public String getNomObjetTransco() {
    return nomObjetTransco;
  }

  public void setNomObjetTransco(String nomObjetTransco) {
    this.nomObjetTransco = nomObjetTransco;
  }

  public List<String> getColNames() {
    return colNames;
  }

  public void setColNames(List<String> colNames) {
    this.colNames = colNames;
  }
}
