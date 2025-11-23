package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Anomaly {
  NO_TP_RIGHTS_CAUSED_BY_WAITINGS_PERIODS(
      "La ou les carences positionnées ne permettent pas de générer des droits TP pour la période demandée"),
  SAS_FOUND_FOR_THIS_CONTRACT("Sas trouvé pour ce contrat"),
  ERROR_FOUND_FOR_THIS_CONTRACT("Autre erreur présente sur le contrat"),
  ERROR_RETRY_RECYCLING("Retry recycling"),
  WAITINGS_PERIODES_SETTINGS_NOT_FOUND(
      "La carence %s n'est pas paramétrée pour la période du %s au %s pour l'OC %s, l'offre %s et le produit %s"),
  PRODUCT_NOT_FOUND(
      "Pas de version d'offre disponible pour la société émettrice %s et le code produit %s"),
  BOBB_NO_PRODUCT_FOUND("La garantie %s ne référence aucun produit depuis le %s"),
  BOBB_SETTINGS_NOT_FOUND("Aucune correspondance Bobb pour : code=%s - codeAssureur=%s"),
  BOBB_NO_CORRESPONDANCE("Aucune correspondance trouvée pour les droits assuré et Bobb"),
  IS_DEMATERIALISED_CARD_NOT_FOUND(
      "isCarteDematerialisee n'est pas positionné sur le servicePrestation"),
  IS_PAPER_CARD_NOT_FOUND("isCartePapier n'est pas positionné sur le servicePrestation"),
  NO_START_DATE_ON_CARD_RIGHT_PERIODS(
      "La date de début de la période droits carte n'est pas positionnée sur le servicePrestation"),
  NO_END_DATE_ON_CARD_RIGHT_PERIODS(
      "La date de fin de la période droits carte n'est pas positionnée sur le servicePrestation"),
  NO_WARRANTY_FOR_THIS_CONTRACT(
      "Impossible de déterminer les domaines de droit du contrat %s pour l’assuré numero personne %s pour l’AMC %s"),
  SERVICE_SETTINGS_UI_NOT_RESPONDING("Le service Settings UI ne répond pas : %s"),
  WARRANTIES_CANCELED_OR_OUT_OF_BOUND("Toutes les garanties sont annulées ou hors périmètre"),
  WARRANTIES_IGNORED("L'ensemble des garanties de l'assuré sont à ignorer"),
  PRODUCT_WORKSHOP_ERROR("Une ou plusieurs erreurs lors de l'appel au Product Workshop : %s"),
  PRODUCT_WORKSHOP_SETTINGS_NOT_FOUND(
      "L'offre %s retourné de ProductWorkshop ne correspond pas à l'offre %s lié au paramétrage Bobb"),
  PRODUCTS_WARRANTIES_OVERLAPPING_DATES(
      "Les produits %s et %s ayant la même nature %s ont des dates qui se chevauchent"),
  UNKNOWN_EXCEPTION("Erreur de traitement du benef  %s :  %s"),
  CODE_CONVENTION_NOT_FOUND("%s introuvable dans le paramétrage de convention de la BDDS"),
  TECHNICAL_ERROR("Erreur technique"),
  PRODUCT_WORKSHOP_NO_COVERAGE(
      "Le paramétrage de l'atelier produit ne couvre pas la globalité des droits TP"),
  DOMAINE_SEVERAL_PARAMETERS(
      "Pour le produit %s de la société émettrice %s, le domaine %s a plusieurs paramètres et cela n’est pas autorisé"),
  NO_CARD_RIGHT_PERIODS("Période d'édition des cartes manquante sur le contrat"),
  NO_CARD_RIGHT_PARAM("Aucun paramétrage de carte TP trouvé"),
  ISSUE_BLOCKING_OTHER_BENEF_IN_TRIGGER(
      "Autre bénéficiaire du contrat bloqué par un paramétrage de carte TP manquant");

  private final String description;

  public String getDescription(Object... args) {
    return String.format(description, args);
  }
}
