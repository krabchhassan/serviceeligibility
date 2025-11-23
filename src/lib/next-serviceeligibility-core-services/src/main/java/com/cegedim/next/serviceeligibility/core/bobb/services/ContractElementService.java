package com.cegedim.next.serviceeligibility.core.bobb.services;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.error.ContractNotFoundException;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTResult;
import com.cegedim.next.serviceeligibility.core.bobb.util.ContractElementServiceUtil;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class ContractElementService {

  public static final String UNKNOWN_CONTRACT_ELEMENT_WITH_ID = "Unknown contract element with id ";
  private final ContractElementRepository repository;

  /**
   * Remove all contract elements and add the given ones. To be used to initialize data.
   *
   * @param elements the new contract elements
   * @throws IllegalArgumentException If a mandatory data is null
   */
  @ContinueSpan(log = "initializeData")
  public void initializeData(@NotNull final List<ContractElement> elements) {
    // remove all existing data
    repository.removeAll();

    // add all new data
    for (ContractElement element : elements) {
      create(element);
    }
  }

  /**
   * GREEN-5062: alert user when a new contract element is created.
   *
   * @param element contract element to create
   * @throws IllegalArgumentException If contract element or one of its mandatory data is null
   */
  @ContinueSpan(log = "create")
  public void create(@NotNull final ContractElement element) {
    // Check contract element is valid
    Assert.notNull(element, "Contract element must not be null");
    Assert.hasText(element.getCodeContractElement(), "Contract element's code must not be null");
    Assert.hasText(element.getCodeInsurer(), "Contract element's insurer must not be null");
    final Optional<ContractElement> existing =
        repository.findByKey(element.getCodeContractElement(), element.getCodeInsurer());
    if (existing.isEmpty()) {
      // Check all given product elements are also valid
      for (ProductElement productElement : element.getProductElements()) {
        validateProductElement(productElement, true);
      }
      final String id = UUID.randomUUID().toString();
      element.setId(id);
      repository.save(element);
    } else {
      // already in DB, check if must resend the alert
      final ContractElement previous = existing.get();
      // GREEN-5062 AC2: only if deadline is shorter
      if (isDeadlineShorter(element, previous)
          // GREEN-5063 AC6: only if no active mapping
          && !isMappingActive(previous)
          // GREEN-5065 AC5: only if not ignored
          && !previous.isIgnored()) {
        // (previous alert was resolved OR deadline is shorter) AND
        // there is no active mapping.
        // resend alert with this new deadline
        previous.setDeadline(element.getDeadline());
        previous.setOrigine(element.getOrigine());
        previous.setUser(element.getUser());
        // save new deadline for this contract element
        repository.save(previous);
      }
    }
  }

  /**
   * Validate a product element is not null and has valid mandatory data.
   *
   * @param productElement The product element to validate.
   * @throws IllegalArgumentException If a mandatory data is missing
   */
  private void validateProductElement(final ProductElement productElement, boolean create) {
    Assert.notNull(productElement, "Product element must not be null");
    Assert.hasText(productElement.getCodeOffer(), "Product element's offer must not be null");
    Assert.hasText(productElement.getCodeProduct(), "Product element's product must not be null");
    if (create) {
      Assert.notNull(
          productElement.getCodeBenefitNature(),
          "Product element's benefit nature must not be null");
    } else {
      Assert.hasText(
          productElement.getCodeBenefitNature(),
          "Product element's benefit nature must not be null");
    }
  }

  /**
   * Determine if new element's deadline is before existing element's deadline. Null means infinite
   * future.
   *
   * @param newContractElement the contract element with new deadline
   * @param existingContractElement the contract element with existing deadline
   * @return true if deadline is shorter, false otherwise
   */
  private boolean isDeadlineShorter(
      @NotNull final ContractElement newContractElement,
      @NotNull final ContractElement existingContractElement) {
    return newContractElement.getDeadline() != null
        && (existingContractElement.getDeadline() == null
            || existingContractElement.getDeadline().isAfter(newContractElement.getDeadline()));
  }

  /**
   * Determine if there is any existing mapping for this contract element at the current date.
   * Active means that mapping's period includes the current date.
   *
   * @param contractElement the contract element
   * @return true if there is an active mapping, false otherwise
   */
  private boolean isMappingActive(@NotNull final ContractElement contractElement) {
    final LocalDateTime now = LocalDateTime.now();
    for (ProductElement productElement : contractElement.getProductElements()) {
      if (isMappingActive(productElement, now)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determine if a mapping is active at given date.
   *
   * @param productElement mapped product element
   * @param date reference date
   * @return true if the mapping is active, false otherwise
   */
  @ContinueSpan(log = "isMappingActive")
  public boolean isMappingActive(
      @NotNull final ProductElement productElement, @NotNull final LocalDateTime date) {
    // date >= effective date
    return ((productElement.getEffectiveDate() == null
            || !LocalDateTime.now().isBefore(productElement.getEffectiveDate()))
        // from <= date
        && (productElement.getFrom() == null || !date.isBefore(productElement.getFrom()))
        // to >= date
        && (productElement.getTo() == null || !date.isAfter(productElement.getTo())));
  }

  /**
   * GREEN-5063 AC7: get existing contract elements.
   *
   * @param codeAMC optional filter, get all if null
   * @return the contract elements
   */
  @ContinueSpan(log = "get contract elements by amc")
  public Collection<ContractElement> get(final String codeAMC) {
    return repository.get(codeAMC);
  }

  /**
   * GREEN-5064 AC4: get a contract element.
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @return the contract element, if any
   */
  @ContinueSpan(log = "get contract elements by codeContractElement and codeInsurer (3 params)")
  @Cacheable(
      value = "bobb",
      key =
          "new org.springframework.cache.interceptor.SimpleKey(#codeContractElement, #codeInsurer,#includeIgnored)")
  public ContractElement get(
      @NotNull final String codeContractElement,
      @NotNull final String codeInsurer,
      boolean includeIgnored) {
    Optional<ContractElement> optionalContractElement =
        repository.getOptional(codeContractElement, codeInsurer, includeIgnored);
    ContractElement contractElement = optionalContractElement.orElse(null);
    removeCanceledProductElement(contractElement);
    mergeOverlappingProductElements(contractElement);

    return contractElement;
  }

  private void mergeOverlappingProductElements(ContractElement contractElement) {
    if (contractElement != null
        && CollectionUtils.isNotEmpty(contractElement.getProductElements())) {
      List<ProductElement> newProductElements = new ArrayList<>();

      Map<ProductElement, List<ProductElement>> groupedProduct =
          contractElement.getProductElements().stream()
              .collect(Collectors.groupingBy(Function.identity()));

      for (List<ProductElement> productElement : groupedProduct.values()) {
        productElement.sort(Comparator.comparing(ProductElement::getFrom));
        ProductElement prev = null;
        for (ProductElement product : productElement) {
          if (product.getEffectiveDate() != null
              && LocalDateTime.now().isBefore(product.getEffectiveDate())) {
            // Si le ProductElement n'est pas effectif a la date de traitement alors on ne
            // le merge pas
            newProductElements.add(product);
            continue;
          }
          if (prev != null) {
            boolean isOverlapping =
                DateUtils.isOverlapping(
                    prev.getFrom(), prev.getTo(), product.getFrom().minusDays(1), product.getTo());
            if (isOverlapping) {
              if (prev.getTo() == null || product.getTo() == null) {
                prev.setTo(null);
              } else {
                prev.setTo(DateUtils.getMaxDate(prev.getTo(), product.getTo()));
              }
            } else {
              newProductElements.add(product);
            }
          } else {
            newProductElements.add(product);
          }
          prev = product;
        }
      }

      contractElement.setProductElements(newProductElements);
    }
  }

  private void removeCanceledProductElement(ContractElement contractElement) {
    if (contractElement != null
        && CollectionUtils.isNotEmpty(contractElement.getProductElements())) {
      contractElement
          .getProductElements()
          .removeIf(product -> !ContractElementServiceUtil.isPeriodeProductElementValid(product));
    }
  }

  /**
   * GREEN-5063 map product element to contract element.
   *
   * @param contractElementId contract element's ID
   * @param productElement product element data
   * @throws IllegalArgumentException if contract element does not exist or product element is
   *     invalid
   */
  @ContinueSpan(log = "map product element to contract element")
  public void map(
      @NotNull final String contractElementId, @NotNull final ProductElement productElement) {
    validateProductElement(productElement, false);
    final Optional<ContractElement> existing = repository.findOneById(contractElementId);
    if (existing.isPresent()) {
      final ContractElement contractElement = existing.get();
      final ProductElement mappedProductElement =
          getMappedProductElement(contractElement, productElement);
      if (mappedProductElement == null) {
        // GREEN-5063 AC9: set new mapping
        contractElement.getProductElements().add(productElement);
      } else {
        // GREEN-5063 AC8: update existing mapping
        mappedProductElement.setFrom(productElement.getFrom());
        mappedProductElement.setTo(productElement.getTo());
        mappedProductElement.setEffectiveDate(productElement.getEffectiveDate());
      }

      // GREEN-5065 AC3: allowed even if contract element if flagged as
      // ignored,
      // removing this status
      contractElement.setIgnored(false);
      repository.save(contractElement);
    } else {
      throw new IllegalArgumentException(UNKNOWN_CONTRACT_ELEMENT_WITH_ID + contractElementId);
    }
  }

  @ContinueSpan(log = "update product element")
  public ContractElement update(
      @NotNull final String contractElementId, @NotNull final ProductElement productElement) {
    validateProductElement(productElement, false);
    final Optional<ContractElement> existing = repository.findOneById(contractElementId);
    if (existing.isPresent()) {
      final ContractElement contractElement = existing.get();
      contractElement.setProductElements(new ArrayList<>());
      contractElement.getProductElements().add(productElement);
      contractElement.setIgnored(false);
      repository.save(contractElement);
      return contractElement;
    } else {
      throw new IllegalArgumentException(UNKNOWN_CONTRACT_ELEMENT_WITH_ID + contractElementId);
    }
  }

  /**
   * Get the product element mapped to this contract element, if any.
   *
   * @param contractElement the contract element
   * @param productElement the requested product element
   * @return the mapped product element or null if not mapped
   */
  private ProductElement getMappedProductElement(
      @NotNull final ContractElement contractElement,
      @NotNull final ProductElement productElement) {
    for (ProductElement mappedProductElement : contractElement.getProductElements()) {
      if (mappedProductElement.equals(productElement)) {
        return mappedProductElement;
      }
    }
    return null;
  }

  /**
   * GREEN-5065: ignore alert for a contract element.
   *
   * @param contractElementId contract element to ignore
   * @throws IllegalArgumentException if contract element does not exist
   */
  @ContinueSpan(log = "setIgnored on product element")
  public void setIgnored(@NotNull final String contractElementId) {
    final Optional<ContractElement> existing = repository.findOneById(contractElementId);
    if (existing.isPresent()) {
      ContractElement contractElement = existing.get();
      contractElement.setIgnored(true);
      repository.save(contractElement);
    } else {
      throw new IllegalArgumentException(UNKNOWN_CONTRACT_ELEMENT_WITH_ID + contractElementId);
    }
  }

  /**
   * GREEN-5066: end an existing mapping
   *
   * @param contractElementId contract element's ID
   * @param productElement product element data
   * @throws IllegalArgumentException if contract element does not exist or product element is not
   *     mapped to
   */
  @ContinueSpan(log = "endMapping")
  public ContractElement endMapping(
      @NotNull final String contractElementId, @NotNull final ProductElement productElement) {
    final Optional<ContractElement> existing = repository.findOneById(contractElementId);
    if (existing.isPresent()) {
      final ContractElement contractElement = existing.get();
      final ProductElement mappedProductElement =
          getMappedProductElement(contractElement, productElement);
      if (mappedProductElement == null) {
        throw new ContractNotFoundException(
            "Product element "
                + productElement.getCodeOffer()
                + ", "
                + productElement.getCodeProduct()
                + ", "
                + productElement.getCodeBenefitNature()
                + " is not mapped to contract element ID "
                + contractElementId);
      } else {
        // GREEN-5066 AC1: set the new end date
        mappedProductElement.setTo(productElement.getTo());
        repository.save(contractElement);
      }

      return contractElement;
    } else {
      throw new ContractNotFoundException(UNKNOWN_CONTRACT_ELEMENT_WITH_ID + contractElementId);
    }
  }

  @ContinueSpan(log = "findAll contract elements")
  public Collection<ContractElement> findAll() {
    return repository.getAll();
  }

  @ContinueSpan(log = "replaceAll contract elements")
  public List<ContractElement> replaceAll(Map<String, ContractElement> stringContractElementMap) {
    repository.removeAll();
    return stringContractElementMap.values().stream().map(repository::save).toList();
  }

  @ContinueSpan(log = "dropCollection (all)")
  public void dropCollection() {
    repository.removeAll();
  }

  @ContinueSpan(log = "getGTResultList")
  public List<GTResult> getGTResultList(List<GTElement> elements) {
    return repository.getGTResultList(elements);
  }

  @ContinueSpan(log = "getAllGarantieTechniques")
  public List<GarantieTechnique> getAllGarantieTechniques(String search, long limit) {
    return repository.getAllGarantieTechniques(search, limit);
  }

  public void delete(String codeInsurer, String codeContractElement) {
    final Optional<ContractElement> existing =
        repository.findByKey(codeContractElement, codeInsurer);
    existing.ifPresent(contractElement -> repository.removeById(contractElement.getId()));
  }
}
