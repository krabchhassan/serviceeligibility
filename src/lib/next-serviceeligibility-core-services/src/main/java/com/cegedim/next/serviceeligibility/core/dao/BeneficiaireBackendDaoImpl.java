package com.cegedim.next.serviceeligibility.core.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public abstract class BeneficiaireBackendDaoImpl
    extends MongoGenericWithManagementScopeDao<DocumentEntity> implements BeneficiaireBackendDao {

  public static final String KEY = "key";

  public static final String AUDIT_DATE_EMISSION = "audit.dateEmission";
  public static final String AMC_ID_DECLARANT = "amc.idDeclarant";

  public static final String ID_NIR = "identite.nir.code";
  public static final String IDENTITE_NIR_AFFILIATION_RO = "identite.affiliationsRO";
  public static final String PERIODE_FIN = "periode.fin";
  public static final String PERIODE_DEBUT = "periode.debut";
  public static final String NIR_CODE = "nir.code";
  public static final String CONTRATS_SOCIETE_EMETTRICE = "contrats.societeEmettrice";
  public static final String CONTRATS_NUM_AMC_ECHANGE = "contrats.numeroAMCEchange";

  public BeneficiaireBackendDaoImpl(
      AuthorizationScopeHandler authorizationScopeHandler, MongoTemplate mongoTemplate) {
    super(mongoTemplate, authorizationScopeHandler);
  }

  protected Aggregation createSearchQueryForBenefById(final String id) {
    final Criteria criteria = new Criteria();

    criteria.and(KEY).in(id);

    return this.getAggregationFromCriteria(criteria);
  }

  protected Aggregation getAggregationFromCriteria(final Criteria criteria) {
    ////////////
    // MATCH1 //
    ////////////
    final MatchOperation match = Aggregation.match(criteria);

    //////////
    // SORT //
    //////////
    final AggregationOperation sortOperation =
        Aggregation.sort(Sort.Direction.DESC, AUDIT_DATE_EMISSION);

    /////////////////
    // AGGREGATION //
    /////////////////
    final List<AggregationOperation> aggregationOperations = new ArrayList<>();

    aggregationOperations.add(match);
    aggregationOperations.add(sortOperation);

    return newAggregation(aggregationOperations)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }

  protected abstract Criteria createCriteriaBenef(
      String nirCode,
      String birthDate,
      String birthRank,
      String startDate,
      String endDate,
      String insurerId,
      String subscriberId,
      String issuingCompanyCode,
      Boolean isForced,
      String clientType);

  protected Aggregation createSearchQueryForBenef(
      final String nirCode,
      final String birthDate,
      final String birthRank,
      final String startDate,
      final String endDate,
      final String insurerId,
      final String subscriberId,
      final String issuingCompanyCode,
      final Boolean isForced,
      String clientType) {
    final Criteria criteria =
        this.createCriteriaBenef(
            nirCode,
            birthDate,
            birthRank,
            startDate,
            endDate,
            insurerId,
            subscriberId,
            issuingCompanyCode,
            isForced,
            clientType);

    return this.getAggregationFromCriteria(criteria);
  }

  protected Criteria nirOdCriteria(
      final String startDate, final String endDate, final String nirCode, final Boolean isForced) {
    Criteria criteriaDate;
    if (isForced) {
      return Criteria.where(IDENTITE_NIR_AFFILIATION_RO)
          .elemMatch(Criteria.where(NIR_CODE).is(nirCode));
    }
    if (endDate != null) {
      criteriaDate =
          Criteria.where(PERIODE_DEBUT)
              .lte(endDate)
              .orOperator(
                  Criteria.where(PERIODE_FIN).is(""),
                  Criteria.where(PERIODE_FIN).exists(false),
                  Criteria.where(PERIODE_FIN).is(null),
                  Criteria.where(PERIODE_FIN).gte(startDate));
    } else {
      criteriaDate =
          new Criteria()
              .orOperator(
                  Criteria.where(PERIODE_FIN).is(""),
                  Criteria.where(PERIODE_FIN).exists(false),
                  Criteria.where(PERIODE_FIN).is(null),
                  Criteria.where(PERIODE_FIN).gte(startDate));
    }

    return Criteria.where(IDENTITE_NIR_AFFILIATION_RO)
        .elemMatch(Criteria.where(NIR_CODE).is(nirCode).andOperator(criteriaDate));
  }
}
