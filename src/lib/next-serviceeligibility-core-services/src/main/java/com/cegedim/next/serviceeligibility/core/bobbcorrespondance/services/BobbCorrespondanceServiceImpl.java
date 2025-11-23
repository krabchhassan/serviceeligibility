package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services;

import static com.cegedim.next.serviceeligibility.core.bobbcorrespondance.constants.CommonConstants.*;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.Versions;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ClosePeriodRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CloseProductElementRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CorrespondenceResponseDto;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.GroupBy;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.PeriodGroupDto;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.exceptions.ContractElementNotFoundException;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.exceptions.NoActiveVersionFoundException;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.model.*;
import com.cegedim.next.serviceeligibility.core.dto.GuaranteeSearchResultDto;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BobbCorrespondanceServiceImpl implements BobbCorrespondanceService {

  private final ContractElementRepository contractElementRepository;
  private final VersionsRepository versionsRepository;

  @Override
  public ContractElement findCorrespondance(String codeContractElement, String codeInsurer) {
    try {
      Versions activeVersion = findAndGetActiveVersion();
      if (activeVersion == null) return null;
      log.info(
          "Searching in mongo database for: codeContractElement = {} and codeInsurer = {}",
          codeContractElement,
          codeInsurer);

      Optional<ContractElement> contractElement =
          contractElementRepository.findByKeyAndVersion(
              codeContractElement, codeInsurer, activeVersion.getId());

      if (contractElement.isPresent()) {
        log.info(
            "Correspondence found for codeContractElement={} and codeInsurer={} with active version={}",
            codeContractElement,
            codeInsurer,
            activeVersion.getId());
        return contractElement.get();
      } else {
        log.info(
            "No correspondence found for codeContractElement={} and codeInsurer={} with active version={}",
            codeContractElement,
            codeInsurer,
            activeVersion.getId());
        return null;
      }
    } catch (Exception e) {
      log.error(
          "Error occurred while fetching data from Mongodb for: codeContractElement = {} and codeInsurer = {}",
          codeContractElement,
          codeInsurer,
          e);
      return null;
    }
  }

  @Override
  public boolean isCorrespondenceExist(String codeContractElement, String codeInsurer) {
    Versions activeVersion = findAndGetActiveVersion();
    if (activeVersion == null) {
      return false;
    }
    if (contractElementRepository.existsByKeyAndVersion(
        codeContractElement, codeInsurer, activeVersion.getId())) {
      log.info(
          "Correspondence exist in MongoDB for codeContractElement : {}, codeInsurer : {}, activeVersion id: {}",
          codeContractElement,
          codeInsurer,
          activeVersion.getId());
      return true;
    } else {
      log.info(
          "Correspondence not exist in MongoDB for codeContractElement : {}, codeInsurer : {}, activeVersion id: {}",
          codeContractElement,
          codeInsurer,
          activeVersion.getId());
      return false;
    }
  }

  @Override
  public CorrespondenceResponseDto getProductsByGtId(
      String gtId, GroupBy groupBy, LocalDate appliedDate) {
    final Versions active = findAndGetActiveVersion();
    if (active == null) {
      throw new NoActiveVersionFoundException("No active version found");
    }

    final Optional<ContractElement> optionalContract =
        contractElementRepository.findByIdAndVersion(gtId, active.getId());
    if (optionalContract.isEmpty()) {
      throw new ContractElementNotFoundException("ContractElement not found");
    }
    ContractElement contract = optionalContract.get();

    if (appliedDate != null) {
      contract.setProductElements(
          filterProductsElementsByAppliedDate(contract, appliedDate.atStartOfDay()));
    }

    final List<ProductElement> sorted = sortProductsElements(contract);

    if (groupBy == GroupBy.PRODUCT) {
      return CorrespondenceResponseDto.product(
          contract.getId(), contract.getCodeInsurer(), contract.getCodeContractElement(), sorted);
    }

    // GroupBy.PERIOD
    record PeriodKey(LocalDateTime start, LocalDateTime end) {}
    Map<PeriodKey, List<ProductElement>> grouped =
        sorted.stream().collect(Collectors.groupingBy(v -> new PeriodKey(v.getFrom(), v.getTo())));

    List<PeriodGroupDto> periods =
        grouped.entrySet().stream()
            .sorted(
                Comparator.comparing(
                        (Map.Entry<PeriodKey, ?> e) -> e.getKey().start(),
                        Comparator.nullsFirst(Comparator.naturalOrder()))
                    .thenComparing(
                        e -> e.getKey().end(), Comparator.nullsLast(Comparator.naturalOrder())))
            .map(e -> new PeriodGroupDto(e.getKey().start(), e.getKey().end(), e.getValue()))
            .toList();

    return CorrespondenceResponseDto.period(
        contract.getId(), contract.getCodeInsurer(), contract.getCodeContractElement(), periods);
  }

  @NotNull
  private static List<ProductElement> sortProductsElements(ContractElement contract) {
    final Comparator<ProductElement> cmp =
        Comparator.comparing(
                ProductElement::getFrom, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(ProductElement::getTo, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(ProductElement::getCodeOffer, Comparator.nullsLast(String::compareTo))
            .thenComparing(ProductElement::getCodeProduct, Comparator.nullsLast(String::compareTo))
            .thenComparing(
                ProductElement::getCodeBenefitNature, Comparator.nullsLast(String::compareTo));

    return contract.getProductElements().stream().sorted(cmp).toList();
  }

  @NotNull
  private static List<ProductElement> filterProductsElementsByAppliedDate(
      ContractElement contract, LocalDateTime appliedDate) {
    return contract.getProductElements().stream()
        .filter(pe -> isActiveOn(appliedDate, pe.getFrom(), pe.getTo()))
        .toList();
  }

  private static boolean isActiveOn(LocalDateTime date, LocalDateTime from, LocalDateTime to) {
    return (from == null || !date.isBefore(from)) && (to == null || !date.isAfter(to));
  }

  @Override
  public CorrespondenceResponseDto getProductsByCodes(
      String codeContractElement, String codeInsurer, GroupBy groupBy, LocalDate appliedDate) {
    ContractElement contract = findCorrespondance(codeContractElement, codeInsurer);
    if (contract == null) {
      throw new ContractElementNotFoundException("ContractElement not found");
    }
    return getProductsByGtId(contract.getId(), groupBy, appliedDate);
  }

  private Versions findAndGetActiveVersion() {
    log.info("Searching for active version in MongoDB...");
    Optional<Versions> activeVersion = versionsRepository.findActiveVersion();

    if (activeVersion.isEmpty()) {
      log.info("No active version found in MongoDB.");
      return null;
    } else {
      log.info("Active version found : {}", activeVersion);
    }
    return activeVersion.get();
  }

  @Override
  public int closeOneProductElementByGt(String gtId, CloseProductElementRequest body) {
    final Versions active = findAndGetActiveVersion();
    if (active == null) {
      log.error("No active version found in MongoDB. Cannot close product element.");
      throw new NoActiveVersionFoundException("No active version found");
    }
    int updated = contractElementRepository.closeOneProductElementByGt(gtId, active.getId(), body);

    log.info("Closed 1+ product element(s) on GT={} (modified={})", gtId, updated);
    return updated;
  }

  @Override
  public int closePeriodByGt(String gtId, ClosePeriodRequest body) {
    final Versions active = findAndGetActiveVersion();
    if (active == null) {
      log.error("No active version found in MongoDB. Cannot close product element.");
      throw new NoActiveVersionFoundException("No active version found");
    }

    int updated = contractElementRepository.closePeriodByGt(gtId, active.getId(), body);

    log.info("Closed period elements on GT={} (modified={})", gtId, updated);
    return updated;
  }

  @Override
  public Page<GuaranteeSearchResultDto> search(SearchCriteria criteria, Pageable pageable) {
    Versions activeVersion = findAndGetActiveVersion();
    Pageable enforced = enforceDefaultSort(pageable);
    if (activeVersion == null || activeVersion.getId() == null) {
      return Page.empty(enforced);
    }
    String versionId = activeVersion.getId();
    Query pageQuery = buildQuery(criteria, versionId);
    long total = contractElementRepository.count(pageQuery);
    if (total == 0) {
      return Page.empty(enforced);
    }
    List<ContractElement> content = contractElementRepository.find(pageQuery, enforced);
    Page<ContractElement> page = new PageImpl<>(content, enforced, total);
    return toGuaranteeSearchResultPage(page);
  }

  private Pageable enforceDefaultSort(Pageable p) {
    int page = (p == null) ? 0 : p.getPageNumber();
    int size = (p == null) ? 20 : p.getPageSize();
    return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, INSERT_DATE));
  }

  private Query buildQuery(SearchCriteria c, String versionId) {
    List<Criteria> rootAnds = new ArrayList<>();
    rootAnds.add(Criteria.where(VERSION_ID).is(versionId));
    if (StringUtils.hasText(c.getCodeInsurer())) {
      rootAnds.add(Criteria.where(CODE_INSURER).is(c.getCodeInsurer()));
    }
    if (StringUtils.hasText(c.getCodeGarantee())) {
      rootAnds.add(Criteria.where(CODE_CONTRACT_ELEMENT).is(c.getCodeGarantee()));
    }
    List<Criteria> peAnds = new ArrayList<>();
    if (StringUtils.hasText(c.getSocieteEmettrice())) {
      peAnds.add(Criteria.where(CODE_AMC).is(c.getSocieteEmettrice()));
    }
    if (StringUtils.hasText(c.getCodeOffer())) {
      peAnds.add(Criteria.where(CODE_OFFER).is(c.getCodeOffer()));
    }
    if (StringUtils.hasText(c.getCodeProduct())) {
      peAnds.add(Criteria.where(CODE_PRODUCT).is(c.getCodeProduct()));
    }
    Instant start = null, end = null;
    if (c.getStartDate() != null) {
      start = c.getStartDate().atStartOfDay(ZoneOffset.UTC).toInstant();
    }
    if (c.getEndDate() != null) {
      end = c.getEndDate().atStartOfDay(ZoneOffset.UTC).toInstant();
    }
    if (start != null) {
      peAnds.add(Criteria.where(FROM).gte(start));
    }
    if (end != null) {
      peAnds.add(Criteria.where(TO).lte(end));
    }

    if (!peAnds.isEmpty()) {
      Criteria elemMatch = new Criteria().andOperator(peAnds.toArray(new Criteria[0]));
      rootAnds.add(Criteria.where(PRODUCT_ELEMENTS).elemMatch(elemMatch));
    }

    Query q = new Query();
    q.addCriteria(new Criteria().andOperator(rootAnds.toArray(new Criteria[0])));
    return q;
  }

  private GuaranteeSearchResultDto toGuaranteeSearchResultDto(ContractElement e) {
    if (e == null) return null;
    return new GuaranteeSearchResultDto(
        e.getId(),
        e.getCodeContractElement(),
        e.getCodeInsurer(),
        Boolean.TRUE.equals(e.isIgnored()));
  }

  private Page<GuaranteeSearchResultDto> toGuaranteeSearchResultPage(Page<ContractElement> page) {
    return page.map(this::toGuaranteeSearchResultDto);
  }

  @Override
  public List<String> findGuaranteeCodes(String codeOffer, String codeAmc) {
    Versions activeVersion = findAndGetActiveVersion();
    if (activeVersion == null) return Collections.emptyList();
    return contractElementRepository.distinctGuaranteeCodesByOfferAndAmc(
        activeVersion.getId(), codeOffer, codeAmc);
  }

  @Override
  public List<String> findOfferCodes(String codeContractElement, String codeAmc) {
    Versions activeVersion = findAndGetActiveVersion();
    if (activeVersion == null) return Collections.emptyList();
    return contractElementRepository.distinctOfferCodesByGuaranteeAndAmc(
        activeVersion.getId(), codeContractElement, codeAmc);
  }

  @Override
  public List<String> findAmcCodes(String codeOffer, String codeContractElement) {
    Versions activeVersion = findAndGetActiveVersion();
    if (activeVersion == null) return Collections.emptyList();
    return contractElementRepository.distinctAmcByOfferAndGuarantee(
        activeVersion.getId(), codeOffer, codeContractElement);
  }

  @Override
  public void checkParams(Map<String, String> allParams, Set<String> allowed) {
    List<String> unknown = allParams.keySet().stream().filter(p -> !allowed.contains(p)).toList();

    if (!unknown.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query param : " + unknown);
    }
  }
}
