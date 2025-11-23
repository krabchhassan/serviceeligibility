package com.cegedim.next.serviceeligibility.almerys608.utils;

import lombok.Getter;

/** Constantes pour les noms des tables temporaires */
@Getter
public enum EnumTempTable {
  SERVICE_TP("ServiceTP"),
  CARENCE("Carence"),
  PRODUIT("Produit"),
  RATTACHEMENT("Rattachement"),
  REJET("Rejet"),
  REJET_NON_BLOQUANT("RejetNonBloquant"),
  REJET_PRODUIT_EXCLU("RejetProduitExclu"),
  MEMBRE_CONTRAT("MembreContrat"),
  CONTRAT("Contrat"),
  ENTREPRISE("Entreprise"),
  SOUSCRIPTEUR("Souscripteur"),
  ADRESSE_AD("AdresseAD"),
  ADRESSE_GE("AdresseGE"),
  BENEFICIAIRE("Beneficiaire");

  private final String tableName;

  public String value() {
    return name();
  }

  EnumTempTable(String tableName) {
    this.tableName = tableName;
  }
}
