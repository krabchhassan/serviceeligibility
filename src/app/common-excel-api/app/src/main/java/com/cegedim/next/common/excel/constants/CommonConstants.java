package com.cegedim.next.common.excel.constants;

/** This class defines commons constants. */
public final class CommonConstants {

  /** The API name. */
  public static final String API_NAME = "next.common.excel.api";

  /** The Bastion console configuration API name. */
  public static final String BASTION_CONSOLE_CONFIGURATION_API_NAME =
      "BASTION-CONSOLE-CONFIGURATION-API";

  /** The Excel content type for XLS. */
  public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";

  /** The Excel content type for XLSX. */
  public static final String XLSX_CONTENT_TYPE =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  /** The Excel file extension for XLSX. */
  public static final String XLSX_FILE_EXTENSION = "xlsx";

  /** Max document size in bytes (10 Mo). */
  public static final long MAX_FILE_SIZE_IN_BYTES = 10485760;

  /** Max document size in megabytes (10 Mo). */
  public static final long MAX_FILE_SIZE_IN_MEGA_BYTES = (MAX_FILE_SIZE_IN_BYTES / 1024) / 1024;

  /** The document author for Excel file building. */
  public static final String DOCUMENT_AUTHOR = "CEGEDIM";

  /** The document description for Excel file building. */
  public static final String DOCUMENT_DESCRIPTION = "Generated Excel file";

  /** Private constructor. */
  private CommonConstants() {}
}
