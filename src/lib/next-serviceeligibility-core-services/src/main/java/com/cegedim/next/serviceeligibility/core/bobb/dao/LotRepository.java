package com.cegedim.next.serviceeligibility.core.bobb.dao;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.GTDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.TechnicalGuaranteeRequest;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.bobb.LotRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.LotResponse;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GuaranteeException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.LotException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.mongodb.client.result.UpdateResult;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class LotRepository extends AbstractRepository<Lot> {

  private final MongoTemplate mongoTemplate;

  public LotRepository(MongoTemplate mongoTemplate) {
    super(Lot.class);
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * Get a lot by its id.
   *
   * @param idLots list of id
   * @return the lot, if any
   */
  @ContinueSpan(log = "getLotsById")
  public List<Lot> getLotsById(List<String> idLots) {
    if (CollectionUtils.isNotEmpty(idLots)) {
      Criteria criteria = Criteria.where(Constants.ID).in(idLots);
      return mongoTemplate.find(new Query(criteria), Lot.class);
    }
    return Collections.emptyList();
  }

  /**
   * Search in the database all lots
   *
   * @return A list of lots. If there is no lot, returns an empty list
   */
  @ContinueSpan(log = "getLots")
  public List<Lot> getLots() {
    return mongoTemplate.findAll(Lot.class);
  }

  @ContinueSpan(log = "getLotsByParametrageCarteTPActifs")
  public List<Lot> getLotsByParametrageCarteTPActifs(List<String> idLots) {
    if (idLots.isEmpty()) {
      return List.of();
    }

    Query lotsQuery = new Query(Criteria.where(Constants.ID).in(idLots));
    return mongoTemplate.find(lotsQuery, Lot.class);
  }

  @ContinueSpan(log = "getLotsAlmerysValides")
  public List<Lot> getLotsAlmerysValides(List<String> listCode) {
    if (listCode.isEmpty()) {
      return List.of();
    }

    Query lotsQuery = new Query(Criteria.where(Constants.CODE).in(listCode));
    return mongoTemplate.find(lotsQuery, Lot.class);
  }

  /**
   * Get a lot by its functional key.
   *
   * @param request lot
   * @return the lot, if any
   */
  @ContinueSpan(log = "getLot")
  public LotResponse getLot(LotRequest request) {
    final Pageable pageableRequest = PageRequest.of(request.getPage() - 1, request.getPerPage());
    Query queryLot = new Query();
    LotResponse response = new LotResponse();

    if (StringUtils.isNotBlank(request.getCode())) {
      queryLot.addCriteria(Criteria.where(Constants.CODE).regex(request.getCode(), "i"));
    }
    if (StringUtils.isNotBlank(request.getLibelle())) {
      queryLot.addCriteria(Criteria.where(Constants.LIBELLE).regex(request.getLibelle(), "i"));
    }
    if (StringUtils.isNotBlank(request.getCodeAssureur())
        && StringUtils.isNotBlank(request.getCodeGarantie())) {
      queryLot.addCriteria(
          Criteria.where(Constants.GARANTIE_TECHNIQUES)
              .elemMatch(
                  new Criteria()
                      .andOperator(
                          Criteria.where(Constants.CODE_ASSUREUR).is(request.getCodeAssureur()),
                          Criteria.where(Constants.CODE_GARANTIE).is(request.getCodeGarantie()))));
    } else if (StringUtils.isNotBlank(request.getCodeAssureur())) {
      queryLot.addCriteria(
          Criteria.where(Constants.GARANTIE_TECHNIQUES)
              .elemMatch(Criteria.where(Constants.CODE_ASSUREUR).is(request.getCodeAssureur())));
    } else if (StringUtils.isNotBlank(request.getCodeGarantie())) {
      queryLot.addCriteria(
          Criteria.where(Constants.GARANTIE_TECHNIQUES)
              .elemMatch(Criteria.where(Constants.CODE_GARANTIE).is(request.getCodeGarantie())));
    }

    queryLot.with(sortDocuments(request.getSortBy(), request.getDirection()));
    List<Lot> totalElementsLot = mongoTemplate.find(queryLot, Lot.class);
    int totalElements = totalElementsLot.size();
    queryLot.with(pageableRequest);
    List<Lot> lot = mongoTemplate.find(queryLot, Lot.class);
    response.setLot(lot);
    PagingResponseModel paging = new PagingResponseModel();
    paging.setPage(request.getPage());
    paging.setPerPage(request.getPerPage());
    paging.setTotalElements(totalElements);
    paging.setTotalPages((int) Math.ceil((double) totalElements / (double) request.getPerPage()));
    response.setPaging(paging);
    return response;
  }

  /**
   * Create a lot.
   *
   * @param newLot object Lot
   */
  @ContinueSpan(log = "createLot")
  public void createLot(Lot newLot) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
    Lot lot = new Lot();
    if (newLot.getId() != null) {
      lot.setId(newLot.getId());
    }
    if (findByCode(newLot.getCode()) != null) {
      throw new LotException("Lot with the same code already exists.");
    }
    List<GarantieTechnique> garantieTechniques = new ArrayList<>();
    lot.setCode(newLot.getCode());
    lot.setLibelle(newLot.getLibelle());
    for (int i = 0; i < newLot.getGarantieTechniques().size(); i++) {
      GarantieTechnique gt = new GarantieTechnique();
      gt.setCodeAssureur(newLot.getGarantieTechniques().get(i).getCodeAssureur());
      gt.setCodeGarantie(newLot.getGarantieTechniques().get(i).getCodeGarantie());
      gt.setDateAjout(currentDate.format(formatter));
      garantieTechniques.add(gt);
    }
    lot.setGarantieTechniques(garantieTechniques);

    template.save(lot, "lot");
  }

  /**
   * Update a lot.
   *
   * @param newLot object Lot
   */
  @ContinueSpan(log = "updateLot")
  public void updateLot(Lot newLot) {
    Lot originalLot = findByCode(newLot.getCode());
    if (originalLot != null) {
      // MAJ Lot
      initializeLot(newLot, originalLot);

      Query query = new Query(Criteria.where(Constants.CODE).is(originalLot.getCode()));
      Update update =
          new Update()
              .set("libelle", originalLot.getLibelle())
              .set("garantieTechniques", originalLot.getGarantieTechniques());

      mongoTemplate.updateFirst(query, update, Lot.class);
    }
  }

  @ContinueSpan(log = "update")
  public void update(Lot newLot) {
    Query query = new Query(Criteria.where(Constants.CODE).is(newLot.getCode()));
    Update update =
        new Update()
            .set("libelle", newLot.getLibelle())
            .set("garantieTechniques", newLot.getGarantieTechniques());
    mongoTemplate.updateFirst(query, update, Lot.class);
  }

  /**
   * Get a lot by its code.
   *
   * @param code code of the lot
   * @return the lot, if any
   */
  @ContinueSpan(log = "findByCode Lot")
  public Lot findByCode(String code) {
    Lot lot = null;
    if (StringUtils.isNotBlank(code)) {
      final Criteria criteria = Criteria.where(Constants.CODE).is(code);
      lot = template.findOne(Query.query(criteria), Lot.class);
    }
    return lot;
  }

  private void initializeLot(Lot newLot, Lot originalLot) {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
    originalLot.setLibelle(newLot.getLibelle());

    updateSuppressionLogique(newLot, originalLot, currentDate, formatter);

    addNewGaranties(newLot, originalLot, currentDate, formatter);

    addGarantiesWithSuppression(newLot, originalLot, currentDate, formatter);
  }

  private void updateSuppressionLogique(
      Lot newLot, Lot originalLot, LocalDateTime currentDate, DateTimeFormatter formatter) {
    originalLot.getGarantieTechniques().stream()
        .filter(originalGT -> !newLot.getGarantieTechniques().contains(originalGT))
        .forEach(originalGT -> originalGT.setDateSuppressionLogique(currentDate.format(formatter)));
  }

  private void addNewGaranties(
      Lot newLot, Lot originalLot, LocalDateTime currentDate, DateTimeFormatter formatter) {
    newLot.getGarantieTechniques().stream()
        .filter(newGT -> !originalLot.getGarantieTechniques().contains(newGT))
        .forEach(
            newGT -> {
              newGT.setDateAjout(currentDate.format(formatter));
              originalLot.getGarantieTechniques().add(newGT);
            });
  }

  private void addGarantiesWithSuppression(
      Lot newLot, Lot originalLot, LocalDateTime currentDate, DateTimeFormatter formatter) {
    for (GarantieTechnique newGT : newLot.getGarantieTechniques()) {
      String codeGarantie = newGT.getCodeGarantie();

      boolean shouldAdd =
          originalLot.getGarantieTechniques().stream()
              .noneMatch(
                  originalGT ->
                      originalGT.getCodeGarantie().equals(codeGarantie)
                          && originalGT.getDateSuppressionLogique() == null);

      if (shouldAdd) {
        GarantieTechnique updatedGT = new GarantieTechnique();
        updatedGT.setCodeAssureur(newGT.getCodeAssureur());
        updatedGT.setCodeGarantie(codeGarantie);
        updatedGT.setDateAjout(currentDate.format(formatter));

        originalLot.getGarantieTechniques().add(updatedGT);
      }
    }
  }

  @ContinueSpan(log = "getAll Lot")
  public Collection<Lot> getAll() {
    return find();
  }

  @ContinueSpan(log = "addGarantieTechnique")
  public void addGarantieTechnique(
      String lotCode, TechnicalGuaranteeRequest technicalGuaranteeRequest) {
    try {
      GTDto newGT = getGtDto(technicalGuaranteeRequest);
      Query query =
          new Query(
              Criteria.where(Constants.CODE)
                  .is(lotCode)
                  .and(Constants.GARANTIE_TECHNIQUES)
                  .not()
                  .elemMatch(
                      Criteria.where(Constants.CODE_ASSUREUR)
                          .is(newGT.getCodeAssureur())
                          .and(Constants.CODE_GARANTIE)
                          .is(newGT.getCodeGarantie())));

      Update update = new Update().push(Constants.GARANTIE_TECHNIQUES, newGT);
      UpdateResult result = mongoTemplate.updateFirst(query, update, Lot.class);

      if (result.getMatchedCount() == 0) {
        throw new GuaranteeException(
            "Cette garantie est déjà associée au lot " + lotCode,
            HttpStatus.BAD_REQUEST,
            RestErrorConstants.ERROR_GUARANTEE);
      }
    } catch (DataAccessException e) {
      throw new LotException(
          "Erreur interne lors de l'ajout de la garantie technique au lot " + lotCode);
    }
  }

  @ContinueSpan(log = "updateDateSuppressionLogiqueGT")
  public void updateDateSuppressionLogiqueGT(
      String lotCode, TechnicalGuaranteeRequest technicalGuaranteeRequest) {
    String dateSuppression = DateUtils.getNow();
    Query query =
        new Query(
            Criteria.where(Constants.CODE)
                .is(lotCode)
                .and(Constants.GARANTIE_TECHNIQUES)
                .elemMatch(
                    Criteria.where(Constants.CODE_ASSUREUR)
                        .is(technicalGuaranteeRequest.getInsurerCode())
                        .and(Constants.CODE_GARANTIE)
                        .is(technicalGuaranteeRequest.getGuaranteeCode())));

    Update update =
        new Update()
            .set(
                Constants.GARANTIE_TECHNIQUES
                    + Constants.DOT
                    + "$"
                    + Constants.DOT
                    + Constants.DATE_SUPPRESSION_LOGIQUE,
                dateSuppression);
    mongoTemplate.updateFirst(query, update, Lot.class);
  }

  private @NotNull GTDto getGtDto(TechnicalGuaranteeRequest technicalGuaranteeRequest) {
    GTDto newGT = new GTDto();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.DD_MM_YY_HH_MM_SS);
    newGT.setCodeGarantie(technicalGuaranteeRequest.getGuaranteeCode());
    newGT.setCodeAssureur(technicalGuaranteeRequest.getInsurerCode());
    newGT.setDateAjout(LocalDateTime.now().format(formatter));
    return newGT;
  }
}
