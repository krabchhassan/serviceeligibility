package com.cegedim.next.serviceeligibility.almerys608.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public interface CommonDao extends IMongoGenericDao<DocumentEntity> {
  <T extends BulkObject> void insertAll(List<T> toInsert, Class<?> clazz, String collectionName);

  <T> Stream<T> getAllStream(String collectionName, Class<T> objectClass);

  <T> Stream<T> getAggregationStream(
      Aggregation aggregation, String collectionName, Class<T> objectClass);

  <T> Collection<T> getAggregation(
      Aggregation aggregation, String collectionName, Class<T> objectClass);

  <T> List<T> getAll(String collectionName, Class<T> objectClass);

  void clearTempTable(Map<String, String> tempTables);

  <T> T get(Class<T> tempClass, String collectionName, String numeroContrat);

  <T> T queryOne(Class<T> tClass, String collection, Criteria criteria);
}
