package com.cegedim.next.serviceeligibility.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BenefSearchConstants {

  public static final String CONTRATS_DATA_NOM_FAMILLE = "contrats.data.nom.nomFamille";

  public static final String CONTRATS_DATA_NOM_USAGE = "contrats.data.nom.nomUsage";

  public static final String CONTRATS_DATA_NOM_PRENOM = "contrats.data.nom.prenom";

  public static final String IDENTITE_NIR_CODE = "identite.nir.code";

  public static final String IDENTITE_AFFILIATIONS_RO_NIR_CODE = "identite.affiliationsRO.nir.code";

  public static final String IDDECLARANT = "amc.idDeclarant";

  public static final String CONTRATS_NUMERO_CONTRAT = "contrats.numeroContrat";
  public static final String CONTRATS_NUMERO_ADHERENT = "contrats.numeroAdherent";

  public static final String IDENTITE_DATE_NAISSANCE = "identite.dateNaissance";

  public static final String IDENTITE_RANG_NAISSANCE = "identite.rangNaissance";

  public static final String IDENTITE_HISTORIQUE_DATE_RANG_NAISSANCE_DATE_NAISSANCE =
      "identite.historiqueDateRangNaissance.dateNaissance";

  public static final String CONTRATS_DATA_ADRESSE_CODE_POSTAL = "contrats.data.adresse.codePostal";

  public static final String CONTRATS_SOCIETE_EMETTRICE = "contrats.societeEmettrice";

  public static final String CONTRATS_NUMERO_CONTRAT_KEYWORD = "contrats.numeroContrat.keyword";
  public static final String CONTRATS_NUMERO_ADHERENT_KEYWORD = "contrats.numeroAdherent.keyword";
  public static final String KEYWORD = ".keyword";
  public static final String STEMMER = ".stemmer";

  public static final String CONTRATS_IN_COLLECTION_BENEF = "contrats";
  public static final String CONTRATS_PERIODES = "contrats.periodes";
  public static final String CONTRATS_PERIODES_DEBUT = "contrats.periodes.debut";
  public static final String CONTRATS_PERIODES_FIN = "contrats.periodes.fin";

  public static final String SOCIETES_EMETTRICES_PERIODES = "societesEmettrices.periodes";
  public static final String SOCIETES_EMETTRICES_PERIODES_DEBUT =
      "societesEmettrices.periodes.debut";
  public static final String SOCIETES_EMETTRICES_PERIODES_FIN = "societesEmettrices.periodes.fin";

  public static final int MAX_SIZE_INNER_HITS = 100;
  public static final int SIZE_NO_INNER_HITS = 0;
}
