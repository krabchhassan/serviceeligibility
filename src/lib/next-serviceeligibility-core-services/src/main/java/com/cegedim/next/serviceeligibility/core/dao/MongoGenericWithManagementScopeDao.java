package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.dao.managementscope.ManagementScopeDao;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.cegedim.next.serviceeligibility.core.services.scopeManagement.AuthorizationScopeHandler;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoGenericWithManagementScopeDao<T extends DocumentEntity> extends ManagementScopeDao
    implements IMongoGenericDao<T> {

  private final MongoTemplate mongoTemplate;

  public MongoGenericWithManagementScopeDao(
      MongoTemplate mongoTemplate, AuthorizationScopeHandler authorizationScopeHandler) {
    super(authorizationScopeHandler);
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  @ContinueSpan(log = "create mongo object")
  public void create(final T object) {
    this.mongoTemplate.insert(object);
  }

  @Override
  @ContinueSpan(log = "findById mongo object")
  public T findById(final String id, final Class<T> clazz) {
    return this.mongoTemplate.findById(id, clazz);
  }

  @Override
  @ContinueSpan(log = "findAll mongo objects")
  public List<T> findAll(final Class<T> clazz) {
    return this.mongoTemplate.findAll(clazz);
  }

  @Override
  @ContinueSpan(log = "update mongo object")
  public T update(final T document) {
    this.mongoTemplate.save(document);
    return document;
  }

  @Override
  @ContinueSpan(log = "delete mongo object")
  public void delete(final T document) {
    this.mongoTemplate.remove(document);
  }

  /**
   * @return the mongoTemplate
   */
  protected MongoTemplate getMongoTemplate() {
    return this.mongoTemplate;
  }

  @Override
  @ContinueSpan(log = "findByField mongo objects")
  public List<T> findByField(final String fieldName, final Object value, final Class<T> clazz) {
    final Query query = new Query();
    query.addCriteria(Criteria.where(fieldName).is(value));
    return this.getMongoTemplate().find(query, clazz);
  }

  @Override
  @ContinueSpan(log = "dropCollection")
  public void dropCollection(final Class<T> collectionType) {
    this.mongoTemplate.remove(new Query(), collectionType);
  }
}
