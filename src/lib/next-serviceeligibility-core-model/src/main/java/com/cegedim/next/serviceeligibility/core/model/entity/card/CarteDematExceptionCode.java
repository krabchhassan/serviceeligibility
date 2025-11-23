package com.cegedim.next.serviceeligibility.core.model.entity.card;

import lombok.Getter;

public enum CarteDematExceptionCode {
  OK("0000", "Autorisation transmise"),
  KO("0999", "Erreur Technique : %s"),
  PARAM_RECHERCHE_INCORRECTS("6000", "Paramètres recherche BDD incorrects"),
  BENEF_INCONNU("6001", "Bénéficiaire inconnu"),
  DROIT_BENEF_NON_OUVERT("6002", "Droits du bénéficiaire non ouverts au %s"),
  BENEF_NON_ELIGIBLE("6003", "Bénéficiaire non éligible au service"),
  PARAM_RECHERCHE_INCORRECTS_SEGMENTS_MANQUENT(
      "6004", "Paramètres recherche BDD incorrects : liste segments recherche vide"),
  NUM_ADHERENT_ABSENT(
      "6005",
      "Le n°adhérent est absent et celui-ci est indispensable pour identifier le bénéficiaire. Veuillez émettre une nouvelle demande avec le n°adhérent"),
  PRIORISATION_INCORRECTE(
      "6006", "Les garanties du bénéficiaire ne sont pas correctement priorisées"),
  CONSULT_CARTE_PAPIER("6008", "Veuillez consulter la carte papier"),
  PARAM_RECHERCHE_INCORRECTS_DATE_REFERENCE(
      "6009", "La date de référence doit être supérieure ou égale à la date du jour"),
  CARTE_DEMAT_NON_TROUVEE("6010", "Aucune carte TP valide pour ce contrat"),
  ERREUR_DATE_ANNEE_CIVILE(
      "6013", "Une demande de droits sur plusieurs exercices n'est pas autorisée"),
  ERREUR_MULTIPLES_ADHERENTS(
      "6014", "Plusieurs adhérents trouvés pour le numéro de contrat demandé");

  @Getter private final String code;
  @Getter private final String libelle;

  CarteDematExceptionCode(String code, String libelle) {
    this.code = code;
    this.libelle = libelle;
  }
}
