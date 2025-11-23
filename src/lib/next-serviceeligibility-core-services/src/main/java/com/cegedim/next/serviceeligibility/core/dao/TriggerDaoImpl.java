package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DecompteDeclarations;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository("triggerDaoImpl")
@RequiredArgsConstructor
public class TriggerDaoImpl implements TriggerDao {

  private final Logger logger = LoggerFactory.getLogger(TriggerDaoImpl.class);

  private final MongoTemplate template;

  private final AuthenticationFacade authenticationFacade;

  @Override
  @ContinueSpan(log = "getTriggerById")
  public Trigger getTriggerById(String id) {
    return template.findById(id, Trigger.class);
  }

  @Override
  @ContinueSpan(log = "getTriggeredBenefById")
  public TriggeredBeneficiary getTriggeredBenefById(String id) {
    return template.findById(id, TriggeredBeneficiary.class);
  }

  @Override
  @ContinueSpan(log = "getTriggers")
  public TriggerResponse getTriggers(
      int perPage, int page, String sortBy, String direction, TriggerRequest request) {
    // case nir ou numContrat present -> use aggregation
    if (StringUtils.isNotBlank(request.getNir())
        || StringUtils.isNotBlank(request.getNumeroContrat())) {
      return getTriggersComplex(perPage, page, sortBy, direction, request);
    }

    TriggerResponse response = new TriggerResponse();
    Query query = new Query();

    Criteria triggerCriteria = getTriggerCriteriaFromRequest(request, "");

    query.addCriteria(triggerCriteria);

    final Pageable pageableRequest = PageRequest.of(page - 1, perPage);
    long totalElements = template.count(query, Trigger.class, Constants.TRIGGER);
    query.with(getSort(sortBy, direction));
    query.with(pageableRequest);
    List<Trigger> triggers = template.find(query, Trigger.class, Constants.TRIGGER);
    triggers.forEach(
        trigger ->
            trigger.setNbBenefToProcess(
                (int)
                    getNbTriggeredBeneficiariesWithStatus(
                        trigger.getId(), TriggeredBeneficiaryStatusEnum.ToProcess)));
    response.setTriggers(triggers);
    // Pagination
    PagingResponseModel paging = getPagingResponseModel(perPage, page, totalElements);
    response.setPaging(paging);

    return response;
  }

  private Sort getSort(String sortBy, String direction) {
    String sortColumn;
    Sort.Direction sortDirection;
    if (StringUtils.isNotBlank(sortBy)) {
      sortColumn = sortBy;
      if (direction.equalsIgnoreCase(Constants.DESC)) {
        sortDirection = Sort.Direction.DESC;
      } else {
        sortDirection = Sort.Direction.ASC;
      }
    } else {
      sortColumn = Constants.DATE_DEBUT_TRAITEMENT;
      sortDirection = Sort.Direction.DESC;
    }
    return Sort.by(sortDirection, sortColumn);
  }

  private PagingResponseModel getPagingResponseModel(int perPage, int page, double totalElements) {
    PagingResponseModel paging = new PagingResponseModel();
    paging.setPage(page);
    paging.setPerPage(perPage);
    paging.setTotalElements((int) totalElements);
    paging.setTotalPages((int) Math.ceil(totalElements / perPage));
    return paging;
  }

