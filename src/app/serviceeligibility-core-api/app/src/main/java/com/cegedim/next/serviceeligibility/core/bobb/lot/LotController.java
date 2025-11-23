package com.cegedim.next.serviceeligibility.core.bobb.lot;

import static com.cegedim.next.serviceeligibility.core.utils.InstanceProperties.TARGET_ENV;

import com.cegedim.beyond.spring.configuration.properties.BeyondPropertiesService;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.TechnicalGuaranteeRequest;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.bobb.LotRequest;
import com.cegedim.next.serviceeligibility.core.bobb.services.LotService;
import com.cegedim.next.serviceeligibility.core.model.entity.LotResponse;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/lot")
public class LotController {

  private final BeyondPropertiesService beyondPropertiesService;
  private final LotService service;
  private final LotXlsxService lotXlsxService;

  @GetMapping(value = "/getLot")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<LotResponse> getLot(LotRequest request) {
    return new ResponseEntity<>(service.getLot(request), HttpStatus.OK);
  }

  @GetMapping(value = "/getLotsById")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<List<Lot>> getLotsById(String ids) {
    List<String> idLots = Util.stringToList(ids);
    return new ResponseEntity<>(service.getLotsById(idLots), HttpStatus.OK);
  }

  @GetMapping(value = "/getLots")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<List<Lot>> getLots() {
    return new ResponseEntity<>(service.getLots(), HttpStatus.OK);
  }

  @GetMapping(value = "/getLotsByParametrageCarteTPActifs")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<List<Lot>> getLotsByParametrageCarteTPActifs() {
    return new ResponseEntity<>(service.getLotsByParametrageCarteTPActifs(), HttpStatus.OK);
  }

  @GetMapping(value = "/getLotsAlmerysValides")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<List<Lot>> getLotsAlmerysValides() {
    return new ResponseEntity<>(service.getLotsAlmerysValides(), HttpStatus.OK);
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  @NewSpan
  public ResponseEntity<Void> createLot(@Valid @RequestBody Lot lot) {
    try {
      checkLotValues(lot);
      service.createLot(lot);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  @PostMapping("/update")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<Void> updateLot(@Valid @RequestBody Lot lot) {
    try {
      checkLotValues(lot);
      service.updateLot(lot);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  @GetMapping("/export")
  @NewSpan
  public ResponseEntity<byte[]> export(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

    final String filename = getFilename();
    final byte[] bytePayload = lotXlsxService.exportLots(filename, authHeader);

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setContentDispositionFormData("attachment", filename);

    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytePayload);
  }

  @GetMapping("/exportOne/{codeLot}")
  @NewSpan
  public ResponseEntity<byte[]> exportOne(
      @PathVariable("codeLot") @NonNull final String codeLot,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

    final String filename = getFilename();
    final byte[] bytePayload = lotXlsxService.exportLot(codeLot, filename, authHeader);

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setContentDispositionFormData("attachment", filename);

    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytePayload);
  }

  @PostMapping("/import")
  @ResponseStatus(HttpStatus.CREATED)
  @NewSpan
  public ResponseEntity<List<Lot>> singleImportVersionFromXls(
      @RequestParam("file") final MultipartFile file,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    return new ResponseEntity<>(lotXlsxService.importLot(file, authHeader), HttpStatus.CREATED);
  }

  @PostMapping("/{lotCode}/associate-gt")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<Void> associateGtToLot(
      @PathVariable String lotCode,
      @RequestBody @Valid TechnicalGuaranteeRequest technicalGuaranteeRequest) {
    service.addGarantieTechniqueToLot(lotCode, technicalGuaranteeRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{lotCode}/dissociate-gt")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<Void> dissociateGt(
      @PathVariable String lotCode,
      @RequestBody @Valid TechnicalGuaranteeRequest technicalGuaranteeRequest) {
    service.dissociateGtFromLot(lotCode, technicalGuaranteeRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private String getFilename() {
    final ZonedDateTime zt = ZonedDateTime.now(ZoneOffset.UTC);
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy-hh-mm-ss");
    final String dateFormatted = dtf.format(zt);
    return String.format(
        "%s_%s_%s_%s.xlsx",
        StringUtils.capitalize("export").replace(" ", ""),
        "lots",
        dateFormatted,
        beyondPropertiesService.getPropertyOrThrowError(TARGET_ENV));
  }

  private void checkLotValues(Lot lot) {
    if (lot.getGarantieTechniques() == null || lot.getGarantieTechniques().size() < 2) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "A lot must contain at least 2 technical guarantees.");
    }
    if (StringUtils.isBlank(lot.getCode())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "The code of the lot must not be empty");
    }
    if (StringUtils.isBlank(lot.getLibelle())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "The label of the lot must not be empty");
    }
    checkGarantieTechniques(lot.getGarantieTechniques());
    removeSpacesInLot(lot);
  }

  private void checkGarantieTechniques(List<GarantieTechnique> garantieTechniques) {
    garantieTechniques.forEach(
        garantieTechnique -> {
          if (StringUtils.isBlank(garantieTechnique.getCodeGarantie())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "The guarantee code must not be empty");
          }
          if (StringUtils.isBlank(garantieTechnique.getCodeAssureur())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "The insurer code must not be empty");
          }
        });
  }

  private void removeSpacesInLot(Lot lot) {
    lot.setCode(lot.getCode().strip());
    lot.setLibelle(lot.getLibelle().strip());
    lot.getGarantieTechniques()
        .forEach(
            garantieTechnique -> {
              garantieTechnique.setCodeGarantie(garantieTechnique.getCodeGarantie().strip());
              garantieTechnique.setCodeAssureur(garantieTechnique.getCodeAssureur().strip());
            });
  }
}
