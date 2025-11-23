package com.cegedim.next.serviceeligibility.batch635.job.helpers;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentLists {
  private static List<String> alreadyUsedExtractionFiles = new ArrayList<>();
  private static List<String> alreadyUsedTmpExtractionFiles = new ArrayList<>();

  public static synchronized boolean alreadyUsedExtractionFilesContains(String fileName) {
    boolean exists = alreadyUsedExtractionFiles.contains(fileName);
    if (!exists) {
      alreadyUsedExtractionFiles.add(fileName);
    }
    return exists;
  }

  public static synchronized boolean alreadyUsedTmpExtractionFilesContains(String tmpFileName) {
    boolean exists = alreadyUsedTmpExtractionFiles.contains(tmpFileName);
    if (!exists) {
      alreadyUsedTmpExtractionFiles.add(tmpFileName);
    }
    return exists;
  }

  /** /** Private constructor. */
  private ConcurrentLists() {}
}
