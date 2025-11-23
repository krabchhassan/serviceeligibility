package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.almerysProductRef.AlmerysProductRequest;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.AlmerysProductResponse;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.dao.AbstractRepository;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ProductCombination;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.utils.AlmerysConstants;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.LotException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class AlmerysProductReferentialRepository extends AbstractRepository<AlmerysProduct> {
  public static final String SEPARATOR = "#";
  private MongoTemplate mongoTemplate;

  public AlmerysProductReferentialRepository(MongoTemplate mongoTemplate) {
    super(AlmerysProduct.class);
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * Search in the database all AlmerysProductReferential
   *
   * @return A list of AlmerysProductReferential. If there is no AlmerysProductReferential, returns
   *     an empty list
   */
  @ContinueSpan(log = "getAlmerysProductReferential")
  public Collection<AlmerysProduct> getAlmerysProductReferential() {
    return find();
  }

  @ContinueSpan(log = "getAlmerysProduct")
  public AlmerysProductResponse getAlmerysProduct(AlmerysProductRequest request) {
    final Pageable pageableRequest = PageRequest.of(request.getPage() - 1, request.getPerPage());
    Query queryAlmerysProduct = new Query();
    AlmerysProductResponse response = new AlmerysProductResponse();

    if (StringUtils.isNotBlank(request.getCode())) {
      queryAlmerysProduct.addCriteria(Criteria.where(Constants.CODE).regex(request.getCode(), "i"));
    }
    if (StringUtils.isNotBlank(request.getDescription())) {
      queryAlmerysProduct.addCriteria(
          Criteria.where(AlmerysConstants.DESCRIPTION).regex(request.getDescription(), "i"));
    }
    productCombinationCriteria(request, queryAlmerysProduct);

    String sortColumn;
    Sort.Direction sortDirection;
    if (StringUtils.isNotBlank(request.getSortBy())) {
      sortColumn = request.getSortBy();
      if (request.getDirection().equalsIgnoreCase(Constants.DESC)) {
        sortDirection = Sort.Direction.DESC;
      } else {
        sortDirection = Sort.Direction.ASC;
      }
    } else {
      sortColumn = Constants.CODE;
      sortDirection = Sort.Direction.ASC;
    }
    queryAlmerysProduct.with(Sort.by(sortDirection, sortColumn));

    List<AlmerysProduct> totalElementsLot =
        mongoTemplate.find(queryAlmerysProduct, AlmerysProduct.class);
    int totalElements = totalElementsLot.size();
    queryAlmerysProduct.with(pageableRequest);
    List<AlmerysProduct> almerysProductList =
        mongoTemplate.find(queryAlmerysProduct, AlmerysProduct.class);

    // Tri productCombinations par dateDebut, en affichant en dernier celles qui sont annulées
    almerysProductList.forEach(this::sortAlmerysProduct);

    response.setAlmerysProductList(almerysProductList);
    PagingResponseModel paging = new PagingResponseModel();
    paging.setPage(request.getPage());
    paging.setPerPage(request.getPerPage());
    paging.setTotalElements(totalElements);
    paging.setTotalPages((int) Math.ceil((double) totalElements / (double) request.getPerPage()));
    response.setPaging(paging);
    return response;
  }

  public void sortAlmerysProduct(AlmerysProduct almerysProduct) {
    if (almerysProduct.getProductCombinations() != null) {
      almerysProduct
          .getProductCombinations()
          .sort(
              Comparator.comparing(
                      (ProductCombination pc) ->
                          !DateUtils.isPeriodeValide(
                              pc.getDateDebut(), pc.getDateFin(), DateUtils.SLASHED_FORMATTER))
                  .thenComparing(
                      pc -> LocalDate.parse(pc.getDateDebut(), DateUtils.SLASHED_FORMATTER)));
    }
  }

  private void productCombinationCriteria(
      AlmerysProductRequest request, Query queryAlmerysProduct) {
    boolean hasGts = CollectionUtils.isNotEmpty(request.getGts());
    boolean hasLots = CollectionUtils.isNotEmpty(request.getLots());
    if (hasGts && hasLots) {
      List<Criteria> combinations = new ArrayList<>();
      for (String gt : request.getGts()) {
        String[] params = gt.split(SEPARATOR);

        if (params.length < 2) {
          continue;
        }

        Criteria gtElem =
            Criteria.where(AlmerysConstants.CODE_ASSUREUR)
                .is(params[0])
                .and(AlmerysConstants.CODE_GARANTIE)
                .is(params[1])
                .and(AlmerysConstants.DATE_SUPPRESSION)
                .exists(request.isShowDeletedGts());

        Criteria lotElem =
            Criteria.where(AlmerysConstants.CODE)
                .in(request.getLots())
                .and(AlmerysConstants.DATE_SUPPRESSION)
                .exists(request.isShowDeletedLots());

        // Construire deux elemMatch séparés : un pour GT, un pour Lot
        Criteria gtCriteria =
            Criteria.where(AlmerysConstants.PRODUCT_COMBINATIONS)
                .elemMatch(Criteria.where(AlmerysConstants.GARANTIES_TECHNIQUE).elemMatch(gtElem));
        Criteria lotCriteria =
            Criteria.where(AlmerysConstants.PRODUCT_COMBINATIONS)
                .elemMatch(Criteria.where(AlmerysConstants.LOTS).elemMatch(lotElem));

        // Combiner les deux avec andOperator pour forcer la présence des deux, même s'ils sont dans
        // des productCombinations différents
        Criteria comboCriteria = new Criteria().andOperator(gtCriteria, lotCriteria);
        combinations.add(comboCriteria);
      }

      if (CollectionUtils.isNotEmpty(combinations)) {
        queryAlmerysProduct.addCriteria(
            new Criteria().orOperator(combinations.toArray(new Criteria[0])));
      }
    } else if (hasGts) {
      List<Criteria> gtCriteriaList = new ArrayList<>();
      for (String gt : request.getGts()) {
        String[] params = gt.split(SEPARATOR);

        if (params.length < 2) {
          continue;
        }

        Criteria gtCriteria =
            Criteria.where(AlmerysConstants.PRODUCT_COMBINATIONS)
                .elemMatch(
                    Criteria.where(AlmerysConstants.GARANTIES_TECHNIQUE)
                        .elemMatch(
                            Criteria.where(AlmerysConstants.CODE_ASSUREUR)
                                .is(params[0])
                                .and(AlmerysConstants.CODE_GARANTIE)
                                .is(params[1])
                                .and(AlmerysConstants.DATE_SUPPRESSION)
                                .exists(request.isShowDeletedGts())));
        gtCriteriaList.add(gtCriteria);
      }

      if (CollectionUtils.isNotEmpty(gtCriteriaList)) {
        queryAlmerysProduct.addCriteria(
            new Criteria().orOperator(gtCriteriaList.toArray(new Criteria[0])));
      }
    } else if (hasLots) {
      Criteria lotCriteria =
          Criteria.where(AlmerysConstants.PRODUCT_COMBINATIONS)
              .elemMatch(
                  Criteria.where(AlmerysConstants.LOTS)
                      .elemMatch(
                          Criteria.where(AlmerysConstants.CODE)
                              .in(request.getLots())
                              .and(AlmerysConstants.DATE_SUPPRESSION)
                              .exists(request.isShowDeletedLots())));
      queryAlmerysProduct.addCriteria(lotCriteria);
    }
  }

  /**
   * Update a almerysProduct.
   *
   * @param newAlmerysProduct object AlmerysProduct
   */
  @ContinueSpan(log = "updateAlmerysProduct")
  public void updateAlmerysProduct(AlmerysProduct newAlmerysProduct) {
    AlmerysProduct originalAlmerysProduct = findByCode(newAlmerysProduct.getCode());
    if (originalAlmerysProduct != null) {
      sortAlmerysProduct(originalAlmerysProduct);
      // MAJ AlmerysProduct
      initializeAlmerysProduct(newAlmerysProduct, originalAlmerysProduct);

      Query query = new Query(Criteria.where(Constants.CODE).is(originalAlmerysProduct.getCode()));
      Update update =
          new Update()
              .set(
                  AlmerysConstants.PRODUCT_COMBINATIONS,
                  originalAlmerysProduct.getProductCombinations());

      mongoTemplate.updateFirst(query, update, AlmerysProduct.class);
    }
  }

  /**
   * Get a almerysProduct by its code.
   *
   * @param code code of the almerysProduct
   * @return the almerysProduct, if any
   */
  @ContinueSpan(log = "findByCode AlmerysProduct")
  public AlmerysProduct findByCode(String code) {
    AlmerysProduct almerysProduct = null;
    if (StringUtils.isNotBlank(code)) {
      final Criteria criteria = Criteria.where(Constants.CODE).is(code);
      almerysProduct = template.findOne(Query.query(criteria), AlmerysProduct.class);
    }
    return almerysProduct;
  }

  @ContinueSpan(log = "getIdLotsInAlmerysProduct")
  public List<String> getIdLotsInAlmerysProduct() {
    AggregationResults<Document> results =
        mongoTemplate.aggregate(
            buildLotsAlmerysValidesAggregation(),
            Constants.ALMERYS_PRODUCT_COLLECTION,
            Document.class);
    return results.getMappedResults().stream()
        .map(d -> d.getString(Constants.ID_LOT_ALIAS))
        .filter(Objects::nonNull)
        .distinct()
        .toList();
  }

  private Aggregation buildLotsAlmerysValidesAggregation() {
    String combos = Constants.PRODUCT_COMBINATIONS;
    String lots = combos + Constants.DOT + Constants.LOT_ALMERYS_LIST;
    String idAlias = Constants.ID_LOT_ALIAS;

    return Aggregation.newAggregation(
        Aggregation.unwind(combos),
        Aggregation.addFields()
            .addField(combos + Constants.DOT + Constants.DATE_DEBUT_DATE)
            .withValue(
                DateOperators.DateFromString.fromString(
                        "$" + combos + Constants.DOT + Constants.DATE_DEBUT)
                    .withFormat(Constants.FORMAT_DATE))
            .addField(combos + Constants.DOT + Constants.DATE_FIN_DATE)
            .withValue(
                DateOperators.DateFromString.fromString(
                        "$" + combos + Constants.DOT + Constants.DATE_FIN)
                    .withFormat(Constants.FORMAT_DATE))
            .build(),
        Aggregation.unwind(lots, true),
        Aggregation.match(
            new Criteria()
                .orOperator(
                    Criteria.where(lots + Constants.DOT + Constants.DATE_SUPPRESSION_LOGIQUE)
                        .is(null),
                    Criteria.where(lots + Constants.DOT + Constants.DATE_SUPPRESSION_LOGIQUE)
                        .is(""))),
        Aggregation.project().and(lots + Constants.DOT + Constants.CODE).as(idAlias),
        Aggregation.group(idAlias).first(idAlias).as(idAlias),
        Aggregation.project(idAlias).andExclude(Constants.ID));
  }

  private void initializeAlmerysProduct(
      AlmerysProduct newAlmerysProduct, AlmerysProduct originalAlmerysProduct) {
    updateProductCombinations(
        newAlmerysProduct.getProductCombinations(),
        originalAlmerysProduct.getProductCombinations());
  }

  private void updateProductCombinations(
      List<ProductCombination> newProductCombination,
      List<ProductCombination> originalProductCombination) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);

    int maxSize = Math.max(newProductCombination.size(), originalProductCombination.size());
    for (int i = 0; i < maxSize; i++) {
      if (i >= originalProductCombination.size()) {
        addProductCombination(
            newProductCombination.get(i),
            originalProductCombination,
            currentDate.format(formatter));
      } else {
        updateProductCombination(
            newProductCombination.get(i),
            originalProductCombination.get(i),
            currentDate.format(formatter));
      }
    }
  }

  private void addProductCombination(
      ProductCombination newProductCombination,
      List<ProductCombination> originalProductCombination,
      String today) {
    Optional.ofNullable(newProductCombination.getGarantieTechniqueList())
        .ifPresent(
            list ->
                list.forEach(
                    gt -> {
                      if (gt.getDateAjout() == null) {
                        gt.setDateAjout(today);
                      }
                    }));

    Optional.ofNullable(newProductCombination.getLotAlmerysList())
        .ifPresent(
            list ->
                list.forEach(
                    lot -> {
                      if (lot.getDateAjout() == null) {
                        lot.setDateAjout(today);
                      }
                    }));
    originalProductCombination.add(newProductCombination);
  }

  private void updateProductCombination(
      ProductCombination newProductCombination,
      ProductCombination originalProductCombination,
      String today) {
    updateEndDate(newProductCombination, originalProductCombination);

    updateGuaranteeList(newProductCombination, originalProductCombination, today);
    updateLotList(newProductCombination, originalProductCombination, today);
  }

  private void updateLotList(
      ProductCombination newProductCombination,
      ProductCombination originalProductCombination,
      String today) {
    initializeLotList(originalProductCombination);
    if (CollectionUtils.isNotEmpty(originalProductCombination.getLotAlmerysList())
        && CollectionUtils.isEmpty((newProductCombination.getLotAlmerysList()))) {
      originalProductCombination.getLotAlmerysList().stream()
          .filter(lot -> lot.getDateSuppressionLogique() == null)
          .forEach(lot -> lot.setDateSuppressionLogique(today));
    }
    if (CollectionUtils.isNotEmpty(originalProductCombination.getLotAlmerysList())
        && CollectionUtils.isNotEmpty(newProductCombination.getLotAlmerysList())) {
      updateSuppressionLogiqueForLot(
          newProductCombination.getLotAlmerysList(),
          originalProductCombination.getLotAlmerysList(),
          today);
    }
    if (CollectionUtils.isNotEmpty(newProductCombination.getLotAlmerysList())) {
      addNewLots(
          newProductCombination.getLotAlmerysList(),
          originalProductCombination.getLotAlmerysList(),
          today);
    }

    if (CollectionUtils.isNotEmpty(originalProductCombination.getLotAlmerysList())
        && CollectionUtils.isNotEmpty(newProductCombination.getLotAlmerysList())) {
      addLotsWithSuppression(
          newProductCombination.getLotAlmerysList(),
          originalProductCombination.getLotAlmerysList(),
          today);
    }
  }

  private static void initializeLotList(ProductCombination originalProductCombination) {
    if (originalProductCombination.getLotAlmerysList() == null) {
      originalProductCombination.setLotAlmerysList(new ArrayList<>());
    }
  }

  private void updateGuaranteeList(
      ProductCombination newProductCombination,
      ProductCombination originalProductCombination,
      String today) {
    initializeGuaranteeList(originalProductCombination);
    if (CollectionUtils.isNotEmpty(originalProductCombination.getGarantieTechniqueList())
        && CollectionUtils.isEmpty((newProductCombination.getGarantieTechniqueList()))) {
      originalProductCombination.getGarantieTechniqueList().stream()
          .filter(gt -> gt.getDateSuppressionLogique() == null)
          .forEach(gt -> gt.setDateSuppressionLogique(today));
    }
    if (CollectionUtils.isNotEmpty(originalProductCombination.getGarantieTechniqueList())
        && CollectionUtils.isNotEmpty(newProductCombination.getGarantieTechniqueList())) {
      updateSuppressionLogiqueForGT(
          newProductCombination.getGarantieTechniqueList(),
          originalProductCombination.getGarantieTechniqueList(),
          today);
    }

    if (CollectionUtils.isNotEmpty(newProductCombination.getGarantieTechniqueList())) {
      addNewGaranties(
          newProductCombination.getGarantieTechniqueList(),
          originalProductCombination.getGarantieTechniqueList(),
          today);
    }

    if (CollectionUtils.isNotEmpty(originalProductCombination.getGarantieTechniqueList())
        && CollectionUtils.isNotEmpty(newProductCombination.getGarantieTechniqueList())) {
      addGarantiesWithSuppression(
          newProductCombination.getGarantieTechniqueList(),
          originalProductCombination.getGarantieTechniqueList(),
          today);
    }
  }

  private static void initializeGuaranteeList(ProductCombination originalProductCombination) {
    if (originalProductCombination.getGarantieTechniqueList() == null) {
      originalProductCombination.setGarantieTechniqueList(new ArrayList<>());
    }
  }

  private static void updateEndDate(
      ProductCombination newProductCombination, ProductCombination originalProductCombination) {
    if (StringUtils.isNotBlank(newProductCombination.getDateFin())) {
      originalProductCombination.setDateFin(newProductCombination.getDateFin());
    } else {
      originalProductCombination.setDateFin(null);
    }
  }

  private String buildGTKey(GarantieTechnique gt) {
    return gt.getCodeAssureur() + SEPARATOR + gt.getCodeGarantie();
  }

  private void updateSuppressionLogiqueForGT(
      List<GarantieTechnique> newGTList,
      List<GarantieTechnique> originalGTList,
      String currentDate) {
    Set<String> existingGTKey =
        newGTList.stream().map(this::buildGTKey).collect(Collectors.toSet());

    originalGTList.stream()
        .filter(originalGT -> !existingGTKey.contains(buildGTKey(originalGT)))
        .filter(originalGT -> originalGT.getDateSuppressionLogique() == null)
        .forEach(originalGT -> originalGT.setDateSuppressionLogique(currentDate));
  }

  private void addNewGaranties(
      List<GarantieTechnique> newGTList,
      List<GarantieTechnique> originalGTList,
      String currentDate) {
    Set<String> existingGTKey =
        originalGTList.stream().map(this::buildGTKey).collect(Collectors.toSet());

    if (CollectionUtils.isNotEmpty(newGTList)) {
      for (GarantieTechnique gt : newGTList) {
        String gtKey = buildGTKey(gt);
        if (!existingGTKey.contains(gtKey)) {
          gt.setDateAjout(currentDate);
          originalGTList.add(gt);
        }
      }
    }
  }

  private void addGarantiesWithSuppression(
      List<GarantieTechnique> newGTList,
      List<GarantieTechnique> originalGTList,
      String currentDate) {
    for (GarantieTechnique newGT : newGTList) {
      String gtKey = buildGTKey(newGT);

      Optional<GarantieTechnique> existingGtOpt =
          originalGTList.stream().filter(gt -> (buildGTKey(gt)).equals(gtKey)).findFirst();

      if (existingGtOpt.isPresent()) {
        GarantieTechnique existingGt = existingGtOpt.get();
        if (existingGt.getDateSuppressionLogique() != null
            && newGT.getDateSuppressionLogique() == null) {
          existingGt.setDateSuppressionLogique(null);
          existingGt.setDateAjout(currentDate);
        }
      } else {
        GarantieTechnique updatedGT = new GarantieTechnique();
        updatedGT.setCodeAssureur(newGT.getCodeAssureur());
        updatedGT.setCodeGarantie(newGT.getCodeGarantie());
        updatedGT.setDateAjout(currentDate);

        originalGTList.add(updatedGT);
      }
    }
  }

  private void updateSuppressionLogiqueForLot(
      List<LotAlmerys> newLotList, List<LotAlmerys> originalLotList, String currentDate) {
    Set<String> newLotCodes =
        newLotList.stream().map(LotAlmerys::getCode).collect(Collectors.toSet());

    originalLotList.stream()
        .filter(originalLot -> !newLotCodes.contains(originalLot.getCode()))
        .filter(originalLot -> originalLot.getDateSuppressionLogique() == null)
        .forEach(originalLot -> originalLot.setDateSuppressionLogique(currentDate));
  }

  private void addNewLots(
      List<LotAlmerys> newLotList, List<LotAlmerys> originalLotList, String currentDate) {
    Set<String> existingCodes =
        originalLotList.stream().map(LotAlmerys::getCode).collect(Collectors.toSet());

    if (CollectionUtils.isNotEmpty(newLotList)) {
      for (LotAlmerys lot : newLotList) {
        if (!existingCodes.contains(lot.getCode())) {
          lot.setDateAjout(currentDate);
          originalLotList.add(lot);
        }
      }
    }
  }

  private void addLotsWithSuppression(
      List<LotAlmerys> newLotList, List<LotAlmerys> originalLotList, String currentDate) {
    for (LotAlmerys newLot : newLotList) {
      String code = newLot.getCode();

      Optional<LotAlmerys> existingLotOpt =
          originalLotList.stream().filter(lot -> lot.getCode().equals(code)).findFirst();

      if (existingLotOpt.isPresent()) {
        LotAlmerys existingLot = existingLotOpt.get();
        if (existingLot.getDateSuppressionLogique() != null
            && newLot.getDateSuppressionLogique() == null) {
          existingLot.setDateSuppressionLogique(null);
          existingLot.setDateAjout(currentDate);
        }
      } else {
        LotAlmerys updatedLot = new LotAlmerys();
        updatedLot.setId(code);
        updatedLot.setCode(code);
        updatedLot.setDateAjout(currentDate);

        originalLotList.add(updatedLot);
      }
    }
  }

  /**
   * Create a almerysProduct.
   *
   * @param newAlmerysProduct object AlmerysProduct
   */
  @ContinueSpan(log = "createAlmerysProduct")
  public AlmerysProduct createAlmerysProduct(AlmerysProduct newAlmerysProduct) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
    AlmerysProduct almerysProduct = new AlmerysProduct();
    if (newAlmerysProduct.getCode() != null) {
      almerysProduct.setCode(newAlmerysProduct.getCode());
    }
    if (findByCode(newAlmerysProduct.getCode()) != null) {
      throw new LotException("Lot with the same code already exists.");
    }
    List<ProductCombination> productCombinations = new ArrayList<>();
    almerysProduct.setCode(newAlmerysProduct.getCode());
    almerysProduct.setDescription(newAlmerysProduct.getDescription());
    for (int i = 0; i < newAlmerysProduct.getProductCombinations().size(); i++) {
      ProductCombination productCombination = newAlmerysProduct.getProductCombinations().get(i);
      createGaranties(productCombination, currentDate.format(formatter));
      createLots(productCombination, currentDate.format(formatter));
      productCombinations.add(productCombination);
    }
    almerysProduct.setProductCombinations(productCombinations);

    return template.save(almerysProduct, "almerysProduct");
  }

  @ContinueSpan(log = "findByGuaranteeCodeAndInsurerCode")
  public List<AlmerysProduct> findByGuaranteeCodeAndInsurerCode(
      String guaranteeCode, String insurerCode) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(
                Constants.PRODUCT_COMBINATIONS + Constants.DOT + Constants.GARANTIE_TECHNIQUE_LIST)
            .elemMatch(
                Criteria.where(Constants.CODE_GARANTIE)
                    .is(guaranteeCode)
                    .and(Constants.CODE_ASSUREUR)
                    .is(insurerCode)));
    return mongoTemplate.find(query, AlmerysProduct.class);
  }

  private void createGaranties(ProductCombination newProductCombination, String currentDate) {
    List<GarantieTechnique> garantieTechniqueList =
        newProductCombination.getGarantieTechniqueList();
    if (CollectionUtils.isNotEmpty(garantieTechniqueList)) {
      garantieTechniqueList.stream()
          .forEach(garantieTechnique -> garantieTechnique.setDateAjout(currentDate));
    }
  }

  private void createLots(ProductCombination newProductCombination, String currentDate) {
    List<LotAlmerys> lotAlmerysList = newProductCombination.getLotAlmerysList();
    if (CollectionUtils.isNotEmpty(lotAlmerysList)) {
      lotAlmerysList.stream().forEach(lotAlmerys -> lotAlmerys.setDateAjout(currentDate));
    }
  }
}
