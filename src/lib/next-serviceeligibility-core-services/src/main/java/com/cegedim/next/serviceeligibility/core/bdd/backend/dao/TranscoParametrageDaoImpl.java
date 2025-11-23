package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.TranscoParametrage;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code TranscoParametrage} de la base de donnees. */
@Repository("transcoParametrageDao")
public class TranscoParametrageDaoImpl extends MongoGenericDao<TranscoParametrage>
    implements TranscoParametrageDao {

  public TranscoParametrageDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findTranscoParametrage")
  public TranscoParametrage findTranscoParametrage(final String codeObjetTransco) {

    if (StringUtils.isBlank(codeObjetTransco)) {
      return null;
    }

    // CRITERIA
    final Criteria criteria = Criteria.where("codeObjetTransco").is(codeObjetTransco.trim());

    // QUERY
    final Query query = Query.query(criteria);

    // RESULT
    return this.getMongoTemplate().findOne(query, TranscoParametrage.class);
  }
}
