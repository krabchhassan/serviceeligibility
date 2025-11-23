package com.cegedim.next.serviceeligibility.core.model.domain.carence;

import lombok.Data;

@Data
public class ParametrageCarence {
  private String offre;
  private String produit;
  private String naturePrestation;
  private String codeCarence;
  private String dateDebutParametrage;
  private String dateFinParametrage;

  public ParametrageCarence() {
    super();
  }

  public ParametrageCarence(
      String offre,
      String produit,
      String naturePrestation,
      String codeCarence,
      String dateDebutParametrage,
      String dateFinParametrage) {
    this.offre = offre;
    this.produit = produit;
    this.naturePrestation = naturePrestation;
    this.codeCarence = codeCarence;
    this.dateDebutParametrage = dateDebutParametrage;
    this.dateFinParametrage = dateFinParametrage;
  }
}
