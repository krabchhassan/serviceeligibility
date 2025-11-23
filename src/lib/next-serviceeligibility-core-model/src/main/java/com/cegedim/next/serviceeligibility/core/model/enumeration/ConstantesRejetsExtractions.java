package com.cegedim.next.serviceeligibility.core.model.enumeration;

import lombok.Getter;

@Getter
public enum ConstantesRejetsExtractions {

  /** Constante Almerys */
  REJET_C12("C12", "CODE EXTERNE PRODUIT ABSENT", "S", "B"),
  REJET_A03("A03", "CONTRAT SANS ASSURE PRINCIPAL", "M", "B"),
  REJET_A04("A04", "TYPE BENEFICIAIRE INCORRECT", "M", "B"),
  REJET_A05("A05", "CODE MOUVEMENT INCORRECT", "M", "B"),
  REJET_A06("A06", "LIEN JURIDIQUE INCORRECT", "M", "B"),
  REJET_A07("A07", "PRODUITS DE MEME ORDRE EN CHEVAUCHEMENT", "M", "B"),
  REJET_A08("A08", "CODE PRODUIT INCORRECT", "M", "B");

  private final String code;
  private final String message;
  private final String typeErreur;
  private final String niveau;

  ConstantesRejetsExtractions(String code, String message, String typeErreur, String niveau) {
    this.code = code;
    this.message = message;
    this.typeErreur = typeErreur;
    this.niveau = niveau;
  }

  @Override
  public String toString() {
    return this.code + ";" + this.message + ";;" + this.typeErreur + ";" + this.niveau + "#";
  }

  public String toString(Object arg) {
    if (arg == null) {
      return this.toString();
    } else {
      return this.code
          + ";"
          + this.message
          + ";"
          + arg
          + ";"
          + this.typeErreur
          + ";"
          + this.niveau
          + "#";
    }
  }

  public static ConstantesRejetsExtractions findByCode(String code) {
    ConstantesRejetsExtractions result = null;
    for (ConstantesRejetsExtractions constantesRejetsExtractions : values()) {
      if (constantesRejetsExtractions.name().equalsIgnoreCase(code)) {
        result = constantesRejetsExtractions;
        break;
      }
    }
    return result;
  }
}
