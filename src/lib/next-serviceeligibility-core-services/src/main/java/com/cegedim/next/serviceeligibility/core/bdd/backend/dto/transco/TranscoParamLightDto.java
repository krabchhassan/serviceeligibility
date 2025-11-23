package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.transco;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

/** Classe DTO de l'entite Transco Param allege. */
public class TranscoParamLightDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String codeObjetTransco;

  private String nomObjetTransco;

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
}
