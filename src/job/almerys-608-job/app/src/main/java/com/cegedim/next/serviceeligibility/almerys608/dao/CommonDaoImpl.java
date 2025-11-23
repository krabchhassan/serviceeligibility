package com.cegedim.next.serviceeligibility.almerys608.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class CommonDaoImpl extends MongoGenericDao<DocumentEntity> implements CommonDao {
  public CommonDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  public <T extends BulkObject> void insertAll(
      List<T> toInsert, Class<?> clazz, String collectionName) {
    BulkOperations bulkInsertion =
        getMongoTemplate().bulkOps(BulkOperations.BulkMode.UNORDERED, clazz, collectionName);
    toInsert.forEach(bulk -> bulk.bulk(bulkInsertion));
    bulkInsertion.execute();
  }

  @Override
  public <T> Stream<T> getAllStream(String collectionName, Class<T> objectClass) {
    return getMongoTemplate().stream(new Query(), objectClass, collectionName);
  }

  @Override
  public <T> Stream<T> getAggregationStream(
      Aggregation aggregation, String collectionName, Class<T> objectClass) {
    return getMongoTemplate().aggregateStream(aggregation, collectionName, objectClass);
  }

  @Override
  public <T> Collection<T> getAggregation(
      Aggregation aggregation, String collectionName, Class<T> objectClass) {
    return getMongoTemplate()
        .aggregate(aggregation, collectionName, objectClass)
        .getMappedResults();
  }

  @Override
  public <T> List<T> getAll(String collectionName, Class<T> objectClass) {
    return getMongoTemplate().findAll(objectClass, collectionName);
  }

  @Override
  public void clearTempTable(Map<String, String> tempTables) {
    for (Map.Entry<String, String> entry : tempTables.entrySet()) {
      getMongoTemplate().dropCollection(entry.getValue());
    }
  }

  @Override
  public <T> T get(Class<T> tempClass, String collectionName, String numeroContrat) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.NUMERO_CONTRAT).is(numeroContrat));
    List<T> objectList = getMongoTemplate().find(query, tempClass, collectionName);
    if (!CollectionUtils.isEmpty(objectList)) {
      return objectList.getFirst();
    }
    return null;
  }

  @Override
  public <T> T queryOne(Class<T> tClass, String collection, Criteria criteria) {
    return getMongoTemplate().findOne(new Query(criteria), tClass, collection);
  }
}
