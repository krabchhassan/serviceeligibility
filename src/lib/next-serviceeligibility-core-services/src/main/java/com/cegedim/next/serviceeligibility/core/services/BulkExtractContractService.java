package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.mapper.ExtractedContractMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BulkContracts;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ExtractedContract;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ExtractedDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ExtractedError;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.model.query.ContractRequest;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractTPAgregationService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class BulkExtractContractService {

  public static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})\\D*(\\d{2})\\D*(\\d{2})");
  public static final Pattern DATE_PATTERN_BIRTHDAY =
      Pattern.compile("(\\d{2})\\D*(\\d{2})\\D*(\\d{2})");

  public static final String SEARCH_DATE_FORMAT = "%s/%s/%s";
  public static final String BIRTHDATE_FORMAT = "%s%s%s";

  // AUTOWIRED
  private final ContractDao contractDao;
  private final ExtractedContractMapper mapper;
  private final ContractTPAgregationService agregationService;

  // --------------------
  // METHODS
  // --------------------
  public BulkContracts extract(final Collection<ContractRequest> bulkRequest) {
    final var init = new BulkContracts(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    return bulkRequest.stream().parallel().map(this::find).reduce(init, BulkContracts::merge);
  }

  // --------------------
  // UTILS
  // --------------------
  String hash(final ContractRequest r) {
    return (r.getHash() != null) ? r.getHash() : String.valueOf(r.hashCode());
  }

  ContractRequest sanitize(final ContractRequest dirty) throws DateTimeException {
    final var pretty = dirty.toBuilder();
    // init hash
    pretty.hash(hash(dirty));
    // birthDate: YYYYMMDD
    pretty.birthDate(sanitizeDate(dirty.getBirthDate(), BIRTHDATE_FORMAT));
    // searchDate: YYYY/MM/DD
    pretty.searchDate(sanitizeDate(dirty.getSearchDate(), SEARCH_DATE_FORMAT));
    // type: ONLINE / OFFLINE
    if (Objects.isNull(dirty.getType())) pretty.type(TypePeriode.OFFLINE);
    return pretty.build();
  }

  /**
   * Convertie la date au format <br>
   * ex: YYYY-MM-DD -> YYYY/MM/DD, YYYY.MM.DD -> YYYYMMDD
   *
   * @param date une date au format YYYY*MM*DD
   * @param targetFormat le format de sortie avec comme param {0} year, {1} month, {2} day
   * @return La date au bon format
   * @throws DateTimeException Si la date ne correspond pas au format d'entrée
   */
  String sanitizeDate(final String date, final String targetFormat) throws DateTimeException {
    if (Objects.isNull(date)) {
      return null; // early exit
    }

    // Handle 6-character date (YYMMDD format)
    if (date.length() == 6 && targetFormat.equals(BIRTHDATE_FORMAT)) {
      final var m = DATE_PATTERN_BIRTHDAY.matcher(date);
      return constructDate(date, targetFormat, m);
    }

    // Handle 8-character date (YYYYMMDD format)
    final var m = DATE_PATTERN.matcher(date);
    return constructDate(date, targetFormat, m);
  }

  private String constructDate(String date, String targetFormat, Matcher m) {
    if (!m.find()) {
      throw new DateTimeException(String.format("Invalid date format: %s", date));
    }

    String year = m.group(1);
    String month = m.group(2);
    String day = m.group(3);
    return String.format(targetFormat, year, month, day);
  }

  BulkContracts find(final ContractRequest request) {
    try {
      final var prettyRequest = this.sanitize(request);
      final var contracts = contractDao.findBy(prettyRequest);
      return this.map(prettyRequest, contracts);
    } catch (DateTimeException ex) {
      log.error("Sanitize error", ex);
      var error = new ExtractedError.InvalidDateFormat(request);
      return BulkContracts.builder().errors(Map.of(hash(request), error)).build();
    }
  }

  BulkContracts map(ContractRequest request, final Collection<ContractTP> contractTPS) {
    final var hash = hash(request);
    // NOT_FOUND
    if (CollectionUtils.isEmpty(contractTPS)) {
      var error = new ExtractedError.NotFound(request);
      return BulkContracts.builder().errors(Map.of(hash, error)).build();
    }

    final var extracted =
        contractTPS.stream()
            .flatMap(
                contract ->
                    mapper
                        .fromContract(
                            agregationService.agregationMailleReferenceCouverture(contract))
                        .stream())
            .filter(dto -> this.benefFilter(dto, request.getNir(), request.getBirthDate()))
            .map(dto -> this.reduceDomain(dto, request.getSearchDate(), request.getType()))
            .filter(dto -> !dto.getDomains().isEmpty())
            .toList();

    // NO_CONTRACT_FOR_DATE
    if (CollectionUtils.isEmpty(extracted)) {
      var error = new ExtractedError.NoActiveContract(request);
      return BulkContracts.builder().errors(Map.of(hash, error)).build();
    }
    return BulkContracts.builder().content(Map.of(hash, extracted)).build();
  }

  boolean benefFilter(ExtractedContract dto, @NotNull String nir, @Nullable String birthday) {
    // NIR & BIRTHDATE
    return (nir.equals(dto.getNirBeneficiaire())
        || nir.equals(dto.getNirOd1())
        || nir.equals(dto.getNirOd2()));
  }

  ExtractedContract reduceDomain(
      @NotNull ExtractedContract dto, @NotNull String date, @NotNull TypePeriode type) {
    // remove not matching domain
    List<ExtractedDomain> mutableDomains = new ArrayList<>(dto.getDomains());
    mutableDomains.removeIf(domain -> !this.domainFilter(domain, date, type));
    dto.setDomains(mutableDomains);
    return dto;
  }

  boolean domainFilter(ExtractedDomain dto, @NotNull String date, @NotNull TypePeriode type) {
    // TYPE_PERIODE && SEARCH_DATE
    return dto.getTypePeriode() == type
        && this.isBetween(date, dto.getPeriodeDebut(), dto.getPeriodeFin());
  }

  boolean isBetween(@NotNull String date, @NotNull String start, @Nullable String end) {
    // NullSafe (start <= d) && (end >= d)
    return ObjectUtils.compare(start, date) <= 0 && ObjectUtils.compare(end, date, true) >= 0;
  }
}
