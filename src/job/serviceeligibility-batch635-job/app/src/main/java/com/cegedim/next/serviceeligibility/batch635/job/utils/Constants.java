package com.cegedim.next.serviceeligibility.batch635.job.utils;

import java.nio.charset.StandardCharsets;

public class Constants {
  public static final String MICROSERVICE_NAME = "next-serviceeligibility-batch635-job";

  public static final String JOB_BEAN_NAME = "batch635";

  public static final String CONTRATS_COLLECTION = "contratsTP";
  public static final String DECLARAMTS_COLLECTION = "declarants";

  public static final String OUTPUT_TMP_FOLDER = "c:/tmp/serviceeligibility_output_tmp/";
  public static final String ARL_FILE_NAME_PREFIX = "ARL_635";
  public static final String ELIGIBLE_LINES_TMP_FILE_NAME_PREFIX = "Eligible_Lines_";
  public static final String EXTRACTION_START_FILE = "Extract_droit_635";
  public static final String EXTRACTION_START_FILE_INPUT = "Extraction_droit_635";

  public static final String COMMON_FILE_ENCODING = StandardCharsets.UTF_8.toString();

  public static final String CSV_EXTENSION = "csv";
  public static final String DELIMITER = ";";
  public static final String DOT = ".";
  public static final String UNDERSCORE = "_";
  public static final String ESCAPED_DOT = "\\.";
  public static final String DASH = "-";
  public static final String META_EXTENSION = ".meta";

  public static final String JOB_NAME = "serviceeligibility-job-635";
  public static final String PARAMETERS_CONTROL_STEP_NAME = "stepForParametersControl";
  public static final String EXTRACTION_STEP_NAME = "stepForExtraction";

  public static final String TIME_KEY = "time";
  public static final String FILE_NAME_KEY = "fileName";
  public static final String EMPTY_INPUT_FILE_KEY = "emptyInputFile";
  public static final String ELIGIBLE_LINES_TMP_FILE_KEY = "eligibleLinesTmpFileName";
  public static final String HIDDEN_ARL_FILE_NAME = "arlFileName";

  public static final double BLOCKING_COEF = 0.9;

  // Event though we can launch more threads concurrently, we don't want to
  // overload the database with more than five request concurrently.
  public static final int MAXIMUM_THREADS_TO_HANDLE = 5;

  public static final String DATABASE_TYPE = "BDD";
  public static final String EXTRACTION_INDICATOR_V = "V";
  public static final String EXTRACTION_INDICATOR_R = "R";

  public static final String TMP_EXTRACTION_FILE_PREFIX = "TMP_EXTRACTION";

  /** /** Private constructor. */
  private Constants() {}
}
