package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.BeneficiaireConsultationHistory;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code BeneficiaireConsultationHistory} de la base de donnees. */
@Repository("beneficiaireConsultationHistoryDao")
public class BeneficiaireConsultationHistoryDaoImpl
    extends MongoGenericDao<BeneficiaireConsultationHistory>
    implements BeneficiaireConsultationHistoryDao {

  public BeneficiaireConsultationHistoryDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findBeneficiaireConsultationHistoriesByCriteria")
  public List<BeneficiaireConsultationHistory> findBeneficiaireConsultationHistoriesByCriteria(
      final String user, final boolean limit) {
    // BLANK
    if (StringUtils.isBlank(user)) {
      return new ArrayList<>();
    }

    final Criteria criteriaUser =
        Criteria.where("user")
            .regex(Pattern.compile(user.trim(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));

    // SORT
    final Query queryUser =
        new Query(criteriaUser).with(Sort.by(Sort.Direction.DESC, "dateConsultation"));

    if (limit) {
      queryUser.limit(10);
    }

    return this.getMongoTemplate().find(queryUser, BeneficiaireConsultationHistory.class);
  }

  @Override
  @ContinueSpan(log = "findBeneficiaireConsultationHistoriesByCriteria")
  public List<BeneficiaireConsultationHistory> findBeneficiaireConsultationHistoriesByCriteria(
      final String user) {
    return this.findBeneficiaireConsultationHistoriesByCriteria(user, false);
  }

  @Override
  @ContinueSpan(log = "deleteAllBeneficiaireConsultationHistory")
  public long deleteAllBeneficiaireConsultationHistory() {
    return getMongoTemplate()
        .findAllAndRemove(new Query(), BeneficiaireConsultationHistory.class)
        .size();
  }
}
