package com.cegedim.next.serviceeligibility.core.services.s3;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.common.base.s3.client.exceptions.S3Exception;
import com.cegedim.common.base.s3.minioclient.service.S3StorageService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3StorageService s3StorageService;
  private final ObjectMapper mapper;
  private final BeyondPropertiesService beyondPropertiesService;

  public JsonNode readS3File(String fullFilePath) {
    JsonNode fileContent = null;
    try {
      InputStream s3FileContent =
          s3StorageService.readFileFromS3Storage(
              beyondPropertiesService.getPropertyOrThrowError(S3_BUCKET), fullFilePath);
      if (s3FileContent != null) {
        fileContent = mapper.readValue(s3FileContent, JsonNode.class);
      } else {
        log.error(
            "Erreur lors de la récupération du fichier {} : pas de contenu récupéré", fullFilePath);
      }
    } catch (IOException | S3Exception e) {
      log.error(
          "Erreur lors de la récupération du fichier de paramétrage S3 {} : {}",
          fullFilePath,
          e.getMessage());
    }
    return fileContent;
  }

  /**
   * Upload le fichier d extraction des histo consos purges (date sauvegarde + d'un an) dans le
   * dossier s3 cible
   */
  public String writeToS3bucketHistoConsos(File extract, String fileName) throws S3Exception {
    String path =
        beyondPropertiesService.getPropertyOrThrowError(S3_EXTRACTION_HISTO_CONSOS)
            + S3StorageService.S3_SEPARATOR_FOLDER
            + fileName;
    return writeToS3bucketPath(extract, path);
  }

  /** Ecrit le File dans le path cible du bucket s3 parametre */
  private String writeToS3bucketPath(File extract, String path) throws S3Exception {
    s3StorageService.uploadFileToS3Storage(
        beyondPropertiesService.getPropertyOrThrowError(S3_BUCKET), extract, path);
    return beyondPropertiesService.getPropertyOrThrowError(S3_BUCKET)
        + S3StorageService.S3_SEPARATOR_FOLDER
        + path;
  }

  /** Upload le fichier d export des trigger benef sur s3 */
  public String writeToS3bucketTriggerBenef(File extract, String triggerId) throws S3Exception {
    String path =
        beyondPropertiesService.getPropertyOrThrowError(S3_EXPORT_TRIGGER_BENEF_PATH)
            + S3StorageService.S3_SEPARATOR_FOLDER
            + triggerId
            + Constants.CSV;
    return writeToS3bucketPath(extract, path);
  }

  /** Recupere le csv d export des infos d un trigger sur s3 via son idTrigger */
  public Stream<String> readTriggerBenefFile(String triggerId) throws S3Exception {
    String fullFilePath =
        beyondPropertiesService.getPropertyOrThrowError(S3_EXPORT_TRIGGER_BENEF_PATH)
            + S3StorageService.S3_SEPARATOR_FOLDER
            + triggerId
            + Constants.CSV;
    InputStream s3FileContent =
        s3StorageService.readFileFromS3Storage(
            beyondPropertiesService.getPropertyOrThrowError(S3_BUCKET), fullFilePath);
    return new BufferedReader(new InputStreamReader(s3FileContent)).lines();
  }
}
