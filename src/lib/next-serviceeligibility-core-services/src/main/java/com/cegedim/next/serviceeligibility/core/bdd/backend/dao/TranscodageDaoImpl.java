package com.cegedim.next.serviceeligibility.core.bdd.backend.dao;

import com.cegedim.next.serviceeligibility.core.dao.MongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** Classe d'accès aux {@code Transcodage} de la base de donnees. */
@Repository("transcodageDao")
public class TranscodageDaoImpl extends MongoGenericDao<Transcodage> implements TranscodageDao {

  private static final String CODE_OBJET_TRANSCO = "codeObjetTransco";
  private static final String CODE_SERVICE = "codeService";

  public TranscodageDaoImpl(MongoTemplate mongoTemplate) {
    super(mongoTemplate);
  }

  @Override
  @ContinueSpan(log = "findByCodeObjetTransco")
  public List<Transcodage> findByCodeObjetTransco(
      final String codeService, final String codeObjetTransco) {
    List<Transcodage> transcoList = new ArrayList<>();
    if (StringUtils.isNotBlank(codeService) && StringUtils.isNotBlank(codeObjetTransco)) {
      // CRITERIA
      final Criteria criteria =
          Criteria.where(CODE_SERVICE)
              .is(codeService.trim())
              .and(CODE_OBJET_TRANSCO)
              .is(codeObjetTransco.trim());

      // QUERY
      final Query query = Query.query(criteria);

      // RESULT
      transcoList = this.getMongoTemplate().find(query, Transcodage.class);
    }
    return transcoList;
  }

  @Override
  @ContinueSpan(log = "findCodeTranscoByCodeObjetTransco")
  public String findCodeTranscoByCodeObjetTransco(
      final String codeService, final String codeObjetTransco) {
    String codeTransco = null;
    if (StringUtils.isNotBlank(codeService) && StringUtils.isNotBlank(codeObjetTransco)) {
      // CRITERIA
      final Criteria criteria =
          Criteria.where(CODE_SERVICE)
              .is(codeService.trim())
              .and(CODE_OBJET_TRANSCO)
              .is(codeObjetTransco.trim());

      // QUERY
      final Query query = Query.query(criteria);

      // RESULT
      final Transcodage transco = this.getMongoTemplate().findOne(query, Transcodage.class);
      if (transco != null) {
        codeTransco = transco.getCodeTransco();
      }
    }
    return codeTransco;
  }
}
