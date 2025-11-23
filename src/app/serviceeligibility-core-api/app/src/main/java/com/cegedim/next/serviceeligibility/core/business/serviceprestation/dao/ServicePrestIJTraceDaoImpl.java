package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestIJTrace;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("servicePrestIJTraceDao")
public class ServicePrestIJTraceDaoImpl extends MongoGenericDao<ServicePrestIJTrace>
    implements ServicePrestIJTraceDao {

  public ServicePrestIJTraceDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findServicePrestIJTrace")
  public List<ServicePrestIJTrace> findServicePrestIJTrace(final String id) {

    /// CRITERIA
    final Criteria criteria = Criteria.where("contratAiId").is(id);

    // QUERY
    final Query queryTrace = Query.query(criteria);

    return this.getMongoTemplate().find(queryTrace, ServicePrestIJTrace.class);
  }
}
