package com.cegedim.next.serviceeligibility.core.bobb.dao;

import static com.cegedim.next.serviceeligibility.core.bobbcorrespondance.constants.CommonConstants.*;
import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.FORMATTERSLASHED;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementDerived;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTElement;
import com.cegedim.next.serviceeligibility.core.bobb.gt.GTResult;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ClosePeriodRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CloseProductElementRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.utils.BobbMongoHelpers;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ContractElementRepository extends AbstractRepository<ContractElement> {

  protected static final String ID = "_id";
  protected static final String CODE_PRODUCT = "codeProduct";
  protected static final String CODE_OFFER = "codeOffer";

  protected static final String CODE_AMC = "codeAmc";
  protected static final String TO = "to";
  protected static final String FROM = "from";
  protected static final String PRODUCT_ELEMENTS = "productElements";
  protected static final String CODE_INSURER = "codeInsurer";
  protected static final String CODE_CONTRACT_ELEMENT = "codeContractElement";
  protected static final String OC = "codeAmc";
  protected static final String DOT = ".";

  protected static final UnwindOperation unwindStage = Aggregation.unwind("$" + PRODUCT_ELEMENTS);

  protected static final GroupOperation groupStage =
      Aggregation.group(
          PRODUCT_ELEMENTS + "." + CODE_OFFER,
          PRODUCT_ELEMENTS + "." + CODE_PRODUCT,
          PRODUCT_ELEMENTS + "." + CODE_AMC);
  protected static final ProjectionOperation projectStage2 =
      Aggregation.project(ID + "." + CODE_OFFER, ID + "." + CODE_PRODUCT, ID + "." + CODE_AMC)
          .andExclude(ID);

  public ContractElementRepository() {
    super(ContractElement.class);
  }

  /** Delete all data from collection. */
  @ContinueSpan(log = "removeAll ContractElement")
  public void removeAll() {
    final Query query = new Query();
    remove(query);
  }

  /**
   * Get a contract element by its functional key.
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @return the contract element, if any
   */
  @ContinueSpan(log = "findByKey ContractElement")
  public Optional<ContractElement> findByKey(
      final String codeContractElement, final String codeInsurer) {
    final Query query = new Query();
    query.addCriteria(
        Criteria.where(CODE_INSURER)
            .is(codeInsurer)
            .and(CODE_CONTRACT_ELEMENT)
            .is(codeContractElement));
    return findOne(query);
  }

  /**
   * Get contract elements by AMC
   *
   * @param codeAMC optional filter, get all if null
   * @return contract elements
   */
  @ContinueSpan(log = "get ContractElement by amc")
  public Collection<ContractElement> get(final String codeAMC) {
    final Query query = new Query();
    if (!StringUtils.isEmpty(codeAMC)) {
      query.addCriteria(Criteria.where("codeAMC").is(codeAMC));
    }
    return find(query);
  }

  /**
   * Get one contract elements by code
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @return the contract element, if any
   */
  @ContinueSpan(log = "get ContractElement by codeContractElement and codeInsurer")
  public List<ContractElement> get(
      @NotNull final String codeContractElement,
      @NotNull final String codeInsurer,
      boolean includeIgnored) {
    final Query query = new Query();
    Criteria criteria =
        Criteria.where(CODE_CONTRACT_ELEMENT)
            .is(codeContractElement)
            .and(CODE_INSURER)
            .is(codeInsurer);
    if (!includeIgnored) {
      criteria = criteria.and(Constants.IGNORED).is(false);
    }
    query.addCriteria(criteria);
    return new ArrayList<>(find(query));
  }

  /**
   * Get one contract elements by code
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @return the contract element, if any
   */
  @ContinueSpan(log = "getOptional ContractElement by codeContractElement and codeInsurer")
  public Optional<ContractElement> getOptional(
      @NotNull final String codeContractElement,
      @NotNull final String codeInsurer,
      boolean includeIgnored) {
    final Query query = new Query();
    Criteria criteria =
        Criteria.where(CODE_CONTRACT_ELEMENT)
            .is(codeContractElement)
            .and(CODE_INSURER)
            .is(codeInsurer);
    if (!includeIgnored) {
      criteria = criteria.and(Constants.IGNORED).is(false);
    }

    query.addCriteria(criteria);
    return findOne(query);
  }

  @ContinueSpan(log = "getAll ContractElement")
  public Collection<ContractElement> getAll() {
    return find();
  }

  /**
   * Get an Offer and product by its functional key.
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @param dateReference validity date
   * @return the contract element, if any
   */
  @ContinueSpan(log = "getProductsElementLight")
  public List<ProductElementLight> getProductsElementLight(
      final String codeContractElement, final String codeInsurer, String dateReference) {
    LocalDate date = LocalDate.now();
    if (StringUtils.isNotBlank(dateReference)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      date = LocalDate.parse(dateReference, formatter);
    }

    Aggregation agg = getAggregation(codeContractElement, codeInsurer, date, date);
    AggregationResults<ProductElementLight> res =
        template.aggregate(agg, Constants.CONTRACT_ELEMENT_COLLECTION, ProductElementLight.class);

    return res.getMappedResults();
  }

  /**
   * Get an Offer and product by its functional key.
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @param dateDebutReference start validity date
   * @param dateFinReference end validity date
   * @return the contract element, if any
   */
  @ContinueSpan(log = "getProductsElementLight")
  public List<ProductElementLight> getProductsElementLight(
      final String codeContractElement,
      final String codeInsurer,
      String dateDebutReference,
      String dateFinReference) {
    if (StringUtils.isBlank(dateDebutReference)) {
      return Collections.emptyList();
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dateDebut = LocalDate.parse(dateDebutReference, formatter);

    LocalDate dateFin = null;
    if (StringUtils.isNotBlank(dateFinReference)) {
      dateFin = LocalDate.parse(dateFinReference, formatter);
    }

    Aggregation agg = getAggregation(codeContractElement, codeInsurer, dateDebut, dateFin);
    AggregationResults<ProductElementLight> res =
        template.aggregate(agg, Constants.CONTRACT_ELEMENT_COLLECTION, ProductElementLight.class);

    return res.getMappedResults();
  }

  public static Aggregation getAggregation(
      String codeContractElement,
      String codeInsurer,
      LocalDate dateDebutReference,
      LocalDate dateFinReference) {

    MatchOperation matchStage1 =
        Aggregation.match(
            new Criteria(CODE_CONTRACT_ELEMENT)
                .is(codeContractElement)
                .and(CODE_INSURER)
                .is(codeInsurer)
                .and(Constants.IGNORED)
                .is(false));
    MatchOperation matchStage2 =
        Aggregation.match(
            Criteria.where(PRODUCT_ELEMENTS + "." + FROM).lte(dateDebutReference.plusDays(1)));
    MatchOperation matchStage3;
    if (dateFinReference != null) {
      matchStage3 =
          Aggregation.match(
              new Criteria()
                  .orOperator(
                      new Criteria(PRODUCT_ELEMENTS + "." + TO).exists(false),
                      new Criteria(PRODUCT_ELEMENTS + "." + TO)
                          .gte(dateFinReference.minusDays(1))));
    } else {
      matchStage3 = Aggregation.match(Criteria.where(PRODUCT_ELEMENTS + "." + TO).exists(false));
    }
    return Aggregation.newAggregation(
        matchStage1, unwindStage, matchStage2, matchStage3, groupStage, projectStage2);
  }

  /**
   * Get an Offer and product by its functional key.
   *
   * @param codeContractElement code contract element
   * @param codeInsurer code insurer
   * @param dateReference validity date
   * @return the contract element, if any
   */
  @ContinueSpan(log = "getProductsElementLightForOc")
  public List<ProductElementLight> getProductsElementLightForOc(
      final String codeContractElement, final String codeInsurer, String dateReference, String oc) {
    LocalDate date = LocalDate.now();
    if (StringUtils.isNotBlank(dateReference)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTERSLASHED);
      date = LocalDate.parse(dateReference, formatter);
    }

    MatchOperation matchStage1 =
        Aggregation.match(
            new Criteria(CODE_CONTRACT_ELEMENT)
                .is(codeContractElement)
                .and(CODE_INSURER)
                .is(codeInsurer));
    MatchOperation matchStageOc =
        Aggregation.match(Criteria.where(PRODUCT_ELEMENTS + "." + OC).is(oc));
    MatchOperation matchStage2 =
        Aggregation.match(Criteria.where(PRODUCT_ELEMENTS + "." + FROM).lte(date));
    MatchOperation matchStage3 =
        Aggregation.match(
            new Criteria()
                .orOperator(
                    new Criteria(PRODUCT_ELEMENTS + "." + TO).exists(false),
                    new Criteria(PRODUCT_ELEMENTS + "." + TO).gte(date)));
    GroupOperation groupStageOfferProduct =
        Aggregation.group(
            PRODUCT_ELEMENTS + "." + CODE_OFFER, PRODUCT_ELEMENTS + "." + CODE_PRODUCT);
    ProjectionOperation projectStageOfferProduct =
        Aggregation.project(ID + "." + CODE_OFFER, ID + "." + CODE_PRODUCT).andExclude(ID);
    Aggregation agg =
        Aggregation.newAggregation(
            matchStage1,
            unwindStage,
            matchStageOc,
            matchStage2,
            matchStage3,
            groupStageOfferProduct,
            projectStageOfferProduct);
    AggregationResults<ProductElementLight> res =
        template.aggregate(agg, Constants.CONTRACT_ELEMENT_COLLECTION, ProductElementLight.class);

    return res.getMappedResults();
  }

  @ContinueSpan(log = "getAllGarantieTechniques")
  public List<GarantieTechnique> getAllGarantieTechniques(String search, long limit) {
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(Aggregation.sort(Sort.Direction.ASC, CODE_CONTRACT_ELEMENT));

    if (StringUtils.isNotBlank(search)) {
      operations.add(Aggregation.match(Criteria.where(CODE_CONTRACT_ELEMENT).regex(search, "i")));
    }

    operations.add(Aggregation.limit(limit));
    ProjectionOperation projectionOperation =
        new ProjectionOperation()
            .and(CODE_INSURER)
            .as("codeAssureur")
            .and(CODE_CONTRACT_ELEMENT)
            .as("codeGarantie");
    operations.add(projectionOperation);

    return template
        .aggregate(
            Aggregation.newAggregation(operations),
            Constants.CONTRACT_ELEMENT_COLLECTION,
            GarantieTechnique.class)
        .getMappedResults();
  }

  @ContinueSpan(log = "getGTResultList")
  public List<GTResult> getGTResultList(List<GTElement> elements) {
    return elements.stream()
        .map(
            element -> {
              Optional<ContractElement> contractElementOptional =
                  getOptional(element.getCodeContractElement(), element.getCodeInsurer(), true);

              GTResult gtResult = new GTResult();
              gtResult.setCodeInsurer(element.getCodeInsurer());
              gtResult.setCodeContractElement(element.getCodeContractElement());

              contractElementOptional.ifPresent(
                  contractElement -> {
                    gtResult.setGtExist(true);
                    gtResult.setLabel(contractElement.getLabel());
                    gtResult.setIgnored(contractElement.isIgnored());

                    List<ProductElementDerived> productElementsDerived =
                        contractElement.getProductElements().stream()
                            .map(
                                productElement ->
                                    new ProductElementDerived(
                                        productElement.getCodeOffer(),
                                        productElement.getCodeProduct(),
                                        productElement.getCodeBenefitNature(),
                                        productElement.getCodeAmc(),
                                        productElement.getFrom(),
                                        productElement.getTo()))
                            .toList();

                    gtResult.setProductElements(productElementsDerived);
                  });

              return gtResult;
            })
        .toList();
  }

  public Optional<ContractElement> findByKeyAndVersion(
      String codeContractElement, String codeInsurer, String versionId) {
    final Query query = new Query();
    query.addCriteria(
        Criteria.where(CODE_CONTRACT_ELEMENT)
            .is(codeContractElement)
            .and(CODE_INSURER)
            .is(codeInsurer)
            .and(VERSION_ID)
            .is(versionId));
    return findOne(query);
  }

  public boolean existsByKeyAndVersion(
      String codeContractElement, String codeInsurer, String versionId) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(CODE_CONTRACT_ELEMENT)
            .is(codeContractElement)
            .and(CODE_INSURER)
            .is(codeInsurer)
            .and(VERSION_ID)
            .is(versionId));
    return template.exists(query, ContractElement.class);
  }

  public Optional<ContractElement> findByIdAndVersion(String gtId, String versionId) {
    Query q = new Query(Criteria.where(GT_ID).is(gtId).and(VERSION_ID).is(versionId));
    return findOne(q);
  }

  public int closeOneProductElementByGt(
      String gtId, String versionId, CloseProductElementRequest req) {

    Date toDate = BobbMongoHelpers.toUtcDate(req.to().atStartOfDay());
    Criteria filters = BobbMongoHelpers.productElementFilters(req.selector());

    return updateToDateByArrayFilter(gtId, versionId, toDate, filters);
  }

  public int closePeriodByGt(String gtId, String versionId, ClosePeriodRequest req) {
    Date toDate = BobbMongoHelpers.toUtcDate(req.to().atStartOfDay());
    Criteria filters = BobbMongoHelpers.periodFilters(req.period());

    return updateToDateByArrayFilter(gtId, versionId, toDate, filters);
  }

  public boolean existsByKey(String codeContractElement, String codeInsurer) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(CODE_CONTRACT_ELEMENT)
            .is(codeContractElement)
            .and(CODE_INSURER)
            .is(codeInsurer));
    return template.exists(query, ContractElement.class);
  }

  private int updateToDateByArrayFilter(
      String gtId, String versionId, Date toDate, Criteria arrayFilter) {

    Query q = Query.query(Criteria.where(GT_ID).is(gtId).and(VERSION_ID).is(versionId));
    Update u = new Update().set(PRODUCT_ELEMENTS + ".$[pe]." + TO, toDate);

    var res =
        template.updateFirst(q, u.filterArray(arrayFilter), Constants.CONTRACT_ELEMENT_COLLECTION);
    return (int) res.getModifiedCount();
  }

  public List<String> distinctGuaranteeCodesByOfferAndAmc(
      String activeVersionId, String codeOffer, String codeAmc) {
    List<AggregationOperation> ops = new ArrayList<>();
    ops.add(match(Criteria.where(VERSION_ID).is(activeVersionId)));
    ops.add(unwind(PRODUCT_ELEMENTS));

    List<Criteria> pe = new ArrayList<>();
    if (StringUtils.isNotBlank(codeOffer)) {
      pe.add(Criteria.where(PRODUCT_ELEMENTS + DOT + CODE_OFFER).is(codeOffer));
    }
    if (StringUtils.isNotBlank(codeAmc)) {
      pe.add(Criteria.where(PRODUCT_ELEMENTS + DOT + CODE_AMC).is(codeAmc));
    }
    if (!pe.isEmpty()) {
      ops.add(match(new Criteria().andOperator(pe.toArray(new Criteria[0]))));
    }
    ops.add(group(CODE_CONTRACT_ELEMENT));

    return distinctByIdAsc(ops);
  }

  public List<String> distinctOfferCodesByGuaranteeAndAmc(
      String activeVersionId, String codeContractElement, String codeAmc) {
    return getDistinctCodesByContractElement(
        activeVersionId, codeAmc, codeContractElement, CODE_AMC, CODE_OFFER);
  }

  public List<String> distinctAmcByOfferAndGuarantee(
      String activeVersionId, String codeOffer, String codeContractElement) {
    return getDistinctCodesByContractElement(
        activeVersionId, codeOffer, codeContractElement, CODE_OFFER, CODE_AMC);
  }

  private List<String> getDistinctCodesByContractElement(
      String activeVersionId,
      String criteriaValue,
      String codeContractElement,
      String criteriaField,
      String wantedField) {
    List<AggregationOperation> ops = new ArrayList<>();

    var top = Criteria.where(VERSION_ID).is(activeVersionId);
    if (StringUtils.isNotBlank(codeContractElement)) {
      top = top.and(CODE_CONTRACT_ELEMENT).is(codeContractElement);
    }
    ops.add(match(top));
    ops.add(unwind(PRODUCT_ELEMENTS));

    if (StringUtils.isNotBlank(criteriaValue)) {
      ops.add(match(Criteria.where(PRODUCT_ELEMENTS + DOT + criteriaField).is(criteriaValue)));
    }
    ops.add(group(PRODUCT_ELEMENTS + DOT + wantedField));

    return distinctByIdAsc(ops);
  }
}
