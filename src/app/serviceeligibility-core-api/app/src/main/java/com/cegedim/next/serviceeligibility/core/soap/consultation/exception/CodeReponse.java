package com.cegedim.next.serviceeligibility.core.soap.consultation.exception;

import lombok.Getter;

/** Enumeration des codes reponses. */
@Getter
public enum CodeReponse {

  /** Code reponse KO. */
  KO("0999"),

  /** Code reponse OK. */
  OK("0000"),

  /** Paramètres recherche BdD incorrects. */
  PARAM_RECHERCHE_INCORRECTS("6000"),

  /** Paramètres recherche BdD incorrects : liste segments recherche vide. */
  PARAM_RECHERCHE_INCORRECTS_SEGMENTS_MANQUENT("6004"),

  /** Consulter la carte papier. */
  CONSULT_CARTE_PAPIER("6008"),

  /** Benef. inconnu */
  BENEF_INCONNU("6001"),

  /** Droits du bénéficiaire non ouverts au xx/xx/xxxx. */
  DROIT_BENEF_NON_OUVERT("6002"),

  /** Bénéficiaire non éligible au service. */
  BENEF_NON_ELIGIBLE("6003"),

  /** Le n°adhérent est absent et celui-ci est indispensable pour identifier le bénéficiaire. */
  NUM_ADHERENT_ABSENT("6005"),

  /** Bénéficiaire non éligible au service. */
  PRIORISATION_INCORRECTE("6006"),

  /** Date référence doit être supérieure à la date du jour. */
  PARAM_RECHERCHE_INCORRECTS_DATE_REFERENCE("6009"),

  /** Pas de carte touvée pour les paramètres. */
  CARTE_DEMAT_NON_TROUVEE("6010"),

  ERREUR_DATE_ANNEE_CIVILE("6013");

  /**
   * -- GETTER -- Getter for code attribute.
   *
   * @return the value of code attribute
   */
  private final String code;

  CodeReponse(final String theCode) {
    code = theCode;
  }
}
