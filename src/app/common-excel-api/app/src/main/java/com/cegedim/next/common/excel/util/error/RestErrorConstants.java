package com.cegedim.next.common.excel.util.error;

/** This class defines some constants on rest errors. */
public final class RestErrorConstants {

  /** Basic prefix code */
  public static final String PREFIX_CODE = "NEXT-COMMONEXCEL";

  /** Error for null pointer exceptions. */
  public static final String ERROR_NPE = PREFIX_CODE + "-00053";

  /** Error which occurs when we get a bind exception. */
  public static final String ERROR_BIND_EXCEPTION = PREFIX_CODE + "-00054";

  /** Error which occurs when we get a message not readable exception. */
  public static final String ERROR_MESSAGE_NOT_READABLE_EXCEPTION = PREFIX_CODE + "-00056";

  /** Error occurs when DocumentParameter#uploadedFile is not filled. */
  public static final String ERROR_VALIDITY_DOCUMENT_UPLOADED_FILE_NOT_FILLED =
      PREFIX_CODE + "-00057";

  /** Error occurs when there is request validation exceptions. */
  public static final String ERROR_REQUEST_VALIDATION_EXCEPTION = PREFIX_CODE + "-00058";

  /** Error occurs when loading the MS Excel file. */
  public static final String ERROR_LOADING_EXCEL_FILE = PREFIX_CODE + "-00059";

  /** Error occurs when JSON {@link String} is not filled. */
  public static final String ERROR_VALIDITY_JSON_EXPORT_NOT_FILLED = PREFIX_CODE + "-00060";

  /** Error occurs when the document is not filled. */
  public static final String ERROR_VALIDITY_DOCUMENT_NULL = PREFIX_CODE + "-00062";

  /** Error occurs when the metadata is not filled. */
  public static final String ERROR_VALIDITY_METADATA_NULL = PREFIX_CODE + "-00063";

  /** Error occurs when the metadata name is not filled. */
  public static final String ERROR_VALIDITY_METADATA_NAME_BLANK = PREFIX_CODE + "-00064";

  /** Error occurs when the metadata sheets number is <= 0. */
  public static final String ERROR_VALIDITY_METADATA_SHEETS_NUMBER_LESS_THAN_OR_EQUAL_TO_ZERO =
      PREFIX_CODE + "-00065";

  /** Error occurs when the sheet name of the sheet content is not filled. */
  public static final String ERROR_VALIDITY_SHEET_CONTENT_SHEET_NAME_BLANK = PREFIX_CODE + "-00068";

  /** Error occurs when the sheet name of the the sheet content is not unique. */
  public static final String ERROR_VALIDITY_SHEET_CONTENT_SHEET_NAME_NOT_UNIQUE =
      PREFIX_CODE + "-00069";

  /** Error occurs when the sheet name of the sheet content is unknown. */
  public static final String ERROR_VALIDITY_SHEET_CONTENT_SHEET_NAME_UNKNOWN =
      PREFIX_CODE + "-00070";

  /** Error occurs when the data line number is not filled. */
  public static final String ERROR_VALIDITY_DATA_LINE_NUMBER_NULL = PREFIX_CODE + "-00072";

  /** Error occurs when the data line number is < 1. */
  public static final String ERROR_VALIDITY_DATA_LINE_NUMBER_LESS_THAN_ONE = PREFIX_CODE + "-00073";

  /** Error occurs when the data line has at least one column not standard. */
  public static final String ERROR_VALIDITY_DATA_LINE_NOT_STANDARD_COLUMN = PREFIX_CODE + "-00074";

  /** Error occurs when the Excel sheet is not filled. */
  public static final String ERROR_VALIDITY_EXCEL_SHEET_NULL = PREFIX_CODE + "-00075";

  /** Error occurs when the Excel sheet name is not filled. */
  public static final String ERROR_VALIDITY_EXCEL_SHEET_NAME_BLANK = PREFIX_CODE + "-00076";

  /** Error occurs when the Excel sheet number is not filled. */
  public static final String ERROR_VALIDITY_EXCEL_SHEET_NUMBER_NULL = PREFIX_CODE + "-00077";

  /** Error occurs when the Excel sheet number is < 0. */
  public static final String ERROR_VALIDITY_EXCEL_SHEET_NUMBER_LESS_THAN_ZERO =
      PREFIX_CODE + "-00078";

  /** Error occurs when the Excel sheet number is not unique. */
  public static final String ERROR_VALIDITY_EXCEL_SHEET_NUMBER_NOT_UNIQUE = PREFIX_CODE + "-00079";

  /** Error occurs when a column is not filled. */
  public static final String ERROR_VALIDITY_COLUMN_NULL = PREFIX_CODE + "-00080";

  /** Error occurs when a column name is not filled. */
  public static final String ERROR_VALIDITY_COLUMN_NAME_BLANK = PREFIX_CODE + "-00081";

  /** Error occurs when a column position is < 0. */
  public static final String ERROR_VALIDITY_COLUMN_POSITION_LESS_THAN_ZERO = PREFIX_CODE + "-00082";

  /** Error occurs when a column position is not unique. */
  public static final String ERROR_VALIDITY_COLUMN_POSITION_NOT_UNIQUE = PREFIX_CODE + "-00083";

  /** Error occurs when a column name is not unique. */
  public static final String ERROR_VALIDITY_COLUMN_NAME_NOT_UNIQUE = PREFIX_CODE + "-00084";

  /** Error occurs when a column format is not valid. */
  public static final String ERROR_VALIDITY_COLUMN_FORMAT_NOT_VALID = PREFIX_CODE + "-00085";

  /** Error occurs when we generate an Excel file. */
  public static final String ERROR_GENERATING_EXCEL_FILE = PREFIX_CODE + "-00086";

  /** Error occurs when the metadata name has not the XLSX file extension. */
  public static final String ERROR_VALIDITY_METADATA_NAME_NOT_XLSX = PREFIX_CODE + "-00087";

  /** Error occurs when a line number is not unique in the same sheet. */
  public static final String ERROR_VALIDITY_DATA_LINE_NUMBER_NOT_UNIQUE = PREFIX_CODE + "-00088";

  /** /** Private constructor. */
  private RestErrorConstants() {}
}
