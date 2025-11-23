package com.cegedim.next.serviceeligibility.core.bdd.backend.utility;

import lombok.Getter;

/** Enumeration for parameters kind */
@Getter
public enum ParametersEnum {
  DOMAINE("domaine"),
  DOMAINE_IS("domaine_IS"),
  DOMAINE_SP("domaine_SP"),
  REJETS("rejets"),
  FORMULE("formules"),
  CONVENTIONNEMENT("conventionnement"),
  PROCESSUS("processus"),
  TYPE_FICHIERS("typeFichiers"),
  PRESTATIONS("prestations"),
  SERVICES_METIERS("servicesMetiers"),
  CODES_RENVOI("codesRenvoi");

  private final String type;

  ParametersEnum(String type) {
    this.type = type;
  }

  public static ParametersEnum byType(String type) {
    for (ParametersEnum parametersEnum : ParametersEnum.values()) {
      if (parametersEnum.getType().equals(type)) {
        return parametersEnum;
      }
    }

    return null;
  }
}
