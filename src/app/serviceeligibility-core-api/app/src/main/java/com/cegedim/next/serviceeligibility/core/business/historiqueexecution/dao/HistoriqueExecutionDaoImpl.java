package com.cegedim.next.serviceeligibility.core.business.historiqueexecution.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code HistoriqueExecution} de la base de donnees. */
@Repository("historiqueExecutionDao")
public class HistoriqueExecutionDaoImpl extends MongoGenericDao<HistoriqueExecution>
    implements HistoriqueExecutionDao {

  public HistoriqueExecutionDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  private Query getQuery(
      final String batch,
      final String idDeclarant,
      String codeService,
      LocalDateTime dateExecution) {
    // CRITERIA
    final Criteria criteria = Criteria.where(Constants.BATCH).is(batch);

    if (!StringUtils.isEmpty(idDeclarant)) {
      criteria.and("idDeclarant").is(idDeclarant);
    }
    if (!StringUtils.isEmpty(codeService)) {
      criteria.and("codeService").is(codeService);
    }
    if (dateExecution != null) {
      criteria.and("dateExecution").gte(dateExecution);
    }
    // QUERY
    return Query.query(criteria);
  }

  @Override
  @ContinueSpan(log = "findById HistoriqueExecution")
  public HistoriqueExecution findById(final String id) {
    return this.findById(id, HistoriqueExecution.class);
  }

  @Override
  @ContinueSpan(log = "findByBatchIdDeclarant HistoriqueExecution")
  public HistoriqueExecution findLastByBatchIdDeclarant(
      final String batch, final String idDeclarant, String codeService) {
    final Query queryHistoExec =
        this.getQuery(batch, idDeclarant, codeService, null)
            .with(Sort.by(Sort.Direction.DESC, "dateExecution"))
            .with(Sort.by(Sort.Direction.DESC, "_id"));
    return this.getMongoTemplate().findOne(queryHistoExec, HistoriqueExecution.class);
  }

  @Override
  @ContinueSpan(log = "deleteByBatchDateExecution HistoriqueExecution")
  public void deleteByBatchDateExecution(
      String batch, String codeService, LocalDateTime dateExecution) {
    Query queryHistoExec = getQuery(batch, null, codeService, dateExecution);
    List<HistoriqueExecution> deleteHistoriqueExecutionList =
        getMongoTemplate().find(queryHistoExec, HistoriqueExecution.class);
    for (HistoriqueExecution historiqueExecution : deleteHistoriqueExecutionList) {
      getMongoTemplate().remove(historiqueExecution);
    }
  }

  @Override
  @ContinueSpan(log = "add HistoriqueExecution")
  public void add(final HistoriqueExecution historiqueExecution) {
    this.getMongoTemplate().save(historiqueExecution);
  }

  @Override
  @ContinueSpan(log = "deleteAll HistoriqueExecution")
  public void deleteAll() {
    this.getMongoTemplate().remove(new Query(), HistoriqueExecution.class);
  }
}
