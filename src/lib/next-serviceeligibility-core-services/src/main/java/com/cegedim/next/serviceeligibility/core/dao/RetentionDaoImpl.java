package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.result.UpdateResult;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository("retentionDaoImpl")
@RequiredArgsConstructor
public class RetentionDaoImpl implements RetentionDao {
  private static final String STATUS = "status";
  private static final String RECEPTION_DATE = "receptionDate";

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "findAndLock")
  public long findAndLock(Retention retention) {
    Criteria criteria = getCriteriaForQuery(retention, RetentionStatus.TO_PROCESS);

    Update updateDefinition = new Update().set(STATUS, RetentionStatus.LOCKED);
    UpdateResult result =
        this.template.updateMulti(new Query(criteria), updateDefinition, Retention.class);
    return result.getMatchedCount();
  }

  @Override
  @ContinueSpan(log = "findAllByDelai")
  public List<Retention> findAllByDelai(String idDeclarant, int delaiRetention) {
    LocalDateTime now = LocalDateTime.now();
    Criteria criteria = getCriteriaForQuery(idDeclarant, now.minusDays(delaiRetention));

    return this.template.find(new Query(criteria), Retention.class);
  }

  @Override
  @ContinueSpan(log = "createRetention")
  public void createRetention(Retention retention) {
    template.save(retention, Constants.RETENTION_COLLECTION);
  }

  @Override
  @ContinueSpan(log = "updateRetention")
  public void updateRetention(Retention retention, RetentionStatus status) {
    Criteria criteria = getCriteriaForQuery(retention, RetentionStatus.LOCKED);

    Update updateDefinition =
        new Update()
            .currentDate(RECEPTION_DATE)
            .set("currentEndDate", retention.getCurrentEndDate())
            .set(STATUS, status)
            .push("historique", retention.getHistorique().getLast());
    this.template.updateMulti(new Query(criteria), updateDefinition, Retention.class);
  }

  @Override
  @ContinueSpan(log = "findRetention")
  public Retention findRetention(
      String insurerId, String subscriberNumber, String contractNumber, String personNumber) {
    Criteria criteria =
        getCriteriaForQuery(
            insurerId, subscriberNumber, contractNumber, personNumber, RetentionStatus.TO_PROCESS);

    return this.template.findOne(new Query(criteria), Retention.class);
  }

  @Override
  public List<Retention> getAll() {
    return this.template.findAll(Retention.class);
  }

  @Override
  public void updateRetentionStatus(Retention retention, RetentionStatus status) {
    Criteria criteria = getCriteriaForQuery(retention, RetentionStatus.TO_PROCESS);

    Update updateDefinition = new Update().currentDate(RECEPTION_DATE).set(STATUS, status);
    this.template.updateFirst(new Query(criteria), updateDefinition, Retention.class);
  }

  @NotNull
  private static Criteria getCriteriaForQuery(Retention retention, RetentionStatus status) {
    return getCriteriaForQuery(
        retention.getInsurerId(),
        retention.getSubscriberNumber(),
        retention.getContractNumber(),
        retention.getPersonNumber(),
        status);
  }

  @NotNull
  private static Criteria getCriteriaForQuery(
      String insurerId,
      String subscriberNumber,
      String contractNumber,
      String personNumber,
      RetentionStatus status) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("insurerId").is(insurerId),
        Criteria.where("subscriberNumber").is(subscriberNumber),
        Criteria.where("contractNumber").is(contractNumber),
        Criteria.where("personNumber").is(personNumber),
        Criteria.where(STATUS).is(status));
    return criteria;
  }

  @NotNull
  private static Criteria getCriteriaForQuery(String idDeclarant, LocalDateTime date) {
    Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("insurerId").is(idDeclarant),
        Criteria.where(RECEPTION_DATE).lte(date),
        Criteria.where(STATUS).is(RetentionStatus.TO_PROCESS));
    return criteria;
  }
}
