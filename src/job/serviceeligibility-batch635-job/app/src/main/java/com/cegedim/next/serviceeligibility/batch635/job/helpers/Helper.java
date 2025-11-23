package com.cegedim.next.serviceeligibility.batch635.job.helpers;

import static com.cegedim.next.serviceeligibility.batch635.job.domain.service.MetadataService.generateMetadataFile;
import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

public class Helper {
  private static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);

  public static final DateTimeFormatter COMMON_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd");
  public static final DateTimeFormatter PROJECTION_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy/MM/dd");
  public static final DateTimeFormatter EXTRACTION_TIME_DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

  public static boolean directoryExists(String path) {
    File dir = new File(path);
    return dir.exists();
  }

  public static void createDirectoryIfNotExists(String directory) {
    File dir = new File(directory);
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  public static void move(String from, String to) throws IOException {
    Files.move(Paths.get(from), Paths.get(to));
  }

  public static boolean fileIsEmpty(Path filePath) throws IOException {
    return Files.size(filePath) == 0;
  }

  public static String extractFileName(JobExecution jobExecution) {
    return (String) jobExecution.getJobParameters().getParameters().get(FILE_NAME_KEY).getValue();
  }

  public static String extractFileName(StepExecution stepExecution) {
    return extractFileName(stepExecution.getJobExecution());
  }

  public static String generateExtractionFileName(String declarantNo) {
    String extractionFileNamePrefix =
        DOT
            + EXTRACTION_START_FILE
            + UNDERSCORE
            + declarantNo
            + UNDERSCORE
            + handleReferenceDateWithBeyondFormat();
    return generateFileName(extractionFileNamePrefix);
  }

  public static String generateTmpExtractionFileName(String declarantNo) {
    String extractionFileNamePrefix =
        DOT
            + TMP_EXTRACTION_FILE_PREFIX
            + UNDERSCORE
            + EXTRACTION_START_FILE
            + UNDERSCORE
            + declarantNo
            + UNDERSCORE;
    return generateFileName(extractionFileNamePrefix);
  }

  private static String handleReferenceDateWithBeyondFormat() {
    return LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
  }

  public static FileAndMeta createMetaAndRenameFiles(String hiddenFileName, String dir)
      throws IOException {
    try {
      if (!Files.exists(Path.of(dir, hiddenFileName))) {
        LOGGER.error("File => '{}' doesn't exist!", hiddenFileName);
        return new FileAndMeta();
      }
      String visibleFileName = hiddenFileName.replaceFirst(ESCAPED_DOT, "");

      String hiddenFile = dir + hiddenFileName;
      String visibleFile = dir + visibleFileName;

      return generateMetaAndMove(hiddenFileName, dir, hiddenFile, visibleFile);
    } catch (Exception ex) {
      LOGGER.error("Error during metadata creation and copy to PEFB!", ex);
      throw ex;
    }
  }

  private static FileAndMeta generateMetaAndMove(
      String fileName, String dir, String from, String to) throws IOException {
    try {
      String loggedFileName = fileName.replaceFirst(ESCAPED_DOT, "");
      LOGGER.info("Generating metadata file for => '{}'", loggedFileName);

      generateMetadataFile(fileName, dir, MICROSERVICE_NAME);

      LOGGER.info("Moving the file => '{}' and its metadata to PEFB folder", loggedFileName);
      move(from + META_EXTENSION, to + META_EXTENSION);
      move(from, to);

      return FileAndMeta.builder()
          .filePath(Path.of(to))
          .metaFilePath(Path.of(to + META_EXTENSION))
          .build();
    } catch (Exception ex) {
      // Clean all files when encountering an error.
      deleteFileIfExists(from);
      deleteFileIfExists(to);
      deleteFileIfExists(from + META_EXTENSION);
      deleteFileIfExists(to + META_EXTENSION);
      throw ex;
    }
  }

  public static void deleteFilesFromDirectory(String folder) throws IOException {
    if (!directoryExists(folder)) {
      return;
    }
    try (Stream<Path> stream = Files.walk(Paths.get(folder), 1)) {
      stream
          .filter(file -> !Files.isDirectory(file))
          .map(path -> new File(path.toString()))
          .forEach(File::delete);
    } catch (IOException e) {
      LOGGER.error("Error while deleting files from directory => '{}'", folder);
      throw e;
    }
  }

  public static String generateFileName(String prefix) {
    try {
      return prefix + DOT + CSV_EXTENSION;
    } catch (Exception ex) {
      LOGGER.error("Error occured while generating file Name for {} ", prefix);
      throw new IllegalStateException(ex);
    }
  }

  public static String extractCivilYear(String date) {
    return date.substring(0, 4);
  }

  public static boolean isDateBetween(
      LocalDate startDate, LocalDate endDate, LocalDate targetDate) {
    return !targetDate.isAfter(endDate) && !targetDate.isBefore(startDate);
  }

  public static void deleteFileIfExists(String filePath) {
    if (filePath != null) {
      try {
        Files.deleteIfExists(Paths.get(filePath));
      } catch (NoSuchFileException e) {
        LOGGER.error("No such file/directory exists");
      } catch (DirectoryNotEmptyException e) {
        LOGGER.error("Directory is not empty.");
      } catch (IOException e) {
        LOGGER.error("Invalid permissions.");
      } catch (Exception ex) {
        LOGGER.error("Failed to delete file => " + filePath, ex);
      }
    }
  }

  public static void deleteFileIfExists(Path path) {
    if (path != null) {
      deleteFileIfExists(path.toString());
    }
  }

  public static void deleteFilesIfExists(Path... filesPaths) {
    if (filesPaths != null && filesPaths.length != 0) {
      for (Path filePath : filesPaths) {
        deleteFileIfExists(filePath);
      }
    }
  }

  public static int computeNumberOfThreads(int itemsSize, int injectedParallelAmcSize) {
    // Available cpu cores.
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    LOGGER.info("Number of available processors => '{}'", availableProcessors);

    // Optimal number of threads.
    int optimalNumberOfThreads = getOptimalNumberOfThreads(availableProcessors);
    LOGGER.info("Optimal number of threads => '{}'", optimalNumberOfThreads);

    int computedNumberOfThreads =
        getComputedNumberOfThreads(itemsSize, optimalNumberOfThreads, injectedParallelAmcSize);
    LOGGER.info("The final computed number of threads => '{}'", computedNumberOfThreads);

    return computedNumberOfThreads;
  }

  public static void copyFileContentContentAndAppendToLines(
      Path source, String toAppend, Path destination) throws IOException {
    PrintWriter extractionWriter = getCustomPrintWriter(destination);
    InputStream inputStream = new FileInputStream(source.toString());
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String oldLine;
      String newLine;
      while ((oldLine = br.readLine()) != null) {
        newLine = oldLine + DELIMITER + toAppend;
        extractionWriter.println(newLine);
      }
    } finally {
      extractionWriter.flush();
      extractionWriter.close();
    }
  }

  public static boolean fileExists(String dir, String fileName) {
    return Files.exists(Path.of(dir, fileName));
  }

  public static PrintWriter getCustomPrintWriter(Path filePath) throws FileNotFoundException {
    OutputStream outputStream = new FileOutputStream(filePath.toString(), false);
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
    return new PrintWriter(bufferedOutputStream);
  }

  private static int getComputedNumberOfThreads(
      int itemsSize, int optimalNumberOfThreads, int injectedParallelAmcSize) {
    // Supported number of threads is not always the optimal number,
    // because we have to take into consideration the number of supported concurrent
    // database requests.
    int supportedNumberOfThreads = Math.min(optimalNumberOfThreads, MAXIMUM_THREADS_TO_HANDLE);
    int threadsNbr = Math.min(injectedParallelAmcSize, supportedNumberOfThreads);
    // If the items size is less than the threads number there's no need to create
    // more threads than the number of items.
    return Math.min(itemsSize, threadsNbr);
  }

  private static int getOptimalNumberOfThreads(int availableProcessors) {
    return (int) (availableProcessors / (1 - BLOCKING_COEF));
  }

  /** /** Private constructor. */
  private Helper() {}
}
