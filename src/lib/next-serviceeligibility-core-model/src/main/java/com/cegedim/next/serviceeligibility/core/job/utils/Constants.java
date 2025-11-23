package com.cegedim.next.serviceeligibility.core.job.utils;

public class Constants {
  private Constants() {
    // Ignore default constructor
  }

  public static final int CODE_RETOUR_OK = 0;
  public static final int CODE_RETOUR_KAFKA_ERROR = 1;
  public static final int CODE_RETOUR_BAD_REQUEST = 2;
  public static final int CODE_RETOUR_UNEXPECTED_EXCEPTION = 3;

  public static final String SERVICE_PRESTATION_COLLECTION = "servicePrestation";
  public static final String PRESTIJ_COLLECTION = "servicePrestIJ";

  public static final String DATE_FORMAT = "yyyy-MM-dd";

  // renouvellementdroitstp-job
  public static final String HISTORIQUE_RENOUV_IS_RDO_FIELD = "isRdo";

  // Batch 620
  public static final String COLLECTION_CONSOLIDATION_CARTES = "declarationsConsolideesCarteDemat";
  public static final String COLLECTION_CONSOLIDATION_ALMERYS = "declarationsConsolideesAlmerys";
}
