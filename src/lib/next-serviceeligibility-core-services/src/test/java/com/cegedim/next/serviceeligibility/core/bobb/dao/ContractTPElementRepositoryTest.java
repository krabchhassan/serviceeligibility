package com.cegedim.next.serviceeligibility.core.bobb.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTResult;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractTPElementRepositoryTest extends ContractElementRepository {

  private final Logger logger = LoggerFactory.getLogger(ContractTPElementRepositoryTest.class);

  @Autowired ContractElementRepository repository;

  @Autowired MongoTemplate template;

  List<String> ids = new ArrayList<>();

  @BeforeEach
  public void load_contract_element() {
    repository.removeAll();

    logger.info("Chargement de 2 ContractElement");

    ContractElement saved = repository.save(getElementOne());
    ids.add(saved.getId());

    saved = repository.save(getElementTwo());
    ids.add(saved.getId());

    Mockito.when(template.findAll(any())).thenReturn(List.of(getElementOne(), getElementTwo()));
  }

  @Test
  void repo() {
    logger.info("Recuperation de ContractElement - getAll");
    Collection<ContractElement> contractElements = repository.getAll();

    Assertions.assertNotNull(contractElements);
    Assertions.assertEquals(2, contractElements.size());

    logger.info("Recuperation d un ContractElement - findByKey");
    Mockito.when(template.findOne(any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(getElementOne());
    Optional<ContractElement> optionalContractFindByKey =
        repository.findByKey("CODE_CONTRACT_ELEMENT_ONE", "CODE_INSURER_ONE");
    Assertions.assertNotNull(optionalContractFindByKey);
    Assertions.assertTrue(optionalContractFindByKey.isPresent());
    ContractElement contractFindByKey = optionalContractFindByKey.get();
    Assertions.assertEquals(
        "CODE_CONTRACT_ELEMENT_ONE", contractFindByKey.getCodeContractElement());
    Assertions.assertEquals("CODE_INSURER_ONE", contractFindByKey.getCodeInsurer());
    Assertions.assertEquals("LABEL_ONE", contractFindByKey.getLabel());
    Assertions.assertEquals("AMC", contractFindByKey.getCodeAMC());
    List<ProductElement> productElements = contractFindByKey.getProductElements();
    Assertions.assertNotNull(productElements);
    Assertions.assertEquals("AMC_ONE", productElements.get(0).getCodeAmc());
    Assertions.assertEquals("CODE_BENEFIT_NATURE", productElements.get(0).getCodeBenefitNature());
    Assertions.assertEquals("CODE_OFFER", productElements.get(0).getCodeOffer());
    Assertions.assertEquals("CODE_PRODUCT", productElements.get(0).getCodeProduct());
    // get by CodeContractElement and CodeInsurer
    Mockito.when(template.find(any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(List.of(getElementTwo()));
    List<ContractElement> optionalContractGet =
        repository.get("CODE_CONTRACT_ELEMENT_TWO", "CODE_INSURER_TWO", true);
    Assertions.assertNotNull(optionalContractGet);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(optionalContractGet));
    ContractElement contractGet = optionalContractGet.get(0);
    Assertions.assertEquals("CODE_CONTRACT_ELEMENT_TWO", contractGet.getCodeContractElement());
    Assertions.assertEquals("CODE_INSURER_TWO", contractGet.getCodeInsurer());
    Assertions.assertEquals("LABEL_TWO", contractGet.getLabel());
    Assertions.assertEquals("AMC", contractGet.getCodeAMC());
    productElements = contractGet.getProductElements();
    Assertions.assertNotNull(productElements);
    Assertions.assertEquals("AMC_ONE", productElements.get(0).getCodeAmc());
    Assertions.assertEquals("CODE_BENEFIT_NATURE", productElements.get(0).getCodeBenefitNature());
    Assertions.assertEquals("CODE_OFFER", productElements.get(0).getCodeOffer());
    Assertions.assertEquals("CODE_PRODUCT", productElements.get(0).getCodeProduct());
    Assertions.assertEquals("AMC_TWO", productElements.get(1).getCodeAmc());
    Assertions.assertEquals(
        "CODE_BENEFIT_NATURE_TWO", productElements.get(1).getCodeBenefitNature());
    Assertions.assertEquals("CODE_OFFER_TWO", productElements.get(1).getCodeOffer());
    Assertions.assertEquals("CODE_PRODUCT_TWO", productElements.get(1).getCodeProduct());

    Mockito.when(template.find(any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(List.of(getElementOne()));
    Optional<ContractElement> optionalContractFindOneById = repository.findOneById(ids.get(0));
    Assertions.assertNotNull(optionalContractFindOneById);
    Assertions.assertTrue(optionalContractFindOneById.isPresent());
    ContractElement contractFindOneById = optionalContractFindOneById.get();
    Assertions.assertEquals(
        "CODE_CONTRACT_ELEMENT_ONE", contractFindOneById.getCodeContractElement());
    Assertions.assertEquals("CODE_INSURER_ONE", contractFindOneById.getCodeInsurer());
    Assertions.assertEquals("LABEL_ONE", contractFindOneById.getLabel());
    Assertions.assertEquals("AMC", contractFindOneById.getCodeAMC());
    productElements = contractFindOneById.getProductElements();
    Assertions.assertNotNull(productElements);
    Assertions.assertEquals("AMC_ONE", productElements.get(0).getCodeAmc());
    Assertions.assertEquals("CODE_BENEFIT_NATURE", productElements.get(0).getCodeBenefitNature());
    Assertions.assertEquals("CODE_OFFER", productElements.get(0).getCodeOffer());
    Assertions.assertEquals("CODE_PRODUCT", productElements.get(0).getCodeProduct());

    // Collection<ContractElement> get(final String codeAMC)
    Mockito.when(template.find(any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(List.of(getElementOne(), getElementTwo()));
    contractElements = repository.get("AMC");
    Assertions.assertEquals(2, contractElements.size());

    Mockito.when(template.find(any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(new ArrayList<>());
    contractElements = repository.get("AMC_INCONNUE");
    Assertions.assertEquals(0, contractElements.size());

    Mockito.when(template.find(any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(List.of(getElementOne(), getElementTwo()));
    contractElements = repository.get("");
    Assertions.assertEquals(2, contractElements.size());

    // ProductElementLight
    ProductElementLight product = new ProductElementLight();
    product.setCodeOffer("OFFER1");
    product.setCodeProduct("PRODUCT1");
    AggregationResults<ProductElementLight> res =
        new AggregationResults<>(List.of(product), new Document());
    Mockito.when(
            template.aggregate(
                any(Aggregation.class), Mockito.anyString(), Mockito.eq(ProductElementLight.class)))
        .thenReturn(res);
    List<ProductElementLight> productsElementLight =
        repository.getProductsElementLight("CODE_CONTRACT_ELEMENT_ONE", "CODE_INSURER_ONE", null);
    Assertions.assertEquals(1, productsElementLight.size());

    // GTResultList
    List<GTElement> elements = new ArrayList<>();
    List<GTResult> gtResultList = repository.getGTResultList(elements);
    Assertions.assertNotNull(gtResultList);
    Assertions.assertTrue(gtResultList.isEmpty());

    // remove
    Mockito.when(template.findAll(ContractElement.class))
        .thenReturn(List.of(getElementOne(), getElementTwo()));
    contractElements = repository.getAll();
    Assertions.assertEquals(2, contractElements.size());

    // removeById
    repository.removeById(ids.get(0));
    Mockito.when(template.findAll(ContractElement.class)).thenReturn(List.of(getElementTwo()));
    contractElements = repository.getAll();
    Assertions.assertEquals(1, contractElements.size());

    repository.removeAll();
    Mockito.when(template.findAll(ContractElement.class)).thenReturn(new ArrayList<>());
    contractElements = repository.getAll();
    Assertions.assertEquals(0, contractElements.size());
  }

  private ContractElement getElementOne() {
    ContractElement contractOne = new ContractElement();
    contractOne.setCodeContractElement("CODE_CONTRACT_ELEMENT_ONE");
    contractOne.setCodeInsurer("CODE_INSURER_ONE");
    contractOne.setLabel("LABEL_ONE");
    contractOne.setCodeAMC("AMC");
    List<ProductElement> productElements = new ArrayList<>();
    ProductElement element = new ProductElement();
    element.setCodeAmc("AMC_ONE");
    element.setCodeBenefitNature("CODE_BENEFIT_NATURE");
    element.setCodeOffer("CODE_OFFER");
    element.setCodeProduct("CODE_PRODUCT");
    element.setFrom(LocalDateTime.now(ZoneOffset.UTC).minusYears(1L));
    productElements.add(element);
    contractOne.setProductElements(productElements);

    return contractOne;
  }

  private ContractElement getElementTwo() {
    ContractElement contractTwo = new ContractElement();
    contractTwo.setCodeContractElement("CODE_CONTRACT_ELEMENT_TWO");
    contractTwo.setCodeInsurer("CODE_INSURER_TWO");
    contractTwo.setLabel("LABEL_TWO");
    contractTwo.setCodeAMC("AMC");
    List<ProductElement> productElements = new ArrayList<>();
    ProductElement element = new ProductElement();
    element.setCodeAmc("AMC_ONE");
    element.setCodeBenefitNature("CODE_BENEFIT_NATURE");
    element.setCodeOffer("CODE_OFFER");
    element.setCodeProduct("CODE_PRODUCT");
    element.setFrom(LocalDateTime.now(ZoneOffset.UTC).minusYears(1L));
    productElements.add(element);
    element = new ProductElement();
    element.setCodeAmc("AMC_TWO");
    element.setCodeBenefitNature("CODE_BENEFIT_NATURE_TWO");
    element.setCodeOffer("CODE_OFFER_TWO");
    element.setCodeProduct("CODE_PRODUCT_TWO");
    element.setFrom(LocalDateTime.now(ZoneOffset.UTC).minusYears(1L));
    productElements.add(element);
    contractTwo.setProductElements(productElements);

    return contractTwo;
  }

  private final MatchOperation matchStage1 =
      Aggregation.match(
          new Criteria(CODE_CONTRACT_ELEMENT)
              .is("A")
              .and(CODE_INSURER)
              .is("B")
              .and(Constants.IGNORED)
              .is(false));

  @Test
  void shouldReturnAggregation() {
    LocalDate localDate = LocalDate.of(2020, 1, 21);
    Aggregation aggregation = ContractElementRepository.getAggregation("A", "B", localDate, null);
    MatchOperation matchStage2 =
        Aggregation.match(new Criteria(PRODUCT_ELEMENTS + "." + FROM).lte(localDate.plusDays(1)));
    MatchOperation matchStage3 =
        Aggregation.match(new Criteria(PRODUCT_ELEMENTS + "." + TO).exists(false));

    Assertions.assertEquals(
        Aggregation.newAggregation(
                matchStage1, unwindStage, matchStage2, matchStage3, groupStage, projectStage2)
            .toString(),
        aggregation.toString());
  }

  @Test
  void shouldReturnAggregationWithEndDate() {
    LocalDate localDate = LocalDate.of(2020, 1, 21);
    LocalDate localDate2 = LocalDate.of(2020, 1, 22);
    Aggregation aggregation =
        ContractElementRepository.getAggregation("A", "B", localDate, localDate2);
    MatchOperation matchStage2 =
        Aggregation.match(Criteria.where(PRODUCT_ELEMENTS + "." + FROM).lte(localDate.plusDays(1)));
    MatchOperation matchStage3 =
        Aggregation.match(
            new Criteria()
                .orOperator(
                    new Criteria(PRODUCT_ELEMENTS + "." + TO).exists(false),
                    new Criteria(PRODUCT_ELEMENTS + "." + TO).gte(localDate2.minusDays(1))));

    Assertions.assertEquals(
        Aggregation.newAggregation(
                matchStage1, unwindStage, matchStage2, matchStage3, groupStage, projectStage2)
            .toString(),
        aggregation.toString());
  }

  @Test
  void test_returns_distinct_guarantee_codes_when_all_parameters_provided_and_valid() {
    ContractElementRepository repository = Mockito.spy(new ContractElementRepository());
    List<String> expectedCodes = Arrays.asList("CODE1", "CODE2", "CODE3");

    doReturn(expectedCodes).when(repository).distinctByIdAsc(any(List.class));

    List<String> result =
        repository.distinctGuaranteeCodesByOfferAndAmc("version123", "OFFER001", "AMC001");

    assertThat(result).isEqualTo(expectedCodes);
    assertThat(result).hasSize(3);
    verify(repository).distinctByIdAsc(any(List.class));
  }

  @Test
  void test_handles_null_active_version_id_parameter() {
    ContractElementRepository repository = Mockito.spy(new ContractElementRepository());
    List<String> expectedCodes = Arrays.asList("CODE1", "CODE2");

    doReturn(expectedCodes).when(repository).distinctByIdAsc(any(List.class));

    List<String> result =
        repository.distinctGuaranteeCodesByOfferAndAmc(null, "OFFER001", "AMC001");

    assertThat(result).isEqualTo(expectedCodes);
    verify(repository).distinctByIdAsc(any(List.class));
  }

  @Test
  void test_returns_distinct_offer_codes_when_all_parameters_provided_and_valid() {
    ContractElementRepository repository = Mockito.spy(new ContractElementRepository());
    List<String> expectedOfferCodes = Arrays.asList("OFFER001", "OFFER002");

    doReturn(expectedOfferCodes).when(repository).distinctByIdAsc(any(List.class));

    List<String> result =
        repository.distinctOfferCodesByGuaranteeAndAmc("v1.0", "CONTRACT001", "AMC001");

    Assertions.assertEquals(expectedOfferCodes, result);
    verify(repository).distinctByIdAsc(any(List.class));
  }

  @Test
  void test_returns_results_when_code_contract_element_is_null_or_empty() {
    ContractElementRepository repository = Mockito.spy(new ContractElementRepository());
    List<String> expectedOfferCodes = Arrays.asList("OFFER003", "OFFER004");

    doReturn(expectedOfferCodes).when(repository).distinctByIdAsc(any(List.class));

    List<String> resultWithNull =
        repository.distinctOfferCodesByGuaranteeAndAmc("v1.0", null, "AMC001");
    List<String> resultWithEmpty =
        repository.distinctOfferCodesByGuaranteeAndAmc("v1.0", "", "AMC001");

    Assertions.assertEquals(expectedOfferCodes, resultWithNull);
    Assertions.assertEquals(expectedOfferCodes, resultWithEmpty);
    verify(repository, times(2)).distinctByIdAsc(any(List.class));
  }

  @Test
  void test_returns_distinct_amc_codes_when_all_parameters_provided() {
    ContractElementRepository repository = Mockito.spy(new ContractElementRepository());
    List<String> expectedAmcCodes = Arrays.asList("AMC001", "AMC002", "AMC003");

    Mockito.doReturn(expectedAmcCodes).when(repository).distinctByIdAsc(Mockito.any());

    List<String> result =
        repository.distinctAmcByOfferAndGuarantee("v1.0", "OFFER123", "CONTRACT456");

    Assertions.assertEquals(expectedAmcCodes, result);
    Mockito.verify(repository).distinctByIdAsc(Mockito.any());
  }

  @Test
  void test_handles_null_code_offer_parameter() {
    ContractElementRepository repository = Mockito.spy(new ContractElementRepository());
    List<String> expectedAmcCodes = Arrays.asList("AMC001", "AMC002");

    Mockito.doReturn(expectedAmcCodes).when(repository).distinctByIdAsc(Mockito.any());

    List<String> result = repository.distinctAmcByOfferAndGuarantee("v1.0", null, "CONTRACT456");

    Assertions.assertEquals(expectedAmcCodes, result);
    Mockito.verify(repository).distinctByIdAsc(Mockito.any());
  }
}
