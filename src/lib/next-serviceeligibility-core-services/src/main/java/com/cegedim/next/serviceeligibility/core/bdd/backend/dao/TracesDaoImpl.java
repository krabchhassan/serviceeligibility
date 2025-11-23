package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Traces;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Traces} de la base de donnees. */
@Repository("tracesDao")
public class TracesDaoImpl extends MongoGenericDao<Traces> implements TracesDao {

  public TracesDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findByIdDeclaration Traces")
  public Traces findByIdDeclaration(final String idDeclaration) {
    final Criteria criteria = Criteria.where("idDeclaration").is(idDeclaration);
    final Query query = new Query(criteria);
    return this.getMongoTemplate().findOne(query, Traces.class);
  }
}