  private TriggerResponse getTriggersComplex(
      int perPage, int page, String sortBy, String direction, TriggerRequest request) {
    AggregationOperation lookup;
    String collection;
    AggregationOperation match1;
    AggregationOperation match2;
    Criteria triggerCriteria;
    Criteria triggerBenefCriteria;
    AddFieldsOperation addFieldsOperation;

    AggregationOperation sort = Aggregation.sort(getSort(sortBy, direction));

    AggregationOperation facet =
        Aggregation.facet(
                Aggregation.count().as(Constants.TOTAL_ELEMENTS),
                Aggregation.addFields().addFieldWithValue(Constants.PAGE2, page).build(),
                Aggregation.addFields().addFieldWithValue(Constants.TOTAL_PAGES, 0).build(),
                Aggregation.addFields().addFieldWithValue(Constants.PER_PAGE, perPage).build())
            .as(Constants.PAGING)
            .and(Aggregation.skip((long) perPage * (long) (page - 1)), Aggregation.limit(perPage))
            .as(Constants.TRIGGERS);
    AggregationOperation unwindPaging = Aggregation.unwind(String.format("$%s", Constants.PAGING));
    Aggregation agg;

    addFieldsOperation =
        Aggregation.addFields()
            .addFieldWithValue(
                Constants.TRIGGER_OBJ_ID,
                ConvertOperators.ToObjectId.toObjectId("$" + Constants.ID_TRIGGER))
            .build();
    lookup =
        Aggregation.lookup(
            Constants.TRIGGER, Constants.TRIGGER_OBJ_ID, Constants.ID, Constants.TRIGGER);
    collection = Constants.TRIGGERED_BENEFICIARY;
    String prefix = String.format("%s.", Constants.TRIGGER);
    triggerCriteria = getTriggerCriteriaFromRequest(request, prefix);
    triggerBenefCriteria = getTriggerBenefCriteriaFromRequest(request, "");
    match1 = Aggregation.match(triggerBenefCriteria);
    match2 = Aggregation.match(triggerCriteria);
    AggregationOperation replaceRoot = Aggregation.replaceRoot(("$_id"));
    AggregationOperation projectUnset =
        Aggregation.project()
            .andExclude(Constants.ID)
            .andExclude(Constants.ACTION_A_REALISER)
            .andExclude(Constants.AMC)
            .andExclude(Constants.COLLECTIVITE)
            .andExclude(Constants.COLLEGE)
            .andExclude(Constants.CRITERE_SECONDAIRE_DETAILLE)
            .andExclude(Constants.DATE_NAISSANCE)
            .andExclude(Constants.ID_TRIGGER)
            .andExclude(Constants.IS_CONTRAT_INDIVIDUEL)
            .andExclude(Constants.LAST_ACTION)
            .andExclude(Constants.NIR)
            .andExclude(Constants.NUMERO_CONTRAT)
            .andExclude(Constants.OFFRE)
            .andExclude(Constants.PARAMETRE_ACTION)
            .andExclude(Constants.PRODUIT);
    AggregationOperation unwind = Aggregation.unwind("$trigger");
    AggregationOperation group =
        Aggregation.group(
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.ID),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.AMC),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.DATE_CREATION),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.DATE_DEBUT_TRAITEMENT),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.DATE_EFFET),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.DATE_FIN_STANDBY),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.DATE_FIN_TRAITEMENT),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.DATE_MODIFICATION),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.NB_BENEF),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.NB_BENEF_KO),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.NB_BENEF_WARNING),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.ORIGINE),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.PARAMETRAGE_CARTE_TP_ID),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.PERIODES),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.STATUS),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.STRING_ID),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.USER_CREATION),
            String.format(Constants.DS_S, Constants.TRIGGER, Constants.USER_MODIFICATION));
    agg =
        Aggregation.newAggregation(
                match1,
                addFieldsOperation,
                lookup,
                match2,
                projectUnset,
                unwind,
                group,
                replaceRoot,
                sort,
                facet,
                unwindPaging)
            .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());

    logger.debug("Aggregation: {}", agg);

    AggregationResults<TriggerResponse> aggResult =
        template.aggregate(agg, collection, TriggerResponse.class);
    TriggerResponse result = aggResult.getUniqueMappedResult();
    if (result != null) {
      result
          .getPaging()
          .setTotalPages(
              (int) Math.ceil((double) result.getPaging().getTotalElements() / (double) perPage));
    } else {
      // Si aucune donnée on retourne tout de même une pagination
      result = new TriggerResponse();
      result.setPaging(new PagingResponseModel(1, 0, 0, perPage));
    }

    return result;
  }

  private Criteria getTriggerBenefCriteriaFromRequest(TriggerRequest request, String prefix) {
    Criteria criteria = new Criteria();

    if (CollectionUtils.isNotEmpty(request.getAmcs())) {
      criteria.and(prefix + Constants.AMC).in(request.getAmcs());
    }

    if (StringUtils.isNotBlank(request.getNir())) {
      criteria.and(prefix + Constants.NIR).is(request.getNir());
    }

    if (StringUtils.isNotBlank(request.getNumeroContrat())) {
      criteria
          .and(
              prefix
                  + (Boolean.TRUE.equals(request.getIsContratIndividuel())
                      ? Constants.NUMERO_CONTRAT
                      : Constants.NUMERO_CONTRAT_COLLECTIVE))
          .is(request.getNumeroContrat());
    }

    return criteria;
  }

  private Criteria getTriggerCriteriaFromRequest(TriggerRequest request, String prefix) {
    Criteria criteria = new Criteria();
    if (CollectionUtils.isNotEmpty(request.getEmitters())) {
      criteria.and(prefix + Constants.ORIGINE).in(request.getEmitters());
    } else {
      criteria.and(prefix + Constants.ORIGINE).in((Object[]) TriggerEmitter.values());
    }
    if (CollectionUtils.isNotEmpty(request.getStatus())) {
      criteria.and(prefix + Constants.STATUS).in(request.getStatus());
    } else {
      criteria.and(prefix + Constants.STATUS).in((Object[]) TriggerStatus.values());
    }
    if (request.getDateDebut() != null) {
      LocalDateTime startDate = getDateFromString(request.getDateDebut());
      if (request.getDateFin() != null) {
        LocalDateTime endDate = getDateFromString(request.getDateFin());
        criteria.and(prefix + Constants.DATE_DEBUT_TRAITEMENT).gte(startDate).lte(endDate);
      } else {
        criteria.and(prefix + Constants.DATE_DEBUT_TRAITEMENT).gte(startDate);
      }
    } else {
      if (request.getDateFin() != null) {
        LocalDateTime endDate = getDateFromString(request.getDateFin());
        criteria.and(prefix + Constants.DATE_DEBUT_TRAITEMENT).lte(endDate);
      }
    }
    if (CollectionUtils.isNotEmpty(request.getAmcs())) {
      criteria.and(prefix + Constants.AMC).in(request.getAmcs());
    }

    if (StringUtils.isNotBlank(request.getOwner())) {
      criteria.and(prefix + Constants.USER_CREATION).is(request.getOwner());
    }

    return criteria;
  }

  @Override
  @ContinueSpan(log = "saveTrigger")
  public Trigger saveTrigger(Trigger trigger) {
    if (trigger.getId() == null) {
      if (trigger.getStatus() == null) {
        trigger.setStatus(TriggerStatus.ToProcess);
      }
      trigger.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));

      String userCreation = authenticationFacade.getAuthenticationUserName();
      if (Constants.UNIDENTIFIED.equals(userCreation)
          && TriggerEmitter.Event.equals(trigger.getOrigine())) {
        userCreation = "EVENT";
      }
      trigger.setUserCreation(userCreation);
    }
    return template.save(trigger, Constants.TRIGGER);
  }

  @Override
  @ContinueSpan(log = "saveTriggeredBeneficiary")
  public TriggeredBeneficiary saveTriggeredBeneficiary(TriggeredBeneficiary triggerBenef) {
    return template.save(triggerBenef, Constants.TRIGGERED_BENEFICIARY);
  }

  @Override
  @ContinueSpan(log = "removeAll triggers and triggeredBenef")
  public void removeAll() {
    template.findAllAndRemove(new Query(), Constants.TRIGGER);
    template.findAllAndRemove(new Query(), Constants.TRIGGERED_BENEFICIARY);
  }

  @Override
  @ContinueSpan(log = "getTriggeredBeneficiaries")
  public List<TriggeredBeneficiary> getTriggeredBeneficiaries(String idTrigger) {
    Query query =
        new Query(new Criteria().and(Constants.ID_TRIGGER).is(idTrigger))
            .with(Sort.by(Sort.Direction.ASC, Constants.SERVICE_PRESTATION_ID));
    return template.find(query, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);
  }

  @Override
  @ContinueSpan(log = "getTriggeredBeneficiarieIdsWithStatus")
  public List<String> getTriggeredBeneficiarieIdsWithStatus(
      String idTrigger, TriggeredBeneficiaryStatusEnum triggeredBeneficiaryStatusEnum) {
    Criteria criteriaStatut =
        new Criteria().and(Constants.STATUT).is(triggeredBeneficiaryStatusEnum);
    Query query =
        new Query(
                new Criteria().and(Constants.ID_TRIGGER).is(idTrigger).andOperator(criteriaStatut))
            .with(Sort.by(Sort.Direction.ASC, Constants.SERVICE_PRESTATION_ID));
    query.fields().include("_id");
    return template.find(query, TriggeredBeneficiary.class).stream()
        .map(TriggeredBeneficiary::getId)
        .toList();
  }

  @Override
  @ContinueSpan(log = "getTriggeredBeneficiariesStream")
  public Iterator<TriggeredBeneficiary> getTriggeredBeneficiariesStream(String idTrigger) {
    Query query =
        new Query(new Criteria().and(Constants.ID_TRIGGER).is(idTrigger))
            .with(Sort.by(Sort.Direction.ASC, Constants.SERVICE_PRESTATION_ID));
    return template.stream(query, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY)
        .iterator();
  }

  @Override
  @ContinueSpan(log = "getNbTriggeredBeneficiaries")
  public long getNbTriggeredBeneficiaries(String idTrigger) {
    Query q = new Query(new Criteria().and(Constants.ID_TRIGGER).is(idTrigger));
    return template.count(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);
  }

  @Override
  @ContinueSpan(log = "getNbTriggeredBeneficiariesWithStatus")
  public long getNbTriggeredBeneficiariesWithStatus(
      String idTrigger, TriggeredBeneficiaryStatusEnum triggeredBeneficiaryStatusEnum) {
    Criteria criteriaStatut =
        new Criteria().and(Constants.STATUT).is(triggeredBeneficiaryStatusEnum);
    Query q =
        new Query(
            new Criteria().and(Constants.ID_TRIGGER).is(idTrigger).andOperator(criteriaStatut));
    return template.count(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);
  }

  @Override
  @ContinueSpan(log = "getTriggeredBeneficiariesByServicePrestation")
  public List<TriggeredBeneficiary> getTriggeredBeneficiariesByServicePrestation(
      String idServicePrestation) {
    Query q =
        new Query()
            .addCriteria(Criteria.where(Constants.SERVICE_PRESTATION_ID).is(idServicePrestation));
    return template.find(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);
  }

  @Override
  @ContinueSpan(log = "getTriggeredBeneficiariesByServicePrestation")
  public TriggeredBeneficiary getLastTriggeredBeneficiariesByServicePrestation(
      String idServicePrestation, String idTriggerBenef) {
    Query q =
        new Query()
            .addCriteria(Criteria.where(Constants.SERVICE_PRESTATION_ID).is(idServicePrestation));
    q.addCriteria(Criteria.where(Constants.ID).ne(idTriggerBenef)).limit(1);
    q.with(Sort.by(Constants.ID_TRIGGER).descending());
    List<TriggeredBeneficiary> triggeredBeneficiaryList =
        template.find(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);
    if (CollectionUtils.isNotEmpty(triggeredBeneficiaryList)) {
      return triggeredBeneficiaryList.getFirst();
    }
    return null;
  }

  @Override
  @ContinueSpan(log = "getTriggeredBeneficiariesWithError")
  public TriggeredBeneficiaryResponse getTriggeredBeneficiariesWithError(
      int perPage, int page, String idTrigger, String motifAnomalieSortDirection) {
    TriggeredBeneficiaryResponse response = new TriggeredBeneficiaryResponse();
    Criteria criteriaStatutError =
        new Criteria().and(Constants.STATUT).is(TriggeredBeneficiaryStatusEnum.Error);
    Criteria criteriaStatutWarning =
        new Criteria().and(Constants.STATUT).is(TriggeredBeneficiaryStatusEnum.Warning);

    Query q =
        new Query(
            new Criteria()
                .and(Constants.ID_TRIGGER)
                .is(idTrigger)
                .orOperator(criteriaStatutError, criteriaStatutWarning));
    String sortColumn = Constants.DERNIERE_ANOMALIE;
    Direction sortDirection = Sort.Direction.ASC;
    if (StringUtils.isNotBlank(motifAnomalieSortDirection)
        && motifAnomalieSortDirection.equalsIgnoreCase("DESC")) {
      sortDirection = Sort.Direction.DESC;
    }
    final Pageable pageableRequest = PageRequest.of(page - 1, perPage);
    long totalElements =
        template.count(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);

    q.with(Sort.by(sortDirection, sortColumn));
    q.with(pageableRequest);
    List<TriggeredBeneficiary> triggeredBeneficiaries =
        template.find(q, TriggeredBeneficiary.class, Constants.TRIGGERED_BENEFICIARY);
    response.setTriggeredBeneficiaries(triggeredBeneficiaries);

    // Pagination
    PagingResponseModel paging = getPagingResponseModel(perPage, page, totalElements);
    response.setPaging(paging);

    return response;
  }

  // this is used in renouvellement and is parallellization friendly
  @Override
  @ContinueSpan(log = "updateOnlyStatus trigger")
  public void updateOnlyStatus(String id, TriggerStatus statut, String source) {
    Criteria q = Criteria.where(Constants.ID).is(id);
    Update u =
        new Update()
            .set("status", statut)
            .set("dateModification", LocalDateTime.now(ZoneOffset.UTC))
            .set("userModification", source);
    template.updateFirst(new Query(q), u, Trigger.class);
  }

  @ContinueSpan(log = "getDateFromString")
  public LocalDateTime getDateFromString(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    return LocalDate.parse(date, formatter).atStartOfDay();
  }

  @Override
  @ContinueSpan(log = "updateTriggeredBeneficiaries")
  public void updateTriggeredBeneficiaries(List<TriggeredBeneficiary> benefs) {
    if (!CollectionUtils.isEmpty(benefs)) {
      benefs.forEach(b -> template.save(b, Constants.TRIGGERED_BENEFICIARY));
    }
  }

  @Override
  @ContinueSpan(log = "manageBenefCounter")
  public int manageBenefCounter(
      String triggerId, int nbBenefError, int nbBenefWarning, int nbBenefToProcess) {
    Query q = new Query(new Criteria().and(Constants.ID).is(triggerId));
    Update update =
        new Update()
            .inc(Constants.NB_BENEF_KO, nbBenefError)
            .inc(Constants.NB_BENEF_WARNING, nbBenefWarning)
            .inc(Constants.NB_BENEF_TO_PROCESS, nbBenefToProcess);
    Trigger trigger = template.findAndModify(q, update, Trigger.class);
    int nbBenef = 0;
    if (trigger != null) {
      nbBenef = trigger.getNbBenefToProcess();
    }
    return nbBenef + nbBenefToProcess;
  }

  @Override
  @ContinueSpan(log = "isTriggerByFilenameNotProcessed")
  public boolean isTriggerByFilenameNotProcessed(String nomFichier, String firstTriggerId) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where(Constants.NOM_FICHIER_ORIGINE)
            .is(nomFichier)
            .and(Constants.STATUS)
            .in(TriggerStatus.Processing, TriggerStatus.ToProcess));
    if (firstTriggerId != null) {
      criteria = criteria.and(Constants.ID).gte(new ObjectId(firstTriggerId));
    }
    return template.exists(new Query(criteria), Trigger.class);
  }

  @Override
  @ContinueSpan(log = "getNombreDeclarationForTriggerByFilename")
  public int[] getNombreDeclarationForTriggerByFilename(String nomFichier) {
    int[] res = {0, 0};
    Criteria criteria = new Criteria();
    criteria.andOperator(Criteria.where(Constants.NOM_FICHIER_ORIGINE).is(nomFichier));
    List<Trigger> listTriggers = template.find(new Query(criteria), Trigger.class);
    if (CollectionUtils.isNotEmpty(listTriggers)) {
      for (Trigger t : listTriggers) {
        int[] sub = getNombreDeclarationForTriggerBenefByTriggerId(t.getId());
        res[0] += sub[0];
        res[1] += sub[1];
      }
    }
    return res;
  }

  private int[] getNombreDeclarationForTriggerBenefByTriggerId(String idTrigger) {
    int[] res = {0, 0};
    Criteria criteria = new Criteria();

    AggregationOperation match =
        Aggregation.match(criteria.andOperator(Criteria.where(Constants.ID_TRIGGER).is(idTrigger)));

    AggregationOperation group =
        Aggregation.group()
            .sum("nbDeclarationsOuverture")
            .as("nbDeclarationsOuverture")
            .sum("nbDeclarationsFermeture")
            .as("nbDeclarationsFermeture");
    Aggregation agg =
        Aggregation.newAggregation(match, group)
            .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());

    DecompteDeclarations decompte =
        template
            .aggregate(agg, Constants.TRIGGERED_BENEFICIARY, DecompteDeclarations.class)
            .getUniqueMappedResult();
    if (decompte != null) {
      res[0] = decompte.getNbDeclarationsOuverture();
      res[1] = decompte.getNbDeclarationsFermeture();
    }
    return res;
  }

  @Override
  @ContinueSpan(log = "deleteTriggerByAmc")
  public long deleteTriggerByAmc(String idDeclarant) {
    Criteria criteria = Criteria.where("amc").is(idDeclarant);

    return template.remove(new Query(criteria), Trigger.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteTriggeredBeneficiaryByAmc")
  public long deleteTriggeredBeneficiaryByAmc(String idDeclarant) {
    Criteria criteria = Criteria.where("amc").is(idDeclarant);

    return template.remove(new Query(criteria), TriggeredBeneficiary.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteTriggerById")
  public long deleteTriggerById(String id) {
    Criteria criteria = Criteria.where(Constants.ID).is(id);

    return template.remove(new Query(criteria), Trigger.class).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteTriggeredBeneficiaryById")
  public long deleteTriggeredBeneficiaryById(String id) {
    Criteria criteria = Criteria.where(Constants.ID).is(id);

    return template.remove(new Query(criteria), TriggeredBeneficiary.class).getDeletedCount();
  }

  @Override
  public Iterator<Trigger> getIDsTriggerRenewNotArchived() {
    Criteria criteria =
        Criteria.where(Constants.ORIGINE)
            .is(TriggerEmitter.Renewal)
            .and(Constants.EXPORTED)
            .ne(true);
    return template.stream(Query.query(criteria), Trigger.class).iterator();
  }

  @Override
  public void setExported(String id, boolean exported) {
    Criteria criteria = Criteria.where(Constants.ID).is(id);
    Update update = Update.update(Constants.EXPORTED, exported);
    template.updateFirst(Query.query(criteria), update, Trigger.class);
  }

  @Override
  public int getNombreServicePrestationByTriggerId(String idTrigger, boolean recyclage) {
    Criteria criteria = new Criteria();

    AggregationOperation group = Aggregation.group("servicePrestationId");
    Aggregation agg;
    if (recyclage) {
      criteria.andOperator(
          Criteria.where(Constants.ID_TRIGGER).is(idTrigger),
          Criteria.where(Constants.STATUT).is(TriggeredBeneficiaryStatusEnum.Error.toString()));
      AggregationOperation match = Aggregation.match(criteria);
      agg =
          Aggregation.newAggregation(match, group)
              .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
    } else {
      AggregationOperation match =
          Aggregation.match(
              criteria.andOperator(Criteria.where(Constants.ID_TRIGGER).is(idTrigger)));
      agg =
          Aggregation.newAggregation(match, group)
              .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
    }
    List<TriggeredBeneficiary> triggeredBeneficiary =
        template
            .aggregate(agg, Constants.TRIGGERED_BENEFICIARY, TriggeredBeneficiary.class)
            .getMappedResults();

    return triggeredBeneficiary.size();
  }
}
