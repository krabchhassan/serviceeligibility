package com.cegedim.next.serviceeligibility.almerysacl.batch.services;

import com.cegedim.next.serviceeligibility.almerysacl.batch.constants.Constants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SftpService {

  @Setter private boolean isUnitTest = false;
  @Getter @Setter private int lastReceived = 0;

  @Value("${WORKDIR_INPUT_FOLDER:/workdir/blue/tmp/testaclin/}")
  private String inputFolder;

  @Value("${WORKDIR_OUTPUT_FOLDER:/workdir/blue/tmp/testaclout/}")
  private String outputFolder;

  @Value("${ARCHIVE_PATH}")
  private String archivePath;

  @Value("${issuer:98549603}")
  private String issuer;

  private final String newLine = System.lineSeparator();

  @Setter OutputStreamWriter unitTestWriter;

  Writer getWriter(String shortFileName) throws IOException {
    if (isUnitTest) {
      return unitTestWriter;
    }
    return new FileWriter(outputFolder + shortFileName);
  }

  public List<String> listFiles() throws IOException {
    Set<String> result;

    try (Stream<Path> stream = Files.walk(Paths.get(inputFolder), 1)) {
      result =
          stream
              .filter(file -> !Files.isDirectory(file))
              .map(Path::getFileName)
              .map(Path::toString)
              .collect(Collectors.toSet());
      return new ArrayList<>(result);
    } catch (IOException e) {
      log.error("could not list files in folder {} error:{}", inputFolder, e.getMessage());
      throw e;
    }
  }

  public boolean processFolder(List<String> files) {
    boolean foundIssue = false;
    log.info("Folder {} contains {} files", inputFolder, files.size());

    for (String file : files) {
      String fileName = inputFolder + file;
      log.info("processing file {}", fileName);
      boolean foundIssueInFile = processFile(fileName, file);
      foundIssue = foundIssue || foundIssueInFile;
      createArchiveOrDeleteOutputFileInError(file, foundIssueInFile);
    }

    if (!foundIssue) {
      for (String file : files) {
        deleteFile(inputFolder + file);
      }
    }

    return foundIssue;
  }

  public boolean processFile(String file, String shortFileName) {
    boolean foundIssue = false;
    boolean emptyFile = false;

    log.debug("before reading a file {}", file);
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      int lineNumber = 0;
      String line = reader.readLine();
      if (line == null) {
        emptyFile = true;
        log.info("File {} empty, ignored", file);
      } else {
        boolean shouldMoveFile = writeOutputFile(shortFileName, line, reader, lineNumber);
        if (shouldMoveFile) {
          // Copie le fichier input vers output si aucun traitement fait
          Files.copy(
              Path.of(inputFolder, shortFileName),
              Path.of(outputFolder, shortFileName),
              StandardCopyOption.REPLACE_EXISTING);
        }
      }
    } catch (IOException e) {
      foundIssue = true;
      log.info("Couldn't read the file {}:{}", file, e.getMessage());
    }
    if (emptyFile && !isUnitTest) {
      manageEmptyFile(file, shortFileName);
    }
    return foundIssue;
  }

  /**
   * Lit ligne par ligne un fichier et ecrit le fichier de sortie avec un seul produit d'ordre 1 si
   * plusieurs produits ont le même ordre et si la date de sortie de ces produits est dans la même
   * année civile
   *
   * @return true si le fichier d'entrée n'est pas éligible à la modification => il faut donc
   *     déplacer le fichier d'entrée vers le dossier de sortie false si le fichier d'entrée est
   *     éligible et qu'un nouveau fichier contenant les modifications a été créé dans le dossier de
   *     sortie
   * @throws IOException
   */
  private boolean writeOutputFile(
      String shortFileName, String line, BufferedReader reader, int lineNumber) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(getWriter(shortFileName))) {
      writer.write(line);
      writer.write(newLine);
      boolean foundEmitteur = false;

      while (reader.ready()) {
        line = readLine(reader, shortFileName, lineNumber);
        if (lineNumber % 100000 == 0) {
          log.info("lineNumber: {}", lineNumber);
        }
        lineNumber++;

        if (!foundEmitteur && line.contains(Constants.EMETTEUR_START_TAG)) {
          String value =
              line.replace(Constants.EMETTEUR_START_TAG, "")
                  .replace(Constants.EMETTEUR_END_TAG, "")
                  .trim();
          writer.write(line);
          writer.write(newLine);

          if (issuer.equalsIgnoreCase(value)) {
            foundEmitteur = true;
            log.info("Found issuer {} Will process file for issuer {}", value, issuer);
          } else {
            log.info(
                "Found issuer {} looking for {} Will move file {}", value, issuer, shortFileName);
            return true;
          }
        } else {
          writeProductLine(line, reader, lineNumber, writer);
        }
      }
    }
    return false;
  }

  private void writeProductLine(
      String line, BufferedReader reader, int lineNumber, BufferedWriter writer)
      throws IOException {
    if (line.contains(Constants.PRODUCT_START_TAG)) {
      writer.write(getLastProduct(line + newLine, reader, lineNumber));
    } else {
      writer.write(line);
      writer.write(newLine);
    }
  }

  private String readLine(BufferedReader reader, String shortFileName, int lineNumber)
      throws IOException {
    String line = reader.readLine();

    // BLUE-5655 : throw an error if a blank line is read to prevent batch from
    // crashing
    if (StringUtils.isBlank(line)) {
      String error =
          String.format(
              "Error in file %s : a blank line was read at line %d, moving to the next file...",
              shortFileName, lineNumber);
      log.error(error);
      throw new IOException(error);
    }

    return line;
  }

  int getOrder(String line) {
    try {
      String rawOrder =
          line.replace(Constants.ORDER_START_TAG, "")
              .replace(Constants.ORDER_END_TAG, "")
              .replace(" ", "");
      if (!rawOrder.isEmpty()) {
        return Integer.parseInt(rawOrder);
      }
    } catch (NumberFormatException e) {
      log.info("couldn't extract order from {}", line);
    }
    return -1;
  }

  String getAnneeSortie(String line) {
    try {
      String rawOrder =
          line.replace(Constants.DATE_SORTIE_PRODUIT_START_TAG, "")
              .replace(Constants.DATE_SORTIE_PRODUIT_END_TAG, "")
              .trim();
      if (rawOrder.length() > 3) {
        return rawOrder.substring(0, 4);
      }
    } catch (NumberFormatException e) {
      log.info("couldn't extract date from {}", line);
    }
    return "9999";
  }

  private static class ProduitAlmerys {
    int ordre = -1;
    String anneeSortie = "9999";
    StringBuilder content = new StringBuilder();
  }

  String getLastProduct(String firstProductLine, BufferedReader reader, int lineNumber)
      throws IOException {
    // Première ligne déjà lue
    StringBuilder fullContent = new StringBuilder(firstProductLine);
    String line;
    String firstLine = null;
    Map<String, ProduitAlmerys> yearProduct = new HashMap<>();
    ProduitAlmerys product = new ProduitAlmerys();
    int padding;
    Set<Integer> listOrder = new HashSet<>();
    boolean noOrder = false;
    StringBuilder lastLine = new StringBuilder();

    while (reader.ready()) {
      line = reader.readLine();
      fullContent.append(line).append(newLine);
      lineNumber++;

      if (firstLine == null) {
        padding = line.length() - line.stripLeading().length();
        firstLine =
            " ".repeat(padding)
                + Constants.ORDER_START_TAG
                + "1"
                + Constants.ORDER_END_TAG
                + newLine;
        product.content.append(firstProductLine).append(firstLine);
      }

      generateProduct(product, line);

      if (line.contains(Constants.PRODUCT_END_TAG)) {
        noOrder = addProductOrderInSetIfMissing(listOrder, product, noOrder);

        ProduitAlmerys productForYear = yearProduct.get(product.anneeSortie);

        if (productForYear == null || productForYear.ordre < product.ordre) {
          yearProduct.put(product.anneeSortie, product);
        }

        line = reader.readLine();
        fullContent.append(line).append(newLine);
        lineNumber++;

        if (!line.contains(Constants.PRODUCT_START_TAG)) {
          lastLine.append(line).append(newLine);
          break;
        } else {
          product = new ProduitAlmerys();
          product.content.append(firstProductLine).append(firstLine);
        }
      }
    }

    StringBuilder productBuilder = getProductBuilder(noOrder, fullContent, yearProduct, lastLine);

    return productBuilder.toString();
  }

  private static StringBuilder getProductBuilder(
      boolean noOrder,
      StringBuilder fullContent,
      Map<String, ProduitAlmerys> yearProduct,
      StringBuilder lastLine) {
    StringBuilder productBuilder = new StringBuilder();

    if (noOrder) {
      productBuilder.append(fullContent);
    } else {
      Collection<ProduitAlmerys> pList = yearProduct.values();
      pList.forEach(item -> productBuilder.append(item.content.toString()));
      productBuilder.append(lastLine);
    }
    return productBuilder;
  }

  private static boolean addProductOrderInSetIfMissing(
      Set<Integer> listOrder, ProduitAlmerys product, boolean noOrder) {
    if (!listOrder.contains(product.ordre)) {
      listOrder.add(product.ordre);
    } else {
      noOrder = true;
    }
    return noOrder;
  }

  void generateProduct(ProduitAlmerys product, String line) {
    if (line.contains(Constants.ORDER_START_TAG)) {
      product.ordre = getOrder(line);
    } else {
      if (line.contains(Constants.DATE_SORTIE_PRODUIT_START_TAG)) {
        product.anneeSortie = getAnneeSortie(line);
      }
      product.content.append(line).append(newLine);
    }
  }

  void deleteFile(String file) {
    try {
      if (!isUnitTest) {
        Path source = Path.of(file);
        Files.delete(source);
      }
    } catch (IOException e) {
      log.error("Couldn't delete file {}, error {}", file, e.getMessage());
    }
  }

  private void createArchiveOrDeleteOutputFileInError(String shortFileName, Boolean inError) {
    try {
      if (!isUnitTest) {
        if (Boolean.FALSE.equals(inError)) {
          Files.createDirectories(Paths.get(archivePath));
          if (Files.exists(Paths.get(outputFolder, shortFileName))) {
            Files.copy(
                Path.of(outputFolder, shortFileName),
                Path.of(archivePath, shortFileName),
                StandardCopyOption.REPLACE_EXISTING);
            log.info("File {} archived", shortFileName);
          }
        } else {
          if (Files.exists(Paths.get(outputFolder, shortFileName))) {
            Files.delete(Path.of(outputFolder, shortFileName));
            log.info("Output file in error {} deleted", shortFileName);
          }
        }
      }
    } catch (IOException e) {
      log.error(
          "Couldn't move file {} to the location {} : {}",
          inputFolder + shortFileName,
          archivePath + shortFileName,
          e.getMessage());
    }
  }

  private void manageEmptyFile(String file, String shortFileName) {
    try {
      Files.copy(
          Path.of(file), Path.of(outputFolder, shortFileName), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      log.error(
          "Couldn't copy empty file {} to the location {}, error {}",
          file,
          outputFolder + shortFileName,
          e.getMessage());
    }
  }
}
