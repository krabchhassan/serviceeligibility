package com.cegedim.next.serviceeligibility.core.dao.traces;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.ClientSession;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository("traceExtractionConsoDaoImpl")
public class TraceExtractionConsoDaoImpl extends MongoGenericDao<TraceExtractionConso>
    implements TraceExtractionConsoDao {

  public TraceExtractionConsoDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  public TraceExtractionConso save(
      TraceExtractionConso traceExtractionConso, ClientSession session) {
    if (session != null) {
      return getMongoTemplate().withSession(session).save(traceExtractionConso);
    } else {
      return getMongoTemplate().save(traceExtractionConso);
    }
  }

  @Override
  public void saveAll(List<TraceExtractionConso> traceExtractionConsos, ClientSession session) {
    BulkOperations bulkInsertion =
        getMongoTemplate().bulkOps(BulkOperations.BulkMode.UNORDERED, TraceExtractionConso.class);
    bulkInsertion.insert(traceExtractionConsos);
    bulkInsertion.execute();
  }

  public List<TraceExtractionConso> getAll() {
    return getMongoTemplate().findAll(TraceExtractionConso.class);
  }

  public void deleteAll() {
    getMongoTemplate().remove(new Query(), TraceExtractionConso.class);
  }

  @Override
  public void insertAll(List<TraceExtractionConso> traceConsos) {
    getMongoTemplate().insertAll(traceConsos);
  }

  @Override
  public List<TraceExtractionConso> getByServiceAndIdDeclaration(
      String idDeclarationConsolidee, String codeService) {
    final Criteria criteria = new Criteria();
    criteria.andOperator(
        Criteria.where("idDeclaration")
            .is(idDeclarationConsolidee)
            .and("codeService")
            .is(codeService));
    return getMongoTemplate()
        .find(
            new Query(criteria),
            TraceExtractionConso.class,
            Constants.TRACE_EXTRA_CONSO_COLLECTION);
  }

  @Override
  public long replaceFileName(List<String> oldNames, String newName) {
    Criteria criteria = Criteria.where("nomFichier").in(oldNames);

    Update updateDefinition = new Update().set("nomFichier", newName);
    UpdateResult result =
        getMongoTemplate()
            .updateMulti(new Query(criteria), updateDefinition, TraceExtractionConso.class);
    return result.getMatchedCount();
  }
}
