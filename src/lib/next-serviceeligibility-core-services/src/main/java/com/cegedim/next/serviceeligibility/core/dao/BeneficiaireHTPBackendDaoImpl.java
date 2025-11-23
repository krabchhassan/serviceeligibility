package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;
import static org.springframework.data.mongodb.core.query.SerializationUtils.serializeToJsonSafely;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service("beneficiaireHTPBackendDao")
public class BeneficiaireHTPBackendDaoImpl extends BeneficiaireBackendDaoImpl {

  private final Logger logger = LoggerFactory.getLogger(BeneficiaireHTPBackendDaoImpl.class);

  public BeneficiaireHTPBackendDaoImpl(
      AuthorizationScopeHandler authorizationScopeHandler, MongoTemplate mongoTemplate) {
    super(authorizationScopeHandler, mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findBenefFromRequest")
  public List<BenefAIV5> findBenefFromRequest(final UniqueAccessPointRequestV5 requete) {
    final Aggregation aggregation;
    if (StringUtils.isNotBlank(requete.getBeneficiaryId())) {
      aggregation = this.createSearchQueryForBenefById(requete.getBeneficiaryId());
    } else {
      aggregation = this.getAggregationV5(requete);
    }
    final AggregationResults<BenefAIV5> results =
        this.getMongoTemplate()
            .aggregate(aggregation, Constants.BENEFICIAIRE_COLLECTION_NAME, BenefAIV5.class);
    return results.getMappedResults();
  }

  private Aggregation getAggregationV5(final UniqueAccessPointRequestV5 requete) {
    final Aggregation aggregation =
        this.createSearchQueryForBenef(
            requete.getNirCode(),
            requete.getBirthDate(),
            requete.getBirthRank(),
            requete.getStartDate(),
            requete.getEndDate(),
            requete.getInsurerId(),
            requete.getSubscriberId(),
            requete.getIssuingCompanyCode(),
            requete.getIsForced(),
            requete.getClientType());
    if (logger.isDebugEnabled()) {
      logger.debug(
          "PAU V5, recherche du bénéficiaire pour HTP. Execution de l'aggregation => {}",
          serializeToJsonSafely(aggregation));
    }
    return aggregation;
  }

  @Override
  protected Criteria createCriteriaBenef(
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
    final Criteria criteria = new Criteria();

    applyIssuingCompanyFilter(
        issuingCompanyCode, criteria, CONTRATS_SOCIETE_EMETTRICE, CLIENT_TYPE_INSURER);
    applyIssuingCompanyFilter(insurerId.trim(), criteria, AMC_ID_DECLARANT, CLIENT_TYPE_OTP);
    applyIssuingCompanyFilter(
        insurerId.trim(), criteria, AMC_ID_DECLARANT, CLIENT_TYPE_INSURER, false);

    if (StringUtils.isNotBlank(birthDate) || StringUtils.isNotBlank(birthRank)) {
      if (StringUtils.isNotBlank(birthDate)) {
        criteria.and("identite.historiqueDateRangNaissance.dateNaissance").is(birthDate.trim());
      }
      if (StringUtils.isNotBlank(birthRank)) {
        criteria.and("identite.historiqueDateRangNaissance.rangNaissance").is(birthRank.trim());
      }
    }

    if (StringUtils.isNotBlank(nirCode)) {
      final Criteria nirOdCriteria = this.nirOdCriteria(startDate, endDate, nirCode, isForced);
      final Criteria nirBeneficiaireCriteria = Criteria.where(ID_NIR).is(nirCode.trim());
      criteria.orOperator(nirBeneficiaireCriteria, nirOdCriteria);
    }

    if (StringUtils.isNotBlank(subscriberId)) {
      criteria.and("contrats.numeroAdherent").is(subscriberId);
    }

    return criteria;
  }

  @ContinueSpan(log = "findBenefitRecipients")
  public BenefAIV5 findBenefitRecipients(
      String idPerson, String subscriberId, String contractNumber, String insurerId) {
    Criteria criteria = createCriteriaBenef(idPerson, subscriberId, contractNumber, insurerId);
    Aggregation aggregation = getAggregationFromCriteria(criteria);

    AggregationResults<BenefAIV5> results =
        getMongoTemplate()
            .aggregate(aggregation, Constants.BENEFICIAIRE_COLLECTION_NAME, BenefAIV5.class);
    return results.getUniqueMappedResult();
  }

  private Criteria createCriteriaBenef(
      String idPerson, String subscriberId, String contractNumber, String insurerId) {
    Criteria criteria = new Criteria();

    criteria.and(KEY).is(idPerson.trim());
    criteria.and("contrats.numeroContrat").is(contractNumber.trim());
    criteria.and(AMC_ID_DECLARANT).is(insurerId.trim());
    if (StringUtils.isNotBlank(subscriberId)) {
      criteria.and("contrats.numeroAdherent").is(subscriberId.trim());
    }

    return criteria;
  }

  public BenefAIV5 findBeneficiaryDataName(String personNumber, String contractNumber) {
    Criteria criteria = new Criteria();

    criteria.and(KEY).is(personNumber.trim());
    criteria.and("contrats.numeroContrat").is(contractNumber.trim());
    Aggregation aggregation = getAggregationFromCriteria(criteria);

    AggregationResults<BenefAIV5> results =
        getMongoTemplate()
            .aggregate(aggregation, Constants.BENEFICIAIRE_COLLECTION_NAME, BenefAIV5.class);
    return results.getUniqueMappedResult();
  }
}
