package com.cegedim.next.serviceeligibility.core.dao;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP;
import static org.springframework.data.mongodb.core.query.SerializationUtils.serializeToJsonSafely;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Slf4j
@Service("beneficiaireTPOnlineBackendDao")
public class BeneficiaireTPOnlineBackendDaoImpl extends BeneficiaireBackendDaoImpl {

  public BeneficiaireTPOnlineBackendDaoImpl(
      AuthorizationScopeHandler authorizationScopeHandler, MongoTemplate mongoTemplate) {
    super(authorizationScopeHandler, mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findBenefFromRequest V5")
  public List<BenefAIV5> findBenefFromRequest(final UniqueAccessPointRequestV5 requete) {
    final Aggregation aggregation;
    if (StringUtils.isNotBlank(requete.getBeneficiaryId())) {
      aggregation = this.createSearchQueryForBenefById(requete.getBeneficiaryId());
    } else {
      aggregation =
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
    }
    if (log.isDebugEnabled()) {
      log.debug(
          "PAU V5, recherche du bénéficiaire pour TP_ONLINE. Execution de l'aggregation => {}",
          serializeToJsonSafely(aggregation));
    }
    final AggregationResults<BenefAIV5> results =
        this.getMongoTemplate()
            .aggregate(aggregation, Constants.BENEFICIAIRE_COLLECTION_NAME, BenefAIV5.class);
    return results.getMappedResults();
  }

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
    var insurer = insurerId != null ? insurerId.trim() : null;

    List<Criteria> orCriterias = new ArrayList<>();
    if (StringUtils.isNotBlank(nirCode)) {
      final Criteria nirOdCriteria = this.nirOdCriteria(startDate, endDate, nirCode, isForced);
      final Criteria nirBeneficiaireCriteria = Criteria.where(ID_NIR).is(nirCode.trim());
      orCriterias.add(nirOdCriteria);
      orCriterias.add(nirBeneficiaireCriteria);
    }

    if (StringUtils.isNotBlank(subscriberId)) {
      orCriterias.add(Criteria.where("contrats.numeroAdherent").is(subscriberId));
    }
    final Criteria criteriaNirOrAdherent = new Criteria().orOperator(orCriterias);

    applyIssuingCompanyFilter(
        issuingCompanyCode, criteria, CONTRATS_SOCIETE_EMETTRICE, CLIENT_TYPE_INSURER);
    if (insurer != null) {
      final Criteria criteriaIdDeclarant = new Criteria();
      applyIssuingCompanyFilter(insurer, criteriaIdDeclarant, AMC_ID_DECLARANT, CLIENT_TYPE_OTP);
      applyIssuingCompanyFilter(
          insurer, criteriaIdDeclarant, AMC_ID_DECLARANT, CLIENT_TYPE_INSURER, false);

      final Criteria criteriaNumAmcEchange = new Criteria();
      applyIssuingCompanyFilter(
          insurer, criteriaNumAmcEchange, CONTRATS_NUM_AMC_ECHANGE, CLIENT_TYPE_OTP);
      applyIssuingCompanyFilter(
          insurer, criteriaNumAmcEchange, CONTRATS_NUM_AMC_ECHANGE, CLIENT_TYPE_INSURER);

      final Criteria criteriaAmc =
          new Criteria().orOperator(criteriaIdDeclarant, criteriaNumAmcEchange);
      criteria.andOperator(criteriaAmc, criteriaNirOrAdherent);
    } else {
      criteria.andOperator(criteriaNirOrAdherent);
    }
    if (StringUtils.isNotBlank(birthDate)) {
      criteria
          .and("identite.historiqueDateRangNaissance")
          .elemMatch(
              Criteria.where("dateNaissance")
                  .is(birthDate.trim())
                  .and("rangNaissance")
                  .is(birthRank.trim()));
    }

    return criteria;
  }
}
