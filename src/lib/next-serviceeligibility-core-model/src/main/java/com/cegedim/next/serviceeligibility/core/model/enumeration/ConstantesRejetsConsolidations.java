package com.cegedim.next.serviceeligibility.core.model.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConstantesRejetsConsolidations {

  /** Constante Almerys */
  REJET_C01("C01", "DROITS NON AGREGES POUR LE PRODUIT ET LE DOMAINE", "S", "B", 2),

  REJET_C02("C02", "GARANTIES NON PRIORISEES", "S", "B", 2),

  REJET_C12("C12", "CODE EXTERNE PRODUIT ABSENT", "S", "B", 2),

  REJET_C13("C13", "AUCUN DOMAINE AVEC UN TAUX DE REMBOURSEMENT SUPERIEUR A ZERO", "M", "B", 2),

  REJET_C14("C14", "UNITES INCOMPATIBLES", "S", "B", 2),

  REJET_C16("C16", "TAUX DE COUVERTURE NON NUMERIQUE", "S", "B", 2),

  REJET_C17("C17", "PROBLEME SUR AU MOINS UN AUTRE BENEFICIAIRE DU CONTRAT", "S", "B", 2),

  REJET_C18(
      "C18",
      "AUCUN DROIT OUVERT POUR L ENSEMBLE DES DECLARATIONS CONSOLIDEES DU CONTRAT X",
      "S",
      "B",
      2),

  REJET_C19("C19", "IMPOSSIBLE DE DEFINIR UNE ADRESSE POUR LE CONTRAT X", "S", "B", 1),

  REJET_C20(
      "C20",
      "LES DOMAINES TP ONT DES CODES RENVOIS, CODES RENVOIS ADDITIONNELS, UNITES DE TAUX OU CONVENTIONNEMENTS DIFFERENTS",
      "S",
      "B",
      2),

  REJET_C21(
      "C21",
      "LE PARAMETRAGE DE REGROUPEMENT DOIT ETRE IDENTIQUE MAIS LA NATURE Y EST ABSENTE",
      "S",
      "B",
      2),

  REJET_C22(
      "C22",
      "LE PARAMÉTRAGE DE REGROUPEMENT DOIT ÊTRE IDENTIQUE MAIS LES TAUX NE SONT PAS EGAUX",
      "S",
      "B",
      2),

  REJET_C23("C23", "LE PARAMETRAGE S3 NE PERMET PAS D EXTRAIRE LA CARTE PAPIER", "S", "B", 1),

  REJET_C24(
      "C24", "LES DOMAINES TP NE SONT PAS LES MEMES POUR TOUS LES BENEFICIAIRES", "S", "B", 2),

  REJET_C25(
      "C25", "LE CODE OFFRE ITELIS N EST PAS LE MEME POUR TOUS LES BENEFICIAIRES", "S", "B", 2),

  REJET_O01("O01", "ORGANISATION PRINCIPALE NON TROUVEE", "R", "B", 2),

  REJET_O02("O02", "ORGANISATION SECONDAIRE NON TROUVEE", "R", "B", 2),

  REJET_O03(
      "O03", "ORGANISATION SECONDAIRE NON RATTACHEE A L ORGANISATION PRINCIPALE", "R", "B", 2),

  REJET_O04("O04", "PROBLEME TECHNIQUE LORS DE L APPEL A ORGANISATION", "R", "B", 2),

  REJET_P20("P20", "GARANTIE NON PARAMETREE DANS LE PARAMETRAGE DE PRODUIT ALMERYS", "S", "B", 2),

  REJET_P21("P21", "DOUBLON DE PRODUIT ALMERYS", "S", "B", 2);

  /** Constante Carte TP */
  private final String code;

  private final String message;
  private final String typeErreur;
  private final String niveau;
  private final int service; // 0 : carte demat, 1 : carte papier, 2 les 2.

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
          + arg.toString()
          + ";"
          + this.typeErreur
          + ";"
          + this.niveau
          + "#";
    }
  }

  public static ConstantesRejetsConsolidations findByCode(String code) {
    ConstantesRejetsConsolidations result = null;
    for (ConstantesRejetsConsolidations constantesRejetsConsolidations : values()) {
      if (constantesRejetsConsolidations.name().equalsIgnoreCase(code)) {
        result = constantesRejetsConsolidations;
        break;
      }
    }
    return result;
  }
}
