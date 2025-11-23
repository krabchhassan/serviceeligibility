package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class AdresseAssure {
  private String ligne1;
  private String ligne2;
  private String ligne3;
  private String ligne4;
  private String ligne5;
  private String ligne6;
  private String ligne7;
  private String codePostal;

  public AdresseAssure(
      String ligne1,
      String ligne2,
      String ligne3,
      String ligne4,
      String ligne5,
      String ligne6,
      String ligne7,
      String codePostal) {
    this.ligne1 = ligne1;
    this.ligne2 = ligne2;
    this.ligne3 = ligne3;
    this.ligne4 = ligne4;
    this.ligne5 = ligne5;
    this.ligne6 = ligne6;
    this.ligne7 = ligne7;
    this.codePostal = codePostal;
  }

  public AdresseAssure() {}

  public AdresseAssure(AdresseAssure source) {
    this.ligne1 = source.getLigne1();
    this.ligne2 = source.getLigne2();
    this.ligne3 = source.getLigne3();
    this.ligne4 = source.getLigne4();
    this.ligne5 = source.getLigne5();
    this.ligne6 = source.getLigne6();
    this.ligne7 = source.getLigne7();
    this.codePostal = source.getCodePostal();
  }

  public void setLigne1(String newLigne1) {
    if (newLigne1 != null) {
      this.ligne1 = newLigne1;
    }
  }

  public void setLigne2(String newLigne2) {
    if (newLigne2 != null) {
      this.ligne2 = newLigne2;
    }
  }

  public void setLigne3(String newLigne3) {
    if (newLigne3 != null) {
      this.ligne3 = newLigne3;
    }
  }

  public void setLigne4(String newLigne4) {
    if (newLigne4 != null) {
      this.ligne4 = newLigne4;
    }
  }

  public void setLigne5(String newLigne5) {
    if (newLigne5 != null) {
      this.ligne5 = newLigne5;
    }
  }

  public void setLigne6(String newLigne6) {
    if (newLigne6 != null) {
      this.ligne6 = newLigne6;
    }
  }

  public void setLigne7(String newLigne7) {
    if (newLigne7 != null) {
      this.ligne7 = newLigne7;
    }
  }

  public void setCodePostal(String newCodePostal) {
    if (newCodePostal != null) {
      this.codePostal = newCodePostal;
    }
  }
}
