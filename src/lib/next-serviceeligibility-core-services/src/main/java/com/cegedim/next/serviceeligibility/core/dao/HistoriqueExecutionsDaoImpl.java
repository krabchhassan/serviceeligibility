package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecutions634;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("HistoriqueExecutions")
@RequiredArgsConstructor
public class HistoriqueExecutionsDaoImpl implements HistoriqueExecutionsDao {

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "getLastExecution")
  public <T extends HistoriqueExecutions<T>> T getLastExecution(String batch, Class<T> tClass) {
    return getLastExecution(batch, null, tClass);
  }

  @Override
  @ContinueSpan(log = "getLastExecution")
  public <T extends HistoriqueExecutions<T>> T getLastExecution(
      String batch, String idDeclarant, Class<T> tClass) {
    Query q = new Query().addCriteria(Criteria.where(Constants.BATCH).is(batch));
    if (StringUtils.isNotBlank(idDeclarant)) {
      q.addCriteria(Criteria.where(Constants.ID_DECLARANT).is(idDeclarant));
    }
    q.with(Sort.by(Sort.Direction.DESC, Constants.ID));
    return template.findOne(q, tClass);
  }

  @Override
  @ContinueSpan(log = "getLastExecution")
  public <T extends HistoriqueExecutions<T>> T getLastExecutionForReprise620(
      String batch, String idDeclarant, Class<T> tClass) {
    Query q = new Query().addCriteria(Criteria.where(Constants.BATCH).is(batch));
    q.addCriteria(Criteria.where("nbDeclarationLue").exists(true));
    if (StringUtils.isNotBlank(idDeclarant)) {
      q.addCriteria(Criteria.where(Constants.ID_DECLARANT).is(idDeclarant));
    }
    q.with(Sort.by(Sort.Direction.DESC, Constants.ID));
    return template.findOne(q, tClass);
  }

  @Override
  @ContinueSpan(log = "saveWithSession historique execution")
  public <T extends HistoriqueExecutions<T>> T saveWithSession(
      T historiqueExecutions, ClientSession session) {
    return template.withSession(session).save(historiqueExecutions);
  }

  @Override
  @ContinueSpan(log = "save historique execution")
  public <T extends HistoriqueExecutions<T>> T save(T historiqueExecutions) {
    return template.save(historiqueExecutions);
  }

  @Override
  @ContinueSpan(log = "deleteAll historique execution")
  public void deleteAll() {
    template.remove(HistoriqueExecutions634.class);
  }

  @Override
  @ContinueSpan(log = "deleteByAMC")
  public long deleteByAMC(String amc) {
    Criteria criteria = Criteria.where(Constants.ID_DECLARANT).is(amc);
    return template.remove(new Query(criteria), HistoriqueExecution.class).getDeletedCount();
  }

  @Override
  public <T extends HistoriqueExecutions<T>> T getLastExecution(
      Criteria criteria, Class<T> tClass) {
    Query query = new Query(criteria).with(Sort.by(Sort.Direction.DESC, Constants.ID));
    return template.findOne(query, tClass);
  }
}
