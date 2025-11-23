package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class RattachementRO {
  private String codeRegime;
  private String codeCaisse;
  @EqualsAndHashCode.Exclude private String codeCentre;

  public RattachementRO(String codeRegime, String codeCaisse, String codeCentre) {
    this.codeRegime = codeRegime;
    this.codeCaisse = codeCaisse;
    this.codeCentre = codeCentre;
  }

  public RattachementRO() {}

  public void setCodeRegime(String newCodeRegime) {
    if (newCodeRegime != null) {
      this.codeRegime = newCodeRegime;
    }
  }

  public void setCodeCaisse(String newCodeCaisse) {
    if (newCodeCaisse != null) {
      this.codeCaisse = newCodeCaisse;
    }
  }

  public void setCodeCentre(String newCodeCentre) {
    if (newCodeCentre != null) {
      this.codeCentre = newCodeCentre;
    }
  }
}
