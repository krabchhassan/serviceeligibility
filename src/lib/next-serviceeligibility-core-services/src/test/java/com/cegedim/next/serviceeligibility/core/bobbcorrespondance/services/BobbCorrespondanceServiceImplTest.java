package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.Versions;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.dao.VersionsRepository;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ClosePeriodRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CloseProductElementRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.GroupBy;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.PeriodSelector;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ProductSelector;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.exceptions.NoActiveVersionFoundException;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.model.SearchCriteria;
import com.cegedim.next.serviceeligibility.core.dto.GuaranteeSearchResultDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BobbCorrespondanceServiceImplTest {

  public static final String CODE_CONTRACT_ELEMENT = "GT_5497_D2";
  public static final String CODE_INSURER = "INS123";
  @Mock private VersionsRepository versionsRepository;
  @Mock private ContractElementRepository contractElementRepository;

  @InjectMocks private BobbCorrespondanceServiceImpl bobbCorrespondanceServiceImpl;

  @Test
  void should_return_contract_element_when_exists_in_db() {
    Versions activeVersion = buildActiveVersion();
    ContractElement expectedContract = new ContractElement();
    expectedContract.setCodeContractElement(CODE_CONTRACT_ELEMENT);
    expectedContract.setCodeInsurer(CODE_INSURER);
    expectedContract.setVersionId("v1");

    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));
    when(contractElementRepository.findByKeyAndVersion(anyString(), anyString(), anyString()))
        .thenReturn(Optional.of(expectedContract));

    ContractElement result =
        bobbCorrespondanceServiceImpl.findCorrespondance(CODE_CONTRACT_ELEMENT, CODE_INSURER);

    assertNotNull(result);
    assertEquals(CODE_CONTRACT_ELEMENT, result.getCodeContractElement());
    assertEquals(CODE_INSURER, result.getCodeInsurer());
  }

  @Test
  void should_return_null_when_no_active_version() {
    when(versionsRepository.findActiveVersion()).thenReturn(null);

    ContractElement result =
        bobbCorrespondanceServiceImpl.findCorrespondance(CODE_CONTRACT_ELEMENT, CODE_INSURER);

    assertNull(result);
  }

  @Test
  void should_return_null_when_contract_element_not_found() {
    Versions activeVersion = buildActiveVersion();

    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));
    when(contractElementRepository.findByKeyAndVersion(anyString(), anyString(), anyString()))
        .thenReturn(null);

    ContractElement result =
        bobbCorrespondanceServiceImpl.findCorrespondance(CODE_CONTRACT_ELEMENT, CODE_INSURER);

    assertNull(result);
  }

  @Test
  void should_return_true_when_contract_exists() {
    Versions activeVersion = buildActiveVersion();

    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));
    when(contractElementRepository.existsByKeyAndVersion(anyString(), anyString(), anyString()))
        .thenReturn(true);

    boolean result =
        bobbCorrespondanceServiceImpl.isCorrespondenceExist(CODE_CONTRACT_ELEMENT, CODE_INSURER);

    assertTrue(result);
  }

  @Test
  void should_return_false_when_no_active_version() {
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());

    boolean result =
        bobbCorrespondanceServiceImpl.isCorrespondenceExist(CODE_CONTRACT_ELEMENT, CODE_INSURER);

    assertFalse(result);
  }

  @Test
  void should_return_false_when_contract_does_not_exist() {
    Versions activeVersion = buildActiveVersion();

    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));
    when(contractElementRepository.existsByKeyAndVersion(anyString(), anyString(), anyString()))
        .thenReturn(false);

    boolean result =
        bobbCorrespondanceServiceImpl.isCorrespondenceExist(CODE_CONTRACT_ELEMENT, CODE_INSURER);

    assertFalse(result);
  }

  @Test
  void get_products_by_gtid_group_by_product_should_return_sorted_items() {
    Versions activeVersion = buildActiveVersion();
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    var products =
        List.of(
            buildProductElement(
                "OFF2", "PROD", "NAT-B", "AMC", LocalDateTime.of(2021, 1, 10, 0, 0), null),
            buildProductElement(
                "OFF1",
                "PROD",
                "NAT-A",
                "AMC",
                LocalDateTime.of(2020, 1, 10, 0, 0),
                LocalDateTime.of(2020, 12, 31, 0, 0)),
            buildProductElement(
                "OFF1",
                "PROD",
                "NAT-A",
                "AMC",
                LocalDateTime.of(2019, 1, 10, 0, 0),
                LocalDateTime.of(2019, 12, 31, 0, 0)));
    var contract = buildOptionalContractWithProducts("GT1", "INS123", "CE001", products);
    when(contractElementRepository.findByIdAndVersion("GT1", "v1")).thenReturn(contract);

    var resp = bobbCorrespondanceServiceImpl.getProductsByGtId("GT1", GroupBy.PRODUCT, null);

    assertNotNull(resp);
    assertEquals("GT1", resp.gtId());
    assertEquals(GroupBy.PRODUCT, resp.groupBy());
    assertNotNull(resp.items());
    assertEquals(3, resp.items().size());

    // check sort results
    assertEquals(LocalDateTime.of(2019, 1, 10, 0, 0), resp.items().get(0).getFrom());
    assertEquals(LocalDateTime.of(2020, 1, 10, 0, 0), resp.items().get(1).getFrom());
    assertEquals(LocalDateTime.of(2021, 1, 10, 0, 0), resp.items().get(2).getFrom());
  }

  @Test
  void get_products_by_gtid_group_by_period_should_return_grouped_periods() {
    Versions activeVersion = buildActiveVersion();
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    var products =
        List.of(
            // 2 éléments même période => même groupe
            buildProductElement(
                "OFF1",
                "P",
                "N1",
                "A",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 0, 0, 0)),
            buildProductElement(
                "OFF2",
                "P",
                "N2",
                "A",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 0, 0, 0)),
            // période ouverte
            buildProductElement(
                "OFF3", "P", "N3", "A", LocalDateTime.of(2021, 1, 1, 0, 0, 0), null));
    var contract = buildOptionalContractWithProducts("GT2", "INS999", "CE002", products);
    when(contractElementRepository.findByIdAndVersion("GT2", "v1")).thenReturn(contract);

    var resp = bobbCorrespondanceServiceImpl.getProductsByGtId("GT2", GroupBy.PERIOD, null);

    assertNotNull(resp);
    assertEquals(GroupBy.PERIOD, resp.groupBy());
    assertNotNull(resp.periods());
    assertEquals(2, resp.periods().size());

    // premier groupe: 2020-01-01 -> 2020-12-31 avec 2 items
    var g1 = resp.periods().get(0);
    assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0), g1.from());
    assertEquals(LocalDateTime.of(2020, 12, 31, 0, 0), g1.to());
    assertEquals(2, g1.items().size());

    // second groupe: 2021-01-01 -> null avec 1 item
    var g2 = resp.periods().get(1);
    assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), g2.from());
    assertNull(g2.to());
    assertEquals(1, g2.items().size());
  }

  @Test
  void get_products_by_gtid_with_applied_date_filters_elements_inclusively() {
    Versions activeVersion = buildActiveVersion();
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    var products =
        List.of(
            buildProductElement(
                "OFF",
                "P",
                "N1",
                "A",
                LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 0, 0, 0)),
            buildProductElement(
                "OFF",
                "P",
                "N2",
                "A",
                LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                null) // exclu pour 2020-06-01
            );
    var contract = buildOptionalContractWithProducts("GT3", "INS1", "CE003", products);
    when(contractElementRepository.findByIdAndVersion("GT3", "v1")).thenReturn(contract);

    var resp =
        bobbCorrespondanceServiceImpl.getProductsByGtId(
            "GT3", GroupBy.PRODUCT, LocalDate.of(2020, 6, 1));

    assertNotNull(resp);
    assertEquals(1, resp.items().size());
    assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0), resp.items().get(0).getFrom());
    assertEquals(LocalDateTime.of(2020, 12, 31, 0, 0), resp.items().get(0).getTo());
  }

  @Test
  void get_products_by_codes_should_delegate_to_gtid_path() {
    Versions activeVersion = buildActiveVersion();
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    var found =
        buildOptionalContractWithProducts(
            "GTX",
            "INS-A",
            "CE-ALPHA",
            List.of(
                buildProductElement(
                    "O", "P", "N", "AMC", LocalDateTime.of(2022, 1, 1, 0, 0), null)));
    when(contractElementRepository.findByKeyAndVersion("CE-ALPHA", "INS-A", "v1"))
        .thenReturn(found);

    when(contractElementRepository.findByIdAndVersion("GTX", "v1")).thenReturn(found);

    var resp =
        bobbCorrespondanceServiceImpl.getProductsByCodes(
            "CE-ALPHA", "INS-A", GroupBy.PRODUCT, null);

    assertNotNull(resp);
    assertEquals("GTX", resp.gtId());
    assertEquals("INS-A", resp.codeInsurer());
    assertEquals("CE-ALPHA", resp.codeContractElement());
    assertNotNull(resp.items());
    assertEquals(1, resp.items().size());
  }

  private Versions buildActiveVersion() {
    Versions versions = new Versions();
    versions.setId("v1");
    versions.setStatus("ACTIVE");
    versions.setNumber(1);
    versions.setLabel("Version 1");
    versions.setCreatedBy("tester");
    versions.setCreationDate(LocalDateTime.now());
    return versions;
  }

  private Optional<ContractElement> buildOptionalContractWithProducts(
      String id, String insurer, String codeCE, List<ProductElement> products) {
    ContractElement contractElement = new ContractElement();
    contractElement.setId(id);
    contractElement.setCodeInsurer(insurer);
    contractElement.setCodeContractElement(codeCE);
    contractElement.setProductElements(products);
    contractElement.setVersionId("v1");
    return Optional.of(contractElement);
  }

  private ProductElement buildProductElement(
      String offer,
      String product,
      String nature,
      String amc,
      LocalDateTime from,
      LocalDateTime to) {
    ProductElement dto = new ProductElement();
    dto.setCodeOffer(offer);
    dto.setCodeProduct(product);
    dto.setCodeBenefitNature(nature);
    dto.setCodeAmc(amc);
    dto.setFrom(from);
    dto.setEffectiveDate(from);
    dto.setTo(to);
    return dto;
  }

  @Test
  void test_returns_paginated_results_when_active_version_exists_and_criteria_matches() {
    SearchCriteria criteria = new SearchCriteria();
    criteria.setCodeInsurer("INS001");
    Pageable pageable = PageRequest.of(0, 10);

    Versions activeVersion = new Versions();
    activeVersion.setId("version123");
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    ContractElement element = new ContractElement();
    element.setId("element1");
    element.setCodeContractElement("CONTRACT001");
    element.setCodeInsurer("INS001");
    element.setIgnored(false);
    List<ContractElement> elements = List.of(element);

    when(contractElementRepository.count(any(Query.class))).thenReturn(1L);
    when(contractElementRepository.find(any(Query.class), any(Pageable.class)))
        .thenReturn(elements);
    Page<GuaranteeSearchResultDto> result =
        bobbCorrespondanceServiceImpl.search(criteria, pageable);
    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isEqualTo(1L);
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().getFirst().id()).isEqualTo("element1");
    assertThat(result.getContent().getFirst().codeContractElement()).isEqualTo("CONTRACT001");
    assertThat(result.getContent().getFirst().codeInsurer()).isEqualTo("INS001");
    assertThat(result.getContent().getFirst().ignored()).isFalse();
  }

  @Test
  void test_returns_empty_page_when_no_active_version_found() {
    SearchCriteria criteria = new SearchCriteria();
    criteria.setCodeInsurer("INS001");
    Pageable pageable = PageRequest.of(0, 20);
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());
    Page<GuaranteeSearchResultDto> result =
        bobbCorrespondanceServiceImpl.search(criteria, pageable);
    assertThat(result).isNotNull();
    assertThat(result.isEmpty()).isTrue();
    assertThat(result.getTotalElements()).isZero();
    assertThat(result.getContent()).isEmpty();
    assertThat(result.getPageable().getPageNumber()).isZero();
    assertThat(result.getPageable().getPageSize()).isEqualTo(20);
    verify(contractElementRepository, never()).count(any(Query.class));
    verify(contractElementRepository, never()).find(any(Query.class), any(Pageable.class));
  }

  @Test
  void close_one_product_element_by_gt_should_return_updated_count_when_active_version_exists() {
    Versions activeVersion = buildActiveVersion();
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    CloseProductElementRequest req =
        new CloseProductElementRequest(
            LocalDate.of(2019, 9, 30),
            new ProductSelector(
                "GFM-A", "TONUS-A", "SOINSCOURANTS", "ASSU00098", LocalDate.of(2019, 6, 1)));

    when(contractElementRepository.closeOneProductElementByGt("GTX", "v1", req)).thenReturn(1);

    int updated = bobbCorrespondanceServiceImpl.closeOneProductElementByGt("GTX", req);

    assertEquals(1, updated);
    verify(versionsRepository, times(1)).findActiveVersion();
    verify(contractElementRepository, times(1)).closeOneProductElementByGt("GTX", "v1", req);
  }

  @Test
  void
      close_one_product_element_by_gt_should_throw_no_active_version_found_exception_when_no_active_version() {
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());

    CloseProductElementRequest req =
        new CloseProductElementRequest(
            LocalDate.of(2020, 12, 31),
            new ProductSelector("OFF", "PROD", "NATURE", "AMC", LocalDate.of(2020, 1, 1)));

    assertThrows(
        NoActiveVersionFoundException.class,
        () -> bobbCorrespondanceServiceImpl.closeOneProductElementByGt("GT_NO_VERSION", req));

    verify(versionsRepository, times(1)).findActiveVersion();
    verify(contractElementRepository, times(0))
        .closeOneProductElementByGt(anyString(), anyString(), any());
  }

  @Test
  void close_period_by_gt_should_throw_no_active_version_found_exception_when_no_active_version() {
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());

    ClosePeriodRequest req =
        new ClosePeriodRequest(
            LocalDate.of(2018, 12, 31), new PeriodSelector(LocalDate.of(2016, 1, 1), null));

    assertThrows(
        NoActiveVersionFoundException.class,
        () -> bobbCorrespondanceServiceImpl.closePeriodByGt("GT_NO_VERSION", req));

    verify(versionsRepository, times(1)).findActiveVersion();
    verify(contractElementRepository, times(0)).closePeriodByGt(anyString(), anyString(), any());
  }

  @Test
  void test_returns_guarantee_codes_when_active_version_exists() {

    Versions activeVersion = new Versions();
    activeVersion.setId("version123");
    Optional<Versions> activeVersionOptional = Optional.of(activeVersion);

    List<String> expectedGuaranteeCodes = Arrays.asList("GC001", "GC002", "GC003");

    when(versionsRepository.findActiveVersion()).thenReturn(activeVersionOptional);
    when(contractElementRepository.distinctGuaranteeCodesByOfferAndAmc(
            "version123", "OFFER001", "AMC001"))
        .thenReturn(expectedGuaranteeCodes);

    List<String> result = bobbCorrespondanceServiceImpl.findGuaranteeCodes("OFFER001", "AMC001");
    assertThat(result).isEqualTo(expectedGuaranteeCodes);
    verify(versionsRepository).findActiveVersion();
    verify(contractElementRepository)
        .distinctGuaranteeCodesByOfferAndAmc("version123", "OFFER001", "AMC001");
  }

  @Test
  void test_returns_empty_list_when_no_active_version_found_get_guarantee_codes() {
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());
    List<String> result = bobbCorrespondanceServiceImpl.findGuaranteeCodes("OFFER001", "AMC001");
    assertThat(result).isEmpty();
    verify(versionsRepository).findActiveVersion();
    verifyNoInteractions(contractElementRepository);
  }

  @Test
  void test_returns_offer_codes_when_active_version_exists() {
    Versions activeVersion = new Versions();
    activeVersion.setId("version-123");
    List<String> expectedOfferCodes = Arrays.asList("OFFER001", "OFFER002", "OFFER003");
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));
    when(contractElementRepository.distinctOfferCodesByGuaranteeAndAmc(
            "version-123", "CONTRACT001", "AMC001"))
        .thenReturn(expectedOfferCodes);
    List<String> result = bobbCorrespondanceServiceImpl.findOfferCodes("CONTRACT001", "AMC001");
    assertThat(result).isEqualTo(expectedOfferCodes);
    verify(versionsRepository).findActiveVersion();
    verify(contractElementRepository)
        .distinctOfferCodesByGuaranteeAndAmc("version-123", "CONTRACT001", "AMC001");
  }

  @Test
  void test_returns_empty_list_when_no_active_version_found_get_offer_codes() {
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());
    List<String> result = bobbCorrespondanceServiceImpl.findOfferCodes("CONTRACT001", "AMC001");
    assertThat(result).isEmpty();
    verify(versionsRepository).findActiveVersion();
    verifyNoInteractions(contractElementRepository);
  }

  @Test
  void test_returns_amc_codes_when_active_version_exists() {
    Versions activeVersion = new Versions();
    activeVersion.setId("version123");
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.of(activeVersion));

    List<String> expectedAmcCodes = Arrays.asList("AMC001", "AMC002", "AMC003");
    when(contractElementRepository.distinctAmcByOfferAndGuarantee(
            "version123", "OFFER001", "CONTRACT001"))
        .thenReturn(expectedAmcCodes);
    List<String> result = bobbCorrespondanceServiceImpl.findAmcCodes("OFFER001", "CONTRACT001");
    assertEquals(expectedAmcCodes, result);
    verify(versionsRepository).findActiveVersion();
    verify(contractElementRepository)
        .distinctAmcByOfferAndGuarantee("version123", "OFFER001", "CONTRACT001");
  }

  @Test
  void test_returns_empty_list_when_no_active_version_found_get_amc_codes() {
    when(versionsRepository.findActiveVersion()).thenReturn(Optional.empty());

    List<String> result = bobbCorrespondanceServiceImpl.findAmcCodes("OFFER001", "CONTRACT001");

    assertEquals(Collections.emptyList(), result);
    verify(versionsRepository).findActiveVersion();
    verifyNoInteractions(contractElementRepository);
  }

  @Test
  void check_Param_ok_when_all_params_allowed() {
    Map<String, String> allParams =
        Map.of(
            "codeInsurer", "AXA",
            "codeOffer", "OFF1");
    Set<String> allowed = Set.of("codeInsurer", "codeOffer");
    assertDoesNotThrow(() -> bobbCorrespondanceServiceImpl.checkParams(allParams, allowed));
  }

  @Test
  void check_param_throws_when_unknown_param_present() {
    Map<String, String> allParams =
        Map.of(
            "codeInsurer", "AXA",
            "foo", "bar");
    Set<String> allowed = Set.of("codeInsurer");
    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> bobbCorrespondanceServiceImpl.checkParams(allParams, allowed));
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertEquals("Invalid query param : [foo]", ex.getReason());
  }

  @Test
  void check_param_ok_when_no_params() {
    Map<String, String> allParams = Map.of();
    Set<String> allowed = Set.of("codeInsurer", "codeOffer");
    assertDoesNotThrow(() -> bobbCorrespondanceServiceImpl.checkParams(allParams, allowed));
  }
}
