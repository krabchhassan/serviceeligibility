package com.cegedim.next.serviceeligibility.core.dao.traces;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo.TraceServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.mongodb.BasicDBObject;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository("traceDaoImpl")
public class TraceDaoImpl implements TraceDao {

  private final MongoTemplate template;

  @Override
  @ContinueSpan(log = "findOneBy Trace")
  public Trace findOneBy(final String id, final String collection) {

    if (StringUtils.isEmpty(id)) {
      throw new IllegalArgumentException("This method required an id");
    }
    Criteria criteria = Criteria.where("id").is(id);

    return template.findOne(new Query(criteria), Trace.class, collection);
  }

  @Override
  @ContinueSpan(log = "deleteServicePrestationTracesByFileName")
  public long deleteServicePrestationTracesByFileName(final String fileName) {
    if (StringUtils.isEmpty(fileName)) {
      throw new IllegalArgumentException("This method required a fileName");
    }

    Criteria criteria = Criteria.where("fileName").is(fileName);

    return template.remove(new Query(criteria), Trace.class, CONTRACT_TRACE).getDeletedCount();
  }

  @ContinueSpan(log = "deleteServicePrestationTracesByAmc")
  public long deleteServicePrestationTracesByAmc(final String idDeclarant) {
    Criteria criteria = getCriteriaDeleteByAmc(idDeclarant);

    return template.remove(new Query(criteria), Trace.class, CONTRACT_TRACE).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteBeneficiaryTracesByOriginId")
  public long deleteBeneficiaryTracesByOriginId(final String originId) {
    if (StringUtils.isEmpty(originId)) {
      throw new IllegalArgumentException("This method required an originId");
    }

    Criteria criteria = Criteria.where("originId").is(originId);

    return template.remove(new Query(criteria), Trace.class, BENEF_TRACE).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteBeneficiaryTracesByAmc")
  public long deleteBeneficiaryTracesByAmc(final String idDeclarant) {
    Criteria criteria = getCriteriaDeleteByAmc(idDeclarant);

    return template.remove(new Query(criteria), Trace.class, BENEF_TRACE).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteDeclarationTracesByAmc")
  public long deleteDeclarationTracesByAmc(final String idDeclarant) {
    Criteria criteria = getCriteriaDeleteByAmc(idDeclarant);

    return template.remove(new Query(criteria), Trace.class, DECLARATION_TRACE).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteDeclarationTracesByOriginId")
  public long deleteDeclarationTracesByOriginId(final String originId) {
    if (StringUtils.isEmpty(originId)) {
      throw new IllegalArgumentException("This method required an originId");
    }

    Criteria criteria = Criteria.where("originId").is(originId);

    return template.remove(new Query(criteria), Trace.class, DECLARATION_TRACE).getDeletedCount();
  }

  @Override
  @ContinueSpan(log = "deleteInsuredDataTracesByAmc")
  public long deleteInsuredDataTracesByAmc(final String idDeclarant) {
    Criteria criteria = getCriteriaDeleteByAmc(idDeclarant);

    return template.remove(new Query(criteria), Trace.class, INSURED_DATA_TRACE).getDeletedCount();
  }

  private Criteria getCriteriaDeleteByAmc(String idDeclarant) {
    if (StringUtils.isEmpty(idDeclarant)) {
      throw new IllegalArgumentException("This method required an idDeclarant");
    }

    return Criteria.where(ID_DECLARANT).is(idDeclarant);
  }

  @Override
  @ContinueSpan(log = "getTraceForArl")
  public Page<TraceServicePrestation> getTraceForArl(
      String fileName, String firstTraceId, Pageable pageable) {
    Criteria crit =
        new Criteria()
            .andOperator(
                Criteria.where("status").ne("SuccesfullyProcessed"),
                Criteria.where("fileName").is(fileName));
    if (firstTraceId != null) {
      crit = crit.and("_id").gte(new ObjectId(firstTraceId));
    }

    Query q = new Query(crit).with(pageable);
    List<TraceServicePrestation> traces =
        template.find(q, TraceServicePrestation.class, CONTRACT_TRACE);
    return PageableExecutionUtils.getPage(
        traces, pageable, () -> template.count(q, TraceServicePrestation.class));
  }

  @Override
  @ContinueSpan(log = "deleteTracesByAMC")
  public long deleteTracesByAMC(String amc) {
    Criteria criteria = getCriteriaDeleteByAmc(amc);
    return template.remove(Query.query(criteria), Trace.class).getDeletedCount();
  }

  @Override
  public long deleteTracesByFileName(String file) {
    Criteria criteria = Criteria.where(NOM_FICHIER_ORIGINE).is(file);
    return template.remove(Query.query(criteria), Trace.class).getDeletedCount();
  }

  @Override
  public Trace save(Trace trace, String collection) {
    return template.save(trace, collection);
  }

  @Override
  public void updateFirst(Query query, UpdateDefinition update, String collection) {
    template.updateFirst(query, update, collection);
  }

  @Override
  public void updateById(String id, BasicDBObject update, String collection) {
    template
        .getCollection(collection)
        .updateOne(new BasicDBObject("_id", new ObjectId(id)), new BasicDBObject("$set", update));
  }
}
