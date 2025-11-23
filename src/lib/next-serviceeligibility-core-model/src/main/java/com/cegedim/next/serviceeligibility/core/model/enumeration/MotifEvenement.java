package com.cegedim.next.serviceeligibility.core.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MotifEvenement {
  DE("Declaration de droits"),
  IN("Initialisation des droits"),
  RE("Renouvellement des droits"),
  EM("Reemission des droits"),
  AD("Declaration carte TP"),
  FE("Fermeture droits"),
  EMPTY("");

  private final String libelle;
}
