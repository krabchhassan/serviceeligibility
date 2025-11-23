package com.cegedim.next.serviceeligibility.core.business.serviceprestation.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository("PrestIJDao")
public class PrestIJDaoImpl extends MongoGenericDao<PrestIJ> implements PrestIJDao {

  private static final String ID_DECLARANT = "oc.identifiant";

  public PrestIJDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findServicePrestIJ")
  public List<PrestIJ> findServicePrestIJ(final String idDeclarant) {

    /// CRITERIA
    final Criteria criteria = Criteria.where(ID_DECLARANT).is(idDeclarant);

    // QUERY
    final Query queryTrace = Query.query(criteria);

    return this.getMongoTemplate().find(queryTrace, PrestIJ.class);
  }
}
