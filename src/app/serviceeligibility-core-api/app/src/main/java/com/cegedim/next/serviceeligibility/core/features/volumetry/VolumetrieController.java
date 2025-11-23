package com.cegedim.next.serviceeligibility.core.features.volumetry;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.TARGET_ENV;
import static org.springframework.http.HttpHeaders.*;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.base.filenamegenerator.FileNameGenerator;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.volumetrie.VolumetrieDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utility.UriConstants;
import com.cegedim.next.serviceeligibility.core.features.volumetry.process.VolumetrieProcess;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.ByteArrayInputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Consommateur des appels clients REST pour les Profils. */
@AllArgsConstructor
@Controller
@RequestMapping("/v1" + UriConstants.VOLUMETRIE)
public class VolumetrieController {
  private final BeyondPropertiesService beyondPropertiesService;
  private final VolumetrieProcess volumetrieProcess;

  /**
   * Resource REST qui trouve la liste de volumetrie.
   *
   * @return la reponse avec la liste de volumetrie.
   */
  @NewSpan
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<VolumetrieDto>> getVolumetries() {
    final List<VolumetrieDto> volumetrieList = volumetrieProcess.getLastVolumetries();
    return new ResponseEntity<>(volumetrieList, HttpStatus.OK);
  }

  @NewSpan
  @GetMapping(value = "/excel")
  public ResponseEntity<InputStreamResource> exportAsXLS(
      @RequestParam(value = "amc", required = false) String amc,
      @RequestParam(value = "codePartenaire", required = false) String codePartenaire,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    byte[] fileContent =
        volumetrieProcess.getFilteredVolumetrieAsXLS(amc, codePartenaire, authHeader);
    return ResponseEntity.status(HttpStatus.OK)
        .headers(prepareResponseHeaders())
        .body(new InputStreamResource(new ByteArrayInputStream(fileContent)));
  }

  private HttpHeaders prepareResponseHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String fileName =
        FileNameGenerator.generateFileName(
            "Export_" + beyondPropertiesService.getPropertyOrThrowError(TARGET_ENV),
            ZonedDateTime.now(ZoneOffset.UTC),
            "xlsx");
    headers.add(CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
    headers.add(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
    headers.add(ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.GET.name());
    headers.add(CACHE_CONTROL, "no-cache");
    return headers;
  }
}
