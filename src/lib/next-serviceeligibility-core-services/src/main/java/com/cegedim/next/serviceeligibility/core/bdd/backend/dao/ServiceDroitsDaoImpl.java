package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServiceDroits;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code ServiceDroits} de la base de donnees. */
@Repository("serviceDroitsDao")
public class ServiceDroitsDaoImpl extends MongoGenericDao<ServiceDroits>
    implements ServiceDroitsDao {

  public ServiceDroitsDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findCodesService ServiceDroits")
  public List<ServiceDroits> findCodesService() {
    // CRITERIA
    final Criteria criteria = Criteria.where("serviceFictif").is(false);

    // QUERY
    final Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "triRestitution"));

    return this.getMongoTemplate().find(query, ServiceDroits.class);
  }

  @Override
  @ContinueSpan(log = "findServicesWithTransco ServiceDroits")
  public List<ServiceDroits> findServicesWithTransco() {
    // CRITERIA
    final Criteria criteria = Criteria.where("transco").not().size(0);

    // QUERY
    final Query query = Query.query(criteria);

    return this.getMongoTemplate().find(query, ServiceDroits.class);
  }

  @Override
  @ContinueSpan(log = "findOneByCodeAndTransco ServiceDroits")
  public ServiceDroits findOneByCodeAndTransco(
      final String serviceCode, final String transcodingCode) {
    final Criteria criteria = Criteria.where("code").is(serviceCode);
    criteria.and("transco").all(transcodingCode);

    // Query
    final Query query = Query.query(criteria);

    return this.getMongoTemplate().findOne(query, ServiceDroits.class);
  }

  @Override
  @ContinueSpan(log = "findOneByCode ServiceDroits")
  public ServiceDroits findOneByCode(final String codeService) {
    final Criteria criteria = Criteria.where("code").is(codeService);

    // Query
    final Query query = Query.query(criteria);

    return this.getMongoTemplate().findOne(query, ServiceDroits.class);
  }
}
