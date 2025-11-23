package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTPRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.PagingResponseModel;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametrageCarteTPResponse;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ParametrageCarteTPNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("parametrageCarteTPDaoImpl")
@RequiredArgsConstructor
public class ParametrageCarteTPDaoImpl implements ParametrageCarteTPDao {
  private final Logger logger = LoggerFactory.getLogger(ParametrageCarteTPDaoImpl.class);

  private final MongoTemplate template;

  @Qualifier("bddAuth")
  private final AuthenticationFacade authenticationFacade;

  @Override
  @ContinueSpan(log = "getByParams")
  public ParametrageCarteTPResponse getByParams(
      int perPage, int page, String sortBy, String direction, ParametrageCarteTPRequest request) {
    final Pageable pageableRequest = PageRequest.of(page - 1, perPage);
    Query queryParametrageCarteTP = new Query();
    ParametrageCarteTPResponse response = new ParametrageCarteTPResponse();

    // Recherche des services de paramétrage pour une liste d'AMCs
    if (CollectionUtils.isNotEmpty(request.getAmcs())) {
      queryParametrageCarteTP.addCriteria(Criteria.where(Constants.AMC).in(request.getAmcs()));
    }
    if (CollectionUtils.isNotEmpty(request.getTriggerMode())) {
      queryParametrageCarteTP.addCriteria(
          Criteria.where(Constants.PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT)
              .in(request.getTriggerMode()));
    }
    if (!request.isStatus()) {
      queryParametrageCarteTP.addCriteria(Criteria.where(Constants.STATUT).in("Actif"));
    }
    if (CollectionUtils.isNotEmpty(request.getLots())) {
      queryParametrageCarteTP.addCriteria(Criteria.where(Constants.ID_LOT).in(request.getLots()));
    }
    if (CollectionUtils.isNotEmpty(request.getGts())) {
      List<Criteria> gtCriterias = new ArrayList<>();
      for (String gt : request.getGts()) {
        String[] params = gt.split("#");
        if (params.length < 2) {
          continue;
        }
        gtCriterias.add(
            Criteria.where(Constants.GARANTIE_TECHNIQUES)
                .elemMatch(
                    Criteria.where(Constants.CODE_ASSUREUR)
                        .is(params[0])
                        .and(Constants.CODE_GARANTIE)
                        .is(params[1])));
      }
      queryParametrageCarteTP.addCriteria(
          new Criteria().orOperator(gtCriterias.toArray(new Criteria[0])));
    }
    String sortColumn;
    Direction sortDirection;
    if (StringUtils.isNotBlank(sortBy)) {
      sortColumn = sortBy;
      if (direction.equalsIgnoreCase("DESC")) {
        sortDirection = Sort.Direction.DESC;
      } else {
        sortDirection = Sort.Direction.ASC;
      }
    } else {
      sortColumn = "dateCreation";
      sortDirection = Sort.Direction.DESC;
    }
    queryParametrageCarteTP.with(Sort.by(sortDirection, sortColumn));
    List<ParametrageCarteTP> totalElementsParametrageCarteTP =
        template.find(
            queryParametrageCarteTP,
            ParametrageCarteTP.class,
            Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
    int totalElements = totalElementsParametrageCarteTP.size();
    queryParametrageCarteTP.with(pageableRequest);
    List<ParametrageCarteTP> parametragesCarteTP =
        template.find(
            queryParametrageCarteTP,
            ParametrageCarteTP.class,
            Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
    response.setParametragesCarteTP(parametragesCarteTP);
    // Pagination
    PagingResponseModel paging = new PagingResponseModel();
    paging.setPage(page);
    paging.setPerPage(perPage);
    paging.setTotalElements(totalElements);
    paging.setTotalPages((int) Math.ceil((double) totalElements / (double) perPage));
    response.setPaging(paging);
    return response;
  }

  @Override
  @ContinueSpan(log = "getServicePrestationByContratIndividuelAndNumAdherent")
  public ContratAIV6 getServicePrestationByContratIndividuelAndNumAdherent(
      String amc, String numeroContratIndividuel, String numeroAdherent) {
    // Recherche du contrat servicePrestation
    Query queryServicePrestation = new Query();
    queryServicePrestation.addCriteria(
        Criteria.where(Constants.ID_DECLARANT)
            .is(amc)
            .and(Constants.NUMERO_CONTRAT_INDIVIDUEL)
            .is(numeroContratIndividuel)
            .and(Constants.NUMERO_ADHERENT)
            .is(numeroAdherent));
    return template.findOne(
        queryServicePrestation, ContratAIV6.class, Constants.SERVICE_PRESTATION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getByAmc parametrageCarteTP")
  public List<ParametrageCarteTP> getByAmc(RequestParametrageCarteTP requestParametrageCarteTP) {
    Criteria criteria = Criteria.where(Constants.AMC).is(requestParametrageCarteTP.getAmc());
    if (requestParametrageCarteTP.isOnlyActif()) {
      criteria = criteria.and(Constants.STATUT).is(ParametrageCarteTPStatut.Actif.toString());
    }
    if (requestParametrageCarteTP.isOnlyActif()) {
      criteria =
          criteria.and(Constants.DATE_DEBUT_VALIDITE).lte(LocalDate.now(ZoneOffset.UTC).toString());
    }
    List<Criteria> subCriteria = new ArrayList<>();
    if (requestParametrageCarteTP.isNotManual()) {
      subCriteria.add(
          Criteria.where(Constants.PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT)
              .ne(ModeDeclenchementCarteTP.Manuel));
    }
    if (requestParametrageCarteTP.isNotPilotageBO()) {
      subCriteria.add(
          Criteria.where(Constants.PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT)
              .ne(ModeDeclenchementCarteTP.PilotageBO));
    }
    Query query = new Query();
    query.addCriteria(criteria);
    if (CollectionUtils.isNotEmpty(subCriteria)) {
      criteria.andOperator(subCriteria);
    }
    return template.find(
        query, ParametrageCarteTP.class, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getById parametrageCarteTP")
  @Cacheable(value = "parametreCarteTPCache", key = "#id")
  public ParametrageCarteTP getById(String id) {
    return template.findById(
        id, ParametrageCarteTP.class, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "create parametrageCarteTP")
  public void create(ParametrageCarteTP parametrageCarteTP) {
    // Par défaut le paramétrage est créé en actif
    if (parametrageCarteTP.getStatut() == null) {
      parametrageCarteTP.setStatut(ParametrageCarteTPStatut.Actif);
    }
    parametrageCarteTP.setDateCreation(LocalDateTime.now(ZoneOffset.UTC));
    parametrageCarteTP.setUserCreation(authenticationFacade.getAuthenticationUserName());
    template.save(parametrageCarteTP, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "deleteAll parametrageCarteTP")
  public void deleteAll() {
    template.findAllAndRemove(
        new Query(), ParametrageCarteTP.class, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "updateStatus parametrageCarteTP")
  public void updateStatus(String id, ParametrageCarteTPStatut statut) {
    ParametrageCarteTP param =
        template.findById(id, ParametrageCarteTP.class, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
    if (param == null) {
      String message =
          String.format(
              "Aucun servicePrestation trouvé avec les informations suivantes : id=%s", id);
      logger.debug(message);
      throw new ParametrageCarteTPNotFoundException(message);
    }
    param.setStatut(statut);
    template.save(param, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "getParametrageToExecute")
  public List<ParametrageCarteTP> getParametrageToExecute(String date, boolean isRdo) {
    List<ParametrageCarteTP> params = new ArrayList<>();

    // En mode RDO, on ne prend en compte que le paramétrage manuel
    if (!isRdo) {
      // Paramétrage à date anniversaire contrat
      params.addAll(getParametragesAnniversaire(date, false));

      // Paramétrage arrivé à échéance
      params.addAll(getParametragesEcheance(date));
    } else {
      // en RDO ajout des parametrages manuels à anniversaire contrat
      params.addAll(getParametragesAnniversaire(date, true));
    }

    // Paramétrage à date de déclenchement manuel non anniversaire
    params.addAll(getParametragesDeclenchementManuel(date));

    // Tri des paramétrages par priorité
    return params.stream()
        .sorted(Comparator.comparingInt(ParametrageCarteTP::getPriorite))
        .distinct()
        .toList();
  }

  private List<ParametrageCarteTP> getParametragesAnniversaire(String date, boolean onlyManual) {
    Query queryAnniversaire = new Query();
    Criteria criteriaDefinition =
        Criteria.where(Constants.STATUT)
            .is(ParametrageCarteTPStatut.Actif.toString())
            .and(
                String.format(
                    Constants.S_S,
                    Constants.PARAMETRAGE_RENOUVELLEMENT,
                    Constants.DATE_RENOUVELLEMENT_CARTE_TP))
            .is(DateRenouvellementCarteTP.AnniversaireContrat.toString());
    if (onlyManual) {
      criteriaDefinition
          .and(Constants.PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT)
          .is(ModeDeclenchementCarteTP.Manuel.toString());
    }
    queryAnniversaire.addCriteria(criteriaDefinition);
    List<ParametrageCarteTP> paramAnniversaire =
        template.find(
            queryAnniversaire, ParametrageCarteTP.class, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);

    paramAnniversaire =
        paramAnniversaire.stream()
            .filter(
                p ->
                    !LocalDate.parse(p.getDateDebutValidite(), DateUtils.FORMATTER)
                        .isAfter(LocalDate.parse(date, DateUtils.FORMATTER)))
            .toList();

    return paramAnniversaire;
  }

  private List<ParametrageCarteTP> getParametragesEcheance(String date) {
    AggregationOperation match1 =
        Aggregation.match(
            new Criteria()
                .and(Constants.STATUT)
                .is(ParametrageCarteTPStatut.Actif.toString())
                .and(
                    String.format(
                        Constants.S_S,
                        Constants.PARAMETRAGE_RENOUVELLEMENT,
                        Constants.DATE_RENOUVELLEMENT_CARTE_TP))
                .is(DateRenouvellementCarteTP.DebutEcheance.toString()));

    String currentYear = Integer.toString(LocalDate.parse(date, DateUtils.FORMATTER).getYear());

    AddFieldsOperation addFieldDebutEcheanceCourant =
        Aggregation.addFields()
            .addFieldWithValue(
                Constants.DEBUT_ECHEANCE_COURANT,
                StringOperators.Concat.valueOf(
                        String.format(
                            Constants.DOLLAR_S_S,
                            Constants.PARAMETRAGE_RENOUVELLEMENT,
                            Constants.DEBUT_ECHEANCE))
                    .concat("/")
                    .concat(currentYear))
            .build();
    AddFieldsOperation addFieldDebutEcheanceCourantDate =
        Aggregation.addFields()
            .addFieldWithValue(
                Constants.DEBUT_ECHEANCE_COURANT_DATE,
                DateOperators.dateFromString(
                        String.format(Constants.DOLLAR_S, Constants.DEBUT_ECHEANCE_COURANT))
                    .withFormat("%d/%m/%Y"))
            .addFieldWithValue(
                Constants.MILLISECOND_TO_SUBSTRACT,
                ArithmeticOperators.valueOf(
                        String.format(
                            Constants.DOLLAR_S_S,
                            Constants.PARAMETRAGE_RENOUVELLEMENT,
                            Constants.DELAI_DECLENCHEMENT_CARTE_TP))
                    .multiplyBy(Constants.MILLISECONDS_FOR_ONE_DAY))
            .build();
    AddFieldsOperation addFieldDebutEcheanceMatch =
        Aggregation.addFields()
            .addFieldWithValue(
                Constants.DEBUT_ECHEANCE_MATCH,
                ArithmeticOperators.valueOf(Constants.DEBUT_ECHEANCE_COURANT_DATE)
                    .subtract(Constants.MILLISECOND_TO_SUBSTRACT))
            .build();
    AddFieldsOperation addFieldDebutEcheanceMatchString =
        Aggregation.addFields()
            .addFieldWithValue(
                Constants.DEBUT_ECHEANCE_MATCH_STRING,
                DateOperators.DateToString.dateToString(
                        String.format(Constants.DOLLAR_S, Constants.DEBUT_ECHEANCE_MATCH))
                    .toString(Constants.FORMAT_DATE_MONGO))
            .build();

    LocalDate dateMinusOneYear = Util.stringToDate(date).minusYears(1);
    String dateMinusString = dateMinusOneYear.format(DateUtils.FORMATTER);
    AggregationOperation matchDate =
        Aggregation.match(
            new Criteria()
                .and(Constants.DEBUT_ECHEANCE_MATCH_STRING)
                .in(date, dateMinusString)
                .and(Constants.DATE_DEBUT_VALIDITE)
                .lte(date));
    Aggregation agg =
        Aggregation.newAggregation(
            match1,
            addFieldDebutEcheanceCourant,
            addFieldDebutEcheanceCourantDate,
            addFieldDebutEcheanceMatch,
            addFieldDebutEcheanceMatchString,
            matchDate);

    AggregationResults<ParametrageCarteTP> aggregationResults =
        template.aggregate(
            agg, Constants.PARAMETRAGE_CARTE_TP_COLLECTION, ParametrageCarteTP.class);

    return aggregationResults.getMappedResults();
  }

  private List<ParametrageCarteTP> getParametragesDeclenchementManuel(String date) {
    AggregationOperation matchActif =
        Aggregation.match(
            new Criteria().and(Constants.STATUT).is(ParametrageCarteTPStatut.Actif.toString()));
    AddFieldsOperation addFieldDateDeclenchementManuel =
        Aggregation.addFields()
            .addFieldWithValue(
                Constants.DATE_DECLENCHEMENT_MANUEL_COURANT,
                StringOperators.Substr.valueOf(
                        String.format(
                            Constants.DOLLAR_S_S,
                            Constants.PARAMETRAGE_RENOUVELLEMENT,
                            Constants.DATE_EXECUTION_BATCH))
                    .substring(0, 10))
            .build();
    AggregationOperation matchDateDeclenchement =
        Aggregation.match(new Criteria().and(Constants.DATE_DECLENCHEMENT_MANUEL_COURANT).is(date));

    AggregationOperation matchNotAnniversary =
        Aggregation.match(
            new Criteria()
                .and(
                    String.format(
                        Constants.S_S,
                        Constants.PARAMETRAGE_RENOUVELLEMENT,
                        Constants.DATE_RENOUVELLEMENT_CARTE_TP))
                .ne(DateRenouvellementCarteTP.AnniversaireContrat.toString()));
    Aggregation aggDeclenchementManuel =
        Aggregation.newAggregation(
            matchActif,
            addFieldDateDeclenchementManuel,
            matchDateDeclenchement,
            matchNotAnniversary);
    List<ParametrageCarteTP> paramDeclenchementManuel =
        template
            .aggregate(
                aggDeclenchementManuel,
                Constants.PARAMETRAGE_CARTE_TP_COLLECTION,
                ParametrageCarteTP.class)
            .getMappedResults();

    paramDeclenchementManuel =
        paramDeclenchementManuel.stream()
            .filter(
                p ->
                    !LocalDate.parse(p.getDateDebutValidite(), DateUtils.FORMATTER)
                        .isAfter(LocalDate.parse(date, DateUtils.FORMATTER)))
            .toList();

    return paramDeclenchementManuel;
  }

  @Override
  @ContinueSpan(log = "update parametrageCarteTP")
  public void update(ParametrageCarteTP param) {
    template.save(param);
  }

  @Override
  @ContinueSpan(log = "deleteByAmc parametrageCarteTP")
  public long deleteByAmc(String amc) {
    Criteria criteria = Criteria.where(Constants.AMC).is(amc);
    return template.remove(new Query(criteria), ParametrageCarteTP.class).getDeletedCount();
  }

  @ContinueSpan(log = "getPriorityByAmc")
  public List<Integer> getPriorityByAmc(String amc) {
    Criteria criteria =
        Criteria.where(Constants.AMC)
            .is(amc)
            .and(Constants.PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT)
            .ne(ModeDeclenchementCarteTP.Manuel);
    List<ParametrageCarteTP> parametrageCarteTPS =
        template.find(new Query(criteria), ParametrageCarteTP.class);
    return parametrageCarteTPS.stream().map(ParametrageCarteTP::getPriorite).toList();
  }

  @Override
  @ContinueSpan(log = "existParametrageCarteTPActif")
  public boolean existParametrageCarteTPActif() {
    Criteria criteria =
        Criteria.where(Constants.STATUT)
            .is(ParametrageCarteTPStatut.Actif)
            .and(Constants.PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT)
            .is(ModeDeclenchementCarteTP.Automatique);
    return template.count(new Query(criteria), Constants.PARAMETRAGE_CARTE_TP_COLLECTION) > 0;
  }

  @Override
  @ContinueSpan(log = "getIdLotsInParametrageCarteTPActif")
  public List<String> getIdLotsInParametrageCarteTPActif() {
    Query query = new Query(Criteria.where(Constants.STATUT).is(ParametrageCarteTPStatut.Actif));
    return template
        .getCollection(Constants.PARAMETRAGE_CARTE_TP_COLLECTION)
        .distinct(Constants.ID_LOTS, query.getQueryObject(), String.class)
        .into(new ArrayList<>());
  }

  @ContinueSpan(log = "findByGuaranteeCodeAndInsurerCode")
  public List<ParametrageCarteTP> findByGuaranteeCodeAndInsurerCode(
      String guaranteeCode, String insurerCode) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where(Constants.GARANTIE_TECHNIQUES)
            .elemMatch(
                Criteria.where(Constants.CODE_GARANTIE)
                    .is(guaranteeCode)
                    .and(Constants.CODE_ASSUREUR)
                    .is(insurerCode)));
    return template.find(query, ParametrageCarteTP.class);
  }
}
