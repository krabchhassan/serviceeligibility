package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationTrace;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("servicePrestationTraceDao")
public class ServicePrestationTraceDaoImpl extends MongoGenericDao<ServicePrestationTrace>
    implements ServicePrestationTraceDao {

  public ServicePrestationTraceDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findServicePrestationTrace")
  public List<ServicePrestationTrace> findServicePrestationTrace(final String id) {

    /// CRITERIA
    final Criteria criteria = Criteria.where("contratAiId").is(id);

    // QUERY
    final Query queryTrace = Query.query(criteria);

    return this.getMongoTemplate().find(queryTrace, ServicePrestationTrace.class);
  }

  @Override
  @ContinueSpan(log = "getAll")
  public List<ServicePrestationTrace> getAll() {
    return this.getMongoTemplate().findAll(ServicePrestationTrace.class);
  }
}
