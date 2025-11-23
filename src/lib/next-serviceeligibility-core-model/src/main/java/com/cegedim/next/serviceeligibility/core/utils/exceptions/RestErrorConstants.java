package com.cegedim.next.serviceeligibility.core.utils.exceptions;

/** This class defines some constants on rest errors. */
public final class RestErrorConstants {
  /** Basic prefix code */
  public static final String PREFIX_CODE = "NEXT-SERVICEELIGIBILITY-CORE";

  /** Occurs when there is a constraint violation */
  public static final String MISSING_QUERY_PARAM = PREFIX_CODE + "-00051";

  /** Occurs when there is a data integration exception */
  public static final String ERROR_CODE_DATA_INTEGRATION_BDD = PREFIX_CODE + "-00052";

  /** Error for null pointer exceptions. */
  public static final String ERROR_CODE_NPE = PREFIX_CODE + "-00053";

  /** Error for request validation exceptions. */
  public static final String ERROR_CODE_REQUEST_VALIDATION_EXCEPTION = PREFIX_CODE + "-00054";

  /** Error for service prestation not found */
  public static final String ERROR_CODE_SERVICE_PRESTATION_NOT_FOUND_EXCEPTION =
      PREFIX_CODE + "-00055";

  /** Error for BOBB not found */
  public static final String ERROR_CODE_BOBB_NOT_FOUND_EXCEPTION = PREFIX_CODE + "-00056";

  /** Error for Parametrage Carte TP not found */
  public static final String ERROR_CODE_PARAMETRAGE_CARTE_TP_NOT_FOUND_EXCEPTION =
      PREFIX_CODE + "-00057";

  /** Error for Declencheur Carte TP */
  public static final String ERROR_CODE_DECLENCHEUR_CARTE_EXCEPTION = PREFIX_CODE + "-00058";

  /** Error for BOBB not responding */
  public static final String ERROR_CODE_BOBB_NOT_RESPONDING_EXCEPTION = PREFIX_CODE + "-00059";

  /** Error for OC not responding */
  public static final String ERROR_CODE_OC_NOT_RESPONDING_EXCEPTION = PREFIX_CODE + "-00060";

  /** Error for PW not responding */
  public static final String ERROR_CODE_PW_NOT_RESPONDING_EXCEPTION = PREFIX_CODE + "-00061";

  /** Error for missing variable in RequestValidator */
  public static final String ERROR_CODE_MISSING_VARIABLE = PREFIX_CODE + "-00062";

  /** Error for wrong context in UAP */
  public static final String ERROR_CODE_PAU_UNKNOWN_CONTEXT = PREFIX_CODE + "-00063";

  /** Error for invalid date format in UAP */
  public static final String ERROR_CODE_PAU_INVALID_DATE_FORMAT = PREFIX_CODE + "-00064";

  /** Error for the start date greater than the end date in UAP */
  public static final String ERROR_CODE_PAU_DATE_START_GTE_END = PREFIX_CODE + "-00065";

  /** Error for the not found contract for collective data */
  public static final String ERROR_CODE_CONTRACT_COLLECTIVE_DATA_NOT_FOUND = PREFIX_CODE + "-00066";

  /** Error for beneficiary not found in UAP */
  public static final String ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND = PREFIX_CODE + "-00067";

  /** Error for servicePrestation not found in UAP */
  public static final String ERROR_CODE_PAU_SERVICE_PRESTATION_NOT_FOUND = PREFIX_CODE + "-00068";

  /** Error for servicePrestation not found in UAP */
  public static final String ERROR_CALL_OC_OR_PW = PREFIX_CODE + "-00069";

  /** Error for ElasticSearch index/alias creation */
  public static final String ERROR_INDEX_ALIAS_ELASTIC = PREFIX_CODE + "-00072";

  /** Error for servicePrestation not found in UAP */
  public static final String ERROR_CODE_PAU_RESILIATED_CONTRACT = PREFIX_CODE + "-00160";

  /** Error for servicePrestation not found in UAP */
  public static final String ERROR_CODE_PAU_CLOSED_RIGHTS = PREFIX_CODE + "-00161";

  /** Error for servicePrestation not found in UAP */
  public static final String ERROR_CODE_PAU_CLOSED_EXPENSE_TYPE = PREFIX_CODE + "-00162";

  /** Error for beneficiary not found in UAP */
  public static final String ERROR_CODE_PAU_UNKNOWN_BENEFICIARY = PREFIX_CODE + "-00163";

  /** Error for beneficiary not found in UAP */
  public static final String ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND_WITHOUT_SUBSCRIBER =
      PREFIX_CODE + "-00164";

  /** Error for beneficiary not found in UAP */
  public static final String ERROR_CODE_PAU_BENEFICIARY_NOT_FOUND_WITH_BENEFICIARY_ID =
      PREFIX_CODE + "-00165";

  /** Error for benefPaymentRecipients */
  public static final String ERROR_DIGITAL_CONTRACT_NOT_FOUND = PREFIX_CODE + "-00070";

  /** Error for benefPaymentRecipients */
  public static final String ERROR_BENEF_PAYMENT_RECIPIENT_DATE_BAD_FORMAT = PREFIX_CODE + "-00071";

  /** Error for benefPaymentRecipients */
  public static final String ERROR_BENEF_FOR_BENEFIT_RECIPIENT_NOTFOUND = PREFIX_CODE + "-00073";

  /** Error : generation of TP rights */
  public static final String ERROR_CODE_NO_TP_RIGHTS_CREATED_FOR_PERIOD_REQUESTED =
      PREFIX_CODE + "-00074";

  /** Error : bad call to Product Workshop */
  public static final String ERROR_CODE_PRODUCT_WORKSHOP_ERROR = PREFIX_CODE + "-00075";

  /** Error for contractsByBeneficiary */
  public static final String ERROR_CONTRACTS_BY_BENEF_NOT_FOUND = PREFIX_CODE + "-00076";

  /** Error for GT already exists */
  public static final String ERROR_GUARANTEE = PREFIX_CODE + "-00089";

  /** Error for unauthorized users */
  public static final String ERROR_USER_NOT_AUTHORIZED = PREFIX_CODE + "-00090";

  public static final String ERROR_CONTRACT_ELEMENT_NOT_FOUND = PREFIX_CODE + "-00091";

  /** /** Private constructor. */
  private RestErrorConstants() {}
}
