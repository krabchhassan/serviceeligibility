package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Circuit;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Circuit} de la base de donnees. */
@Repository("circuitDao")
public class CircuitDaoImpl extends MongoGenericDao<Circuit> implements CircuitDao {

  public CircuitDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findAllCircuits")
  public List<Circuit> findAllCircuits() {

    // CRITERIA
    final Criteria criteria = new Criteria();

    // QUERY
    final Query query = new Query(criteria).with(Sort.by(Sort.Direction.ASC, "codeCircuit"));

    // RESULT
    return this.getMongoTemplate().find(query, Circuit.class);
  }
}
