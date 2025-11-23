package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class Amc {
  private String idDeclarant;
  private String libelle;

  public void setIdDeclarant(String newIdDeclarant) {
    if (newIdDeclarant != null) {
      this.idDeclarant = newIdDeclarant;
    }
  }

  public void setLibelle(String newLibelle) {
    if (newLibelle != null) {
      this.libelle = newLibelle;
    }
  }
}
