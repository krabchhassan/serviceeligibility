package com.cegedim.next.serviceeligibility.core.bobbcorrespondence;

import static com.cegedim.next.serviceeligibility.core.common.CommonConstant.*;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.DELETE_CONTRACT_PERMISSION;
import static com.cegedim.next.serviceeligibility.core.utils.PermissionConstants.READ_PERMISSION;

import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ClosePeriodRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CloseProductElementRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CorrespondenceResponseDto;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.GroupBy;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.model.SearchCriteria;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services.BobbCorrespondanceService;
import com.cegedim.next.serviceeligibility.core.dto.GuaranteeSearchResultDto;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bobb-correspondences")
public class BobbCorrespondenceController {
  private final BobbCorrespondanceService bobbCorrespondanceService;

  @GetMapping("/products/by-gt/{gtId}")
  @PreAuthorize(READ_PERMISSION)
  public CorrespondenceResponseDto getProductsByGtId(
      @PathVariable("gtId") @NonNull final String gtId,
      @RequestParam(name = "groupBy", required = false, defaultValue = "period") String groupBy,
      @RequestParam(name = "appliedDate", required = false) LocalDate appliedDate) {
    final GroupBy criterion;
    try {
      criterion = GroupBy.fromString(groupBy);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    return bobbCorrespondanceService.getProductsByGtId(gtId, criterion, appliedDate);
  }

  @GetMapping("/products/by-codes/{codeContractElement}/{codeInsurer}")
  @PreAuthorize(READ_PERMISSION)
  public CorrespondenceResponseDto getProductsByCodes(
      @PathVariable("codeContractElement") @NonNull final String codeContractElement,
      @PathVariable("codeInsurer") @NonNull final String codeInsurer,
      @RequestParam(name = "groupBy", required = false, defaultValue = "period") String groupBy,
      @RequestParam(name = "appliedDate", required = false) LocalDate appliedDate) {

    final GroupBy criterion;
    try {
      criterion = GroupBy.fromString(groupBy);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    return bobbCorrespondanceService.getProductsByCodes(
        codeContractElement, codeInsurer, criterion, appliedDate);
  }

  @GetMapping("/contract-elements/search")
  @PreAuthorize(READ_PERMISSION)
  public Page<GuaranteeSearchResultDto> search(
      @ModelAttribute SearchCriteria request,
      @RequestParam Map<String, String> allParams,
      @PageableDefault(size = 20) Pageable pageable) {
    Set<String> allowed =
        Set.of(
            CODE_INSURER,
            CODE_GARANTEE,
            SOCIETE_EMETTRICE,
            CODE_OFFER,
            CODE_PRODUCT,
            START_DATE,
            END_DATE);

    bobbCorrespondanceService.checkParams(allParams, allowed);
    return bobbCorrespondanceService.search(request, pageable);
  }

  @PatchMapping("/{gtId}/product-closures")
  @PreAuthorize(DELETE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> closeProductElementByGt(
      @PathVariable("gtId") String gtId, @RequestBody @Valid CloseProductElementRequest body) {
    if (gtId == null || gtId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gtId manquant.");
    }
    if (body == null || body.selector() == null || body.to() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requête de clôture invalide.");
    }

    int updated = bobbCorrespondanceService.closeOneProductElementByGt(gtId, body);
    return updated > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @PatchMapping("/{gtId}/period-closures")
  @PreAuthorize(DELETE_CONTRACT_PERMISSION)
  public ResponseEntity<Void> closePeriodByGt(
      @PathVariable("gtId") String gtId, @RequestBody @Valid ClosePeriodRequest body) {

    if (gtId == null || gtId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "gtId manquant.");
    }
    if (body == null || body.period() == null || body.to() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Requête de clôture de période invalide.");
    }

    int updated = bobbCorrespondanceService.closePeriodByGt(gtId, body);
    return updated > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @GetMapping(value = "/guarantees", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<String>> getGuaranteeCodes(
      @RequestParam(required = false) String codeOffer,
      @RequestParam(required = false) String codeAmc,
      @RequestParam Map<String, String> allParams) {

    Set<String> allowed = Set.of(CODE_OFFER, CODE_AMC);

    bobbCorrespondanceService.checkParams(allParams, allowed);
    String offer = StringUtils.hasText(codeOffer) ? codeOffer.trim() : null;
    String amc = StringUtils.hasText(codeAmc) ? codeAmc.trim() : null;
    List<String> guarantees = bobbCorrespondanceService.findGuaranteeCodes(offer, amc);
    if (guarantees == null || guarantees.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(guarantees);
  }

  @GetMapping(value = "/offers", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<String>> getOfferCodes(
      @RequestParam(required = false) String codeContractElement,
      @RequestParam(required = false) String codeAmc,
      @RequestParam Map<String, String> allParams) {
    Set<String> allowed = Set.of(CODE_CONTRACT_ELEMENT, CODE_AMC);

    bobbCorrespondanceService.checkParams(allParams, allowed);

    String guarantee = StringUtils.hasText(codeContractElement) ? codeContractElement.trim() : null;
    String amc = StringUtils.hasText(codeAmc) ? codeAmc.trim() : null;

    List<String> offers = bobbCorrespondanceService.findOfferCodes(guarantee, amc);

    if (offers == null || offers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(offers);
  }

  @GetMapping(value = "/amcCodes", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(READ_PERMISSION)
  public ResponseEntity<List<String>> getAmcCodes(
      @RequestParam(required = false) String codeOffer,
      @RequestParam(required = false) String codeContractElement,
      @RequestParam Map<String, String> allParams) {
    Set<String> allowed = Set.of(CODE_OFFER, CODE_CONTRACT_ELEMENT);
    bobbCorrespondanceService.checkParams(allParams, allowed);

    String offer = StringUtils.hasText(codeOffer) ? codeOffer.trim() : null;
    String guarantee = StringUtils.hasText(codeContractElement) ? codeContractElement.trim() : null;

    List<String> amcCodes = bobbCorrespondanceService.findAmcCodes(offer, guarantee);

    if (amcCodes == null || amcCodes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(amcCodes);
  }
}
