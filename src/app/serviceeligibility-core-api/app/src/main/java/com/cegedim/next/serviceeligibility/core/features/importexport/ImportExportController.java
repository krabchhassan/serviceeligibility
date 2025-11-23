package com.cegedim.next.serviceeligibility.core.features.importexport;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.TARGET_ENV;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.CREATE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.base.filenamegenerator.FileNameGenerator;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.importexport.RestImportDto;
import com.cegedim.next.serviceeligibility.core.features.importexport.dto.*;
import com.cegedim.next.serviceeligibility.core.features.importexport.service.ImportExportService;
import com.cegedim.next.serviceeligibility.core.features.importexport.service.ParametrageImportExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
public class ImportExportController {

  private static final String CONTENT_PATTERN = "attachment; filename=";
  private static final String JSON = "json";

  private final BeyondPropertiesService beyondPropertiesService;

  private final ImportExportService service;

  private final ParametrageImportExportService parametrageService;

  @NewSpan
  @PostMapping(value = "/v1/importParametrage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(CREATE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> importParametrage(@Valid final ImportParameter importParameter)
      throws IOException {
    final ParametrageImportExportDto dto =
        buildParametrageImportExportDto(importParameter.getUploadedFile());
    parametrageService.importData(dto);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  private ParametrageImportExportDto buildParametrageImportExportDto(
      final MultipartFile multipartFile) throws IOException {
    ParametrageImportExportDto dto;

    if (multipartFile == null || multipartFile.isEmpty()) {
      dto = new ParametrageImportExportDto();
    } else {
      try (final ByteArrayInputStream importStream =
          new ByteArrayInputStream(multipartFile.getBytes())) {
        final String restImportDtoJson = IOUtils.toString(importStream, StandardCharsets.UTF_8);

        final RestParametrageImportDto restImportDto =
            new ObjectMapper().readValue(restImportDtoJson, RestParametrageImportDto.class);

        dto = restImportDto.getData();
      }
    }

    return dto;
  }

  @NewSpan
  @GetMapping(value = "/v1/export", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<RestImportDto> exportAll() {
    final HttpHeaders headers = buildExportFileHttpHeaders(false);
    RestImportDto exportDto = new RestImportDto();
    exportDto.setData(this.service.exportAll());
    return new ResponseEntity<>(exportDto, headers, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/v1/exportParametrage", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<RestParametrageImportDto> exportParametrage() {
    final HttpHeaders headers = buildExportFileHttpHeaders(true);
    RestParametrageImportDto exportDto = new RestParametrageImportDto();
    exportDto.setData(this.parametrageService.exportAll());
    return new ResponseEntity<>(exportDto, headers, HttpStatus.OK);
  }

  private HttpHeaders buildExportFileHttpHeaders(boolean isParametrage) {
    final String fileName =
        FileNameGenerator.generateFileName(
            "Serviceeligibility_"
                + (isParametrage ? "parametrage" : "")
                + beyondPropertiesService.getPropertyOrThrowError(TARGET_ENV),
            ZonedDateTime.now(ZoneOffset.UTC),
            JSON);

    final String contentDisposition = CONTENT_PATTERN.concat(fileName);

    final HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

    return headers;
  }
}
