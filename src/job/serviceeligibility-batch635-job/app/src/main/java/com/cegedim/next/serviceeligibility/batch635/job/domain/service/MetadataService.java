package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import static com.cegedim.next.serviceeligibility.batch635.job.utils.Constants.ESCAPED_DOT;

import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MetadataService.class);

  @ContinueSpan(log = "generateMetadataFile")
  public static void generateMetadataFile(String fileName, String folderPath, String issuer) {
    String filePath = Paths.get(folderPath, fileName).toString();
    createMetadata(filePath, issuer);
  }

  private static void createMetadata(String csvFilePath, String issuer) {
    try {
      Path filePath = Paths.get(csvFilePath);
      BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
      LocalDateTime createdOn =
          LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
      String meta =
          create(filePath.toFile(), StandardCharsets.UTF_8.displayName(), createdOn, issuer);
      LOGGER.debug("meta: {}", meta);
      Files.writeString(Paths.get(csvFilePath + ".meta"), meta, StandardOpenOption.CREATE);
    } catch (IOException ex) {
      LOGGER.error(
          String.format("Can't open and read the file %s %s", csvFilePath, ex.getMessage()));
      throw new IllegalStateException(ex);
    }
  }

  private static String create(
      File file, String charset, LocalDateTime dateCreatedOn, String issuer) {
    JSONObject metaFile = new JSONObject();
    if (file.getName().startsWith(".")) {
      metaFile.put("fileName", file.getName().replaceFirst(ESCAPED_DOT, ""));
    } else {
      metaFile.put("fileName", file.getName());
    }

    try {
      FileInputStream input = new FileInputStream(file);
      metaFile.put("checksum", DigestUtils.sha1Hex(input));
      input.close();
      metaFile.put("charset", charset);
      metaFile.put("fileSize", file.length());
    } catch (FileNotFoundException ex) {
      LOGGER.error(String.format("File not found: %s", file.getName()));
      throw new IllegalStateException(ex);
    } catch (SecurityException | IOException ex) {
      LOGGER.error(String.format("Can't read file: %s", file.getName()));
      throw new IllegalStateException(ex);
    }

    metaFile.put(
        "creationDate", dateCreatedOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    metaFile.put("issuer", issuer);
    return metaFile.toString();
  }

  /** /** Private constructor. */
  private MetadataService() {}
}
