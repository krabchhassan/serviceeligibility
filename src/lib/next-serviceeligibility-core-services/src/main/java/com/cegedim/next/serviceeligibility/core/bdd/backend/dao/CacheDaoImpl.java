package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Cache;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Cache} de la base de donnees. */
@Repository("cacheDao")
public class CacheDaoImpl extends MongoGenericDao<Cache> implements CacheDao {

  public CacheDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findByName Cache")
  public Cache findByName(final String cacheName) {
    final Criteria criteria = Criteria.where("cacheName").is(cacheName);
    final Query query = new Query(criteria);
    return this.getMongoTemplate().findOne(query, Cache.class);
  }
}
