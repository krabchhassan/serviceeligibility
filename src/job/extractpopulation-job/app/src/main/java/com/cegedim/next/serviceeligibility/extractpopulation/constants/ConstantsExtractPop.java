package com.cegedim.next.serviceeligibility.extractpopulation.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantsExtractPop {
  public static final int INVALID_ARGUMENT = -1;
  public static final String JSON = "JSON";
  public static final String CSV = "CSV";
  public static final String JSON_EXTENSION = ".json";
  public static final String CSV_EXTENSION = ".csv";

  // Headers CSV
  public static final String NUM_AMC = "N° AMC";
  public static final String NUM_ADHERENT = "N° Adhérent";
  public static final String NUM_CONTRAT = "N° de contrat";
  public static final String NUM_PERSONNE = "N° de la personne rattachée au contrat";

  // Util pour .meta
  public static final String ISSUER = "ExtractionPopulation-job";

  // Nom fichiers
  public static final String EXTRACTION_CONTRATS = "Extraction_Contrats_";
  public static final String EXTRACTION_CONTRATS_ASSURES = "Extraction_Contrats_Assures_";
  public static final String EXTRACTION_CONTRATS_ASSURES_GARANTIES =
      "Extraction_Contrats_Assures_Garanties_";

  public static final int MAX_CONTRACTS_PER_FILE_DEFAULT = 1000000;
}
