package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.ServicePrestationMongo;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("servicePrestationMongoDao")
public class ServicePrestationMongoDaoImpl extends MongoGenericDao<ServicePrestationMongo>
    implements ServicePrestationMongoDao {

  private static final String ID_DECLARANT = "idDeclarant";

  public ServicePrestationMongoDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findServicePrestationMongo")
  public List<ServicePrestationMongo> findServicePrestationMongo(final String idDeclarant) {
    /// CRITERIA
    final Criteria criteria = Criteria.where(ID_DECLARANT).is(idDeclarant);

    // QUERY
    final Query queryTrace = Query.query(criteria);

    return this.getMongoTemplate().find(queryTrace, ServicePrestationMongo.class);
  }
}
