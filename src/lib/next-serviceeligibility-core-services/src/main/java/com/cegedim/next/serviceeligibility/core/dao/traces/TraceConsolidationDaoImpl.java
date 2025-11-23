package com.cegedim.next.serviceeligibility.core.dao.traces;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("traceConsolidationDaoImpl")
public class TraceConsolidationDaoImpl extends MongoGenericDao<TraceConsolidation>
    implements TraceConsolidationDao {

  public TraceConsolidationDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public void insertAll(List<TraceConsolidation> traceConsos) {
    BulkOperations bulkInsertion =
        getMongoTemplate()
            .bulkOps(
                BulkOperations.BulkMode.UNORDERED,
                TraceConsolidation.class,
                Constants.TRACE_CONSOLIDATION_COLLECTION);
    bulkInsertion.insert(traceConsos);
    bulkInsertion.execute();
  }

  public void saveAll(List<TraceConsolidation> traceConsos) {
    // TODO upsert
    for (TraceConsolidation traceConsolidation : traceConsos) {
      getMongoTemplate().save(traceConsolidation);
    }
  }

  public List<TraceConsolidation> getAll() {
    return getMongoTemplate().findAll(TraceConsolidation.class);
  }

  @Override
  public TraceConsolidation getByServiceAndIdDeclaration(String idDeclaration, String codeService) {
    final Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("idDeclaration").is(idDeclaration).and("codeService").is(codeService));
    return getMongoTemplate()
        .findOne(
            new Query(criteria),
            TraceConsolidation.class,
            Constants.TRACE_CONSOLIDATION_COLLECTION);
  }

  public void deleteAll() {
    getMongoTemplate().remove(new Query(), TraceConsolidation.class);
  }
}
